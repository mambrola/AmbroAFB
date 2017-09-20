/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.conversion.dialog.ConversionDialog;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class ConversionManager implements DocManager {

    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";
    
    @Override
    public EditorPanelable getOneFromDB(int id) {
        return new Conversion();
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        return DBUtils.saveConversionDoc((Conversion)newDocComponent);
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, id);
    }

    @Override
    public void undo() {
    }

    @Override
    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        ConversionDialog dialog = new ConversionDialog(object, type, owner);
        return dialog;
    }
    
}