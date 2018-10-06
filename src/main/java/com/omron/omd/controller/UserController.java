package com.omron.omd.controller;

import com.jfinal.core.ActionKey;
import com.omron.omd.controller.base.BaseController;
import com.omron.omd.service.UserService;

/**
 * UserController
 *
 * @author zxy
 */
public class UserController extends BaseController {

    private UserService service = UserService.BIZ;

    /**
     * 列表页
     */
    public void index() {
        render("list.html");
    }

    /**
     * 查询列表
     */
    public void list() {
        renderJson(service.list(getPageInfo()));
    }

    /**
     * 查询单条
     */
    public void info() {
        setAttrs(service.info(getPageInfo()));
        render("info.html");
    }

    /**
     * 保存数据
     */
    public void save() {
        renderJson(service.save(getPageInfo()));
    }

    /**
     * 删除数据
     */
    public void delete() {
        renderJson(service.delete(getPageInfo()));
    }

    /**
     * 个人中心
     */
    public void userinfo() {
        render("userinfo.html");
    }

    /**
     * 保存用户信息
     */
    @ActionKey("/user/userinfo/save")
    public void userInfoSave() {

    }

    /**
     * 重置密码
     */
    public void repass() {
        renderJson(service.repass(getPageInfo()));
    }
}
