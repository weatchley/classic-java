package gov.ymp.csi.shuzi;

/**
 * Launch Class {abstarct} Summary Comment.
 * Parent Class to LaunchApp, LaunchServ, LaunchURL
 * 
 * <p>
 * 
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */	

abstract class Launch {
		
		long _lType;
		String _pId;
		String _pName;
		String _uId;
		String _uIP;
		
		private boolean cFlag = false;
		private boolean lStatus = false;
		
		/**
		 * Launch Constructor with input parameters. 
		 * 
		 * 
		 * <p>
		 * This method returns boolean on whether or not a user has an access to the 
		 * specific protlet.
		 * long value pId (portlet ID) and String value uId must be specified.
		 *
		 * @param  lType  Long value of user id for the portal
		 * @param  pId  Strings value of portalID
		 * @param  pName  String value of portal Name (URL for LauchURL child class...)
		 * @param  uId  String value of user id for the portal
		 * @param  pIP  Strings value of user IP address
		 */
		public Launch( long lType, String pId, String pName, String uId, String uIP )
		    {
			_lType = lType;
			_pId = pId;
			_pName = pName; //launch URL for LaunchURL portlet
			_uId = uId;
			_uIP = uIP;
		    }
		
		/**
		 * The main method for Launch class. 
		 * 
		 * 
		 * <p>
		 * This method runs permission check (checkPerm) - generates frame (execPFrame) 
		 * if the user/service has a proper permission - updates the log (UpdateLog)
		 * 
		 *
		 */	
		public void doLaunch(){
			lStatus = checkPerm(_uId, _pId);
			
			if (lStatus){
			execPFrame();
			}
				
			//UpdateLog ul = new UpdateLog();	//creates UpdateLog Class Constructor
			UpdateLog.setLogs(_lType, 
					lStatus, 
					_pId, 
					_pName, 
					_uId, 
					_uIP);	
		}
		
		/**
		 * Rerurns boolean on whether or not a user has an access to the specific protlet. 
		 * uId and pId arguments must be specified.
		 * 
		 * <p>
		 * This method returns boolean on whether or not a user has an access to the 
		 * specific protlet.
		 * long value pId (portlet ID) and String value uId must be specified.
		 *
		 * @param  uId  String value of user id for the portal
		 * @param  pId  Strings value of portalID
		 * @return boolean     
		 */	
		protected boolean checkPerm(String uId, String pId){ //checks the privilege on the serviceRegistry
			return cFlag;
		}; 	
		
		/**
		 * Generates Portal Frame (TBA). 
		 * 
		 * 
		 * <p>
		 * This method generates a portal frame (TBA). 
		 *
		 * 
		 * 
		 */
		protected void execPFrame(){
			//generates portal frame	
		}; 	
					
}
