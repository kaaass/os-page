package net.kaaass.ospage.util;

public class StringUtils {

    public static String padBinary(int value, int len) {
        return String.format("%" + len + "s", Integer.toBinaryString(value)).replace(" ", "0");
    }
}
