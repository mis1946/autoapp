/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.sales;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Arsiela
 * Date Created : 01-17-2024
 */
public class SalesAgentTableList {
    private SimpleStringProperty tblindexrw; //
    private SimpleStringProperty tblindex01; //
    private SimpleStringProperty tblindex02; //
    private SimpleStringProperty tblindex03; //
    private SimpleStringProperty tblindex04; //
    private SimpleStringProperty tblindex05; //
    private SimpleStringProperty tblindex06; //
    private SimpleStringProperty tblindex07; //
    private SimpleStringProperty tblindex08; //
    private SimpleStringProperty tblindex09; // 
    private SimpleStringProperty tblindex10; //
    private SimpleStringProperty tblindex11; //

    SalesAgentTableList( 
                        String tblindexrw,
                        String tblindex01,
                        String tblindex02,
                        String tblindex03,
                        String tblindex04,
                        String tblindex05,
                        String tblindex06,
                        String tblindex07,
                        String tblindex08,
                        String tblindex09,
                        String tblindex10,
                        String tblindex11
                        ){

         this.tblindexrw = new SimpleStringProperty(tblindexrw);
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
    }

    //
    public String getTblindexrw(){return tblindexrw.get();}
    public void setTblindexrw(String tblindexrw){this.tblindexrw.set(tblindexrw);}
    //
    public String getTblindex01(){return tblindex01.get();}
    public void setTblindex01(String tblindex01){this.tblindex01.set(tblindex01);}
    //
    public String getTblindex02(){return tblindex02.get();}
    public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
    //
    public String getTblindex03(){return tblindex03.get();}
    public void setTblindex03(String tblindex03){this.tblindex03.set(tblindex03);}
    //
    public String getTblindex04(){return tblindex04.get();}
    public void setTblindex04(String tblindex04){this.tblindex04.set(tblindex04);}
    //
    public String getTblindex05(){return tblindex05.get();}
    public void setTblindex05(String tblindex05){this.tblindex05.set(tblindex05);}
    //
    public String getTblindex06(){return tblindex06.get();}
    public void setTblindex06(String tblindex06){this.tblindex06.set(tblindex06);}
    //
    public String getTblindex07(){return tblindex07.get();}
    public void setTblindex07(String tblindex07){this.tblindex07.set(tblindex07);}
    //
    public String getTblindex08(){return tblindex08.get();}
    public void setTblindex08(String tblindex08){this.tblindex08.set(tblindex08);}
    // 
    public String getTblindex09(){return tblindex09.get();}
    public void setTblindex09(String tblindex09){this.tblindex09.set(tblindex09);}
    // 
    public String getTblindex10(){return tblindex10.get();}
    public void setTblindex10(String tblindex10){this.tblindex10.set(tblindex10);}
    // 
    public String getTblindex11(){return tblindex11.get();}
    public void setTblindex11(String tblindex11){this.tblindex11.set(tblindex11);}

}
