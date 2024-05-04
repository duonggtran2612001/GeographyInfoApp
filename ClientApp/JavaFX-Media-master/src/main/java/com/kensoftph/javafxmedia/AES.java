package com.kensoftph.javafxmedia;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AES{

    public static SecretKey generateAESKey() throws Exception {
        // Tạo đối tượng KeyGenerator với thuật toán AES
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        // Đặt kích thước khóa (ví dụ: 128 bits, 192 bits, 256 bits)
        keyGenerator.init(128);

        // Tạo khóa
        return keyGenerator.generateKey();
    }
    public static String encryptAES(String plaintext,SecretKey secretKey ) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            // Encode dữ liệu đã mã hóa thành chuỗi Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptAES(String encryptedText,SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decode chuỗi Base64 để có mảng byte
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

            // Giải mã dữ liệu
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Chuyển mảng byte giải mã thành chuỗi
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

