package gov.ymp.csi.shuzi;

import java.util.*;

/**
 * UiProfile Class Summary Comment.
 * 
 * 
 *<p>
 * 
 *<ul>
 *	<li>Req. 9: updates profile<br>
 *	<ul>
 *		<li>Req. 9.1: Automatically updates the profile through user behavior<br>
 * 		<ul>
 *			<li>Req. 9.1.1 sets choice of skin<br>
 *			<li>Req. 9.1.2 sets portlet positioning<br>
 *		</ul>
 *		<li>Req. 9.2: Stores the information in session / cookies<br>
 *		<li>Req. 9.3: Stores the information using Data Engine<br>
 *					//needs function call information from Data Engine Class <br>
 *	</ul>  
 *	<li>Req. 10: retrieves profile<br>
 *	<ul>
 *		<li>Req. 10.1: Retrieves user profile information<br>
 * 		<ul>
 *			<li>Req. 10.1.1 gets choice of skin<br>
 *			<li>Req. 10.1.2 gets portlet positioning<br>
 *		</ul>
 *		<li>Req. 10.2: Retrieves the information from session / cookies<br>
 *		<li>Req. 10.3: Retrieves the information using Data Engine<br>
 * 					//needs function call information from Data Engine Class<br>
 *	</ul>  
 *</ul>
 *
 * @author Shuhei Higashi
 * @version 0.1

 */

public class UiProfile {
	
	private long sID = 666;
	private long pTotal;
	private ArrayList pList = null; //pages list
	private ArrayList posList = null; //portlet position list
	private ArrayList oList = null;
	
	UiProfile(){}
	
	private void setPosition (String uId, ArrayList coord, long pageId){ //9.1.2
	//write2Cookie(); //call to external Cookie engine
	//write2Db();//call to external DB engine
	}
			
	private ArrayList getPosition (String uId, long portletId, long pageId){ //10.1.2
		//readCookie(); //call to external Cookie engine
		//readDb();//call to external DB engine
		return posList;
	}
			
	/**
	 * Sets User profile for the GUI.
	 * Requires the following arguments:
	 * uId, coord (ArrayList, which contains x, y, z coordinates for the portlet window), 
	 * pageId.
	 * <p>
	 * Sets User profile for the GUI.
	 * Requires the following arguments:
	 * uId, coord (ArrayList, which contains x, y, z coordinates for the portlet window), 
	 * pageId.
	 * 
	 * @param  uId String String value of user id for the portal
	 * @param  coord ArrayList 
	 * @param  pageId long 
	 */			
	public void setUiProfile(String uId, ArrayList coord, long pageId){ // 9.*
		setPosition (uId, coord, pageId);
	}
		
	/**
	 * Rerurns an ArrayList, which conatins positioning information for each portlets. 
	 * uid and portletIds argument must be specified.
	 *  
	 * <p>
	 * Rerurns an ArrayList, which conatins positoning information for each portlets. 
	 * saved either in cookie in or in a database.  If the portlet positioning 
	 * coordinates have not been set previously, the default values will be
	 * assigned.
	 * 
	 * @param  uId  String value of user id for the portal
	 * @param  portletIds an ArrayList which contains list of portlet IDs
	 * @param  pageId long 
	 * @return ArrayList this ArrayList contains positoning
	 * information for each portlets
	 */	
	public ArrayList getUiProfile(String uId, ArrayList portletIds, long pageId){ // 10.*
	long portletId = 666 ;
	
	//process to iterate through the portletIds ArrayList...
	
	getPosition(uId, portletId, pageId); // iterate through portletIDs 
	return oList;
	}
	
	
/* ==================================================================================== */	
/* ======================= the following features will be ============================= */	
/* ========================  implemented in near future   ============================= */	
/* =============================== sx 07/14/2005 ====================================== */	
/* ==================================================================================== */	
	
/*
	the following skin related methods need to be fleshed out and designed in near future 
	- sx: 07/14/2005
*/	

	/**
	 * Sets skin for the application.
	 * public for future admin access.
	 *
	 * <p>
	 * Sets skin for the application.
	 * Required argument: skinType : long
	 * 
	 * @param skinType long
	 */
	public void setSkin (long skinType){ //9.1.1 // give it public access for future skin editor
	//write2Cookie(); //call to external Cookie engine
	//write2Db();//call to external DB engine
	}
	
	/**
	 * Returns skinId.
	 * Required argument: uId : String.
	 *
	 * <p>
	 * Returns skinId.
	 * Required argument: uId : String.
	 *
	 * @param  uId String
	 * @return long
	 */
	public long getSkin (String uId){ //10.1.1
	//readCookie(); //call to external Cookie engine
	//readDb();//call to external DB engine
	return sID;
	}
	
/*
	the following two methods deal with 'page' setup - the idea is to generate a new page definition 
	everytime a user click 'save' button and once saved the information can be accessed indefinately
	until the user so choose to 'delete' the particular page...
	
	this logic needs to be designed further more.
*/	
	/**
	 * Sets the number of pages. 
	 *
	 * <p>
	 * Sets the number of pages.
	 * Requires uId - user ID: 
	 * Returns the total numbers of the pages.
	 *
	 * @param  uId String
	 */	
	public long setPages(String uId){
		return pTotal;
	}
	
	/**
	 * Retrieves a set of pages.
	 * 
	 * <p>
	 *
	 *
	 * @param  uId String
	 * @return ArrayList
	 */		
	public ArrayList getPages(String uId){
		return pList;
	}
	
	
	
} 
