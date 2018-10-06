// 暴露全局变量
var basePath = $('#basePath').attr('href');

// 获取选择 ID
function getChecked() {
    var checks = layui.table.checkStatus('grid'),
        rows = checks.data.length;
    if (rows > 0) {
        var ids = '';
        for (var i = 0; i < rows; i++) {
            ids += checks.data[i].id + ',';
        }
        return ids;
    }
    parent.layer.msg('请至少选择 1 条数据', {offset: 't', skin: 'top-message-danger', anim: 6});
    return false;
}

// 过滤 HTML 标签及空白
function removeHTMLTag(str) {
    if (!str) {
        return '';
    }
    str = typeof str === 'object' ? $(str).html() : str;
    return str.replace(/<\/?[^>]*>/g, '') // 去除 HTML 标签
        .replace(/[ | ]*\n/g, '') // 去除行尾空白换行
        .replace(/ /ig, ''); // 去掉空格
}

// URL 为空
function urlIsEmpty(url) {
    return !url || url == '' || url == 'javascript:;';
}

// Ajax 请求
function ajax(url, data, async, dataType, success, error) {
    if (arguments.length == 0) {
        throw 'error: arguments is empty';
    }
    var i, item, params = [];
    // 缓存 arguments
    for (i = 0; i < arguments.length; i++) {
        params[i] = arguments[i];
    }
    // 设置默认参数
    data = {};
    async = true;
    dataType = 'json';
    // 设置请求参数
    for (i = 1; i < params.length; i++) {
        item = params[i];
        if (typeof item == 'object') {
            data = item;
        } else if (typeof item == 'boolean') {
            async = item;
        } else if (typeof item == 'string') {
            dataType = item;
        } else if (typeof item == 'function') {
            if (!success) {
                success = item;
            } else {
                error = item;
            }
        }
    }
    $.ajax({
        url: url,
        data: data,
        type: 'POST',
        dataType: dataType,
        async: async,
        success: success,
        error: error
    });
}

// Ajax 异步提交
function axios(url, data, elem, success, error) {
    if (arguments.length == 0) {
        throw 'error: arguments is empty';
    }
    var layer = parent.layer || layui.layer;
    if (typeof elem == 'function') {
        error = success;
        success = elem;
        elem = undefined;
    }
    if (typeof data == 'function') {
        error = elem;
        success = data;
        data = undefined;
    } else if (typeof data == 'object') {
        if (typeof elem == 'undefined' && data.innerHTML) {
            elem = data;
            data = undefined;
        } else if (data.field && data.elem) {
            elem = data.elem;
            data = data.field;
        }
    }
    // 默认成功回调
    success = success || function (res) {
            if (JSON.stringify(res) === 'true' || res.success) {
                layer.msg('操作成功', {icon: 1, time: 1000, anim: 4}, function () {
                    parent.layer.close(layerIndex(elem));
                    var $btn = $('.layui-laypage-btn')[0];
                    $btn && $btn.click();
                });
            } else {
                var msg = res.message || '操作失败';
                layer.alert(msg, {icon: 2});
            }
        };
    // 默认异常回调
    error = error || function () {
        };
    ajax(url, data, success, error);
}

// 取消关闭弹框
$('body').on('click', '[lay-close="cancel"]', function () {
    parent.layer.close(layerIndex(this));
});

// 获取窗口 index
function layerIndex(obj) {
    if (window.name || window.name == '') {
        return $(obj).parents('.layui-layer').attr('times');
    }
    return parent.layer.getFrameIndex(window.name);
}

// 确认对话框
function confirm(message, callback) {
    parent.layer.confirm(message, {icon: 0, anim: 6}, callback);
}

// 单页面弹框
function openAjax(title, area, url, data, callback) {
    data = data || {};
    callback = callback || function () {
            $('.layui-laypage-btn')[0].click();
        };
    ajax(url, data, 'html', function (res) {
        $(res).find('.sub-form').attr('data-index');
        parent.layer.open({
            title: title,
            type: 1,
            area: area,
            resize: false,
            content: res,
            end: callback
        });
    });
}

// 扩展 load 方法设置请求头
function load(elem, url, data) {
    if (!elem) {
        throw 'elem is must be';
    }
    url = url || '/';
    url = url.indexOf(basePath) == -1 ? basePath + '/' + url : url;
    url = url.replace(/\/\//g, '/');
    $.ajax({
        url: url,
        headers: {
            'Ajax-Type': 'html'
        },
        type: data ? 'POST' : 'GET',
        data: data ? data : {},
        dataType: 'html',
        success: function (res) {
            $(elem).html(res);
        }
    });
}
