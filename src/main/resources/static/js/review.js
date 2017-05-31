/**
 * Created by 22340 on 2017/5/31.
 */
$(document).ready(function () {
    var $table = $("#reviewList").find("tbody");

    $.ajax({
        url: "/reviews",
        type: "get",
        data: {"token": getCookie("token"), "state": 0},
        success: function (result) {
            // $table.find("tr").remove();
            //加载特效
            var _display = function (item) {
                var itemhtml ="<tr style='display: none' id='tr" + item.id + "'>" +
                    "<td>" + item.id + "</td>" +
                    "<td>" + item.user.name + "</td>" +
                    "<td>" + item.time + "</td>" +
                    "<td class='myTable-operation-info icon-search' "+
                    // " onclick=\"openPop_review(\'" + item + "\')\"></td>" +
                    "</tr>";
                $table.append(itemhtml);
                console.log("display:"+itemhtml)
            };
            var _afterdisplay = function (item) {
                $("#tr" + item.id).fadeIn(500);
            };
            beautifyDisplay(_display, _afterdisplay, result.data, "reviewsList");
        },
        error: function () {
            alert("ajax请求发送失败");
        }
    })
});


function openPop_review(review) {
    $(".pop li").css({"min-height": "3em", "line-height": "3em"});  //todo 弹出窗口样式
    openPop();
    // $.ajax({
    //     url: "/query/itemInfo",
    //     type: "post",
    //     data: {"itemCode": itemCode},
    //     success: function (result) {
    //         if (result.message == "success") {
    //             openPop();
    //             var item = result.content.itemEntity;
    //             document.getElementsByName("itemCode")[1].value = item.itemCode;
    //             document.getElementsByName("itemName")[1].value = item.itemName;
    //             //命名冲突
    //             // $("[name='itemCode']").val(item.itemCode);
    //             // $("[name='itemName']").val(item.itemName);
    //             $("[name='itemSpec']").val(item.itemSpec);
    //             $("[name='itemCount']").val(item.itemCount);
    //             $("[name='itemPrice']").val(item.itemPrice);
    //             $("[name='itemIntroduce']").val(item.itemIntroduce);
    //             $("[name='itemBorrowTimelimit']").val(item.itemBorrowTimelimit);
    //             $("[name='itemState']").val(item.itemState);
    //             $("[name='itemExamine']").val(item.itemExamine);
    //             $("[name='itemRemind']").val(item.itemRemind);
    //             $("[name='itemCategory']").val(item.categoryEntity.categoryName);
    //             $("[name='companyName']").val(item.companyEntity.companyName);
    //             $("[name='itemSlot']").val(result.content.slot);
    //         } else {
    //             alert("查询详情出错");
    //         }
    //     },
    //     error: function () {
    //         alert("ajax请求发送失败");
    //     }
    // });
}