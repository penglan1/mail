<%--
  @Author PENGL
  2020-04-05
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<script>
    function junkProcess() {
        if (this.xxxTab.name !== this.editableTabsValue) {
            // 显示页面
            this.editableTabs[1] = this.xxxTab;
            this.editableTabsValue = this.xxxTab.name;
        }
        /*如果正在获取数据，则不对重复调用进行响应*/
        if (this.xxxLoadingFlag) {
            this.$message({
                showClose: true,
                message: "请稍等，数据正在传输中...",
                type: "warning"
            });
            return;
        }

        /*检测是否已经加载过一次收件箱中的数据了*/
        if (!this.loadingXxxMailListOnceFlag) {
            this.$message({
                showClose: true,
                message: "正在获取数据...",
            });
            this.xxxLoadingFlag = true;
            // 加载数据
            loadingXxxMailList(mailContainerVue);
        } else {
            if (!this.updatingLatestXxxFlag) {
                console.log('正在更新数据');
                this.updatingLatestXxxFlag = true;
                this.$message({
                    showClose: true,
                    message: "正在更新数据...",
                });
                this.xxxLoadingFlag = true;
                updatingXxxMailList(mailContainerVue);
            }
        }
    }

    function loadingXxxMailList(goalVue) {
        goalVue.xxxLoadingFlag = true;
        $.ajax({
            url: "/mail/....",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                owner: goalVue.currentMailAccount,
                action: 'getXxx'
            }),
            success: function (data) {
                try {
                    if (data.status === "success") {
                        goalVue.xxxTableData = data.mailList;
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        });
                        //设置加载了一次的标志，防止不必要的多次重复加载
                        goalVue.loadingXxxMailListOnceFlag = true;
                        goalVue.xxxLoadingFlag = false;
                        //调用更新最新数据，更新收件箱中的数据
                        goalVue.xxxProcess();
                        return;
                    } else {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'warning'
                        });
                    }
                } catch (error) {
                    console.log("加载收件箱信息过程中出错");
                    console.log(error);
                }
                goalVue.xxxLoadingFlag = false;
            },
            error: function (xhr, status, error) {
                console.log("服务器正在维护中...");
                console.log(error);
                goalVue.$message({
                    showClose: true,
                    message: '服务器正在维护中...',
                    type: 'error'
                });
                goalVue.xxxLoadingFlag = false;
            }
        });
    }

    function updatingSentMailList(goalVue) {
        goalVue.updatingLatestXxxFlag = true;
        $.ajax({
            url: "/mail/....",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                owner: goalVue.currentMailAccount,
                action: 'receiveMail',
            }),
            success: function (data) {
                try {
                    if (data.status === "success") {
                        for (var i= 0; i < data.mailList.length; i++) {
                            goalVue.xxxTableData.push(data.mailList[i]);
                        }
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        });
                    } else {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'warning'
                        });
                    }
                } catch (error) {
                    console.log("更新已发送邮件信息过程中出错");
                    console.log(error);
                }
                goalVue.xxxLoadingFlag = false;
                //放开标志，意味着可以再一次的获取最新数据
                goalVue.updatingLatestXxxFlag = false;
            },
            error: function (xhr, status, error) {
                console.log("服务器正在维护中...");
                console.log(error);
                goalVue.$message({
                    showClose: true,
                    message: '服务器正在维护中...',
                    type: 'error'
                });
                goalVue.xxxLoadingFlag = false;
                //放开标志，意味着可以再一次的获取最新数据
                goalVue.updatingLatestXxxFlag = false;
            }
        });
    }

    function deleteMailInSent(goalVue, rows) {
        $.ajax({
            url: "/mail/....",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                owner: goalVue.currentMailAccount,
                action: 'deleteMailBaseInfo',
                xxxMailBaseInfoVOs: rows
            }),
            success: function (data) {
                try {
                    if (data.status === "success") {
                        /*取消选择*/
                        goalVue.$refs.xxxMultipleTable.clearSelection();
                        /*从客户端的收件箱列表中删除该邮件*/
                        for (var i = 0; i < rows.length; i++) {
                            goalVue.xxxTableData.remove(rows[i]);
                        }
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        });
                        goalVue.containerLoadingFlag = false;
                        return;
                    } else {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'warning'
                        });
                        goalVue.containerLoadingFlag = false;
                        return;
                    }
                } catch (error) {
                    console.log("删除收件箱基本信息时出错");
                    console.log(error);
                }
                goalVue.containerLoadingFlag = false;
                /*修改失败，允许重新加载所有数据*/
                goalVue.loadingXxxMailListOnceFlag = false;
            },
            error: function (xhr, status, error) {
                console.log("服务器正在维护中...");
                console.log(error);
                goalVue.$message({
                    showClose: true,
                    message: '服务器正在维护中...',
                    type: 'error'
                });
                goalVue.containerLoadingFlag = false;
                /*失败，允许重新加载所有数据*/
                goalVue.loadingXxxMailListOnceFlag = false;
            }
        });

    }

    function updateSentMailBaseInfo(goalVue, rows) {
        $.ajax({
            url: "/mail/....",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                owner: goalVue.currentMailAccount,
                action: 'updateMailBaseInfo',
                xxxMailBaseInfoVOs: rows
            }),
            success: function (data) {
                try {
                    if (data.status === "success") {
                        /*取消选择*/
                        goalVue.$refs.xxxMultipleTable.clearSelection();
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        });
                        goalVue.containerLoadingFlag = false;
                        return;
                    } else {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'warning'
                        });
                    }
                } catch (error) {
                    console.log("更新已发送基本信息出错");
                    console.log(error);
                }
                goalVue.containerLoadingFlag = false;
                /*修改失败，允许重新加载所有数据*/
                goalVue.loadingXxxMailListOnceFlag = false;
            },
            error: function (xhr, status, error) {
                console.log("服务器正在维护中...");
                console.log(error);
                goalVue.$message({
                    showClose: true,
                    message: '服务器正在维护中...',
                    type: 'error'
                });
                goalVue.containerLoadingFlag = false;
                /*修改失败，允许重新加载所有数据*/
                goalVue.loadingXxxMailListOnceFlag = false;
            }
        });
    }
</script>