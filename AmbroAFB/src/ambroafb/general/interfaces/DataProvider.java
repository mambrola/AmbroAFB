/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.GeneralConfig;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public abstract class DataProvider {
    
    public final JSONObject PARAM_ALL = new ConditionBuilder().build();
    
    protected String DB_VEIW_NAME = "";
    
    
    protected void deleteObjectFromDB(String deleteProcName, int id) throws IOException, AuthServerException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        dbClient.callProcedure(deleteProcName, id);
    }
    
    
    /**
     *  The method returns list by condition.
     * @param params The JSON object for condition.
     * @return List of EditorPanelable object by condition. If nothing exists on given condition or make exception, then returns empty list.
     */
    public abstract List<EditorPanelable> getListByConditoin(JSONObject params);
    
    
    /**
     *  The method gets one by id.
     * @param recId The unique identifier for object.
     * @return Object on given recId.
     */
    public abstract EditorPanelable getOneFromDB(int recId);
    
    
    /**
     *  The method removes one object by recId.
     * @param recId The unique identifier for object.
     * @param success The action when delete was successful. It will call in Platform.runLater and before stage close. 
     *                                      If nothing is executed, then gives null value.
     * @param error The action when delete was not successful.  If nothing is executed, then gives null value.
     */
    public abstract void deleteOneFromDB(int recId, Function<Object, ButtonType> success, Function<Exception, ButtonType> error);
    
    
    /**
     *  The method changes existed object.
     * @param object The object that must be change.
     * @param success The action when delete was successful. It will call in Platform.runLater and before stage close. 
     *                                      If nothing is executed, then gives null value.
     * @param error The action when delete was not successful. If nothing is executed, then gives null value.
     */
    public abstract void editOneToDB(EditorPanelable object, Function<Object, ButtonType> success, Function<Exception, ButtonType> error);
    
    
    /**
     *  The method saveOneToDB new object.
     * @param object The new Object.
     * @param success The action when delete was successful. It will call in Platform.runLater and before stage close. 
     *                                      If nothing is executed, then gives null value.
     * @param error The action when delete was not successful.  If nothing is executed, then gives null value.
     */
    public abstract void saveOneToDB(EditorPanelable object, Function<Object, ButtonType> success, Function<Exception, ButtonType> error);
    
}