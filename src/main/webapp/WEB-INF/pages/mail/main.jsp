<%--
  @Author PENGL
  2020-03-29
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>main</title>

    <link rel="stylesheet" href="/mail/lib/css/element-ui.css"/>
    <%--加载的顺序：vue.js一定要在element-ui.js前面，因为它后者会使用到前者--%>
    <script src="/mail/lib/js/vue.js"></script>
    <script src="/mail/lib/js/element-ui.js"></script>
    <script src="/mail/lib/js/jQuery.js"></script>

    <link href="/mail/lib/css/bootstrap.css" rel="stylesheet">
    <script src="/mail/lib/js/bootstrap.js"></script>
    <script src="/mail/lib/js/summernote.js"></script>
    <%--有毒了，一定要用cdn里面的样式，否则出现图标乱码--%>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.16/dist/summernote.min.css" rel="stylesheet">
    <%--添加中文支持--%>
    <script src="/mail/lib/js/summernote-zh-CN.js"></script>

    <style>
        .el-aside.mail-container {
            border-right: 1px #f6f9fb solid;
        }
        .el-header.mail-container {
            background: #eaeff4;
        }

        .el-header, .el-footer {
            color: #51fa87;
            text-align: center;
            line-height: 20px;
        }

        .el-aside {
            color: #333;
            text-align: center;
            line-height: 20px;
            overflow-x: hidden;
            overflow-y: auto;
        }


        .el-main {
            color: #333;
            text-align: center;
            line-height: 0px;
        }

        body > .el-container {
            margin-bottom: 40px;
        }

        .el-container:nth-child(5) .el-aside,
        .el-container:nth-child(6) .el-aside {
            line-height: 20px;
        }

        .el-container:nth-child(7) .el-aside {
            line-height: 320px;
        }

        body {
            margin: 0px 0px 0px 0px;
        }

        html, body, #mail-Container, #mail-Container-1 {
            height: 100%;
        }

        .tab-pan-content {
            line-height: 20px;
        }

        /*账号选择下拉选项的样式*/
        .demonstration {
            margin: 10px auto 2px auto;
            display: inline-block;
        }

        /* 邮件列表操作按钮的样式 */
        .mail-operation-button {
            text-align: left;
        }

        /*邮件列表头部*/
        .el-table__header-wrapper {
            margin-top: -10px;
        }

        /* 头部功能按钮样式 */
        .header-menu-item {
            color: black;
        }

        .el-tabs--border-card > .el-tabs__content {
            padding: 0px;
        }

        /* 邮件详细信息页面的样式 */
        .mail-entity-item {
            text-align: left;
        }
        .mail-entity-info-button {
            border: 0px;
            line-height: 15px;
            font-size: 15px;
        }
        .el-form-item.mail-entity-item {
            margin: 0px;
        }

        .mail-entity-detail {
            /*height: 550px;
            overflow: auto;
            width: 830px;*/
            text-align: left;
            height: 550px;
            overflow: auto;
            padding: 0px 20px 20px 20px;
            word-break:break-all;
            word-wrap:break-word;
        }
        /*收件人添加按钮样式*/
        .el-tag + .el-tag {
            margin-left: 10px;
        }

        .input-toRcpt {
            width: 300px;
            vertical-align: center;
            height: 32px;
            line-height: 32px;
        }
        .mail-entity-item.writing > label {
            margin: 0px;
        }
        .input-toRcpt > input {
            border: 0px;
            padding-left: 0px;
        }
        .mail-entity-item.writing  {
            border-bottom: 1px #bfd9f0 solid;
        }
        /*文件上传按钮*/
        input[type="file"] {
            display: contents;
        }

        .el-upload-list {
        }
        .el-upload-list__item:first-child {
            margin-top: 5px;
        }

        .el-upload-list > li {
            width: 200px;
            float: left;
            margin: 5px 5px 5px 0px;
            background: #eaf1f5;
        }

        /*写信页面的主题区域的样式*/
        .writing-subject.el-input.el-input--small > input {
            border: 0px;
            padding: 0px;
            line-height: 25px;
            font-size: 20px;
            /*text-align: center;*/
        }
        /*邮件详情页面中的附件下载连接的样式*/
        .mail-entity-attachment-button.el-link.el-link--default {
            display: inline-block;
            border-radius: 3px;
            margin: 0px 5px 0px 0px;
            background: #f6f9fb;
        }
        /*写信回复页面的原始邮件按钮的样式*/
        .mail-entity-attachment-button.group {
            display: inline-block;
            background: #f6f9fb;
            margin-right: 3px;
        }
        /*账号中心的样式*/
        .account-item-input.port.el-input.el-input--small {
            width: 100px;
        }
        .account-item-input.el-input.el-input--small {
            width: 200px;
            float: left;
        }
        .account-item-input.el-input.el-input--small > input {
            padding-left: 2px;
            font-size: 15px;
        }
        .el-aside.accountForm-area {
            padding: 20px;
            border-right: 1px #f6f9fb solid;
        }

        /*summernote的工具栏样式*/
        .panel-default>.panel-heading {
            background-color: #f9fafb;
        }
        /*边框*/
        .note-editor.note-airframe, .note-editor.note-frame {
            border: 1px solid #ecf0f3;
        }
        /*logol样式*/
        .logol {
            color: #b4c3cc;
            font-size: 25px;
            margin: 30px 0px 10px 5px;
            font-style: oblique;
        }

    </style>

</head>
<body>
    <div id="mail-Container"
         v-loading="containerLoadingFlag"
         :element-loading-text="containerLoadingText"
         element-loading-spinner="el-icon-loading"
         element-loading-background="rgba(0, 0, 0, 0.1)">

        <el-container id="mail-Container-1" class="mail-container">
            <!-- ============================================================================================= -->
            <!-- ================================aside 部分================================================== -->
            <!-- ============================================================================================= -->
            <el-aside width="220px" class="mail-container">
                <!-- 邮箱账号切换========================= -->
                <div id="user-choose-area">
                    <div class="block">
                        <span class="demonstration">{{currentMailAccountLabel}}</span>
                        <el-cascader
                                style="width: 200px;"
                                v-model="value"
                                :options="mailAccountOptions"
                                @change="mailAccountChange"></el-cascader>

                    </div>
                </div>

                <div style="margin: 20px 0px">
                    <el-button-group>
                        <el-button @click="inboxProcess" icon="el-icon-takeaway-box" round>收信</el-button>
                        <el-button @click="writeLetter" icon="el-icon-edit" round>写信</el-button>
                    </el-button-group>
                </div>

                <!-- 导航菜单========================= -->
                <el-menu
                        default-active="1"
                        class="el-menu-vertical-demo"
                        @open="handleOpen"
                        @close="handleClose"
                        :default-openeds="defaultOpeneds"
                        ctive-text-color="#ffd04b">

                    <el-submenu index="1">
                        <template slot="title">
                            <i class="el-icon-folder-opened"></i>
                            <span>常用文件夹</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="1-1" @click="inboxProcess">收件箱</el-menu-item>
                            <el-menu-item index="1-2" @click="sentProcess">已发送</el-menu-item>
                            <el-menu-item index="1-3" @click="junkProcess">垃圾邮件</el-menu-item>
                            <el-menu-item index="1-4" @click="deletedProcess">已删除</el-menu-item>
                            <el-menu-item index="1-5" @click="draftProcess">草稿箱</el-menu-item>
                            <%--<el-menu-item index="1-6" @click="sentFailureProcess">发件箱</el-menu-item>--%>
                            <%--<el-menu-item index="1-7" @click="flagProcess">星标邮件</el-menu-item>--%>
                        </el-menu-item-group>
                    </el-submenu>
                    <el-menu-item index="2">
                        <i class="el-icon-setting"></i>
                        <span slot="title" @click="accountCenterProcess">邮件账号管理</span>
                    </el-menu-item>

                </el-menu>

            </el-aside>
            <el-container class="mail-container">
                <!-- ============================================================================================= -->
                <!-- ================================header 部分================================================== -->
                <!-- ============================================================================================= -->

                <el-header style="height: 70px;" class="mail-container">
                    <div class="header-menu-option" style="float: right; height: 40px;">
                        <el-button type="text" class="header-menu-item" icon="el-icon-s-custom" @click="userInfoManager">{{userInfo.nickname}}</el-button>
                       <%-- <el-divider direction="vertical"></el-divider>
                        <el-button type="text" class="header-menu-item" icon="el-icon-setting">设置</el-button>
--%>                        <el-divider direction="vertical"></el-divider>
                        <el-button type="text" class="header-menu-item"
                                   icon="el-icon-bottom-right" @click="confirmToLogout">退出</el-button>
                    </div>
                    <div class="logol" style="text-align: left;">PL Mail</div>
                </el-header>

                <!-- ============================================================================================= -->
                <!-- ================================main 部分================================================== -->
                <!-- ============================================================================================= -->
                <el-main style="padding-top: 0px;padding-left: 0px;padding-bottom: 0px;padding-right: 0px;" class="mail-container">
                    <el-tabs v-model="editableTabsValue" type="border-card" @tab-remove="removeTab"
                             style="height: 100%; border: 0px;">
                        <el-tab-pane :key="editableTabs[0].name"
                                     :label="editableTabs[0].title"
                                     :name="editableTabs[0].name"
                                     style="border: 0px black solid;">
                            <div v-html="editableTabs[0].content" class="tab-pan-content"></div>
                        </el-tab-pane>

                        <el-tab-pane
                                :key="editableTabs[1].name"
                                :label="editableTabs[1].title"
                                :name="editableTabs[1].name">
                            <div class="tab-pan-content"  v-if="editableTabs[1].type == 'common'">
                                <!-- 收件箱列表页面-->
                                <div v-if="editableTabs[1].name == 'inbox'"  v-loading="inboxLoadingFlag"
                                     :element-loading-text="inboxLoadingText">
                                    <div class="mail-operation-button"  style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="confirmToDeleteFromInbox">删除</el-button>
                                            <el-button icon="el-icon-document-checked" size="small"
                                                       @click="setReadInInbox">标为已读</el-button>
                                            <el-button icon="el-icon-message" size="small"
                                                       @click="setUnreadInInbox">标为未读</el-button>
                                            <el-button icon="el-icon-star-on" size="small"
                                                       @click="toggleFlagInInbox">星标</el-button>
                                        </el-button-group>
                                    </div>
                                    <div>
                                        <el-table
                                                ref="inboxMultipleTable"
                                                :data="inboxTableData"
                                                tooltip-effect="dark"
                                                style="width: 100%"
                                                height="550px"
                                                highlight-current-row
                                                :default-sort = "{prop: 'sentDate', order: 'descending'}"
                                                @selection-change="handleSelectionChangeForInbox"
                                                @row-click="showMailDetailForInbox">
                                            <el-table-column
                                                    type="selection"
                                                    width="55">
                                            </el-table-column>
                                            <el-table-column
                                                    label="状态"
                                                    width="80">
                                                <!-- <template slot-scope="scope">{{ scope.row.date }}</template> -->
                                                <template slot-scope="scope">
                                                    <el-tooltip class="item" effect="light" content="未读邮件"
                                                                placement="top" v-if="scope.row.readFlag == 'N'">
                                                        <i class="el-icon-message" style="color: red;"></i>
                                                    </el-tooltip>
                                                    <el-tooltip class="item" effect="light" content="已读邮件"
                                                                placement="top" v-else>
                                                        <i class="el-icon-document-checked"></i>
                                                    </el-tooltip>
                                                </template>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="from"
                                                    label="发件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="subject"
                                                    label="主题"
                                                    show-overflow-tooltip>
                                            </el-table-column>
                                            <el-table-column
                                                    label=""
                                                    width="55">
                                                <template slot-scope="scope">
                                                    <el-tooltip class="item" effect="light" content="星标邮件" placement="top">
                                                        <i class="el-icon-star-on" v-if="scope.row.flag == 1"></i>
                                                    </el-tooltip>
                                                </template>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="sentDate"
                                                    label="日期"
                                                    sortable
                                                    width="200">
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="inbox-list-footer">
                                        <el-button round class="mail-entity-info-button" v-if="inboxTableData.length > 0">
                                            共：{{inboxTableData.length}}封
                                        </el-button>
                                    </div>

                                </div>
                                <!-- 已发送列表页面 -->
                                <div v-else-if="editableTabs[1].name == 'sent'" v-loading="sentLoadingFlag"
                                     :element-loading-text="sentLoadingText">
                                    <div class="mail-operation-button"  style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="confirmToDeleteFromSent">删除</el-button>
                                            <el-button icon="el-icon-star-on" size="small"
                                                       @click="toggleFlagInSent">星标</el-button>
                                        </el-button-group>
                                    </div>
                                    <div>
                                        <el-table
                                                ref="sentMultipleTable"
                                                :data="sentTableData"
                                                tooltip-effect="dark"
                                                style="width: 100%"
                                                height="550px"
                                                highlight-current-row
                                                :default-sort = "{prop: 'sentDate', order: 'descending'}"
                                                @selection-change="handleSelectionChangeForSent"
                                                @row-click="showMailDetailForSent">
                                            <el-table-column
                                                    type="selection"
                                                    width="55">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="from"
                                                    label="发件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="to"
                                                    label="收件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="300">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="subject"
                                                    label="主题"
                                                    show-overflow-tooltip>
                                            </el-table-column>
                                            <el-table-column
                                                    label=""
                                                    width="55">
                                                <template slot-scope="scope">
                                                    <el-tooltip class="item" effect="light" content="星标邮件" placement="top">
                                                        <i class="el-icon-star-on" v-if="scope.row.flag == 1"></i>
                                                    </el-tooltip>
                                                </template>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="sentDate"
                                                    label="日期"
                                                    sortable
                                                    width="200">
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="inbox-list-footer">
                                        <el-button round class="mail-entity-info-button" v-if="sentTableData.length > 0">
                                            共：{{sentTableData.length}}封
                                        </el-button>
                                    </div>
                                </div>
                                <!-- 垃圾邮件列表页面 -->
                                <div v-else-if="editableTabs[1].name == 'junk'"  v-loading="junkLoadingFlag"
                                     :element-loading-text="sentLoadingText">
                                    <div class="mail-operation-button"  style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="confirmToDeleteFromJunk">删除</el-button>
                                            <el-button icon="el-icon-star-on" size="small"
                                                       @click="toggleFlagInJunk">星标</el-button>
                                        </el-button-group>
                                    </div>
                                    <div>
                                        <el-table
                                                ref="junkMultipleTable"
                                                :data="junkTableData"
                                                tooltip-effect="dark"
                                                style="width: 100%"
                                                height="550px"
                                                highlight-current-row
                                                :default-sort = "{prop: 'sentDate', order: 'descending'}"
                                                @selection-change="handleSelectionChangeForJunk"
                                                @row-click="showMailDetailForJunk">
                                            <el-table-column
                                                    type="selection"
                                                    width="55">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="from"
                                                    label="发件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="to"
                                                    label="收件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="300">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="subject"
                                                    label="主题"
                                                    show-overflow-tooltip>
                                            </el-table-column>
                                            <el-table-column
                                                    label=""
                                                    width="55">
                                                <template slot-scope="scope">
                                                    <el-tooltip class="item" effect="light" content="星标邮件" placement="top">
                                                        <i class="el-icon-star-on" v-if="scope.row.flag == 1"></i>
                                                    </el-tooltip>
                                                </template>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="sentDate"
                                                    label="日期"
                                                    sortable
                                                    width="200">
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="inbox-list-footer">
                                        <el-button round class="mail-entity-info-button" v-if="junkTableData.length > 0">
                                            共：{{junkTableData.length}}封
                                        </el-button>
                                    </div>
                                </div>
                                <!-- 已删除邮件列表页面 -->
                                <div v-else-if="editableTabs[1].name == 'deleted'" v-loading="deletedLoadingFlag"
                                     :element-loading-text="deletedLoadingText">
                                    <div class="mail-operation-button"  style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="confirmToDeleteFromDeleted">删除</el-button>
                                        </el-button-group>
                                    </div>
                                    <div>
                                        <el-table
                                                ref="deletedMultipleTable"
                                                :data="deletedTableData"
                                                tooltip-effect="dark"
                                                style="width: 100%"
                                                height="550px"
                                                highlight-current-row
                                                :default-sort = "{prop: 'sentDate', order: 'descending'}"
                                                @selection-change="handleSelectionChangeForDeleted"
                                                @row-click="showMailDetailForDeleted">
                                            <el-table-column
                                                    type="selection"
                                                    width="55">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="from"
                                                    label="发件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="to"
                                                    label="收件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="300">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="subject"
                                                    label="主题"
                                                    show-overflow-tooltip>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="sentDate"
                                                    label="日期"
                                                    sortable
                                                    width="200">
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="inbox-list-footer">
                                        <el-button round class="mail-entity-info-button" v-if="deletedTableData.length > 0">
                                            共：{{deletedTableData.length}}封
                                        </el-button>
                                    </div>
                                </div>
                                <!-- 草稿邮件列表页面 -->
                                <div v-else-if="editableTabs[1].name == 'draft'" v-loading="draftLoadingFlag"
                                     :element-loading-text="draftLoadingText">
                                    <div class="mail-operation-button"  style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="confirmToDeleteFromDraft">删除</el-button>
                                            <el-button icon="el-icon-star-on" size="small"
                                                       @click="toggleFlagInDraft">星标</el-button>
                                        </el-button-group>
                                    </div>
                                    <div>
                                        <el-table
                                                ref="draftMultipleTable"
                                                :data="draftTableData"
                                                tooltip-effect="dark"
                                                style="width: 100%"
                                                height="550px"
                                                highlight-current-row
                                                :default-sort = "{prop: 'sentDate', order: 'descending'}"
                                                @selection-change="handleSelectionChangeForDraft"
                                                @row-click="showMailDetailForDraft">
                                            <el-table-column
                                                    type="selection"
                                                    width="55">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="from"
                                                    label="发件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="200">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="to"
                                                    label="收件人"
                                                    sortable
                                                    show-overflow-tooltip
                                                    width="300">
                                            </el-table-column>
                                            <el-table-column
                                                    prop="subject"
                                                    label="主题"
                                                    show-overflow-tooltip>
                                            </el-table-column>
                                            <el-table-column
                                                    prop="sentDate"
                                                    label="日期"
                                                    sortable
                                                    width="200">
                                            </el-table-column>
                                        </el-table>
                                    </div>
                                    <div class="inbox-list-footer">
                                        <el-button round class="mail-entity-info-button" v-if="draftTableData.length > 0">
                                            共：{{draftTableData.length}}封
                                        </el-button>
                                    </div>
                                </div>
                                <!-- 发件箱列表页面 -->
                                <div v-else-if="editableTabs[1].name == 'sentFailure'">
                                    sentFailure
                                </div>
                                <!-- 星标邮件列表页面 -->
                                <%--<div v-else-if="editableTabs[1].name == 'flag'">
                                    flag
                                </div>--%>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane
                                v-for="(item, index) in editableTabs"
                                v-if="index >= 2"
                                :key="item.name"
                                :label="item.title"
                                :name="item.name"
                                closable
                                style="height: 100%;">
                            <div class="tab-pan-content">
                                <%--邮件详情页面--%>
                                <div v-if="item.type == 'mailEntity'"  v-loading="mailEntityLoadingFlag">
                                    <div class="mail-operation-button"
                                         style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-s-promotion" size="small"
                                                       @click="reply(item)"
                                                       v-if="item.utilityData.type !== 'draft'">回复</el-button>
                                            <el-button icon="el-icon-s-promotion" size="small"
                                                       @click="reply(item, true)"
                                                       v-if="item.utilityData.type !== 'draft'">回复全部</el-button>
                                            <el-button icon="el-icon-s-promotion" size="small"
                                                       @click="edit(item)"
                                                       v-if="item.utilityData.type === 'draft'">编辑</el-button>
                                        </el-button-group>
                                        <el-button-group>
                                            <el-button size="small"
                                                       @click="toggleFlagInMailEntity(item)"
                                                       v-if="item.utilityData.row.flag === 0">设为星标</el-button>
                                            <el-button icon="el-icon-star-on" size="small"
                                                       @click="toggleFlagInMailEntity(item)"
                                                       v-if="item.utilityData.row.flag === 1">取消星标</el-button>
                                            <el-button icon="el-icon-delete" size="small"
                                                       @click="deleteMailEntity(item)">删除</el-button>
                                        </el-button-group>
                                    </div>
                                    <div class="mail-entity-detail">
                                        <el-form ref="form" label-width="80px" size="small">
                                            <el-form-item label="主题:" class="mail-entity-item">
                                                <h1 style="text-align: center; margin: 0px;">
                                                    {{item.mailEntity.subject}}
                                                </h1>
                                            </el-form-item>
                                            <el-form-item label="发件人:" class="mail-entity-item">
                                                <el-button round class="mail-entity-info-button">
                                                    {{item.mailEntity.from}}
                                                </el-button>
                                            </el-form-item>
                                            <el-form-item label="收件人:" class="mail-entity-item">
                                                <el-button round class="mail-entity-info-button"
                                                           v-for="(rcpt, index) in item.to" :key="index">
                                                    {{rcpt}}
                                                </el-button>
                                            </el-form-item>
                                            <el-form-item label="抄送人:" class="mail-entity-item" v-if="item.cc.length > 0">
                                                <el-button round
                                                           class="mail-entity-info-button"
                                                           v-for="(rcpt, index) in item.cc" :key="index">
                                                    {{rcpt}}
                                                </el-button>
                                            </el-form-item>
                                            <el-form-item label="时间:" class="mail-entity-item">
                                                <el-button round class="mail-entity-info-button">
                                                    {{item.mailEntity.sentDate}}
                                                </el-button>
                                            </el-form-item>
                                            <el-form-item label="附件:" class="mail-entity-item" v-if="item.attachmentList.length > 0">
                                                <el-link class="mail-entity-attachment-button"
                                                         v-for="(attach, index) in item.attachUrlList"
                                                         :key="index"
                                                         :href="attach.url"
                                                         :underline="false"
                                                         icon="el-icon-folder">
                                                    {{attach.filename}}
                                                </el-link>
                                            </el-form-item>
                                            <el-divider content-position="center">正文</el-divider>
                                            <div class="mail-entity-content"
                                                 v-html="item.mailEntity.content">

                                            </div>

                                        </el-form>
                                    </div>
                                </div>
                                <%--写信页面--%>
                                <div v-if="item.type == 'writing'" v-loading="writingLoadingFlag"
                                     :element-loading-text="writingLoadingText">
                                    <div class="mail-operation-button"
                                         style="margin: 10px 0px 10px 10px;">
                                        <el-button-group>
                                            <el-button icon="el-icon-s-promotion" size="small"
                                                       @click="sentMail">发送</el-button>
                                            <%--<el-button icon="el-icon-s-promotion" size="small">预览</el-button>--%>
                                            <%--<el-button icon="el-icon-edit-outline" size="small">存草稿</el-button>--%>
                                            <el-button icon="el-icon-bottom-right" size="small" @click="cancelWriting">取消</el-button>
                                        </el-button-group>
                                    </div>
                                    <div class="mail-entity-detail">
                                        <el-form ref="form" label-width="80px" size="small">
                                            <el-form-item label="主题:" class="mail-entity-item writing">
                                                <el-input v-model="subject" class="writing-subject"></el-input>
                                            </el-form-item>
                                            <el-form-item label="收件人:" class="mail-entity-item writing">
                                                <el-tag
                                                        :key="to"
                                                        v-for="to in toRcptList"
                                                        closable
                                                        :disable-transitions="false"
                                                        @close="handleToRcptClose(to)">
                                                    {{to}}
                                                </el-tag>
                                                <el-input
                                                        class="input-toRcpt"
                                                        v-model="inputToRcptValue"
                                                        ref="saveToRcptInput"
                                                        size="small"
                                                        @keyup.enter.native="handleEnterForToRcpt"
                                                        @keyup.delete.native="handleDeleteForToRcpt"
                                                        @blur="handleEnterForToRcpt"
                                                        @input="toRcptInput"
                                                >
                                                </el-input>
                                            </el-form-item>
                                            <el-form-item label="抄送:" class="mail-entity-item writing">
                                                <el-tag
                                                        :key="cc"
                                                        v-for="cc in ccRcptList"
                                                        closable
                                                        :disable-transitions="false"
                                                        @close="handleCcRcptClose(cc)">
                                                    {{cc}}
                                                </el-tag>
                                                <el-input
                                                        class="input-toRcpt"
                                                        v-model="inputCcRcptValue"
                                                        ref="saveCcRcptInput"
                                                        size="small"
                                                        @keyup.enter.native="handleEnterForCcRcpt"
                                                        @keyup.delete.native="handleDeleteForCcRcpt"
                                                        @blur="handleEnterForCcRcpt"
                                                        @input="ccRcptInput"
                                                >
                                                </el-input>
                                            </el-form-item>
                                            <el-form-item label="密送:" class="mail-entity-item writing">
                                                <el-tag
                                                        :key="bcc"
                                                        v-for="bcc in bccRcptList"
                                                        closable
                                                        :disable-transitions="false"
                                                        @close="handleBccRcptClose(bcc)">
                                                    {{bcc}}
                                                </el-tag>
                                                <el-input
                                                        class="input-toRcpt"
                                                        v-model="inputBccRcptValue"
                                                        ref="saveBccRcptInput"
                                                        size="small"
                                                        @keyup.enter.native="handleEnterForBccRcpt"
                                                        @keyup.delete.native="handleDeleteForBccRcpt"
                                                        @blur="handleEnterForBccRcpt"
                                                        @input="bccRcptInput"
                                                >
                                                </el-input>
                                            </el-form-item>
                                            <el-form-item label="原始附件:" class="mail-entity-item writing" v-if="attachUrlList.length > 0">
                                                <div class="mail-entity-attachment-button group" v-for="(attach, index) in attachUrlList"
                                                     :key="index">
                                                    <el-link class="mail-entity-attachment-button"
                                                             :href="attach.url"
                                                             :underline="false">
                                                        {{attach.filename}}
                                                    </el-link>
                                                    <el-button icon="el-icon-delete"
                                                               circle size="mini"
                                                               @click="deleteOriginalAttachForReply(attach)"></el-button>
                                                </div>
                                            </el-form-item>
                                            <el-form-item label="附件:" class="mail-entity-item">
                                                <el-upload
                                                        class="upload-demo"
                                                        ref="upload"
                                                        action="https://jsonplaceholder.typicode.com/posts/"
                                                        :on-preview="handlePreviewAttachment"
                                                        :on-remove="handleRemoveAttachment"
                                                        :http-request="writingAttachUpload"
                                                        :on-error="handleUploadError"
                                                        :on-success="handleUploadSuccess"
                                                        :file-list="fileList"
                                                        :auto-upload="true">
                                                    <el-button slot="trigger" size="small">添加附件</el-button>
                                                    <%--<div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>--%>
                                                </el-upload>
                                            </el-form-item>
                                            <%--<el-divider content-position="center">正文</el-divider>--%>
                                            <div class="mail-entity-content">
                                                <textarea id="summernote" name="editordata">邮件正文</textarea>
                                            </div>
                                        </el-form>
                                    </div>
                                </div>
                                <div v-if="item.type == 'sentSuccessfully'">
                                    <h1 style="text-align: center; color: #67C23A;">{{item.message}}</h1>
                                    <h3 style="text-align: center;">{{item.subject}}</h3>
                                </div>
                                <%--账号中心管理页面--%>
                                <div v-if="item.type == 'accountCenter'" v-loading="accountCenterLoadingFlag"
                                     :element-loading-text="accountCenterLoadingText">
                                    <el-container>
                                        <el-aside width="450px" class="accountForm-area">
                                            <div>
                                                <el-form ref="accountForm" :model="accountForm" label-width="80px">
                                                    <el-form-item label="账号" size="small">
                                                        <el-input v-model="accountForm.mailAccount.account" class="account-item-input"></el-input>
                                                    </el-form-item>
                                                    <el-form-item label="密码" size="small">
                                                        <el-input type="password" v-model="accountForm.mailAccount.password" class="account-item-input"></el-input>
                                                    </el-form-item>
                                                    <el-divider content-position="left">接收配置</el-divider>
                                                    <el-form-item label="服务器" size="small">
                                                        <el-input v-model="accountForm.receive.host" class="account-item-input"></el-input>
                                                    </el-form-item>
                                                    <el-form-item label="端口" size="small">
                                                        <el-input v-model="accountForm.receive.port" class="account-item-input port"></el-input>
                                                        <el-checkbox-group v-model="accountForm.receive.isSSL">
                                                            <el-checkbox label="SSL" name="isSSL"></el-checkbox>
                                                        </el-checkbox-group>
                                                    </el-form-item>
                                                    <el-divider content-position="left">发送配置</el-divider>
                                                    <el-form-item label="服务器" size="small">
                                                        <el-input v-model="accountForm.send.host" class="account-item-input"></el-input>
                                                    </el-form-item>
                                                    <el-form-item label="端口" size="small">
                                                        <el-input v-model="accountForm.send.port" class="account-item-input port"></el-input>
                                                        <el-checkbox-group v-model="accountForm.send.isSSL">
                                                            <el-checkbox label="SSL" name="isSSL"></el-checkbox>
                                                        </el-checkbox-group>
                                                    </el-form-item>
                                                    <el-form-item>
                                                        <el-button type="primary" @click="testConnection" :loading="testConnectionLoadingFlag">测试连接</el-button>
                                                        <el-button type="primary" @click="confirmToAddAccount" v-if="accountForm.operationType === 'add'">添加</el-button>
                                                        <el-button type="primary" @click="updateAccount" v-if="accountForm.operationType == 'edit'">更新</el-button>
                                                        <el-button type="primary" @click="removeAccountForm">取消</el-button>
                                                    </el-form-item>
                                                </el-form>
                                            </div>
                                        </el-aside>

                                        <el-main class="account-area-main">
                                            <template>
                                                <el-table
                                                        :data="accountData"
                                                        height="500px"
                                                        style="width: 100%">
                                                    <el-table-column
                                                            label="账号"
                                                            show-overflow-tooltip
                                                            width="200" align="center">
                                                        <template slot-scope="scope">{{scope.row.mailAccount.account}}</template>
                                                    </el-table-column>
                                                    <el-table-column label="接收配置" align="center">
                                                        <el-table-column
                                                                label="服务器"
                                                                show-overflow-tooltip
                                                                width="130" align="center">
                                                            <template slot-scope="scope">{{scope.row.receive.host}}</template>
                                                        </el-table-column>
                                                        <el-table-column
                                                                label="端口"
                                                                show-overflow-tooltip
                                                                width="50" align="center">
                                                            <template slot-scope="scope">{{scope.row.receive.port}}</template>
                                                        </el-table-column>
                                                        <el-table-column
                                                                label="SSL"
                                                                show-overflow-tooltip
                                                                width="50" align="center">
                                                            <template slot-scope="scope">{{scope.row.receive.isSSL}}</template>
                                                        </el-table-column>
                                                    </el-table-column>
                                                    <el-table-column label="发送配置" align="center">
                                                        <el-table-column
                                                                label="服务器"
                                                                show-overflow-tooltip
                                                                width="130" align="center">
                                                            <template slot-scope="scope">{{scope.row.send.host}}</template>
                                                        </el-table-column>
                                                        <el-table-column
                                                                label="端口"
                                                                show-overflow-tooltip
                                                                width="50" align="center">
                                                            <template slot-scope="scope">{{scope.row.send.port}}</template>
                                                        </el-table-column>
                                                        <el-table-column
                                                                label="SSL"
                                                                show-overflow-tooltip
                                                                width="50" align="center">
                                                            <template slot-scope="scope">{{scope.row.send.isSSL}}</template>
                                                        </el-table-column>
                                                    </el-table-column>
                                                    </el-table-column>
                                                    <el-table-column label="操作" align="center">
                                                        <template slot-scope="scope">
                                                            <el-button
                                                                    size="mini"
                                                                    @click="handleAccountEdit(scope.$index, scope.row)">编辑</el-button>
                                                            <el-button
                                                                    size="mini"
                                                                    type="danger"
                                                                    @click="handleAccountDelete(scope.$index, scope.row)">删除</el-button>
                                                        </template>
                                                    </el-table-column>
                                                </el-table>
                                                <el-row>
                                                    <el-button @click="handleAccountAdd">+ 添加账号</el-button>
                                                </el-row>
                                            </template>

                                        </el-main>
                                    </el-container>
                                </div>
                                <%--<div v-if="item.type == 'userInfoManager'" v-loading="userInfoManagerLoadingFlag"
                                     :element-loading-text="userInfoManagerLoadingText">
                                    <div v-if="item.operationType == 'display'">
                                        <el-form ref="userInfo" :model="userInfo" label-width="80px">
                                            <el-form-item label="用户昵称" size="small">
                                                {{userInfo.nickname}}
                                            </el-form-item>
                                            <el-form-item label="用户ID" size="small">
                                                {{userInfo.pk_UserID}}
                                            </el-form-item>
                                            <el-form-item label="绑定邮箱账号" size="small">
                                                {{userInfo.bandingMailAccount}}
                                            </el-form-item>
                                            <el-form-item label="注册时间" size="small">
                                                {{userInfo.registeredTime}}
                                            </el-form-item>
                                            <el-form-item>
                                                <el-button type="primary" @click="changeUserPassword">修改密码</el-button>
                                            </el-form-item>
                                        </el-form>
                                    </div>
                                    <div v-if="item.operationType == 'edit'">
                                        <el-form ref="userInfo" :model="userInfo" label-width="80px">
                                            <el-form-item label="新密码" size="small">
                                                <el-input v-model="userInfo.password"></el-input>
                                            </el-form-item>
                                            <el-form-item label="验证码" size="small">
                                                {{userInfo.bandingMailAccount}}
                                            </el-form-item>
                                            <el-form-item label="注册时间" size="small">
                                                {{userInfo.registeredTime}}
                                            </el-form-item>
                                            <el-form-item>
                                                <el-button type="primary" @click="edit" :loading="editUserInfo" v-if="item.operationType == 'display'">编辑</el-button>
                                                <el-button type="primary" @click="changeUserPassword" v-if="accountForm.operationType === 'display'">修改密码</el-button>
                                            </el-form-item>
                                        </el-form>
                                    </div>
                                </div>--%>
                            </div>
                        </el-tab-pane>
                    </el-tabs>

                </el-main>
                <!-- ============================================================================================= -->
                <!-- ================================footer 部分================================================== -->
                <!-- ============================================================================================= -->
                <%--<el-footer style="height: 30px;">Footer</el-footer>--%>
            </el-container>
        </el-container>
    </div>

    <script>

        var mailContainerVue = new Vue({
            el: "#mail-Container",
            data() {
                return {
                    /*用户和邮件账号的相关信息*/
                    userInfo: {
                        pk_UserID: '123456',
                        password: '',
                        nickname: '',
                        registeredTime: '',
                        bandingMailAccount: ''
                    },
                    mailAccount: [{
                        pk_MailAccount: '',
                        pk_UserID: '',
                        account: '',
                        password: '',
                    }],
                    currentMailAccount: '',
                    currentMailAccountLabel: '邮箱切换',

                    /*============加载 提示信息 控制=======================================================*/
                    containerLoadingFlag: false,
                    containerLoadingText: '',
                    /*============tab标签页 相关数据=======================================================*/
                    editableTabsValue: 'first',
                    editableTabs: [{
                        title: '首页',
                        name: 'first',
                        type: 'first',
                        content: '<h1>由于时间和工作量的原因，部分功能暂待日后完成！</h1>'
                    }, {
                        title: '收件箱',
                        name: 'inbox',
                        type: 'common',
                        content: ''
                    }],
                    tabIndex: 2,
                    /*============邮件账号选择 相关数据==================================================*/
                    value: [],
                    mailAccountOptions: [{
                        value: 'one@qq.com',
                        label: 'one@qq.com'
                    },{
                        value: 'two@163.com',
                        label: 'two'
                    },{
                        value: 'penglanm@163.com',
                        label: 'penglanm@163.com'
                    }],
                    /*左边导航菜单默认打开的位置*/
                    defaultOpeneds: ['1'],
                    /*============inbox列表相关数据========================================================*/
                    inboxLoadingFlag: false,
                    inboxLoadingText: '正在获取收件箱列表数据...',
                    /*标志，是否正在更新最新收件箱列表数据*/
                    updatingLatestInboxFlag: false,
                    /*标志，是否已经加载过一次收件箱中数据*/
                    loadingInboxMailListOnceFlag: false,
                    inboxTab: {
                        title: '收件箱',
                        name: 'inbox',
                        type: 'common',
                    },
                    inboxTableData: [],
                    inboxMultipleSelection: [],
                    /*============mailEntity页面相关数据==================================================*/
                    mailEntityLoadingFlag: false,
                    /*============sent列表相关数据==========================================================*/
                    sentLoadingFlag: false,
                    sentLoadingText: '正在获取已发送列表数据...',
                    /*标志，是否正在更新已发送列表的数据*/
                    updatingLatestSentFlag: false,
                    /*标志，是否已经加载过了一次已发送邮件的数据*/
                    loadingSentMailListOnceFlag: false,
                    sentTab: {
                        title: '已发送',
                        name: 'sent',
                        type: 'common'
                    },
                    sentTableData: [],
                    sentMultipleSelection: [],
                    /*============junk列表相关数据==========================================================*/
                    junkLoadingFlag: false,
                    junkLoadingText: '正在获取垃圾邮件列表数据...',
                    /*标志，是否正在更新已发送列表的数据*/
                    updatingLatestJunkFlag: false,
                    /*标志，是否已经加载过了一次已发送邮件的数据*/
                    loadingJunkMailListOnceFlag: false,
                    junkTab: {
                        title: '垃圾邮件',
                        name: 'junk',
                        type: 'common'
                    },
                    junkTableData: [],
                    junkMultipleSelection: [],
                    /*============deleted列表相关数据==========================================================*/
                    deletedLoadingFlag: false,
                    deletedLoadingText: '正在获取已删除列表数据...',
                    updatingLatestDeletedFlag: false,
                    loadingDeletedMailListOnceFlag: false,
                    deletedTab: {
                        title: '已删除',
                        name: 'deleted',
                        type: 'common'
                    },
                    deletedTableData: [],
                    deletedMultipleSelection: [],
                    /*============draft列表相关数据==========================================================*/
                    draftLoadingFlag: false,
                    draftLoadingText: '正在获取草稿箱列表数据...',
                    updatingLatestDraftFlag: false,
                    loadingDraftMailListOnceFlag: false,
                    draftTab: {
                        title: '草稿箱',
                        name: 'draft',
                        type: 'common'
                    },
                    draftTableData: [],
                    draftMultipleSelection: [],
                    /*=============writing相关数据===========================================================*/
                    writingFinishedFlag: true,
                    writingCount: 0,
                    writingTab: {
                        title: '写信',
                        name: 'writing',
                        type: 'writing',
                        message: '',
                        subject: ''
                    },
                    writingLoadingFlag: false,
                    writingLoadingText: '',
                    subject: '',
                    /*收件人数据*/
                    toRcptList: [],
                    inputToRcptValue: '',
                    toRcptDeleteFlag: false,
                    /*抄送人数据*/
                    ccRcptList: [],
                    inputCcRcptValue: '',
                    ccRcptDeleteFlag: false,
                    /*秘送人数据*/
                    bccRcptList: [],
                    inputBccRcptValue: '',
                    bccRcptDeleteFlag: false,
                    /*附件数据*，这个是给element-ui使用的，我自己不用这个*/
                    fileList: [],
                    /*我使用的是这个，用这个来存储附件信息*/
                    writingAttachList: [],
                    /*回复时，保存原来的附件的Url集合*/
                    attachUrlList: [],
                    /*=============账号中心页面的相关数据===========================================================*/
                    accountForm: {
                        mailAccount: {
                            account: '',
                            password: ''
                        },
                        receive: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        },
                        send: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        },
                        operationType: ''
                    },
                    accountCenterLoadingFlag: false,
                    accountCenterLoadingText: '',
                    accountCenterTab: {
                        title: '账号管理',
                        name: 'accountCenter',
                        type: 'accountCenter'
                    },
                    accountData: [{
                        mailAccount: {
                            pk_MailAccount: '',
                            pk_UserID: '',
                            account: 'penglanm@qq.com',
                            password: ''
                        },
                        receive: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        },
                        send: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        }
                    }],
                    /*测试连接按钮加载标志*/
                    testConnectionLoadingFlag: false
                }
            },
            methods: {
                /*============tab标签页 ====================================================================*/
                addTab(tabObj) {
                    /*检查是否已经存在该页签了*/
                    if (this.editableTabsValue == tabObj.name)
                        return;

                    for (var i = 0; i < this.editableTabs.length; i++) {
                        if (this.editableTabs[i].name == tabObj.name) {
                            this.editableTabsValue = tabObj.name;
                            return;
                        }
                    }
                    this.editableTabs.push(tabObj);
                    this.editableTabsValue = tabObj.name;
                },
                removeTab(targetName) {
                    let tabs = this.editableTabs;
                    let activeName = this.editableTabsValue;
                    if (activeName === targetName) {
                        tabs.forEach((tab, index) => {
                            if (tab.name === targetName) {
                                let nextTab = tabs[index + 1] || tabs[index - 1];
                                if (nextTab) {
                                    activeName = nextTab.name;
                                }
                            }
                        });
                    }

                    /*结束当此写信*/
                    if (targetName === this.writingTab.name)
                        this.writingFinishedFlag = true;

                    /*清除账户管理中心的数据*/
                    if (targetName === this.accountCenterTab.name) {
                        this.removeAccountForm();
                        this.accountData = [];
                    }

                    this.editableTabsValue = activeName;
                    this.editableTabs = tabs.filter(tab => tab.name !== targetName);
                },
                /*==========邮箱账号选择 =====================================================================*/

                mailAccountChange(value) {
                    this.currentMailAccountLabel = '正在切换...';
                    console.log(value[0]);
                    handleMailAccountChange(mailContainerVue, value[0]);
                    /*将加载标志都重设一遍，允许重新的读取所有的数据*/
                    this.loadingInboxMailListOnceFlag = false;
                    this.loadingSentMailListOnceFlag = false;
                    this.loadingJunkMailListOnceFlag = false;
                    this.loadingDraftMailListOnceFlag = false;
                    this.loadingDeletedMailListOnceFlag = false;
                },
                /*============导航菜单 ============================================================*/

                handleOpen(key, keyPath) {
                    console.log(key, keyPath);
                },
                handleClose(key, keyPath) {
                    console.log(key, keyPath);
                },
                /*============左侧导航菜单点击事件处理==================================================*/
                inboxProcess() {
                    if (this.inboxTab.name != this.editableTabsValue) {
                        // 显示页面
                        this.editableTabs[1] = this.inboxTab;
                        this.editableTabsValue = this.inboxTab.name;
                    }

                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.inboxLoadingFlag) {
                        return;
                    }

                    /*检测是否已经加载过一次收件箱中的数据了*/
                    if (!this.loadingInboxMailListOnceFlag) {
                        this.inboxLoadingText = '正在获取收件箱列表数据...';
                        this.inboxLoadingFlag = true;
                        // 加载数据
                        loadingInboxMailList(mailContainerVue);
                    } else if (!this.updatingLatestInboxFlag) {
                        //console.log('正在更新数据');
                        this.updatingLatestInboxFlag = true;
                        this.inboxLoadingText = '正在更新数据...';
                        this.inboxLoadingFlag = true;
                        updatingInboxMailList(mailContainerVue);
                    }
                },
                sentProcess() {
                    if (this.sentTab.name != this.editableTabsValue) {
                        // 显示页面
                        this.editableTabs[1] = this.sentTab;
                        this.editableTabsValue = this.sentTab.name;
                    }
                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.sentLoadingFlag) {
                        return;
                    }

                    if (!this.loadingSentMailListOnceFlag) {
                        this.sentLoadingText = '正在获取已发送数据...';
                        this.sentLoadingFlag = true;
                        // 加载数据
                        loadingSentMailList(mailContainerVue);
                    } else if (!this.updatingLatestSentFlag) {
                        console.log('正在更新数据');
                        this.updatingLatestSentFlag = true;
                        this.sentLoadingText = '正在更新数据...';
                        this.sentLoadingFlag = true;
                        updatingSentMailList(mailContainerVue);
                    }
                },
                deletedProcess() {
                    if (this.deletedTab.name != this.editableTabsValue) {
                        // 显示页面
                        this.editableTabs[1] = this.deletedTab;
                        this.editableTabsValue = this.deletedTab.name;
                    }
                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.deletedLoadingFlag) {
                        return;
                    }

                    /*检测是否已经加载过一次收件箱中的数据了*/
                    if (!this.loadingDeletedMailListOnceFlag) {
                        this.deletedLoadingText = '正在获取已删除数据...';
                        this.deletedLoadingFlag = true;
                        // 加载数据
                        loadingDeletedMailList(mailContainerVue);
                    } else if (!this.updatingLatestDeletedFlag) {
                        //console.log('正在更新数据');
                        this.updatingLatestDeletedFlag = true;
                        this.deletedLoadingText = '正在更新数据...';
                        this.deletedLoadingFlag = true;
                        updatingDeletedMailList(mailContainerVue);
                    }
                },
                junkProcess() {
                    if (this.junkTab.name != this.editableTabsValue) {
                        // 显示页面
                        this.editableTabs[1] = this.junkTab;
                        this.editableTabsValue = this.junkTab.name;
                    }
                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.junkLoadingFlag) {
                        return;
                    }

                    /*检测是否已经加载过一次收件箱中的数据了*/
                    if (!this.loadingJunkMailListOnceFlag) {
                        this.junkLoadingText = '正在获取垃圾邮件数据...';
                        this.junkLoadingFlag = true;
                        // 加载数据
                        loadingJunkMailList(mailContainerVue);
                    } else if (!this.updatingLatestJunkFlag) {
                        this.junkLoadingText = '正在更新数据...';
                        this.junkLoadingFlag = true;
                        updatingJunkMailList(mailContainerVue);
                    }
                },
                /*sentFailureProcess() {
                    var obj = {
                        title: '发件箱',
                        name: 'sentFailure',
                        type: 'common',
                        content: ''
                    };
                    // 显示页面
                    this.editableTabs[1] = obj;
                    this.editableTabsValue = obj.name;

                    // 加载数据

                    // 设置为加载状态
                },*/
                /*flagProcess() {
                    var obj = {
                        title: '星标邮件',
                        name: 'flag',
                        type: 'common',
                        content: ''
                    };
                    // 显示页面
                    this.editableTabs[1] = obj;
                    this.editableTabsValue = obj.name;

                    // 加载数据

                    // 设置为加载状态
                },*/
                draftProcess() {
                    if (this.draftTab.name !== this.editableTabsValue) {
                        // 显示页面
                        this.editableTabs[1] = this.draftTab;
                        this.editableTabsValue = this.draftTab.name;
                    }
                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.draftLoadingFlag) {
                        return;
                    }

                    /*检测是否已经加载过一次收件箱中的数据了*/
                    if (!this.loadingDraftMailListOnceFlag) {
                        this.draftLoadingText = '正在获取草稿箱数据...';
                        this.draftLoadingFlag = true;
                        // 加载数据
                        loadingDraftMailList(mailContainerVue);
                    } else if (!this.updatingLatestDraftFlag) {
                        //console.log('正在更新数据');
                        this.updatingLatestDraftFlag = true;
                        this.draftLoadingText = '正在更新数据...';
                        this.draftLoadingFlag = true;
                        updatingDraftMailList(mailContainerVue);
                    }
                },
                /*账号管理中心*/
                accountCenterProcess() {
                    if (this.accountCenterTab.name != this.editableTabsValue) {
                        /*检查是否已经存在该页签了*/
                        for (var i = 0; i < this.editableTabs.length; i++) {
                            if (this.editableTabs[i].name == this.accountCenterTab.name) {
                                this.editableTabsValue = this.accountCenterTab.name;
                                return;
                            }
                        }
                        this.editableTabs.push(this.accountCenterTab);
                        this.editableTabsValue = this.accountCenterTab.name;
                        /*显示加载信息，获取后端账户数据，成功之后再显示数据*/
                        this.$nextTick(
                          function () {
                              mailContainerVue.accountCenterLoadingFlag = true;
                              mailContainerVue.accountCenterLoadingText = '正在获取账户数据';
                              $.ajax({
                                  url: "/mail/info/get/mailAccountTO",
                                  type: "post",
                                  contentType: "application/json",
                                  dataType: "json",
                                  success: function (data) {
                                      try {
                                          if (data.status === 'success') {
                                              let accountData = [];
                                              for (let i = 0; i < data.accountData.length; i++) {
                                                  let mailAccountTO = {};
                                                  if (data.accountData[i].accountProperties[0].protocol === 'smtp') {
                                                      mailAccountTO.send = data.accountData[i].accountProperties[0];
                                                      mailAccountTO.receive = data.accountData[i].accountProperties[1];
                                                  } else {
                                                      mailAccountTO.receive = data.accountData[i].accountProperties[0];
                                                      mailAccountTO.send = data.accountData[i].accountProperties[1];
                                                  }
                                                  mailAccountTO.mailAccount = data.accountData[i].mailAccount;
                                                  accountData.push(mailAccountTO);
                                              }

                                              mailContainerVue.accountData = accountData;

                                              mailContainerVue.$message({
                                                  showClose: true,
                                                  message: data.message,
                                                  type: 'success'
                                              });
                                          } else {
                                              mailContainerVue.$message({
                                                  showClose: true,
                                                  message: data.message,
                                                  type: 'warning'
                                              });
                                          }
                                      } catch (error) {
                                          console.log("账号测试连接出错");
                                          console.log(error);
                                      }
                                      mailContainerVue.accountCenterLoadingFlag = false;
                                  },
                                  error: function (xhr, status, error) {
                                      console.log("服务器正在维护中...");
                                      console.log(error);
                                      mailContainerVue.$message({
                                          showClose: true,
                                          message: '服务器正在维护中...',
                                          type: 'error'
                                      });
                                      mailContainerVue.accountCenterLoadingFlag = false;
                                  }
                              });
                          }
                        );
                    }
                },
                /*==============================================*/
                showTab(data) {
                    var tabs = this.editableTabs;
                    var flag = false;
                    for (let index in tabs) {
                        console.log(tabs[index].name);

                        if (tabs[index].name === name) {
                            this.editableTabsValue = tabs[index].name;
                            flag = true;
                            break;
                        }
                    }

                    if (flag === false) {

                        tabs.push(data);
                        this.editableTabsValue = name;
                    }
                },
                /*==================================================================*/
                /*============inbox 列表的事件处理===================================*/
                /*==================================================================*/
                /*toggleSelection(rows) {
                    if (rows) {
                        rows.forEach(row => {
                            this.$refs.multipleTable.toggleRowSelection(row);
                        });
                    } else {
                        this.$refs.multipleTable.clearSelection();
                    }
                },*/
                handleSelectionChangeForInbox(val) {
                    this.inboxMultipleSelection = val;
                },
                /* 邮件删除确认======================== */
                confirmToDeleteFromInbox() {
                    if (this.inboxMultipleSelection.length > 0) {
                        this.$confirm('确认将所选邮件删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.containerLoadingFlag = true;
                            this.containerLoadingText = '正在同步删除...';
                            deleteMailInInbox(mailContainerVue, this.inboxMultipleSelection);
                        }).catch(() => {

                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                /*设置收件箱中的邮件为已读*/
                setReadInInbox() {
                    if (this.inboxMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.inboxMultipleSelection.length; i++) {
                            this.inboxMultipleSelection[i].readFlag = 'Y';
                        }
                        /*将修改同步至服务器*/
                        updateInboxMailBaseInfo(mailContainerVue, this.inboxMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                /*设置收件箱中的邮件为未读*/
                setUnreadInInbox() {
                    if (this.inboxMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.inboxMultipleSelection.length; i++) {
                            this.inboxMultipleSelection[i].readFlag = 'N';
                        }
                        /*将修改同步至服务器*/
                        updateInboxMailBaseInfo(mailContainerVue, this.inboxMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                /*设置收件箱中的邮件为星标邮件*/
                toggleFlagInInbox() {
                    if (this.inboxMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.inboxMultipleSelection.length; i++) {
                            if (this.inboxMultipleSelection[i].flag === 0)
                                this.inboxMultipleSelection[i].flag = 1;
                            else
                                this.inboxMultipleSelection[i].flag = 0;
                        }
                        /*将修改同步至服务器*/
                        updateInboxMailBaseInfo(mailContainerVue, this.inboxMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                /*==================================================================*/
                /*============sent列表中的事件处理======================================*/
                /*==================================================================*/
                confirmToDeleteFromSent() {
                    if (this.sentMultipleSelection.length > 0) {
                        this.$confirm('确认将所选邮件删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.containerLoadingFlag = true;
                            this.containerLoadingText = '正在同步删除...';
                            deleteMailInSent(mailContainerVue, this.sentMultipleSelection);
                        }).catch(() => {

                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                toggleFlagInSent() {
                    if (this.sentMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.sentMultipleSelection.length; i++) {
                            if (this.sentMultipleSelection[i].flag === 0)
                                this.sentMultipleSelection[i].flag = 1;
                            else
                                this.sentMultipleSelection[i].flag = 0;
                        }
                        /*将修改同步至服务器*/
                        updateSentMailBaseInfo(mailContainerVue, this.sentMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                handleSelectionChangeForSent(val) {
                    this.sentMultipleSelection = val;
                },
                /*==================================================================*/
                /*============junk列表中的事件处理======================================*/
                /*==================================================================*/
                confirmToDeleteFromJunk() {
                    if (this.junkMultipleSelection.length > 0) {
                        this.$confirm('确认将所选邮件删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.containerLoadingFlag = true;
                            this.containerLoadingText = '正在同步删除...';
                            deleteMailInJunk(mailContainerVue, this.junkMultipleSelection);
                        }).catch(() => {

                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                toggleFlagInJunk() {
                    if (this.junkMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.junkMultipleSelection.length; i++) {
                            if (this.junkMultipleSelection[i].flag === 0)
                                this.junkMultipleSelection[i].flag = 1;
                            else
                                this.junkMultipleSelection[i].flag = 0;
                        }
                        /*将修改同步至服务器*/
                        updateJunkMailBaseInfo(mailContainerVue, this.junkMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                handleSelectionChangeForJunk(val) {
                    this.junkMultipleSelection = val;
                },
                /*==================================================================*/
                /*============deleted列表中的事件处理===================================*/
                /*==================================================================*/
                confirmToDeleteFromDeleted() {
                    if (this.deletedMultipleSelection.length > 0) {
                        this.$confirm('确认将所选邮件删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.containerLoadingFlag = true;
                            this.containerLoadingText = '正在同步删除...';
                            deleteMailInDeleted(mailContainerVue, this.deletedMultipleSelection);
                        }).catch(() => {

                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                handleSelectionChangeForDeleted(val) {
                    this.deletedMultipleSelection = val;
                },
                /*==================================================================*/
                /*=============draft列表中的事件处理===================================*/
                /*==================================================================*/
                confirmToDeleteFromDraft() {
                    if (this.draftMultipleSelection.length > 0) {
                        this.$confirm('确认将所选邮件删除?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            this.containerLoadingFlag = true;
                            this.containerLoadingText = '正在同步删除...';
                            deleteMailInDraft(mailContainerVue, this.draftMultipleSelection);
                        }).catch(() => {

                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                toggleFlagInDraft() {
                    if (this.draftMultipleSelection.length > 0) {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = "正在同步修改...";
                        /*先将客户端的数据进行修改*/
                        for (var i = 0; i < this.draftMultipleSelection.length; i++) {
                            if (this.draftMultipleSelection[i].flag === 0)
                                this.draftMultipleSelection[i].flag = 1;
                            else
                                this.draftMultipleSelection[i].flag = 0;
                        }
                        /*将修改同步至服务器*/
                        updateDraftMailBaseInfo(mailContainerVue, this.draftMultipleSelection);
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请先选择记录',
                            type: 'warning'
                        });
                    }
                },
                handleSelectionChangeForDraft(val) {
                    this.draftMultipleSelection = val;
                },
                /*==================================================================*/
                /*============header 头部菜单按钮的事件处理===============================*/
                /*==================================================================*/
                /* 注销用户确认 */
                confirmToLogout() {
                    this.$confirm('确认是否退出系统', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        /* 确认 */
                        mailContainerVue.containerLoadingFlag = true;
                        mailContainerVue.containerLoadingText = '正在退出系统...';
                        try {
                            logout(mailContainerVue);
                        } catch (error) {
                            console.log("请求出错");
                        }

                    }).catch(() => {

                    });
                },
                /*==================================================================*/
                /*==========邮件详情 事件处理==========================================*/
                /*==================================================================*/
                /* 点击某一行时，需要显示详细信息 */
                showMailDetailForInbox(row, column, event) {
                    /*检查是否已经存在该邮件详情了*/
                    for (var i = 0; i < this.editableTabs.length; i++) {
                        if (this.editableTabs[i].name === row.pk_mailEntity) {
                            this.editableTabsValue = row.pk_mailEntity;
                            return;
                        }
                    }


                    // 从服务器端获取详细的数据，开一个新的页面进行显示，
                    // 另外还需要判断是否需要将该邮件设置为已读，并写入
                    // 数据库，这个任务需要在ajax的success方法中完成
                    var utilityData = {
                        type: 'inbox',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);

                },
                showMailDetailForSent(row, column, event) {
                    var utilityData = {
                        type: 'sent',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);
                },
                showMailDetailForJunk(row, column, event) {
                    var utilityData = {
                        type: 'junk',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);
                },
                showMailDetailForDeleted(row, column, event) {
                    var utilityData = {
                        type: 'deleted',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);
                },
                showMailDetailForDraft(row, column, event) {
                    var utilityData = {
                        type: 'draft',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);
                },
                showMailDetailForSentFailure(row, column, event) {
                    var utilityData = {
                        type: 'sentFailure',
                        row: row
                    };
                    loadingMailDetail(mailContainerVue, row, utilityData);
                },
                /*下载附件*/
                downloadAttach(attach) {
                },
                /*删除邮件*/
                deleteMailEntity(item) {
                    var type = item.utilityData.type;
                    var row = item.utilityData.row;
                    this.$message({
                        showClose: true,
                        message: '正在删除该邮件...',
                    });
                    if (type === 'inbox') {
                        deleteMailInInbox(this, [row]);
                    } else if (type === 'junk') {
                        deleteMailInJunk(this, [row]);
                    } else if (type === 'deleted') {
                        deleteMailInDeleted(this, [row]);
                    } else if (type === 'draft') {
                        deleteMailInDraft(this, [row]);
                    } else if (type === 'sent') {
                        deleteMailInSent(this, [row]);
                    }
                },
                /*星标设置切换*/
                toggleFlagInMailEntity(item) {
                    let row = item.utilityData.row;
                    let type = item.utilityData.type;

                    if (row.flag === 0)
                        row.flag = 1;
                    else
                        row.flag = 0;

                    this.$message({
                        showClose: true,
                        message: '正在同步修改',
                    });
                    if (type === 'inbox') {
                        updateInboxMailBaseInfo(this, [row]);
                    } else if (type === 'junk') {
                        updateJunkMailBaseInfo(this, [row]);
                    } else if (type === 'draft') {
                        updateDraftMailBaseInfo(this, [row]);
                    } else if (type === 'sent') {
                        updateSentMailBaseInfo(this, [row]);
                    }
                },
                /*回复邮件发件人*/
                reply(item, replayAll) {
                    let toRcpt = [item.mailEntity.from];
                    if (replayAll) {
                        for (let i = 0; i < item.to.length; i++) {
                            if (toRcpt.indexOf(item.to[i]) === -1)
                                toRcpt.push(item.to[i]);
                        }
                        for (let i = 0; i < item.cc.length; i++) {
                            if (toRcpt.indexOf(item.cc[i]) === -1)
                                toRcpt.push(item.cc[i]);
                        }
                    }

                    let newWritingFlag = false;
                    if (this.writingFinishedFlag) {
                        this.writingTab.title = '回复';
                        this.writingTab.name = 'writing';
                        this.writingTab.type = 'writing';
                        this.writingAttachList = item.attachmentList;
                        this.toRcptList = toRcpt;
                        this.ccRcptList = [];
                        this.bccRcptList = [];
                        this.subject = 'Re:' + item.mailEntity.subject;
                        this.attachUrlList = item.attachUrlList;
                        newWritingFlag = true;
                    }

                    /*开启一次回复写信*/
                    this.writingFinishedFlag = false;
                    this.addTab(this.writingTab);

                    if (newWritingFlag) {
                        /*等Vue对DOM渲染完成之后，再回调方法进行summernote初始化*/
                        this.$nextTick(function() {
                            $('#summernote').summernote({
                                lang: 'zh-CN',
                                height: 350,
                                callbacks: {
                                    onImageUpload: function(files) {
                                        var fileData = new FormData();
                                        fileData.append('item', files[0]);
                                        $.ajax({
                                            url: "/mail/upload/process/inline",
                                            type: "post",
                                            contentType: false,
                                            processData: false,
                                            data: fileData,
                                            success: function (data) {
                                                try {
                                                    if (data.status === "success") {
                                                        mailContainerVue.writingAttachList.push(data.attachment);
                                                        $('#summernote').summernote('insertImage', data.url);
                                                        mailContainerVue.$message({
                                                            showClose: true,
                                                            message: data.message,
                                                            type: 'success'
                                                        });
                                                    } else {
                                                        mailContainerVue.$message({
                                                            showClose: true,
                                                            message: data.message,
                                                            type: 'warning'
                                                        });
                                                    }
                                                } catch (error) {
                                                    console.log("文件上传出错");
                                                    console.log(error);
                                                }

                                            },
                                            error: function (xhr, status, error) {
                                                console.log('inline图片上传出现错误');
                                            }
                                        });
                                    },
                                    onMediaDelete: function (target) {
                                        console.log("hahah");
                                    }
                                }
                            });
                            let pre = '<hr/>' +
                                '<div style="background: #f6f9fb;">' +
                                '<h4>原始邮件如下：</h4>' +
                                '<h3>主题：<xmp style="display: inline;">' + item.mailEntity.subject + '</xmp></h3>' +
                                '<h4>发件人：<xmp style="display: inline;">' + item.mailEntity.from + '</xmp></h4>' +
                                '<h4>收件人：<xmp style="display: inline;">' + item.mailEntity.to + '</xmp></h4>' +
                                '<h4>抄送：<xmp style="display: inline;">' + item.mailEntity.cc + '</xmp></h4>' +
                                '<h4>时间：<xmp style="display: inline;">' + item.mailEntity.sentDate + '</xmp></h4>' +
                                '</div>';
                            let originalContent = item.mailEntity.content;
                            $('#summernote').summernote('pasteHTML', '<div>' + pre + originalContent + '</div>');
                        });
                    }
                },
                /*==================================================================*/
                /*==========写信页面相关事件 ==========================================*/
                /*==================================================================*/
                /*写信按钮点击*/
                writeLetter() {
                    var newWritingFlag = false;
                    if (this.writingFinishedFlag) {
                        this.writingTab.title = '写信';
                        this.writingTab.name = 'writing';
                        this.writingTab.type = 'writing';
                        this.writingAttachList = [];
                        this.toRcptList = [];
                        this.ccRcptList = [];
                        this.bccRcptList = [];
                        this.subject = '';
                        this.attachUrlList = [];
                        newWritingFlag = true;
                    }

                    /*开启一次写信*/
                    this.writingFinishedFlag = false;
                    this.addTab(this.writingTab);

                    if (newWritingFlag) {
                        /*等Vue对DOM渲染完成之后，再回调方法进行summernote初始化*/
                        this.$nextTick(function() {
                            $('#summernote').summernote({
                                lang: 'zh-CN',
                                height: 350,
                                toolbar: [
                                    ['style', ['style']],
                                    ['font', ['bold', 'italic', 'underline', 'clear']],
                                    ['fontname', ['fontname']],
                                    ['color', ['color']],
                                    ['para', ['ul', 'ol', 'paragraph']],
                                    ['height', ['height']],
                                    ['table', ['table']],
                                    ['insert', ['link', 'picture', 'hr']],
                                    ['view', ['fullscreen']]
                                ],
                                callbacks: {
                                    onImageUpload: function(files) {
                                        var fileData = new FormData();
                                        fileData.append('item', files[0]);
                                        $.ajax({
                                            url: "/mail/upload/process/inline",
                                            type: "post",
                                            contentType: false,
                                            processData: false,
                                            data: fileData,
                                            success: function (data) {
                                                try {
                                                    if (data.status === "success") {
                                                        mailContainerVue.writingAttachList.push(data.attachment);
                                                        $('#summernote').summernote('insertImage', data.url);
                                                        mailContainerVue.$message({
                                                            showClose: true,
                                                            message: data.message,
                                                            type: 'success'
                                                        });
                                                    } else {
                                                        mailContainerVue.$message({
                                                            showClose: true,
                                                            message: data.message,
                                                            type: 'warning'
                                                        });
                                                    }
                                                } catch (error) {
                                                    console.log("文件上传出错");
                                                    console.log(error);
                                                }

                                            },
                                            error: function (xhr, status, error) {
                                                console.log('inline图片上传出现错误');
                                            }
                                        });
                                    },
                                    onMediaDelete: function (target) {
                                        console.log("hahah");
                                    }
                                }
                            });
                        });
                    }
                },
                cancelWriting() {
                    this.removeTab(this.editableTabsValue);
                },
                /*=======to收件人=======*/
                handleToRcptClose(to) {
                    this.toRcptList.splice(this.toRcptList.indexOf(to), 1);
                },
                handleEnterForToRcpt() {
                    let inputValue = this.inputToRcptValue;
                    if (inputValue) {
                        for (var i = 0; i < this.toRcptList.length; i++) {
                            if (inputValue === this.toRcptList[i]) {
                                this.inputToRcptValue = '';
                                return;
                            }
                        }
                        this.toRcptList.push(inputValue);
                    }
                    this.inputToRcptValue = '';
                },
                handleDeleteForToRcpt() {
                    if (this.toRcptDeleteFlag && this.inputToRcptValue === '') {
                        this.toRcptList.splice(this.toRcptList.length - 1, 1);
                    } else if (this.inputToRcptValue === '') {
                        this.toRcptDeleteFlag = true;
                    }
                },
                toRcptInput() {
                    if (this.inputToRcptValue)
                        this.toRcptDeleteFlag = false;
                },
                /*=======cc抄送人=======*/
                handleCcRcptClose(cc) {
                    this.ccRcptList.splice(this.ccRcptList.indexOf(cc), 1);
                },
                handleEnterForCcRcpt() {
                    let inputValue = this.inputCcRcptValue;
                    if (inputValue) {
                        for (var i = 0; i < this.ccRcptList.length; i++) {
                            if (inputValue === this.ccRcptList[i]) {
                                this.inputCcRcptValue = '';
                                return;
                            }
                        }
                        this.ccRcptList.push(inputValue);
                    }
                    this.inputCcRcptValue = '';
                },
                handleDeleteForCcRcpt() {
                    if (this.ccRcptDeleteFlag && this.inputCcRcptValue === '') {
                        this.ccRcptList.splice(this.ccRcptList.length - 1, 1);
                    } else if (this.inputCcRcptValue === '') {
                        this.ccRcptDeleteFlag = true;
                    }
                },
                ccRcptInput() {
                    if (this.inputCcRcptValue)
                        this.ccRcptDeleteFlag = false;
                },
                /*=======bcc密送人=======*/
                handleBccRcptClose(bcc) {
                    this.bccRcptList.splice(this.bccRcptList.indexOf(bcc), 1);
                },
                handleEnterForBccRcpt() {
                    let inputValue = this.inputBccRcptValue;
                    if (inputValue) {
                        for (var i = 0; i < this.bccRcptList.length; i++) {
                            if (inputValue === this.bccRcptList[i]) {
                                this.inputBccRcptValue = '';
                                return;
                            }
                        }
                        this.bccRcptList.push(inputValue);
                    }
                    this.inputBccRcptValue = '';
                },
                handleDeleteForBccRcpt() {
                    if (this.bccRcptDeleteFlag && this.inputBccRcptValue === '') {
                        this.bccRcptList.splice(this.bccRcptList.length - 1, 1);
                    } else if (this.inputBccRcptValue === '') {
                        this.bccRcptDeleteFlag = true;
                    }
                },
                bccRcptInput() {
                    if (this.inputBccRcptValue)
                        this.bccRcptDeleteFlag = false;
                },
                /*附件处理事件*/
                handleRemoveAttachment(file, fileList) {
                    for (var i = 0; i < mailContainerVue.writingAttachList.length; i++) {
                        if (file.response.attachment.pk_attachment === mailContainerVue.writingAttachList[i].pk_attachment) {
                            mailContainerVue.writingAttachList.splice(i, 1);
                        }
                    }
                },
                handlePreviewAttachment(file) {
                    console.log(file);
                },
                writingAttachUpload(content) {
                    var fileData = new FormData();
                    fileData.append('item', content.file);
                    $.ajax({
                        url: "/mail/upload/process/attachment",
                        type: "post",
                        contentType: false,
                        processData: false,
                        data: fileData,
                        success: function (data) {
                            try {
                                if (data.status === "success") {
                                    content.onSuccess(data);
                                    //mailContainerVue.fileList.push(content);
                                    mailContainerVue.writingAttachList.push(data.attachment);
                                } else {
                                    content.onError(data);
                                }
                            } catch (error) {
                                console.log("文件上传出错");
                                console.log(error);
                            }

                        },
                        error: function (xhr, status, error) {
                            content.onError({
                                message: '文件上传出错'
                            });
                        }
                    });
                },
                handleUploadError(data) {
                    this.$message({
                        showClose: true,
                        message: data.message,
                        type: 'warning'
                    });
                },
                handleUploadSuccess(data) {
                    this.$message({
                        showClose: true,
                        message: data.message,
                        type: 'success'
                    });
                },
                /*原始邮件列表中的删除按钮*/
                deleteOriginalAttachForReply(attach) {
                    for (let i = 0; i < mailContainerVue.writingAttachList.length; i++) {
                        if (attach.pk_attachment === mailContainerVue.writingAttachList[i].pk_attachment) {
                            mailContainerVue.writingAttachList.splice(i, 1);
                            for (let j = 0; j < this.attachUrlList.length; j++) {
                                if (attach.pk_attachment === this.attachUrlList[j].pk_attachment)
                                    this.attachUrlList.splice(j, 1);
                            }
                        }
                    }
                },
                /*======发送邮件=======*/
                sentMail() {
                    /*如果正在获取数据，则不对重复调用进行响应*/
                    if (this.writingLoadingFlag) {
                        return;
                    }

                    this.writingLoadingText = '正在发送邮件...';
                    this.writingLoadingFlag = true;

                    sendMailToService(mailContainerVue);
                },
                /*==================================================================*/
                /*==========账号管理中心事件 ==========================================*/
                /*==================================================================*/
                handleAccountEdit(index, row) {
                    /*为避免数据修改的影响，拷贝一份数据*/
                    let accountForm = {
                        mailAccount: {
                            pk_MailAccount: row.mailAccount.pk_MailAccount,
                            pk_UserID: row.mailAccount.pk_UserID,
                            account: row.mailAccount.account,
                            password: row.mailAccount.password
                        },
                        receive: {
                            pk_AccountProperty: row.receive.pk_AccountProperty,
                            pk_MailAccount: row.receive.pk_MailAccount,
                            protocol: row.receive.protocol,
                            host: row.receive.host,
                            port: row.receive.port,
                            auth: row.receive.auth,
                            isSSL: row.receive.isSSL
                        },
                        send: {
                            pk_AccountProperty: row.send.pk_AccountProperty,
                            pk_MailAccount: row.send.pk_MailAccount,
                            protocol: row.send.protocol,
                            host: row.send.host,
                            port: row.send.port,
                            auth: row.send.auth,
                            isSSL: row.send.isSSL
                        },
                        operationType: 'edit'
                    }
                    
                    this.accountForm = accountForm;
                    if (this.accountForm.receive.isSSL === 'true')
                        this.accountForm.receive.isSSL = true;
                    else
                        this.accountForm.receive.isSSL = false;

                    if (this.accountForm.send.isSSL === 'true')
                        this.accountForm.send.isSSL = true;
                    else
                        this.accountForm.send.isSSL = false;

                },
                handleAccountDelete(index, row) {
                    this.$confirm('确认将该账号信息删除?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.containerLoadingFlag = true;
                        this.containerLoadingText = '正在删除该账号...';
                        deleteAccountInService(mailContainerVue, row, index);
                    }).catch(() => {

                    });
                },
                confirmToAddAccount() {
                    /*检查是否含有这个账号*/
                    let existsFlag = false;
                    let accountTO;
                    for (let i = 0; i < this.accountData.length; i++) {
                        if (this.accountData[i].mailAccount.account === this.accountForm.mailAccount.account) {
                            existsFlag = true;
                            accountTO = this.accountData[i];
                            break;
                        }
                    }

                    /*不存在该账号，意味着修改了账号*/
                    if (existsFlag) {
                        this.$message({
                            showClose: true,
                            message: '该账号已存在',
                            type: 'warning'
                        });
                        return;
                    }

                    this.accountCenterLoadingFlag = true;
                    this.accountCenterLoadingText = '正在保存账号信息...';

                    /*更新账号相关信息*/
                    AddAccountInService(this);
                },
                handleAccountAdd() {
                    this.removeAccountForm();
                    this.accountForm.operationType = 'add'
                },
                /*清除*/
                removeAccountForm() {
                    this.accountForm = {
                        mailAccount: {
                            account: '',
                            password: ''
                        },
                        receive: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        },
                        send: {
                            pk_AccountProperty: '',
                            pk_MailAccount: '',
                            protocol: '',
                            host: '',
                            port: '',
                            auth: '',
                            isSSL: false
                        }
                    };
                },
                /*测试连接*/
                testConnection() {
                    this.testConnectionLoadingFlag = true;
                    testConnectionInService(this);
                },
                /*更新账号*/
                updateAccount() {
                    /*检查是否含有这个账号*/
                    let existsFlag = false;
                    let accountTO;
                    for (let i = 0; i < this.accountData.length; i++) {
                        if (this.accountData[i].mailAccount.account === this.accountForm.mailAccount.account) {
                            existsFlag = true;
                            accountTO = this.accountData[i];
                            break;
                        }
                    }

                    /*不存在该账号，意味着修改了账号*/
                    if (!existsFlag) {
                        this.$message({
                            showClose: true,
                            message: '不存在该账号',
                            type: 'warning'
                        });
                        return;
                    }

                    this.accountCenterLoadingFlag = true;
                    this.accountCenterLoadingText = '正在更新账号信息...';

                    /*更新账号相关信息*/
                    updateAccountInService(this, accountTO);

                },
                /*==================================================================*/
                /*==========个人信息管理事件 ==========================================*/
                /*==================================================================*/
                userInfoManager() {

                }
            }
        });

        /*===============================================================================================================*/
        /*===============================================================================================================*/
        /*===============================================================================================================*/
        /*===============================================================================================================*/
        /*==========================================自定义函数============================================================*/
        /*===============================================================================================================*/
        /*邮箱账户切换*/
        function handleMailAccountChange(goalVue, mailAccountValue) {
            goalVue.containerLoadingText = '正在切换邮箱...';
            goalVue.containerLoadingFlag = true;
            var mailAccount;
            for (var i = 0; i < goalVue.mailAccount.length; i++) {
                if (goalVue.mailAccount[i].account == mailAccountValue)
                    mailAccount = goalVue.mailAccount[i];
            }
            $.ajax({
                url: "/mail/info/set/currentMailAccount",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(mailAccount),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.currentMailAccount = mailAccountValue;
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
                        console.log("邮箱切换过程中出错");
                        console.log(error);
                    }

                    /*将加载状态取消*/
                   goalVue.currentMailAccountLabel = "邮箱切换";
                   goalVue.containerLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    goalVue.containerLoadingFlag = false;

                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                }
            });
        }

        /*退出=============*/
        function logout(goalVue) {
            $.ajax({
                url: "/mail/info/check/logout",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(goalVue.userInfo),
                success: function (data) {
                    /*将加载状态取消*/
                    goalVue.containerLoadingFlag = false;

                    var message = data.message;
                    console.log(message);
                    if (data.status === "success") {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'success'
                        });
                        window.location.href = data.targetUrl;
                    } else {
                        goalVue.$message({
                            showClose: true,
                            message: data.message,
                            type: 'warning'
                        });
                    }


                },
                error: function (xhr, status, error) {
                    goalVue.containerLoadingFlag = false;

                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                }

            });
        }

        /*页面初始化数据调用的方法*/
        $(window).on('load',function () {
            mailContainerVue.containerLoadingText = "正在初始化数据...";
            mailContainerVue.containerLoadingFlag = true;
            init(mailContainerVue);
        });
        function init(goalVue) {
            /*先初始化一些之后会用到的工具属性*/
            tools();
            $.ajax({
                url: "/mail/info/get/allMailAccounts",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.userInfo = data.user;
                            goalVue.mailAccount = data.mailAccount;
                            goalVue.mailAccountOptions = [];
                            for (var i = 0; i < data.mailAccount.length; i++) {
                                goalVue.mailAccountOptions.push({
                                    value: data.mailAccount[i].account,
                                    label: data.mailAccount[i].account
                                });
                            }
                        } else {
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'warning'
                            });
                        }
                    } catch (error) {
                        console.log("初始化信息过程中出错");
                        console.log(error);
                    }

                    /*将加载状态取消*/
                    goalVue.containerLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    goalVue.containerLoadingFlag = false;

                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                }
            });
        }

        /**
         * 注册一些工具属性
         */
        function tools() {
            /*数组的处理*/
            Array.prototype.indexOf = function(val) {
                for (var i = 0; i < this.length; i++) {
                    if (this[i] == val) return i;
                }
                return -1;
            };
            Array.prototype.remove = function(val) {
                var index = this.indexOf(val);
                if (index > -1) {
                    this.splice(index, 1);
                }
            };
        }

        /*==========================================inbox 相关方法============================================================*/
        /*===============================================================================================================*/

        /**
         * 获取服务端收件箱列表数据
         */
        function loadingInboxMailList(goalVue) {
            goalVue.inboxLoadingFlag = true;
            $.ajax({
                url: "/mail/inbox/get/inbox",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getInbox',
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.inboxTableData = data.mailList;
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            //设置加载了一次的标志，防止不必要的多次重复加载
                            goalVue.loadingInboxMailListOnceFlag = true;
                            goalVue.inboxLoadingFlag = false;
                            //调用更新最新数据，更新收件箱中的数据
                            goalVue.inboxProcess();
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
                    goalVue.inboxLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.inboxLoadingFlag = false;
                }
            });
        }

        /**
         * 更新收件箱列表中的数据，获取服务器端的最新数据
         */
        function updatingInboxMailList(goalVue) {
            goalVue.updatingLatestInboxFlag = true;
            $.ajax({
                url: "/mail/inbox/receiveMail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'receiveMail',
                    msgUIDToDelete: [],
                    mailEntityVO: {},
                    attachmentVOs: []
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            for (var i= 0; i < data.mailList.length; i++) {
                                goalVue.inboxTableData.push(data.mailList[i]);
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
                        console.log("更新收件箱信息过程中出错");
                        console.log(error);
                    }
                    goalVue.inboxLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestInboxFlag = false;
                },
                error: function (xhr, status, error) {
                    goalVue.containerLoadingFlag = false;

                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.inboxLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestInboxFlag = false;
                }
            });
        }

        function loadingMailDetail(goalVue, rowData, utilityData) {
            $.ajax({
                url: "/mail/common/get/mailEntity",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getMailEntity',
                    msgUIDToDelete: [],
                    mailEntityVO: {
                        pk_mailEntity: rowData.pk_mailEntity
                    },
                    attachmentVOs: []
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            // 创建一个tab
                            var mailEntity = data.mailEntity;
                            var title = mailEntity.subject;
                            /*处理主题为空从而造成邮件不显示的情况*/
                            if(title == '' || title == null)
                                title = '主题为空';
                            /*处理邮件主题过长的情况*/
                            if (title.length > 5)
                                title = title.substring(0, 5) + "...";
                            /*处理收件人多个的情况*/
                            var to = mailEntity.to.split(',');
                            if (mailEntity.to == '')
                                to = [];
                            var cc = mailEntity.cc.split(',');
                            if (mailEntity.cc == '')
                                cc = [];
                            var tabObj = {
                                title: title,
                                name: mailEntity.pk_mailEntity,
                                type: 'mailEntity',
                                mailEntity: mailEntity,
                                to: to,
                                cc: cc,
                                utilityData: utilityData,
                                attachmentList: data.attachmentList,
                                attachUrlList: data.attachUrlList
                            };

                            /*增加标签显示该邮件的详细信息*/
                            goalVue.addTab(tabObj);

                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            /*将邮件设置为已读*/
                            if (rowData.readFlag === 'N') {
                                rowData.readFlag = 'Y';
                                updateInboxMailBaseInfo(goalVue, [rowData]);
                            }

                            return;
                        } else {
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'warning'
                            });
                        }
                    } catch (error) {
                        console.log("获取邮件详细信息出错");
                        console.log(error);
                    }
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                }
            });
        }

        /**
         * 设置inbox基本信息
         */
        function updateInboxMailBaseInfo(goalVue, mailBaseInfoList) {
            $.ajax({
                url: "/mail/inbox/update/mailBaseInfo",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'updateMailBaseInfo',
                    inboxMailBaseInfoVOs: mailBaseInfoList
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.inboxMultipleTable.clearSelection();
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
                        console.log("更新收件箱基本信息出错");
                        console.log(error);
                    }
                    goalVue.containerLoadingFlag = false;
                    /*修改失败，允许重新加载所有数据*/
                    goalVue.loadingInboxMailListOnceFlag = false;
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
                    goalVue.loadingInboxMailListOnceFlag = false;
                }
            });
        }

        /**
         * 从收件箱中删除该邮件
         */
        function deleteMailInInbox(goalVue, rows) {
            $.ajax({
                url: "/mail/inbox/delete/mailBaseInfo",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'deleteMailBaseInfo',
                    inboxMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.inboxMultipleTable.clearSelection();
                            /*从客户端的收件箱列表中删除该邮件*/
                            for (var i = 0; i < rows.length; i++) {
                                goalVue.inboxTableData.remove(rows[i]);
                                deleteMailEntityIfPresent(goalVue, rows[i]);
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
                    goalVue.loadingInboxMailListOnceFlag = false;
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
                    goalVue.loadingInboxMailListOnceFlag = false;
                }
            });
        }

        /*==========================================sent 相关方法============================================================*/
        /*===============================================================================================================*/

        /**
         * 检查已发送文件夹是否有新的未接收的邮件
         */
        function updatingSentMailList(goalVue) {
            goalVue.updatingLatestSentFlag = true;
            $.ajax({
                url: "/mail/outbox/get/receiveMail",
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
                                goalVue.sentTableData.push(data.mailList[i]);
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
                    goalVue.sentLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestSentFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.sentLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestSentFlag = false;
                }
            });
        }

        /**
         * 获取已发送文件夹中的列表数据
         */
        function loadingSentMailList(goalVue) {
            goalVue.sentLoadingFlag = true;
            $.ajax({
                url: "/mail/outbox/get/sentMail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getSent'
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.sentTableData = data.mailList;
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            //设置加载了一次的标志，防止不必要的多次重复加载
                            goalVue.loadingSentMailListOnceFlag = true;
                            goalVue.sentLoadingFlag = false;
                            //调用更新最新数据，更新收件箱中的数据
                            goalVue.sentProcess();
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
                    goalVue.sentLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.sentLoadingFlag = false;
                }
            });
        }

        /**
         * 更新sentMailBaseInfo
         */
        function updateSentMailBaseInfo(goalVue, rows) {
            $.ajax({
                url: "/mail/outbox/update/mailBaseInfo",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'updateMailBaseInfo',
                    sentMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.sentMultipleTable.clearSelection();
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
                    goalVue.loadingSentMailListOnceFlag = false;
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
                    goalVue.loadingSentMailListOnceFlag = false;
                }
            });
        }

        function deleteMailInSent(goalVue, rows) {
            $.ajax({
                url: "/mail/outbox/delete/mail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'deleteMailBaseInfo',
                    sentMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.sentMultipleTable.clearSelection();
                            /*从客户端的收件箱列表中删除该邮件*/
                            for (var i = 0; i < rows.length; i++) {
                                goalVue.sentTableData.remove(rows[i]);
                                deleteMailEntityIfPresent(goalVue, rows[i]);
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
                    goalVue.loadingSentMailListOnceFlag = false;
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
                    goalVue.loadingSentMailListOnceFlag = false;
                }
            });

        }

        /*==========================================junk 相关方法============================================================*/
        /*===============================================================================================================*/

        /**
         * 获取垃圾邮件文件夹中的列表数据
         * @param goalVue
         */
        function loadingJunkMailList(goalVue) {
            goalVue.junkLoadingFlag = true;
            $.ajax({
                url: "/mail/junk/get/junks",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getJunk'
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.junkTableData = data.mailList;
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            //设置加载了一次的标志，防止不必要的多次重复加载
                            goalVue.loadingJunkMailListOnceFlag = true;
                            goalVue.junkLoadingFlag = false;
                            //调用更新最新数据，更新收件箱中的数据
                            goalVue.junkProcess();
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
                    goalVue.junkLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.junkLoadingFlag = false;
                }
            });
        }

        /**
         * 检查Junk文件中是否有新的未接收的邮件
         * @param goalVue
         */
        function updatingJunkMailList(goalVue) {
            goalVue.updatingLatestJunkFlag = true;
            $.ajax({
                url: "/mail/junk/receiveJunk",
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
                                goalVue.junkTableData.push(data.mailList[i]);
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
                        console.log("更新垃圾邮件信息过程中出错");
                        console.log(error);
                    }
                    goalVue.junkLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestJunkFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.junkLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestJunkFlag = false;
                }
            });
        }
        
        function deleteMailInJunk(goalVue, rows) {
            $.ajax({
                url: "/mail/junk/delete/mail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'deleteMailBaseInfo',
                    junkMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.junkMultipleTable.clearSelection();
                            /*从客户端的收件箱列表中删除该邮件*/
                            for (var i = 0; i < rows.length; i++) {
                                goalVue.junkTableData.remove(rows[i]);
                                deleteMailEntityIfPresent(goalVue, rows[i]);
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
                        console.log("删除垃圾邮件基本信息时出错");
                        console.log(error);
                    }
                    goalVue.containerLoadingFlag = false;
                    /*修改失败，允许重新加载所有数据*/
                    goalVue.loadingJunkMailListOnceFlag = false;
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
                    goalVue.loadingJunkMailListOnceFlag = false;
                }
            });
        }
        
        function updateJunkMailBaseInfo(goalVue, rows) {
            $.ajax({
                url: "/mail/junk/update/mailBaseInfo",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'updateMailBaseInfo',
                    junkMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.junkMultipleTable.clearSelection();
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
                    goalVue.loadingJunkMailListOnceFlag = false;
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
                    goalVue.loadingJunkMailListOnceFlag = false;
                }
            });
        }

        /*==========================================deleted 相关方法============================================================*/
        /*===============================================================================================================*/
        function loadingDeletedMailList(goalVue) {
            goalVue.deletedLoadingFlag = true;
            $.ajax({
                url: "/mail/deleting/get/deletedMail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getDeleted'
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.deletedTableData = data.mailList;
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            //设置加载了一次的标志，防止不必要的多次重复加载
                            goalVue.loadingDeletedMailListOnceFlag = true;
                            goalVue.deletedLoadingFlag = false;
                            //调用更新最新数据，更新收件箱中的数据
                            goalVue.deletedProcess();
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
                    goalVue.deletedLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.deletedLoadingFlag = false;
                }
            });
        }
        
        function updatingDeletedMailList(goalVue) {
            goalVue.updatingLatestDeletedFlag = true;
            $.ajax({
                url: "/mail/deleting/receiveDeletedMail",
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
                                goalVue.deletedTableData.push(data.mailList[i]);
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
                    goalVue.deletedLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestDeletedFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.deletedLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestDeletedFlag = false;
                }
            });
        }
        
        function deleteMailInDeleted(goalVue, rows) {
            $.ajax({
                url: "/mail/deleting/delete/mail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'deleteMailBaseInfo',
                    deletedMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.deletedMultipleTable.clearSelection();
                            /*从客户端的收件箱列表中删除该邮件*/
                            for (var i = 0; i < rows.length; i++) {
                                goalVue.deletedTableData.remove(rows[i]);
                                deleteMailEntityIfPresent(goalVue, rows[i]);
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
                        console.log("删除已删除邮件的基本信息时出错");
                        console.log(error);
                    }
                    goalVue.containerLoadingFlag = false;
                    /*修改失败，允许重新加载所有数据*/
                    goalVue.loadingDeletedMailListOnceFlag = false;
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
                    goalVue.loadingDeletedMailListOnceFlag = false;
                }
            });
        }
        
        /*==========================================draft 相关方法============================================================*/
        /*===============================================================================================================*/

        function loadingDraftMailList(goalVue) {
            goalVue.draftLoadingFlag = true;
            $.ajax({
                url: "/mail/draft/get/drafts",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'getDraft'
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.draftTableData = data.mailList;
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            //设置加载了一次的标志，防止不必要的多次重复加载
                            goalVue.loadingDraftMailListOnceFlag = true;
                            goalVue.draftLoadingFlag = false;
                            //调用更新最新数据，更新收件箱中的数据
                            goalVue.draftProcess();
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
                    goalVue.draftLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.draftLoadingFlag = false;
                }
            });
        }
        
        function updatingDraftMailList(goalVue) {
            goalVue.updatingLatestDraftFlag = true;
            $.ajax({
                url: "/mail/draft/receive/drafts",
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
                                goalVue.draftTableData.push(data.mailList[i]);
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
                    goalVue.draftLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestDraftFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.draftLoadingFlag = false;
                    //放开标志，意味着可以再一次的获取最新数据
                    goalVue.updatingLatestDraftFlag = false;
                }
            });
        }
        
        function deleteMailInDraft(goalVue, rows) {
            $.ajax({
                url: "/mail/draft/delete/mail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'deleteMailBaseInfo',
                    draftMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.draftMultipleTable.clearSelection();
                            /*从客户端的收件箱列表中删除该邮件*/
                            for (var i = 0; i < rows.length; i++) {
                                goalVue.draftTableData.remove(rows[i]);
                                deleteMailEntityIfPresent(goalVue, rows[i]);
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
                    goalVue.loadingDraftMailListOnceFlag = false;
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
                    goalVue.loadingDraftMailListOnceFlag = false;
                }
            });
        }
        
        function updateDraftMailBaseInfo(goalVue, rows) {
            $.ajax({
                url: "/mail/draft/update/mailBaseInfo",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'updateMailBaseInfo',
                    draftMailBaseInfoVOs: rows
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            /*取消选择*/
                            goalVue.$refs.draftMultipleTable.clearSelection();
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
                    goalVue.loadingDraftMailListOnceFlag = false;
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
                    goalVue.loadingDraftMailListOnceFlag = false;
                }
            });
        }

        /*==========================================所有文件夹对列表邮件删除之后需调用的 方法====================================*/
        /*===============================================================================================================*/


        function deleteMailEntityIfPresent(goalVue, row) {
            for (let i = 0; i < goalVue.editableTabs.length; i++) {
                if (goalVue.editableTabs[i].name === row.pk_mailEntity) {
                    goalVue.editableTabs.splice(i, 1);
                    /*设置第2页签为活跃状态*/
                    goalVue.editableTabsValue = goalVue.editableTabs[1].name;
                }
            }

        }

        /*==========================================发送邮件 相关方法=========================================================*/
        /*===============================================================================================================*/

        function sendMailToService(goalVue) {
            let toRcpts = '';
            if (goalVue.toRcptList.length > 0)
                toRcpts = goalVue.toRcptList.join(',');

            let ccRcpts = '';
            if (goalVue.ccRcptList.length > 0)
                ccRcpts = goalVue.ccRcptList.join(',');

            let bccRcpts = '';
            if (goalVue.bccRcptList.length > 0)
                bccRcpts = goalVue.bccRcptList.join(',');

            $.ajax({
                url: "/mail/outbox/send/mail",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    owner: goalVue.currentMailAccount,
                    action: 'sendMail',
                    mailEntityVO: {
                        owner: goalVue.currentMailAccount,
                        from: goalVue.currentMailAccount,
                        to: toRcpts,
                        cc: ccRcpts,
                        bcc: bccRcpts,
                        subject: goalVue.subject,
                        content: $('#summernote').summernote('code')
                    },
                    attachmentVOs: goalVue.writingAttachList
                }),
                success: function (data) {
                    try {
                        if (data.status === "success") {
                            goalVue.writingLoadingFlag = false;
                            /*删除原来的标签页*/
                            goalVue.removeTab(goalVue.writingTab.name);
                            /*切换为发送成功页面*/
                            goalVue.writingTab.title = '发送成功';
                            goalVue.writingTab.name = 'sentSuccessfully';
                            goalVue.writingTab.type = 'sentSuccessfully';
                            goalVue.writingTab.message = data.message;
                            goalVue.writingTab.subject = goalVue.subject;
                            /*新增提示的标签页*/
                            goalVue.addTab(goalVue.writingTab);
                            /*清除上一次的发送数据*/
                            goalVue.writingAttachList = [];
                            goalVue.toRcptList = [];
                            goalVue.ccRcptList = [];
                            goalVue.bccRcptList = [];
                            goalVue.subject = '';

                            goalVue.writingFinishedFlag = true;

                            return;
                        } else {
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'warning'
                            });
                        }
                    } catch (error) {
                        console.log("发送邮件出错");
                        console.log(error);
                    }
                    goalVue.writingLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.writingLoadingFlag = false;
                }
            });

        }

        /*==========================================账号管理中心 相关方法======================================================*/
        /*===============================================================================================================*/

        function testConnectionInService(goalVue) {
            $.ajax({
                url: "/mail/info/testConnection",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    mailAccount: goalVue.accountForm.mailAccount,
                    accountProperties: [goalVue.accountForm.receive, goalVue.accountForm.send]
                }),
                success: function (data) {
                    try {
                        if (data.status === 'success') {
                            goalVue.$notify({
                                title: '连接成功',
                                message: data.message,
                                type: 'success'
                            });
                        } else {
                            goalVue.$notify({
                                title: '请重新检查配置信息',
                                message: data.message,
                                type: 'warning',
                                duration: 0
                            });
                        }
                    } catch (error) {
                        console.log("账号测试连接出错");
                        console.log(error);
                    }
                    goalVue.testConnectionLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.testConnectionLoadingFlag = false;
                }
            });
        }

        function updateAccountInService(goalVue, accountTO) {
            $.ajax({
                url: "/mail/info/update/mailAccount",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    mailAccount: goalVue.accountForm.mailAccount,
                    accountProperties: [goalVue.accountForm.receive, goalVue.accountForm.send]
                }),
                success: function (data) {
                    try {
                        if (data.status === 'success') {
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
                        console.log("账号测试连接出错");
                        console.log(error);
                    }
                    goalVue.accountCenterLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.accountCenterLoadingFlag = false;
                }
            });
        }

        function AddAccountInService(goalVue) {
            let mailAccount = goalVue.accountForm.mailAccount;
            $.ajax({
                url: "/mail/info/add/mailAccount",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    mailAccount: {
                        pk_UserID: goalVue.userInfo.pk_UserID,
                        account: mailAccount.account,
                        password: mailAccount.password
                    },
                    accountProperties: [goalVue.accountForm.receive, goalVue.accountForm.send]
                }),
                success: function (data) {
                    try {
                        if (data.status === 'success') {
                            let mailAccountTO = {};
                            if (data.accountTO.accountProperties[0].protocol === 'smtp') {
                                mailAccountTO.send = data.accountTO.accountProperties[0];
                                mailAccountTO.receive = data.accountTO.accountProperties[1];
                            } else {
                                mailAccountTO.receive = data.accountTO.accountProperties[0];
                                mailAccountTO.send = data.accountTO.accountProperties[1];
                            }
                            mailAccountTO.mailAccount = data.accountTO.mailAccount;

                            goalVue.accountData.push(mailAccountTO);
                            goalVue.removeAccountForm();
                            goalVue.accountForm.operationType = '';
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });

                            init(goalVue);
                        } else {
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'warning'
                            });
                        }
                    } catch (error) {
                        console.log("账号测试连接出错");
                        console.log(error);
                    }
                    goalVue.accountCenterLoadingFlag = false;
                },
                error: function (xhr, status, error) {
                    console.log("服务器正在维护中...");
                    console.log(error);
                    goalVue.$message({
                        showClose: true,
                        message: '服务器正在维护中...',
                        type: 'error'
                    });
                    goalVue.accountCenterLoadingFlag = false;
                }
            });
        }

        function deleteAccountInService(goalVue, row, index) {
            $.ajax({
                url: "/mail/info/delete/mailAccount",
                type: "post",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(row),
                success: function (data) {
                    try {
                        if (data.status === 'success') {
                            goalVue.accountData.splice(index, 1);
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'success'
                            });
                            init(goalVue);
                        } else {
                            goalVue.$message({
                                showClose: true,
                                message: data.message,
                                type: 'warning'
                            });
                        }
                    } catch (error) {
                        console.log("账号测试连接出错");
                        console.log(error);
                    }
                    goalVue.containerLoadingFlag = false;
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
                }
            });
        }

        /*==========================================定时器，定时执行邮件接收=================================================*/
        /*===============================================================================================================*/
        /*每隔2分钟更新收件箱中的数据*/
        var receiveInbox = window.setInterval('updatingInboxMailList(mailContainerVue);', 1000 * 60 * 5);

    </script>
</body>
</html>