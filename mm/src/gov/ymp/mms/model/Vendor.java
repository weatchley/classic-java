package gov.ymp.mms.model;

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
* Vendor is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class Vendor {
    public int ID = 0;
    public String abrv = null;
    public String name = null;
    public String address = null;
    public String city = null;
    public String state = null;
    public String zip = null;
    public String country = null;
    public String remitAddress = null;
    public String remitCity = null;
    public String remitState = null;
    public String remitZip = null;
    public String remitCountry = null;
    public String pointOfContact = null;
    public String phone = null;
    public String extension = null;
    public String fax = null;
    public String email = null;
    public String url = null;
    public String mainPhone = null;
    public String mainProduct = null;
    public String lastPO = null;
    public java.util.Date lastUsed = null;
    public int status = 0;
    public int relatedTo = 0;
    public String relationship = null;
    public java.util.Date dateCreated = null;
    public boolean [] hasTaxID = null;
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
        SCHEMA = "mms";
        SCHEMAPATH = "mms";
        DBTYPE = tempDB.getDBType();
    }


    /**
    * Creates a new empty Vendor object
    */
    public Vendor () {
        ID = 0;
        init();
    }


    /**
    * Creates an Vendor object and uses the given id to populate it from the db
    *
    * @param id     The id of the Vendor to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public Vendor (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a Vendor from the db and stores it in the current Vendor object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a Vendor from the db and stores it in the current Vendor object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        name = null;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;
        try {

            int maxSites = 21;
            hasTaxID = new boolean [maxSites];
            for (int i=0; i<maxSites; i++) {
                hasTaxID[i] = false;
            }

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            stmt2 = conn.createStatement ();

            String sqlcode = "SELECT id, abrv, name, address, city, state, zip, country, remitaddress, remitcity, remitstate, remitzip, remitcountry, ";
            sqlcode += "pointofcontact, phone, extension, fax, email, url, mainphone, mainproduct, lastpo, lastused, ";
            sqlcode += "status, relatedto, relationship, datecreated FROM " + SCHEMAPATH + ".vendors WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                abrv = rs.getString(2);
                name = rs.getString(3);
                address = rs.getString(4);
                city = rs.getString(5);
                state = rs.getString(6);
                zip = rs.getString(7);
                country = rs.getString(8);
                remitAddress = rs.getString(9);
                remitCity = rs.getString(10);
                remitState = rs.getString(11);
                remitZip = rs.getString(12);
                remitCountry = rs.getString(13);
                pointOfContact = rs.getString(14);
                phone = rs.getString(15);
                extension = rs.getString(16);
                fax = rs.getString(17);
                email = rs.getString(18);
                url = rs.getString(19);
                mainPhone = rs.getString(20);
                mainProduct = rs.getString(21);
                lastPO = rs.getString(22);
                lastUsed = rs.getTimestamp(23);
                status = rs.getInt(24);
                relatedTo = rs.getInt(25);
                relationship = rs.getString(26);
                dateCreated = rs.getTimestamp(27);
                isNew = false;
                //

                String sqlcode2 = "SELECT vendor, site, hastaxid, taxid, effectivedate FROM " + SCHEMAPATH + ".vendor_tax_permit WHERE vendor=" + ID;
//System.out.println(sqlcode2);
                rs2 = stmt2.executeQuery(sqlcode2);
                while(rs2.next()) {
//System.out.println("Vendor.java - Got here 1");
                    int vid = rs2.getInt(1);
                    if (ID == vid) {
                        hasTaxID[rs2.getInt(2)] = ((rs2.getString(3).equals("T")) ? true : false);
                    }
                }

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - Vendor lookup");
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
            if (rs != null)
                try { rs2.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt2.close(); } catch (Exception i) {}
        }
    }

    public String getAbrv() {
        return abrv;
    }

    public void setAbrv(String abrv) {
        this.abrv = abrv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public java.util.Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean[] getHasTaxID() {
        return hasTaxID;
    }

    public void setHasTaxID(boolean[] hasTaxID) {
        this.hasTaxID = hasTaxID;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getLastPO() {
        return lastPO;
    }

    public void setLastPO(String lastPO) {
        this.lastPO = lastPO;
    }

    public java.util.Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(java.util.Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPointOfContact() {
        return pointOfContact;
    }

    public void setPointOfContact(String pointOfContact) {
        this.pointOfContact = pointOfContact;
    }

    public int getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(int relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRemitAddress() {
        return remitAddress;
    }

    public void setRemitAddress(String remitAddress) {
        this.remitAddress = remitAddress;
    }

    public String getRemitCity() {
        return remitCity;
    }

    public void setRemitCity(String remitCity) {
        this.remitCity = remitCity;
    }

    public String getRemitCountry() {
        return remitCountry;
    }

    public void setRemitCountry(String remitCountry) {
        this.remitCountry = remitCountry;
    }

    public String getRemitState() {
        return remitState;
    }

    public void setRemitState(String remitState) {
        this.remitState = remitState;
    }

    public String getRemitZip() {
        return remitZip;
    }

    public void setRemitZip(String remitZip) {
        this.remitZip = remitZip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }


    public int getID() {
        return ID;
    }


    /**
    * Get a list of Vendor from the DB
    *
    * @param myConn       Connection to the database
    * @param pr           purchase document to get vendor list for
    */
    public static Vendor[] getItemList(DbConn myConn, String pr) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        Vendor[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".vendors ";
            sqlWhere = " WHERE 1=1 ";
            sqlWhere += (pr != "0" && pr != null) ? " AND id IN (SELECT vendor FROM " + mySchemaPath + ".vendor_list WHERE prnumber='" + pr + "')" : "";
            sqlOrderBy = " ORDER BY name ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new Vendor[returnSize];

            sqlcode = "SELECT id, abrv, name, address, city, state, zip, country, remitaddress, remitcity, remitstate, remitzip, remitcountry, ";
            sqlcode += "pointofcontact, phone, extension, fax, email, url, mainphone, mainproduct, lastpo, lastused, ";
            sqlcode += "status, relatedto, relationship, datecreated" + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
//System.out.println("Vendor-getItemList - Got Here 1");
            while (rs.next()) {
                item[i] = new Vendor();
                item[i].ID = rs.getInt(1);
                item[i].abrv = rs.getString(2);
                item[i].name = rs.getString(3);
                item[i].address = rs.getString(4);
                item[i].city = rs.getString(5);
                item[i].state = rs.getString(6);
                item[i].zip = rs.getString(7);
                item[i].country = rs.getString(8);
                item[i].remitAddress = rs.getString(9);
                item[i].remitCity = rs.getString(10);
                item[i].remitState = rs.getString(11);
                item[i].remitZip = rs.getString(12);
                item[i].remitCountry = rs.getString(13);
                item[i].pointOfContact = rs.getString(14);
                item[i].phone = rs.getString(15);
                item[i].extension = rs.getString(16);
                item[i].fax = rs.getString(17);
                item[i].email = rs.getString(18);
                item[i].url = rs.getString(19);
                item[i].mainPhone = rs.getString(20);
                item[i].mainProduct = rs.getString(21);
                item[i].lastPO = rs.getString(22);
                item[i].lastUsed = rs.getTimestamp(23);
                item[i].status = rs.getInt(24);
                item[i].relatedTo = rs.getInt(25);
                item[i].relationship = rs.getString(26);
                item[i].dateCreated = rs.getTimestamp(27);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Vendor - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - Vendor - getItemList");
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
