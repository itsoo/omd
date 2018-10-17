package com.omron.omd.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.omron.omd.common.AppConst;
import com.omron.omd.model.PageInfo;
import com.omron.omd.util.ArraysUtil;
import com.omron.omd.util.AuthcUtil;
import com.omron.omd.util.DataUtil;
import com.omron.omd.util.EhCacheUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 角色业务层
 *
 * @author zxy
 */
public class RoleService {

    public static final RoleService BIZ = new RoleService();

    private static final String TABLE_NAME = "t_role";

    /**
     * 查询列表
     *
     * @param pageInfo Page对象
     * @return PageInfo
     */
    public PageInfo list(PageInfo pageInfo) {
        SqlPara sql = Db.getSqlPara("role.list", pageInfo.getModel());
        Page<Record> page = Db.paginate(pageInfo.getPage(), pageInfo.getLimit(), sql);
        return pageInfo.initPageInfo(page);
    }

    /**
     * 查询单条
     *
     * @param pageInfo Page对象
     * @return Map
     */
    public Map<String, Object> info(PageInfo pageInfo) {
        Map<String, Object> model = pageInfo.getModel();
        Record role = Db.findById(TABLE_NAME, model.get("id"));
        if (role != null) {
            model.putAll(role.getColumns());
        }
        return model;
    }

    /**
     * 保存数据
     *
     * @param pageInfo Page对象
     * @return boolean
     */
    public boolean save(PageInfo pageInfo) {
        Record role = new Record().setColumns(pageInfo.getModel());
        String authc = role.getStr("authc");
        role.remove("authc");
        // 添加角色
        if (DataUtil.isAdd(role)) {
            Db.save(TABLE_NAME, role);
        } else {
            Db.update(TABLE_NAME, role);
        }
        // 更新权限
        int roleId = role.getInt("id");
        Db.deleteById("t_role_authc", "role_id", roleId);
        ArraysUtil.getArray(authc).forEach(id -> Db.save("t_role_authc", new Record().set("authc_id", id).set("role_id", roleId)));
        return true;
    }

    /**
     * 删除数据
     *
     * @param pageInfo Page对象
     */
    public boolean delete(PageInfo pageInfo) {
        String ids = (String) pageInfo.getModel().get("id");
        if (StrKit.isBlank(ids)) {
            return false;
        }
        ArraysUtil.getArray(ids).forEach(id -> {
            // 删除角色
            Db.deleteById(TABLE_NAME, id);
            // 删除角色关联权限
            Db.deleteById("t_role_authc", "role_id", id);
        });
        return true;
    }

    /**
     * 查询权限列表
     *
     * @param pageInfo Page对象
     * @return List<Record>
     */
    public List<Record> findRoleAuthc(PageInfo pageInfo) {
        List<Record> list = Db.find(Db.getSqlPara("role.findRoleAuthc", pageInfo.getModel()));
        // 字符串 true 转 boolean 类型
        list.forEach(r -> r.set("checked", Boolean.parseBoolean(r.getStr("checked"))));
        return list;
    }

    /**
     * 重置权限缓存
     */
    public boolean refreshAuthc() {
        // 全部权限
        List<Record> authcList = Db.find(Db.getSql("role.allAuthc"));
        // home 权限
        Iterator<Record> it = authcList.iterator();
        Record authc;
        while (it.hasNext()) {
            authc = it.next();
            if ("/home".equals(authc.getStr("url"))) {
                EhCacheUtil.put("", AppConst.HOME_AUTHC, authc);
                it.remove();
                break;
            }
        }
        // 寻找根节点
        List<Record> rootList = new ArrayList<>();
        authcList.forEach(r -> {
            if (StrKit.isBlank(r.getStr("pid"))) {
                rootList.add(r);
            }
        });
        rootList.forEach(r -> r.set("subList", AuthcUtil.getChildList(r.getStr("id"), authcList)));
        // 配置缓存
        EhCacheUtil.put("", AppConst.ALL_AUTHC, rootList);
        // 全部用户相关
        List<Record> list = Db.find(Db.getSql("role.allRoleAuthc"));
        // 配置缓存
        String idAuthc = AppConst.ID_AUTHC;
        String keyAuthc = AppConst.KEY_AUTHC;
        list.forEach(r -> AuthcUtil.setAuthcList(r.getStr("user_id"), idAuthc, keyAuthc, r));
        return true;
    }
}
