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
    <title>部署</title>
</head>

<body>
<h1 class="text-center ">服务部署</h1>
<br/>
<button onclick="openModel()" type="button" class="btn btn-info center-block" >服务器信息管理</button>
<a class="btn btn-info col-md-offset-1" href="/index">返回主页</a>
<button type="button" class="btn btn-primary" onclick="openServiceModel()">添加服务</button>
<!-- 模态框 -->
<div class="modal fade bd-example-modal-xl" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
    <div class="modal-dialog" style="width:90%" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalScrollableTitle">服务器列表</h5>
                <button type="button" class="close" onclick="closeModel()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <table id="table"></table>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" onclick="closeModel()">Close</button>
                <button type="button" class="btn btn-primary" onclick="openServiceModel()">添加服务</button>
            </div>
        </div>
    </div>
</div>
<!-- 模态框2 -->
<div class="modal fade bd-example-modal-xl" id="serviceModel" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
    <div class="modal-dialog" style="width:90%" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalScrollableTitle">添加服务</h5>
                <button type="button" class="close" onclick="closeServiceModel()" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form class="form-horizontal" id="addServiceForm">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="comment">注释</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="comment" name="comment" placeholder="例：开发服务器" required>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label" for="ip">ip</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="ip" name="ip" placeholder="ip" required>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label" for="username">用户名</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="username" name="username" placeholder="username"  required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="password">密码</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="password" name="password" placeholder="password"  required>
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
</body>
<script>
    function addService(){
        var d = {};
        var t = $('#addServiceForm').serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        const func = (data) => {
            if(data&&data.code==="000000"){
                $("#table").bootstrapTable('refresh');
                closeServiceModel();
            }else{
                alert(data.msg);
            }
        };
        postReq("/addService",d,func)
    }

    function openModel(){
        refreshTable();
        $('#exampleModalScrollable').modal('show');
    }

    function refreshTable(){
        $('#table').bootstrapTable({
            url: 'listSSH',
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
                    field: 'ip',
                    title: 'ip地址'
                },
                {
                    field: 'username',
                    title: '用户名'
                },
                {
                    field: 'password',
                    title: '密码'
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
    window.operateEvents = {
        'click #getJava': function (e, value, row, index) {
            getJavaFiles(row.tableName);
        },
        'click #createDatas': function (e, value, row, index) {
            queryTableInfo(row.tableName);
        }
    }
    function addFunctionAlty(value, row, index) {
        return [
            '<button id="getJava" type="button" class="btn btn-default">修改</button>',
            '<button id="createDatas" type="button" class="btn btn-default">删除</button>',
        ].join('');
    }

    function closeModel(){
        $('#exampleModalScrollable').modal('hide');
    }

    function openServiceModel(){
        $('#serviceModel').modal('show');
    }

    function closeServiceModel(){
        $('#serviceModel').modal('hide');
    }

    listSSH();

    
    function listSSH(){
        const func = (data) => {
            if(data&&data.code==="000000"){
                if(data.result){
                    console.log(data.result)
                }
            }
        };
        getReq("/listSSH",func);
    }


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
</script>
</html>