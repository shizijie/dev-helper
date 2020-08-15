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
            <label class="col-sm-2 control-label" for="log">选择数据库信息</label>
            <div class="col-sm-6">
                <select type="text" class="form-control" id="selData" name="selData" placeholder="selData" >
                </select>
            </div>
            <button id="btnSure" class="btn btn-success col-sm-1" onclick="checkDatabase()" type="button">确定</button>
            <button class="btn btn-primary col-sm-1" onclick="openModel()" type="button">管理</button>
        </div>
    </form>
    <a class="btn btn-info text-center" href="/index">返回主页</a>
    <table id="table"></table>
    <!-- 模态框 1-->
    <div class="modal fade bd-example-modal-xl" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
        <div class="modal-dialog" style="width:90%" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalScrollableTitle">数据库列表</h5>
                    <button type="button" class="close" onclick="closeModel()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <table id="dataBaseTable"></table>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModel()">Close</button>
                    <button type="button" class="btn btn-primary" onclick="openServiceModel()">添加数据库</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框2 -->
    <div class="modal fade bd-example-modal-xl" id="serviceModel" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
        <div class="modal-dialog" style="width:90%" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalScrollableTitle">添加数据库</h5>
                    <button type="button" class="close" onclick="closeServiceModel()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form class="form-horizontal" id="addServiceForm">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="comment">注释</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="comment" name="comment" placeholder="例：开发数据库" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="url">ip</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="url" name="url" placeholder="ip:port" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="database">库名</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="database" name="database" placeholder="database" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="username">用户名</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="username" name="username" placeholder="username"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="pwd">密码</label>
                        <div class="col-sm-8">
                            <input type="text" class="form-control" id="pwd" name="pwd" placeholder="password"  required>
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
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeServiceModel()">Close</button>
                    <button type="button" class="btn btn-primary" onclick="addService()">提交</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 模态框 3-->
    <div class="modal fade bd-example-modal-xl" id="tableModel" tabindex="-1" role="dialog" aria-labelledby="tableModelTitle" aria-hidden="true">
        <div class="modal-dialog" style="width:90%" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="tableModelTitle">表造数</h5>
                    <button type="button" class="close" onclick="closeTableModel()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeTableModel()">Close</button>
                    <button type="button" class="btn btn-primary" onclick="createDatas()">创建数据</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    listDatabase();
    listDataEnum();
    var nowDatabaseId;
    var dataEnumArr;
    function listDataEnum() {
        $.ajax({
            type: "get",
            url: "/listDataEnum",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success:function (data) {
                if(data&&data.code==="000000"){
                    dataEnumArr=data.result;
                }
            }
        });
    }
    function checkDatabase(){
        var d = {};
        d['id']=$("#selData").val();
        nowDatabaseId=$("#selData").val();
        const func = (data) => {
            if(data&&data.code==="000000"){
                $("#selData").attr("disabled",true);
                $("#btnSure").attr("disabled",true);
                showTable(d);
            }else{
                alert(data.msg);
            }
        };
        getReq("/data/checkDatabase",d,func);
    }

    function showTable(data) {
        $('#table').bootstrapTable({
            url: 'data/listTable',
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
            // onClickRow: function (row) {
            //     queryTableInfo(row.tableName);
            // },
            columns: [
                {
                    field: 'tableName',
                    title: '表名'
                },
                {
                    field: 'columnsName',
                    title: '字段名'
                },
                {
                    field: 'operate',
                    title: '操作',
                    events: tableEvents,//给按钮注册事件
                    formatter: addTableBtn//表格中增加按钮
                },
            ]
        });
    }

    function addTableBtn(value, row, index) {
        return [
            '<button id="getJava" type="button" class="btn btn-default">获取java文件</button>',
            '<button id="createDatas" type="button" class="btn btn-default">造数</button>',
        ].join('');
    }
    window.tableEvents = {
        'click #getJava': function (e, value, row, index) {
            getJavaFiles(row.tableName);
        },
        'click #createDatas': function (e, value, row, index) {
            queryTableInfo(row.tableName);
        }
    }

    function getJavaFiles(table){
        var url = "java/getJavaFiles";
        var exportForm = $('<form method="get" action="'+url+'"></form>');
        var nameInput=$('<input>');
        nameInput.attr('type','text');
        nameInput.attr('name','id');
        nameInput.attr('value',nowDatabaseId);
        exportForm.append(nameInput);
        var nameInput2=$('<input>');
        nameInput2.attr('type','text');
        nameInput2.attr('name','tableName');
        nameInput2.attr('value',table);
        exportForm.append(nameInput2);
        $(document.body).append(exportForm);
        exportForm.submit();
        exportForm.remove();
    }

    function queryTableInfo(table) {
        var d = {};
        d['id']=nowDatabaseId;
        d['tableName']=table;
        const func = (data) => {
            if(data&&data.code==="000000"){
                var exportForm = $('<form id="'+table+'" class="form-horizontal">' +
                        '<div class="row">' +
                        '<label class="col-md-2 text-center">字段名</label>' +
                        '<label class="col-md-1 text-center">字段类型</label>' +
                        '<label class="col-md-2 text-center">字段注释</label>' +
                        '<label class="col-md-1 text-center">字典值</label>' +
                        '<label class="col-md-6 text-center">自定义</label>' +
                        '</div></form>');
                $.each(data.result, function () {
                    var divTmp=$('<div class="row"></div>' );
                    var html='<input class="col-md-2 text-center" readonly name="columnName" title="'+this.columnName+'" value="'+this.columnName+'">';
                    html+='<input class="col-md-1 text-center" readonly name="columnType" title="'+this.columnType+'" value="'+this.columnType+'">';
                    html+='<input class="col-md-2 text-center" readonly name="columnRemark" title="'+this.columnRemark+'" value="'+this.columnRemark+'">';
                    html+='<select id="'+table+'_'+this.columnName+'_dictType'+'" class="col-md-1" name="dictType">'+getOptionHtml(this.columnType)+'</select>';
                    html+='<input  id="'+table+'_'+this.columnName+'_dictValue'+'" class="col-md-6" name="dictValue">';
                    divTmp.append(html);
                    exportForm.append(divTmp);
                });
                var numberHtml='<div class="row"><div class="col-md-8"></div><label class="text-center" for="number">生成数量 : </label>' +
                        '<input type="number" id="'+table+'_number" name="number" placeholder="number" required value="1">' +
                        '</div>';
                exportForm.append(numberHtml);
                $('.modal-body').append(exportForm);
                $('#tableModel').modal('show');
            }else if(data){
                alert(data.msg);
            }
        };
        getReq("/data/queryTableInfo",d,func);
    }

    function getOptionHtml(columnType) {
        var optionsHtml = '';
        $.each(dataEnumArr, function () {
            if (columnType === 'timestamp' || columnType.indexOf("time") >= 0 || columnType.indexOf("date") >= 0) {
                if (this.enumCode.indexOf("FUNC") == 0 || this.enumCode === "SQL_VALUE") {
                    if (this.enumCode === "FUNC_TIMESTAMP") {
                        optionsHtml += '<option value="' + this.enumCode + '" selected>' + this.enumName + '</option>';
                    } else {
                        optionsHtml += '<option value="' + this.enumCode + '">' + this.enumName + '</option>';
                    }
                }
                if (this.enumCode === "SQL_VALUE") {
                    optionsHtml += '<option value="' + this.enumCode + '">' + this.enumName + '</option>';
                }
            } else if (columnType.indexOf('int') >= 0 || columnType === 'numeric') {
                if (this.enumCode.indexOf("NUMBER") == 0 || this.enumCode === "SQL_VALUE") {
                    if (this.enumCode === "NUMBER_RONDOM") {
                        optionsHtml += '<option value="' + this.enumCode + '" selected>' + this.enumName + '</option>';
                    } else {
                        optionsHtml += '<option value="' + this.enumCode + '">' + this.enumName + '</option>';
                    }
                }
            } else {
                if (this.enumCode.indexOf("TEXT") == 0 || this.enumCode === "SQL_VALUE") {
                    optionsHtml += '<option value="' + this.enumCode + '">' + this.enumName + '</option>';
                }
            }
        });
        return optionsHtml;
    }

    function listDatabase(){
        const func = (data) => {
            if(data&&data.code==="000000"){
                $.each(data.result, function () {
                    var optionsHtml='<option value="'+this.id+'">'+this.comment+'  (  '+this.url+'/'+this.database+'   -   '+this.username+"/"+this.pwd +'  )</option>';
                    $("#selData").append(optionsHtml);
                });
            }else{
                alert(data.msg);
            }
        };
        getReq("/data/listDatabase",null,func);
    }

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

    function openModel(){
        refreshTable();
        $('#exampleModalScrollable').modal('show');
    }
    function closeModel(){
        $("#dataBaseTable").bootstrapTable('destroy');
        $('#exampleModalScrollable').modal('hide');
    }

    function closeTableModel(){
        $('#tableModel').modal('hide');
    }

    function openServiceModel(){
        $('#serviceModel').modal('show');
    }

    function closeServiceModel(){
        $('#serviceModel').modal('hide');
        document.getElementById("addServiceForm").reset();
        $("#dataBaseTable").bootstrapTable('refresh');
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
    function getReq(url,data,func) {
        $.ajax({
            type: "get",
            url: url,
            contentType: "application/json;charset=utf-8",
            data:data,
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
</script>
</html>