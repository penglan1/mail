package pers.penglan.mail.controller.processor;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.penglan.mail.context.MailContextOperator;
import pers.penglan.mail.controller.session.UserSessionOperator;
import pers.penglan.mail.mapper.user.UserMapper;
import pers.penglan.mail.mapper.user.mail.AccountPropertyMapper;
import pers.penglan.mail.mapper.user.mail.MailAccountMapper;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;
import pers.penglan.mail.model.MessageSource;
import pers.penglan.mail.model.user.User;
import pers.penglan.mail.model.user.UserGlobalInfo;
import pers.penglan.mail.model.user.mail.*;
import pers.penglan.mail.service.info.InfoProcessor;
import pers.penglan.mail.utils.date.DateUtility;
import pers.penglan.mail.utils.exception.ExceptionUtility;
import pers.penglan.mail.utils.id.UIDCenter;
import pers.penglan.mail.utils.message.MessageUtility;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Author PENGL
 * 2020-03-27
 */
@Controller
@RequestMapping(path = "/info", method = RequestMethod.POST)
public class InfoProcessorController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailAccountMapper mailAccountMapper;

    @Autowired
    private AccountPropertyMapper accountPropertyMapper;

    @Autowired
    private Logger logger;

    @Autowired
    private InfoProcessor infoProcessor;

    /**
     * 用户登入认证
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(path = "/check/login")
    @ResponseBody
    public Object checkUser(@RequestBody User user, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        /*检测是否二次登入*/
        HttpSession session = request.getSession(false);
        if (session != null && UserSessionOperator.getUserGlobalInfo(session) != null) {
            map.put("message", "当前会话有效，无需二次登入");
            map.put("status", "success");
            map.put("targetUrl", "/mail/main");
            return map;
        }

        String userID = user.getPk_UserID();
        String password = user.getPassword();
        /*从数据库中查询处该用户的信息*/
        User dbUser = userMapper.selectByUserIdOrMailAccount(userID);

        /*核对信息*/
        if (dbUser != null && dbUser.getPassword().equals(password)) {
            UserGlobalInfo userGlobalInfo = createUserGlobalInfo(dbUser, request);
            /*将userGlobalInfo放入到session中*/
            session = request.getSession(true);
            /*设置会话最大有效无操作间隔期 20 分钟*/
            session.setMaxInactiveInterval(60 * 20);
            UserSessionOperator.setUserGlobalInfo(session, userGlobalInfo);
            map.put("message", "用户验证成功");
            map.put("status", "success");
            map.put("targetUrl", "/mail/main");
            return map;
        } else if (dbUser != null){
            map.put("message", "密码错误, 请重新输入");
            map.put("status", "failure");
        } else {
            map.put("message", "该用户不存在");
            map.put("status", "failure");
        }

        return map;
    }

    /**
     * 审核用户信息，进行用户注册
     * @param registerTO
     * @param request
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public Object registerUser(@RequestBody RegisterTO registerTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        /*检查是否已经发送了验证码*/
        RegisterTO sentRegisterTO = UserSessionOperator.getRegisterTO(request.getSession());
        if (sentRegisterTO == null) {
            map.put("status", "failure");
            map.put("message", "请重新注册！");
            return map;
        }
        if (sentRegisterTO.getVerification().equals(registerTO.getVerification())) {
            /*验证邮箱账号是否前后一致*/
            if (!sentRegisterTO.getUser().getBandingMailAccount().equals(registerTO.getUser().getBandingMailAccount())) {
                map.put("status", "failure");
                map.put("messaeg", "绑定的邮箱账号前后不一致，请重新绑定！");
            }

            try {
                /*更新某些字段信息*/
                User sentUser = sentRegisterTO.getUser();
                sentUser.setNickname(registerTO.getUser().getNickname());
                sentUser.setPassword(registerTO.getUser().getPassword());
                /*完善用户对象属性*/
                sentUser.setRegisteredTime(DateUtility.getCurrentDate());
                sentUser.setPk_UserID(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
                /*添加用户*/
                infoProcessor.addUser(sentUser);

                request.getSession().invalidate();
                map.put("status", "success");
                map.put("message", "用户注册成功");
                map.put("targetUrl", "/mail/main");
            } catch (Exception e) {
                logger.error(ExceptionUtility.getTrace(e));
                map.put("status", "failure");
                map.put("message", "用户添加失败");
            }
        } else {
            map.put("status", "failure");
            map.put("message", "验证码输入错误，请重新输入！");
        }

        return map;
    }

    /**
     * 发送验证码
     * <p>向注册用户绑定的邮箱发送验证码邮件，同时将验证码加入到{@link RegisterTO}对象中，
     * 并将该对象放入到当前注册的使用的session中，session失效时间为5分钟</p>
     * @param registerTO
     * @param request
     * @return
     */
    @RequestMapping("/get/verification")
    @ResponseBody
    public Object sendVerification(@RequestBody RegisterTO registerTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        /*检查是否已经发送了验证码*/
        RegisterTO sentRegisterTO = UserSessionOperator.getRegisterTO(request.getSession());
        if (sentRegisterTO != null) {
            map.put("status", "failure");
            map.put("message", "验证码已发送，请勿重复提交");
            return map;
        }
        /*检查该邮箱账号是否已经被绑定了*/
        User user = userMapper.selectByUserIdOrMailAccount(registerTO.getUser().getBandingMailAccount());
        if (user != null) {
            map.put("status", "failure");
            map.put("message", "该邮箱账号已绑定其它账号！");
            return map;
        }

        MailAccountCenter systemMailAccountCenter = MailContextOperator.getSystemMailAccountCenter(request.getServletContext(),
                mailAccountMapper, accountPropertyMapper);

        String bandingMailAccount = registerTO.getUser().getBandingMailAccount();
        MailEntityVO mailEntity = new MailEntityVO();
        String verificationCode = Long.toString(UIDCenter.UID_GENERATOR.nextId());
        verificationCode = verificationCode.substring(verificationCode.length() - 6);
        /*将验证码加入到注册对象中*/
        registerTO.setVerification(verificationCode);
        /*创建邮件内容*/
        mailEntity.setSubject("PL_Mail邮箱验证请求");
        mailEntity.setContent("<p>欢迎使用PL_Mail邮件管理服务，验证码:<strong style='color: red;'>" + verificationCode + "</strong><br/>" +
                "如非本人操作，请删除该邮件! 验证码有效时间为5分钟！</p>");
        mailEntity.setFrom(systemMailAccountCenter.getOwner());
        mailEntity.setTo(bandingMailAccount);
        mailEntity.setContentType("text/html;charset=UTF-8");

        try {
            /*发送邮件，这里单独发送邮件，不使用系统的接口来发送*/
            MessageSource messageSource = new MessageSource(mailEntity, new ArrayList<>());
            Message message = MessageUtility.createMessage(systemMailAccountCenter.getSession(), messageSource);
            Transport transport = systemMailAccountCenter.getTransportWithConnected();
            transport.sendMessage(message, message.getAllRecipients());

            /*将注册对象放入到session中，设置session的失效时间为5分钟*/
            HttpSession session = request.getSession();
            /*失效时间为5分钟*/
            session.setMaxInactiveInterval(5 * 60);
            //registerTO.getUser().setRegisteredTime(DateUtility.getCurrentDate());
            UserSessionOperator.addRegisterTO(session, registerTO);

            map.put("status", "success");
            map.put("message", "验证码已发送，请注意查收!");
        } catch (Exception e) {
           logger.error(ExceptionUtility.getTrace(e));
           map.put("status", "failure");
           map.put("message", "邮件发送失败，请检查该邮箱账号是否有效");
        }


        return map;
    }

    /**
     * 用户注销
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(path = "/check/logout")
    @ResponseBody
    public Object logout(@RequestBody User user, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        String userID = user.getPk_UserID();
        /*从数据库中查询处该用户的信息*/
        /*核对信息*/
        HttpSession session = request.getSession(false);
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(session);
        String sessionUserID = userGlobalInfo.getUserID();

        if (sessionUserID.equals(userID)) {
            session.invalidate();
            map.put("message", "用户已安全退出");
            map.put("status", "success");
            map.put("targetUrl", "/mail/");
        } else {
            map.put("message", "用户不匹配，无权退出");
            map.put("status", "failure");
        }

        return map;
    }

    /**
     * 添加新的邮箱账号信息
     *
     * @param clientMailAccountTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/mailAccount")
    @ResponseBody
    public Object addMailAccountCenter(@RequestBody ClientMailAccountTO clientMailAccountTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        MailAccount mailAccount = clientMailAccountTO.getMailAccount();
        AccountProperty[] accountProperties = clientMailAccountTO.getAccountProperties();

        /*检验用户是否ID被修改*/
        if (!checkUserID(request, mailAccount.getPk_UserID())) {
            map.put("status", "failure");
            map.put("message", "用户ID与登入用户ID不一致");
            return map;
        }
        /*检验待修改的邮箱账号是否存在*/
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        Map<String, MailAccountCenter> mailAccountCenterMap = userGlobalInfo.getAccountMap();
        if (mailAccountCenterMap.containsKey(mailAccount.getAccount())) {
            map.put("status", "failure");
            map.put("message", "该账号已存在");
            return map;
        }

        /*测试连接*/
        Map<String, String> testMap = (Map<String, String>) testConnection(clientMailAccountTO, request);
        if (testMap.containsValue("failure")) {
            return testMap;
        }

        /*账号的收发协议均测试有效，则保存到数据库中*/
        /*设置键*/
        mailAccount.setPk_MailAccount(Long.toString(UIDCenter.UID_GENERATOR.nextId()));

        accountProperties[0].setPk_AccountProperty(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        accountProperties[0].setPk_MailAccount(mailAccount.getPk_MailAccount());
        accountProperties[0].setAuth("true");
        accountProperties[1].setPk_AccountProperty(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        accountProperties[1].setPk_MailAccount(mailAccount.getPk_MailAccount());
        accountProperties[1].setAuth("true");

        try {
            infoProcessor.addAccountTO(mailAccount, accountProperties);
            /*将mailAccountCenter添加到用户全局信息中*/
            MailAccountCenter mailAccountCenter = createMailAccountCenter(mailAccount, Arrays.asList(accountProperties));
            mailAccountCenterMap.put(mailAccountCenter.getOwner(), mailAccountCenter);
            map.put("status", "success");
            map.put("message", "账号已成功添加");
            map.put("accountTO", clientMailAccountTO);
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "账号添加失败");
        }

        return map;
    }

    /**
     * 删除邮箱账号
     *
     * @param clientMailAccountTO
     * @param request
     * @return
     */
    @RequestMapping("/delete/mailAccount")
    @ResponseBody
    public Object deleteMailAccountCenter(@RequestBody ClientMailAccountTO clientMailAccountTO, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        MailAccount mailAccount = clientMailAccountTO.getMailAccount();
        AccountProperty[] accountProperties = clientMailAccountTO.getAccountProperties();

        /*检验用户是否ID被修改*/
        if (!checkUserID(request, mailAccount.getPk_UserID())) {
            map.put("status", "failure");
            map.put("message", "用户ID与登入用户ID不一致");
            return map;
        }
        /*检验待修改的邮箱账号是否存在*/
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        Map<String, MailAccountCenter> mailAccountCenterMap = userGlobalInfo.getAccountMap();
        if (!mailAccountCenterMap.containsKey(mailAccount.getAccount())) {
            map.put("status", "failure");
            map.put("message", "该账号不存在");
            return map;
        }
        try {
            /*删除该账号*/
            infoProcessor.deleteAccountTO(mailAccount, accountProperties);
            map.put("status", "success");
            map.put("message", "账号已成功移除");
            /*将mailAccountCenter从用户全局信息中移除*/
            mailAccountCenterMap.remove(mailAccount);
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "账号删除失败");
        }


        return map;
    }

    /**
     * 邮箱账号信息更新
     *
     * @param clientMailAccountTO
     * @param request
     * @return
     */
    @RequestMapping("/update/mailAccount")
    @ResponseBody
    public Object updateMailAccountCenter(@RequestBody ClientMailAccountTO clientMailAccountTO,
                                          HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        MailAccount mailAccount = clientMailAccountTO.getMailAccount();
        AccountProperty[] accountProperties = clientMailAccountTO.getAccountProperties();

        /*检验用户是否ID被修改*/
        if (!checkUserID(request, mailAccount.getPk_UserID())) {
            map.put("status", "failure");
            map.put("message", "用户ID与登入用户ID不一致");
            return map;
        }
        /*检验待修改的邮箱账号是否存在*/
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        Map<String, MailAccountCenter> mailAccountCenterMap = userGlobalInfo.getAccountMap();
        if (!mailAccountCenterMap.containsKey(mailAccount.getAccount())) {
            map.put("status", "failure");
            map.put("message", "该账号已存在");
            return map;
        }

        /*测试连接*/
        Map<String, String> testMap = (Map<String, String>) testConnection(clientMailAccountTO, request);
        if (testMap.containsValue("failure")) {
            return testMap;
        }
        try {
            infoProcessor.updateAccountTO(mailAccount, accountProperties);
            map.put("status", "success");
            map.put("message", "账号更新成功");
            MailAccountCenter mailAccountCenter = createMailAccountCenter(mailAccount, Arrays.asList(accountProperties));
            /*更新用户全局信息中mailAccountCenter*/
            mailAccountCenterMap.replace(mailAccountCenter.getOwner(), mailAccountCenter);
        } catch (Exception e) {
            logger.error(ExceptionUtility.getTrace(e));
            map.put("status", "failure");
            map.put("message", "账号更新失败");
        }

        return map;
    }

    /**
     * 设置当前正在使用的邮箱账号
     *
     * @param mailAccount
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/set/currentMailAccount")
    @ResponseBody
    public Object setCurrentMailAccount(@RequestBody MailAccount mailAccount, ModelMap model, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        /*检验待修改的邮箱账号是否存在*/
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        Map<String, MailAccountCenter> mailAccountCenterMap = userGlobalInfo.getAccountMap();
        if (!mailAccountCenterMap.containsKey(mailAccount.getAccount())) {
            map.put("message", "邮箱账号" + mailAccount.getAccount() + "不存在");
            map.put("status", "failure");
            return map;
        }
        userGlobalInfo.setCurrentMailAccount(mailAccount.getAccount());
        map.put("status", "success");
        map.put("message", "邮箱切换成功，当前邮箱账号为 " + mailAccount.getAccount());

        return map;
    }

    /**
     * 获取用户的所有邮件账号信息
     * @param request
     * @return
     */
    @RequestMapping("/get/allMailAccounts")
    @ResponseBody
    public Object getAllMailAccount(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        List<MailAccount> mailAccountList = mailAccountMapper.selectByUserID(userGlobalInfo.getUserID());
        if (mailAccountList == null) {
            mailAccountList = new ArrayList<>();
        }

        /*清除密码*/
        for (MailAccount mailAccount : mailAccountList) {
            mailAccount.setPassword("");
        }
        map.put("mailAccount", mailAccountList);

        User globalUser = userGlobalInfo.getUser();
        User user = new User();
        user.setPk_UserID(globalUser.getPk_UserID());
        user.setNickname(globalUser.getNickname());
        user.setRegisteredTime(globalUser.getRegisteredTime());
        user.setBandingMailAccount(globalUser.getBandingMailAccount());
        map.put("user", user);

        map.put("status", "success");

        return map;
    }

    @RequestMapping("/get/mailAccountTO")
    @ResponseBody
    public Object getClientMailAccountTO(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession());
        List<MailAccount> mailAccountList = mailAccountMapper.selectByUserID(userGlobalInfo.getUserID());
        if (mailAccountList == null) {
            mailAccountList = new ArrayList<>();
        }

        List<ClientMailAccountTO> clientMailAccountTOList = new ArrayList<>();
        for (MailAccount mailAccount : mailAccountList) {
            List<AccountProperty> accountPropertyList = accountPropertyMapper.selectByPKMailAccount(mailAccount.getPk_MailAccount());
            ClientMailAccountTO mailAccountTO = new ClientMailAccountTO();
            mailAccountTO.setMailAccount(mailAccount);
            mailAccountTO.setAccountProperties(accountPropertyList.toArray(new AccountProperty[0]));
            clientMailAccountTOList.add(mailAccountTO);
        }

        map.put("status", "success");
        map.put("message", "账户数据获取成功");
        map.put("accountData", clientMailAccountTOList);

        return map;
    }

    @RequestMapping("/testConnection")
    @ResponseBody
    public Object testConnection(@RequestBody ClientMailAccountTO mailAccountTO, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        MailAccount mailAccount = mailAccountTO.getMailAccount();
        AccountProperty[] accountProperties = mailAccountTO.getAccountProperties();

        if (accountProperties.length != 2) {
            map.put("status", "failure");
            map.put("message", "必须只包含'接收配置'和'发送配置'");
            return map;
        }

        for (AccountProperty accountProperty : accountProperties) {
            accountProperty.setHost(accountProperty.getHost().trim());
            if (accountProperty.getHost().indexOf("smtp") == 0)
                accountProperty.setProtocol("smtp");
            else if (accountProperty.getHost().indexOf("imap") == 0)
                accountProperty.setProtocol("imap");
            else if (accountProperty.getHost().indexOf("pop3") == 0)
                accountProperty.setProtocol("pop3");
            else {
                map.put("status", "failure");
                map.put("message", "error: 不支持该协议 " + accountProperty.getHost() + "\n");
                return map;
            }
            accountProperty.setAuth("true");
        }

        /*创建账号中心*/
        MailAccountCenter mailAccountCenter = createMailAccountCenter(mailAccount, Arrays.asList(accountProperties));
        /*开始测试账号连接*/
        int errorCount = 0;
        try {
            mailAccountCenter.getStoreWithConnected();
            map.put("message", "success: 接收配置连接成功;  ");
        } catch (MessagingException e) {
            errorCount++;
            map.put("message", "error: 接收配置连接失败;  ");
        }

        try {
            mailAccountCenter.getTransportWithConnected();
            map.put("message", map.get("message") + "success: 发送配置连接成功;  ");
        } catch (MessagingException e) {
            errorCount++;
            map.put("message", map.get("message") + "error: 发送配置连接失败;  ");
        }

        if (errorCount == 0)
            map.put("status", "success");
        else {
            map.put("status", "failure");
            if (errorCount == 2)
                map.put("message", map.get("message") + "提示：请检查账号或密码是否正确;  ");
        }

        return map;
    }


    /**
     * 检测用户的ID是否有效
     *
     * @param request
     * @param userID
     * @return true: 有效；false: 无效
     */
    private boolean checkUserID(HttpServletRequest request, String userID) {
        boolean flag = true;
        UserGlobalInfo userGlobalInfo = UserSessionOperator.getUserGlobalInfo(request.getSession(false));
        String sessionUserID = userGlobalInfo.getUserID();
        if (!sessionUserID.equals(userID))
            flag = false;

        return flag;
    }

    /**
     * 创建用户全局信息
     *
     * @param user
     * @param request
     * @return
     */
    private UserGlobalInfo createUserGlobalInfo(User user, HttpServletRequest request) {
        UserGlobalInfo userGlobalInfo = new UserGlobalInfo();
        userGlobalInfo.setUser(user);

        Map<String, MailAccountCenter> mailAccountCenterMap = new HashMap<>();

        /*获取MailAccount*/
        List<MailAccount> mailAccountList = mailAccountMapper.selectByUserID(user.getPk_UserID());
        /*获取AccountProperty*/
        for (MailAccount mailAccount : mailAccountList) {
            List<AccountProperty> accountPropertyList = accountPropertyMapper.selectByPKMailAccount(mailAccount.getPk_MailAccount());
            if (accountPropertyList.size() == 2) {
                MailAccountCenter mailAccountCenter = createMailAccountCenter(mailAccount, accountPropertyList);
                /*将mailAccountCenter收集到map中*/
                mailAccountCenterMap.put(mailAccount.getAccount(), mailAccountCenter);
            } else {
                throw new RuntimeException("用户ID为" + user.getPk_UserID() + "：账号" + mailAccount.getAccount() + "未配置 发送 和 接收 的协议");
            }
        }

        userGlobalInfo.setAccountMap(mailAccountCenterMap);

        return userGlobalInfo;
    }


    /**
     * 创建邮箱账号中心
     *
     * @param mailAccount
     * @param accountPropertyList
     * @return
     */
    public static MailAccountCenter createMailAccountCenter(MailAccount mailAccount,
                                                      List<AccountProperty> accountPropertyList) {
        Properties properties = new Properties();

        if (accountPropertyList.size() == 2) {
            /*checkProtocol用于检测是否发送和收取的协议都设置了，值如下：
             * checkProtocol[0]为1，代表transport协议已经设置好；
             * checkProtocol[1]为1，代表store协议已经设置好*/
            int[] checkProtocol = new int[]{0, 0};
            for (AccountProperty accountProperty : accountPropertyList) {
                String protocol = accountProperty.getProtocol();
                if ("smtp".equals(protocol)) {
                    properties.setProperty("mail.transport.protocol", protocol);
                    checkProtocol[0] = 1;
                } else if("pop3".equals(protocol) || "imap".equals(protocol)) {
                    properties.setProperty("mail.store.protocol", protocol);
                    checkProtocol[1] = 1;
                } else {
                    throw new RuntimeException("协议" + accountProperty.getProtocol() + "不支持");
                }

                properties.put("mail." + protocol + ".host", accountProperty.getHost());
                properties.put("mail." + protocol + ".port", accountProperty.getPort());
                properties.put("mail." + protocol + ".auth", accountProperty.getAuth());
                properties.put("mail." + protocol + ".ssl.enable", accountProperty.getIsSSL());
                /*特别注意，此处的属性必须使用put来设置，因为超时时间它好像只认识整数，字符串没有效果，切记*/
                properties.put("mail." + protocol + ".connectiontimeout", 3000);
                properties.put("mail." + protocol + ".timeout", 3000);
                properties.put("mail." + protocol + ".writetimeout", 10000);

                if ("true".equals(accountProperty.getIsSSL())) {
                    properties.setProperty("mail." + protocol + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                }
            }

            if (checkProtocol[0] == 0 || checkProtocol[1] == 0)
                throw new RuntimeException("协议仅包含transport或者store协议，需两种协议都设置");

            properties.setProperty("mail.user.account", mailAccount.getAccount());
            properties.setProperty("mail.user.password", mailAccount.getPassword());

        } else if (accountPropertyList.size() > 0 || accountPropertyList.size() <= 0) {
            throw new RuntimeException("协议仅包含《收》《发》两种协议，但检测到协议数量不等于 2 ");
        }

        /*利用properties创建MailAccountCenter*/
        MailAccountCenter mailAccountCenter = new MailAccountCenter(properties);

        return mailAccountCenter;
    }
}
