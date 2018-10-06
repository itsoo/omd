package com.omron.omd.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;
import com.omron.omd.util.HttpUtil;

import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 *
 * @author zxy
 */
public class LoginInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        HttpSession session = controller.getSession();
        if (session == null) {
            HttpUtil.renderNotLogon(controller);
        } else {
            Record user = (Record) session.getAttribute(AppConst.SESSION);
            // 已登录放行请求
            if (user != null) {
                inv.invoke();
            }
            // 未登录转发请求
            else {
                HttpUtil.renderNotLogon(controller);
            }
        }
    }
}
