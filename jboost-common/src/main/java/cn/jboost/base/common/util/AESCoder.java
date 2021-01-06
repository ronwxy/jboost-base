package cn.jboost.base.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES Coder<br/>
 * secret key length:   128bit, default:    128 bit<br/>
 * mode:    ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding: Nopadding/PKCS5Padding/ISO10126Padding/
 *
 * @author Aub
 */
@Slf4j
public class AESCoder {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 初始化密钥
     *
     * @return byte[] 密钥
     * @throws Exception
     */
    public static byte[] initSecretKey(String key) {
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象  
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
            return new byte[0];
        }
        //初始化此密钥生成器，使其具有确定的密钥大小  
        //AES 要求密钥长度为 128  
        // kg.init(128);  
        byte[] keyStart = key.getBytes();
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keyStart);
            kg.init(128, secureRandom);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new byte[0];
        }
        //生成一个密钥  
        SecretKey secretKey = kg.generateKey();
        return keyStart;

        // return secretKey.getEncoded();  
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return 密钥
     */
    public static Key toKey(byte[] key) {
        //生成密钥  
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  二进制密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }


    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        //还原密钥  
        Key k = toKey(key);
        return encrypt(data, k, cipherAlgorithm);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为加密模式  
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //执行操作  
        return (cipher.doFinal(data));
    }


    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws Exception {
        //还原密钥  
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     * @return byte[]   解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws Exception {
        //实例化  
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        //使用密钥初始化，设置为解密模式  
        cipher.init(Cipher.DECRYPT_MODE, key);
        //执行操作  
        // data = Base64.getDecoder().decode(data);
        return cipher.doFinal(data);
    }

    private static String showByteArray(byte[] data) {
        if (null == data) {
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for (byte b : data) {
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
//        return sb.toString();  
        return Hex.encodeHexStr(data);
    }

//    public static void main(String[] args) throws Exception {
//        byte[] key = initSecretKey();
//        System.out.println("key：" + showByteArray(key));
//
//        Key k = toKey(key);
//
//        String data = "Slj8SRphtsO+XPx/ZO6Kiw==";
//        // byte[] dataBytes = Hex.decodeHex(data.toCharArray());
//        byte[] dataBytes = data.getBytes();
//        System.out.println("加密前数据: string:" + data);
//        System.out.println("加密前数据: byte[]:" + showByteArray(dataBytes));
//        System.out.println();
//        byte[] encryptData = encrypt(dataBytes, k);
//        System.out.println("加密后数据: byte[]:" + showByteArray(encryptData));
//        System.out.println("加密后数据: hexStr:" + Hex.encodeHexStr(encryptData));
//        key = initSecretKey();
//        System.out.println("key：" + showByteArray(key));
//        k = toKey(key);
//        System.out.println();
//        byte[] decryptData = decrypt(encryptData, k);
//        System.out.println("解密后数据: byte[]:" + showByteArray(decryptData));
//        System.out.println("解密后数据: string:" + new String(decryptData));
//
//    }

    /**
     * reference apache commons <a
     * href="http://commons.apache.org/codec/">http://commons.apache.org/codec/</a>
     *
     * @author Aub
     */
    static public class Hex {

        /**
         * 用于建立十六进制字符的输出的小写字符数组
         */
        private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        /**
         * 用于建立十六进制字符的输出的大写字符数组
         */
        private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        /**
         * 将字节数组转换为十六进制字符数组
         *
         * @param data byte[]
         * @return 十六进制char[]
         */
        public static char[] encodeHex(byte[] data) {
            return encodeHex(data, true);
        }

        /**
         * 将字节数组转换为十六进制字符数组
         *
         * @param data        byte[]
         * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
         * @return 十六进制char[]
         */
        public static char[] encodeHex(byte[] data, boolean toLowerCase) {
            return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
        }

        /**
         * 将字节数组转换为十六进制字符数组
         *
         * @param data     byte[]
         * @param toDigits 用于控制输出的char[]
         * @return 十六进制char[]
         */
        protected static char[] encodeHex(byte[] data, char[] toDigits) {
            int l = data.length;
            char[] out = new char[l << 1];
            // two characters form the hex value.  
            for (int i = 0, j = 0; i < l; i++) {
                out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
                out[j++] = toDigits[0x0F & data[i]];
            }
            return out;
        }

        /**
         * 将字节数组转换为十六进制字符串
         *
         * @param data byte[]
         * @return 十六进制String
         */
        public static String encodeHexStr(byte[] data) {
            return encodeHexStr(data, true);
        }

        /**
         * 将字节数组转换为十六进制字符串
         *
         * @param data        byte[]
         * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
         * @return 十六进制String
         */
        public static String encodeHexStr(byte[] data, boolean toLowerCase) {
            return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
        }

        /**
         * 将字节数组转换为十六进制字符串
         *
         * @param data     byte[]
         * @param toDigits 用于控制输出的char[]
         * @return 十六进制String
         */
        protected static String encodeHexStr(byte[] data, char[] toDigits) {
            return new String(encodeHex(data, toDigits));
        }

        /**
         * 将十六进制字符数组转换为字节数组
         *
         * @param data 十六进制char[]
         * @return byte[]
         * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
         */
        public static byte[] decodeHex(char[] data) {

            int len = data.length;

            if ((len & 0x01) != 0) {
                throw new RuntimeException("Odd number of characters.");
            }

            byte[] out = new byte[len >> 1];

            // two characters form the hex value.  
            for (int i = 0, j = 0; j < len; i++) {
                int f = toDigit(data[j], j) << 4;
                j++;
                f = f | toDigit(data[j], j);
                j++;
                out[i] = (byte) (f & 0xFF);
            }

            return out;
        }

        /**
         * 将十六进制字符转换成一个整数
         *
         * @param ch    十六进制char
         * @param index 十六进制字符在字符数组中的位置
         * @return 一个整数
         * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
         */
        protected static int toDigit(char ch, int index) {
            int digit = Character.digit(ch, 16);
            if (digit == -1) {
                throw new RuntimeException("Illegal hexadecimal character " + ch
                        + " at index " + index);
            }
            return digit;
        }

    }
}  
