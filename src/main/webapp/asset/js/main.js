;!function () {
    // 导航收起/展开功能
    var adminTag = '.layui-layout-admin', minTag = 'layui-nav-min';
    $('.nav-switch').find('a').click(function () {
        var $this = $(this);

        if (!$this.hasClass('minac')) { // 置为收起状态
            $(adminTag).addClass(minTag);
            $this.addClass('minac');
        } else { // 置为展开状态
            $(adminTag).removeClass(minTag);
            $this.removeClass('minac');
        }
    });

    // 鼠标悬停导航提示
    var sideTag = '.layui-nav-min .layui-side .p-item>a';
    $('.layui-layout-body').on('mouseenter', sideTag, function () {
        layer.tips(removeHTMLTag(this), this, {skin: 'side-tips'});
        return false;
    }).on('mouseleave', sideTag, function () {
        layer.closeAll('tips');
        return false;
    });

    // 监听 hash 改变
    $.history.init(function (hash) {
        hash = renderBread(hash);

        // 重置菜单状态
        var c = 'layui-this';
        $('.' + c).removeClass(c);

        // 限制选中的标签
        var $li = $('._nav[path="' + hash + '"]').parent().eq(0).addClass(c);
        if ($li[0].tagName.toUpperCase() == 'LI') {
            $li.parents('li').addClass('layui-nav-itemed');
        }

        // 加载内容页
        load('#frame', basePath + hash.replace(/^#!/, ''));
    });

    // 改变 hash 值
    $('body').on('click', '._nav', function () {
        location.hash = renderBread($(this).attr('path'));
        return false;
    });

    // 缓存 hash
    var map = {};

    // 渲染面包屑导航
    function renderBread(hash) {
        // 默认值
        var homePath = '#!/home', homeText = '首页';

        // 处理 hash 值
        hash = !hash || hash == '' ? homePath : hash;
        hash = !/^#/.test(hash) ? '#' + hash : hash;

        // 保存 hash 缓存
        if (!map || $.isEmptyObject(map)) {
            map = {'': homeText};
            $('._nav').each(function () {
                var $this = $(this);
                map[$this.attr('path')] = removeHTMLTag($this.html());
            });
            map[homePath] = homeText;
        }

        // 组织面包屑
        var sep = '<span lay-separator>/</span>';
        var html = hash == homePath
            ? ''
            : '<a class="_nav" path="' + homePath + '">' + homeText + '</a>' + sep;

        // 遍历并找到上级导航
        $('._nav[path="' + hash + '"]').parents('.layui-nav-item').each(function () {
            var $pa = $(this).find('a[href="javascript:;"]').eq(0);
            var hash = $pa.attr('path');

            if (!urlIsEmpty(hash)) {
                html += '<a class="_nav" path="' + hash + '">' + map[hash] + '</a>' + sep;
            } else {
                if ($pa.length > 0) {
                    html += '<a href="javascript:;">' + removeHTMLTag($pa.html()) + '</a>' + sep;
                }
            }
        });
        $('#bread').html(html + '<a class="_nav" path="' + hash + '">' + map[hash] + '</a>');

        // 返回 hash 值
        return hash;
    }
}();
