package com.omron.omd.controller;

import com.omron.omd.controller.base.BaseController;
import com.omron.omd.service.RoleService;

/**
 * RoleController
 *
 * @author zxy
 */
public class RoleController extends BaseController {

    private RoleService service = RoleService.BIZ;

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
     * 查询权限列表
     */
    public void findRoleAuthc() {
        renderJson(service.findRoleAuthc(getPageInfo()));
    }

    /**
     * 重置权限缓存
     */
    public void refreshAuthc() {
        renderJson(service.refreshAuthc());
    }
}
