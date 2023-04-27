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
//
//public class InquiryTableRequirements {
//
//    private SimpleBooleanProperty selected;
//    private SimpleStringProperty tblindex01;
//    private SimpleStringProperty tblindex02;
//
//    public InquiryTableRequirements(String tblindex01, String tblindex02) {
//        this.selected = new SimpleBooleanProperty(false);
//        this.tblindex01 = new SimpleStringProperty(tblindex01);
//        this.tblindex02 = new SimpleStringProperty(tblindex02);
//    }
//
//    public boolean isSelected() {
//        return selected.get();
//    }
//
//    public SimpleBooleanProperty selectedProperty() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected.set(selected);
//    }
//
//    public String getTblindex01() {
//        return tblindex01.get();
//    }
//
//    public void setTblindex01(String tblindex01) {
//        this.tblindex01.set(tblindex01);
//    }
//
//    public String getTblindex02() {
//        return tblindex02.get();
//    }
//
//    public void setTblindex02(String tblindex02) {
//        this.tblindex02.set(tblindex02);
//    }
//}

public class InquiryTableRequirements {
     
     private SimpleBooleanProperty tblindex01; //Check box
     private SimpleStringProperty tblindex02; //Requirements
     private SimpleStringProperty tblindex03; //Received By
     private SimpleStringProperty tblindex04; //Received Date
     
     public InquiryTableRequirements(boolean tblindex01
                                   , String tblindex02
                                   , String tblindex03
                                   , String tblindex04
                                   ) {
          this.tblindex01 = new SimpleBooleanProperty(tblindex01);
          this.tblindex02 = new SimpleStringProperty(tblindex02);
          this.tblindex03 = new SimpleStringProperty(tblindex03);
          this.tblindex04 = new SimpleStringProperty(tblindex04);
     }
     
     //Check box
     public boolean isTblindex01() { return tblindex01.get();}
     public void setTblindex01(boolean tblindex01) { this.tblindex01.set(tblindex01);}
     public BooleanProperty selectedProperty() {return tblindex01;}

     //Requirements
     public String getTblindex02(){return tblindex02.get();}
     public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
     
     //Received By
     public String getTblindex03(){return tblindex03.get();}
     public void setTblindex03(String tblindex03){this.tblindex03.set(tblindex03);}
     
     //Received Date
     public String getTblindex04(){return tblindex04.get();}
     public void setTblindex04(String tblindex04){this.tblindex04.set(tblindex04);}
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
