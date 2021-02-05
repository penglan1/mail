<%--
  @Author PENGL
  2020-04-11
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Register</title>

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
            width: 250px;
        }
        #register {
            margin: 0 auto 0 auto;
            width: 400px;
            box-shadow: 0 2px 12px 0;
            rgba(0, 0, 0, 0.1);
            height: 450px;
            padding-top: 50px;
        }
    </style>
</head>
<body>
    <div id="register">
        <el-form :model="registerForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model="registerForm.nickname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="registerForm.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
                <el-input type="password" v-model="registerForm.checkPass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="绑定邮箱" prop="bandingMailAccount">
                <el-input v-model="registerForm.bandingMailAccount" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="验证码" prop="verification">
                <el-input v-model="registerForm.verification"
                          autocomplete="off"></el-input>
                <el-button @click="sendVerification" size="mini">获取邮箱验证码</el-button>
            </el-form-item>
            <div style="text-align: center;">
                <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </div>
        </el-form>
    </div>

    <script>
        var registerVue = new Vue({
            el: '#register',
            data() {
                var validateNickname = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请输入用户昵称'));
                    } else {
                        callback();
                    }
                };
                var validatePass = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请输入密码'));
                    } else {
                        if (this.registerForm.checkPass !== '') {
                            this.$refs.ruleForm.validateField('checkPass');
                        }
                        callback();
                    }
                };
                var validatePass2 = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请再次输入密码'));
                    } else if (value !== this.registerForm.password) {
                        callback(new Error('两次输入密码不一致!'));
                    } else {
                        callback();
                    }
                };
                var validateMailAccount = (rule, value, callback) => {
                    if (value === '') {
                        callback(new Error('请输入邮箱账号'));
                    } else {
                        callback();
                    }
                };
                return {
                    registerForm: {
                        nickname: '',
                        account: '',
                        password: '',
                        checkPass: '',
                        bandingMailAccount: '',
                        verification: ''
                    },
                    rules: {
                        nickname: [
                            { validator: validateNickname, trigger: 'blur' }
                        ],
                        password: [
                            { validator: validatePass, trigger: 'blur' }
                        ],
                        checkPass: [
                            { validator: validatePass2, trigger: 'blur' }
                        ],
                        bandingMailAccount: [
                            { validator: validateMailAccount, trigger: 'blur' }
                        ]
                    }
                };
            },
            methods: {
                submitForm(formName) {
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            if (registerVue.registerForm.verification === '') {
                                registerVue.$message({
                                    showClose: true,
                                    message: '请输入验证码',
                                    type: 'warning'
                                });
                            } else {
                                registerVue.registerSubmit();
                            }
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                },
                resetForm(formName) {
                    this.$refs[formName].resetFields();
                },
                sendVerification() {
                    this.$refs.ruleForm.validate((valid) => {
                        if (valid) {
                            $.ajax({
                                url: "/mail/info/get/verification",
                                type: "post",
                                contentType: "application/json",
                                dataType: "json",
                                data: JSON.stringify({
                                    user: {
                                        nickname: this.registerForm.nickname,
                                        password: this.registerForm.password,
                                        bandingMailAccount: this.registerForm.bandingMailAccount
                                    },
                                    verification: this.verification
                                }),
                                success: function (data) {
                                    if (data.status === "success") {
                                        registerVue.$message({
                                            showClose: true,
                                            message: data.message,
                                            type: 'success'
                                        });
                                    } else {
                                        registerVue.$message({
                                            showClose: true,
                                            message: data.message,
                                            type: 'warning'
                                        });
                                    }


                                },
                                error: function (xhr, status, error) {
                                    console.log("服务器正在维护中...");
                                    console.log(error);
                                    registerVue.$message({
                                        showClose: true,
                                        message: '服务器正在维护中...',
                                        type: 'error'
                                    });
                                }

                            });
                        } else {
                            console.log('error get verification!!');
                            return false;
                        }
                    });
                },
                registerSubmit() {
                    $.ajax({
                        url: "/mail/info/register",
                        type: "post",
                        contentType: "application/json",
                        dataType: "json",
                        data: JSON.stringify({
                            user: {
                                nickname: this.registerForm.nickname,
                                password: this.registerForm.password,
                                bandingMailAccount: this.registerForm.bandingMailAccount
                            },
                            verification: this.registerForm.verification
                        }),
                        success: function (data) {
                            if (data.status === "success") {
                                registerVue.$message({
                                    showClose: true,
                                    message: data.message,
                                    type: 'success'
                                });
                                window.location.href = data.targetUrl;
                            } else {
                                registerVue.$message({
                                    showClose: true,
                                    message: data.message,
                                    type: 'warning'
                                });
                            }


                        },
                        error: function (xhr, status, error) {
                            console.log("服务器正在维护中...");
                            console.log(error);
                            registerVue.$message({
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