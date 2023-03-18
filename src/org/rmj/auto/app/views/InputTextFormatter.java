/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.auto.app.views;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;

/**
 * Date : 3/14/2023
 * @author Arsiela
 */
public class InputTextFormatter extends TextFormatter<String> {

    public InputTextFormatter(Pattern pattern) {
        super(new NumericUnaryOperator(pattern));
    }

    private static class NumericUnaryOperator implements UnaryOperator<TextFormatter.Change> {

        private final Pattern pattern;

        public NumericUnaryOperator(Pattern pattern) {
            this.pattern = pattern;
        }

        @Override
        public TextFormatter.Change apply(TextFormatter.Change change) {
            if (!pattern.matcher(change.getControlNewText()).matches()) {
                return null;
            }
            return change;
        }
    }
}