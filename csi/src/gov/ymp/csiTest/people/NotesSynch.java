package gov.ymp.csiTest.people;

import java.io.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import javax.naming.*;
import javax.naming.directory.*;

import lotus.domino.Session;
import gov.ymp.csi.db.*;
import gov.ymp.csi.db.ALog.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.systems.*;
import gov.ymp.lotus.core.lotusapi;
import gov.ymp.csi.spring.ldap.*;

/**
* NotesSynch is a helper class for NotesUtils.
* NotesSynch makes calls to methods that are in 
* NotesUtils to perform CSI/Notes Addressbook synchronization.
* 
* 
* @author   Rajesh Patel
*/
public class NotesSynch {
	/**
    * Check to see if a user can log in to the domain
    *
    * 
    * 
    */
	private static NotesUtils NotesUtils;
	private static ArrayList arraylist;
	  
	public static void main(String[] args){
				  
			//create NotesUtils object
			NotesUtils = new NotesUtils();
			
		  try{			
			
			arraylist = NotesUtils.getNotesInfo("YDLNStaging.ymp.gov:63148","rtest","12345678", "YDLNStaging/YD/RWDOE", "raj/names.nsf");
		  
			 }
			 catch(Exception e){
				System.out.println(e);
			 }
	}		
}