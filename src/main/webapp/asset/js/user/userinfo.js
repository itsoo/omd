;!function () {
    layui.form.verify({
        pass: [/^(\d|\w){6,12}$/, '密码格式不正确']
    });

    layui.form.on('submit(save)', function (data) {
        console.log(data.field);
        
        layer.msg("编辑成功！", {icon: 6, time: 1000, anim: 4}, function () {
            parent.layer.close(parent.layer.getFrameIndex(window.name));
        });
        return false;
    });
}();
