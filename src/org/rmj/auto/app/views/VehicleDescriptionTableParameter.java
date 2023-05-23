/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Arsiela
 * Date Created: 05-23-2023
 */
public class VehicleDescriptionTableParameter {
    private SimpleStringProperty tblindex01; //Row
    private SimpleStringProperty tblindex02; //Description
    private SimpleStringProperty tblindex03; //Code
    private SimpleStringProperty tblindex04; //sSourceID
    
    VehicleDescriptionTableParameter(   String tblindex01,
                                        String tblindex02,
                                        String tblindex03,
                                        String tblindex04
                                    ){

         this.tblindex01 = new SimpleStringProperty(tblindex01);
         this.tblindex02 = new SimpleStringProperty(tblindex02);
         this.tblindex03 = new SimpleStringProperty(tblindex03);
         this.tblindex04 = new SimpleStringProperty(tblindex04);
    }

    //Row
    public String getTblindex01(){return tblindex01.get();}
    public void setTblindex01(String tblindex01){this.tblindex01.set(tblindex01);}
    //Description
    public String getTblindex02(){return tblindex02.get();}
    public void setTblindex02(String tblindex02){this.tblindex02.set(tblindex02);}
    //Code
    public String getTblindex03(){return tblindex03.get();}
    public void setTblindex03(String tblindex03){this.tblindex03.set(tblindex03);}
    //sSourceID
    public String getTblindex04(){return tblindex04.get();}
    public void setTblindex04(String tblindex04){this.tblindex04.set(tblindex04);}


     
}
