package gov.ymp.csi.shuzi;

import java.io.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class FileStuff {
    
    //public String dirPath = "/tmp/higashis/bettergateway/build/shuzi/logs/";
	
    public FileStuff(){ //contructor
    }
    
    public void fWrite(String strInput, String fName, boolean AppFlag){
	    	//System.out.println ("FileStuff.fWrite called with: "+fName);
    	String logDirPath = null;
    	try{
			Context initCtx = new InitialContext();
			logDirPath = (String) initCtx.lookup("java:comp/env/logDirPath");
				 	//String curDir = System.getProperty("user.dir");
				 	//System.out.println(curDir);
				//System.out.println(logDirPath+fName);
			BufferedWriter out = new BufferedWriter(new FileWriter(logDirPath+fName, AppFlag));
			out.write(strInput);
			out.close();
				//System.out.println (strInput);
    		}catch (Exception e){
                        //System.err.println ("Error writing to file");
			System.out.println ("gov.ymp.csi.shuzi.FileStuff.fWrite: Error writing to file");
            }
    }
    
    public void fRead(String fName){
	    	System.out.println ("gov.ymp.csi.shuzi.FileStuff.fWrite: FileStuff.fRead called!!");	
    }
    
    public void fDelete(String fName){
    	boolean exists = (new File("filename")).exists();
    	boolean dSuccess = (new File("filename")).delete();
        if (exists) {
    		//System.out.println (fName + " exists.");
    		if (!dSuccess) {
            	System.out.println ("...deletion failed: " + fName);
            }else{
            	//System.out.println (fName + " deleted.");
            }
        } else {
        	System.out.println (fName + " does not exist.");
        }	
    }
      	
}