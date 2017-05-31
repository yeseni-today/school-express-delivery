/**
 * Created by 22340 on 2017/5/31.
 */
$(document).ready(function () {
    var $table = $("#reviewList");
        // reviewList.find("tbody");tbody
    var tablecolor = 'myTable-operation-info';
    // alert(GLOBAL.token);

    $.ajax({
        url: "/reviews",
        type: "get",
        data: {"token": getCookie("token"), "state": 0},
        success: function (result) {
            var tr = $table.find("tr").remove();

            var items = result.data;

            //加载特效
            var _display = function (item) {
                var itemhtml ="<tr style='display: block' id='tr" + item.id + "'>" +
                    "<td>" + item.id + "</td>" +
                    "<td>" + item.user.name + "</td>" +
                    "<td>" + item.time + "</td>" +
                    "<td class='" + tablecolor + "icon-search" +
                    " onclick=\"openPop_select(\'" + item.id + "\',\'" + item.user.name + "\',\'icon-search\')\"></td>" +
                    "</tr>";
                $table.append(itemhtml);
                console.log("display:"+itemhtml)
            };

            for(var  i = 0;i < items.length;i++){
                _display(items[i])
            }

            var _afterdisplay = function (item) {
                $("#tr" + item.id).fadeIn(500);
            };
            beautifyDisplay(_display, _afterdisplay, items, "reviewsList");
        },
        error: function () {
            alert("ajax请求发送失败");
        }
    })

});