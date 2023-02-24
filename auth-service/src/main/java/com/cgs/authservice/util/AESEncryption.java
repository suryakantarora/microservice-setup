package com.cgs.authservice.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class AESEncryption {
    static final int KEY_SIZE = 256;
    static final int IV_SIZE = 128;
    static final String HASH_CIPHER = "AES/CBC/PKCS7Padding";
    static final String AES = "AES";
    static final String CHARSET_TYPE = "UTF-8";
    static final String KDF_DIGEST = "MD5";
    // Seriously crypto-js, what's wrong with you?
    static final String APPEND = "Salted__";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * Encrypt
     *
     * @param password  passphrase
     * @param plainText plain string
     */
    public static String encrypt(String password, String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] saltBytes = generateSalt();
        byte[] key = new byte[KEY_SIZE / 8];
        byte[] iv = new byte[IV_SIZE / 8];

        EvpKDF(password.getBytes(CHARSET_TYPE), saltBytes, key, iv);

        SecretKey keyS = new SecretKeySpec(key, AES);

        Cipher cipher = Cipher.getInstance(HASH_CIPHER);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keyS, ivSpec);
        byte[] cipherText = cipher.doFinal(plainText.getBytes(CHARSET_TYPE));

        // Thanks kientux for this: https://gist.github.com/kientux/bb48259c6f2133e628ad
        // Create CryptoJS-like encrypted !

        byte[] sBytes = APPEND.getBytes(CHARSET_TYPE);
        byte[] b = new byte[sBytes.length + saltBytes.length + cipherText.length];
        System.arraycopy(sBytes, 0, b, 0, sBytes.length);
        System.arraycopy(saltBytes, 0, b, sBytes.length, saltBytes.length);
        System.arraycopy(cipherText, 0, b, sBytes.length + saltBytes.length, cipherText.length);

        byte[] bEncode = Base64.getEncoder().encode(b);

        return new String(bEncode);
    }


    public static void main(String[] args) {
        try {
            System.out.println("encrypt "+encrypt("ef17699fba61db57752203c42f448a9dea5325921819c69f12500434505dc68e7cf","monika"));
            String decrypt = decrypt("ef17699fba61db57752203c42f448a9dea5325921819c69f12500434505dc68e7cf", "U2FsdGVkX1/dWNbllE8ZibG0C035L0YmNeOJztBRQD8=");
            System.out.println(decrypt);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Decrypt
     * Thanks Artjom B. for this: http://stackoverflow.com/a/29152379/4405051
     *
     * @param password   passphrase
     * @param cipherText encrypted string
     */
    public static String decrypt(String password, String cipherText) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] ctBytes = Base64.getDecoder().decode(cipherText.getBytes(CHARSET_TYPE));
        byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
        byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
        byte[] key = new byte[KEY_SIZE / 8];
        byte[] iv = new byte[IV_SIZE / 8];

        EvpKDF(password.getBytes(CHARSET_TYPE), saltBytes, key, iv);

        Cipher cipher = Cipher.getInstance(HASH_CIPHER);
        SecretKey keyS = new SecretKeySpec(key, AES);

        cipher.init(Cipher.DECRYPT_MODE, keyS, new IvParameterSpec(iv));
        byte[] plainText = cipher.doFinal(ciphertextBytes);
        return new String(plainText);
    }

    private static void EvpKDF(byte[] password, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        EvpKDF(password, AESEncryption.KEY_SIZE, AESEncryption.IV_SIZE, salt, resultKey, resultIv);
    }

    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(AESEncryption.KDF_DIGEST);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);
            block = hasher.digest(salt);
            hasher.reset();

            // Iterations

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length / 4;
        }

        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);

        return derivedBytes; // key + iv
    }

    private static byte[] generateSalt() {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[8];
        r.nextBytes(salt);
        return salt;
    }

    @SuppressWarnings("unused")
    public static String generateSecretKey(int length) {
        SecureRandom r = new SecureRandom();
        byte[] salt = new byte[length];
        r.nextBytes(salt);
        return new String(salt);
    }
}