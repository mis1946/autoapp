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
public class TableClientMobile {
     private SimpleStringProperty contindex01;
     private SimpleStringProperty contindex02; 
     private SimpleStringProperty contindex03;
     private SimpleStringProperty contindex04;
     
     TableClientMobile( String contindex01,
                         String contindex02,
                         String contindex03,
                         String contindex04
                    ){
     
          this.contindex01 = new SimpleStringProperty(contindex01);
          this.contindex02 = new SimpleStringProperty(contindex02);
          this.contindex03 = new SimpleStringProperty(contindex03);
          this.contindex04 = new SimpleStringProperty(contindex04);
     }
     public String getContindex04(){return contindex04.get();}
     public void setContindex04(String contindex01){this.contindex04.set(contindex01);}
     
     public String getContindex01(){return contindex01.get();}
     public void setContindex01(String contindex01){this.contindex01.set(contindex01);}
     
     public String getContindex02(){return contindex02.get();}
     public void setContindex02(String contindex02){this.contindex02.set(contindex02);}
     
     public String getContindex03(){return contindex03.get();}
     public void setContindex03(String contindex03){this.contindex03.set(contindex03);}
    
     
}
