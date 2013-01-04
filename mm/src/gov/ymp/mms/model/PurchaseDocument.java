package gov.ymp.mms.model;

// Support classes
import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import gov.ymp.csi.misc.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

/**
* PurchaseDocument is the class for archived reports in the db
*
* @author   Bill Atchley
*/
public class PurchaseDocument {
    public String prnumber = null;
    public String ponumber = null;
    public String amendment = null;
    public String priority = null;
    public java.util.Date dueDate = null;
    public int author = 0;
    public int requester = 0;
    public String briefDescription = null;
    public String justification = null;
    public String soleSource = null;
    public String ssJustification = null;
    public int status = 0;
    public java.util.Date dateRequested = null;
    public java.util.Date dateRequired = null;
    public int deptID = 0;
    public String chargeNumber = null;
    public int contractType = 0;
    public String taxExempt = null;
    public double tax = 0.0;
    public double shipping = 0.0;
    public int poType = 0;
    public int fob = 0;
    public String shipVia = null;
    public String paymentTerms = null;
    public int buyer = 0;
    public int amendedBy = 0;
    public int awardType = 0;
    public int vendorID = 0;
    public Vendor vendor = null;
    public int rfpDeliveryDays = 0;
    public int rfpDaysValid = 0;
    public java.util.Date rfpDueDate = null;
    public String relatedPR = null;
    public PurchaseDocument relatedPRInfo = null;
    public java.util.Date prDate = null;
    public java.util.Date poDate = null;
    public String bidRemarks = null;
    public String selectionMemo = null;
    public String enclosures = null;
    public java.util.Date startDate = null;
    public java.util.Date endDate = null;
    public int creditCardHolder = 0;
    public double pdTotal = 0.0;
    public String refnumber = null;
    public PDItem [] items = null;
    public boolean isNew = true;
    public boolean isHistory = false;
    public java.util.Date changeDate = null;
    public double shippingChange = 0.0;
    public double taxChange = 0.0;
    public String changes = null;
    public int changedBy = 0;

    public int site = 0;

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
    * Creates a new empty PurchaseDocument object
    */
    public PurchaseDocument () {
        init();
    }


    /**
    * Creates an PurchaseDocument object and uses the given id to populate it from the db
    *
    * @param pr     The id of the PurchaseDocument to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public PurchaseDocument (String pr, DbConn myConn) {
        init();
        lookup(pr, myConn, false, 0);
    }


    /**
    * Creates an PurchaseDocument object and uses the given id to populate it from the db
    *
    * @param pr     The id of the PurchaseDocument to lookup from the db (String)
    * @param myConn Connection to the database
    * @param hist   History flag (boolean)
    * @param stat   History status to lookup
    */
    public PurchaseDocument (String pr, DbConn myConn, boolean hist, int stat) {
        init();
        lookup(pr, myConn, hist, stat);
    }


    /**
    * Retrieves a PurchaseDocument from the db and stores it in the current PurchaseDocument object
    *
    * @param pr     The id  to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void getInfo (String pr, DbConn myConn) {
        lookup(pr, myConn, false, 0);
    }


    /**
    * Retrieves a PurchaseDocument from the db and stores it in the current PurchaseDocument object
    *
    * @param pr     The id  to lookup from the db (String)
    * @param myConn Connection to the database
    * @param hist   History flag (boolean)
    * @param stat   History status to lookup
    */
    public void getInfo (String pr, DbConn myConn, boolean hist, int stat) {
        lookup(pr, myConn, hist, stat);
    }


    /**
    * Retrieves a PurchaseDocument from the db and stores it in the current PurchaseDocument object
    *
    * @param pr     The id to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public void lookup (String pr, DbConn myConn) {
        lookup(pr, myConn, false, 0);
    }


    /**
    * Retrieves a PurchaseDocument from the db and stores it in the current PurchaseDocument object
    *
    * @param pr     The id to lookup from the db (String)
    * @param myConn Connection to the database
    * @param hist   History flag (boolean)
    * @param stat   History status to lookup
    */
    public void lookup (String pr, DbConn myConn, boolean hist, int stat) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT prnumber, ponumber, amendment, priority, duedate, author, requester, briefdescription, ";
            sqlcode += "justification, solesource, ssjustification, status, daterequested, daterequired, deptid, ";
            sqlcode += "chargenumber, contracttype, taxexempt, tax, shipping, potype, fob, shipvia, paymentterms, ";
            sqlcode += "buyer, amendedby, awardtype, vendor, rfpdeliverdays, rfpdaysvalid, rfpduedate, relatedpr, ";
            sqlcode += "prdate, podate, bidremarks, selectionmemo, enclosures, startdate, enddate, creditcardholder, pdtotal, refnumber ";
            if (hist) {
                sqlcode += ", changedate, shippingchange, taxchange, changes, changedby ";
            }
            sqlcode += "FROM " + SCHEMAPATH + "." + ((!hist) ? "purchase_documents" : "pd_history" ) + " WHERE prnumber='" + pr + "'";
            if (hist) {
                sqlcode += " AND status=" + stat;
            }

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            String myPR = rs.getString(1);
            if (myPR.equals(pr)) {
                prnumber = myPR;
                ponumber = rs.getString(2);
                amendment = rs.getString(3);
                priority = rs.getString(4);
                dueDate = rs.getTimestamp(5);
                author = rs.getInt(6);
                requester = rs.getInt(7);
                briefDescription = rs.getString(8);
                justification = rs.getString(9);
                soleSource = rs.getString(10);
                ssJustification = rs.getString(11);
                status = rs.getInt(12);
                dateRequested = rs.getTimestamp(13);
                dateRequired = rs.getTimestamp(14);
                deptID = rs.getInt(15);
                chargeNumber = rs.getString(16);
                contractType = rs.getInt(17);
                taxExempt = rs.getString(18);
                tax = rs.getDouble(19);
                shipping = rs.getDouble(20);
                poType = rs.getInt(21);
                fob = rs.getInt(22);
                shipVia = rs.getString(23);
                paymentTerms = rs.getString(24);
                buyer = rs.getInt(25);
                amendedBy = rs.getInt(26);
                awardType = rs.getInt(27);
                vendorID = rs.getInt(28);
                vendor = ((vendorID != 0) ? new Vendor(vendorID, myConn) : null);
                rfpDeliveryDays = rs.getInt(29);
                rfpDaysValid = rs.getInt(30);
                rfpDueDate = rs.getTimestamp(31);
                relatedPR = rs.getString(32);
                relatedPRInfo = ((relatedPR != null) ? new PurchaseDocument(relatedPR, myConn) : null);
                prDate = rs.getTimestamp(33);
                poDate = rs.getTimestamp(34);
                bidRemarks = rs.getString(35);
                selectionMemo = rs.getString(36);
                enclosures = rs.getString(37);
                startDate = rs.getTimestamp(38);
                endDate = rs.getTimestamp(39);
                creditCardHolder = rs.getInt(40);
                pdTotal = rs.getDouble(41);
                refnumber = rs.getString(42);
                if (hist) {
                    changeDate = rs.getTimestamp(43);
                    shippingChange = rs.getDouble(44);
                    taxChange = rs.getDouble(45);
                    changes = rs.getString(46);
                    changedBy = rs.getInt(47);
                    isHistory = true;
                }
                isNew = false;
                // get items
                if (hist) {
                    items = PDItem.getHistoryItemList(myConn, prnumber, changeDate);
                } else {
                    items = PDItem.getItemList(myConn, prnumber);
                }

                //rs.close();
                //stmt.close();
                sqlcode = "SELECT site FROM " + SCHEMAPATH + ".departments WHERE id=" + deptID;
//System.out.println(sqlcode);
                rs = stmt.executeQuery(sqlcode);
                rs.next();
                site = rs.getInt(1);
//System.out.println("PurchaseDocument.java - " + "Got Here 1 prnumber: " + prnumber + ", site: " + site);

            }
        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - PurchaseDocument lookup");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - PurchaseDocument lookup");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
    }


    /**
    * Retrieves a PR number from a given PO number
    *
    * @param po     The id to lookup from the db (String)
    * @param myConn Connection to the database
    */
    public static String lookupPRFromPO (String po, DbConn myConn) {
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        String myPR = null;
        String mySCHEMAPATH = "mms";
        try {

            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();

            String sqlcode = "SELECT prnumber ";
            sqlcode += "FROM " + mySCHEMAPATH + ".purchase_documents WHERE ponumber='" + po.toUpperCase() + "' OR ponumber='" + po.substring(0,(po.length()-1)).toUpperCase() + "'";

//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            myPR = rs.getString(1);

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - PurchaseDocument - lookupPRFromPO");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"N/A",0, outLine + " - PurchaseDocument - lookupPRFromPO");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }
        return myPR;
    }

    public int getAmendedBy() {
        return amendedBy;
    }

    public void setAmendedBy(int amendedBy) {
        this.amendedBy = amendedBy;
    }

    public String getAmendment() {
        return amendment;
    }

    public void setAmendment(String amendment) {
        this.amendment = amendment;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public String getBidRemarks() {
        return bidRemarks;
    }

    public void setBidRemarks(String bidRemarks) {
        this.bidRemarks = bidRemarks;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public int getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(String chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public int getContractType() {
        return contractType;
    }

    public void setContractType(int contractType) {
        this.contractType = contractType;
    }

    public int getCreditCardHolder() {
        return creditCardHolder;
    }

    public void setCreditCardHolder(int creditCardHolder) {
        this.creditCardHolder = creditCardHolder;
    }

    public java.util.Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(java.util.Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public java.util.Date getDateRequired() {
        return dateRequired;
    }

    public void setDateRequired(java.util.Date dateRequired) {
        this.dateRequired = dateRequired;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public java.util.Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(java.util.Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(String enclosures) {
        this.enclosures = enclosures;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public int getFob() {
        return fob;
    }

    public void setFob(int fob) {
        this.fob = fob;
    }

    public PDItem[] getItems() {
        return items;
    }

    public void setItems(PDItem[] items) {
        this.items = items;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public double getPdTotal() {
        return pdTotal;
    }

    public void setPdTotal(double pdTotal) {
        this.pdTotal = pdTotal;
    }

    public java.util.Date getPoDate() {
        return poDate;
    }

    public void setPoDate(java.util.Date poDate) {
        this.poDate = poDate;
    }

    public int getPoType() {
        return poType;
    }

    public void setPoType(int poType) {
        this.poType = poType;
    }

    public String getPonumber() {
        return ponumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public java.util.Date getPrDate() {
        return prDate;
    }

    public void setPrDate(java.util.Date prDate) {
        this.prDate = prDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPrnumber() {
        return prnumber;
    }

    public void setPrnumber(String prnumber) {
        this.prnumber = prnumber;
    }

    public String getRefnumber() {
        return refnumber;
    }

    public void setRefnumber(String refnumber) {
        this.refnumber = refnumber;
    }

    public String getRelatedPR() {
        return relatedPR;
    }

    public void setRelatedPR(String relatedPR) {
        this.relatedPR = relatedPR;
    }

    public PurchaseDocument getRelatedPRInfo() {
        return relatedPRInfo;
    }

    public int getRequester() {
        return requester;
    }

    public void setRequester(int requester) {
        this.requester = requester;
    }

    public int getRfpDaysValid() {
        return rfpDaysValid;
    }

    public void setRfpDaysValid(int rfpDaysValid) {
        this.rfpDaysValid = rfpDaysValid;
    }

    public int getRfpDeliveryDays() {
        return rfpDeliveryDays;
    }

    public void setRfpDeliveryDays(int rfpDeliveryDays) {
        this.rfpDeliveryDays = rfpDeliveryDays;
    }

    public java.util.Date getRfpDueDate() {
        return rfpDueDate;
    }

    public void setRfpDueDate(java.util.Date rfpDueDate) {
        this.rfpDueDate = rfpDueDate;
    }

    public String getSelectionMemo() {
        return selectionMemo;
    }

    public void setSelectionMemo(String selectionMemo) {
        this.selectionMemo = selectionMemo;
    }

    public String getShipVia() {
        return shipVia;
    }

    public void setShipVia(String shipVia) {
        this.shipVia = shipVia;
    }

    public double getShipping() {
        return shipping;
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public double getShippingChange() {
        return shippingChange;
    }

    public void setShippingChange(double shippingChange) {
        this.shippingChange = shippingChange;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getSoleSource() {
        return soleSource;
    }

    public void setSoleSource(String soleSource) {
        this.soleSource = soleSource;
    }

    public String getSsJustification() {
        return ssJustification;
    }

    public void setSsJustification(String ssJustification) {
        this.ssJustification = ssJustification;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTaxChange() {
        return taxChange;
    }

    public void setTaxChange(double taxChange) {
        this.taxChange = taxChange;
    }

    public String getTaxExempt() {
        return taxExempt;
    }

    public void setTaxExempt(String taxExempt) {
        this.taxExempt = taxExempt;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    public java.util.Date getChangeDate() {
        return changeDate;
    }

    public boolean isIsHistory() {
        return isHistory;
    }


    /**
    * Get a list of PurchaseDocument from the DB
    *
    * @param myConn       Connection to the database
    * @param fy           Flag to only retrieve items for a particular fyscal year
    * @param site         Flag to only retrieve items for a particular site
    */
    public static PurchaseDocument[] getItemList(DbConn myConn, int fy, int site) {
        return getItemList(myConn, fy, site, null, null);
    }


    /**
    * Get a list of PurchaseDocument from the DB
    *
    * @param myConn       Connection to the database
    * @param fy           Flag to only retrieve items for a particular fyscal year
    * @param site         Flag to only retrieve items for a particular site
    * @param pdList       List of purchase documents to lookup
    * @param searchText   Text to search for
    */
    public static PurchaseDocument[] getItemList(DbConn myConn, int fy, int site, String pdList, String searchText) {
        //init();
        String outLine = "";
        ResultSet rs = null;
        Statement stmt = null;
        int returnSize = 0;
        String sqlcode = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";
        PurchaseDocument[] item = null;
        try {
            Connection conn = myConn.conn;

            // Create a Statement
            stmt = conn.createStatement ();
            //String mySchemaPath = myConn.getSchemaPath();
            String mySchemaPath = "mms";

            sqlFrom = " FROM " + mySchemaPath + ".purchase_documents pd, " + mySchemaPath + ".departments d ";
            sqlWhere = " WHERE pd.deptid=d.id ";
            sqlWhere += (fy != 0) ? " AND ((pd.daterequested>=TO_DATE('10/01/" + (fy - 1) + "', 'MM/DD/YYYY') AND pd.daterequested<TO_DATE('10/01/" + (fy) + "', 'MM/DD/YYYY')))" : "";
            sqlWhere += (site !=0) ? "AND d.site=" + site + " " : "";
            sqlWhere += (pdList != null) ? "AND pd.prnumber IN (" + ((pdList.equals("")) ? "'No PDs Found'" : pdList) + ") " : "";
            sqlWhere += (searchText != null) ? "AND (LOWER(pd.briefdescription) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(pd.justification) LIKE '%" + searchText.toLowerCase() + "%' OR " +
                  "LOWER(pd.enclosures) LIKE '%" + searchText.toLowerCase() + "%' OR LOWER(pd.selectionmemo) LIKE '%" + searchText.toLowerCase() + "%' OR " +
                  "LOWER(pd.ssjustification) LIKE '%" + searchText.toLowerCase() + "%' OR " +
                  "pd.prnumber IN (SELECT vl.prnumber FROM " + mySchemaPath + ".vendor_list vl, " + mySchemaPath + ".vendors v WHERE LOWER(v.name) LIKE '%" + searchText.toLowerCase() + "%' AND v.id=vl.vendor)" +
                  ") " : "";
            sqlOrderBy = " ORDER BY prnumber ";

            sqlcode = "SELECT count(*) " + sqlFrom + sqlWhere;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            rs.next();
            returnSize = rs.getInt(1);
            rs.close();
            item = new PurchaseDocument[returnSize];

            sqlcode = "SELECT pd.prnumber, pd.ponumber, pd.amendment, pd.priority, pd.duedate, pd.author, pd.requester, pd.briefdescription, ";
            sqlcode += "pd.justification, pd.solesource, pd.ssjustification, pd.status, pd.daterequested, pd.daterequired, pd.deptid, ";
            sqlcode += "pd.chargenumber, pd.contracttype, pd.taxexempt, pd.tax, pd.shipping, pd.potype, pd.fob, pd.shipvia, pd.paymentterms, ";
            sqlcode += "pd.buyer, pd.amendedby, pd.awardtype, pd.vendor, pd.rfpdeliverdays, pd.rfpdaysvalid, pd.rfpduedate, pd.relatedpr, ";
            sqlcode += "pd.prdate, pd.podate, pd.bidremarks, pd.selectionmemo, pd.enclosures, pd.startdate, pd.enddate, pd.creditcardholder, pd.pdtotal, pd.refnumber, ";
            sqlcode += "d.site" + sqlFrom + sqlWhere  + sqlOrderBy;
//System.out.println(sqlcode);
            rs = stmt.executeQuery(sqlcode);
            int i = 0;
//System.out.println("PurchaseDocument-getItemList - Got Here 1");
            while (rs.next()) {
                item[i] = new PurchaseDocument();
                item[i].prnumber = rs.getString(1);
                item[i].ponumber = rs.getString(2);
                item[i].amendment = rs.getString(3);
                item[i].priority = rs.getString(4);
                item[i].dueDate = rs.getTimestamp(5);
                item[i].author = rs.getInt(6);
                item[i].requester = rs.getInt(7);
                item[i].briefDescription = rs.getString(8);
                item[i].justification = rs.getString(9);
                item[i].soleSource = rs.getString(10);
                item[i].ssJustification = rs.getString(11);
                item[i].status = rs.getInt(12);
                item[i].dateRequested = rs.getTimestamp(13);
                item[i].dateRequired = rs.getTimestamp(14);
                item[i].deptID = rs.getInt(15);
                item[i].chargeNumber = rs.getString(16);
                item[i].contractType = rs.getInt(17);
                item[i].taxExempt = rs.getString(18);
                item[i].tax = rs.getDouble(19);
                item[i].shipping = rs.getDouble(20);
                item[i].poType = rs.getInt(21);
                item[i].fob = rs.getInt(22);
                item[i].shipVia = rs.getString(23);
                item[i].paymentTerms = rs.getString(24);
                item[i].buyer = rs.getInt(25);
                item[i].amendedBy = rs.getInt(26);
                item[i].awardType = rs.getInt(27);
                item[i].vendorID = rs.getInt(28);
                item[i].vendor = ((item[i].vendorID != 0) ? new Vendor(item[i].vendorID, myConn) : null);
                item[i].rfpDeliveryDays = rs.getInt(29);
                item[i].rfpDaysValid = rs.getInt(30);
                item[i].rfpDueDate = rs.getTimestamp(31);
                item[i].relatedPR = rs.getString(32);
                item[i].relatedPRInfo = ((item[i].relatedPR != null) ? new PurchaseDocument(item[i].relatedPR, myConn) : null);
                item[i].prDate = rs.getTimestamp(33);
                item[i].poDate = rs.getTimestamp(34);
                item[i].bidRemarks = rs.getString(35);
                item[i].selectionMemo = rs.getString(36);
                item[i].enclosures = rs.getString(37);
                item[i].startDate = rs.getTimestamp(38);
                item[i].endDate = rs.getTimestamp(39);
                item[i].creditCardHolder = rs.getInt(40);
                item[i].pdTotal = rs.getDouble(41);
                item[i].refnumber = rs.getString(42);
                item[i].site = rs.getInt(43);
                item[i].isNew = false;
                i++;
            }
            rs.close();

        }
        catch (SQLException e) {
            outLine = outLine + "SQLException caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - PurchaseDocument - getItemList");
        }

        catch (Exception e) {
            outLine = outLine + "Exception caught: " + e.getMessage();
            //log(outLine);
            gov.ymp.csi.db.ALog.logError(myConn, 0,"slts",0, outLine + " - PurchaseDocument - getItemList");
        }
        finally {
            if (rs != null)
                try { rs.close(); } catch (Exception i) {}
            if (stmt != null)
                try { stmt.close(); } catch (Exception i) {}
        }

        return item;
    }


    /**
    * format dollar values for display
    *
    * @param val     The id to lookup from the db (String)
    */
    public static String dollarFormat (double val) {
        String outLine = "";

        DecimalFormat myFormatter = new DecimalFormat("$###,###,###.00");
        String result = myFormatter.format(val);

        return result;
    }



}
