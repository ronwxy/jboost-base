package cn.jboost.base.common.util.security;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 非对称加密RSA工具类
 * @Author ronwxy
 * @Date 2020/7/27 8:54
 * @Version 1.0
 */
public class EncryptUtil {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";  //兼容安卓sdk的加密算法

    /**
     * 返回公钥私钥对
     * @return Pair<RSAPublicKey, RSAPrivateKey>
     * @throws NoSuchAlgorithmException
     */
    public static Pair<Key, Key> generateRSAKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return new ImmutablePair<>(keyPair.getPublic(), keyPair.getPrivate());
    }


    /**
     * 使用公钥加密
     * @param source 待加密的原始字符串
     * @param publicKey base64编码的公钥
     * @return 加密后base64编码的字符串
     * @throws Exception
     */
    public static String encrypt(String source, String publicKey) throws Exception {
        X509EncodedKeySpec ks = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = kf.generatePublic(ks);
        //RSA加密
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(source.getBytes()));
    }

    /**
     * 使用私钥解密
     * @param encryptStr 加密后base64编码的字符串
     * @param privateKey base64编码的私钥
     * @return 解密后的原始字符串
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = factory.generatePrivate(pkcs8EncodedKeySpec);
        //RSA加密
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptStr)));
    }

    public static void main(String[] args) throws Exception {
//        Pair<Key, Key> rsaKeys = generateRSAKey();
//        System.out.println("公钥：" + Base64.getEncoder().encodeToString(rsaKeys.getLeft().getEncoded()));
//        System.out.println("私钥：" + Base64.getEncoder().encodeToString(rsaKeys.getRight().getEncoded()));

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG19fp/8lEREHl7e8G/Q0aMhNigu5iX3YuiuTgKncF/Dw1cYY6DbwaZILFdN9HfPDwuea4FQpB7EIgbX0IdbESMoAZs75Qa2wsh5zGy+UQy6tlVaCa883qgLfpsIY0G0kDFKPC7csKV4ygix0yJhmxysStZUpVdCZ1m2Bx4aojUQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIbX1+n/yUREQeXt7wb9DRoyE2KC7mJfdi6K5OAqdwX8PDVxhjoNvBpkgsV030d88PC55rgVCkHsQiBtfQh1sRIygBmzvlBrbCyHnMbL5RDLq2VVoJrzzeqAt+mwhjQbSQMUo8LtywpXjKCLHTImGbHKxK1lSlV0JnWbYHHhqiNRAgMBAAECgYAdI/o6TJffU+z11rSoSvmQ5q9/w5Hm2oB80HFj7GlqdrdASXdbhmpXPc9xAq/aECs1qscskwdzda5YubCiqmSInJOQyBuONrMrqROta8EbEkCvh2pnkIDQ38xCuXONSMkGCR5Q67Xt1lUHPg4zri5icKMhh4Zw64joVidEZZib3QJBAMuPYpf66mx0GNvo671GqYbwKY9jrzaaZ4kJQqKNrsEon7uxl4ulZ+dIz03HBQ4F2OTuPpipgUrz010YXGV5veMCQQCplKIlpPQwgaQkUA2bFxhYa3QF0/VI02YJNnqO+A5EH5PzA2Bci+BcE8ft5+daTduN8gM9WJwqCEkmMcTj1SA7AkEAuOoh+Em5VpgIVQy7yY89RRU5y8YUTpfo4bWF0MDANTvADvL+5Z52SsX7e34Fe32YUC6Usn2YtAvWv8T3ej8jXQJAc1kYmgaD2uKz5cdWGeIxuyML/lLYizrQMwANiWYNPiVEm432Y7z5VWhXG+ocIf7HktvFfFrjjmuoZgSAaoCztQJAawMxDiGcMKKkO6jCJ002+1+Cr6NdIPNL+X/ER2aCg3YFmiD2HkEjvOwVe/eaMYqJA56fMwpJ3Qn7z+RhYSNoOQ==";

        String encryptData = encrypt("00E2691F056D#1.0#1596012043744", publicKey);
//
//        System.out.println("加密数据：" + encryptData);
        System.out.println("解密数据：" + decrypt("B463yVdizQ1MVWEq6Nx/nuOK3yLtFp4EUa5xgUtO3xOe4QlwwSutDKzEKFMi5r5lWyU5rQMUAX2HFr3ybO3fjM2I6Oxx7XGevP908jHx+uHuYUC78/ttcJ6HwZkfic6l3NiNtzeF2tkblSOkUPg/oWIkx9T/SHF3UCaCN5TuMIw=", privateKey));

    }
}
