package com.silverhetch.horae;

import com.silverhetch.horae.socket.MessageListener;
import com.silverhetch.horae.socket.SocketConnectionImpl;
import com.silverhetch.horae.socket.SocketDevice;
import com.silverhetch.horae.upnp.*;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    private final SocketDevice socketDevice;
    private final ControlPoint device;

    public Application() {
        this.socketDevice = new SocketConnectionImpl().server(8912, new MessageListener() {
            @Override
            public void onReceive(String message) {
                socketDevice.sendMessage("This is response of " + message);
            }
        });
        this.device = new HoraeUPnPImpl().controlPoint(new HoraeDiscoverListener() {
            @Override
            public void onDeviceDiscovered(RemoteDevice remoteDevice) {
                System.out.println("Discovered " +remoteDevice.identity());
            }

            @Override
            public void onDeviceLeave(RemoteDevice remoteDevice) {
                System.out.println("Leaves " +remoteDevice.identity());
            }
        });
    }

    @Override
    public void init() throws Exception {
        super.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                device.launch();
            }
        }).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        device.shutdown();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new StackPane(), 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
