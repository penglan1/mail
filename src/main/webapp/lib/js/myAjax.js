/**
 * ajaxSend 异步请求方法
 * 参数：
 * readyState4 当请求成功返回数据时ajax调用该函数，该函数为无参函数，形式如下：
 *          {
 *             state200: function () {...}
 *             state404: function () {...}
 *             state500: function () {...}
 *          }
 * method 请求的方式 POST, GET
 * url 请求资源的地址
 * bodyData 请求体数据
 * header 请求头，header为对象，其中含有key和value
 *        EG: {
 *              key: "Content-type",
 *              value: "application/x-www-form-urlencoded;charset=UTF-8"
 *            }
 * isAsync 是否启用异步调用
 * readyState3 请求处理中，该函数为无参函数，形式如下：
 *             {
 *                callback: function () {...}
 *             }
 * readyState2 请求已接收，该函数为无参函数，形式如下：
 *             {
 *                callback: function () {...}
 *             }
 * readyState1 服务器连接已建立，该函数为无参函数，形式如下：
 *             {
 *                callback: function () {...}
 *             }
 */
function ajaxSend(readyState4, method, url, bodyData, header, isAsync, readyState3, readyState2, readyState1) {
    //创建ajax引擎对象
    var ajax;
    if (window.XMLHttpRequest) { /*火狐等主流浏览器都支持*/
        ajax = new XMLHttpRequest();
    } else if (window.ActiveXObject) { /*IE低版本*/
        ajax = new ActiveXObject("Microsoft.XMLHTTP");
    }
    //复写Onreadystatechange函数
    ajax.onreadystatechange = function () {
        //判断ajax状态
        if (ajax.readyState == 4) {
            //判断响应状态码
            if (ajax.status == 200) { /*响应成功*/
                if (readyState4.state200) {
                    readyState4.state200(ajax);
                } else {
                    alert("请求-响应成功")
                }

            } else if (ajax.status == 404) { /*页面不存在*/
                if (readyState4.state404) {
                    readyState4.state404(ajax);
                } else {
                    alert("请求的资源不存在");
                }
            } else if (ajax.status == 500) { /*服务器内部错误*/
                if (readyState4.state500) {
                    readyState4.state500(ajax);
                } else {
                    alert("服务器繁忙");
                }
            }
        } else if (ajax.readyState == 3) {
            if (readyState3) {
                readyState3.callback(ajax);
            }
        } else if (ajax.readyState == 2) {
            if (readyState2) {
                readyState2.callback(ajax);
            }
        } else if (ajax.readyState == 1) {
            if (readyState1) {
                readyState1.callback(ajax);
            }
        }
    }
    //创建链接和发送请求数据 ajax.open(method, url, async, username, password);
    ajax.open(method,url,(isAsync == undefined) ? true : isAsync);
    //注意：如果使用post方式发送数据，如果想要像form表单那样让服务器获得键值对，则需要设置请求头application/x-www-form-urlencoded
    /*有意思的事：POST方式以键值对的形式发送数据是utf-8编码，这是固定的，这是浏览器，或者说 HTTP 协议这样定的
    以下修改不生效
    ajax.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=ISO-8859");
    如果不设置请求头，post方式默认以text/plain;charset=UTF-8的请求头发送，并且其编码是固定的UTF-8,浏览器固定的
    */
    if (header) {
        ajax.setRequestHeader(header.key, header.value);
    }

    if (bodyData) {
        ajax.send(bodyData);
    } else {
        /*对于GET方式，这里一定要设置为null */
        ajax.send(null);
    }
}