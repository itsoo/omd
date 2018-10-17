package com.omron.omd.service;

import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.omron.omd.common.AppConst;
import com.omron.omd.model.PageInfo;
import com.omron.omd.util.ArraysUtil;
import com.omron.omd.util.DataUtil;
import com.omron.omd.util.MD5Util;

import java.util.*;

/**
 * 用户业务层
 *
 * @author zxy
 */
public class UserService {

    public static final UserService BIZ = new UserService();

    private static final String TABLE_NAME = "t_user";

    /**
     * 查询列表
     *
     * @param pageInfo Page对象
     * @return PageInfo
     */
    public PageInfo list(PageInfo pageInfo) {
        SqlPara sql = Db.getSqlPara("user.list", pageInfo.getModel());
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
        Object id = model.get("id");
        if (id != null) {
            // 用户信息
            Record user = Db.findById(TABLE_NAME, id);
            if (user != null) {
                model.putAll(user.getColumns());
            }
            // 用户角色
            List<Record> role = Db.find(Db.getSqlPara("user.findUserRole", id));
            model.put("role", ArraysUtil.joinAs(role, "role_id", ArraysUtil.SP));
        }
        // 全部角色
        model.put("roles", Db.find(Db.getSqlPara("role.list")));
        return model;
    }

    /**
     * 保存数据
     *
     * @param pageInfo Page对象
     * @return boolean
     */
    public boolean save(PageInfo pageInfo) {
        Record user = new Record().setColumns(pageInfo.getModel());
        // 获取 role
        List<String> roles = ArraysUtil.getArray(user.getStr("role"));
        // 添加用户
        if (DataUtil.isAdd(user, "role")) {
            MD5Util.encrypt(user.set("password", AppConst.INIT_PASSWORD));
            Db.save(TABLE_NAME, user);
        } else {
            Db.update(TABLE_NAME, user);
        }
        // 删除用户角色
        int userId = user.getInt("id");
        Db.deleteById("t_user_role", "user_id", userId);
        // 添加用户角色
        int length = roles.size();
        List<Record> userRole = new ArrayList<>(length);
        roles.forEach(roleId -> userRole.add(new Record().set("user_id", userId).set("role_id", roleId)));
        Db.batchSave("t_user_role", userRole, length);
        return true;
    }

    /**
     * 删除数据
     *
     * @param pageInfo Page对象
     * @return boolean
     */
    public boolean delete(PageInfo pageInfo) {
        String ids = (String) pageInfo.getModel().get("id");
        if (StrKit.isBlank(ids)) {
            return false;
        }
        ArraysUtil.getArray(ids).forEach(id -> {
            // 删除用户角色
            Db.deleteById("t_user_role", "user_id", id);
            // 删除用户
            Db.deleteById(TABLE_NAME, id);
        });
        return true;
    }

    /**
     * 登录查询
     *
     * @param map map
     * @return Map {success: boolean, message: object}
     */
    public Map<String, Object> logon(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("success", false);
        // 密码加密
        MD5Util.encrypt(map);
        // 数据库查询验证登录
        Record user = Db.findFirst(Db.getSqlPara("user.logon", map));
        if (user != null) {
            // 登录成功
            if ("0".equals(user.getStr("state"))) {
                result.put("success", true);
                result.put("message", user);
            } else {
                result.put("message", "账号被禁用");
            }
        } else {
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    /**
     * 重置密码
     *
     * @param pageInfo Page对象
     * @return boolean
     */
    public boolean repass(PageInfo pageInfo) {
        String ids = (String) pageInfo.getModel().get("id");
        if (StrKit.isBlank(ids)) {
            return false;
        }
        String password = (String) pageInfo.getModel().get("password");
        if (StrKit.isBlank(password)) {
            password = AppConst.INIT_PASSWORD;
        }
        // 查询用户信息
        List<Record> list = Db.find(Db.getSqlPara("user.findUserList", Kv.by("idArray", ArraysUtil.getArray(ids))));
        // 重置用户密码
        String repass = password;
        list.forEach(user -> {
            MD5Util.encrypt(user.set("password", repass));
            Db.update(TABLE_NAME, user);
        });
        return true;
    }

    /**
     * 用户信息
     *
     * @param pageInfo Page对象
     * @return Map
     */
    public Map<String, Object> userinfo(PageInfo pageInfo) {
        Map<String, Object> userinfo = info(pageInfo);
        List<Record> roles = new ArrayList<>();
        String role = (String) userinfo.remove("role");
        // 处理用户拥有角色
        if (StrKit.notBlank(role)) {
            List<String> roleIds = ArraysUtil.getArray(role);
            ((List<Record>) userinfo.get("roles")).forEach(r -> {
                for (Iterator<String> it = roleIds.iterator(); it.hasNext(); ) {
                    if (it.next().equals(r.getStr("id"))) {
                        roles.add(r);
                        it.remove();
                        break;
                    }
                }
            });
        }
        userinfo.put("roles", roles);
        return userinfo;
    }

    /**
     * 保存用户信息
     *
     * @param pageInfo Page对象
     * @return boolean
     */
    public boolean userInfoSave(PageInfo pageInfo) {
        Record user = new Record().setColumns(pageInfo.getModel()).remove("verPass");
        String oldPass = user.getStr("oldPass");
        String newPass = user.getStr("newPass");
        Record r = Db.findById(TABLE_NAME, Integer.parseInt(user.getStr("userId")));
        String password = r.getStr("password");
        String username = r.getStr("username");
        if (MD5Util.verify(username + oldPass, password)) {
            MD5Util.encrypt(r.set("password", newPass));
            Db.update(TABLE_NAME, r.set("nickname", user.getStr("nickname")));
            return true;
        }
        return false;
    }
}
