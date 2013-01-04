package gov.ymp.csi.shuzi;

import java.io.*;
import java.net.*;

public class HtmlGrab {
	private String vReturn ="";  //placeholder for returned value
		
    public void doGrab(String iURL) {
        String sTemp = "";
	String vURL;    //placeholder for input URL
	Reader r;
        
	vURL = iURL;
        
	try {
            //if (spec.indexOf("://") > 0) {
                URL u = new URL(vURL);
                Object content = u.getContent();
                if (content instanceof InputStream) {
                    r = new InputStreamReader((InputStream)content);
                }
                else if (content instanceof Reader) {
                    r = (Reader)content;
                }
                else {
                    throw new Exception("Bad URL content type.");
                }
            //}
            //else {
            //    r = new FileReader(spec);
            //}

            //HTMLEditorKit.Parser parser;
                BufferedReader in = new BufferedReader(r);
		String str = "";
	    	while (str != null) {
			str = in.readLine();
			//System.out.println(str);
			sTemp += str;
		}

	    r.close();
	    		//System.out.println(sTemp);
	    vReturn = sTemp;
        }
        catch (Exception e) {
            System.err.println("Error: " + e);
            e.printStackTrace(System.err);
        }
    }
    
    public String getvReturn(){
		return  vReturn;
	}
	
}


    


