package com.kensoftph.javafxmedia;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import com.kensoftph.javafxmedia.SocketManager;
import com.kensoftph.javafxmedia.MediaPlayerController;

import java.io.IOException;

public class Main extends Application {
    private static String host = "localhost";
    private static int port = 5656;
    private static Socket socket;
    private static BufferedReader inStream;
    private static BufferedWriter outStream;
    private static BufferedReader stdIn;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("media-player.fxml"));
        Parent root = loader.load();
        MediaPlayerController controller = loader.getController();
        stage.setTitle("JavaFX MediaPlayer!");
        stage.setScene(new Scene(root,900,500));
        stage.show();
        stage.setOnCloseRequest(event -> controller.stopThread());
    }


    public static void main(String[] args) {
        launch();
    }
}