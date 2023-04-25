/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Date Created: 04-25-2023
 * @author Arsiela
 */
public class InquiryTableRequirements {
     
     private SimpleBooleanProperty tblindex01; //Check box
     private SimpleStringProperty tblindex02; //Requirements
     
     public InquiryTableRequirements(boolean tblindex01, String tblindex02) {
          this.tblindex01 = new SimpleBooleanProperty(tblindex01);
          this.tblindex02 = new SimpleStringProperty(tblindex02);
     }
     
     //Check box
     public boolean isTblindex01() { return tblindex01.get();}
     public void setTblindex01(boolean tblindex01) { this.tblindex01.set(tblindex01);}
     public BooleanProperty selectedProperty() {return tblindex01;}

     //Requirements
     public String getTblindex02(){return tblindex02.get();}
     public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
}



//public class InquiryTableRequirements {
//     
//     private SimpleStringProperty tblindex01; //Check box
//     private SimpleStringProperty tblindex02; //Requirements
//     
//     InquiryTableRequirements( String tblindex01,
//                               String tblindex02
//                         ){
//     
//          this.tblindex01 = new SimpleStringProperty(tblindex01);
//          this.tblindex02 = new SimpleStringProperty(tblindex02);
//     }
//     
//     //Check box
//     public String getTblindex01(){return tblindex01.get();}
//     public void setTblindex01(String tblindex01){this.tblindex01.set(tblindex01);}
//     //Requirements
//     public String getTblindex02(){return tblindex02.get();}
//     public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
//}
