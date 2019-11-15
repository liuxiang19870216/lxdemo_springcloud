package lxdemo.springcloud.systemAdmin.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {


    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String str2Md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            String md5Str = new String(encodeHex(bytes));
            return md5Str;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("System doesn't support your encoding.");
        }
    }

    /**
     *
     * @param bytes
     * @return
     */
    private static char[] encodeHex(byte[] bytes) {
        int len = bytes.length;
        char[] hex = new char[len << 1];

        for (int i = 0, j = 0; i < len; i++) {
            hex[j++] = DIGITS[(0xF0 & bytes[i]) >>> 4];
            hex[j++] = DIGITS[0x0F & bytes[i]];
        }
        return hex;
    }


}