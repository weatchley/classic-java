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
* Crosswalk is the class for Crosswalks in the db
*
* @author   Bill Atchley
*/
public class Crosswalk {
    private int ID = 0;
    private int RSID = 0;
    private String name = null;
    private int QADesignation = 0;
    private String QADesignationDescription = null;
    private String accessRestrictions = null;
    private String accessRestrictionsDescription = null;
    private String unscheduled = "F";
    private int requirements = 0;
    private String requirementsDescription = null;
    private Requirement [] requirementList = null;
    private String requirementsOther = null;
    private String freezeHoldFlag = "F";
    private String freezeHoldText = null;
    private String vitalRecord = "F";
    private java.sql.Date  dateAdded = null;
    private java.sql.Date  dateDepricated = null;
    private java.sql.Date  dateChanged = null;
    private String status = "new";
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
    * Creates a new empty Crosswalk object
    */
    public Crosswalk () {
        ID = 0;
        init();
    }


    /**
    * Creates a Crosswalk object and uses the given id to populate it from the db
    *
    * @param id     The id of the Crosswalk to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Crosswalk (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Crosswalk from the db and stores it in the current Crosswalk object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Crosswalk from the db and stores it in the current Crosswalk object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        RSID = 0;
        name = null;
        QADesignation = 0;
        QADesignationDescription = null;
        accessRestrictions = null;
        accessRestrictionsDescription = null;
        unscheduled = "F";
        requirements = 0;
        requirementsDescription = null;
        requirementsOther = null;
        freezeHoldFlag = "F";
        freezeHoldText = null;
        vitalRecord = "F";
        dateAdded = null;
        dateDepricated = null;
        dateChanged = null;
        status = "new";
        isNew = true;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, recordseriesid, name, qadesignation, accessrestrictions, unscheduled, requirements, requirementsother, " +
                  "freezeholdflag, freezeholdtext, vitalrecord, dateadded, datedepricated, datechanged, status FROM " + SCHEMAPATH + ".crosswalk WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            int myRSID = rs.getInt(2);
            String myName = rs.getString(3);
            int myQADesignation = rs.getInt(4);
            String myAccessRestrictions = rs.getString(5);
            String myUnscheduled = rs.getString(6);
            int myRequirements = rs.getInt(7);
            String myRequirementsOther = rs.getString(8);
            String myFreezeHoldFlag = rs.getString(9);
            String myFreezeHoldText = rs.getString(10);
            String myVitalRecord = rs.getString(11);
            java.sql.Date myDateAdded = rs.getDate(12);
            java.sql.Date myDateDepricated = rs.getDate(13);
            java.sql.Date myDateChanged = rs.getDate(14);
            String myStatus = rs.getString(15);
//System.out.println("Crosswalk - lookup - Got here 1");
            if (myID == id) {
                ID = myID;
                RSID = myRSID;
                name = myName;
                QADesignation = myQADesignation;
                if (QADesignation != 0) {
                    QADesignator qad = new QADesignator(QADesignation, myConn);
                    QADesignationDescription = qad.getDescription();
                } else {
                    QADesignationDescription = "";
                }
                accessRestrictions = myAccessRestrictions;
                if (accessRestrictions != null) {
                    ARestriction ar = new ARestriction(accessRestrictions, myConn);
                    accessRestrictionsDescription = ar.getDescription();
                } else {
                    accessRestrictionsDescription = "";
                }
                unscheduled = myUnscheduled;
                requirements = myRequirements;
                if (requirements != 0) {
                    Requirement req = new Requirement(requirements, myConn);
                    requirementsDescription = req.getDescription();
                } else {
                    requirementsDescription = "";
                }
                stmt2 = conn.createStatement ();
                String sqlcode2 = "SELECT count(*) FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk=" + ID;
//System.out.println(sqlcode2);
                rs2 = stmt2.executeQuery(sqlcode2);
                rs2.next();
                int returnSize = rs2.getInt(1);
                //rs2.close();
                requirementList = new Requirement[returnSize];
                sqlcode2 = "SELECT crosswalk, requirement FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk=" + ID;
//System.out.println(sqlcode2);
                rs2 = stmt2.executeQuery(sqlcode2);
                int i=0;
                while (rs2.next()) {
                    requirementList[i] = new Requirement(rs2.getInt(2), myConn);
                    i++;
                }
                requirementsOther = myRequirementsOther;
                freezeHoldFlag = myFreezeHoldFlag;
                freezeHoldText = myFreezeHoldText;
                vitalRecord = myVitalRecord;
                dateAdded = myDateAdded;
                dateDepricated = myDateDepricated;
                dateChanged = myDateChanged;
                status = myStatus;
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Crosswalk lookup");
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
            if (rs2 != null)
                try { rs2.close(); } catch (Exception i) {}
            if (stmt2 != null)
                try { stmt2.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieve the id for the current Crosswalk
    *
    * @return     The id for the current Crosswalk
    */
    public int getID() {
        int id = ID;
        return(id);
    }


    /**
    * Retrieve the id for the current record set
    *
    * @return     The id for the current record set
    */
    public int getRSID() {
        int id = RSID;
        return(id);
    }


    /**
    * Retrieve the name for the current Crosswalk
    *
    * @return     The text for the current Crosswalk
    */
    public String getName() {
        return(name);
    }



    /**
    * Retrieve the QA Designation id for the current Crosswalk
    *
    * @return     The QA Designation id for the current Crosswalk
    */
    public int getQADesignationID() {
        return(QADesignation);
    }



    /**
    * Retrieve the QA Designation text for the current Crosswalk
    *
    * @return     The QA Designation text for the current Crosswalk
    */
    public String getQADesignation() {
        return(QADesignationDescription);
    }


    /**
    * Retrieve the Access Restrictions code for the current Crosswalk
    *
    * @return     The Access Restrictions code for the current Crosswalk
    */
    public String getAccessRestrictionsCode() {
        return(accessRestrictions);
    }


    /**
    * Retrieve the Access Restrictions text for the current Crosswalk
    *
    * @return     The Access Restrictions text for the current Crosswalk
    */
    public String getAccessRestrictions() {
        return(accessRestrictionsDescription);
    }


    /**
    * Retrieve the unscheduled flag for the current Crosswalk
    *
    * @return     The unscheduled flag for the current Crosswalk
    */
    public String getUnscheduled() {
        return(unscheduled);
    }


    /**
    * Retrieve the requirements id for the current Crosswalk
    *
    * @return     The requirements id for the current Crosswalk
    */
    public int getRequirementsID() {
        return(requirements);
    }



    //**
    //* Retrieve the requirements text for the current Crosswalk
    //*
    //* //@return     The requirements text for the current Crosswalk
    //*/
    //public String getRequirements() {
    //    return(requirementsDescription);
    //}


    /**
    * Retrieve the requirements text for the current Crosswalk
    *
    * @return     The requirements text for the current Crosswalk
    */
    public String getRequirements() {
        String tmp = "";
        if (requirementList != null) {
            for (int i=0; i<requirementList.length; i++) {
                tmp += ((i>0) ? "\n" : "") + requirementList[i].getDescription();
            }
        }
        return(tmp);
    }


    /**
    * Retrieve the requirement list for the current Crosswalk
    *
    * @return     The requirement list for the current Crosswalk
    */
    public Requirement [] getRequirementList() {
        return(requirementList);
    }


    /**
    * Retrieve the other requirements for the current Crosswalk
    *
    * @return     The text for the current Crosswalk
    */
    public String getRequirementsOther() {
        return(requirementsOther);
    }


    /**
    * Retrieve the Freeze Hold Flag for the current Crosswalk
    *
    * @return     The Freeze Hold Flag for the current Crosswalk
    */
    public String getFreezeHoldFlag() {
        return(freezeHoldFlag);
    }


    /**
    * Retrieve the Freeze Hold Flag text for the current Crosswalk
    *
    * @return     The Freeze Hold Flag text for the current Crosswalk
    */
    public String getFreezeHoldFlagText() {
        return(freezeHoldText);
    }


    /**
    * Retrieve the vital record Flag for the current Crosswalk
    *
    * @return     The Vital Record Flag for the current Crosswalk
    */
    public String getVitalRecord() {
        return(vitalRecord);
    }


    /**
    * Retrieve the date added for the current Crosswalk
    *
    * @return     The date added for the current Crosswalk
    */
    public java.sql.Date getDateAddedSQL() {
        return(dateAdded);
    }


    /**
    * Retrieve the date added for the current Crosswalk
    *
    * @return     The date added for the current Crosswalk
    */
    public java.util.Date getDateAdded() {
        java.util.Date tmpDate = ((dateAdded!=null) ? new java.util.Date(dateAdded.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date depricated for the current Crosswalk
    *
    * @return     The date depricated for the current Crosswalk
    */
    public java.sql.Date getDateDepricatedSQL() {
        return(dateDepricated);
    }


    /**
    * Retrieve the date depricated for the current Crosswalk
    *
    * @return     The date depricated for the current Crosswalk
    */
    public java.util.Date getDateDepricated() {
        java.util.Date tmpDate = ((dateDepricated!=null) ? new java.util.Date(dateDepricated.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date changed for the current Crosswalk
    *
    * @return     The date changed for the current Crosswalk
    */
    public java.sql.Date getDateChangedSQL() {
        return(dateChanged);
    }


    /**
    * Retrieve the date changed for the current Crosswalk
    *
    * @return     The date changed for the current Crosswalk
    */
    public java.util.Date getDateChanged() {
        java.util.Date tmpDate = ((dateChanged!=null) ? new java.util.Date(dateChanged.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the status for the current Crosswalk
    *
    * @return     The status for the current Crosswalk
    */
    public String getStatus() {
        return(status);
    }


    /**
    * Set the Record Set id for the current Crosswalk
    *
    * @param id     The new Record Set id of the Crosswalk (int)
    */
    public void setRSID(int id) {
        RSID = id;
    }


    /**
    * Set the name for the current Crosswalk
    *
    * @param txt     The new name of the Crosswalk (int)
    */
    public void setName(String txt) {
        name = txt;
    }


    /**
    * Set the QA designation id for the current Crosswalk
    *
    * @param id     The new QA designation of the Crosswalk (int)
    */
    public void setQADesignation(int id) {
        QADesignationDescription = null;
        QADesignation = id;
    }


    /**
    * Set the access restrictions code for the current Crosswalk
    *
    * @param code     The new access restrictions code of the Crosswalk (String)
    */
    public void setAccessRestrictions(String code) {
        accessRestrictionsDescription = null;
        accessRestrictions = code;
    }


    /**
    * Set the unscheduled flag for the current Crosswalk
    *
    * @param flag     The new unscheduled flag of the Crosswalk (String)
    */
    public void setUnscheduled(String flag) {
        unscheduled = (flag != null) ? ((flag.equals("T")) ? "T" : "F") : "F";
    }


    /**
    * Set the requirements id for the current Crosswalk
    *
    * @param id     The new requirements id of the Crosswalk (int)
    */
    public void setRequirements(int id) {
        requirementsDescription = null;
        requirements = id;
    }


    /**
    * Set the requirements list for the current Crosswalk
    *
    * @param list     The new requirement list of the Crosswalk (Requirement)
    */
    public void setRequirements(Requirement [] list) {
        requirementList = list;
    }


    /**
    * Set the requirements other text for the current Crosswalk
    *
    * @param txt     The new requirements other text of the Crosswalk (String)
    */
    public void setRequirementsOther(String txt) {
        requirementsOther = txt;
    }


    /**
    * Set the freeze hold flag for the current Crosswalk
    *
    * @param flag     The new freeze hold flag of the Crosswalk (String)
    */
    public void setFreezeHoldFlag(String flag) {
        freezeHoldFlag = (flag != null) ? ((flag.equals("T")) ? "T" : "F") : "F";
    }


    /**
    * Set the freeze hold text for the current Crosswalk
    *
    * @param txt     The new freeze hold text of the Crosswalk (String)
    */
    public void setFreezeHoldText(String txt) {
        freezeHoldText = txt;
    }


    /**
    * Set the vital record flag for the current Crosswalk
    *
    * @param flag     The new vital record flag of the Crosswalk (String)
    */
    public void setVitalRecord(String flag) {
        vitalRecord = (flag != null) ? ((flag.equals("T")) ? "T" : "F") : "F";
    }


    /**
    * Save the current Crosswalk to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current Crosswalk to the DB
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
                sqlcode = "SELECT " + SCHEMAPATH + ".CROSSWALK_ID_SEQ.NEXTVAL FROM dual";
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                int myID = rs.getInt(1);
                ID = myID;

                //sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".crosswalk (id, recordseriesid, name, qadesignation, accessrestrictions, unscheduled, requirements, requirementsother, " +
                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".crosswalk (id, recordseriesid, name, qadesignation, accessrestrictions, unscheduled, requirementsother, " +
                    "freezeholdflag, freezeholdtext, vitalrecord, dateadded, datedepricated, datechanged, status) " +
                    "VALUES (" + ID + ", " + RSID + ", " +
                    ((name == null || name == "n/a") ? "NULL" : "?") + ", " +
                    QADesignation + ", " +
                    ((accessRestrictions == null || accessRestrictions == "n/a") ? "NULL" : "?") + ", " +
                    "'" + unscheduled + "', " +
                    //requirements + ", " +
                    ((requirementsOther == null || requirementsOther == "n/a") ? "NULL" : "?") + ", " +
                    "'" + freezeHoldFlag + "', " +
                    ((freezeHoldText == null || freezeHoldText == "n/a") ? "NULL" : "?") + ", " +
                    "'" + vitalRecord + "', " +
                    " SYSDATE, NULL, SYSDATE, NULL" +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (name != null && name != "n/a") {
                    pstmt.setString(++sqlID, name);
                }
                if (accessRestrictions != null && accessRestrictions != "n/a") {
                    pstmt.setString(++sqlID, accessRestrictions);
                }
                if (requirementsOther != null && requirementsOther != "n/a") {
                    pstmt.setString(++sqlID, requirementsOther);
                }
                if (freezeHoldText != null && freezeHoldText != "n/a") {
                    pstmt.setString(++sqlID, freezeHoldText);
                }
                rows = pstmt.executeUpdate();
                isNew = false;
// do auth stuff
            } else {
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".crosswalk SET " +
                    "recordseriesid = " + RSID + ", " +
                    "name = " + ((name == null || name == "n/a") ? "NULL" : "?") + ", " +
                    "qadesignation = " + QADesignation + ", " +
                    "accessrestrictions = " + ((accessRestrictions == null || accessRestrictions == "n/a") ? "NULL" : "?") + ", " +
                    "unscheduled = '" + unscheduled + "', " +
                    //"requirements = " + requirements + ", " +
                    "requirementsother = " + ((requirementsOther == null || requirementsOther == "n/a") ? "NULL" : "?") + ", " +
                    "freezeholdflag = '" + freezeHoldFlag + "', " +
                    "freezeholdtext = " + ((freezeHoldText == null || freezeHoldText == "n/a") ? "NULL" : "?") + ", " +
                    "vitalrecord = '" + vitalRecord + "', " +
                    "datedepricated = " + ((dateDepricated == null) ? "NULL" : "?") + ", " +
                    "dateChanged = SYSDATE, " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "?") + " " +
                    "WHERE id = " + ID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (name != null && name != "n/a") {
                    pstmt.setString(++sqlID, name);
                }
                if (accessRestrictions != null && accessRestrictions != "n/a") {
                    pstmt.setString(++sqlID, accessRestrictions);
                }
                if (requirementsOther != null && requirementsOther != "n/a") {
                    pstmt.setString(++sqlID, requirementsOther);
                }
                if (freezeHoldText != null && freezeHoldText != "n/a") {
                    pstmt.setString(++sqlID, freezeHoldText);
                }
                if (dateDepricated != null) {
                    pstmt.setDate(++sqlID, dateDepricated);
                }
                rows = pstmt.executeUpdate();
            }
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk = " + ID
                );
            rows = pstmt.executeUpdate();
            if (requirementList != null) {
                for (int i=0; i< requirementList.length; i++) {
                    sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".crosswalk_requirements (crosswalk, requirement) values (" + ID + ", " + requirementList[i].getID() + "); end;";
//System.out.println(sqlcode);
                    pstmt = conn.prepareStatement(sqlcode);
                    rows = pstmt.executeUpdate();
                }
            }
            isNew = false;
            conn.commit();
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Crosswalk save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Crosswalk save");
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
    * Remove the current Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current Crosswalk from the DB
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
                "DELETE FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk = " + ID
                );
            rows = pstmt.executeUpdate();
            pstmt = conn.prepareStatement(
                "DELETE FROM " + SCHEMAPATH + ".crosswalk WHERE id = " + ID
                );
            rows = pstmt.executeUpdate();


            ID = 0;
            RSID = 0;
            name = null;
            QADesignation = 0;
            QADesignationDescription = null;
            accessRestrictions = null;
            accessRestrictionsDescription = null;
            unscheduled = "F";
            requirements = 0;
            requirementsDescription = null;
            requirementList = null;
            requirementsOther = null;
            freezeHoldFlag = "F";
            freezeHoldText = null;
            vitalRecord = "F";
            dateAdded = null;
            dateDepricated = null;
            dateChanged = null;
            status = "new";
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Crosswalk drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - Crosswalk drop");
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
    * Get a list of Crosswalk from the DB
    *
    */
    public static Crosswalk[] getItemList() {
        DbConn myConn = new DbConn();
        Crosswalk[] cList = getItemList(myConn, -1, (int[]) null, "name", "all");
        myConn.release();
        return cList;
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    */
    public static Crosswalk[] getItemList(DbConn myConn) {
        return getItemList(myConn, -1, (int[]) null, "name", "all");
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static Crosswalk[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, -1, (int[]) null, sort, "all");
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    */
    public static Crosswalk[] getItemList(DbConn myConn, int[] list) {
        return getItemList(myConn, -1, list, "none", "subset");
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    * @param id         A record set id to be fetched from the db
    */
    public static Crosswalk[] getItemList(DbConn myConn, int id) {
        return getItemList(myConn, id, (int[]) null, "name", "subset");
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static Crosswalk[] getItemList(DbConn myConn, int[] list, String sort) {
        return getItemList(myConn, -1, list, sort, "subset");
    }


    /**
    * Get a list of Crosswalk from the DB
    *
    * @param myConn     Connection to the database
    * @param rsid       The Record Set id to be fetched from the db
    * @param list       An int array of id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    */
    public static Crosswalk[] getItemList(DbConn myConn, int rsid, int[] list, String sort, String flag) {
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
        Crosswalk[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            stmt2 = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "rpms";

            //sqlFrom = " FROM " + SCHEMAPATH + ".crosswalk ";
            //sqlFrom = " FROM " + myConn.getSchemaPath() + ".crosswalk ";
            sqlFrom = " FROM " + mySchemaPath + ".crosswalk ";
            if (flag.equals("subset")) {
                sqlWhere = " WHERE ";
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
                if (rsid > -1) {
                    if (sqlCount > 0) {
                        sqlWhere += "AND ";
                    }
                    sqlWhere += "recordseriesid = " + rsid;
                }
            }

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Crosswalk[returnSize];

            sqlcode = "SELECT id, recordseriesid, name, qadesignation, accessrestrictions, unscheduled, requirements, requirementsother, " +
                  "freezeholdflag, freezeholdtext, vitalrecord, dateadded,datedepricated,datechanged,status " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
//System.out.println("Crosswalk - getItemList - Got here 1");
            while (rs.next()) {
                item[i] = new Crosswalk();
                item[i].ID = rs.getInt(1);
                item[i].RSID = rs.getInt(2);
                item[i].name = rs.getString(3);
                item[i].QADesignation = rs.getInt(4);
                if (item[i].QADesignation != 0) {
                    QADesignator qad = new QADesignator(item[i].QADesignation, myConn);
                    item[i].QADesignationDescription = qad.getDescription();
                } else {
                    item[i].QADesignationDescription = "";
                }
                item[i].accessRestrictions = rs.getString(5);
                if (item[i].accessRestrictions != null) {
                    ARestriction ar = new ARestriction(item[i].accessRestrictions, myConn);
                    item[i].accessRestrictionsDescription = ar.getDescription();
                } else {
                    item[i].accessRestrictionsDescription = "";
                }
                item[i].unscheduled = rs.getString(6);
                item[i].requirements = rs.getInt(7);
                if (item[i].requirements != 0) {
                    Requirement req = new Requirement(item[i].requirements, myConn);
                    item[i].requirementsDescription = req.getDescription();
                } else {
                    item[i].requirementsDescription = "";
                }
                String sqlcode2 = "SELECT count(*) FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk=" + item[i].ID;
//System.out.println(sqlcode2);
                rs2 = stmt2.executeQuery(sqlcode2);
                rs2.next();
                returnSize = rs2.getInt(1);
                //rs2.close();
                item[i].requirementList = new Requirement[returnSize];
                sqlcode2 = "SELECT crosswalk, requirement FROM " + SCHEMAPATH + ".crosswalk_requirements WHERE crosswalk="  + item[i].ID;
//System.out.println(sqlcode2);
                rs2 = stmt2.executeQuery(sqlcode2);
                int j=0;
                while (rs2.next()) {
                    item[i].requirementList[j] = new Requirement(rs2.getInt(2), myConn);
                    j++;
                }
                item[i].requirementsOther = rs.getString(8);
                item[i].freezeHoldFlag = rs.getString(9);
                item[i].freezeHoldText = rs.getString(10);
                item[i].vitalRecord = rs.getString(11);
                item[i].dateAdded = rs.getDate(12);
                item[i].dateDepricated = rs.getDate(13);
                item[i].dateChanged = rs.getDate(14);
                item[i].status = rs.getString(15);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Crosswalk list lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Crosswalk list lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
            if (rs2 != null)
                try { rs2.close(); } catch (Exception i) {}
            if (stmt2 != null)
                try { stmt2.close(); } catch (Exception i) {}
        }

        return item;
    }




}
