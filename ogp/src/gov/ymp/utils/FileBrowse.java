package gov.ymp.utils;

import java.io.*;

public class FileBrowse {
	
	//public FileBrowse() {
		//super();
	//}
		
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		//System.out.println("FileBrowse() called!");
		String dirname = "d:\\workspace\\gDev\\build\\ocrwmgateway";
		File f1 = new File(dirname);
		visitAllDirsAndFiles(f1);
		
	    /*
		if (f1.isDirectory()) {
		//System.out.println("Directory of " + dirname);
	      String s[] = f1.list();
	      for (int i = 0; i < s.length; i++) {
	        File f = new File(dirname + "/" + s[i]);
		        if (f.isDirectory()) {
		          //System.out.println(s[i] + " is a directory");
		          visitAllDirsAndFiles(f);
		        } else {
		          //System.out.println(s[i] + " is a file");
		        }
	      }
	    } else {
	      //System.out.println(dirname + " is not a directory");
	    }
		*/
		
	  }
	
    public static void visitAllDirsAndFiles(File dir) {
    	String ts = "";
    	//System.out.println(dir);
    	//ts = fileRead("fileList.txt");
    	ts += dir.getPath() + "\n\r";
    	//fileOut("fileList.txt",ts);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                visitAllDirsAndFiles(new File(dir, children[i]));
            }
        }else{
        }
    }
		
    public static void fileOut(String fName,String fContent){
    	FileOutputStream out;
    	PrintStream p;
    	
    	try
    	{
    		out = new FileOutputStream(fName);
    		p = new PrintStream(out);
    		p.println(fContent);
    		p.close();
    	}catch(Exception e)
    	{
    		System.err.println("Error writing to file");
    	}
    	
    }
    
    public static String fileRead(String fName){
    	String rs = "";
    	try{
    		FileInputStream fStream = new FileInputStream(fName);
    		DataInputStream in = new DataInputStream(fStream);
    		while(in.available()!=0){
    			//System.out.println(in.readLine());
    			rs += in.readLine();
    		}
    		
    	}catch(Exception e){
    		System.err.println("File input error");
    	}
    	return rs;
    }
    
	/*		
    String dirname = "d:\\workspace\\gDev\\build\\ocrwmgateway";
    File f1 = new File(dirname);
    if (f1.isDirectory()) {
      //System.out.println("Directory of " + dirname);
      String s[] = f1.list();
      for (int i = 0; i < s.length; i++) {
        File f = new File(dirname + "/" + s[i]);
        if (f.isDirectory()) {
          //System.out.println(s[i] + " is a directory");
          
        } else {
          //System.out.println(s[i] + " is a file");
        }
      }
    } else {
      //System.out.println(dirname + " is not a directory");
    }
    */
    
}