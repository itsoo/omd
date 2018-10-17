package com.omron.omd.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;
import com.omron.omd.util.AuthcUtil;
import com.omron.omd.util.HttpUtil;

/**
 * 权限拦截器
 *
 * @author zxy
 */
public class AuthcInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        // userId
        String userId = ((Record) controller.getSession().getAttribute(AppConst.SESSION)).getStr("id");
        // 无权限
        if (!AuthcUtil.hasAuthc(userId, inv.getActionKey())) {
            HttpUtil.renderNotAuthc(controller);
            return;
        }
        inv.invoke();
    }
}
