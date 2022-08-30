
package org.springcrazy.common.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 生成订单号 当前用户id+毫秒数
     */
    public static String getOrderNum() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int a = (int)(Math.random() * 9000.0D) + 1000;
        System.out.println(a);
        Date date = new Date();
        String str = sdf.format(date);
        String[] split = str.split("-");
        String s = split[0] + split[1] + split[2];
        String[] split1 = s.split(" ");
        String s1 = split1[0] + split1[1];
        String[] split2 = s1.split(":");
        return split2[0] + split2[1] + split2[2] + a;
    }

}
