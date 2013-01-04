package gov.ymp.nacrd;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.*;
import java.util.*;
import java.lang.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import gov.ymp.nacrd.*;
import gov.ymp.util.*;
import gov.ymp.util.db.*;
import java.sql.*;


/**
* CmtRsp is the set of CmtRsp related functions for NACRD
*
* @author   Bill Atchley
*/
public class CmtRsp {
    //
    private int id = 0;
    private String number = null;
    private int user = 0;
    private String org = null;
    private int document = 0;
    private int refersto = 0;
    private java.util.Date dateSubmitted = null;
    private int responder = 0;
    private String comment = null;
    private String response = null;
    public CmtRsp [] relatedComments = null;
    //private ServletContext context = getServletContext();
    //private String docRoot = context.getRealPath("/");
    private static String SCHEMA = "nacrd";
    private boolean isNew = true;

    /**
    * creates a new empty CmtRsp object
    */
    public CmtRsp () {
        //
    }

    /**
    * Creates a CmtRsp object and retrieves it
    *
    * @param cID     The CmtRsp id to lookup
    */
    public CmtRsp (int cID) {
        lookup (cID);
    }

    /**
    * Retrieves an Org and stores it in the current object
    *
    * @param uname     The username to lookup
    */
    public void lookup (int cID) {
        String outLine = "";
        try {
//
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            ResultSet rset = stmt.executeQuery ("SELECT  id,number,user,org,document,refersto,datesubmitted,responder,comment,response " +
                  "FROM " + SCHEMA + ".comments  WHERE id=" + cID);
            while (rset.next()) {
                id = rset.getInt(1);
                number = rset.getString(2);
                user = rset.getInt(3);
                org = rset.getString(4);
                document = rset.getInt(5);
                refersto = rset.getInt(6);
                dateSubmitted = rset.getDate(7);
                responder = rset.getInt(8);
                comment = rset.getString(9);
                response = rset.getString(10);
            }
            isNew = false;
            myConn.release();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(user, "CmtRsp: " + e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(user, "CmtRsp: " + outLine);
        }
    }

    /**
    * add a CmtRsp and store it
    *
    */
    public void add () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            dateSubmitted = new java.util.Date();
            number = Utils.genNewID(Integer.toString(user));
            String sDate = Utils.dateToString(dateSubmitted, "yyyy-MM-dd");
            String sqlcode = "INSERT INTO comments (user,number,org,document,refersto,datesubmitted,responder,comment,response) " +
                "VALUES (" + user + ", '" + number + "', '" + org + "', " + document + ", " + ((refersto > 0) ? "" + refersto : "NULL") + ", " +
                "'" + sDate + "', " + ((responder > 0) ? "" + responder : "NULL") + ", " +
                "?, " + ((response != null  && response.compareTo("  ") > 0) ? "?" : "NULL") + ")";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            pstmt.setString(1,comment);
            int respCol = 1;
            if (comment != null  && comment.compareTo("  ") > 0) {
                pstmt.setString(1,comment);
                respCol = 2;
            }
            if (response != null  && response.compareTo("  ") > 0) {
                pstmt.setString(respCol,response);
            }
            if ((comment != null  && comment.compareTo("  ") > 0) || (response != null  && response.compareTo("  ") > 0)) {
                int rows = pstmt.executeUpdate();
                isNew = false;
                //ALog.logActivity(user, "Comment " + number + " was added for document # " + document + ".");
            }
            myConn.release();

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(user, "CmtRsp: " + e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(user, "CmtRsp: " + outLine);
        }
    }

    /**
    * Update a CmtRsp and store it
    *
    */
    public void update () {
        String outLine = "";
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            PreparedStatement pstmt = null;
            String sqlcode ="UPDATE " + SCHEMA + ".comments SET responder=" + responder + ", " +
                "comment=" + ((comment != null  && comment.compareTo("  ") > 0) ? "?" : "NULL") + ", " +
                "response=" + ((response != null  && response.compareTo("  ") > 0) ? "?" : "NULL") + " " +
                "WHERE id=" + id +
                "";
//System.out.println(sqlcode);
            pstmt = myConn.conn.prepareStatement(sqlcode);
            int respCol = 1;
            if (comment != null  && comment.compareTo("  ") > 0) {
                pstmt.setString(1,comment);
                respCol = 2;
            }
            if (response != null  && response.compareTo("  ") > 0) {
                pstmt.setString(respCol,response);
            }
            int rows = pstmt.executeUpdate();
            isNew = false;
            myConn.release();
            //ALog.logActivity(user, "Comment " + number + " was updated for document # " + document + ".");

        }
        catch (java.sql.SQLException e) {
            System.out.println(e + e.getMessage());
            ALog.logError(user, "CmtRsp: " + e + e.getMessage());
        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError(user, "CmtRsp: " + outLine);
        }
    }

    /**
    * Retrieve the id for the current CmtRsp
    *
    * @return      The id for the current CmtRsp
    */
    public int getID() {
        return(id);
    }

    /**
    * Retrieve the number for the current CmtRsp
    *
    * @return      The number for the current CmtRsp
    */
    public String getNumber() {
        return(number);
    }

    /**
    * Retrieve the name for the current CmtRsp
    *
    * @return      The name for the current CmtRsp
    */
    public int getUser() {
        return(user);
    }

    /**
    * Retrieve the org for the current CmtRsp
    *
    * @return      The org for the current CmtRsp
    */
    public String getOrg() {
        return(org);
    }

    /**
    * Retrieve the document for the current CmtRsp
    *
    * @return      The document for the current CmtRsp
    */
    public int getDocument() {
        return(document);
    }

    /**
    * Retrieve the refersto for the current CmtRsp
    *
    * @return      The refersto for the current CmtRsp
    */
    public int getRefersTo() {
        return(refersto);
    }

    /**
    * Retrieve the date submitted for the current CmtRsp
    *
    * @return      The date submitted for the current CmtRsp
    */
    public java.util.Date getDateSubmitted() {
        return(dateSubmitted);
    }

    /**
    * Retrieve the responder for the current CmtRsp
    *
    * @return      The responder for the current CmtRsp
    */
    public int getResponder() {
        return(responder);
    }

    /**
    * Retrieve the comment text for the current CmtRsp
    *
    * @return      The comment text for the current CmtRsp
    */
    public String getComment() {
        return(comment);
    }

    /**
    * Retrieve the response text for the current CmtRsp
    *
    * @return      The response text for the current CmtRsp
    */
    public String getResponse() {
        return(response);
    }


    /**
    * Retrieve the isNew flag
    *
    * @return      The status for the current CmtRsp
    */
    public boolean getIsNew() {
        return(isNew);
    }

    /**
    * Set the ID for the current CmtRsp
    *
    * @param myID     The id for the current CmtRsp
    */
    public void setID(int myID) {
        id = myID;
    }

    /**
    * Set the number for the current CmtRsp
    *
    * @param myNumber     The number for the current CmtRsp
    */
    public void setNumber(String myNumber) {
        number = myNumber;
    }

    /**
    * Set the user for the current CmtRsp
    *
    * @param un     The user for the current CmtRsp
    */
    public void setUser(int un) {
        user = un;
    }

    /**
    * Set the fullname for the current CmtRsp
    *
    * @param or     The org for the current CmtRsp
    */
    public void setOrg(String or) {
        org = or;
    }

    /**
    * Set the document for the current CmtRsp
    *
    * @param doc     The document for the current CmtRsp
    */
    public void setDocument(int doc) {
        document = doc;
    }

    /**
    * Set the refersto for the current CmtRsp
    *
    * @param ref     The refersto for the current CmtRsp
    */
    public void setRefersTo(int ref) {
        refersto = ref;
    }

    /**
    * Set the date submitted for the current CmtRsp
    *
    * @param inDate     The date submitted for the current CmtRsp
    */
    public void setDateSubmitted(java.util.Date inDate) {
        dateSubmitted = inDate;
    }

    /**
    * Set the responder for the current CmtRsp
    *
    * @param res     The responder for the current CmtRsp
    */
    public void setResponder(int res) {
        responder = res;
    }

    /**
    * Set the comment text for the current CmtRsp
    *
    * @param com     The comment for the current CmtRsp
    */
    public void setComment(String com) {
        comment = com;
    }

    /**
    * Set the response text for the current CmtRsp
    *
    * @param res     The response for the current CmtRsp
    */
    public void setResponse(String res) {
        response = res;
    }


    /**
    * Retrieves a CmtRsp with subcomments for a given document and comment
    *
    * @param  docID  Document ID to retrieve comments for
    * @param  comID  Comment ID to retrieve subcomments for
    * @return      A CmtRsp with subcomments
    */
    public static CmtRsp commentTree (int docID, int comID) {
        CmtRsp [] myCmtA = commentList(docID, comID, true);
        //CmtRsp myCmt = myCmtA[0];
        //return (myCmt);
        return (myCmtA[0]);
    }

    /**
    * Retrieves a List of CmtRsps as an array of CmtRsp for a given document
    *
    * @param  docID  Document ID to retrieve comments for
    * @return        A CmtRsp list
    */
    public static CmtRsp[] commentList (int docID) {
        return (commentList(docID, 0, false));
    }

    /**
    * Retrieves a List of CmtRsps as an array of CmtRsp for a given document
    *
    * @param  docID  Document ID to retrieve comments for
    * @param  tree   Flag to determine if subcomments are to be retrieved
    * @return        A CmtRsp list
    */
    public static CmtRsp[] commentList (int docID, boolean tree) {
        return (commentList(docID, 0, tree));
    }

    /**
    * Retrieves a List of CmtRsps as an array of CmtRsp for a given document
    *
    * @param  docID  Document ID to retrieve comments for
    * @param  comID  Comment ID to retrieve subcomments for
    * @param  tree   Flag to determine if subcomments are to be retrieved
    * @return      A CmtRsp list
    */
    public static CmtRsp[] commentList (int docID, int comID, boolean asTree) {
        String outLine = "";
        CmtRsp[] myList = null;
        try {
            DbConn myConn = new DbConn();
            Statement stmt = myConn.conn.createStatement();
            String whereClause = "WHERE document=" + docID + ((asTree) ? " AND refersto IS NULL" : "");
            whereClause = whereClause + ((comID > 0) ? " AND id=" + comID : "");
            ResultSet rset = stmt.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".comments " + whereClause);
            rset.next();
            int aSize = rset.getInt(1);
            rset.close();
            rset = stmt.executeQuery ("SELECT id,number,user,org,document,refersto,datesubmitted,responder,comment,response " +
                "FROM " + SCHEMA + ".comments " + whereClause + " ORDER BY id");
            myList = new CmtRsp[aSize];
            int i = 0;
            while (rset.next()) {
                myList[i] = new CmtRsp();
                myList[i].setID(rset.getInt(1));
                myList[i].setNumber(rset.getString(2));
                myList[i].setUser(rset.getInt(3));
                myList[i].setOrg(rset.getString(4));
                myList[i].setDocument(rset.getInt(5));
                myList[i].setRefersTo(rset.getInt(6));
                myList[i].setDateSubmitted(rset.getDate(7));
                myList[i].setResponder(rset.getInt(8));
                myList[i].setComment(rset.getString(9));
                myList[i].setResponse(rset.getString(10));
                if (asTree) {
                    Statement stmt2 = myConn.conn.createStatement();
                    ResultSet rset2 = stmt2.executeQuery ("SELECT count(*) FROM " + SCHEMA + ".comments WHERE refersto=" + myList[i].getID());
                    while (rset2.next()) {
                        aSize = rset2.getInt(1);
                        myList[i].relatedComments = new CmtRsp[aSize];
                    }
                    rset2.close();
                    rset2 = stmt2.executeQuery ("SELECT id,number,user,org,document,refersto,datesubmitted,responder,comment,response " +
                        "FROM " + SCHEMA + ".comments WHERE refersto=" + myList[i].getID() + " ORDER BY id");
                    int j = 0;
                    while (rset2.next()) {
                        myList[i].relatedComments[j] = new CmtRsp();
                        myList[i].relatedComments[j].setID(rset2.getInt(1));
                        myList[i].relatedComments[j].setNumber(rset2.getString(2));
                        myList[i].relatedComments[j].setUser(rset2.getInt(3));
                        myList[i].relatedComments[j].setOrg(rset2.getString(4));
                        myList[i].relatedComments[j].setDocument(rset2.getInt(5));
                        myList[i].relatedComments[j].setRefersTo(rset2.getInt(6));
                        myList[i].relatedComments[j].setDateSubmitted(rset2.getDate(7));
                        myList[i].relatedComments[j].setResponder(rset2.getInt(8));
                        myList[i].relatedComments[j].setComment(rset2.getString(9));
                        myList[i].relatedComments[j].setResponse(rset2.getString(10));
                        j++;
                    }
                    rset2.close();
                    rset2 = null;
                }
                i++;
            }
            myConn.release();

        }
        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            System.out.println(outLine);
            ALog.logError("CmtRsp: " + outLine);
        }
        return (myList);
    }





}