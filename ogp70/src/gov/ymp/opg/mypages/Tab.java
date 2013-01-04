package gov.ymp.opg.mypages;

import gov.ymp.csi.items.TextItem;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
/**
* Tab is the child class of CSI TextItem class extended for the specialized use
* @author   S. Higashi
*/
public class Tab extends TextItem {
	
	public Tab(){
		super();
	}
	
    public Tab (long id, DbConn myConn) {
    	super(id,myConn);
    }
   
}