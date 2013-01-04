package gov.ymp.slts;

import javax.servlet.*;
import javax.servlet.http.*;
// Support classes
import java.io.*;
import java.util.*;
import java.math.*;
import java.awt.*;
import java.text.*;
import gov.ymp.csi.people.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.misc.*;
import gov.ymp.csi.systems.*;
import gov.ymp.csi.auth.*;
import gov.ymp.csi.items.*;
import gov.ymp.csi.UNID.*;
import javax.naming.*;
import gov.ymp.slts.model.*;
import gov.ymp.slts.mssql.db.*;
import java.sql.*;

public class runInventoryUpdate extends HttpServlet {

    static Timer timer = null;

    public void init(ServletConfig servletConfig) throws ServletException {
        String outLine;
        String tempText;
        boolean isDeveloperWorkstation = true;
        String productionStatus = "development";
        long interval = 24 * 60 * 60 * 1000;

        super.init(servletConfig);

        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 19);
        time.set(Calendar.MINUTE, 30);
        time.set(Calendar.SECOND, 0);
        timer = new Timer();

        try {
            Context initCtx = new InitialContext();
            productionStatus = (String) initCtx.lookup("java:comp/env/ProductionStatus");
            tempText = (String) initCtx.lookup("java:comp/env/isDeveloperWorkstation");
            isDeveloperWorkstation = (tempText != null && tempText.equals("true")) ? true : false;
        } catch (NamingException e) {
            isDeveloperWorkstation = false;
        }

        if (!isDeveloperWorkstation) {
            System.out.println("Setting up DoInventory task...");
            timer.scheduleAtFixedRate(new DoInventory(), time.getTime(), interval);
        }
    }

    public void destroy() {
        //System.out.println("runInventoryUpdate destroy");
        timer.cancel();
        super.destroy();
    }

    public class DoInventory extends TimerTask {

        public void run() {

            DbConn myConn = new DbConn("csi");
            DbConn lockConn = new DbConn("csi");
            DbConnM myConn2 = new DbConnM();
            String outLine = "";
            Statement stmt = null;

            try {

                outLine += "Start Inventory Process at " + Utils.genDateID() + "\n";
                // set up lock so only one update can happen at a time
                stmt = lockConn.conn.createStatement();
                stmt.execute("SELECT * FROM slts.sw_trans_type FOR UPDATE");

                ALog.logActivity(0, "slts", 2, "SLTS Inventory: Started");
                System.out.println("Altirs Data Update - Started " + Utils.genDateID());
                outLine += "Load Domains: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Load/Update Domains " + Utils.genDateID());
                Domain.load(myConn, myConn2);

                outLine += "Load Computers: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Delete Computers " + Utils.genDateID());
                Computer.empty(myConn);

                System.out.println("Altirs Data Update - Load Computers " + Utils.genDateID());
                Computer.load(myConn, myConn2);

                outLine += "Load Applications: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Delete Applications " + Utils.genDateID());
                AppInventory.empty(myConn);

                System.out.println("Altirs Data Update - Load Applications " + Utils.genDateID());
                AppInventory.load(myConn, myConn2);

                outLine += "Load Services: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Delete Services " + Utils.genDateID());
                ServiceInventory.empty(myConn);

                System.out.println("Altirs Data Update - Load Services " + Utils.genDateID());
                ServiceInventory.load(myConn, myConn2);
                outLine += "Update Counts: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Update Counts " + Utils.genDateID());

                SWProduct.zeroOutCounts(myConn);
                SWTransactions.updateLicenseCounts(myConn);
                ProdAppInvMatch.updateLicenseCounts(myConn);
                ProdServInvMatch.updateLicenseCounts(myConn);
                SWProduct.calcRemainingCounts(myConn);
                ProductUsage.updateUsage(myConn);
                myConn.conn.commit();
                outLine += "End: (" + Utils.genDateID() + ")\n";
                System.out.println("Altirs Data Update - Finished " + Utils.genDateID());
                ALog.logActivity(0, "slts", 2, "SLTS Inventory: " + outLine + "");

            } catch (Exception e) {
                System.out.println("SLTS DS data update error " + e.getMessage());
                outLine = outLine + "Exception caught: " + e.getMessage();
                ALog.logError(0, "slts", 0, "DoInventory error: '" + outLine + "'");
            } finally {
                myConn.release();
                myConn2.release();
                try {lockConn.conn.commit();} catch (Exception e) { }
                lockConn.release();
            }
        }
    }
}
