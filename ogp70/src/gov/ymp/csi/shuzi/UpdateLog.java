package gov.ymp.csi.shuzi;

/**
 * UpdateLog Class Summary Comment.
 * 
 * 
 *<p>
 * 
 *<ul>
 *	<li>Req. 8.1: captures the detailed information of use activities such as:<br>
 *	<ul>
 *		<li>Req. 8.1.1: user IP addresses<br>
 *		<li>Req. 8.1.2: URL of the sites that the user accessed<br>
 *		<li>Req. 8.1.3: The List of applications and services that the user employed<br>
 *	</ul>  
 *</ul> 
 *
 * @author Shuhei Higashi
 * @version 0.1

 */

public class UpdateLog {
	
	/**
	 * Rerurns boolean on whether or not a user has an access to the specific protlet. 
	 * uId and pId argument must be specified.
	 * 
	 * <p>
	 * This method returns boolean on whether or not a user has an access to the 
	 * specific protlet.
	 * long value pId (portlet ID) and String value uId must be specified.
	 *
	 * @param  lType long launch type
	 * @param  lStatus boolean launch status
	 * @param  pId String Portal ID
	 * @param  pName String Portal Name  // or URL for launch URL component
	 * @param  uId String User ID   
	 * @param  uIP String User IP address 
	 */	
		
	private static void write2Logs(long lType, boolean lStatus, String pId, String pName, String uId, String uIP){
	
	//output to an eternal log file
	
	}; // 8.1.1, 8.1.2, 8.1.3
	
	public static void setLogs(long lType, boolean lStatus, String pId, String pName, String uId, String uIP){
	write2Logs(lType,lStatus,pId,pName,uId,uIP);	
	}; // 8.1.1, 8.1.2, 8.1.3
	
}
