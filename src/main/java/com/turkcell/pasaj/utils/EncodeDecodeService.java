package com.turkcell.pasaj.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class EncodeDecodeService {
    private static final String IV_PARAMETER = "KAFEIN_SECRET__1";
    private static final String SECRET_KEY = "KAFEIN_SECRET__2";

    private static EncodeDecodeService singletonObject = null;

    private IvParameterSpec ivParameterSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    private EncodeDecodeService() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        ivParameterSpec = new IvParameterSpec(IV_PARAMETER.getBytes("UTF-8"));
        secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }

    public static EncodeDecodeService getInstance() throws Exception {
        if (singletonObject == null) {
            singletonObject = new EncodeDecodeService();
        }

        return singletonObject;
    }

    /**
     * Encodes the string with this internal algorithm.
     *
     * @param valueToEncode string object to be encoded.
     * @return returns encrypted string.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public String encodeBase64(String valueToEncode) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(valueToEncode.getBytes());
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * Decodes this string with the internal algorithm. The passed argument should be encrypted using
     * {@link #encodeBase64(String) encodeBase64} method of this class.
     *
     * @param valueToDecode Encoded string that was encrypted using {@link #encodeBase64(String) encodeBase64} method.
     * @return decoded string.
     */
    public String decodeBase64(String valueToDecode) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(valueToDecode));
        return new String(decryptedBytes);
    }
}
