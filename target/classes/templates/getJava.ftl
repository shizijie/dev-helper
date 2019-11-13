<!DOCTYPE html>
<html lang="en">
<head>
    <script src="webjars/jquery/3.4.1/jquery.min.js"></script>
    <script src="webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="webjars/bootstrap/3.3.5/css/bootstrap.min.css" />
    <meta charset="UTF-8">
    <title>GET JAVA FILES</title>
</head>

<body>
    <h1 class="text-center ">GET JAVA FILES</h1>
    <form class="form-horizontal">
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
        <div class="form-group">
            <label class="col-sm-2 control-label" for="tableName">tableName（数据库表名）</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="tableName" name="tableName" placeholder="tableName"  required>
            </div>
        </div>
        <button onclick="test()" type="button" class="btn btn-primary col-md-offset-5">生成文件</button>
        <a class="btn btn-info col-md-offset-1" href="/index">返回主页</a>
    </form>

</body>
<script>
    function test() {
        var d = {};
        var t = $('form').serializeArray();
        $.each(t, function () {
            d[this.name] = this.value;
        });
        // $.ajax({
        //     type: "post",
        //     url: "/getTableColumns",
        //     contentType: "application/json;charset=utf-8",
        //     data:JSON.stringify(d),
        //     dataType: "json",
        //     success:function (data) {
        //         console.log(data)
        //         if(data){
        //             alert(data.msg);
        //         }
        //     }
        // });

        var url = "/getTableColumns";
        var exportForm = $('<form method="post" action="'+url+'"></form>');
        $.each(t, function () {
            var nameInput=$('<input>');
            nameInput.attr('type','text');
            nameInput.attr('name',this.name);
            nameInput.attr('value',this.value);
            exportForm.append(nameInput);
        });
        $(document.body).append(exportForm);
        exportForm.submit();
        exportForm.remove();
    }
</script>
</html>