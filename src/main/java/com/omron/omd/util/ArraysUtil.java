package com.omron.omd.util;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 数组工具类
 *
 * @author zxy
 */
public class ArraysUtil {

    public static final String SP = ",";

    /**
     * 判断数组中包含元素
     *
     * @param array 元数据
     * @param value 值
     * @return boolean
     */
    public static boolean contains(String[] array, String value) {
        for (int i = 0, len = array == null ? 0 : array.length; i < len; i++) {
            if (array[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合转字符串（按分隔符）
     *
     * @param list 元数据
     * @param key  键
     * @param sp   分隔符
     * @return String
     */
    public static String joinAs(List list, String key, String sp) {
        List target = new ArrayList();
        Object item;
        for (int i = 0, len = list == null ? 0 : list.size(); i < len; i++) {
            item = list.get(i);
            if (item instanceof Record) {
                target.add(((Record) item).get(key));
            } else if (item instanceof Map) {
                target.add(((Map) item).get(key));
            }
        }
        return joinAs(target, sp);
    }

    /**
     * 集合转字符串
     *
     * @param list 元数据
     * @return String
     */
    public static String joinAs(List list) {
        return joinAs(list, SP);
    }

    /**
     * 集合转字符串（按分隔符）
     *
     * @param list 元数据
     * @param sp   分隔符
     * @return String
     */
    public static String joinAs(List list, String sp) {
        StringBuilder sbr = new StringBuilder();
        for (int i = 0, len = list == null ? 0 : list.size(); i < len; i++) {
            sbr.append(list.get(i).toString());
            if (i < len - 1) {
                sbr.append(sp);
            }
        }
        return sbr.toString();
    }

    /**
     * 数组转字符串（按分隔符）
     *
     * @param arr 元数据
     * @param sp  分隔符
     * @return String
     */
    public static <T> String joinAs(T[] arr, String sp) {
        return joinAs(Arrays.asList(arr), sp);
    }

    /**
     * 得到非空元素的数组
     *
     * @param str 元数据
     * @return String[]
     */
    public static String[] getArray(String str) {
        String[] array = str.split(SP);
        List<String> arrayList = new ArrayList<>(array.length);
        for (String s : array) {
            if (StrKit.isBlank(s)) {
                continue;
            }
            arrayList.add(s);
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }
}
