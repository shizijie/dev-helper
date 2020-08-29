//打开 管理 模态框
function openModel(){
    refreshTable();
    $('#exampleModalScrollable').modal('show');
}
//关闭 管理 模态框
function closeModel(){
    $("#dataBaseTable").bootstrapTable('destroy');
    $('#exampleModalScrollable').modal('hide');
    listDatabase();
}
//修改数据源
function updateDatabase(row){
    $("#id").val(row.id);
    $("#comment").val(row.comment);
    $("#url").val(row.url);
    $("#database").val(row.database);
    $("#username").val(row.username);
    $("#pwd").val(row.pwd);
    $("#driver").val(row.driver);
    openServiceModel();
}
//删除数据源
function deleteDatabase(row){
    var d = {};
    d['id']=row.id;
    const func = (data) => {
        if(data&&data.code==="000000"){
            $("#dataBaseTable").bootstrapTable('refresh');
        }else{
            alert(data.msg);
        }
    };
    getReq("/data/deleteDatabase",d,func);
}
//打开添加界面
function openServiceModel(){
    $('#serviceModel').modal('show');
}
//关闭添加界面
function closeServiceModel(){
    $('#serviceModel').modal('hide');
    document.getElementById("addServiceForm").reset();
    $("#dataBaseTable").bootstrapTable('refresh');
}
//添加/修改数据源
function addService(){
    var d = {};
    var t = $('#addServiceForm').serializeArray();
    $.each(t, function () {
        d[this.name] = this.value;
    });
    const func = (data) => {
        if(data&&data.code==="000000"){
            $("#dataBaseTable").bootstrapTable('refresh');
            closeServiceModel();
        }else{
            alert(data.msg);
        }
    };
    postReq("/data/addDatabase",d,func);
}

function refreshTable(){
    $('#dataBaseTable').bootstrapTable({
        url: 'data/listDatabase',
        method: 'get',
        //toolbar: '#toolbar',              //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                     //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ip",                     //每一行的唯一标识，一般为主键列
        showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        responseHandler:function(res){
            return res.result;
        },
        // onClickRow: function (row) {
        //     queryTableInfo(row.tableName);
        // },
        columns: [
            {
                field: 'comment',
                title: '注释'
            },
            {
                field: 'url',
                title: 'ip:port'
            },
            {
                field: 'database',
                title: '库名'
            },
            {
                field: 'username',
                title: '用户名'
            },
            {
                field: 'pwd',
                title: '密码'
            },
            {
                field: 'driver',
                title: '连接类型'
            },
            {
                field: 'operate',
                title: '操作',
                events: dataEvents,//给按钮注册事件
                formatter: addDataBtn//表格中增加按钮
            },
        ]
    });
}
window.dataEvents = {
    'click #updateDatabase': function (e, value, row, index) {
        updateDatabase(row);
    },
    'click #deleteDatabase': function (e, value, row, index) {
        deleteDatabase(row);
    }
}
function addDataBtn(value, row, index) {
    return [
        '<button id="updateDatabase" type="button" class="btn btn-primary">修改</button>',
        '<button id="deleteDatabase" type="button" class="btn btn-danger">删除</button>',
    ].join('');
}