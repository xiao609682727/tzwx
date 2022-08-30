package org.springcrazy.common.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    public static String toString(BigDecimal de){
        return de.setScale(2, RoundingMode.FLOOR).toString();
    }

    public static String toInt(BigDecimal de){
        return de.setScale(0, RoundingMode.FLOOR).toString();
    }

    public static void main(String[] args) {

        System.out.println(toInt(new BigDecimal("1.1").multiply(new BigDecimal(100))));
    }
}
