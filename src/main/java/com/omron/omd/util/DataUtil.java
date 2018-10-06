package com.omron.omd.util;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 数据处理工具类
 *
 * @author zxy
 */
public class DataUtil {

    /**
     * 添加方法钩子
     *
     * @param record 元数据
     * @return boolean
     */
    public static boolean isAdd(Record record) {
        if (StrKit.isBlank(record.getStr("id"))) {
            record.remove("id");
            return true;
        }
        setNumId(record);
        return false;
    }

    /**
     * 带移除参数的添加方法钩子
     *
     * @param record     元数据
     * @param removePara 移除的参数
     * @return boolean
     */
    public static boolean isAdd(Record record, String... removePara) {
        for (int i = 0, len = removePara == null ? 0 : removePara.length; i < len; i++) {
            record.remove(removePara[i]);
        }
        return isAdd(record);
    }

    /**
     * 设置数字类型 id
     *
     * @param record 元数据
     */
    public static void setNumId(Record record) {
        record.set("id", Integer.parseInt(record.getStr("id")));
    }

    /**
     * 转换 SQL 注入类符号
     *
     * @param array 入参
     */
    public static String[] convertSqlKeyword(String[] array) {
        for (int i = 0, len = array == null ? 0 : array.length; i < len; i++) {
            array[i] = sqliteEscape(array[i]);
        }
        return array;
    }

    /**
     * SQL 转义字符
     * <p>Sqlite 数据库未实现 JDBC 标准预编译，需要手动过滤注入字符</p>
     * <p>此做法性能较差，但目前没有更好的解决方案</p>
     *
     * @param keyWord 关键字
     * @return String
     */
    public static String sqliteEscape(String keyWord) {
        // 此处正则实现更简洁，但回溯过多影响性能
        return keyWord.replace("/", "//")
                .replace("'", "''")
                .replace("[", "/[")
                .replace("]", "/]")
                .replace("%", "/%")
                .replace("&", "/&")
                .replace("_", "/_")
                .replace("(", "/(")
                .replace(")", "/)");
    }
}
