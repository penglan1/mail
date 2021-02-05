package pers.penglan.mail.service.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import pers.penglan.mail.mapper.user.UserMapper;
import pers.penglan.mail.mapper.user.mail.AccountPropertyMapper;
import pers.penglan.mail.mapper.user.mail.MailAccountMapper;
import pers.penglan.mail.model.user.User;
import pers.penglan.mail.model.user.mail.AccountProperty;
import pers.penglan.mail.model.user.mail.MailAccount;
import pers.penglan.mail.utils.id.UIDCenter;

import java.util.Arrays;

/**
 * @Author PENGL
 * 2020-04-10
 */
@Service
public class InfoProcessor {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailAccountMapper mailAccountMapper;

    @Autowired
    private AccountPropertyMapper accountPropertyMapper;

    public void addAccountTO(MailAccount mailAccount, AccountProperty[] accountProperties) {
        int one = mailAccountMapper.insertOne(mailAccount);
        int two = accountPropertyMapper.insertList(Arrays.asList(accountProperties));
        if (one != 1 || two != 2)
            throw new RuntimeException("新增账号数量不等于1，或者新增协议数量不等于2");
    }

    public void deleteAccountTO(MailAccount mailAccount, AccountProperty[] accountProperties) {
        MailAccount deleteGoal = mailAccountMapper.selectByUserIDAndAccount(mailAccount.getPk_UserID(), mailAccount.getAccount());
        int one = mailAccountMapper.deleteByPkMailAccount(deleteGoal.getPk_MailAccount());
        int two = accountPropertyMapper.deleteByPkMailAccount(deleteGoal.getPk_MailAccount());
        if (one != 1 || two != 2)
            throw new RuntimeException("删除账号数量不等于1，或者删除协议数量不等于2");
    }

    public void updateAccountTO(MailAccount mailAccount, AccountProperty[] accountProperties) {
        /*开始将数据更新到数据库中*/
        /*获取主键，用于更新时定位*/
        MailAccount oldMailAccount = mailAccountMapper.selectByUserIDAndAccount(mailAccount.getPk_UserID(), mailAccount.getAccount());

        mailAccount.setPk_MailAccount(oldMailAccount.getPk_MailAccount());

        for (int i = 0; i < 2; i++) {
            accountProperties[i].setPk_MailAccount(mailAccount.getPk_MailAccount());
            accountProperties[i].setPk_AccountProperty(Long.toString(UIDCenter.UID_GENERATOR.nextId()));
        }

        /*更新*/
        /*禁止更新的字段*/
        int one = mailAccountMapper.update(mailAccount);
        if (one != 1)
            throw new RuntimeException("更新账号时的影响数量不等于1");
        /*删除后再插入*/
        int two = accountPropertyMapper.deleteByPkMailAccount(mailAccount.getPk_MailAccount());
        /*检测删除结果*/
        if (two != 2) {
            throw new RuntimeException("删除的AccountProperty记录不等于2");
        }
        two = accountPropertyMapper.insertList(Arrays.asList(accountProperties));
        /*检测插入的结果*/
        if (two != 2) {
            throw new RuntimeException("重新插入的AccountProperty记录不等于2");
        }
    }

    /**
     * 新增用户
     * @param sentUser
     */
    public void addUser(User sentUser) {
        userMapper.insertOne(sentUser);
    }
}
