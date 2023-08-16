/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.auto.app.parts;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author John Dave Date Created: 8-4-2023
 */
public class ItemEntryModelTable {

    private SimpleStringProperty tblindexRow;
    private SimpleStringProperty tblindex01;
    private SimpleStringProperty tblindex02;
    private CheckBox select;
    private SimpleStringProperty tblIndex06_mdl;
    private SimpleStringProperty tblIndex07_mdl;
    private SimpleStringProperty tblIndex03_yr;
    private SimpleStringProperty tblIndexRow_mdlyr;

    public ItemEntryModelTable(String tblindexRow, String tblindex01, String tblindex02, String tblIndex06_mdl, String tblIndex07_mdl, String tblIndex03_yr, String tblIndexRow_mdlyr) {
        this.tblindexRow = new SimpleStringProperty(tblindexRow);
        this.tblindex01 = new SimpleStringProperty(tblindex01);
        this.tblindex02 = new SimpleStringProperty(tblindex02);
        this.select = new CheckBox();
        this.tblIndex06_mdl = new SimpleStringProperty(tblIndex06_mdl);
        this.tblIndex07_mdl = new SimpleStringProperty(tblIndex07_mdl);
        this.tblIndex03_yr = new SimpleStringProperty(tblIndex03_yr);
        this.tblIndexRow_mdlyr = new SimpleStringProperty(tblIndexRow_mdlyr);
    }

    public String getTblindexRow() {
        return tblindexRow.get();
    }

    public void setTblindexRow(String tblindexRow) {
        this.tblindexRow.set(tblindexRow);
    }

    public String getTblindex01() {
        return tblindex01.get();
    }

    public void setTblindex01(String tblindex01) {
        this.tblindex01.set(tblindex01);
    }

    public String getTblindex02() {
        return tblindex02.get();
    }

    public void setTblindex02(String tblindex02) {
        this.tblindex02.set(tblindex02);
    }

    public CheckBox getSelect() {
        return select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public String getTblIndex06_mdl() {
        return tblIndex06_mdl.get();
    }

    public void setTblIndex06_mdl(String tblIndex06_mdl) {
        this.tblIndex06_mdl.set(tblIndex06_mdl);
    }

    public String getTblIndex07_mdl() {
        return tblIndex07_mdl.get();
    }

    public void setTblIndex07_mdl(String tblIndex07_mdl) {
        this.tblIndex07_mdl.set(tblIndex07_mdl);
    }

    public String getTblIndex03_yr() {
        return tblIndex03_yr.get();
    }

    public void setTblIndex03_yr(String tblIndex03_yr) {
        this.tblIndex03_yr.set(tblIndex03_yr);
    }

    public String getTblIndexRow_mdlyr() {
        return tblIndexRow_mdlyr.get();
    }

    public void setTblIndexRow_mdlyr(String tblIndexRow_mdlyr) {
        this.tblIndexRow_mdlyr.set(tblIndexRow_mdlyr);
    }

}
