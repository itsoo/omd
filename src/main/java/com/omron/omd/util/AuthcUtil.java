package com.omron.omd.util;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限工具类
 *
 * @author zxy
 */
public final class AuthcUtil {

    private static final String BASE_PATH = "/";

    /**
     * 得到首页权限
     *
     * @param userId userId
     * @return Record
     */
    public static Record getHomeUrl(String userId) {
        Set<String> authcKeySet = EhCacheUtil.get(userId, AppConst.KEY_AUTHC);
        if (authcKeySet.contains("/home")) {
            return EhCacheUtil.get("", AppConst.HOME_AUTHC);
        }
        return null;
    }

    /**
     * 设置权限集合
     *
     * @param userId   userId
     * @param idAuthc  idAuthc
     * @param keyAuthc keyAuthc
     * @param r        Record
     */
    public static void setAuthcList(String userId, String idAuthc, String keyAuthc, Record r) {
        Set<Integer> authcIdSet;
        Set<String> authcKeySet;
        if (EhCacheUtil.has(userId, idAuthc)) {
            // 更新权限 id
            authcIdSet = EhCacheUtil.get(userId, idAuthc);
            authcIdSet.add(r.getInt("authc_id"));
            // 更新权限 key
            authcKeySet = EhCacheUtil.get(userId, keyAuthc);
            authcKeySet.add(r.getStr("url"));
            authcKeySet.add(BASE_PATH);
        } else {
            // 初始化权限 id
            authcIdSet = new HashSet<>();
            authcIdSet.add(r.getInt("authc_id"));
            EhCacheUtil.put(userId, idAuthc, authcIdSet);
            // 初始化权限 key
            authcKeySet = new HashSet<>();
            authcKeySet.add(r.getStr("url"));
            authcKeySet.add(BASE_PATH);
            EhCacheUtil.put(userId, keyAuthc, authcKeySet);
        }
    }

    /**
     * 获取权限集合
     *
     * @param userId   userId
     * @param allAuthc allAuthc
     */
    public static List<Record> getAuthcList(String userId, List<Record> allAuthc) {
        List<Record> authcList = new ArrayList<>(allAuthc);
        // 用户权限
        Set<Integer> authcIdSet = EhCacheUtil.get(userId, AppConst.ID_AUTHC);
        // 过滤权限菜单
        authcList.removeIf(r -> "1".equals(r.getStr("type")) || !authcIdSet.contains(r.getInt("id")));
        return authcList;
    }

    /**
     * 获取权限集合
     *
     * @param id       menuId
     * @param rootMenu rootMenuList
     * @return List
     */
    public static List<Record> getChildList(String id, List<Record> rootMenu) {
        List<Record> childList = new ArrayList<>();
        // 设置一级菜单的子菜单
        rootMenu.forEach(menu -> {
            String pid = menu.getStr("pid");
            if (StrKit.notBlank(pid) && pid.equals(id)) {
                childList.add(menu);
            }
        });
        // 递归子菜单的子菜单
        childList.forEach(menu -> {
            if (StrKit.isBlank(menu.getStr("url"))) {
                menu.set("subList", getChildList(menu.getStr("id"), rootMenu));
            }
        });
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /**
     * 判断是否有用权限
     *
     * @param userId    userId
     * @param actionKey actionKey
     * @return boolean
     */
    public static boolean hasAuthc(String userId, String actionKey) {
        // 获取用户权限缓存
        Set<String> authcKeySet = EhCacheUtil.get(userId, AppConst.KEY_AUTHC);
        // 无权限
        return authcKeySet != null && authcKeySet.contains(actionKey);
    }
}
