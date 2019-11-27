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
    <div class="modal fade bd-example-modal-xl" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
        <div class="modal-dialog" style="width:90%" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalScrollableTitle">表造数</h5>
                    <button type="button" class="close" onclick="closeModel()" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModel()">Close</button>
                    <button type="button" class="btn btn-primary" onclick="createDatas()">创建数据</button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    //当前连接池信息
    var nowDB={};
    var nowTableName;
    //保存打开模态框form信息
    var formArr={};
    //form具体信息
    var formDataArr={};
    var dataEnumArr;
    var jdbcConf;
    listDataEnum();
    //关闭时触发
    function closeModel(){
        var t = $('#'+nowTableName).serializeArray();
        var prefix="";
        var tableData=[];
        $.each(t, function () {
            var tmpArr={};
            if(this.name==='columnName'){
                prefix=nowTableName+'_'+this.value;
            }else if(this.name==='dictType'){
                tmpArr['id']=prefix+'_dictType';
                tmpArr['value']=this.value;
                tableData.push(tmpArr);
            }else if(this.name==='dictValue'){
                tmpArr['id']=prefix+'_dictValue';
                tmpArr['value']=this.value;
                tableData.push(tmpArr);
            }else if(this.name==='number'){
                tmpArr['id']=nowTableName+'_number';
                tmpArr['value']=this.value;
                tableData.push(tmpArr);
            }
        });
        formDataArr[nowTableName]=tableData;
        $('#exampleModalScrollable').modal('hide');
    }
    function listDataEnum() {
        $.ajax({
            type: "get",
            url: "/listDataEnum",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success:function (data) {
                if(data&&data.code==="000000"){
                    dataEnumArr=data.result;
                    // optionsHtml='';
                    // $.each(data.result, function () {
                    //     optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                    // });
                }
            }
        });
    }

    function addFunctionAlty(value, row, index) {
        return [
            '<button id="getJava" type="button" class="btn btn-default">获取java文件</button>',
            '<button id="createDatas" type="button" class="btn btn-default">造数</button>',
        ].join('');
    }
    window.operateEvents = {
        'click #getJava': function (e, value, row, index) {
            getJavaFiles(row.tableName);
        },
        'click #createDatas': function (e, value, row, index) {
            queryTableInfo(row.tableName);
        }
    }

    function getOptionHtml(columnType){
        var optionsHtml='';
        $.each(dataEnumArr, function () {
            if(columnType==='timestamp'||columnType.indexOf("time")>=0||columnType.indexOf("date")>=0){
                if(this.enumCode.indexOf("FUNC")==0||this.enumCode==="SQL_VALUE"){
                    if(this.enumCode==="FUNC_TIMESTAMP"){
                        optionsHtml+='<option value="'+this.enumCode+'" selected>'+this.enumName+'</option>';
                    }else{
                        optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                    }
                }
                if(this.enumCode==="SQL_VALUE"){
                    optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                }
            }else if(columnType.indexOf('int')>=0||columnType==='numeric'){
                if(this.enumCode.indexOf("NUMBER")==0||this.enumCode==="SQL_VALUE"){
                    if(this.enumCode==="NUMBER_RONDOM"){
                        optionsHtml+='<option value="'+this.enumCode+'" selected>'+this.enumName+'</option>';
                    }else{
                        optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                    }
                }
            }else{
                if(this.enumCode.indexOf("TEXT")==0||this.enumCode==="SQL_VALUE"){
                    optionsHtml+='<option value="'+this.enumCode+'">'+this.enumName+'</option>';
                }
            }
        });
        return optionsHtml;
    }
    
    function createDatas() {
        var request={};
        var t = $('#'+nowTableName).serializeArray();
        var columnName=[];
        var columnType=[];
        var dictType=[];
        var dictValue=[];
        $.each(t, function () {
            if(this.name==='columnName'){
                columnName.push(this.value);
            }else if(this.name==='columnType'){
                columnType.push(this.value);
            }else if(this.name==='dictType'){
                dictType.push(this.value);
            }else if(this.name==='dictValue'){
                dictValue.push(this.value);
            }else if(this.name==='number'){
                request['number']=this.value;
            }
        });
        $.each(jdbcConf, function () {
            request[this.name] = this.value;
        });
        request['columnName']=columnName;
        request['columnType']=columnType;
        request['dictType']=dictType;
        request['dictValue']=dictValue;
        request['tableName']=nowTableName;
        $.ajax({
            type: "post",
            url: "/getDataSql",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(request),
            dataType: "json",
            success:function (data) {
                console.log(data);
                if(data&&data.code==="000000"){
                    closeModel();
                }else if(data){
                    alert(data.msg);
                }
            }
        });
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
                    jdbcConf=t;
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
                    events: operateEvents,//给按钮注册事件
                    formatter: addFunctionAlty//表格中增加按钮
                },
            ]
        });
    }

    function getJavaFiles(table){
        var url = "/getJavaFiles";
        nowDB['tableName']=table;
        var exportForm = $('<form method="post" action="'+url+'"></form>');
        for(var i in nowDB){
            var nameInput=$('<input>');
            nameInput.attr('type','text');
            nameInput.attr('name',i);
            nameInput.attr('value',nowDB[i]);
            exportForm.append(nameInput);
        }
        $(document.body).append(exportForm);
        exportForm.submit();
        exportForm.remove();
    }

    function queryTableInfo(table) {
        nowDB['tableName']=table;
        if(nowTableName===table){
            $('#exampleModalScrollable').modal('show');
            return;
        }else{
            if(nowTableName){
                formArr[nowTableName]='<form id="'+nowTableName+'" class="form-horizontal">'+$('#'+nowTableName).html()+'</form>';
                $('#'+nowTableName).remove();
            }
            nowTableName=table;
        }
        if(formArr[table]){
            $('.modal-body').append(formArr[table]);
            if(formDataArr[table]){
                $.each(formDataArr[table], function () {
                    $('#'+this.id).val(this.value);
                });
            }
            $('#exampleModalScrollable').modal('show');
        }else{
            $.ajax({
                type: "post",
                url: "/queryTableInfo",
                contentType: "application/json;charset=utf-8",
                data:JSON.stringify(nowDB),
                dataType: "json",
                success:function (data) {
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
                        $('#exampleModalScrollable').modal('show');
                    }else if(data){
                        alert(data.msg);
                    }
                }
            });
        }
    }

</script>
</html>