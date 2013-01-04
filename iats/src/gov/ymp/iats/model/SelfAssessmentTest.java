package gov.ymp.iats.model;

// Support classes
import java.io.*;
import java.util.*;
import gov.ymp.csi.db.*;

public class SelfAssessmentTest {
		
	  public static void main(String args[]) {
	
	  String sanumber = null;
	  String title = null;
	  String scope = null;
	  String purpose = null;
	  int orgid = 0;
	  int teamleadid = 0;
	  int year = 0;
	  int sequenceid = 0;
	  java.util.Date scheduleddate = null;    
	  java.util.Date rescheduleddate = null;
	  java.util.Date signeddate = null;
	  String comments = null;
	  java.util.Date cancelleddate = null;
	  String cancelledrationale = null;
	  int supportteamleadid = 0;
	  String hascirs = null;
	  String type = null;
	  int divid = 0;
	  String asstype = null;
	  String assobj = null;
	  String ll = null;
	  String crlevels = null;
	  String crnums = null;
	  String llnums = null;
	  String pdffilename = null;
	  int porgid = 0;
		  
	  String id = args[0];
	  DbConn myConn = new DbConn("csi");
	  SelfAssessment sa = new SelfAssessment(Integer.parseInt(id),myConn);		
		
	  sanumber = sa.getSANumber();
	  System.out.println("sanumber: "+sanumber);
	  
	  teamleadid = sa.getTeamleadid();
	  String teamleadname = null;
	  teamleadname = sa.getTeamleadname(Integer.valueOf(teamleadid), myConn);
	  System.out.println("teamleadname: "+teamleadname);	  
	  title = sa.getTitle();	  
	  System.out.println("title: "+title);
	  purpose = sa.getPurpose();
	  System.out.println("purpose: "+purpose);
	  scheduleddate = sa.getScheduleddate();
	  System.out.println("scheduleddate: "+scheduleddate);
	  rescheduleddate = sa.getRescheduleddate();
	  System.out.println("rescheduleddate: "+rescheduleddate);
	  signeddate = sa.getSigneddate();
	  System.out.println("signeddate: "+signeddate);
	  cancelleddate = sa.getCancelleddate();
	  System.out.println("cancelleddate: "+cancelleddate);
	  cancelledrationale = sa.getCancelledrationale();
	  System.out.println("cancelledrationale: "+cancelledrationale);  
	  comments = sa.getComments();
	  System.out.println("comments: "+comments); 
	  hascirs = sa.getHascirs();
	  System.out.println("hascirs: "+hascirs); 
	  asstype = sa.getAsstype();
	  System.out.println("asstype: "+asstype); 
	  assobj = sa.getAssobj();
	  System.out.println("assobj: "+assobj); 
	  crlevels = sa.getCrlevels();
	  System.out.println("crlevels: "+crlevels); 
	  crnums = sa.getCrnums();
	  System.out.println("crnums: "+crnums); 
	  llnums = sa.getLlnums();
	  System.out.println("llnums: "+llnums); 
	  
	  }
}