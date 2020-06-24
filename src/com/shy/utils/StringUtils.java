package com.shy.utils;

import java.util.Map;

/**
 * @author 石皓岩
 * @create 2020-06-24 9:49
 * 描述：
 */
public class StringUtils {

    public static String transformPlaceholders(String sql, Map<Integer, String> map) {
        int index = 1;
        while (sql.contains("#{")) {
            int start = sql.indexOf("#{");
            int end = sql.indexOf("}");
            // 这个是#{username}
            String substring = sql.substring(start, end + 1);
            map.put(index++,substring);
            // 开始替换字符串
            sql = sql.replace(substring, "?");
        }
        return sql;
    }


}
