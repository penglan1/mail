<%--
  @Author PENGL
  2020-03-28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>提示页面</title>

    <link href="/mail/lib/500lib/bootstrap.min.css" rel="stylesheet">
    <link href="/mail/lib/500lib/dmaku.main.css" rel="stylesheet">
    <link href="/mail/lib/500lib/respons.css" rel="stylesheet">

    <script src="/mail/lib/500lib/jquery-2.1.1.min.js" type="text/javascript"></script>
    <script src="/mail/lib/500lib/bootstrap.min.js" type="text/javascript"></script>
    <script src="/mail/lib/500lib/modernizr.custom.js" type="text/javascript"></script>
    <script src="/mail/lib/500lib/jquery.nicescroll.min.js" type="text/javascript"></script>
    <script src="/mail/lib/500lib/scripts.js" type="text/javascript"></script>

    <base href="http://localhost/mail/"/>
</head>
<body>
<div class="animationload" style="display: none;">
    <div class="loader" style="display: none;">
    </div>
</div>
<div id="wrapper">
    <div class="container">
        <div class="col-xs-12 col-sm-7 col-lg-7">
            <div class="info">
                <h1>稍等!</h1>
                <h2>服务器资源繁忙!</h2>
                <a href="" class="btn">返回首页</a>
            </div>
        </div>
        <div class="col-xs-12 col-sm-5 col-lg-5 text-center">
            <div class="guardian">
                <img src="lib/500lib/guardian.gif" alt="Fighting">
            </div>
        </div>
    </div>
</div>
<div id="ascrail2000" class="nicescroll-rails"
     style="width: 4px; z-index: 999999; cursor: default;
     position: fixed; top: 0px; height: 100%; right: 0px;
     display: none;">
    <div style="position: relative; top: 0px; float: right;
    width: 2px; height: 0px; background-color: rgb(255, 255, 255);
    border: 1px solid rgb(255, 255, 255); background-clip: padding-box;
    border-radius: 5px;">
    </div>
</div>

</body>
</html>