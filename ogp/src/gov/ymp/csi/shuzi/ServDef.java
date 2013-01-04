package gov.ymp.csi.shuzi;

import java.io.*;

/**
 * ServDef Interface that is to be implemented implemented by LaunchServ class.
 * 
 * 
 * <p>
 * 
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */


public interface ServDef {
	/**
	*  This method executes Service in a portlet frame.
	 * 
	 * 
	 * <p>
	 * This method executes Service in a portlet frame.
	 * Requires Arguments in array.
	 * 
	 *
	 * @param  values String[][] array of input parameters 
	 ***	[0][0] - pName
	 ***	[0][1] - pId
	 * @return String : content in HTML/javascript
	 */
	public String execServ( String [][] values ) throws IOException; //calls portal object
}