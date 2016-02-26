/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.AmbroAFB;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class AlertMessage extends Alert {
    
    /**
     * კონსტრუქტორი უზრუნველყოფს შექმნას შესაბამისი AlertType alert-ი.
     * დაუსეტავს ფანჯარას დასახელებას და ასევე დასასელებას, თუ რის შესახებაა დიალოგი. , 
     * @param alertType    - მესიჯის ტიპი, (AlertType.ERROR, AlertType.WARNING, AlertType.CONFIRMATION ...)
     * @param message  - შიდა, კონტექსტის დასახელება
     */
    public AlertMessage(AlertType alertType, Exception ex, String messageName) {
        super(alertType);
        setTitle(GeneralConfig.getInstance().getTitleFor(Names.ALERT_ERROR_WINDOW_TITLE));
        setHeaderText(GeneralConfig.getInstance().getTitleFor(messageName));
        
        
        getDialogPane().getScene().getStylesheets().add("/ambroafb/general/core.css");
        ((Stage)getDialogPane().getScene().getWindow()).initOwner(AmbroAFB.mainStage);

        Utils.log(messageName, ex);
    }
    
    /**
     * მეთოდი უზრუნველყოფს შეცდომის ტექსტის გამოჩენას ეკრანზე. 
     * Alert-ი ჩანს მანამ სანამ მომხმარებელი არ დააჭერს OK ღილაკს. 
     */
    public void showAlert(){
        showAndWait();
    }
    
}
