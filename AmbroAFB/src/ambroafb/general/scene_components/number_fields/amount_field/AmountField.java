/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields.amount_field;

import ambroafb.general.scene_components.number_fields.NumberField;

/**
 *
 * @author dkobuladze
 */
public class AmountField extends NumberField {
    
    public static final String FINALY_CONTENT_PATTERN = "(0|[1-9]\\d*)(\\.\\d{2})?";
    public static final String FINALY_CONTENT_DESCRIP = "Amount field content is incorrect"; // must be bundle key
    
    private final String extraIntegerValueLength = "{0,9}"; // default max length is 10, but 0 or [1-9] any digit on the first place - decrease this count by 1.
    private final String integerPart = "(0|[1-9]\\d" + extraIntegerValueLength + ")";
    private final String fractinoalPart = "(\\.|\\.\\d|\\.\\d\\d?)?";
    
    public AmountField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       contentRuntimePatternListener(integerPart + fractinoalPart);
    }
    
    /**
     *  The method changes  maximum lengths of float number integer part.
     * If maxLength less then 1, the change will not be apply. Note that, there is not possible to set range of length. 
     * The runtime pattern uses in text listener, so field must allow to press from 0 to maxLength digits.
     * @param maxLength The maximum length of float number integer part..
     */
    public void setIntegerPartLength(int maxLength){
        if (maxLength < 1) return;
        String newLength = "{0," + (maxLength - 1) + "}"; //  For numberField content on the first place must be 0 or [1-9] any digit, so max length count decrease by 1.
        String newPattern = integerPart.replace(extraIntegerValueLength, newLength) + fractinoalPart;
        contentRuntimePatternListener(newPattern);
    }
}