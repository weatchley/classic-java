package gov.ymp.csi.shuzi;

import java.io.*;

/**
 * AppDef Interface that is to be implemented implemented by LaunchApp class.
 * 
 * 
 * <p>
 * 
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */	

public interface AppDef {
	/**
	*  This method executes App in a portlet frame.
	 * 
	 * 
	 * <p>
	 * This method executes App in a portlet frame.
	 * Requires Arguments in array.
	 * 
	 *
	 * @param  values String[][] array of input parameters 
	 ***	[0][0] - pName
	 ***	[0][1] - pId
	 * @return String : content in HTML/javascript
	 */
	public String execApp( String [][] values ) throws IOException; //calls portal object
}