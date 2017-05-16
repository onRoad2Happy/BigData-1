package com.wyd.BigData.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.regex.Pattern;

public class StringUtil implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8829731057831041459L;

    /**
     * 判断字符串是否为数字
     * 
     * @param str
     * @return
     */
    public final static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 过滤掉超过3个字节的UTF8字符
     * @param text
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String filterOffUtf8Mb4(String text) {
        try {
            byte[] bytes = text.getBytes("utf-8");
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256; // 去掉符号位
                if (((b >> 5) ^ 0x6) == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                } else if (((b >> 4) ^ 0xE) == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                } else if (((b >> 3) ^ 0x1E) == 0) {
                    i += 4;
                } else if (((b >> 2) ^ 0x3E) == 0) {
                    i += 5;
                } else if (((b >> 1) ^ 0x7E) == 0) {
                    i += 6;
                } else {
                    buffer.put(bytes[i++]);
                }
            }
            buffer.flip();
            return new String(buffer.array(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
