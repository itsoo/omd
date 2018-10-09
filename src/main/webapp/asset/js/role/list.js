;!function () {
    var table = layui.table;
    // 查询 URL
    var url = basePath + '/role/list';
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
            {field: 'name', title: '名称', width: 200},
            {field: 'describe', title: '描述', width: 200},
            {title: '操作', width: 200, align: 'center', toolbar: '#toolbar'}
        ]]
    });
    // 表格工具条
    table.on('tool(grid)', function (obj) {
        if (obj.event == 'edit') {
            openAjax('编辑角色', ['400px', '300px'], basePath + '/role/info', {id: obj.data.id});
        } else if (obj.event == 'del') {
            confirm('确定要删除吗？', function () {
                axios(basePath + '/role/delete', obj.data);
            });
        }
    });
    // 添加角色
    $('#add').click(function () {
        openAjax('添加角色', ['400px', '300px'], basePath + '/role/info');
    });
    // 批量删除
    $('#batchDel').click(function () {
        var ids = '';
        if ((ids = getChecked())) {
            confirm('确定要批量删除吗？', function () {
                axios(basePath + '/role/delete', {id: ids});
            });
        }
    });
    // 刷新缓存
    $('#refresh').click(function () {
        confirm('确定要刷新缓存吗？', function () {
            axios(basePath + '/role/refreshAuthc');
        });
    });
}();
