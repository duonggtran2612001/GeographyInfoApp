package com.kensoftph.javafxmedia;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    private static PublicKey publicKey;

    public RSA(String encodedPublicKey) {
        try {
            // Decode chuỗi Base64 để có mảng byte
            byte[] publicKeyBytes = Base64.getDecoder().decode(encodedPublicKey);

            // Tạo đối tượng KeyFactory với thuật toán RSA
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Tạo đối tượng X509EncodedKeySpec từ mảng byte của public key
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            // Tạo đối tượng PublicKey từ X509EncodedKeySpec
            publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String plaintext) {
        try {
            // Mã hóa dữ liệu bằng khóa công khai
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            // Encode dữ liệu đã mã hóa thành chuỗi Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }


}
