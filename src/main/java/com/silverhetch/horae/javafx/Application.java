package com.silverhetch.horae.javafx;

import com.silverhetch.horae.device.Device;
import com.silverhetch.horae.device.DeviceImpl;
import com.silverhetch.horae.socket.SocketServer;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    private final Device device;

    public Application() {
        this.device = new DeviceImpl();
    }

    @Override
    public void init() throws Exception {
        super.init();
        device.enable();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        device.disable();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new StackPane(), 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            new SocketServer(8912).run();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}