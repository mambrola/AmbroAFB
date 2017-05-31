/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.minitables.attitudes.Attitude;
import ambroafb.minitables.merchandises.Merchandise;
import ambroafb.params_general.helper.ParamGeneralDBResponse;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    public static final String DB_SELECT_PROC_NAME = "process_general_param_select";
    public static final String DB_INSERT_UPDATE_PROC_NAME = "process_general_param_insert_update";
                                                
    @AView.Column(title = "%client", width = "150", styleClass = "textCenter")
    private final StringProperty clientDescrip;
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%attitude", width = "100", styleClass = "textCenter")
    private final StringProperty attitudeDescrip;
    private final ObjectProperty<Attitude> attitudeObj;
    
    @AView.Column(title = "%merchandise", width = "100", styleClass = "textCenter")
    private final StringProperty merchandiseDescrip;
    private final ObjectProperty<Merchandise> merchandiseObj;
    
    @AView.Column(title = "%param_type", width = "150")
    private final StringProperty paramType;
    
    @AView.Column(title = "%param", width = "100")
    private final StringProperty param;
    
    private static final String ALL = "ALL";
    
    public ParamGeneral(){
        clientDescrip = new SimpleStringProperty(ALL);
        clientObj = new SimpleObjectProperty<>(new Client());
        clientObj.get().setFirstName(ALL);
        clientObj.get().setRecId(0);
        
        attitudeDescrip = new SimpleStringProperty(ALL);
        attitudeObj = new SimpleObjectProperty<>(new Attitude());
        attitudeObj.get().setDescrip(ALL);
        attitudeObj.get().setRecId(0);
        
        merchandiseDescrip = new SimpleStringProperty(ALL);
        merchandiseObj = new SimpleObjectProperty<>(new Merchandise());
        merchandiseObj.get().setDescrip(ALL);
        merchandiseObj.get().setRecId(0);
        
        paramType = new SimpleStringProperty("");
        param = new SimpleStringProperty("");
        
        clientObj.addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            resetClientDescrip();
        });
        
        attitudeObj.addListener((ObservableValue<? extends Attitude> observable, Attitude oldValue, Attitude newValue) -> {
            rebindAttitudeDescrip();
        });
        rebindAttitudeDescrip();
        
        merchandiseObj.addListener((ObservableValue<? extends Merchandise> observable, Merchandise oldValue, Merchandise newValue) -> {
            rebindMerchandiseDescrip();
        });
        rebindMerchandiseDescrip();
    }
    
    private void resetClientDescrip(){
        Client newValue = clientObj.get();
        if (newValue != null){
            clientDescrip.set(newValue.getShortDescrip(",").get());
        }
    }
    
    private void rebindAttitudeDescrip(){
        attitudeDescrip.unbind();
        if (attitudeObj.get() != null){
            attitudeDescrip.bind(attitudeObj.get().descripProperty());
        }
    }
    
    private void rebindMerchandiseDescrip(){
        merchandiseDescrip.unbind();
        if (merchandiseObj.get() != null){
            merchandiseDescrip.bind(merchandiseObj.get().descripProperty());
        }
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        JSONObject json = new ConditionBuilder().build();
        ParamGeneralDBResponse paramsGeneralResponse = DBUtils.getParamsGeneral(DB_SELECT_PROC_NAME, json);
//        DBUtils.getObjectsListFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, lang, json) ;// 
        ArrayList<ParamGeneral> paramsGeneral = paramsGeneralResponse.getParamGenerals();
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p1.getRecId() - p2.getRecId());
        return paramsGeneral;
    }
    
    public static ParamGeneral getOneFromDB(int id) {
        JSONObject json = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, json);
    }
    
    public static ParamGeneral saveOneToDB(ParamGeneral genParam) {
        if (genParam == null) return null;
        return DBUtils.saveParamGeneral(genParam, DB_INSERT_UPDATE_PROC_NAME);
    }
    
    public static boolean deleteOneFromDB(int id) {
        System.out.println("delete general_params ??");
        return false;
    }
    
    
    // Propertis:
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public ObjectProperty<Attitude> attitudeProperty(){
        return attitudeObj;
    }
    
    public StringProperty attitudeDescripProperty(){
        return attitudeDescrip;
    }
    
    public ObjectProperty<Merchandise> merchandiseProperty(){
        return merchandiseObj;
    }
    
    public StringProperty merchandiseDescripProperty(){
        return merchandiseDescrip;
    }
    
    public StringProperty paramTypeProperty() {
        return paramType;
    }

    public StringProperty paramProperty() {
        return param;
    }
    
    
    // Getters:
    private Integer getIdFrom(EditorPanelable object){
        Integer result = null;
        if (object != null){
            int id = object.getRecId();
            result = (id == 0) ? null : id;  // For DB we need that method returns null;
        }
        return result;
    }
    
    public Integer getClientId() {
        return getIdFrom(clientObj.get());
    }
    
    public Integer getAttitude() {
        return getIdFrom(attitudeObj.get());
    }

    @JsonIgnore
    public String getAttitudeDescrip() {
        return (attitudeObj.get() != null) ? attitudeObj.get().getDescrip() : attitudeDescrip.get();
    }

    public Integer getMerchandise() {
        return getIdFrom(merchandiseObj.get());
    }

    @JsonIgnore
    public String getMerchandiseDescrip() {
        return merchandiseDescrip.get();
    }

    public String getParamType() {
        return paramType.get();
    }

    public String getParam() {
        return param.get();
    }
    
    
    // Setters:
    public void setClientId(Integer clientId) { // It is needed in "copyFrom" method.
        if (clientObj.get() != null){
            clientObj.get().setRecId((clientId == null) ? 0 : clientId);
        }
    }

    public void setAttitude(Integer attitudeId) {
        if (attitudeObj.get() != null){
            attitudeObj.get().setRecId((attitudeId == null) ? 0 : attitudeId);
        }
    }
    
    @JsonProperty
    public void setAttitudeDescrip(String descrip) {
        if (attitudeObj.get() != null){
            attitudeObj.get().setDescrip(descrip);
        }
    }

    public void setMerchandise(Integer merchandiseId) {
        if (merchandiseObj.get() != null){
            merchandiseObj.get().setRecId((merchandiseId == null) ? 0 : merchandiseId);
        }
    }
    
    @JsonProperty
    public void setMerchandiseDescrip(String descrip) {
        if (merchandiseObj.get() != null){
            merchandiseObj.get().setDescrip(descrip);
        }
    }

    public void setParamType(String paramType) {
        this.paramType.set(paramType);
    }

    public void setParam(String param) {
        this.param.set(param);
    }
    
    

    // Overrides:
    @Override
    public ParamGeneral cloneWithoutID() {
        ParamGeneral clone = new ParamGeneral();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public ParamGeneral cloneWithID() {
        ParamGeneral clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        ParamGeneral otherParamGeneral = (ParamGeneral)other;
        
        clientObj.set(otherParamGeneral.clientProperty().get());
        setClientId(Utils.avoidNullAndReturnInt(otherParamGeneral.getClientId()));
        
        attitudeObj.get().copyFrom(otherParamGeneral.attitudeProperty().get());
//        setAttitude(Utils.avoidNullAndReturnInt(otherParamGeneral.getAttitude()));
//        setAttitudeDescrip(otherParamGeneral.getAttitudeDescrip());
        merchandiseObj.get().copyFrom(otherParamGeneral.merchandiseProperty().get());
//        setMerchandise(Utils.avoidNullAndReturnInt(otherParamGeneral.getMerchandise()));
//        setMerchandiseDescrip(otherParamGeneral.getMerchandiseDescrip());
        
        setParamType(otherParamGeneral.getParamType());
        setParam(otherParamGeneral.getParam());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ParamGeneral other = (ParamGeneral)backup;
        return  Utils.avoidNullAndReturnInt(getClientId()) == Utils.avoidNullAndReturnInt(other.getClientId()) &&
                attitudeObj.get().compares(other.attitudeProperty().get()) &&
//                Utils.avoidNullAndReturnInt(getAttitude()) == Utils.avoidNullAndReturnInt(other.getAttitude()) &&
//                getAttitudeDescrip().equals(other.getAttitudeDescrip()) &&
//                Utils.avoidNullAndReturnInt(getMerchandise()) == Utils.avoidNullAndReturnInt(other.getMerchandise()) &&
//                getMerchandiseDescrip().equals(other.getMerchandiseDescrip()) &&
//                merchandiseObj.get().compares(other.attitudeObjProperty().get()) &&
                merchandiseObj.get().compares(other.merchandiseProperty().get()) &&
                getParamType().equals(other.getParamType()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
//        int clientID = Utils.avoidNullAndReturnInt(getClientId());
//        String clientDescr = ""; // (clientID <= 0) ? ALL : "" + clientID;
        
//        int attitudeID = Utils.avoidNullAndReturnInt(getAttitude());
//        String attitudeStr = (attitudeID <= 0) ? ALL : "" + attitudeID;

        int merchandiseID =  Utils.avoidNullAndReturnInt(getMerchandise());
        String merchandiseStr = (merchandiseID <= 0) ? ALL : "" + merchandiseID;
        return  clientDescrip.get() + " " + attitudeDescrip.get() + " " + merchandiseDescrip.get() + " " +
                    getParamType() + " " + getParam();
    }
    
    @Override
    public String toString(){
        String delimiter = "; ";
        return  clientDescrip.get() + delimiter + attitudeDescrip.get() + delimiter + 
                merchandiseDescrip.get() + delimiter + getParamType() + delimiter + getParam();
    }
    
    
    
//    public static class ParamsCellFactory implements Callback<TableColumn<ParamGeneral, String>, TableCell<ParamGeneral, String>> {
//
//        @Override
//        public TableCell<ParamGeneral, String> call(TableColumn<ParamGeneral, String> param) {
//            return new TableCell<ParamGeneral, String>() {
//                @Override
//                public void updateItem(String str, boolean empty) {
//                    setText(empty ? null : (str.isEmpty() ? "ALL" : str));
//                }
//            };
//        }
//    } 
//    
//    public static class AttitudeCellFactory implements Callback<TableColumn<ParamGeneral, Attitude>, TableCell<ParamGeneral, Attitude>> {
//
//        @Override
//        public TableCell<ParamGeneral, Attitude> call(TableColumn<ParamGeneral, Attitude> param) {
//            return new TableCell<ParamGeneral, Attitude>() {
//                @Override
//                public void updateItem(Attitude attitude, boolean empty) {
//                    setText(empty ? null : (attitude.getDescrip().isEmpty() ? "ALL" : attitude.getDescrip()));
//                }
//            };
//        }
//    } 
//    
//    public static class MerchandiseCellFactory implements Callback<TableColumn<ParamGeneral, Merchandise>, TableCell<ParamGeneral, Merchandise>> {
//
//        @Override
//        public TableCell<ParamGeneral, Merchandise> call(TableColumn<ParamGeneral, Merchandise> param) {
//            return new TableCell<ParamGeneral, Merchandise>() {
//                @Override
//                public void updateItem(Merchandise attitude, boolean empty) {
//                    setText(empty ? null : (attitude.getDescrip().isEmpty() ? "ALL" : attitude.getDescrip()));
//                }
//            };
//        }
//    } 
}
