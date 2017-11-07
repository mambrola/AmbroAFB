/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.doc;

import ambro.AView;
import ambroafb.docs.Doc;
import ambroafb.docs.filter.DocFilter;
import ambroafb.docs.types.DocManagersFactory;
import ambroafb.docs.types.conversion.ConversionManager;
import ambroafb.docs.types.custom.CustomManager;
import ambroafb.docs.types.monthly.MonthlyManager;
import ambroafb.docs.types.utilities.charge.ChargeUtilityManager;
import ambroafb.docs.types.utilities.payment.PaymentUtilityManager;
import ambroafb.general.GeneralConfig;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.ListingStage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class DocEditorPanel extends EditorPanel implements Initializable {

    public DocEditorPanel() {
        assignLoader();
    }
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources){
        MenuItem addConversion = createMenuItem("doc_convert");
        addConversion.setOnAction(this::addConversion);
        
        Menu utilities = createMenu("doc_utility");
        MenuItem paymentUtility = createMenuItem("payment");
        paymentUtility.setOnAction(this::addPaymentUtility);
        MenuItem chargeUtility = createMenuItem("charge");
        chargeUtility.setOnAction(this::addChargeUtility);
        utilities.getItems().addAll(paymentUtility, chargeUtility);

        MenuItem addMonthlyAccrual = createMenuItem("doc_monthly_accrual");
        addMonthlyAccrual.setOnAction(this::addMonthlyAccrual);
        
        add.getItems().add(new SeparatorMenuItem());
        add.getItems().add(addConversion);
        add.getItems().add(utilities);
        add.getItems().add(new SeparatorMenuItem());
        add.getItems().add(addMonthlyAccrual);
    }
    
    public MenuItem createMenuItem(String nameBundleKey){
        MenuItem result = new MenuItem();
        result.setGraphic(createLabelFrom("/images/new.png", GeneralConfig.getInstance().getTitleFor("new"), GeneralConfig.getInstance().getTitleFor(nameBundleKey)));
        return result;
    }
    
    public Menu createMenu(String nameBundleKey){
        Menu result = new Menu();
        result.setGraphic(createLabelFrom("/images/new.png", GeneralConfig.getInstance().getTitleFor("new"), GeneralConfig.getInstance().getTitleFor(nameBundleKey)));
        return result;
    }
    
    private Label createLabelFrom(String imgPath, String TooltipText, String name){
        Label visual = new Label();
        visual.setTooltip(new Tooltip(TooltipText));
        visual.setText(name);
        HBox hBox = new HBox(4);
        ImageView imgView = new ImageView(imgPath);
        imgView.setFitWidth(18);
        imgView.setFitHeight(18);
        hBox.getChildren().add(imgView);
        hBox.getChildren().add(new Separator(Orientation.VERTICAL));
        visual.setGraphic(hBox);
        return visual;
    }

    @Override
    public void delete(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Doc selected = getSelectedDoc();
        EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
        Consumer<EditorPanelable> successAction = (obj) -> {
            Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.DELETE, obj);
            List<Doc> result = dialog.getResult();
            List<Doc> removedDocs = new ArrayList<>();
            result.stream().forEach((doc) -> {
                    removedDocs.add(tableData.stream().map((elem) -> (Doc)elem).filter((tableDoc) -> {
                                        if (doc.getRecId() != 0) return doc.getRecId() == tableDoc.getRecId();
                                        return doc.getParentRecId() == tableDoc.getParentRecId();
                                    }).findFirst().get());

            });
            tableData.removeAll(removedDocs);
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

    @Override
    public void edit(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Doc selected = getSelectedDoc();
        EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
        Consumer<EditorPanelable> successAction = (obj) -> {
            Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.EDIT, obj);
            List<Doc> result = dialog.getResult();
            result.stream().forEach((newDoc) -> {
                                Optional<Doc> docFromAFBTable = tableData.stream().map((elem) -> (Doc)elem).
                                                                    filter((Doc doc) -> doc.getRecId() == newDoc.getRecId()).findFirst();
                                if (docFromAFBTable.isPresent()){
                                    docFromAFBTable.get().copyFrom(newDoc);
                                }
            });
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

    @Override
    public void view(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Doc selected = getSelectedDoc();
        EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
        Consumer<EditorPanelable> successAction = (obj) -> {
            Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.VIEW, obj);
            dialog.showAndWait();
        };
        manager.getDataFetchProvider().getOneFromDB(selected.recId, successAction, null);
    }

    @Override
    public void add(ActionEvent event) {
        if (allowToOpenDialogOrFilter()) {
            makeAddAction(new CustomManager());
        }
    }
    
    public void addConversion(ActionEvent e){
        if (allowToOpenDialogOrFilter()) {
            makeAddAction(new ConversionManager());
        }
    }
    
    
    public void addPaymentUtility(ActionEvent e) {
        if (allowToOpenDialogOrFilter()) {
            makeAddAction(new PaymentUtilityManager());
        }
    }
    
    public void addChargeUtility(ActionEvent e) {
        if (allowToOpenDialogOrFilter()) {
            makeAddAction(new ChargeUtilityManager());
        }
    }
    
    public void addMonthlyAccrual(ActionEvent e){
        if (allowToOpenDialogOrFilter()) {
            makeAddAction(new MonthlyManager());
        }
    }
    
    private void makeAddAction(EditorPanelableManager manager){
        Dialogable dialog = manager.getDialogFor((Stage)getScene().getWindow(), EditorPanel.EDITOR_BUTTON_TYPE.ADD, null);
        List<Doc> docsFromDialog = dialog.getResult();
        if (docsFromDialog != null){
            tableData.addAll(0, docsFromDialog);
        }
    }

    @Override
    public void addBySample(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Doc selected = getSelectedDoc();
        EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
        Consumer<EditorPanelable> successAction = (obj) -> {
            EditorPanelable cloneFromReal = ((EditorPanelable)obj).cloneWithoutID(); // Without this coping, program make "edit" action.
            Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE, cloneFromReal);
            List<Doc> docsFromDialog = dialog.getResult();
            if (docsFromDialog != null && !docsFromDialog.isEmpty()){
                    tableData.addAll(0, docsFromDialog);
            }
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }
    
    // The method returns selected doc on table.
    private Doc getSelectedDoc(){
        return (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
    }

    @Override
    public void refresh(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Filterable filter = new DocFilter(editorPanelSceneStage);
        FilterModel model = filter.getResult();
        if (model != null && !model.isCanceled()){
            editorPanelSceneStage.getController().reAssignTable(model);
        }
        refresh.setSelected(false);
    }

}
