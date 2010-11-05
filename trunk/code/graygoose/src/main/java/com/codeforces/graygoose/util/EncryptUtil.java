package com.codeforces.graygoose.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtil {

    private static final byte[] RANDOM_SEED = {0x1a, 0x03, 0x52, 0x63, 0x04, 0x1b, 0x0c, 0x0d};
    private static final String XFORM = "DES/ECB/PKCS5Padding";

    private static final SecretKey SECRET_KEY;

    private static byte[] encrypt(byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance(XFORM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException("Encryption exception.", e);
        }
    }

    private static byte[] decrypt(byte[] input) {
        try {
            Cipher cipher = Cipher.getInstance(XFORM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw new RuntimeException("Decryption exception.", e);
        }
    }

    public static String encrypt(String plainText) {
        try {
            byte[] input = IOUtils.toByteArray(IOUtils.toInputStream(plainText));
            byte[] output = encrypt(input);
            return Hex.encodeHexString(output);
        } catch (IOException e) {
            return null;
        }
    }

    public static String decrypt(String cypherText) {
        try {
            byte[] input = Hex.decodeHex(cypherText.toCharArray());
            byte[] output = decrypt(input);
            return new String(output);
        } catch (DecoderException e) {
            return null;
        }
    }

    private EncryptUtil() {
    }

    static {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't nitialize a secret key generator.", e);
        }
        keyGenerator.init(56, new SecureRandom(RANDOM_SEED));   // 56 is the keysize. Fixed for DES.
        SECRET_KEY = keyGenerator.generateKey();
    }
}
