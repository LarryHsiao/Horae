package com.silverhetch.horae.example;

import com.silverhetch.clotho.observable.Observer;
import com.silverhetch.horae.HoraeImpl;
import com.silverhetch.horae.MessageHandle;
import com.silverhetch.horae.autoconnection.DeviceStatus;
import com.silverhetch.horae.upnp.HoraeUPnPImpl;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.fourthline.cling.UpnpServiceImpl;

public class Application extends javafx.application.Application implements Observer<DeviceStatus> {
    private final HoraeImpl horaeImpl;

    public Application() {
        this.horaeImpl = new HoraeImpl(new HoraeUPnPImpl(new UpnpServiceImpl()), new LogImpl());
    }

    @Override
    public void init() throws Exception {
        super.init();
        horaeImpl.launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        horaeImpl.shutdown();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new StackPane(), 300, 300));
        primaryStage.show();
    }

    @Override
    public void onUpdate(com.silverhetch.clotho.observable.Observable<DeviceStatus> observable, DeviceStatus deviceStatus) {
        System.out.println("Now this device is master? " + deviceStatus.isMaster());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
