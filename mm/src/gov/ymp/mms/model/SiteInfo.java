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
* SiteInfo is the class for siteinfo in the db
*
* @author   Bill Atchley
*/
public class SiteInfo {
    public int ID = 0;
    public String name = null;
    public String shortName = null;
    public String longName = null;
    public String companyCode = null;
    public String organization = null;
    public String address = null;
    public String city = null;
    public String state = null;
    public String zip = null;
    public String dAddress = null;
    public String dCity = null;
    public String dState = null;
    public String dZip = null;
    public String phone = null;
    public String fax = null;
    public String contractNumber = null;
    public double salesTax = 0.0;
    public boolean TaxExempt = false;
    public String siteCode = null;
    public boolean trackFunding = false;

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
    * Creates a new empty SiteInfo object
    */
    public SiteInfo () {
        ID = 0;
        init();
    }


    /**
    * Creates a SiteInfo object and uses the given id to populate it from the db
    *
    * @param id     The id of the SiteInfo to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public SiteInfo (int id, DbConn myConn) {
        init();
        lookup(id, myConn);
    }


    /**
    * Retrieves a SiteInfo from the db and stores it in the current SiteInfo object
    *
    * @param id     The id  to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void getInfo (int id, DbConn myConn) {
        lookup(id, myConn);
    }


    /**
    * Retrieves a SiteInfo from the db and stores it in the current SiteInfo object
    *
    * @param id     The id to lookup from the db (int)
    * @param myConn Connection to the database
    */
    public void lookup (int id, DbConn myConn) {
        ID = 0;
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT id, name, shortname, longname, companycode, organization, address, city, state, zip, daddress, dcity, dstate, dzip, " +
                             "phone, fax, contractnumber, salestax, taxexempt, sitecode, trackfunding ";
            sqlcode += " FROM " + SCHEMAPATH + ".site_info WHERE id=" + id;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            int myID = rs.getInt(1);
            if (myID == id) {
                ID = myID;
                name = rs.getString(2);
                shortName = rs.getString(3);
                longName = rs.getString(4);
                companyCode = rs.getString(5);
                organization = rs.getString(6);
                address = rs.getString(7);
                city = rs.getString(8);
                state = rs.getString(9);
                zip = rs.getString(10);
                dAddress = rs.getString(11);
                dCity = rs.getString(12);
                dState = rs.getString(13);
                dZip = rs.getString(14);
                phone = rs.getString(15);
                fax = rs.getString(16);
                contractNumber = rs.getString(17);
                salesTax = rs.getDouble(18);
                TaxExempt = ((rs.getString(19).equals("T")) ? true : false);
                siteCode = rs.getString(20);
                trackFunding = ((rs.getString(21).equals("T")) ? true : false);

                isNew = false;

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SiteInfo lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - SiteInfo lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Get a list of SiteInfo from the DB
    *
    * @param myConn       Connection to the database
    */
    public static SiteInfo[] getItemList(DbConn myConn) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        SiteInfo[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".site_info ";
            sqlWhere = " WHERE 1=1 ";
            sqlOrderBy = " ORDER BY name,id ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new SiteInfo[returnSize];

            sqlcode = "SELECT id, name, shortname, longname, companycode, organization, address, city, state, zip, daddress, dcity, dstate, dzip, " +
                             "phone, fax, contractnumber, salestax, taxexempt, sitecode, trackfunding " + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
            while (rs.next()) {
                item[i] = new SiteInfo();
                item[i].ID = rs.getInt(1);
                item[i].name = rs.getString(2);
                item[i].shortName = rs.getString(3);
                item[i].longName = rs.getString(4);
                item[i].companyCode = rs.getString(5);
                item[i].organization = rs.getString(6);
                item[i].address = rs.getString(7);
                item[i].city = rs.getString(8);
                item[i].state = rs.getString(9);
                item[i].zip = rs.getString(10);
                item[i].dAddress = rs.getString(11);
                item[i].dCity = rs.getString(12);
                item[i].dState = rs.getString(13);
                item[i].dZip = rs.getString(14);
                item[i].phone = rs.getString(15);
                item[i].fax = rs.getString(16);
                item[i].contractNumber = rs.getString(17);
                item[i].salesTax = rs.getDouble(18);
                item[i].TaxExempt = ((rs.getString(19).equals("T")) ? true : false);
                item[i].siteCode = rs.getString(20);
                item[i].trackFunding = ((rs.getString(21).equals("T")) ? true : false);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SiteInfo - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - SiteInfo - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isTaxExempt() {
        return TaxExempt;
    }

    public void setTaxExempt(boolean TaxExempt) {
        this.TaxExempt = TaxExempt;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getdAddress() {
        return dAddress;
    }

    public void setdAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public String getdCity() {
        return dCity;
    }

    public void setdCity(String dCity) {
        this.dCity = dCity;
    }

    public String getdState() {
        return dState;
    }

    public void setdState(String dState) {
        this.dState = dState;
    }

    public String getdZip() {
        return dZip;
    }

    public void setdZip(String dZip) {
        this.dZip = dZip;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(double salesTax) {
        this.salesTax = salesTax;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isTrackFunding() {
        return trackFunding;
    }

    public void setTrackFunding(boolean trackFunding) {
        this.trackFunding = trackFunding;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }




}
