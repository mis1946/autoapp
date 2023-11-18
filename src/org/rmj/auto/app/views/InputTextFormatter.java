/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.text.NumberFormat;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import static org.rmj.appdriver.agentfx.CommonUtils.NumberFormat;

/**
 * Date : 3/14/2023
 * @author Arsiela
 */

public class InputTextFormatter extends TextFormatter<String> {

//    public InputTextFormatter() {
//        super(new NumericUnaryOperator());
//    }
    
    public InputTextFormatter(Pattern pattern) {
        super(new NumericUnaryOperator(pattern));
    }


    private static class NumericUnaryOperator implements UnaryOperator<TextFormatter.Change> {

        //private final Pattern pattern = Pattern.compile("^\\d*(,\\d{0,2})?(\\.\\d{0,2})?$");
        
        private final Pattern pattern;

        public NumericUnaryOperator(Pattern pattern) {
            this.pattern = pattern;
        }
        
        @Override
        public TextFormatter.Change apply(TextFormatter.Change change) {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                //System.out.println("Accepted: " + change.getControlNewText());
                return change;
            } else {
                //System.out.println("Rejected: " + change.getControlNewText());
                return null;
            }
        }
    }
   
}


//public class InputTextFormatter extends TextFormatter<String> {
//
//    public InputTextFormatter(Pattern pattern) {
//        super(new NumericUnaryOperator(pattern));
//    }
//
//    private static class NumericUnaryOperator implements UnaryOperator<TextFormatter.Change> {
//
//        private final Pattern pattern;
//
//        public NumericUnaryOperator(Pattern pattern) {
//            this.pattern = pattern;
//        }
//
//        @Override
//        public TextFormatter.Change apply(TextFormatter.Change change) {
//            if (!pattern.matcher(change.getControlNewText()).matches()) {
//                return null;
//            }
//            return change;
//        }
//    }
//}