/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.docs.types.custom.Custom;
import ambroafb.docs.types.monthly.Monthly;
import ambroafb.docs.types.refund.Refund;
import ambroafb.docs.types.utilities.Utility;

/**
 *
 * @author dkobuladze
 */
public class DocDialogsFactory {
    
    public static DocDialogAbstraction getDocDialogObject(int id){
        switch (id) {
            case 1:
                return new Custom();
            case 2:
                return new Utility();
            case 3:
                return new Monthly();
            case 4:
                return new Refund();
            default:
                break;
        }
        return null;
    }
}