package com.kensoftph.javafxmedia;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.geometry.Insets;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;


import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaPlayerController {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile boolean isRunning = true;
    private static String host = "localhost";
    private static int port = 5656;
    public SocketManager socketManager;
    @FXML
    private Button btnSearch;

    @FXML
    private TextField lblSearch;

    @FXML
    private ImageView imgview;

    @FXML
    private Label lblinfo;

    @FXML
    private Label lbldetail;

    @FXML
    private VBox vbox;

    @FXML
    private VBox vboxct;


    @FXML
    public void initialize() {
        connectToServer();
    }

    private void connectToServer() {
        executorService.submit(() -> {
            socketManager = new SocketManager(host, port);
            // Kiểm tra trạng thái kết nối
//            while (!socketManager.isConnected() && isRunning) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e){
//                    if(!isRunning){
//                        break;
//                    }
//                }
//            }
        });
    }
    public void stopThread() {
        isRunning = false;
        executorService.shutdownNow();
        Platform.exit();
    }

    @FXML
    private void Search(ActionEvent event) {
            if (socketManager != null && socketManager.isConnected()) {
                String input = lblSearch.getText();
                if (input.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Nhập dữ liệu", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                socketManager.SendData(input);
                System.out.println(input);
                resetInterface();
                LoadData();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Không thể kết nối đến server", ButtonType.OK);
                alert.showAndWait();
                connectToServer();
            }
    }



    private void LoadData() {
            try {
                String data = socketManager.ReceiveData();
                if (data.equalsIgnoreCase("{}\n")){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Không tìm thấy", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                JSONObject obj = new JSONObject(data);
                System.out.println(data);
                if(obj.has("borders")) {
                        Platform.runLater(() -> {
//                        String languagekey = (String) obj.getJSONObject("language").keySet().iterator().next();
//                        String languagevalue = (String) obj.getJSONObject("language").get(languagekey);
//                        String currencieskey = (String) obj.getJSONObject("currencies").keySet().iterator().next();
//                        String currenciesvalue = (String) obj.getJSONObject("currencies").getJSONObject(currencieskey).get("name");
                            try {
                                String ngonngu = "";
                                for (int i = 0; i < obj.getJSONArray("language").length(); i++) {
                                     {
                                        ngonngu += obj.getJSONArray("language").get(i) + ",";
                                    }
                                }
                                String biengioi = "";
                                for (int i = 0; i < obj.getJSONArray("borders").length(); i++) {
                                    biengioi += obj.getJSONArray("borders").get(i) + ",";
                                }
                                lblinfo.setText("Tên quốc gia: " + obj.get("name") + "\n" +
                                        "Ngôn ngữ: " + ngonngu + "\n" +
                                        "Đơn vị tiền tệ: " + obj.getJSONArray("currencies").get(0) + "\n" +
                                        "Giáp: " + biengioi);
                                String toado = obj.get("latitude") + "," + obj.get("longitude");
                                String population = String.valueOf(obj.get("population"));
                                String weather = "";
                                if (!(obj.get("weather_data").equals("{}"))) {
                                    weather = "Tốc độ gió: " + String.valueOf(obj.getJSONObject("weather_data").get("wind_kph")) + "\n" + " Nhiệt độ: " + String.valueOf(obj.getJSONObject("weather_data").get("temp_c")) + "\n" + " Thời tiết: " + (String) obj.getJSONObject("weather_data").get("condition");
                                }
                                if (lbldetail == null) {
                                    lbldetail = new Label();
                                }
                                lbldetail.setText("Tọa độ lãnh thổ: " + toado + "\n" +
                                        "Dân số: " + population + "\n" +
                                        weather);
                                imgview.setImage(new Image(obj.get("flag").toString()));
                                if (vboxct == null) {
                                    vboxct = new VBox();
                                }
                                news(obj.getJSONArray("relate_news"));
                                newsct(obj.getJSONArray("tourist_attractions"));
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Thông tin không đầy đủ", ButtonType.OK);
                                alert.showAndWait();
                            }
                        });
                }else{
                    Platform.runLater(() ->{
                        try {
                            String toado = obj.get("latitude") + "," + obj.get("longitude");
                            String country = (String) obj.get("country");
                            String population = String.valueOf(obj.get("population"));
                            String weather = "";
                            if (!(obj.get("weather_data").toString().equals("{}"))) {
                                weather = "Tốc độ gió: " + String.valueOf(obj.getJSONObject("weather_data").get("wind_kph")) + "\n" + "Nhiệt độ: " + String.valueOf(obj.getJSONObject("weather_data").get("temp_c")) + "\n" + "Thời tiết: " + (String) obj.getJSONObject("weather_data").get("condition") + "\n" + "Độ ẩm: " + String.valueOf(obj.getJSONObject("weather_data").get("humidity"));
                            }
                            lblinfo.setText("Tọa độ lãnh thổ: " + toado + "\n" +
                                    "Quốc gia: " + country + "\n" +
                                    "Dân số: " + population);
                            if (lbldetail == null) {
                                lbldetail = new Label();
                            }
                            lbldetail.setText(weather);
                            if (vboxct == null) {
                                vboxct = new VBox();
                            }
                            imgview.setImage(new Image("file:C:\\Users\\admin\\IdeaProjects\\JavaFX-Media-master\\src\\main\\resources\\com\\kensoftph\\javafxmedia\\default-image.jpg"));
                            news(obj.getJSONArray("relate_news"));
                            newshotel(obj.getJSONArray("hotels"));
                        }catch (Exception e){
                            e.printStackTrace();
//                            Alert alert = new Alert(Alert.AlertType.ERROR, "Không tìm thấy địa điểm", ButtonType.OK);
//                            alert.showAndWait();
                        }
                    });
                }
            } catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Không tìm thấy địa điểm", ButtonType.OK);
                alert.showAndWait();
            }
    }

    private void news(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String title = (String) obj.get("title");
            String desc = (String) obj.get("desc");
            String link = (String) obj.get("link");
            String image = String.valueOf(obj.get("image"));
            ImageView imageView = new ImageView();
            if (image.equalsIgnoreCase("images/logo.png")){
                imageView = new ImageView(new Image("file:C:\\Users\\admin\\IdeaProjects\\JavaFX-Media-master\\src\\main\\resources\\com\\kensoftph\\javafxmedia\\default-image.jpg"));
            }else{
                imageView = new ImageView(new Image(image));
            }
            VBox newsVBox = new VBox();
            HBox newsHBox = new HBox();
            imageView.setFitWidth(150);
            imageView.setFitHeight(100);
            Text titleText = new Text(title);
            Text descText = new Text(desc);
            Hyperlink hyperlink = new Hyperlink(link);
            hyperlink.setOnAction(event -> {
                // Mở liên kết trong trình duyệt
                openLinkInBrowser(link);
            });
            newsVBox.getChildren().addAll(titleText, descText, hyperlink);
            newsHBox.getChildren().addAll(imageView, newsVBox);
            vbox.getChildren().add(newsHBox);
        }
    }

    private void newsct(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String reviews = "Đánh giá: ";
            String rating = "Xếp hạng: ";
            String price = "Giá cả: ";
            if (obj.has("reviews")){
                reviews ="Đánh giá: "+ String.valueOf(obj.get("reviews"));
            }
            if (obj.has("rating")){
                rating ="Xếp hạng: "+ String.valueOf(obj.get("rating"));
            }
            if (obj.has("price")){
                price ="Giá cả: "+ String.valueOf(obj.get("price"));
            }
            String title ="Địa danh: "+ (String) obj.get("title");
            String desc ="Mô tả: "+ (String) obj.get("description");
            String link ="Link: "+ (String) obj.get("link");
            String image = (String) obj.get("thumbnail");
            VBox newsVBox = new VBox();
            HBox newsHBox = new HBox();
            ImageView imageView = new ImageView(new Image(image));
            imageView.setFitWidth(150);
            imageView.setFitHeight(100);
            Text titleText = new Text(title);
            Text reviewsText = new Text(reviews);
            Text ratingText = new Text(rating);
            Text priceText = new Text(price);
            Text descText = new Text(desc);
            Hyperlink hyperlink = new Hyperlink(link);
            hyperlink.setOnAction(event -> {
                // Mở liên kết trong trình duyệt
                openLinkInBrowser(link);
            });
            newsVBox.getChildren().addAll(titleText,reviewsText,ratingText,priceText, descText, hyperlink);
            newsHBox.getChildren().addAll(imageView, newsVBox);
            vboxct.getChildren().add(newsHBox);
        }
    }

    private void newshotel(JSONArray arr) {
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String name = "Tên khách sạn: ";
            String address = "Địa chỉ: ";
            String phone = "Điện thoại: ";
            String price = "Giá cả: ";
            String rating = "Xếp hạng: ";
            String image = "";
            String website;
            if (obj.has("name")){
                name += String.valueOf(obj.get("name"));
            }
            if (obj.has("address")){
                address += String.valueOf(obj.get("address"));
            }
            if (obj.has("phone")){
                phone += String.valueOf(obj.get("phone"));
            }
            if (obj.has("price")){
                price += String.valueOf(obj.get("price"));
            }
            if (obj.has("rating")){
                rating += String.valueOf(obj.get("rating"));
            }
            if (obj.has("image")){
                image = String.valueOf(obj.get("image"));
            }
            if (obj.has("website")){
                website = String.valueOf(obj.get("website"));
            } else {
                website = "";
            }
            VBox newsVBox = new VBox();
            newsVBox.setId("vbox" + i);
            HBox newsHBox = new HBox();
            ImageView imageView = new ImageView(new Image(image));
            imageView.setFitWidth(150);
            imageView.setFitHeight(100);
            Text nameText= new Text(name);
            Text addressText = new Text(address);
            Text phoneText = new Text(phone);
            Text priceText = new Text(price);
            Text ratingText = new Text(rating);
            Hyperlink hyperlink = new Hyperlink(website);

            hyperlink.setOnAction(event -> {
                // Mở liên kết trong trình duyệt
                openLinkInBrowser(website);
            });
            newsVBox.getChildren().addAll(nameText,addressText,phoneText,priceText, ratingText, hyperlink);
            newsHBox.getChildren().addAll(imageView, newsVBox);
            vboxct.getChildren().add(newsHBox);
            newsVBox.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    reviewPanel(newsVBox.getId(), (String) obj.get("reviews_link"));
                }
            });
        }
    }

    private void reviewPanel(String info,String link) {
        Stage infoStage = new Stage();
        infoStage.initModality(Modality.APPLICATION_MODAL);
        infoStage.setTitle("Thông tin");

        VBox infoLayout = new VBox(10);
        infoLayout.setPadding(new Insets(10));
        try {
            Document doc = Jsoup.connect(link+"&key=292c11538bb2f9b265a687e33a2118e18d9530dbd6e7fecb7f1cd2411ebcb798").method(Connection.Method.GET).ignoreContentType(true).execute().parse();
            JSONObject newsJSON = new JSONObject(doc.text());
            JSONArray items = newsJSON.getJSONArray("reviews");
            for (int i=0; i<items.length(); i++) {
                String name = "Tên người dùng: " +(String) items.getJSONObject(i).getJSONObject("user").get("name");
                String source = (String) items.getJSONObject(i).get("source");
                String date = (String) items.getJSONObject(i).get("date");
                String rating = "Xếp hạng: ";
                if (items.getJSONObject(i).has("rating")) {
                    rating += String.valueOf(items.getJSONObject(i).get("rating"));
                }
                String rooms = "Phòng: ";
                String service = "Dịch vụ: ";
                String location = "Địa điểm: ";
                if (items.getJSONObject(i).has("details")) {
                    if (items.getJSONObject(i).getJSONObject("details").has("rooms")) {
                        rooms += (String) items.getJSONObject(i).getJSONObject("details").get("rooms");
                    }if (items.getJSONObject(i).getJSONObject("details").has("service")) {
                        service += (String) items.getJSONObject(i).getJSONObject("details").get("service");
                    }if (items.getJSONObject(i).getJSONObject("details").has("location")) {
                        location += (String) items.getJSONObject(i).getJSONObject("details").get("location");
                    }
                }
                String snippet = "Nhận xét: ";
                if (items.getJSONObject(i).has("snippet")) {
                    snippet = (String) items.getJSONObject(i).get("snippet");
                }
                Text nameText = new Text(name);
                Text sourceText = new Text(source);
                Text dateText = new Text(date);
                Text ratingText = new Text(rating);
                Text snippetText = new Text(snippet);
                Text roomsText = new Text(rooms);
                Text serviceText = new Text(service);
                Text locationText = new Text(location);
                infoLayout.getChildren().addAll(nameText,sourceText,dateText,ratingText,snippetText,roomsText,serviceText,locationText);
                Separator separator = new Separator();
                infoLayout.getChildren().add(separator);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ScrollPane scrollPane = new ScrollPane(infoLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        Scene infoScene = new Scene(scrollPane, 500, 400);
        infoStage.setScene(infoScene);
        infoStage.show();
    }

    public void resetInterface() {
        // Đặt lại giá trị của các thành phần giao diện về trạng thái ban đầu
        imgview.setImage(null);
        lblinfo.setText("");
        lbldetail.setText("");
        vbox.getChildren().clear();
        vboxct.getChildren().clear();
    }
        private void openLinkInBrowser(String link){
            try {
                Desktop.getDesktop().browse(new URI(link));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//    CipherFactory factory = CipherFactory.getInstance();
//    ICipher cipher = factory.get(CipherFactory.Algorithm.AES);
    }
