package com.codeforces.graygoose.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EncryptUtil {
    private static final int[] ENCRYPT_PERMUTATION = new int[32];
    private static final int[] DECRYPT_PERMUTATION = new int[32];

    private static char decryptChar(String code) {
        if (code.length() != 8) {
            throw new IllegalArgumentException("code.length should be 8, but " + code.length() + " has been found.");
        }

        if (code.startsWith("???????")) {
            return code.charAt(7);
        } else {
            int[] vals = new int[8];

            for (int i = 0; i < 8; i++) {
                vals[i] = DECRYPT_PERMUTATION[code.charAt(i) - (' ' + 1)];
            }

            int num = 0;
            for (int i = 0; i < 8; i++) {
                num = num * 32 + vals[7 - i];
            }

            return (char)(num);
        }
    }

    private static String encrypt(char c) {
        int num = (int)c;

        if (num <= 0) {
            return "???????" + c;
        } else {
            int[] vals = new int[8];

            for (int i = 0; i < 8; i++) {
                vals[i] = num & 31;
                num /= 32;
            }

            StringBuilder result = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                result.append((char)(' ' + 1 + ENCRYPT_PERMUTATION[vals[i]]));
            }

            return result.toString();
        }
    }

    public static String decrypt(String message) {
        if (message.length() % 8 != 0) {
            throw new IllegalArgumentException("Encypted message length expected to be divisible by 8, " +
                    "but " + message.length() + " found.");
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < message.length(); i += 8) {
            result.append(decryptChar(message.substring(i, i + 8)));
        }

        return result.toString();
    }

    public static String encrypt(String message) {
        StringBuilder result = new StringBuilder(message.length() * 8);
        for (int i = 0; i < message.length(); i++) {
            result.append(encrypt(message.charAt(i)));
        }
        return result.toString();
    }

    static {
        List<Integer> perm = new ArrayList<Integer>();

        for (int i = 0; i < 32; i++) {
            perm.add(i);
        }

        Random random = new Random("EncryptUtil".hashCode());
        Collections.shuffle(perm, random);

        for (int i = 0; i < 32; i++) {
            ENCRYPT_PERMUTATION[i] = perm.get(i);
            DECRYPT_PERMUTATION[ENCRYPT_PERMUTATION[i]] = i;
        }
    }
}
