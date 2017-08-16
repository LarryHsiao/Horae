//package com.silverhetch.horae.javafx;
//
//import com.silverhetch.horae.socket.ComputeUnit;
//import com.silverhetch.horae.socket.SocketConnectionImpl;
//import com.silverhetch.horae.socket.SocketDevice;
//import javafx.scene.Scene;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class Application extends javafx.application.Application {
//    private final SocketDevice socketDevice;
//
//    public Application() {
//        this.socketDevice = new SocketConnectionImpl().server(8912, new ComputeUnit() {
//            @Override
//            public String compute(String message) {
//                return "response";
//            }
//        });
//    }
//
//    @Override
//    public void init() throws Exception {
//        super.init();
//        socketDevice.launch();
//        Thread.sleep(3000);
//        socketDevice.sendMessage("123");
//    }
//
//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        socketDevice.shutdown();
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene(new StackPane(), 300, 300));
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
