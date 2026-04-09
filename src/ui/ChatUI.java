package ui;

import Cliente.ChatClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.Base64;

public class ChatUI extends Application {

    private VBox chatBox = new VBox(15);
    private VBox usersBox = new VBox(10);

    private ChatClient client;
    private String username;
    private String selectedUser = null;

    private Label typingLabel = new Label();

    @Override
    public void start(Stage stage) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Digite seu nome:");
        username = dialog.showAndWait().orElse("User");

        try {
            client = new ChatClient("localhost", 5000, username);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Label title = new Label("CHAT DO " + username.toUpperCase());
        title.setStyle("-fx-font-size: 28;");

        typingLabel.setStyle("-fx-font-size: 13;");

        VBox headerBox = new VBox(title, typingLabel);

        HBox header = new HBox(headerBox);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #290000;");


        chatBox.setPadding(new Insets(20));

        ScrollPane chatScroll = new ScrollPane(chatBox);
        chatScroll.setFitToWidth(true);


        Label online = new Label("ONLINE");
        online.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20;");

        VBox usersPanel = new VBox(15, online, usersBox);
        usersPanel.setPadding(new Insets(20));
        usersPanel.setStyle("-fx-background-color: #2b0400;");


        TextField input = new TextField();
        input.setPromptText("Digite uma mensagem...");

        input.setOnKeyTyped(e -> client.sendMessage("TYPING|" + username));

        Button send = new Button("➤");
        Button imgBtn = new Button("📷");

        imgBtn.setOnAction(e -> sendImage(stage));

        HBox inputBox = new HBox(10, imgBtn, input, send);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #00012b;");
        HBox.setHgrow(input, Priority.ALWAYS);


        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(chatScroll);
        root.setRight(usersPanel);
        root.setBottom(inputBox);

        send.setOnAction(e -> sendMessage(input));
        input.setOnAction(e -> sendMessage(input));

        new Thread(this::listenMessages).start();

        stage.setScene(new Scene(root, 1000, 600));
        stage.setTitle("Chat");
        stage.show();
    }

    private void listenMessages() {
        try {
            BufferedReader in = client.getReader();
            String msg;

            while ((msg = in.readLine()) != null) {
                String finalMsg = msg;
                Platform.runLater(() -> handleMessage(finalMsg));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String msg) {

        if (msg.startsWith("USERS|")) {
            updateUsers(msg.substring(6));
            return;
        }

        if (msg.startsWith("TYPING|")) {
            String user = msg.split("\\|")[1];
            if (!user.equals(username)) {
                typingLabel.setText(user + " está digitando...");
            }
            return;
        }

        if (msg.startsWith("IMG|")) {

            try {
                String[] parts = msg.split("\\|", 3);
                String sender = parts[1];

                byte[] bytes = Base64.getDecoder().decode(parts[2]);
                Image img = new Image(new ByteArrayInputStream(bytes));

                ImageView view = new ImageView(img);
                view.setFitWidth(200);
                view.setPreserveRatio(true);

                HBox wrapper = new HBox(view);

                if (sender.equals(username)) {
                    wrapper.setAlignment(Pos.CENTER_RIGHT);
                } else {
                    wrapper.setAlignment(Pos.CENTER_LEFT);
                }

                chatBox.getChildren().add(wrapper);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        if (msg.startsWith("MSG|")) {

            String[] p = msg.split("\\|", 3);
            String sender = p[1];
            String text = p[2];

            boolean isMe = sender.equals(username);

            Label name = new Label(sender.toUpperCase());
            name.setStyle("-fx-font-size: 11;");

            Label message = new Label(text);
            message.setWrapText(true);
            message.setMaxWidth(250);

            HBox bubble = new HBox(message);

            if (isMe) {
                bubble.setStyle("-fx-background-color: #1e5bd8; -fx-background-radius: 15; -fx-padding: 8 12;");
                message.setStyle("-fx-text-fill: white;");
            } else {
                bubble.setStyle("-fx-background-color: #151414; -fx-background-radius: 15; -fx-padding: 8 12;");
                message.setStyle("-fx-text-fill: white;");
            }

            VBox box = new VBox(name, bubble);

            HBox wrapper = new HBox(box);

            if (isMe) {
                wrapper.setAlignment(Pos.CENTER_RIGHT);
            } else {
                wrapper.setAlignment(Pos.CENTER_LEFT);
            }

            chatBox.getChildren().add(wrapper);
            typingLabel.setText("");
        }
    }

    private void updateUsers(String data) {

        usersBox.getChildren().clear();

        String[] users = data.split(",");

        for (String user : users) {

            if (user.isEmpty()) continue;

            Label label = new Label("• " + user.toUpperCase());
            label.setStyle("-fx-text-fill: white;");

            label.setOnMouseClicked(e -> selectedUser = user);

            usersBox.getChildren().add(label);
        }
    }

    private void sendMessage(TextField input) {

        String msg = input.getText();
        if (msg.isEmpty()) return;

        if (selectedUser != null) {
            client.sendMessage("DM|" + selectedUser + "|" + msg);
        } else {
            client.sendMessage(msg);
        }

        input.clear();
    }

    private void sendImage(Stage stage) {

        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(stage);

        if (file == null) return;

        try {
            byte[] bytes = new FileInputStream(file).readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);

            client.sendMessage("IMG|" + username + "|" + base64);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}