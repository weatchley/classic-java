package gov.ymp.opg.mypages;

import java.util.*;
import gov.ymp.csi.db.*;
import java.sql.*;

public class Layouts {

	private long personID;
	private String userName;
	private Vector layouts;
	private StringBuffer stringXML;

	public Layouts(long personID) {
		super();
		this.personID = personID;
		if (layouts == null || layouts.isEmpty()) {
			this.layouts = getLayoutsFromDB();
		}
	}

	public Layouts(long personID, Vector layouts) {
		super();
		this.personID = personID;
		if (layouts.isEmpty()) {
			this.layouts = getLayoutsFromDB();
		} else {
			this.layouts = layouts;
		}
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public long getPersonID() {
		return(this.personID);
	}

	private Vector getLayoutsFromDB() {
		// Get DBConn
		try {
			DbConn myConn = new DbConn();
			Connection conn = myConn.getConn();
			Statement stmt = conn.createStatement();

			//String sql = "Select * FROM NAJARIANJ.PERSON WHERE ID = "+personID;
			String sql = "Select * FROM CSI.PERSON WHERE ID = "+personID;
			ResultSet rSet = stmt.executeQuery(sql);
			while(rSet.next()) {
				userName = rSet.getString("USERNAME");
			}
			//sql = "SELECT * FROM NAJARIANJ.LAYOUTS l, NAJARIANJ.WINDOWS w WHERE l.PERSONID = "+personID+
					//" and w.LAYOUTID = l.LAYOUTID ORDER BY l.LAYOUTID, w.WINDOWID";
			sql = "SELECT * FROM CSI.LAYOUTS l, CSI.WINDOWS w WHERE l.PERSONID = "+personID+
			" and w.LAYOUTID = l.LAYOUTID ORDER BY l.LAYOUTID, w.WINDOWID";

			rSet = stmt.executeQuery(sql);
			// Get windows and layouts order by layoutID and WindowID
			// Create new layout
			// Create new window and add window to the layout windows vector
			// Add the layout to the layouts vector
			layouts = new Vector(0);
			long layoutID = -1;
			Layout layout = new Layout();
			Window window;
			while(rSet.next()) {
				int i = rSet.getInt("layoutid");
				if (layoutID < 0 ) {
					layoutID = i;		
					layout = new Layout(
							layoutID,
							(long)rSet.getInt("PERSONID"),
							rSet.getString("NAME"),
							rSet.getInt("WIN_COUNT"),
							rSet.getInt("POP_COUNT"));
				}
				if (layoutID != rSet.getInt("LAYOUTID")) {
					this.layouts.add(layout);
					layoutID = rSet.getInt("LAYOUTID");
					layout = new Layout(
							layoutID,
							rSet.getInt("PERSONID"),
							rSet.getString("NAME"),
							rSet.getInt("WIN_COUNT"),
							rSet.getInt("POP_COUNT"));
				}
				window = new Window(
						rSet.getInt("WINDOWID"),
						rSet.getString(7),
						rSet.getString("URL"),
						rSet.getInt("WIDTH"),
						rSet.getInt("HEIGHT"),
						rSet.getInt("XAXIS"),
						rSet.getInt("YAXIS"),
						rSet.getInt("ZAXIS"),
						rSet.getString("STATE"),
						rSet.getInt("LAYOUTID"),
						rSet.getInt("POPID"),
						rSet.getString("BC"));
						
				layout.windows.add(window);
			}
			layouts.add(layout);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return this.layouts;
	}

	
	public int getLayoutsSize(){
		return layouts.size();
	}
	
	
	public String getUserXML () {
		stringXML = new StringBuffer("<userInfo>" +
					"<userId>"+personID+"</userId>" +
					"<userName>"+userName+"</userName>"+
					"<winnum>"+layouts.size()+"</winnum>" +
					"</userInfo>\n");
		return stringXML.toString();
	}

	public String getPagesNamesXML() {
		stringXML = new StringBuffer("<pageInfo>");
		stringXML.append("<pagenum>"+layouts.size()+"</pagenum>");
		for (int i = 0; i < layouts.size(); i++) {
			Layout layout = (Layout)layouts.elementAt(i);
			stringXML.append("<lDesc"+i+">"+layout.getLayoutName()+"</lDesc"+i+">");
			stringXML.append("<lNum"+i+">"+layout.getLayoutID()+"</lNum"+i+">");
		}
		stringXML.append("</pageInfo>");
		return stringXML.toString();
	}

	public String getLayoutsXML(long layoutID) {
		//public String getLayoutsXML() {
		
		stringXML = new StringBuffer("<pageInfo>");
		for (int i = 0; i < layouts.size(); i++) {
			Layout layout = (Layout)layouts.elementAt(i);
			if(layoutID==layout.getLayoutID()){
				stringXML.append("<layid>"+layout.getLayoutID()+"</layid>");
				stringXML.append("<winnum>"+layout.getWinCount()+"</winnum>");
				stringXML.append("<pwCount>"+layout.getPopCount()+"</pwCount>");
				stringXML.append("<lDesc>"+layout.getLayoutName()+"</lDesc>");
				for (int ii = 0; ii < layout.getWinCount(); ii++) {
					Window window = (Window)layout.windows.elementAt(ii);
					stringXML.append("<cn"+ii+">"+window.getWindowName()+"</cn"+ii+">");
					stringXML.append("<cu"+ii+">"+window.getWindowUrl() +"</cu"+ii+">");
					stringXML.append("<origw"+ii+">"+window.getWidth()+"</origw"+ii+">");
					stringXML.append("<origh"+ii+">"+window.getHeight()+"</origh"+ii+">");
					stringXML.append("<origx"+ii+">"+window.getXaxis()+"</origx"+ii+">");
					stringXML.append("<origy"+ii+">"+window.getYaxis()+"</origy"+ii+">");
					stringXML.append("<zIndex"+ii+">"+window.getZaxis()+"</zIndex"+ii+">");
					stringXML.append("<state"+ii+">"+window.getState()+"</state"+ii+">");
					stringXML.append("<bc"+ii+">"+window.getBC()+"</bc"+ii+">");
				}
			}
		}
		stringXML.append("</pageInfo>");
		return stringXML.toString();
		
		/*
		stringXML = new StringBuffer("<pageInfo>");
		for (int i = 0; i < layouts.size(); i++) {
			Layout layout = (Layout)layouts.elementAt(i);
			stringXML.append("<layid>"+layout.getLayoutID()+"</layid>");
			stringXML.append("<winnum>"+layout.getWinCount()+"</winnum>");
			stringXML.append("<pwCount>"+layout.getPopCount()+"</pwCount>");
			for (int ii = 0; ii < layout.getWinCount(); ii++) {
				Window window = (Window)layout.windows.elementAt(ii);
				stringXML.append("<cn"+ii+">"+window.getWindowName()+"</cn"+ii+">");
				stringXML.append("<cu"+ii+">"+window.getWindowUrl() +"</cu"+ii+">");
				stringXML.append("<origw"+ii+">"+window.getWidth()+"</origw"+ii+">");
				stringXML.append("<origh"+ii+">"+window.getHeight()+"</origh"+ii+">");
				stringXML.append("<origx"+ii+">"+window.getXaxis()+"</origx"+ii+">");
				stringXML.append("<origy"+ii+">"+window.getYaxis()+"</origY"+ii+">");
				stringXML.append("<zIndex"+ii+">"+window.getZaxis()+"</zIndex"+ii+">");
				stringXML.append("<state"+ii+">"+window.getState()+"</state"+ii+">");
				stringXML.append("<bc"+ii+">"+window.getBC()+"</bc"+ii+">");
			}
		}
		stringXML.append("</pageInfo>");

		return stringXML.toString();
		*/
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getLayoutWinCount(int idx) {
		Layout layout = (Layout)layouts.elementAt(idx);
		if (layout == null){
			return 0;
		}
		return layout.getWinCount();
	}

	public int getLayoutPopWinCount(int idx) {
		Layout layout = (Layout)layouts.elementAt(idx);
		if (layout == null){
			return 0;
		}
		return layout.getPopCount();
	}

}