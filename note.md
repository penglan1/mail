# mail邮件项目

## 模块介绍
每个模块都由一个类来管理：
* **OutboxManager**: 负责 发送邮件 和 获取已发送邮件
* **InboxManager**: 负责 接受新邮件 和 获取收件箱中的邮件（建议在XXXManagerWrap中完成数据库的操作）
* **DraftManager**: 负责 保存草稿（同上，建议再XXXManagerWrap中完成），更新草稿（同上，建议....),
获取草稿邮件
* **JunkManager**: 负责 获取垃圾邮件
* **DeletingManager**: 负责从服务器端删除邮件



> 原则：以上模块类都不直接访问数据库，为了使缓存一致，数据库的操作都统一在其子类 XXXManagerWrap
上实现。

<s>另外，对于OutboxManager, InboxManager, DraftManager, JunkManager这四个模块的邮件的删除
操作，由于只是删除本地的，只是涉及到数据库的访问，所以将其对应的删除方法统一的放置在XXXManagerWrap子
类中来完成。</s>

## 缓存中心
为了尽可能的减少对数据库的访问，必须通过算法来是缓存和数据库中的信息一致。

## 邮件的删除
如果将邮件标记为删除，则会直接从服务器中永久删除该标记，如果想要将该删除之后会存留在已删除邮件中，则需要该邮件
再删除之前先复制到已删除文件夹下面，然后再从原文件夹中删除该邮件。
```cfml
int len = msgUIDsToDelete == null ? 0 : msgUIDsToDelete.size();
Message[] messagesToDelete = new Message[len];
int i = 0;
for (MessageTO messageTO : messageTOList) {
    if (msgUIDsToDelete.contains(messageTO.getMsgUID())) {
        messagesToDelete[i++] = messageTO.getMessage();
    }
}
/*判断是否有待删除的邮件需要转移到已删除文件中*/
if (messagesToDelete.length > 0) {
    Folder deletedFolder = FolderHelper.getDeletedMessagesFolderWithOpen(ACCOUNT_INFO);
    folder.copyMessages(messagesToDelete, deletedFolder);  // 先将邮件复制到目标文件夹下
    deletedFolder.close(true);
}

for (MessageTO messageTO : messageTOList) {
    try {
        if (msgUIDsToDelete.contains(messageTO.getMsgUID())) {
            messageTO.getMessage().setFlag(Flags.Flag.DELETED, true);
        }
    } catch (MessagingException e) {
        logger.error(ExceptionUtility.getTrace(e));
    }
}

folder.close(true);
```

## 项目中碰到的问题
1. spring mvc中配置的拦截器不起作用。
   > 解决方案：查阅很多博客，发现是由于在<mvc:annotation-driven />这个里面有个拦截器的级别高,导致不生效,所以
   需要配置一个独立的bean。
   
   
   <font color="red">经过测试，发现我们需要配置的这个独立的拦截器bean的级别是最高的，每次都会先执行这个bean，然后
   再依次执行mvc:interceptor中配置的拦截器，因此，我们可以这样理解，由于这个最高级别的拦截器已经取代了默认的再标签
   mvc:annotation-driven中的那个拦截器，所以接下来它需要完成的唯一功能就是无条件的返回true,从而保证后续的拦截器的
   执行。</font>
   ```cfml
    <!--配置拦截器-->
    <mvc:interceptors>
        <!--在这里添加一个总是返回true的拦截器，将他设置为最高级别，从而覆盖掉默认的那个拦截器->
        <bean class="pers.penglan.mail.controller.interceptor.OverrideDefaultInterceptor"/>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <mvc:exclude-mapping path="/info/check/login"/>
            <bean class="pers.penglan.mail.controller.interceptor.CommonInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
```
    
2. mvc:resource 和 拦截器 的冲突
如果拦截器路径中包含有mvc:resource中的资源的路径，则静态资源也会被拦截，如果想要两者不冲突，则需要在拦截器中配置
不拦截静态资源的路径。

# 要人命的坑
1. 坑
   ![这个要坑死人](https://note.youdao.com/yws/public/resource/0d5e3ffb1b1a1909fa17ced40b5baaa8/xmlnote/03A69B75A4134257A92DF11FF0B5E350/33071)

2. 坑
  ```cfml
    <!---加载的顺序，一定要注意加载顺序--->
    <link rel="stylesheet" href="/mail/lib/css/element-ui.css"/>
    <!--加载的顺序：vue.js一定要在element-ui.js前面，因为后者会使用到前者-->
    <script src="/mail/lib/js/vue.js"></script>
    <script src="/mail/lib/js/element-ui.js"></script>
```

# 碰到的问题
1. 数据库建表时，有一个字段前面不小心加了空格，导致数据一直插入不进去，花费了大量时间来排查。

2. 表中由于有关键字，没有使用"\`"这个字符，所以报错，后来找出原因来了，改过来了，还是报错，相同的错误，我就纳闷了，
搞了半天时间，后来才知道，原来我使用不是同一个字符，我用的时字符串的单引号"'"，看起来好像"\`"而已。我的天哪。

3. 邮件中的附件以"<"开头，以”>"结尾，查了好久才发现这个问题，一直都没有显示附件，头大，真是要人命。

4. 测试连接时，有可能会出现长时间没有响应的情况，这是因为它一直的在不断的尝试连接成功，为了避免这种情况，
应当设置连接超时的时间"mail.protocol.connectiontimeout"这个配置。
> 补充：除了设置连接超时，还得设置读取数据超时时间，以及写数据超时时间
```cfml
properties.put("mail." + protocol + ".connectiontimeout", 3000);
<!---当时我就是没有注意这个配置，导致一直连接没有回复，也不显示超时异常，等了有十几分钟了，要命！--->
properties.put("mail." + protocol + ".timeout", 3000);
properties.put("mail." + protocol + ".writetimeout", 10000);
```

# 系统操作问题
1. 删除操作，如果该邮件为仅为本地邮件，则会有就删除，如果该邮件在邮件服务器端也有，则会将该邮件
的本地端删除，同时将服务器端的邮件移动到已删除文件夹中

# 发现了一个宝藏
这里面有很多的问题，以及解决方案
[java mail问题整理](https://javaee.github.io/javamail/FAQ)

[java mail平台](https://javaee.github.io/javamail)

## summer note源码
[summer note CDN](https://cdn.jsdelivr.net/npm/summernote@0.8.16/dist)