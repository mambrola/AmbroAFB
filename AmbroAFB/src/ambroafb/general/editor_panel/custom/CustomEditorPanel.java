/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.custom;

import ambro.AView;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.ListingStage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class CustomEditorPanel extends EditorPanel {

    public CustomEditorPanel() {
        assignLoader();
    }
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources){
    }
    
    @Override
    public void delete(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (objFromDB) -> {
                if (objFromDB != null){
                    selected.copyFrom((EditorPanelable)objFromDB);
                }
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.DELETE, selected);
                EditorPanelable result = dialog.getResult();
                if (result != null){
                    tableData.remove(selected);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
                
        } else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void edit(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager(); // EPManagerFactory.getEPManager(selected);
            Consumer<Object> successAction = (ObjFromDB) -> {
                if (ObjFromDB != null) {
                    selected.copyFrom((EditorPanelable)ObjFromDB);
                }
                EditorPanelable backup = selected.cloneWithID();
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.EDIT, selected);
                EditorPanelable result = dialog.getResult();
                if (result == null){
                    selected.copyFrom(backup);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void view(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (ObjFromDB) -> {
                if (ObjFromDB != null) {
                    selected.copyFrom((EditorPanelable)ObjFromDB);
                }
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.VIEW, selected);
                dialog.showAndWait();
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);

        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void add(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.ADD, null);
            
            EditorPanelable result = dialog.getResult();
            if (result != null) {
                tableData.add(result);
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void addBySample(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (objFromDB) -> {
                EditorPanelable cloneOfSelected;
                if (objFromDB != null) {
                    cloneOfSelected = ((EditorPanelable)objFromDB).cloneWithoutID();
                }
                else {
                    cloneOfSelected = selected.cloneWithoutID();
                }

                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE, cloneOfSelected);
                EditorPanelable result = dialog.getResult();
                if (result != null) {
                    tableData.add(result);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void refresh(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage filterStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (filterStage == null || !filterStage.isShowing()){
            Filterable filter = editorPanelSceneStage.getEPManager().getFilterFor(editorPanelSceneStage);
            FilterModel model = (filter != null) ? filter.getResult() : null;
            if (model != null && !model.isCanceled()){
                editorPanelSceneStage.getController().reAssignTable(model);
            }
        }
        else {
            filterStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, filterStage);
        }
        refresh.setSelected(false);
    }

    
}
