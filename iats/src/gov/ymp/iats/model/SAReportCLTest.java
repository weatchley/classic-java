package gov.ymp.iats.model;

// Support classes
import java.io.*;
import java.util.*;
import gov.ymp.csi.db.*;

public class SAReportCLTest {
		
	  public static void main(String args[]) {
		  String orgid = args[0];
		  String year = args[1];
		  DbConn myConn = new DbConn("csi");
		  try{
		  SAReportCL sarcl = new SAReportCL(Integer.valueOf(orgid),Integer.valueOf(year), myConn);		  
		  }catch(Exception ex){}
	  }
	  
}