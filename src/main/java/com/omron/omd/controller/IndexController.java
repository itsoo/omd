package com.omron.omd.controller;

import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;
import com.omron.omd.controller.base.BaseController;
import com.omron.omd.service.UserService;
import com.omron.omd.util.AuthcUtil;
import com.omron.omd.util.EhCacheUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * IndexController
 *
 * @author zxy
 */
public class IndexController extends BaseController {

    private UserService service = UserService.BIZ;

    /**
     * 首页
     */
    public void index() {
        // 获取权限菜单列表
        List<Record> allAuthc = EhCacheUtil.get("", AppConst.ALL_AUTHC);
        Record user = (Record) getSession().getAttribute(AppConst.SESSION);
        String userId = user.getStr("id");
        // 用户菜单权限
        setAttr("home", AuthcUtil.getHomeUrl(userId));
        setAttr("menuList", AuthcUtil.getAuthcList(userId, allAuthc));
        render("index.html");
    }

    /**
     * 登入
     */
    @Clear
    public void login() {
        // 验证码正确
        if (validateCaptcha("vcode")) {
            Map<String, Object> result = service.logon(removeKey("vcode"));
            if ((boolean) result.get("success")) {
                getSession().setAttribute(AppConst.SESSION, result.remove("message"));
            }
            setAttrs(result);
        } else {
            setAttr("success", false);
            setAttr("message", "验证码错误");
        }
        renderJson();
    }

    /**
     * 登出
     */
    @Clear
    public void logout() {
        getSession().removeAttribute(AppConst.SESSION);
        render("login.html");
    }

    /**
     * 验证码
     */
    @Clear
    public void vcode() throws IOException {
        renderCaptcha();
    }
}
