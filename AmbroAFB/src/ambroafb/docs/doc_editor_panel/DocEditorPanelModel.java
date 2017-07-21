/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.doc_editor_panel;

import ambroafb.general.interfaces.DocsDataManager;

/**
 *
 * @author dkobuladze
 */
public class DocEditorPanelModel {
    
    private DocsDataManager dataManager;
    
    public DocEditorPanelModel(DocsDataManager dm){
        dataManager = dm;
    }
    
    public DocEditorPanelModel(){
        this(new DBDocsDataManager());
    }
    
    
}