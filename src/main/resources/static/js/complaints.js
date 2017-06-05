/**
 * Created by 22340 on 2017/6/5.
 */
$(document).ready(function () {
    $complaints = $("#complaints");
    $emptyComplaint = $(".message");


});


function formateDate(date) {
    var  time = new Date(date);
    return time.getYear()+"-"+(time.getMonth()+1)+"-"+time.getDay();
}

function complaints_TypeOf(type) {
    $.ajax({
        url: "/complaints",
        type: "get",
        data: {"token": getCookie("token"), "state": type},
        success: function (result) {
            complaints = result.data;

            if(result.data.length===0){
                return;
            }
            $complaints.empty();
            //加载特效
            var _display = function (item) {
                var itemhtml =
                    '<div class="message effect4" id="'+'#tr'+item.id+'">'+
                        '<span class="message-title">申述单id: <strong>'+item.id+'</strong></span>'+
                        // '<span class="message-date">提交者Id:'+item.userId+'</span>'+
                        '<div class="message-content">提交者Id:'+item.userId+'</div>'+
                        '<div class="message-operation">'+
                            '<span onclick="msg_hide(\'messageID\')"></span>'+
                        '</div>'+
                    '</div>';
                $table.append(itemhtml);
            };
            var _afterdisplay = function (item) {
                $("#tr" + item.id).fadeIn(500);
            };
            beautifyDisplay(_display, _afterdisplay, result.data, "reviewsList");
        },
        error: function () {
            alert("complaints ajax请求发送失败");
        }
    })
}


