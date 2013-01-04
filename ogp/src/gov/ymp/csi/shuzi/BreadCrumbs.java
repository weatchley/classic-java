package gov.ymp.csi.shuzi;

import java.util.*;

/**
 * Sets and retrives an Arraylist of user's navigation history.
 * Extends UpdateRel class. 
 * 
 *<p>
 *<ul>
 *<li>Req. 7.1: records and update the audit trail<br>
 *<li>Req. 7.2: enables users to traverse the tree view of the trails<br>
 *<li>Req. 7.3: output the List of the trail<br>
 *</ul>  
 *
 *
 * @author Shuhei Higashi
 * @version 0.1

 */	

public class BreadCrumbs extends UpdateRel {
	
	private ArrayList relList = new ArrayList();
		
	public void setRel(Object usrSession, long appObjId){ //6.2
			
	}; //7.1, 7.2
	
	public ArrayList getRel(Object usrSession, long portletId){//6.1
		
		
		return relList;
		
	};  // 7.3
}
