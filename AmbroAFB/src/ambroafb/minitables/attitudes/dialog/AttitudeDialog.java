/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.minitables.attitudes.Attitude;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class AttitudeDialog extends UserInteractiveStage implements Dialogable {

    private Attitude attitude;
    private final Attitude attitudeBackup;
    
    private AttitudeDialogController dialogController;
    
    public AttitudeDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "attitude", "/images/dialog.png");
        
        if (object == null)
            this.attitude = new Attitude();
        else
            this.attitude = (Attitude) object;
        this.attitudeBackup = attitude.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/attitudes/dialog/AttitudeDialog.fxml", null);
        dialogController = (AttitudeDialogController) currentScene.getProperties().get("controller");
        dialogController.bindAttitude(this.attitude);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupAttitude(this.attitudeBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
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
    
}