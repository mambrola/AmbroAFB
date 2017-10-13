/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes.dialog;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.minitables.attitudes.Attitude;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class AttitudeDialog extends UserInteractiveDialogStage implements Dialogable {

    private Attitude attitude;
    private final Attitude attitudeBackup;
    
    public AttitudeDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/minitables/attitudes/dialog/AttitudeDialog.fxml", "attitude");
        
        if (object == null)
            attitude = new Attitude();
        else
            attitude = (Attitude) object;
        attitudeBackup = attitude.cloneWithID();
        
        dialogController.setSceneData(attitude, attitudeBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return attitude;
    }

    @Override
    public void operationCanceled() {
        attitude = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}