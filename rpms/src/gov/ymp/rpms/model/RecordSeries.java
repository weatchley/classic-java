package gov.ymp.rpms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* RecordSeries is the class for RecordSeries's in the db
*
* @author   Bill Atchley
*/
public class RecordSeries {
    private int ID = 0;
    private String description = null;
    private String commonFlag = null;
    private int location = 0;
    private String locationDescription = null;
    private String ocrwmcode = null;
    private int citation = 0;
    private String citationDescription = null;
    private int cutoff = 0;
    private String cutoffDescription = null;
    private int retentionPeriod = 0;
    private String retentionPeriodDescription = null;
    private int retentionGroupFlag = 0;
    private String retentionGroupFlagCode = null;
    private String retentionGroupFlagDescription = null;
    private String retentionCode = null;
    private String retentionCodeDescription = null;
    private java.sql.Date  dateAdded = null;
    private java.sql.Date  dateDepricated = null;
    private java.sql.Date  dateChanged = null;
    private String status = "new";
    private Crosswalk [] cw = null;
    public boolean isNew = true;

    //public static String SCHEMA = "csi";
    public static String SCHEMA = null;
    public static String SCHEMAPATH = null;
    public static String DBTYPE = null;

    /**
    * init new object
    */
    private void init () {
        DbConn tempDB = new DbConn("dummy");
        //SCHEMA = tempDB.getSchemaName();
        //SCHEMAPATH = tempDB.getSchemaPath();
        SCHEMA = "rpms";
        SCHEMAPATH = "rpms";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty RecordSeries object
    */
    public RecordSeries () {
        ID = 0;
        init();
    }


    /**
    * Creates a RecordSeries object and uses the given id to populate it from the db
    *
    * @param id     The id of the RecordSeries to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public RecordSeries (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a RecordSeries from the db and stores it in the current RecordSeries object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a RecordSeries from the db and stores it in the current RecordSeries object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        description = null;
        commonFlag = null;
        location = 0;
        locationDescription = null;
        ocrwmcode = null;
        citation = 0;
        citationDescription = null;
        cutoff = 0;
        cutoffDescription = null;
        retentionPeriod = 0;
        retentionPeriodDescription = null;
        retentionGroupFlag = 0;
        retentionCode = null;
        dateAdded = null;
        dateDepricated = null;
        dateChanged = null;
        status = "new";
        cw = null;

        String outLine = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        Statement stmt = null;
        Statement stmt2 = null;
        try {

            Connection conn = myConn.conn;

            //HashMap retentionGroupFlags = getRetentionGroupFlagsHM(myConn);
            HashMap retentionGroupFlags = RetentionGroupFlag.getItemHM(myConn);

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, description, commonflag, location, ocrwmcode, citation, cutoff, retentionperiod, " +
                  "retentiongroupflag, retentioncode, dateadded, datedepricated, datechanged, status FROM " + SCHEMAPATH + ".record_series WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            String myDescription = rs.getString(2);
            String myCommonFlag = rs.getString(3);
            int myLocation = rs.getInt(4);
            String myOCRWMCode = rs.getString(5);
            int myCitation = rs.getInt(6);
            int myCutoff = rs.getInt(7);
            int myRetentionPeriod = rs.getInt(8);
            int myRetentionGroupFlag = rs.getInt(9);
            String myRetentionCode = rs.getString(10);
            java.sql.Date myDateAdded = rs.getDate(11);
            java.sql.Date myDateDepricated = rs.getDate(12);
            java.sql.Date myDateChanged = rs.getDate(13);
            String myStatus = rs.getString(14);
            if (myID == id) {
                ID = myID;
                description = myDescription;
                commonFlag = myCommonFlag;
                location = myLocation;
                if (location != 0) {
                    FOrganization tmp = new FOrganization(location, myConn);
                    locationDescription = tmp.getDescription();
                } else {
                    locationDescription = "";
                }
                ocrwmcode = myOCRWMCode;
                citation = myCitation;
                if (citation != 0) {
                    CCategory tmp = new CCategory(citation, myConn);
                    citationDescription = tmp.getDescription();
                } else {
                    citationDescription = "";
                }
                cutoff = myCutoff;
                if (cutoff != 0) {
                    CutoffList tmp = new CutoffList(cutoff, myConn);
                    cutoffDescription = tmp.getDescription();
                } else {
                    cutoffDescription = "";
                }
                retentionPeriod = myRetentionPeriod;
                if (retentionPeriod != 0) {
                    RetentionPeriod tmp = new RetentionPeriod(retentionPeriod, myConn);
                    retentionPeriodDescription = tmp.getDescription();
                } else {
                    retentionPeriodDescription = "";
                }
                retentionCode = myRetentionCode;
                if (retentionCode != null) {
                    RetentionCode rc = new RetentionCode(retentionCode, myConn);
                    retentionCodeDescription = rc.getDescription();
                } else {
                    retentionCodeDescription = "";
                }
                retentionGroupFlag = myRetentionGroupFlag;
                dateAdded = myDateAdded;
                dateDepricated = myDateDepricated;
                dateChanged = myDateChanged;
                status = myStatus;

                retentionGroupFlagCode = ((RetentionGroupFlag) retentionGroupFlags.get(retentionGroupFlag)).getCode();
                retentionGroupFlagDescription = ((RetentionGroupFlag) retentionGroupFlags.get(retentionGroupFlag)).getDescription();

                cw = Crosswalk.getItemList(myConn, ID);
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - RecordSeries lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            //if (rs2 != null)
            //    try { rs2.close(); } catch (Exception i) {}
            //if (stmt2 != null)
            //    try { stmt2.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieve the id for the current RecordSeries
    *
    * @return     The id for the current RecordSeries
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the description for the current RecordSeries
    *
    * @return     The description for the current RecordSeries
    */
    public String getDescription() {
        return(description);
    }


    /**
    * Retrieve the commonFlag for the current RecordSeries
    *
    * @return     The commonFlag for the current RecordSeries
    */
    public String getCommonFlag() {
        return(commonFlag);
    }


    /**
    * Retrieve the location for the current RecordSeries
    *
    * @return     The location for the current RecordSeries
    */
    public int getLocation() {
        return(location);
    }


    /**
    * Retrieve the locationDescription for the current RecordSeries
    *
    * @return     The locationDescription for the current RecordSeries
    */
    public String getLocationDescription() {
        return(locationDescription);
    }


    /**
    * Retrieve the ocrwmcode for the current RecordSeries
    *
    * @return     The ocrwmcode for the current RecordSeries
    */
    public String getOCRWMCode() {
        return(ocrwmcode);
    }


    /**
    * Retrieve the citation for the current RecordSeries
    *
    * @return     The citation for the current RecordSeries
    */
    public int getCitation() {
        return(citation);
    }


    /**
    * Retrieve the citationDescription for the current RecordSeries
    *
    * @return     The citationDescription for the current RecordSeries
    */
    public String getCitationDescription() {
        return(citationDescription);
    }


    /**
    * Retrieve the cutoff for the current RecordSeries
    *
    * @return     The cutoff for the current RecordSeries
    */
    public int getCutoff() {
        return(cutoff);
    }


    /**
    * Retrieve the cutoffDescription for the current RecordSeries
    *
    * @return     The cutoffDescription for the current RecordSeries
    */
    public String getCutoffDescription() {
        return(cutoffDescription);
    }


    /**
    * Retrieve the retentionPeriod for the current RecordSeries
    *
    * @return     The retentionPeriod for the current RecordSeries
    */
    public int getRetentionPeriod() {
        return(retentionPeriod);
    }


    /**
    * Retrieve the retentionPeriodDescription for the current RecordSeries
    *
    * @return     The retentionPeriodDescription for the current RecordSeries
    */
    public String getRetentionPeriodDescription() {
        return(retentionPeriodDescription);
    }


    /**
    * Retrieve the retentionGroupFlag for the current RecordSeries
    *
    * @return     The retentionGroupFlag for the current RecordSeries
    */
    public int getRetentionGroupFlag() {
        return(retentionGroupFlag);
    }


    /**
    * Retrieve the retentionGroupFlag Description for the current RecordSeries
    *
    * @return     The retentionGroupFlag Description for the current RecordSeries
    */
    public String getRetentionGroupFlagDescription() {
        return(retentionGroupFlagDescription);
    }


    /**
    * Retrieve the retentionGroupFlag Code for the current RecordSeries
    *
    * @return     The retentionGroupFlag Code for the current RecordSeries
    */
    public String getRetentionGroupFlagCode() {
        return(retentionGroupFlagCode);
    }


    /**
    * Retrieve the retentionCode for the current RecordSeries
    *
    * @return     The retentionCode for the current RecordSeries
    */
    public String getRetentionCode() {
        return(retentionCode);
    }


    /**
    * Retrieve the retentionCodeDescription for the current RecordSeries
    *
    * @return     The retentionCodeDescription for the current RecordSeries
    */
    public String getRetentionCodeDescription() {
        return(retentionCodeDescription);
    }


    /**
    * Retrieve the date added for the current RecordSeries
    *
    * @return     The date added for the current RecordSeries
    */
    public java.sql.Date getDateAddedSQL() {
        return(dateAdded);
    }


    /**
    * Retrieve the date added for the current RecordSeries
    *
    * @return     The date added for the current RecordSeries
    */
    public java.util.Date getDateAdded() {
        java.util.Date tmpDate = ((dateAdded!=null) ? new java.util.Date(dateAdded.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date depricated for the current RecordSeries
    *
    * @return     The date depricated for the current RecordSeries
    */
    public java.sql.Date getDateDepricatedSQL() {
        return(dateDepricated);
    }


    /**
    * Retrieve the date depricated for the current RecordSeries
    *
    * @return     The date depricated for the current RecordSeries
    */
    public java.util.Date getDateDepricated() {
        java.util.Date tmpDate = ((dateDepricated!=null) ? new java.util.Date(dateDepricated.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date changed for the current RecordSeries
    *
    * @return     The date changed for the current RecordSeries
    */
    public java.sql.Date getDateChangedSQL() {
        return(dateChanged);
    }


    /**
    * Retrieve the date changed for the current RecordSeries
    *
    * @return     The date changed for the current RecordSeries
    */
    public java.util.Date getDateChanged() {
        java.util.Date tmpDate = ((dateChanged!=null) ? new java.util.Date(dateChanged.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the status for the current RecordSeries
    *
    * @return     The status for the current RecordSeries
    */
    public String getStatus() {
        return(status);
    }


    /**
    * Retrieve the crosswalk set for the current RecordSeries
    *
    * @return     The crosswalk set for the current RecordSeries
    */
    public Crosswalk [] getCrosswalkSet() {
        return(cw);
    }


    /**
    * Set the description id for the current RecordSeries
    *
    * @param text     The new description of the RecordSeries (String)
    */
    public void setDescription(String text) {
        description = text;
    }


    /**
    * Set the common flag for the current RecordSeries
    *
    * @param flag     The new common flag of the RecordSeries (String)
    */
    public void setCommonFlag(String flag) {
        commonFlag = (flag != null) ? ((flag.equals("T")) ? "T" : "F") : "F";
    }


    /**
    * Set the location for the current RecordSeries
    *
    * @param id     The new location of the RecordSeries (int)
    */
    public void setLocation(int id) {
        locationDescription = null;
        location = id;
    }


    /**
    * Set the ocrwmcode id for the current RecordSeries
    *
    * @param text     The new ocrwmcode of the RecordSeries (String)
    */
    public void setOCRWMCode(String text) {
        ocrwmcode = text;
    }


    /**
    * Set the citation for the current RecordSeries
    *
    * @param id     The new citation id of the RecordSeries (int)
    */
    public void setCitation(int id) {
        citationDescription = null;
        citation = id;
    }


    /**
    * Set the cutoff for the current RecordSeries
    *
    * @param id     The new cutoff id of the RecordSeries (int)
    */
    public void setCutoff(int id) {
        cutoffDescription = null;
        cutoff = id;
    }


    /**
    * Set the retentionPeriod for the current RecordSeries
    *
    * @param id     The new retentionPeriod id of the RecordSeries (int)
    */
    public void setRetentionPeriod(int id) {
        retentionPeriodDescription = null;
        retentionPeriod = id;
    }


    /**
    * Set the retentionGroupFlag id for the current RecordSeries
    *
    * @param flag     The new retentionGroupFlag of the RecordSeries (String)
    */
    public void setRetentionGroupFlag(int flag) {
        retentionGroupFlag = flag;
        //retentionGroupFlagDescription = (String) retentionGroupFlag;
    }


    /**
    * Set the retentionCode id for the current RecordSeries
    *
    * @param text     The new retentionCode of the RecordSeries (String)
    */
    public void setRetentionCode(String text) {
        retentionCode = text;
    }


    /**
    * Save the current RecordSeries to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current RecordSeries to the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
    public void save(DbConn myConn, long userID) {
        //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            if (isNew) {
                sqlcode = "SELECT " + SCHEMAPATH + ".RECORD_SERIES_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;
                //UNID myUID = new UNID();
                //myUID.create("recordseries", myConn);
                //myUID.save(conn);
                //ID = myUID.getID();

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".record_series (id, description, commonflag, location, ocrwmcode, citation, cutoff, retentionperiod, " +
                    "retentiongroupflag, retentioncode, dateadded, datedepricated, datechanged, status) " +
                    "VALUES (" + ID + ", " +
                    ((description == null || description == "n/a") ? "NULL" : "?") + ", " +
                    "'" + commonFlag + "', " + location + ", " +
                    ((ocrwmcode == null || ocrwmcode == "n/a") ? "NULL" : "?") + ", " +
                    citation + ", " + cutoff + ", " + retentionPeriod + ", " + retentionGroupFlag + ", " +
                    ((retentionCode == null || retentionCode == "n/a") ? "NULL" : "?") + ", " +
                    " SYSDATE, NULL, SYSDATE, NULL" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (description != null && description != "n/a") {
                    pstmt.setString(++sqlID, description);
                }
                if (ocrwmcode != null && ocrwmcode != "n/a") {
                    pstmt.setString(++sqlID, ocrwmcode);
                }
                if (retentionCode != null && retentionCode != "n/a") {
                    pstmt.setString(++sqlID, retentionCode);
                }
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".record_series SET " +
                    "description = " + ((description == null || description == "n/a") ? "NULL" : "?") + ", " +
                    "commonflag = '" + commonFlag + "', " +
                    "location = " + location + ", " +
                    "ocrwmcode = " + ((ocrwmcode == null || ocrwmcode == "n/a") ? "NULL" : "?") + ", " +
                    "citation = " + citation + ", " +
                    "cutoff = " + cutoff + ", " +
                    "retentionperiod = " + retentionPeriod + ", " +
                    "retentiongroupflag = " + retentionGroupFlag + ", " +
                    "retentioncode = " + ((retentionCode == null || retentionCode == "n/a") ? "NULL" : "?") + ", " +
                    "datedepricated = " + ((dateDepricated == null) ? "NULL" : "?") + ", " +
                    "dateChanged = SYSDATE, " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "?") + " " +
                    "WHERE id = " + ID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (description != null && description != "n/a") {
                    pstmt.setString(++sqlID, description);
                }
                if (ocrwmcode != null && ocrwmcode != "n/a") {
                    pstmt.setString(++sqlID, ocrwmcode);
                }
                if (retentionCode != null && retentionCode != "n/a") {
                    pstmt.setString(++sqlID, retentionCode);
                }
                if (dateDepricated != null) {
                    pstmt.setDate(++sqlID, dateDepricated);
                }
                rows = pstmt.executeUpdate();
            }
            for (int i=0; i<cw.length; i++) {
                cw[i].save(myConn, userID);
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RecordSeries save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RecordSeries save");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
// do auth stuff
    }


    /**
    * Remove the current RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the user performing the action (long)
    */
    public void drop(DbConn myConn, long userID) {
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
//            stmt = conn.createStatement ();
// do auth stuff
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".record_series WHERE id = " + ID
                );
            rows = pstmt.executeUpdate();


            ID = 0;
            description = null;
            commonFlag = null;
            location = 0;
            locationDescription = null;
            ocrwmcode = null;
            citation = 0;
            citationDescription = null;
            cutoff = 0;
            cutoffDescription = null;
            retentionPeriod = 0;
            retentionPeriodDescription = null;
            retentionGroupFlag = 0;
            retentionGroupFlagDescription = null;
            retentionCode = "F";
            dateAdded = null;
            dateDepricated = null;
            dateChanged = null;
            status = "new";
            cw = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RecordSeries drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - RecordSeries drop");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    */
    public static RecordSeries[] getItemList() {
        DbConn myConn = new DbConn();
        RecordSeries[] cList = getItemList(myConn, (int[]) null, "description", "all", 0, true, "none");
        myConn.release();
        return cList;
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    */
    public static RecordSeries[] getItemList(DbConn myConn) {
        return getItemList(myConn, (int[]) null, "description", "all", 0, true, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param loc        An int defining what location/functional organization to look up
    */
    public static RecordSeries[] getItemList(DbConn myConn, int loc) {
        return getItemList(myConn, (int[]) null, "description", ((loc > 0) ? "subset" : "all"), loc, true, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param loc        An int defining what location/functional organization to look up
    * @param common     A boolean defining if common is included in location lookups
    */
    public static RecordSeries[] getItemList(DbConn myConn, int loc, boolean common) {
        return getItemList(myConn, (int[]) null, "description", ((loc > 0) ? "subset" : "all"), loc, common, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param loc        An int defining what location/functional organization to look up
    * @param common     A boolean defining if common is included in location lookups
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static RecordSeries[] getItemList(DbConn myConn, int loc, boolean common, String sort) {
        return getItemList(myConn, (int[]) null, sort, ((loc > 0) ? "subset" : "all"), loc, common, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param loc        An int defining what location/functional organization to look up
    * @param common     A boolean defining if common is included in location lookups
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param termSelection  A string defining the term selection (Long, Short, or none)
    */
    public static RecordSeries[] getItemList(DbConn myConn, int loc, boolean common, String sort, String termSelection) {
        return getItemList(myConn, (int[]) null, sort, ((loc > 0 || !termSelection.equals("none")) ? "subset" : "all"), loc, common, termSelection);
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static RecordSeries[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (int[]) null, sort, "all", 0, true, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    */
    public static RecordSeries[] getItemList(DbConn myConn, int[] list) {
        return getItemList(myConn, list, "none", "subset", 0, false, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static RecordSeries[] getItemList(DbConn myConn, int[] list, String sort) {
        return getItemList(myConn, list, sort, "subset", 0, false, "none");
    }


    /**
    * Get a list of RecordSeries from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    * @param loc        An int defining what location/functional organization to look up
    * @param common     A boolean defining if common is included in location lookups
    * @param termSelection  A string defining the term selection (Long (L), Short (S), or none)
    */
    public static RecordSeries[] getItemList(DbConn myConn, int[] list, String sort, String flag, int loc, boolean common, String termSelection) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        RecordSeries[] item = null;
//System.out.println("RecordSeries - got Here 1");
        try {
            Connection conn = myConn.conn;

            HashMap retentionGroupFlags = RetentionGroupFlag.getItemHM(myConn);

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".record_series ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".record_series ";
            sqlFrom = " FROM " + mySchemaPath + ".record_series rs, " + mySchemaPath + ".citation_query cc, " + mySchemaPath + ".retention_group_flags rgf, " + mySchemaPath + ".fo_query fo ";
            sqlWhere = " WHERE rs.citation=cc.cid AND rgf.id = rs.retentiongroupflag AND rs.location=fo.foid ";
            if (flag.equals("subset")) {
                sqlWhere += " AND ";
                int sqlCount = 0;
                if (list != null) {
                    sqlWhere += "id IN (";
                    for (int i = 0; i<list.length; i++) {
                        sqlWhere += (i!=0) ? ", " : "";
                        sqlWhere += list[i];
                    }
                    sqlWhere += ") ";
                    sqlCount++;
                }
                if (loc > 0) {
                    if (sqlCount > 0) {
                        sqlWhere += "AND ";
                    }
                    sqlWhere += "(location = " + loc + ((!common) ? ")" : "") + " ";
                    sqlCount++;
                }
                if (common) {
                    if (sqlCount > 0) {
                        sqlWhere += "OR ";
                    }
                    sqlWhere += ((loc == 0) ? "(" : "") + "commonflag = 'T') ";
                    sqlCount++;
                }
                if (!termSelection.equals("none")) {
                    if (sqlCount > 0) {
                        //if (list == null && loc == 0 && common) {
                        //    sqlWhere += "OR ";
                        //} else {
                            sqlWhere += "AND ";
                        //}
                    }
                    //sqlWhere += "retentiongroupflag = '" + termSelection + "' ";
                    sqlWhere += "rgf.code = '" + termSelection + "' ";
                    sqlCount++;
                }
            }

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new RecordSeries[returnSize];

            sqlcode = "SELECT rs.id, rs.description, rs.commonflag, rs.location, rs.ocrwmcode, rs.citation, rs.cutoff, rs.retentionperiod, " +
                  "rs.retentiongroupflag, rs.retentioncode, rs.dateadded, rs.datedepricated, rs.datechanged, rs.status " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new RecordSeries();
                item[i].ID = rs.getInt(1);
                item[i].description = rs.getString(2);
                item[i].commonFlag = rs.getString(3);
                item[i].location = rs.getInt(4);
                if (item[i].location != 0) {
                    FOrganization tmp = new FOrganization(item[i].location, myConn);
                    item[i].locationDescription = tmp.getDescription();
                } else {
                    item[i].locationDescription = "";
                }
                item[i].ocrwmcode = rs.getString(5);
                item[i].citation = rs.getInt(6);
                if (item[i].citation != 0) {
                    CCategory tmp = new CCategory(item[i].citation, myConn);
                    item[i].citationDescription = tmp.getDescription();
                } else {
                    item[i].citationDescription = "";
                }
                item[i].cutoff = rs.getInt(7);
                if (item[i].cutoff != 0) {
                    CutoffList tmp = new CutoffList(item[i].cutoff, myConn);
                    item[i].cutoffDescription = tmp.getDescription();
                } else {
                    item[i].cutoffDescription = "";
                }
                item[i].retentionPeriod = rs.getInt(8);
                if (item[i].retentionPeriod != 0) {
                    RetentionPeriod tmp = new RetentionPeriod(item[i].retentionPeriod, myConn);
                    item[i].retentionPeriodDescription = tmp.getDescription();
                } else {
                    item[i].retentionPeriodDescription = "";
                }
                item[i].retentionGroupFlag = rs.getInt(9);
                item[i].retentionCode = rs.getString(10);
                if (item[i].retentionCode != null) {
                    RetentionCode rc = new RetentionCode(item[i].retentionCode, myConn);
                    item[i].retentionCodeDescription = rc.getDescription();
                } else {
                    item[i].retentionCodeDescription = "";
                }
                item[i].dateAdded = rs.getDate(11);
                item[i].dateDepricated = rs.getDate(12);
                item[i].dateChanged = rs.getDate(13);
                item[i].status = rs.getString(14);

                item[i].retentionGroupFlagCode = ((RetentionGroupFlag) retentionGroupFlags.get(item[i].retentionGroupFlag)).getCode();
                item[i].retentionGroupFlagDescription = ((RetentionGroupFlag) retentionGroupFlags.get(item[i].retentionGroupFlag)).getDescription();

                item[i].cw = Crosswalk.getItemList(myConn, item[i].ID);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - RecordSeries getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }




}
