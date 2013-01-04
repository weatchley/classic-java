package gov.ymp.csi.shuzi;

import java.util.*;

/**
* SessManager Class Summary Comment.
 * 
 * 
 * <p>
 * 
 *<ul>
 *<li>Req. 1.1: manages user session using server session variables (tomcat, apache and etc.)<br>
 *<li>Req. 1.2: manages user session using portal specific session<br> 
 *</ul>
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */

public class SessManager{

	SessManager(){}
	
	private Object conObj = null; 
	private Object exObj = null;
	protected String uName;
	protected String password;
	protected String dbLoc;
			
	private Object sessConnector(String uName,String password,String dbLoc) {
		return conObj;
	} //1.1, 1.2
			
	public Object sessExporter(ArrayList credentials) {
		exObj = this.sessConnector(uName,password,dbLoc);
		return exObj;
	} //1.1, 1.2, 1.3

}
