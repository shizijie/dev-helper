<!DOCTYPE html>
<html lang="en">
<head>
    <script src="webjars/jquery/3.4.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="webjars/bootstrap-table/1.9.1/bootstrap-table.min.js"></script>
    <script src="webjars/bootstrap-table/1.9.1/locale/bootstrap-table-zh-CN.js"></script>
    <link rel="stylesheet" href="webjars/bootstrap/3.3.5/css/bootstrap.min.css" />
    <link rel="stylesheet" href="webjars/bootstrap-table/1.9.1/bootstrap-table.min.css" />
    <meta charset="UTF-8">
    <title>造数</title>
</head>
<body>
    <h1 class="text-center ">CREATE DATAS</h1>
    <form id="dbForm" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label" for="url">url（数据库连接url）</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="url" name="url" placeholder="url" required>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="username">username（数据库帐号）</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="username" name="username" placeholder="username" required>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="pwd">pwd（数据库密码）</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="pwd" name="pwd" placeholder="pwd"  required>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label" for="driver">driver（数据库连接包）</label>
            <div class="col-sm-8">
                <select class="form-control" name="driver" id="driver" >
                    <option value="com.mysql.jdbc.Driver">
                        com.mysql.jdbc.Driver
                    </option>
                    <option value="org.postgresql.Driver">
                        org.postgresql.Driver
                    </option>
                </select>
            </div>
        </div>
    </form>
    <button id="checkConnection" onclick="checkConnection()" type="button" class="btn btn-primary col-md-offset-5">校验连接池信息</button>
    <a class="btn btn-info col-md-offset-1" href="/index">返回主页</a>
    <table id="table"></table>
    <!-- 模态框 -->
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModalScrollable">test</button>
    <div class="modal fade bd-example-modal-xl" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
        <div class="modal-dialog" style="width:90%" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalScrollableTitle">表造数</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="createDatas()">创建数据</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    var nowDB={}
    var formTmp;
    var optionsHtml;
    listDataEnum();

    $('#exampleModalScrollable').on('hide.bs.modal', function () {
        if(formTmp){
            formTmp.remove();
        }
    });
    function listDataEnum() {
        $.ajax({
            type: "get",
            url: "/listDataEnum",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success:function (data) {
                if(data&&data.code==="000000"){
                    optionsHtml='';
                    $.each(data.result, function () {
                        optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                    });
                }
            }
        });
    }
    
    function createDatas() {
        var d = {};
        var t = $('#modelForm').serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        console.log(d);
        console.log(t);
    }
    
    function checkConnection() {
        var d = {};
        var t = $('#dbForm').serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        $.ajax({
            type: "post",
            url: "/checkConnection",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(d),
            dataType: "json",
            success:function (data) {
                if(data&&data.code==="000000"){
                    //禁用form表单
                    var temp={};
                    $.each(t, function () {
                        temp[this.name] = this.value;
                        nowDB[this.name] = this.value;
                        $("#"+this.name).attr("disabled",true);
                    });
                    $("#checkConnection").attr("disabled",true);
                    showTable(temp);
                }else if(data){
                    alert(data.msg);
                }
            }
        });
    }
    
    function showTable(data) {
        $('#table').bootstrapTable({
            url: 'listTableByConnection',
            method: 'post',
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
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            queryParams : function (params) {
                return data;
            },
            responseHandler:function(res){
                return res.result;
            },
            onClickRow: function (row) {
                queryTableInfo(row.tableName);
            },
            columns: [{
                field: 'tableName',
                title: '表名'
            }, {
                field: 'columnsName',
                title: '字段名'
            },]
        });
    }

    function queryTableInfo(table) {
        nowDB['tableName']=table;
        $.ajax({
            type: "post",
            url: "/queryTableInfo",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(nowDB),
            dataType: "json",
            success:function (data) {
                if(data&&data.code==="000000"){
                    var exportForm = $('<form id="modelForm" class="form-horizontal">' +
                            '<div class="row">' +
                            '<label class="col-md-2 text-center">字段名</label>' +
                            '<label class="col-md-1 text-center">字段类型</label>' +
                            '<label class="col-md-2 text-center">字段注释</label>' +
                            '<label class="col-md-1 text-center">字典值</label>' +
                            '<label class="col-md-6 text-center">自定义</label>' +
                            '</div></form>');
                    $.each(data.result, function () {
                        var divTmp=$('<div class="row"></div>' );
                        var html='<input class="col-md-2 text-center" readonly name="columnName" value="'+this.columnName+'">';
                        html+='<input class="col-md-1 text-center" readonly name="columnType" value="'+this.columnType+'">';
                        html+='<input class="col-md-2 text-center" readonly name="columnRemark" value="'+this.columnRemark+'">';
                        html+='<select class="col-md-1" name="dictType">'+optionsHtml+'</select>';
                        html+='<input class="col-md-6" name="dictValue">';
                        divTmp.append(html);
                        exportForm.append(divTmp);
                    });
                    formTmp=exportForm;
                    $('.modal-body').append(exportForm);
                    $('#exampleModalScrollable').modal('show');
                }else if(data){
                    alert(data.msg);
                }
            }
        });
    }

</script>
</html>