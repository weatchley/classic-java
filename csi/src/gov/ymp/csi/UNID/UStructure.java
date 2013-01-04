package gov.ymp.csi.UNID;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;

/**
* UStructure is the base class for collections/sets of UNID's in the CSI
*
* @author   Bill Atchley
*/
public abstract class UStructure {
    public long uID = 0;
    public String type = null;
    public String subType = null;
    public String description = null;
    public long creator = 0;
    public java.sql.Date creationDate = null;
    public java.sql.Date expirationDate = null;
    public String status = null;
    public UNID myUID = null;
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
        SCHEMA = tempDB.getSchemaName();
        SCHEMAPATH = tempDB.getSchemaPath();
        DBTYPE = tempDB.getDBType();
    }

    /**
    * Creates a new empty UStructure object
    */
    public UStructure () {
        uID = 0;
        init();
        //myUID = new UNID();
        ////myUID.create("ustructure", myUID.getID());
        //myUID.create("ustructure");
        //uID = myUID.getID();
        description = "";
    }

    /**
    * Creates a new empty UStructure object
    *
    * @param myConn Connection to the database
    */
    public UStructure (DbConn myConn) {
        uID = 0;
        init();
        myUID = new UNID();
        //myUID.create("ustructure", myUID.getID(), myConn);
        myUID.create("ustructure", myConn);
        uID = myUID.getID();
        description = "";
    }

    /**
    * Creates a new empty UStructure object
    *
    * @param myConn Connection to the database
    * @param userID  The person ID of the user performing the action
    */
    public UStructure (DbConn myConn, long userID) {
        uID = 0;
        init();
        myUID = new UNID();
        //myUID.create("ustructure", myUID.getID(), myConn, userID);
        myUID.create("ustructure", myConn, userID);
        uID = myUID.getID();
        description = "";
    }

    /**
    * Creates a UStructure object and uses the given id to populate it from the db
    *
    * @param id     The id of the UStructure to lookup from the db (long)
    */
    public UStructure (long id) {
        init();
        lookup(id);
    }

    /**
    * Creates a UStructure object and uses the given id to populate it from the db
    *
    * @param id     The id of the UStructure to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UStructure (long id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }

    /**
    * Creates a UStructure object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UStructure to lookup from the db (long)
    */
    public UStructure (UNID uid) {
        init();
        lookup(uid.getID());
    }

    /**
    * Creates a UStructure object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the UStructure to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public UStructure (UNID uid, DbConn myConn) {
        init();
        lookup(uid.getID(), myConn);
    }

    /**
    * Creates a UStructure object and uses the given Description to populate it from the db
    *
    * @param descr     The Description to lookup from the db (long)
    * @param myConn    Connection to the database
    */
    public UStructure (String descr, DbConn myConn) {
        init();
        long myID = 0;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String outLine = "";
        try {
            Connection conn = myConn.conn;
            String sqlcode = "SELECT id FROM " + SCHEMAPATH + ".ustructure WHERE description=?";
            pstmt = conn.prepareStatement(sqlcode);
//System.out.println(sqlcode);
            pstmt.setString(1, descr);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                myID = rs.getLong(1);
            }
            uID = myID;
//System.out.println("Structure ID: " + myID + ", uID: " + uID + ", Descr: " + descr);
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure lookup by description");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine);
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (pstmt != null)
                try { pstmt.close(); } catch (Exception i) {}
        }


        lookup(myID, myConn);
    }

    /**
    * Retrieves a UStructure from the db and stores it in the current UStructure object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    */
    public void lookup (long id) {
        DbConn myConn = new DbConn(SCHEMA);
        lookup(id, myConn);
        myConn.release();
    }

    /**
    * Retrieves a UStructure from the db and stores it in the current UStructure object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    */
    public void getInfo (long id) {
        lookup(id);
    }

    /**
    * Retrieves a UStructure from the db and stores it in the current UStructure object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void getInfo (long id, DbConn myConn) {
        lookup(id, myConn);
    }

    /**
    * Retrieves a UStructure from the db and stores it in the current UStructure object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public abstract void lookup (long id, DbConn myConn);

    /**
    * Retrieves a UStructure from the db and stores it in the current UStructure object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void usLookup (long id, DbConn myConn) {
        uID = 0;
        type = null;
        subType = null;
        description = null;
        creator = 0;
        creationDate = null;
        expirationDate = null;
        status = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            myUID = new UNID(id);
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, type, subtype, NVL(description,'n/a'), NVL(creator, 0), creationdate, expirationdate, status FROM " + SCHEMAPATH + ".ustructure WHERE id=" + id;
            //String sqlcode = "SELECT id, type, subtype, ISNULL(description,'n/a'), ISNULL(creator, 0), creationdate, expirationdate, status FROM " + SCHEMAPATH + ".ustructure WHERE id=" + id
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long myID = rs.getLong(1);
            String myType = rs.getString(2);
            String mySubType = rs.getString(3);
            String myDescription = rs.getString(4);
            long myCreator = rs.getLong(5);
            java.sql.Date myCreationDate = rs.getDate(6);
            java.sql.Date myExpirationDate = rs.getDate(7);
            String myStatus = rs.getString(8);
            if (myID == id) {
                uID = myID;
                type = myType;
                subType = mySubType;
                description = myDescription;
                creator = myCreator;
                creationDate = myCreationDate;
                expirationDate = myExpirationDate;
                status = myStatus;
                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure lookup");
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
    }


    /**
    * Retrieves a list of UStructure ID's from the db with the one of the given creator id's
    *
    * @param type     The type of structure to lookup from the db (uset, ulist, etc...)
    * @param list     A long array of creator id's to be fetched from the db
    * @param myConn   Connection to the database
    */
    public static long [] getIDList (String type, long [] list, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        long [] idList = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlFrom = "FROM " + myConn.getSchemaPath() + ".ustructure ";
            String sqlWhere = "WHERE type='" + type + "' ";
            if (list!=null) {
                sqlWhere += " AND creator IN (";
                for (int i = 0; i<list.length; i++) {
                    sqlWhere += (i!=0) ? ", " : "";
                    sqlWhere += list[i];
                }
                sqlWhere += ") ";
            }

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            idList = new long[returnSize];

            sqlcode = "SELECT id " + sqlFrom + sqlWhere + " ORDER BY description";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                idList[i] = rs.getLong(1);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure id list lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure id list lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return idList;
    }


    /**
    * Retrieves a list of UStructure ID's from the db with the given subtype
    *
    * @param type     The type of structure to lookup from the db
    * @param subtype  The sub type to lookup from the db
    * @param myConn   Connection to the database
    */
    public static long [] getSubTypeIDList (String type, String subtype, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        long [] idList = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlFrom = "FROM " + myConn.getSchemaPath() + ".ustructure ";
            String sqlWhere = "WHERE type='" + type + "' AND subtype='" + subtype + "' ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            idList = new long[returnSize];

            sqlcode = "SELECT id " + sqlFrom + sqlWhere + " ORDER BY description";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                idList[i] = rs.getLong(1);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure id by subtype list lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - UStructure id by subtype list lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return idList;
    }


    /**
    * Retrieve the UNID id for the current UStructure
    *
    * @return id    The UNID id for the current UStructure
    */
    public long getID() {
        long id = uID;
        return(id);
    }

    /**
    * Retrieve the UNID for the current UStructure
    *
    * @return myUNID    The UNID id for the current UStructure
    */
    public UNID getUNID() {
        UNID myUNID = myUID;
        return(myUNID);
    }

    /**
    * Retrieve the type for the current UStructure
    *
    * @return type    The type for the current UStructure
    */
    public String getType() {
        return(type);
    }

    /**
    * Retrieve the subType for the current UStructure
    *
    * @return subType    The subType for the current UStructure
    */
    public String getSubType() {
        return(subType);
    }

    /**
    * Retrieve the description for the current UStructure
    *
    * @return description    The description for the current UStructure
    */
    public String getDescription() {
        return(description);
    }

    /**
    * Retrieve the creator of the current UStructure
    *
    * @return creator    The creator of the current UStructure
    */
    public long getCreator() {
        return(creator);
    }

    /**
    * Retrieve the creation date for the current UStructure as a java.sql.Date
    *
    * @return creationDate    The creation date for the current UStructure
    */
    public java.sql.Date getCreationDateSQL() {
        return(creationDate);
    }

    /**
    * Retrieve the creation date for the current UStructure
    *
    * @return creationDate    The creation date for the current UStructure
    */
    public java.util.Date getCreationDate() {
        java.util.Date tmpDate = null;
        if (expirationDate != null) {
            tmpDate = new java.util.Date(creationDate.getTime());
        }
        return(tmpDate);
    }

    /**
    * Retrieve the expiration date for the current UStructure as a java.sql.Date
    *
    * @return expirationDate    The expiration date for the current UStructure
    */
    public java.sql.Date getExpirationDateSQL() {
        return(expirationDate);
    }

    /**
    * Retrieve the expiration date for the current UStructure
    *
    * @return expirationDate    The expiration date for the current UStructure
    */
    public java.util.Date getExpirationDate() {
        java.util.Date tmpDate = null;
        if (expirationDate != null) {
            tmpDate = new java.util.Date(expirationDate.getTime());
        }
        return(tmpDate);
    }

    /**
    * Retrieve the status for the current UStructure
    *
    * @return status    The status for the current UStructure
    */
    public String getStatus() {
        return(status);
    }

    /**
    * Set the type for the current UStructure
    *
    * @param typ     The new type of the UStructure (String)
    */
    public void setType(String typ) {
        type = typ;
    }

    /**
    * Set the subType for the current UStructure
    *
    * @param sub     The new subType of the UStructure (String)
    */
    public void setSubtype(String sub) {
        subType = sub;
    }

    /**
    * Set the description for the current UStructure
    *
    * @param descr     The new description of the UStructure (String)
    */
    public void setDescription(String descr) {
        description = descr;
    }

    /**
    * Set the creator of the current UStructure
    *
    * @param create     The new creator of the UStructure (long)
    */
    public void setCreator(long create) {
        creator = create;
    }

    /**
    * Set the expiration date for the current UStructure
    *
    * @param expire     The new expiration date of the UStructure (java.sql.Date)
    */
    public void setExpirationDate(java.sql.Date expire) {
        expirationDate = expire;
    }

    /**
    * Set the expiration date for the current UStructure
    *
    * @param expire     The new expiration date of the UStructure (java.sql.Date)
    */
    public void setExpirationDate(java.util.Date expire) {
        java.sql.Date tmpDate = new java.sql.Date(expire.getTime());
        expirationDate = tmpDate;
    }

    /**
    * Set the status for the current UStructure
    *
    * @param stat     The new status of the UStructure (String)
    */
    public void setStatus(String stat) {
        status = stat;
    }


    /**
    * Save the current UStructure to the DB
    */
    public void save() {
        DbConn myConn = new DbConn(SCHEMA);
        save(myConn, 0);
        myConn.release();
    }


    /**
    * Save the current UStructure to the DB
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }

    /**
    * Save the current UStructure to the DB
    */
    public abstract void save(DbConn myConn, long userID);

    /**
    * Save the current UStructure to the DB
    *
    * @param myConn     Connection to the database
    * @param id         The id of the UNID to lookup from the db (long)
    * @param userID     The id of the ov the user performing the action (long)
    */
    public void usSave(DbConn myConn, long userID) {
        //save funciton
        String outLine = "";
        int rows;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sqlcode = "";
        try {

            Connection conn = myConn.conn;

            myUID.save(conn);

            // Create a Statement
//            stmt = conn.createStatement ();
            if (isNew) {
                sqlcode = "INSERT INTO " + SCHEMAPATH + ".ustructure (id, type, subtype, description, creator, expirationdate, status) " + "VALUES (" + uID + ", " +
                    ((type == null || type == "n/a") ? "NULL" : "'" + type + "'") + ", " +
                    ((subType == null || subType == "n/a") ? "NULL" : "'" + subType + "'") + ", " +
                    ((description == null || description == "n/a") ? "NULL" : "'" + description + "'") + ", " +
                    ((creator == 0) ? "NULL" : "'" + creator + "'") + ", " +
                    ((expirationDate == null) ? "NULL" : "?") + ", " +
                    ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ")";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                if (expirationDate != null) {
                    pstmt.setDate(1, expirationDate);
                }
                rows = pstmt.executeUpdate();
// do auth stuff
            } else {
                sqlcode = "UPDATE " + SCHEMAPATH + ".ustructure SET " +
                    "type = " + ((type == null || type == "n/a") ? "NULL" : "'" + type + "'") + ", " +
                    "subType = " + ((subType == null || subType == "n/a") ? "NULL" : "'" + subType + "'") + ", " +
                    "description = " + ((description == null || description == "n/a") ? "NULL" : "'" + description + "'") + ", " +
                    "creator = " + ((creator == 0) ? "NULL" : "" + creator + "") + ", " +
                    "expirationdate = " + ((expirationDate == null) ? "NULL" : "?") + ", " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + " " +
                    "WHERE id = " + uID;
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                if (expirationDate != null) {
                    pstmt.setDate(1, expirationDate);
                }
                rows = pstmt.executeUpdate();
            }
            isNew = false;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UStructure save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine);
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
    * Remove the current UStructure from the DB
    */
    public void drop() {
        DbConn myConn = new DbConn(SCHEMA);
        drop(myConn, 0);
        myConn.release();
    }

    /**
    * Remove the current UStructure from the DB
    */
    public abstract void drop(DbConn myConn, long userID);

    /**
    * Remove the current UStructure from the DB
    */
    public void usDrop(DbConn myConn, long userID) {
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
                "DELETE FROM " + SCHEMAPATH + ".ustructure WHERE id = " + uID
                );
            rows = pstmt.executeUpdate();

            myUID.setStatus("removed");
            myUID.save(conn);

            uID = 0;
            description = null;
            creator = 0;
            creationDate = null;
            expirationDate = null;
            status = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - UStructure drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine);
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


}
