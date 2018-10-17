package com.omron.omd.controller.base;

import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.Base64Kit;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.common.AppConst;
import com.omron.omd.model.PageInfo;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseController
 *
 * @author zxy
 */
public class BaseController extends Controller {

    private static final String PAGE = "page";
    private static final String LIMIT = "limit";

    /**
     * 获取分页信息
     *
     * @return PageInfo
     */
    protected PageInfo getPageInfo() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPage(getParaToInt(PAGE));
        pageInfo.setLimit(getParaToInt(LIMIT));
        Map<String, String[]> paraMap = getParaMap();
        pageInfo.setModel(paraMap, PAGE, LIMIT);
        return pageInfo;
    }

    /**
     * 获取用户 id
     *
     * @return String
     */
    protected String getUserId() {
        return ((Record) getSession().getAttribute(AppConst.SESSION)).getStr("id");
    }

    /**
     * 移除 key
     *
     * @param keys key[]
     * @return PageInfo
     */
    protected Map<String, Object> removeKey(String... keys) {
        PageInfo pageInfo = getPageInfo();
        Map<String, Object> map = pageInfo.getModel();
        for (int i = 0, len = keys == null ? 0 : keys.length; i < len; i++) {
            map.remove(keys[i]);
        }
        return map;
    }

    /**
     * 获取请求 RequestBody
     *
     * @return Map
     */
    protected Map<String, Object> getRequestMap() {
        String para;
        try {
            para = IOUtils.toString(getRequest().getInputStream(), Const.DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
        return FastJson.getJson().parse(para, HashMap.class);
    }

    /**
     * 获取请求 RequestBody
     *
     * @param convertKey 转换目标
     * @return Map
     */
    protected Map<String, Object> getRequestMap(String convertKey) {
        Map<String, Object> map = getRequestMap();
        map.put(convertKey, Base64Kit.decodeToStr((String) map.get(convertKey)));
        return map;
    }

    /**
     * 获取请求 RequestBody
     *
     * @return List
     */
    protected List getRequestList() {
        String para;
        try {
            para = IOUtils.toString(getRequest().getInputStream(), Const.DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
        return FastJson.getJson().parse(para, ArrayList.class);
    }
}
