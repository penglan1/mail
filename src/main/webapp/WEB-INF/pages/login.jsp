<%--
  @Author PENGL
  2020-03-28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Login</title>

    <link rel="stylesheet" href="/mail/lib/css/element-ui.css"/>
    <%--加载的顺序：vue.js一定要在element-ui.js前面，因为它后者会使用到前者--%>
    <script src="/mail/lib/js/vue.js"></script>
    <script src="/mail/lib/js/element-ui.js"></script>
    <script src="/mail/lib/js/jQuery.js"></script>

    <style>
        .el-input {
            position: relative;
            font-size: 14px;
            display: inline-block;
            width: 200px;
        }
        #form {

            margin: 0 auto 0 auto;
            width: 400px;
            box-shadow: 0 2px 12px 0;

            rgba(0, 0, 0, 0.1);
            height: 300px;
            padding-top: 50px;

        }
        #form-button {
            text-align: center;
        }
    </style>
</head>
<body>
    <div id="form">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="账号" prop="userID">
                <el-tooltip class="item" effect="light" content="使用绑定的邮箱账号登入" placement="top">
                    <el-input v-model="ruleForm.userID" autocomplete="off"></el-input>
                </el-tooltip>
            </el-form-item>
            <%--绑定按键事件时，如果不起作用，可以使用.native来覆盖原来的事件--%>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="ruleForm.password" autocomplete="off"
                          v-on:keyup.enter.native="submitForm('ruleForm')"></el-input>
            </el-form-item>
            <div id="form-button">
                <el-button type="primary" :loading="submitLoadingFlag" @click="submitForm('ruleForm')">提交</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </div>
            <div style="margin: 20px 10px 10px 0px; text-align: right;">
                <el-link type="info" href="/mail/register.jsp">创建一个账号?</el-link>
            </div>
        </el-form>
    </div>
    <script>
        var loginVue = new Vue({
            el: "#form",
            data() {
                var validateUserID = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请输入账号'));
                    } else {
                        callback();
                    }
                };
                var validatePassword = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请输入密码'));
                    } else {
                        callback();
                    }
                };
                return {
                    ruleForm: {
                        userID: '',
                        password: ''
                    },
                    rules: {
                        userID: [
                            { validator: validateUserID, trigger: 'blur' }
                        ],
                        password: [
                            { validator: validatePassword, trigger: 'blur' }
                        ]
                    },
                    submitLoadingFlag: false
                };
            },
            methods: {
                submitForm(formName) {
                    if (this.submitLoadingFlag == true) {
                        this.$message({
                            showClose: true,
                            message: "登入验证进行中，请稍等...",
                            type: 'warning'
                        });
                        return false;
                    }

                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            this.submitLoadingFlag = true;
                            console.log("进行登入认证...");
                            this.checkLogin();
                            console.log("认证完成");
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                },
                resetForm(formName) {
                    if (this.submitLoadingFlag == true) {
                        this.$message({
                            showClose: true,
                            message: "登入验证进行中，请稍等...",
                            type: 'warning'
                        });
                        return false;
                    }
                    this.$refs[formName].resetFields();
                },
                /*登入认证*/
                checkLogin: function () {
                    $.ajax({
                        url: "/mail/info/check/login",
                        type: "post",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify({
                            "pk_UserID":this.ruleForm.userID,
                            "password":this.ruleForm.password
                        }),
                        success: function (data) {
                            loginVue.submitLoadingFlag = false;
                            var message = data.message;
                            console.log(message);
                            if (data.status === "success") {
                                loginVue.$message({
                                    showClose: true,
                                    message: data.message,
                                    type: 'success'
                                });
                                window.location.href = data.targetUrl;
                            } else {
                                loginVue.$message({
                                    showClose: true,
                                    message: data.message,
                                    type: 'warning'
                                });
                            }


                        },
                        error: function (xhr, status, error) {
                            loginVue.submitLoadingFlag = false;
                            console.log("服务器正在维护中...");
                            console.log(error);
                            loginVue.$message({
                                showClose: true,
                                message: '服务器正在维护中...',
                                type: 'error'
                            });
                        }

                    });
                }
            }
        });
    </script>
</body>
</html>