/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.Names;
import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 *
 * @author dkobuladze
 */
public class BalanceAccountsControllerTest {
    
    private final BalanceAccountsController instance = new BalanceAccountsController();
    
    ArrayList<EditorPanelable> spyList = Mockito.spy(ArrayList.class);
    @Mock
    BalanceAccountsController.BalanceAccountsRunnable balanceAccountsRannable;
    
    
    public BalanceAccountsControllerTest() {
    }
    
    @Before
    public void setUp() {
        spyList.add(createBalAccount(1, 1000, "Current Actives"));
            spyList.add(createBalAccount(1, 1100, "Money in cashBox"));
                spyList.add(createBalAccount(1, 1110, "Money in Georgian Currency"));
            spyList.add(createBalAccount(1, 1200, "Money in Bank"));
                spyList.add(createBalAccount(1, 1210, "Acreditive"));
            spyList.add(createBalAccount(1, 1300, "Short-term Investments"));
                spyList.add(createBalAccount(1, 1310, "Short-term Investments in Shares"));
    }
    
    private BalanceAccount createBalAccount(int actPas, int balAccCode, String descrip){
        BalanceAccount balAcc = new BalanceAccount();
        balAcc.setActPas(actPas);
        balAcc.setBalAcc(balAccCode);
        balAcc.setDescrip(descrip);
        return balAcc;
    }
    
    @After
    public void tearDown() {
        spyList.clear();
    }

    /**
     * Test of reAssignTable method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testReAssignTable() {
        Supplier<ArrayList<EditorPanelable>> supplier = () -> spyList;
        instance.reAssignTable(supplier);
        
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(BalanceAccountsControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Mockito.verify(supplier).get();
    }

    /**
     * Test of accountAlreadyExistForCode method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testAccountAlreadyExistForCode() {
        System.out.println("accountAlreadyExistForCode");
        EditorPanelable newElem = null;
        Names.EDITOR_BUTTON_TYPE type = null;
        BalanceAccountsController instance = new BalanceAccountsController();
        boolean expResult = false;
        boolean result = instance.accountAlreadyExistForCode(newElem, type);
    }

    /**
     * Test of accountHasParent method, of class BalanceAccountsController.
     */
    @Test @Ignore
    public void testAccountHasParent() {
        System.out.println("accountHasParent");
        String balAccCode = "";
        BalanceAccountsController instance = new BalanceAccountsController();
        boolean expResult = false;
        boolean result = instance.accountHasParent(balAccCode);
    }
    
}