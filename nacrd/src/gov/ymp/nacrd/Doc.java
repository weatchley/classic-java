package gov.ymp.nacrd;

// Support classes
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import gov.ymp.nacrd.*;
import gov.ymp.util.db.*;
import java.sql.*;


/**
* Docs is the set of document related funcitons for NACRD
*
* @author   Bill Atchley
*/
public class Doc {
    //
    private int docID = 0;
    private String number = null;
    private String filename = null;
    private String archivefilename = "";
    private String duedate = null;
    private String archivedate = "";
    private String type = null;
    private String status = null;
    private String title = null;
    //private ServletContext context = getServletContext();
    //private String docRoot = context.getRealPath("/");
    private String docRoot = "";
    private String docPath = "";
    private boolean isNew = true;
    private static String SCHEMA = "nacrd";

    /**
    * creates a new empty Doc object
    */
    public Doc () {
        //
    }

    /**
    * Creates a Doc object and retrieves it
    *
    * @param dnumb     The document number to lookup
    */
    public Doc (String dnumb) {
        lookup (dnumb);
    }

    /**
    * Creates a Doc object and retrieves it
    *
    * @param myID     The document ID number to lookup
    */
    public Doc (int myID) {
        lookup (myID);
    }

    /**
    * Creates a Doc object and retrieves it
    *
    * @param root      The document root
    * @param dnumb     The document number to lookup
    */
    public Doc (String root, String dnumb) {
        docRoot = root;
        lookup (dnumb);
    }

    /**
    * Retrieves a Doc and stores it in the current object
    *
    * @param dnumb     The Doc to lookup
    */
    public void lookup (String dnumb) {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT id,number,filename,archivefilename,duedate,archivedate,type,status,title " +
                  "FROM " + SCHEMA + ".documents " +
                  "WHERE number=UPPER('" + dnumb + "')");
            while (rset.next()) {
                docID = rset.getInt(1);
                number = rset.getString(2);
                filename = rset.getString(3);
                archivefilename = rset.getString(4);
                duedate = Utils.dateToString(rset.getDate(5));
                archivedate = ((rset.getDate(6) != null) ? Utils.dateToString(rset.getDate(6)) : "");
                type = rset.getString(7);
                status = rset.getString(8);
                title = rset.getString(9);
            }
            isNew = false;
            myConn.release();

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
            //log(outLine);
        }
    }

    /**
    * Retrieves a Doc and stores it in the current object
    *
    * @param dnumb     The Doc to lookup
    */
    public void lookup (int myID) {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT id,number,filename,archivefilename,duedate,archivedate,type,status,title " +
                  "FROM " + SCHEMA + ".documents " +
                  "WHERE id=" + myID);
            while (rset.next()) {
                docID = rset.getInt(1);
                number = rset.getString(2);
                filename = rset.getString(3);
                archivefilename = rset.getString(4);
                duedate = Utils.dateToString(rset.getDate(5));
                archivedate = ((rset.getDate(6) != null) ? Utils.dateToString(rset.getDate(6)) : "");
                type = rset.getString(7);
                status = rset.getString(8);
                title = rset.getString(9);
            }
            isNew = false;
            myConn.release();

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            ALog.logError(outLine);
        }
    }

    /**
    * Retrieve the ID number for the current document
    *
    * @return      The ID number for the current document
    */
    public int getID() {
        return(docID);
    }

    /**
    * Retrieve the number for the current document
    *
    * @return      The number for the current document
    */
    public String getNumber() {
        return(number);
    }

    /**
    * Retrieve the filename for the current document
    *
    * @return      The filename for the current document
    */
    public String getFilename() {
        return(filename);
    }

    /**
    * Retrieve the archive filename for the current document
    *
    * @return      The archive filename for the current document
    */
    public String getArchiveFilename() {
        return(archivefilename);
    }

    /**
    * Retrieve the duedate for the current document
    *
    * @return      The duedate for the current document
    */
    public String getDueDate() {
        return(duedate);
    }

    /**
    * Retrieve the duedate for the current document
    *
    * @return      The duedate for the current document
    */
    public java.util.Date getDueDateAsDate() {
        return((java.util.Date) Utils.toDate(duedate, "MM/dd/yyyy"));
    }

    /**
    * Retrieve the archive date for the current document
    *
    * @return      The archive date for the current document
    */
    public String getArchiveDate() {
        return(archivedate);
    }

    /**
    * Retrieve the title for the current document
    *
    * @return title     The title for the current document
    */
    public String getTitle() {
        return(title);
    }

    /**
    * Retrieve the type for the current document
    *
    * @return type     The type for the current document
    */
    public String getType() {
        return(type);
    }

    /**
    * Retrieve the status for the current document
    *
    * @return status     The status for the current document
    */
    public String getStatus() {
        return(status);
    }

    /**
    * Retrieve the isNew flag
    *
    * @return      The status for the current document
    */
    public boolean getIsNew() {
        return(isNew);
    }


    /**
    * Set the ID number for the current document
    *
    * @param num     The ID number for the current document
    */
    public void setID(int num) {
        docID = num;
    }

    /**
    * Set the ID number for the current document
    *
    * @param num     The ID number for the current document
    */
    public void setID(String num) {
        docID = Integer.parseInt(num);
    }

    /**
    * Set the number for the current document
    *
    * @param num     The number for the current document
    */
    public void setNumber(String num) {
        number = num;
    }

    /**
    * Set the filename for the current document
    *
    * @param fn     The filename for the current document
    */
    public void setFilename(String fn) {
        filename = fn;
    }

    /**
    * Set the archivefilename for the current document
    *
    * @param fn     The archivefilename for the current document
    */
    public void setArchiveFilename(String fn) {
        archivefilename = fn;
    }

    /**
    * Set the duedate for the current document
    *
    * @param dDate     The duedate for the current document
    */
    public void setDueDate(String dDate) {
        duedate = dDate;
    }

    /**
    * Set the archivedate for the current document
    *
    * @param aDate     The archivedate for the current document
    */
    public void setArchiveDate(String aDate) {
        archivedate = aDate;
    }

    /**
    * Set the type for the current document
    *
    * @param dType     The type for the current document
    */
    public void setType(String dType) {
        type = dType;
    }

    /**
    * Set the status for the current document
    *
    * @param dStatus     The status for the current document
    */
    public void setStatus(String dStatus) {
        status = dStatus;
    }

    /**
    * Set the title for the current document
    *
    * @param dTitle     The title for the current document
    */
    public void setTitle(String dTitle) {
        title = dTitle;
    }


    /**
    * Retrieve a set of documents
    *
    * @return      A document hashset
    */
    //public static Doc[] getDocArray(String docRoot) {
    public static HashSet getDocSet(String docRoot) {
        HashSet myDocs = new HashSet();
        String[] dir = getDocList();
        for (int i=0; i<dir.length; i++) {
            File f = new File(docRoot + "data/documents/" + dir[i]);
            if (f.isDirectory()) {
                myDocs.add(new Doc(docRoot, dir[i]));
            }
        }
        return(myDocs);
    }

    /**
    * Retrieve a list of documents
    *
    * @return      A document list
    */
    public static String[] getDocList(String root) {
        return(getDocList());
    }

    /**
    * Retrieve a list of documents
    *
    * @return      A document list
    */
    public static String[] getDocList() {
        String outLine = "";
        String[] dir = null;
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".documents");
            rset.next();
            int aSize = rset.getInt(1);
            rset = stmt.executeQuery ("SELECT id,number,filename,archivefilename,duedate,archivedate,type,status,title " +
                "FROM " + SCHEMA + ".documents " +
                "ORDER BY number");
            dir = new String[aSize];
            int i = 0;
            while (rset.next()) {
                dir[i] = rset.getString(2);
                i++;
            }
            myConn.release();
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }
        return(dir);
    }

    /**
    * Save document info
    *
    */
    public void save(String root) {
        String outLine = "";
        docRoot = root;
        if (isNew) {
            addDoc();
        } else {
            updateDoc();
        }

    }


    /**
    * Add a document to the DB
    *
    */
    public void addDoc() {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            String duedate2 = Utils.dateToString((java.util.Date) Utils.toDate(duedate), "yyyy-MM-dd");
            String archivedate2 = ((archivedate != null && archivedate.compareTo("  ") > 0) ? Utils.dateToString((java.util.Date) Utils.toDate(archivedate), "yyyy-MM-dd") : "");
            pstmt = myConn.conn.prepareStatement(
                "INSERT INTO " + SCHEMA + ".documents (number, filename, archivefilename, duedate, archivedate, type, status, title) " +
                "VALUES ('" + number + "', '" + filename + "', " +
                ((archivefilename != null) ? "'" + archivefilename + "'" : "NULL") + ", '" + duedate2 + "', " +
                ((archivedate != null) ? "'" + archivedate2 + "'" : "NULL") + ", '" + type + "', '" + status + "', ?)"
                );
            pstmt.setString(1,title);
            int rows = pstmt.executeUpdate();
            myConn.release();

            isNew = false;
            //ALog.logActivity("Document " + number + " was added.");
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            //log(outLine);
            ALog.logError(outLine);
        }

    }


    /**
    * Update a document in the DB
    *
    */
    public void updateDoc() {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            String duedate2 = Utils.dateToString(Utils.toDate(duedate), "yyyy-MM-dd");
            String archivedate2 = ((archivedate != null && archivedate.compareTo("  ") > 0) ? Utils.dateToString((java.util.Date) Utils.toDate(archivedate), "yyyy-MM-dd") : "");
            pstmt = myConn.conn.prepareStatement(
                "UPDATE " + SCHEMA + ".documents SET number='" + number + "', filename='" + filename + "', " +
                "archivefilename=" + ((archivefilename != null) ? "'" + archivefilename + "'" : "NULL") + ", " +
                "duedate='" + duedate2 + "', " +
                "archivedate=" + ((archivedate != null) ? "'" + archivedate2 + "'" : "NULL") + ", " +
                "type='" + type + "', status='" + status + "', title=? " +
                "WHERE id=" + docID
                );
            pstmt.setString(1,title);
            int rows = pstmt.executeUpdate();
            myConn.release();

            isNew = false;
            //ALog.logActivity("Document " + number + " was updated.");
        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(outLine);
        }

    }

}