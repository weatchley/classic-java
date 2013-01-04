package gov.ymp.csi.shuzi;

import java.io.*;

/**
 * UrlDef Interface that is to be implemented implemented by LaunchURL class.
 * 
 * 
 * <p>
 * 
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */	

public interface UrlDef {
	/**
	 *  This method executes URL in a portlet frame.
	 * 
	 * 
	 * <p>
	 * This method executes URL in a portlet frame.
	 * Requires Arguments in array.
	 * 
	 *
	 * @param  values String[][] array of input parameters 
	 ***	[0][0] - pName
	 ***	[0][1] - pId
	 * @return String : content in HTML/javascript
	 */
	 public String execURL( String [][] values ) throws IOException; //calls portal object
}