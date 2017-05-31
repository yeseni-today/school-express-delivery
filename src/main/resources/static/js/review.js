/**
 * Created by 22340 on 2017/5/31.
 */
$(document).ready(function () {
    var $table = $("#reviewList").find("tbody");
    var tablecolor='myTable-operation-info';

    $.ajax({
        url: "/reviews",
        type: "get",
        data: {"token":GLOBAL.token},
        success: function (result) {
            $table.find("tr").remove();

            var items = result.data;
            //加载特效
            var _display = function (item) {
                var itemhtml =
                    "<td>" + item.id + "</td>" +
                    "<td>" + item.user.name + "</td>" +
                    "<td>" + item.time + "</td>" +
                    "<td class='"+tablecolor+" " + operation + "' " +
                    "onclick=\"openPop_select(\'" + item.itemCode + "\',\'" + item.itemName + "\',\'" + operation + "\')\"></td>" +
                    "</tr>";
                $table.append(itemhtml);
            };
            var _afterdisplay = function (item) {
                $("#tr" + item.itemCode).fadeIn(500);
            };
            beautifyDisplay(_display, _afterdisplay, items, "reviewList");
        },
        error: function () {
            alert("ajax请求发送失败");
        }
    })

});