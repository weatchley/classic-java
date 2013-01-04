package gov.ymp.opg.mypages;

import java.util.*;
import gov.ymp.csi.db.*;

public class TabSets {
	private long personID;
	private String userName;
	private Vector tabSets;
	
	public TabSets(long personID){
		this.personID = personID;
		if (tabSets == null || tabSets.isEmpty()) {
		
		}
	}
	
	public TabSets(long personID, Vector tabSets){
		this.personID = personID;
		if (tabSets.isEmpty()) {
		} else {
			this.tabSets = tabSets;
		}
	}
	
	public int getTabSetsSize(){
		return tabSets.size();
	}
		
	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public long getPersonID() {
		return this.personID;
	}
			
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}