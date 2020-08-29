function checkDatabase(){
    var d = {};
    d['id']=$("#selData").val();
    nowDatabaseId=$("#selData").val();
    const func = (data) => {
        if(data&&data.code==="000000"){
            $("#selData").attr("disabled",true);
            $("#btnSure").attr("disabled",true);
            $("#btnManager").attr("disabled",true);
            showTable(d);
        }else{
            alert(data.msg);
        }
    };
    getReq("/data/checkDatabase",d,func);
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

function closeTableModel(){
    $('#tableModel').modal('hide');
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