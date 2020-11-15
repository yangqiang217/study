package com.yq.imageviewer.utils.des;

import java.nio.charset.Charset;

public class DesUtil {

    private static final String  SKEY    = "abcdefgh";
    private static final Charset CHARSET = Charset.forName("gb2312");

    /**
     * 加密
     *
     * @param srcStr
     * @return
     */
    public static String encrypt(String srcStr) {
        byte[] src = srcStr.getBytes(CHARSET);
        byte[] buf = Des.encrypt(src, SKEY);
        return Des.parseByte2HexStr(buf);
    }

    /**
     * 解密
     *
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static String decrypt(String hexStr) throws Exception {
        byte[] src = Des.parseHexStr2Byte(hexStr);
        byte[] buf = Des.decrypt(src, SKEY);
        return new String(buf, CHARSET);
    }
}
