function getReq(url,func) {
    $.ajax({
        type: "get",
        url: url,
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success:func
    });
}

function postReq(url,data,func) {
    $.ajax({
        type: "post",
        url: url,
        contentType: "application/json;charset=utf-8",
        data:JSON.stringify(data),
        dataType: "json",
        success:func
    });
}