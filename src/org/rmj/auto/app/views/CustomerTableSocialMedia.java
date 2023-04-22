/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleStringProperty;

/**
 * Date: 03-07-2023
 * @author Arsiela
 */
public class CustomerTableSocialMedia {
     private SimpleStringProperty socmindex01;
     private SimpleStringProperty socmindex02; 
     private SimpleStringProperty socmindex03;
     //private SimpleStringProperty xSocialID;
     
     CustomerTableSocialMedia( String socmindex01,
                         String socmindex02,
                         String socmindex03
                         //String xSocialID
                         ){
     
          this.socmindex01 = new SimpleStringProperty(socmindex01);
          this.socmindex02 = new SimpleStringProperty(socmindex02);
          this.socmindex03 = new SimpleStringProperty(socmindex03);
          //this.xSocialID = new SimpleStringProperty(xSocialID);
     }
/*    
     public String getxSocialID() {
          return xSocialID.get();
     }

     public void setxSocialID(String xSocialID) {
          this.xSocialID.set(xSocialID);
     }
*/     
     public String getSocmindex01(){return socmindex01.get();}
     public void setSocmindex01(String socmindex01){this.socmindex01.set(socmindex01);}
     
     public String getSocmindex02(){return socmindex02.get();}
     public void setSocmindex02(String socmindex02){this.socmindex02.set(socmindex02);}
     
     public String getSocmindex03(){return socmindex03.get();}
     public void setSocmindex03(String socmindex03){this.socmindex03.set(socmindex03);}
     
}
