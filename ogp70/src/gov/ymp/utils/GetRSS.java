package gov.ymp.utils;

import java.io.*;
import java.net.*;
import java.lang.*;

public class GetRSS {
		  public static void main(String[] args)
		  {
	
		    if ( (args.length < 1) )
		    {
		      System.err.println( "\nUsage: java GetRSS [urlToGet] [pathtofile]" );
		      //System.exit(1);
		    }
	
		    String url = args[0];
		    String pathtofile = args[1];
		    //String pathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
		    //String filePath = pathToRoot + pathtofile;
		    String filePath = pathtofile;
		    
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
				      //System.out.println(s);
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
