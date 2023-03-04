package org.rmj.auto.app;

import javafx.application.Application;
import org.rmj.appdriver.GRider;
import org.rmj.auto.app.views.Autoapp;

public class LetMeInn {
    public static void main(String [] args){
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        GRider oApp = new GRider();
        
        if (!oApp.logUser("AutoApp", "M001111122")){
            System.err.println(oApp.getMessage() + oApp.getErrMsg());
            System.exit(1);
        }
        
          Autoapp instance = new Autoapp();
          instance.setGRider(oApp);

          Application.launch(instance.getClass());
    }
}
