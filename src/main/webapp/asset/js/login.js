;!function () {
    // 登录
    layui.form.on('submit(login)', function (data) {
        layer.msg("登录中...", {icon: 6, time: 60000, anim: 4});
        // ajax 登录动作
        $.ajax({
            url: basePath + '/login',
            data: data.field,
            type: 'POST',
            dataType: 'json',
            success: function (res) {
                if (res.success) {
                    location.href = basePath + '/';
                } else {
                    layer.msg(res.message, {icon: 5, anim: 6});
                    var $vcode = $('.vcode-img');
                    var src = $vcode.attr('src');
                    $vcode.attr('src', src + '?' + new Date().getTime());
                }
            }
        });
        return false;
    });
    // 刷新验证码
    $('.vcode-img').on('click', function () {
        var src = $(this).attr('src');
        $(this).attr('src', src + '?' + new Date().getTime());
    });
}();
