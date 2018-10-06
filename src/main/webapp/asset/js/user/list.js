;!function () {
    var layer = parent.layer || layui.layer,
        table = layui.table;
    // 查询 URL
    var url = basePath + '/user/list';
    // 查询按钮
    layui.form.on('submit(search)', function (data) {
        table.reload('grid', {
            url: url,
            where: data.field
        });
        return false;
    });
    // 渲染表格
    table.render({
        elem: '#grid',
        url: url,
        page: true,
        cols: [[
            {field: 'id', type: 'checkbox'},
            {field: 'username', title: '用户名', width: 200},
            {field: 'nickname', title: '真实姓名', width: 200},
            {
                field: 'status', title: '状态', width: 100, align: 'center',
                templet: function (d) {
                    return d.state == '0'
                        ? '<span style="color:#5FB878">正常</span>'
                        : '<span style="color:#FF5722">禁用</span>'
                }
            },
            {title: '操作', width: 200, align: 'center', toolbar: '#toolbar'}
        ]]
    });
    // 表格工具条
    table.on('tool(grid)', function (obj) {
        if (obj.event == 'edit') {
            openAjax('编辑用户', ['400px', '350px'], basePath + '/user/info', {id: obj.data.id});
        } else if (obj.event == 'del') {
            confirm('确定要删除吗？', function () {
                axios(basePath + '/user/delete', obj.data);
            });
        }
    });
    // 添加角色
    $('#add').click(function () {
        openAjax('添加用户', ['400px', '380px'], basePath + '/user/info');
    });
    // 重置密码
    $('#resetPass').click(function () {
        var ids = getChecked();
        if (ids) {
            layer.prompt({
                formType: 0,
                title: '新密码'
            }, function (value, index) {
                axios(basePath + '/user/repass', {id: ids, password: value});
                layer.close(index);
            });
        }
    });
    // 批量删除
    $('#batchDel').click(function () {
        var ids = getChecked();
        if (ids) {
            confirm('确定要批量删除吗？', function () {
                axios(basePath + '/user/delete', {id: ids});
            });
        }
    });
}();
