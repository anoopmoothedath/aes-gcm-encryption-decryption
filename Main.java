package org.example;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class Main {

    private static final int TAG_LENGTH = 128;

    private static final String TEXT_TO_BE_ENCRYPTED= "This is a random text to be encrypted";
    public static void main(String[] args) throws Exception {
        System.out.println("Started Java-AES-GCM-Swift Program!");

        String secret  = generateRandomString(32);
        String nonce = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));

        String encryptedData = encrypt(TEXT_TO_BE_ENCRYPTED, secret, nonce);
        String decryptedString = decrypt(encryptedData,secret,nonce);
        System.out.println("Decrypted String: "+decryptedString);
}
    public static String decrypt(String cipherText, String secret, String nonce) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        System.out.println("Passphrase: "+secret);
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        System.out.println("Nonce/IV: "+nonce);
        byte[] nonceBytes = Base64.getDecoder().decode(nonce);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedCipherText = decoder.decode(cipherText);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, nonceBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

        byte[] plaintextBytes = cipher.doFinal(decodedCipherText);
        String plaintext = new String(plaintextBytes, StandardCharsets.UTF_8);
        return plaintext;
    }

    public static String encrypt(String plainText, String secret, String nonce) {
        try {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

            byte[] nonceBytes = java.util.Base64.getDecoder().decode(nonce);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            GCMParameterSpec gcmSpec = new GCMParameterSpec(TAG_LENGTH, nonceBytes);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] plaintextBytes = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);

            System.out.println("Cipher Text: " + java.util.Base64.getEncoder().encodeToString(ciphertextBytes));
            return Base64.getEncoder().encodeToString(ciphertextBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}