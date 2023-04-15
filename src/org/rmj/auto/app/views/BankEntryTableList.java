/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.views;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author John Dave
 */
public class BankEntryTableList {
    
    
    private SimpleStringProperty tblindex01;
    
    
    BankEntryTableList(String tblindex01){
        this.tblindex01 = new SimpleStringProperty(tblindex01);
    }

    public String getTblindex01() { 
        return tblindex01.get();
    }
    public void setTblindex01(String tblindex01){
        this.tblindex01.set(tblindex01);
    }
    
}
