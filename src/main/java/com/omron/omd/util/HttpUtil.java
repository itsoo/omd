package com.omron.omd.util;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.omron.omd.common.AppConst;

import javax.servlet.http.HttpServletRequest;

/**
 * Http 请求工具类
 *
 * @author zxy
 */
public class HttpUtil {

    private static final String REQUEST_HEADER = "x-requested-with";
    private static final String AJAX_TYPE = "Ajax-Type";

    /**
     * 钩子方法：判断是否为 ajax 请求
     *
     * @param controller Controller
     * @return boolean
     */
    public static boolean isAjaxReq(Controller controller) {
        return isAjaxReq(controller.getRequest());
    }

    /**
     * 钩子方法：判断是否为 ajax 请求
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isAjaxReq(HttpServletRequest request) {
        boolean result = false;
        //判断是否为ajax请求，默认不是
        if (!StrKit.isBlank(request.getHeader(REQUEST_HEADER))
                && "XMLHttpRequest".equals(request.getHeader(REQUEST_HEADER))) {
            result = true;
        }
        return result;
    }

    /**
     * 无权限渲染
     *
     * @param controller Controller
     */
    public static void renderNotAuthc(Controller controller) {
        String viewPath = AppConst.BASE_VIEW_PATH + "/error";
        // 是 ajax 请求
        if (isAjaxReq(controller)) {
            // load 页面
            if ("html".equals(controller.getHeader(AJAX_TYPE))) {
                controller.render(viewPath + "/load/401.html");
            }
            // json ajax
            else {
                controller.renderJson(Kv.by("success", false).set("message", "很抱歉，您没有访问权限"));
            }
        }
        // 非 ajax 请求
        else {
            controller.render(viewPath + "/401.html");
        }
    }

    /**
     * 未登录渲染
     *
     * @param controller Controller
     */
    public static void renderNotLogon(Controller controller) {
        // 是 ajax 请求
        if (isAjaxReq(controller)) {
            // load 页面
            if ("html".equals(controller.getHeader(AJAX_TYPE))) {
                String basePath = JFinal.me().getServletContext().getContextPath();
                controller.renderHtml("<script>location.href='" + basePath + "/'</script>");
            }
            // json ajax
            else {
                controller.renderJson(Kv.by("success", false).set("message", "您未登录，或登录已超时"));
            }
        }
        // 非 ajax 请求
        else {
            controller.forwardAction("/logout");
        }
    }
}
