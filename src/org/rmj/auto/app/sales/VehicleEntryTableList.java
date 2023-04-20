/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleStringProperty;

/**
 * DATE CREATED: 04-04-2023
 * @author Arsiela
 */
public class VehicleEntryTableList {
        private SimpleStringProperty tblindex01; //Row
     private SimpleStringProperty tblindex02; //CS No
     private SimpleStringProperty tblindex03; //Plate No
     private SimpleStringProperty tblindex04; //Vehicle Description
     //For loading of Fields
     private SimpleStringProperty tblindex05; //Key No
     private SimpleStringProperty tblindex06; //Book No
     private SimpleStringProperty tblindex07; //Frame No
     private SimpleStringProperty tblindex08; //Engine No
     private SimpleStringProperty tblindex09; //Category 
     private SimpleStringProperty tblindex10; //DI Date
     private SimpleStringProperty tblindex11; //DI No
     private SimpleStringProperty tblindex12; //Vehicle Status
     private SimpleStringProperty tblindex13; //Location
     private SimpleStringProperty tblindex14; //Date Sold
     private SimpleStringProperty tblindex15; //DR No
     private SimpleStringProperty tblindex16; //Sold to
     
     VehicleEntryTableList( String tblindex01,
                         String tblindex02,
                         String tblindex03,
                         String tblindex04,
                         String tblindex05,
                         String tblindex06,
                         String tblindex07,
                         String tblindex08,
                         String tblindex09,
                         String tblindex10,
                         String tblindex11,
                         String tblindex12,
                         String tblindex13,
                         String tblindex14,
                         String tblindex15,
                         String tblindex16

                         ){
     
          this.tblindex01 = new SimpleStringProperty(tblindex01);
          this.tblindex02 = new SimpleStringProperty(tblindex02);
          this.tblindex03 = new SimpleStringProperty(tblindex03);
          this.tblindex04 = new SimpleStringProperty(tblindex04);
          
          this.tblindex05 = new SimpleStringProperty(tblindex05);
          this.tblindex06 = new SimpleStringProperty(tblindex06);
          this.tblindex07 = new SimpleStringProperty(tblindex07);
          this.tblindex08 = new SimpleStringProperty(tblindex08);
          this.tblindex09 = new SimpleStringProperty(tblindex09);
          this.tblindex10 = new SimpleStringProperty(tblindex10);
          this.tblindex11 = new SimpleStringProperty(tblindex11);
          this.tblindex12 = new SimpleStringProperty(tblindex12);
          this.tblindex13 = new SimpleStringProperty(tblindex13);
          this.tblindex14 = new SimpleStringProperty(tblindex14);
          this.tblindex15 = new SimpleStringProperty(tblindex15);
          this.tblindex16 = new SimpleStringProperty(tblindex16);
          
     }
     
     //Row
     public String getTblindex01(){return tblindex01.get();}
     public void setTblindex01(String tblindex01){this.tblindex01.set(tblindex01);}
     //CS No
     public String getTblindex02(){return tblindex02.get();}
     public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
     //Plate No
     public String getTblindex03(){return tblindex03.get();}
     public void setTblindex03(String tblindex03){this.tblindex03.set(tblindex03);}
     //Vehicle Description
     public String getTblindex04(){return tblindex04.get();}
     public void setTblindex04(String tblindex04){this.tblindex04.set(tblindex04);}
     
     //For loading of Fields
     //Key No
     public String getTblindex05(){return tblindex05.get();}
     public void setTblindex05(String tblindex05){this.tblindex05.set(tblindex05);}
     //Book No
     public String getTblindex06(){return tblindex06.get();}
     public void setTblindex06(String tblindex06){this.tblindex06.set(tblindex06);}
     //Frame No
     public String getTblindex07(){return tblindex07.get();}
     public void setTblindex07(String tblindex07){this.tblindex07.set(tblindex07);}
     //Engine No
     public String getTblindex08(){return tblindex08.get();}
     public void setTblindex08(String tblindex08){this.tblindex08.set(tblindex08);}
     // Category
     public String getTblindex09(){return tblindex09.get();}
     public void setTblindex09(String tblindex09){this.tblindex09.set(tblindex09);}
     //DI Date 
     public String getTblindex10(){return tblindex10.get();}
     public void setTblindex10(String tblindex10){this.tblindex10.set(tblindex10);}
     // DI No
     public String getTblindex11(){return tblindex11.get();}
     public void setTblindex11(String tblindex11){this.tblindex11.set(tblindex11);}
     //Vehicle Status
     public String getTblindex12(){return tblindex12.get();}
     public void setTblindex12(String tblindex12){this.tblindex12.set(tblindex12);}
     //Location
     public String getTblindex13(){return tblindex13.get();}
     public void setTblindex13(String tblindex13){this.tblindex13.set(tblindex13);}
     //Date Sold  
     public String getTblindex14(){return tblindex14.get();}
     public void setTblindex14(String tblindex14){this.tblindex14.set(tblindex14);}
     //DR No
     public String getTblindex15(){return tblindex15.get();}
     public void setTblindex15(String tblindex15){this.tblindex15.set(tblindex15);}
     //Sold to  
     public String getTblindex16(){return tblindex16.get();}
     public void setTblindex16(String tblindex16){this.tblindex16.set(tblindex16);}
       
     
}
