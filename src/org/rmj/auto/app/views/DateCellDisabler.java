/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.util.Callback;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;

/**
 *
 * @author MIS
 */
public class DateCellDisabler {
    public static GRider oApp;
    
    /*Convert Date to String*/
//    public static LocalDate strToDate(String val) {
//        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(val, date_formatter);
//        return localDate;
//    }
    
    public static Callback<DatePicker, DateCell> createDisableDateCallback(int daysToDisable) {
        return param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setDisable(true);
                    return;
                }

                // Get today's date
                LocalDate today = LocalDate.now();

                // Calculate the minimum and maximum dates for disabling
                LocalDate minDate = today.minusDays(0);
                LocalDate maxDate = today.plusDays(daysToDisable);

                setDisable(item.isBefore(minDate) || item.isAfter(maxDate));
            }
        };
    }
//        
//        return param -> new DateCell() {
//            @Override
//            public void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if (item == null || empty) {
//                    setDisable(true);
//                    return;
//                }
//                Date serverDate = oApp.getServerDate();
//
//                LocalDate minDate = strToDate(CommonUtils.xsDateShort(serverDate));
//                LocalDate maxDate = strToDate(CommonUtils.xsDateShort(serverDate));
//
//                maxDate = maxDate.plusDays(daysToDisable);
//
//                setDisable(item.isBefore(minDate) || item.isAfter(maxDate));
//            }
//        };
//    }
    
}  
