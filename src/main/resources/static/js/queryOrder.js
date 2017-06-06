/**
 * Created by 22340 on 2017/6/6.
 */
function queryOrder() {
    var orderId =$("#orderId").val();
    $.ajax({
        url: "/orders/"+orderId,
        type: "get",
        data: {"token": getCookie("token")},
        success: function (result) {
            if(result.status===200){
                showOrder(result.data);
            }else {
                alert("出错");
            }
        },
        error: function () {
            alert("查询订单 ajax请求发送失败");
        }
    })
}

function showOrder(order) {
    console.log(JSON.stringify(order));
    if(order===null) return;

    $("#id").text(order.id);
    $("#createTime").text(order.createTime);
    $("#price").text(order.price);

    $("#expressName").text(order.expressName);
    $("#pickupTime").text(order.pickupTime);
    $("#pickupAddress").text(order.pickupAddress);
    $("#expressCode").text(order.expressCode);
    $("#replacementUid").text(order.replacement.uid);
    $("#replacementName").text(order.replacement.name);
    $("#replacementPhone").text(order.replacement.phone);

    $("#deliveryTime").text(order.deliveryTime);
    $("#deliveryAddress").text(order.deliveryAddress);
    $("#recipientUid").text(order.recipient.uid);
    $("#recipientName").text(order.recipient.name);
    $("#recipientPhone").text(order.recipient.phone);
}