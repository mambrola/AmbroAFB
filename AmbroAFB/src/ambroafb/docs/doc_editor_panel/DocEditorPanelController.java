/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.doc_editor_panel;

import ambro.AFilterableTableView;
import ambro.AFilterableTreeTableView;
import ambro.ATableView;
import ambro.AView;
import ambroafb.docs.Doc;
import ambroafb.docs.dialog.DocDialog;
import ambroafb.docs.types.DocComponent;
import ambroafb.general.DataDistributor;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.interfaces.DocDialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class DocEditorPanelController implements Initializable {

    @FXML
    private Button exit, delete, edit, view;
    
    @FXML
    private SplitMenuButton add;
    
    @FXML
    private ToggleButton refresh;
    
    @FXML
    private TextField search;


    @FXML
    private MenuItem addBySample;
    
    @FXML
    private Initializable outerController;
    
    @FXML
    private Region region;

    @FXML
    private HBox formNode;
    
    private enum CLASS_TYPE {OBJECT, DIALOG, FILTER, CONTROLLER};
    
    private ObservableList<EditorPanelable> tableData;
    private final DocEditorPanelModel editorPanelModel = new DocEditorPanelModel();
    
    @FXML
    private void delete(ActionEvent e) {
        System.out.println("delete");
    }
    
    @FXML
    private void edit(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc docFromList = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            System.out.println("rec id: " + docFromList.getRecId());
            DocComponent docComp = editorPanelModel.getDocComponent(docFromList.getRecId());
            
            DocDialogable dialogable = new DocDialog(docComp, Names.EDITOR_BUTTON_TYPE.EDIT, (Stage) exit.getScene().getWindow());
            DataDistributor dataDis = dialogable.getResult();
            if (dataDis != null){
                System.out.println("--- make Ok ---\nDataDistribution is: " + dataDis);
            }
            else {
                System.out.println("--- make Cancel ---");
            }
        }
    }
    
    @FXML
    private void view(ActionEvent e) {
        System.out.println("view");
    }
    
    @FXML
    private void add(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            DocDialogable dialogable = new DocDialog(null, Names.EDITOR_BUTTON_TYPE.ADD, (Stage) exit.getScene().getWindow());
            DataDistributor dataDis = dialogable.getResult();
            if (dataDis != null){
                System.out.println("--- make Ok ---\nDataDistribution is: " + dataDis);
            }
            else {
                System.out.println("--- make Cancel ---");
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        System.out.println("add By Sample");
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        System.out.println("refresh");
    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * The method saves table data list and also provides to search element in table.
     * @param table Table component on scene.
     * @param list  Data list of given table (At the beginning, it may be empty).
     */
    public void setTableDataList(ATableView<EditorPanelable> table, ObservableList<EditorPanelable> list){
        tableData = list;
        table.setItems(list);
        if (table instanceof AFilterableTableView){
            AFilterableTableView<EditorPanelable> filterableTable = (AFilterableTableView) table;
            filterableTable.makeBindingsForFilterOn(search, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(search.getText().toLowerCase()));
        }
    }
    
    public void buttonsMainPropertysBinder (AView<EditorPanelable> aView){
        BooleanBinding allowModify = Bindings.createBooleanBinding(() -> {
                                                                    if (aView.getCustomSelectedItem() == null){
                                                                        return true;
                                                                    }
                                                                    return aView.getCustomSelectedItem().isAllowToModify().not().get();
                                                                }, aView.getCustomSelectionModel().selectedItemProperty());
        if (aView instanceof AFilterableTreeTableView){
            AFilterableTreeTableView<EditorPanelable> treeTable = (AFilterableTreeTableView)aView;
            delete.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                if (aView.getCustomSelectionModel().selectedItemProperty().isNull().get()) {
                    return true;
                }
                return !treeTable.getSelectionModel().getSelectedItem().isLeaf();
            }, aView.getCustomSelectionModel().selectedItemProperty()));
        }
        else if (aView instanceof ATableView){
            delete.disableProperty().bind(allowModify);
        }
        edit.disableProperty().bind(allowModify);
        view.disableProperty().bind(aView.getCustomSelectionModel().selectedItemProperty().isNull());
        addBySample.disableProperty().bind(aView.getCustomSelectionModel().selectedItemProperty().isNull());
    }
    
    /**
     * Returns panel width without space size before search field.
     * @return 
     */
    public double getPanelMinWidth(){
        return formNode.getWidth() - region.getWidth();
    }
    
    public Button getExitButton(){
        return exit;
    }
    
    public void setOuterController(Initializable controller){
        outerController = controller;
    }
    
}
