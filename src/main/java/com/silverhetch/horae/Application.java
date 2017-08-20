//package com.silverhetch.horae;
//
//import com.silverhetch.horae.socket.MessageListener;
//import com.silverhetch.horae.socket.SocketConnectionImpl;
//import com.silverhetch.horae.socket.SocketDevice;
//import com.silverhetch.horae.upnp.HoraeDevice;
//import com.silverhetch.horae.upnp.UPnPDevice;
//import javafx.scene.Scene;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class Application extends javafx.application.Application {
//    private final SocketDevice socketDevice;
//    private final UPnPDevice uPnPDevice;
//
//    public Application() {
//        this.socketDevice = new SocketConnectionImpl().server(8912, new MessageListener() {
//            @Override
//            public void onReceive(String message) {
//                socketDevice.sendMessage("This is response of " + message);
//            }
//        });
//        this.uPnPDevice = new HoraeDevice(8911);
//    }
//
//    @Override
//    public void init() throws Exception {
//        super.init();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                uPnPDevice.launch();
//            }
//        }).start();
//    }
//
//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        uPnPDevice.shutdown();
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
