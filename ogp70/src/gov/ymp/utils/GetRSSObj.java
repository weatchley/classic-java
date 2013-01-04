package gov.ymp.utils;

import java.io.*;
import java.net.*;
import java.lang.*;

public class GetRSSObj {
		  
	public GetRSSObj() {}
	
	public void save(String sUrl, String sPath) {
		String url = sUrl;
	    String filePath = sPath;
	    
	    URL u;
	    InputStream is = null;
	    DataInputStream dis;
	    String s;
	    
	    try
	    {
	      u = new URL(url);
	      is = u.openStream();
	      dis = new DataInputStream(new BufferedInputStream(is));
	      
	      System.out.println("RSS saved as: "+filePath);
	      
	      BufferedWriter o = new BufferedWriter(new FileWriter(filePath));
	      	      
	      while ((s = dis.readLine()) != null)
	      {
	    	  o.write(s);
	      }
	      
	      o.close();
	        
	    }
	    catch (MalformedURLException mue)
	    {
	      System.err.println("Ouch - a MalformedURLException happened.");
	      mue.printStackTrace();
	      //System.exit(2);
	    }
	    catch (IOException ioe)
	    {
	    	//System.err.println("Oops- an IOException happened.");
		    System.err.println("Oops - the following RSS server is inaccessible. Try again later: \n"
		    	+ url);	 
		    //ioe.printStackTrace();
		    //System.exit(3);
	    }
	    finally
	    {
		      try
		      {
		    	  is.close();
			      is = null;
			      u = null;
			      System.out.println("Http connection explicitly closed. Connection-related objects nullified.");
		      }
		      catch (IOException ioe)
		      {
		      }
	    }

	    
	}
	
}
