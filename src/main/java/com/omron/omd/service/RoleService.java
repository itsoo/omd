package com.omron.omd.service;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.omron.omd.model.PageInfo;
import com.omron.omd.util.ArraysUtil;
import com.omron.omd.util.DataUtil;

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
        }
        // 修改角色
        else {
            Db.update(TABLE_NAME, role);
        }
        // 更新权限
        int roleId = role.getInt("id");
        Db.deleteById("t_role_authc", "role_id", roleId);
        String[] authcs = ArraysUtil.getArray(authc);
        int id;
        for (String idStr : authcs) {
            id = Integer.parseInt(idStr);
            Db.save("t_role_authc", new Record().set("authc_id", id).set("role_id", roleId));
        }
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
        int id;
        for (String idStr : ArraysUtil.getArray(ids)) {
            id = Integer.parseInt(idStr);
            // 删除角色
            Db.deleteById(TABLE_NAME, id);
            // 删除角色关联权限
            Db.deleteById("t_role_authc", "role_id", id);
        }
        return true;
    }

    /**
     * 查询权限列表
     *
     * @param pageInfo Page对象
     * @return List<Record>
     */
    public List<Record> findRoleAuth(PageInfo pageInfo) {
        List<Record> list = Db.find(Db.getSqlPara("role.findRoleAuth", pageInfo.getModel()));
        // 字符串 true 转 boolean 类型
        for (Record r : list) {
            r.set("checked", Boolean.parseBoolean(r.getStr("checked")));
        }
        return list;
    }
}
