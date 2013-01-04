package gov.ymp.csi.items;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
import javax.naming.*;

/**
* FilesItem is the class for generic file items with optional dates and version
*
* @author   Bill Atchley
*/
public class FilesItem {
    private long uID = 0;
    private int version = 1;
    private java.sql.Date date1 = null;
    private java.sql.Date date2 = null;
    private String name = null;
    private String extension = "noexten";
    private String description = null;
    public String status = null;  // approved, inreview, pending
    public long owner = 0;
    public UNID myUID = null;
    private String mimeType = null;
    public boolean isNew = true;
    private ByteArrayOutputStream fileOutStream = null;

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
    * Creates a new empty FilesItem object
    */
    public FilesItem () {
        uID = 0;
        version = 1;
        init();
    }


    /**
    * Creates a FilesItem object and uses the given id to populate it from the db
    *
    * @param id     The id of the FilesItem to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public FilesItem (long id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Creates a FilesItem object and uses the given UNID to populate it from the db
    *
    * @param uid     The UNID of the FilesItem to lookup from the db (long)
    * @param myConn  Connection to the database
    */
    public FilesItem (UNID uid, DbConn myConn) {
        init();
        lookup(uid.getID(), myConn);
    }



    /**
    * Retrieves a FilesItem from the db and stores it in the current FilesItem object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void getInfo (long id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a FilesItem from the db and stores it in the current FilesItem object
    *
    * @param id     The id of the UNID to lookup from the db (long)
    * @param myConn Connection to the database
    */
    public void lookup (long id, DbConn myConn) {
        uID = 0;
        version = 0;
        date1 = null;
        date2 = null;
        name = null;
        extension = null;
        status = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            myUID = new UNID(id, myConn);
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT fi.id, fi.version, fi.date1, fi.date2, fi.name, SUBSTR(fi.name,INSTR(fi.name,'.',LENGTH(fi.name)-4,1)+1), " +
                  "fi.status, fi.owner, fi.description, NVL(mt.mimetype,'noExtenOrMymeType') " +
                  "FROM " + SCHEMAPATH + ".file_items fi, " + SCHEMAPATH + ".mimetypes mt WHERE fi.id=" + id + " " +
                  " AND SUBSTR(fi.name,INSTR(fi.name,'.',LENGTH(fi.name)-4,1)+1)=mt.filetype (+)";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            long myID = rs.getLong(1);
            int myVersion = rs.getInt(2);
            java.sql.Date myDate1 = rs.getDate(3);
            java.sql.Date myDate2 = rs.getDate(4);
            String myName = rs.getString(5);
            String myExtension = rs.getString(6);
            String myStatus = rs.getString(7);
            long myOwner = rs.getLong(8);
            String myDescription = rs.getString(9);
            if (myID == id) {
                uID = myID;
                version = myVersion;
                date1 = myDate1;
                date2 = myDate2;
                name = myName;
                extension = myExtension;
                status = myStatus;
                owner = myOwner;
                isNew = false;
                description = myDescription;
                mimeType = rs.getString(10);

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieve the UNID id for the current FilesItem
    *
    * @return id    The UNID id for the current FilesItem
    */
    public long getID() {
        long id = uID;
        return(id);
    }

    /**
    * Retrieve the UNID for the current FilesItem
    *
    * @return myUNID    The UNID id for the current FilesItem
    */
    public UNID getUNID() {
        UNID myUNID = myUID;
        return(myUNID);
    }

    /**
    * Retrieve the version for the current FilesItem
    *
    * @return ver    The version for the current FilesItem
    */
    public int getVersion() {
        int ver = version;
        return(ver);
    }


    /**
    * Retrieve the date1 for the current FilesItem as a java.sql.Date
    *
    * @return date1    The creation date for the current FilesItem
    */
    public java.sql.Date getDate1SQL() {
        return(date1);
    }


    /**
    * Retrieve the date1 for the current FilesItem
    *
    * @return date1    The creation date for the current FilesItem
    */
    public java.util.Date getDate1() {
        java.util.Date tmpDate = ((date1!=null) ? new java.util.Date(date1.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the date2 for the current FilesItem as a java.sql.Date
    *
    * @return date2    The creation date for the current FilesItem
    */
    public java.sql.Date getDate2SQL() {
        return(date2);
    }


    /**
    * Retrieve the date2 for the current FilesItem
    *
    * @return date2    The creation date for the current FilesItem
    */
    public java.util.Date getDate2() {
        java.util.Date tmpDate = ((date2!=null) ? new java.util.Date(date2.getTime()) : null);
        return(tmpDate);
    }


    /**
    * Retrieve the name for the current FilesItem
    *
    * @return name    The name for the current FilesItem
    */
    public String getName() {
        if (name != null && name.indexOf("<isLob>") > -1) {
            return(description);
        } else {
            return(name);
        }
    }


    /**
    * Retrieve the description from the current FilesItem
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (int)
    */
    public String getDescription() {
        String desc = description;
        return(desc);
    }


    /**
    * Retrieve the first (maxLen) characters of description from the current FilesItem
    *
    * @param maxLen   The max number of characters to retrieve from the front of the string (int)
    * @return desc    The description for the current FilesItem
    */
    public String getDescriptionBrief(int maxLen) {
        String desc = description;
        if (desc.length() > maxLen) {
            desc = desc.substring(0, maxLen);
        }
        return(desc);
    }


    /**
    * Retrieve the extension for the current FilesItem
    *
    * @return extension    The extension for the current FilesItem
    */
    public String getExtension() {
        return(extension);
    }


    /**
    * Retrieve the status for the current FilesItem
    *
    * @return status    The status for the current FilesItem
    */
    public String getStatus() {
        return(status);
    }


    /**
    * Retrieve the UNID id for the owner of the current FilesItem
    *
    * @return id    The UNID id for the owner of the current FilesItem
    */
    public long getOwner() {
        long id = owner;
        return(id);
    }


    /**
    * Retrieve the mime type of the current FilesItem
    *
    * @return type    The mime type of the current FilesItem
    */
    public String getMimeType() {
        String type = mimeType;
        return(type);
    }


    /**
    * Retrieve an output stream for the current FilesItem
    *
    * @param myConn     Connection to the database
    *
    * @return out    The output stream for the current FilesItem
    */
    public ByteArrayOutputStream getFileOutStream() {
        DbConn myConn = new DbConn("csi");
        ByteArrayOutputStream out = getFileOutStream(myConn);
        myConn.release();

        return(out);
    }


    /**
    * Retrieve an output stream for the current FilesItem
    *
    * @param myConn     Connection to the database
    *
    * @return out    The output stream for the current FilesItem
    */
    public ByteArrayOutputStream getFileOutStream(DbConn myConn) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Context initCtx = new InitialContext();
            String PathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
            String fileWithPath = PathToRoot + "/docstore/" + uID + "-" + version + "." + extension;
//System.out.println("getFileOutStream - uID:" + uID + ", Ver:" + version + ", ext:" + extension + ", fileWithPath:" + fileWithPath);
            File myFile = new File(fileWithPath);
            InputStream is = new FileInputStream(myFile);
            byte[] bytes = new byte[1024];

            int readLen = is.read(bytes);
            while (readLen > -1) {
                out.write(bytes, 0, readLen);
                readLen = is.read(bytes);
            }

            is.close();
        }

        catch (Exception e) {
            String outLine = "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem ByteArrayOutputStream");
        }


        return(out);
    }


    /**
    * Retrieve an output byte array for the current FilesItem
    *
    * @param myConn     Connection to the database
    *
    * @return bytes    The output byte array for the current FilesItem
    */
    public byte[] getFileByteArray(DbConn myConn) {
        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte [] bytes = null;
        try {
            Context initCtx = new InitialContext();
            String PathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
            String fileWithPath = PathToRoot + "/docstore/" + uID + "-" + version + "." + extension;
//System.out.println("getFileByteArray - uID:" + uID + ", Ver:" + version + ", ext:" + extension + ", fileWithPath:" + fileWithPath);
            File myFile = new File(fileWithPath);
            InputStream is = new FileInputStream(myFile);
            int fileLen = (int) myFile.length();
            bytes = new byte[fileLen];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }


            if (offset < bytes.length) {
                throw new IOException("could not completely read file ");
            }

            is.close();
        }

        catch (Exception e) {
            String outLine = "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem getFileByteArray");
        }


        return(bytes);
    }


    /**
    * Set the version for the current FilesItem
    *
    * @param ver     The new version of the FilesItem (int)
    */
    public void setVersion(int ver) {
        version = ver;
    }


    /**
    * Set the date1 date for the current FilesItem
    *
    * @param inDate     The new Date1 date of the FilesItem (java.sql.Date)
    */
    public void setDate1(java.sql.Date inDate) {
        date1 = inDate;
    }

    /**
    * Set the date1 date for the current FilesItem
    *
    * @param inDate     The new Date1 date of the FilesItem (java.util.Date)
    */
    public void setDate1(java.util.Date inDate) {
        java.sql.Date tmpDate = new java.sql.Date(inDate.getTime());
        date1 = tmpDate;
    }

    /**
    * Set the date2 date for the current FilesItem
    *
    * @param inDate     The new Date2 date of the FilesItem (java.sql.Date)
    */
    public void setDate2(java.sql.Date inDate) {
        date2 = inDate;
    }

    /**
    * Set the date2 date for the current FilesItem
    *
    * @param inDate     The new Date2 date of the FilesItem (java.util.Date)
    */
    public void setDate2(java.util.Date inDate) {
        java.sql.Date tmpDate = new java.sql.Date(inDate.getTime());
        date2 = tmpDate;
    }


    /**
    * Set the name for the current FilesItem
    *
    * @param txt     The new name of the FilesItem (String)
    */
    public void setName(String txt) {
        name = txt;
    }


    /**
    * Set the description for the current FilesItem
    *
    * @param txt     The new description of the FilesItem (String)
    */
    public void setDescription(String txt) {
        description = txt;
    }


    /**
    * Set the status for the current FilesItem
    *
    * @param stat     The new status of the FilesItem (String)
    */
    public void setStatus(String stat) {
        status = stat;
    }


    /**
    * Set the owner for the current FilesItem
    *
    * @param own     The new owner of the FilesItem (long)
    */
    public void setOwner(long own) {
        owner = own;
    }


    /**
    * Set the file output stream FilesItem
    *
    * @param buff     The output stream for the file
    */
    public void setFileOutStream(ByteArrayOutputStream buff) {
        fileOutStream = buff;
    }


    /**
    * Save the current FilesItem to the DB
    *
    * @param myConn     Connection to the database
    */
    public void save(DbConn myConn) {
        save(myConn, 0);
    }


    /**
    * Save the current FilesItem to the DB
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
        CLOB fileLobPtr = null;
        Writer output = null;
        try {

            Connection conn = myConn.conn;


            // Create a Statement
            // stmt = conn.createStatement ();
            conn.setAutoCommit(false);
            if (isNew) {
                myUID = new UNID();
                myUID.create("fileitem", myConn);
                myUID.save(conn);
                uID = myUID.getID();

                sqlcode = "begin INSERT INTO " + SCHEMAPATH + ".file_items (id, version, date1, date2, name, status, owner, description) " +
                    "VALUES (" + uID + ", " + version + ", " +
                    ((date1 == null) ? "NULL" : "?") + ", " +
                    ((date2 == null) ? "NULL" : "?") + ", " +
                    ((name == null || name == "n/a") ? "NULL" : "?") + ", " +
                    ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ", " +
                    owner + "," +
                    ((description == null || description == "n/a") ? "NULL" : "?") +
                    "); end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (date1 != null) {
                    pstmt.setDate(++sqlID, date1);
                }
                if (date2 != null) {
                    pstmt.setDate(++sqlID, date2);
                }
                if (name != null && name != "n/a") {
                    pstmt.setString(++sqlID, name);
                }
                if (description != null && description != "n/a") {
                    pstmt.setString(++sqlID, description);
                }
                rows = pstmt.executeUpdate();
// do auth stuff
            } else {
                myUID.save(conn);
                sqlcode = "begin UPDATE " + SCHEMAPATH + ".file_items SET " +
                    "version = " + version + ", " +
                    "date1 = " + ((date1 == null) ? "NULL" : "?") + ", " +
                    "date2 = " + ((date2 == null) ? "NULL" : "?") + ", " +
                    "name = " + ((name == null || name == "n/a") ? "NULL" : "?") + ", " +
                    "owner = " + owner + ", " +
                    "status = " + ((status == null || status == "n/a") ? "NULL" : "'" + status + "'") + ", " +
                    "description = " + ((description == null || description == "n/a") ? "NULL" : "?") + " " +
                    "WHERE id = " + uID + "; end;";
//System.out.println(sqlcode);
                pstmt = conn.prepareStatement(sqlcode);
                int sqlID = 0;
                if (date1 != null) {
                    pstmt.setDate(++sqlID, date1);
                }
                if (date2 != null) {
                    pstmt.setDate(++sqlID, date2);
                }
                if (name != null && name != "n/a") {
                    pstmt.setString(++sqlID, name);
                }
                if (description != null && description != "n/a") {
                    pstmt.setString(++sqlID, description);
                }
                rows = pstmt.executeUpdate();
            }
            isNew = false;
            conn.commit();
            if (fileOutStream != null) {
                Context initCtx = new InitialContext();
                String PathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
                String fileWithPath = PathToRoot + "/docstore/" + uID + "-" + version + "." + extension;
                FileOutputStream outFile = new FileOutputStream(fileWithPath);
                fileOutStream.writeTo(outFile);
                outFile.close();
            }
        }

        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - FilesItem save");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - FilesItem save");
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
    * Remove the current FilesItem from the DB
    *
    * @param myConn     Connection to the database
    */
    public void drop(DbConn myConn) {
        drop(myConn, 0);
    }


    /**
    * Remove the current FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param userID     The id of the ov the user performing the action (long)
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
                "DELETE FROM " + SCHEMAPATH + ".file_items WHERE id = " + uID + " AND version=" + version
                );
            rows = pstmt.executeUpdate();

            Context initCtx = new InitialContext();
            String PathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
            String fileWithPath = PathToRoot + "/docstore/" + uID + "-" + version + "." + extension;
            File fi = new File(fileWithPath);
            fi.delete();

            myUID.setStatus("removed");
            myUID.save(conn);

            uID = 0;
            version = 0;
            date1 = null;
            date2 = null;
            name = null;
            extension = null;
            description = null;
            status = null;
            isNew = true;
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - FilesItem drop");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, userID,"N/A",0, outLine + " - FilesItem drop");
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
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A USet of items to be fetched from the db
    */
    public static FilesItem[] getItemList(DbConn myConn, USet list) {
        return getItemList(myConn, list, "none");
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A USet of items to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static FilesItem[] getItemList(DbConn myConn, USet list, String sort) {
        long[] myList = list.getItemsArray();
        return getItemList(myConn, (long[]) myList, sort, "subset", 0);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static FilesItem[] getItemList(DbConn myConn, String sort) {
        return getItemList(myConn, (long[]) null, sort, "all", 0);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param own        A long UNID id of the owner of items to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static FilesItem[] getItemList(DbConn myConn, long own, String sort) {
        return getItemList(myConn, (long[]) null, sort, "all", own);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param own        A long UNID id of the owner of items to be fetched from the db
    */
    public static FilesItem[] getItemList(DbConn myConn, long own) {
        return getItemList(myConn, (long[]) null, "none", "all", own);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    */
    public static FilesItem[] getItemList(DbConn myConn, long[] list) {
        return getItemList(myConn, list, "none", "subset", 0);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    */
    public static FilesItem[] getItemList(DbConn myConn, long[] list, String sort) {
        return getItemList(myConn, list, sort, "subset", 0);
    }


    /**
    * Get a list of FilesItem from the DB
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    * @param sort       A string defining the sort to be put on the returned values ('none' will do no sort)
    * @param flag       A string defining if all records are returned or just a subset
    * @param own        A long UNID id of the owner of items to be fetched from the db
    */
    public static FilesItem[] getItemList(DbConn myConn, long[] list, String sort, String flag, long own) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        FilesItem[] fi = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            //sqlFrom = " FROM " + SCHEMAPATH + ".file_items fi ";
            sqlFrom = " FROM " + myConn.getSchemaPath() + ".file_items fi, " + myConn.getSchemaPath() + ".mimetypes mt ";
            sqlWhere = "WHERE SUBSTR(fi.name,INSTR(fi.name,'.',LENGTH(fi.name)-4,1)+1)=mt.filetype (+) ";
            if (flag.equals("subset")) {
                sqlWhere += " AND fi.id IN (";
                for (int i = 0; i<list.length; i++) {
                    sqlWhere += (i!=0) ? ", " : "";
                    sqlWhere += list[i];
                }
                sqlWhere += ")";
            }
            if (own != 0) {
                sqlWhere += " AND fi.owner=" + own;
            }

            sqlOrderBy = (!sort.equals("none")) ? " ORDER BY " + sort + " " : "";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            fi = new FilesItem[returnSize];

//            sqlcode = "SELECT fi.id, fi.version, fi.date1, fi.date2, fi.name, SUBSTR(fi.name,INSTR(fi.name,'.',LENGTH(fi.name)-4,1)+1), fi.status, fi.owner,  " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
            sqlcode = "SELECT fi.id, fi.version, fi.date1, fi.date2, fi.name, SUBSTR(fi.name,INSTR(fi.name,'.',LENGTH(fi.name)-4,1)+1), fi.status, fi.owner, fi.description, NVL(mt.mimetype,'noExtenOrMymeType') " + sqlFrom + sqlWhere + " " + sqlOrderBy + " ";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                fi[i] = new FilesItem();
                fi[i].uID = rs.getLong(1);
                fi[i].version = rs.getInt(2);
                fi[i].date1 = rs.getDate(3);
                fi[i].date2 = rs.getDate(4);
                fi[i].name = rs.getString(5);
                fi[i].extension = rs.getString(6);
                fi[i].status = rs.getString(7);
                fi[i].owner = rs.getLong(8);
                fi[i].description = rs.getString(9);
                fi[i].myUID = new UNID(fi[i].uID, myConn);
                fi[i].isNew = false;
                fi[i].mimeType = rs.getString(10);
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - FilesItem lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return fi;
    }


    /**
    * Get a list of FilesItem from the DB as a hashmap
    *
    * @param myConn     Connection to the database
    * @param list       A long array of UNID id's to be fetched from the db
    */
    public static HashMap getItemHashMap(DbConn myConn, long[] list) {
        FilesItem[] fi = getItemList(myConn, (long[]) list, "none");
        HashMap hm = new HashMap(fi.length);
        for (int i=0; i<fi.length; i++) {
            hm.put(Long.toString(fi[i].getID()), (Object) fi[i]);
        }

        return hm;
    }


    /**
    * Get a mime type based on a file extension
    *
    * @param myConn     Connection to the database
    * @param txt        Mime type to look up from the db
    */
    public static String lookupMimeType(DbConn myConn, String txt) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        String sqlcode = "";
        String mt = "none";
        try {
            Connection conn = myConn.conn;
            stmt = conn.createStatement ();

            sqlcode = "SELECT mimetype FROM " + myConn.getSchemaPath() + ".mimetypes WHERE filetype='" + txt + "'";
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            mt = rs.getString(1);
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - MimeType lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - MimeType lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return mt;
    }




}
