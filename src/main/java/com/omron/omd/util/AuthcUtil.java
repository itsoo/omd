package com.omron.omd.util;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 权限工具类
 *
 * @author zxy
 */
public class AuthcUtil {

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
     * 设置公共权限
     *
     * @param authcKeySet
     */
    public static void setPublicAuth(Set<String> authcKeySet) {
        // 首页
        authcKeySet.add("/");
    }

    /**
     * 设置权限集合
     *
     * @param userId   userId
     * @param allAuthc allAuthc
     */
    public static List<Record> getAuthcList(String userId, List<Record> allAuthc) {
        List<Record> authcList = new ArrayList<>(allAuthc);
        // 用户权限
        Set<Integer> authcIdSet = EhCacheUtil.get(userId, AppConst.ID_AUTHC);
        // 遍历权限过滤菜单
        for (Iterator<Record> iterator = authcList.iterator(); iterator.hasNext(); ) {
            Record r = iterator.next();
            // 0 菜单，1 按钮
            if ("1".equals(r.getStr("type"))) {
                iterator.remove();
            } else if (!authcIdSet.contains(r.getInt("id"))) {
                iterator.remove();
            }
        }
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
        // 子菜单
        String pid;
        for (Record menu : rootMenu) {
            pid = menu.getStr("pid");
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (StrKit.notBlank(pid)) {
                if (pid.equals(id)) {
                    childList.add(menu);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (Record menu : childList) {
            // 没有url子菜单还有子菜单
            if (StrKit.isBlank(menu.getStr("url"))) {
                // 递归
                menu.set("subList", getChildList(menu.getStr("id"), rootMenu));
            }
        }
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
