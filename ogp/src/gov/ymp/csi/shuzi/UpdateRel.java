package gov.ymp.csi.shuzi;

import java.util.*;

/**
 * UpdateRel Class is an abstract class which sets and retrives an Arraylist of
 * user's navigation history.
 * 
 *<p>
 *<ul>
 *<li>Req. 6.1: retrieve the baseline relationship from configuration files  
 *<li>Req. 6.2: update the configuration files to reflect the up-to-date relationships
 *</ul>
 *
 * @author Shuhei Higashi
 * @version 0.1

 */

abstract class UpdateRel {
		
	private ArrayList relList = new ArrayList();
	
	/**
	 * Rerurns an ArrayList of user's navigation history. 
	 * usrSession Object and portletId arguments must be specified.
	 * 
	 * <p>
	 * Rerurns an ArrayList of user's navigation history. 
	 * usrSession Object and portletId arguments must be specified.
	 *
	 * @param  usrSession Object
	 * @param  portletId long 
	 * @return ArrayList     
	 */	
	public ArrayList getRel(Object usrSession, long portletId){//6.1
		
		return relList;
	}; 
	
	/**
	 * Sets user's navigation history 
	 * usrSession object and appObjId arguments must be specified.
	 * 
	 * <p>
	 * Sets user's navigation history 
	 * usrSession object and appObjId arguments must be specified.
	 *
	 * @param  usrSession Object
	 * @param  appObjId long 
	 */	
	public void setRel(Object usrSession, long appObjId){ //6.2
		
	};
	//calls UpdateLog class method to implement 7.4
		
}
