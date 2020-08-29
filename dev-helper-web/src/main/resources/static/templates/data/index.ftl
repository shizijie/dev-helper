<!DOCTYPE html>
<html lang="en">
<head>
    <script src="webjars/jquery/3.4.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="webjars/bootstrap-table/1.9.1/bootstrap-table.min.js"></script>
    <script src="webjars/bootstrap-table/1.9.1/locale/bootstrap-table-zh-CN.js"></script>
    <script src="static/js/common.js"></script>
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
            <button id="btnManager" class="btn btn-primary col-sm-1" onclick="openModel()" type="button">管理</button>
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
<script src="static/templates/data/js/database.js"></script>
<script src="static/templates/data/js/listTable.js"></script>
<script>
    listDatabase();
    listDataEnum();
    var nowDatabaseId;
    var dataEnumArr;
    function listDatabase(){
        const func = (data) => {
            if(data&&data.code==="000000"){
                $("#selData").empty();
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

</script>
</html>