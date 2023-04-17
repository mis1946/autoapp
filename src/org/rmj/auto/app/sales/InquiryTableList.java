/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleStringProperty;

/**
 * DATE CREATED: 04-17-2023
 * @author Arsiela
 */
public class InquiryTableList {
     private SimpleStringProperty tbllistindex01; //Row
     private SimpleStringProperty tbllistindex02; //Inquiry Date
     private SimpleStringProperty tbllistindex03; //Customer Name
     
     private SimpleStringProperty tblcinqindex02; //sBranchCD
     private SimpleStringProperty tblcinqindex03; //dTransact
     private SimpleStringProperty tblcinqindex04; //sEmployID
     private SimpleStringProperty tblcinqindex05; //cIsVhclNw
     private SimpleStringProperty tblcinqindex06; //
     private SimpleStringProperty tblcinqindex07; //
     private SimpleStringProperty tblcinqindex08; //
     private SimpleStringProperty tblcinqindex09; // 
     private SimpleStringProperty tblcinqindex10; //
     private SimpleStringProperty tblcinqindex11; //
     
     
     
     
     InquiryTableList( String tbllistindex01,
                         String tbllistindex02,
                         String tbllistindex03,
                         String tblcinqindex02,
                         String tblcinqindex03,
                         String tblcinqindex04,
                         String tblcinqindex05,
                         String tblcinqindex06,
                         String tblcinqindex07,
                         String tblcinqindex08,
                         String tblcinqindex09,
                         String tblcinqindex10,
                         String tblcinqindex11
                         ){
     
          this.tbllistindex01 = new SimpleStringProperty(tbllistindex01);
          this.tbllistindex02 = new SimpleStringProperty(tbllistindex02);
          this.tbllistindex03 = new SimpleStringProperty(tbllistindex03);
          
          this.tblcinqindex02 = new SimpleStringProperty(tblcinqindex02);
          this.tblcinqindex03 = new SimpleStringProperty(tblcinqindex03);
          this.tblcinqindex04 = new SimpleStringProperty(tblcinqindex04);
          this.tblcinqindex05 = new SimpleStringProperty(tblcinqindex05);
          this.tblcinqindex06 = new SimpleStringProperty(tblcinqindex06);
          this.tblcinqindex07 = new SimpleStringProperty(tblcinqindex07);
          this.tblcinqindex08 = new SimpleStringProperty(tblcinqindex08);
          this.tblcinqindex09 = new SimpleStringProperty(tblcinqindex09);
          this.tblcinqindex10 = new SimpleStringProperty(tblcinqindex10);
          this.tblcinqindex11 = new SimpleStringProperty(tblcinqindex11);
     }
     
     //Row
     public String getTblcinqindex01(){return tbllistindex01.get();}
     public void setTblcinqindex01(String tbllistindex01){this.tbllistindex01.set(tbllistindex01);}
     //
     public String getTbllistindex02(){return tbllistindex02.get();}
     public void setTbllistindex02(String tbllistindex02){this.tbllistindex02.set(tbllistindex02);}
     //
     public String getTbllistindex03(){return tbllistindex03.get();}
     public void setTbllistindex03(String tbllistindex03){this.tbllistindex03.set(tbllistindex03);}
     
     
     //
     public String getTblcinqindex02(){return tblcinqindex02.get();}
     public void setTblcinqindex02(String tblcinqindex02){this.tblcinqindex02.set(tblcinqindex02);}
     //
     public String getTblcinqindex03(){return tblcinqindex03.get();}
     public void setTblcinqindex03(String tblcinqindex03){this.tblcinqindex03.set(tblcinqindex03);}
     //
     public String getTblcinqindex04(){return tblcinqindex04.get();}
     public void setTblcinqindex04(String tblcinqindex04){this.tblcinqindex04.set(tblcinqindex04);}
     
     //
     public String getTblcinqindex05(){return tblcinqindex05.get();}
     public void setTblcinqindex05(String tblcinqindex05){this.tblcinqindex05.set(tblcinqindex05);}
     //
     public String getTblcinqindex06(){return tblcinqindex06.get();}
     public void setTblcinqindex06(String tblcinqindex06){this.tblcinqindex06.set(tblcinqindex06);}
     //
     public String getTblcinqindex07(){return tblcinqindex07.get();}
     public void setTblcinqindex07(String tblcinqindex07){this.tblcinqindex07.set(tblcinqindex07);}
     //
     public String getTblcinqindex08(){return tblcinqindex08.get();}
     public void setTblcinqindex08(String tblcinqindex08){this.tblcinqindex08.set(tblcinqindex08);}
     // 
     public String getTblcinqindex09(){return tblcinqindex09.get();}
     public void setTblcinqindex09(String tblcinqindex09){this.tblcinqindex09.set(tblcinqindex09);}
     // 
     public String getTblcinqindex10(){return tblcinqindex10.get();}
     public void setTblcinqindex10(String tblcinqindex10){this.tblcinqindex10.set(tblcinqindex10);}
     // 
     public String getTblcinqindex11(){return tblcinqindex11.get();}
     public void setTblcinqindex11(String tblcinqindex11){this.tblcinqindex11.set(tblcinqindex11);}
     
     
}
