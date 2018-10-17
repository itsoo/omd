;!function () {
    var layer = top.layer || layui.layer;
    var form = layui.form;
    // 验证密码
    form.verify({
        pass: [/^(\d|\w){6,12}$/, '密码格式不正确']
    });
    // 提交
    form.on('submit(save)', function (data) {
        var json = data.field;
        if (json.newPass && !json.oldPass) {
            layer.msg('请输入旧密码', {icon: 5, anim: 6});
            return false;
        } else if (json.newPass != json.verPass) {
            layer.msg('两次输入的密码不一致', {icon: 5, anim: 6});
            return false;
        }
        // 验证通过 ajax 提交
        axios(basePath + '/user/userinfo/save', json, function (res) {
            if (JSON.stringify(res) === 'true' || res.success) {
                layer.msg('操作成功', {icon: 1, time: 1000, anim: 4});
                $('[name="oldPass"]').val('');
                $('[name="newPass"]').val('');
                $('[name="verPass"]').val('');
            } else {
                layer.alert('旧密码或其它校验失败', {icon: 2});
            }
        });
        return false;
    });
}();
