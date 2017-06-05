/**
 * Created by 22340 on 2016/11/27.
 */

// var GLOBAL = {};

window.onload = function () {
    document.body.style.fontSize = screen.width * 0.01 + "px";
    document.body.style.display = "block";
};

$(document).ready(function () {
    $("#loginButton").click(function () {
        sign();
    });
});

function sign() {
    var uid = $("[name='uid']").val();
    var password = $("[name='password']").val();
    if (checkForm(uid, password) === true) {
        $.ajax({
            type: "post",
            url: "/tokens",             //向springboot请求数据的url
            data: {"uid": uid, "password": password},
            success: function (json) {
                console.log(json);
                $("#loginButton").val("正在登陆");
                showLoading();
                setTimeout(function () {
                    if (json.status === 200) {
                        // GLOBAL.token = json.data.token;
                        setCookie("token",json.data.token,10);
                        // todo
                        location = "/sed/examine";        //跳转
                    } else {
                        $("#loginButton").val("登陆失败请重试");
                    }
                    hideLoading();
                }, 1000);
            }
        });
    }
}

function checkForm(username, password) {
    var ifInput = true;
    if (username === "") {
        ifInput = false;
        $("#loginButton").val("请输入账户名");
    } else if (password === "") {
        ifInput = false;
        $("#loginButton").val("请输入密码");
    }
    return ifInput;
}

function showLoading() {
    $("#load").css("display", "block");
}

function hideLoading() {
    $("#load").css("display", "none");
}