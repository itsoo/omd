package com.omron.omd.model;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.omron.omd.util.ArraysUtil;
import com.omron.omd.util.DataUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PageInfo
 *
 * @author zxy
 */
@Data
public class PageInfo {

    private Map<String, Object> model;
    private Integer code = 0;
    private String msg = "";
    private List<Record> data;
    private Integer page = 1;
    private Integer limit = 10;
    private Integer count = 0;

    /**
     * 初始化 PageInfo 结果集
     *
     * @param page 分页数据
     * @return PageInfo
     */
    public PageInfo initPageInfo(Page<Record> page) {
        setData(page.getList());
        setCount(page.getTotalRow());
        return this;
    }

    /**
     * 设置查询模型
     *
     * @param model Map
     */
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    /**
     * 设置查询模型
     *
     * @param paraMap Map
     * @param ignore  String...
     */
    public void setModel(Map<String, String[]> paraMap, String... ignore) {
        Map<String, Object> model = new HashMap<>(16);
        String key;
        String[] value;
        // 迭代放入元素到 model
        for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
            key = entry.getKey();
            // 过滤掉忽略的 key
            if (ArraysUtil.contains(ignore, key)) {
                continue;
            }
            // 过滤掉 SQL 注入字符
            value = DataUtil.convertSqlKeyword(entry.getValue());
            if (value.length == 0) {
                model.put(key, null);
            } else if (value.length == 1) {
                model.put(key, value[0]);
            } else {
                model.put(key, value);
            }
        }
        setModel(model);
    }
}
