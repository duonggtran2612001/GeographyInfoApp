package com.kensoftph.javafxmedia;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Objects;

public class SocketManager {
    Socket socket;
    BufferedReader inStream;
    BufferedWriter outStream;
    public static SecretKey secretKey;


    // Constructor của client
    public SocketManager(String host, int port) {
        try {
            socket = new Socket(host, port);
            System.out.println("Client đã kết nối đến server " + socket.getRemoteSocketAddress());
            // Gắn kết stream
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Sendkey();
        } catch (Exception ignored) {

        }
    }

    public void closeSocket() {
        try {
            inStream.close();
            outStream.close();
            socket.close();
            System.out.println("Client đóng socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void Sendkey(){
        try{
            String pubkey = inStream.readLine();
            RSA rsa = new RSA(pubkey);
            System.out.println(pubkey);
            secretKey = AES.generateAESKey();
            byte[] secretKeyBytes = secretKey.getEncoded();
            String base64EncodedsecretKey = Base64.getEncoder().encodeToString(secretKeyBytes);
            outStream.write(rsa.encrypt(base64EncodedsecretKey) + "\n");
            System.out.println(rsa.encrypt(base64EncodedsecretKey) + "\n");
            outStream.flush();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ngắt kết nối với server", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void SendData(String input){
        try{
            outStream.write(AES.encryptAES(input,secretKey) + "\n");
            outStream.flush();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ngắt kết nối với server", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public String ReceiveData() {
        String endata = null;
        String data = null;
        try {
            endata = inStream.readLine();
            data = AES.decryptAES(endata,secretKey);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ngắt kết nối với server", ButtonType.OK);
            alert.showAndWait();
        }
        return data;
    }

}
