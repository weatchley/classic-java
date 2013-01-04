package gov.ymp.opg.mypages;

import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;
import java.util.*;
import java.sql.*;

public class MyPagesDBMS {

	private DbConn myConn;
	private Connection conn;
	private Statement stmt;
	private StringBuffer sqlStr;

	MyPagesDBMS() {
		
	}

	public boolean getDBConn() {
		try {
			myConn = new DbConn();
			conn = myConn.getConn();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (myConn != null && conn != null)
			return true;
		return false;
	}

	public long layoutModify(Layout layout) {
		StringBuffer sql = new StringBuffer("UPDATE LAYOUTS SET ");
		sql.append("NAME = '");
		if (layout.getLayoutName() != null && layout.getLayoutName().length() > 0) {
			sql.append(layout.getLayoutName());
		}
		sql.append("', WIN_COUNT = '");
		sql.append(layout.getWinCount());
		sql.append("', POP_COUNT = '");
		sql.append(layout.getPopCount());
		sql.append(" WHERE LAYOUTID = ");
		sql.append(layout.getLayoutID());
		try {
            stmt = conn.createStatement();
            stmt.execute(sqlStr.toString());
            stmt.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return layout.getLayoutID();
	}
	
	public long layoutSave(Layout layout) {
		long layoutID = 0;
		if (getDBConn() != false) {
		    try {
		    	
		    	/* generate UNID for layout ID*/
		    	UNID myUNID = new UNID();
	            myUNID.create("layout");
	            layoutID = myUNID.getID();
	            myUNID.save(myConn.conn);

	            stmt = conn.createStatement();
	            sqlStr = new StringBuffer("INSERT INTO LAYOUTS VALUES(");
	            sqlStr.append(layoutID);
	            sqlStr.append(",'");
	            sqlStr.append(layout.getLayoutName());
	            sqlStr.append("',");
	            sqlStr.append(layout.getWinCount());
	            sqlStr.append(",");
	            sqlStr.append(layout.getPopCount());
	            sqlStr.append(",");
	            sqlStr.append(layout.getPersonID());
	            sqlStr.append(")");
	            System.out.println(sqlStr.toString());
	            ResultSet rSet = stmt.executeQuery(sqlStr.toString()); 
	            if (rSet == null) {
	            	layoutID = 0;
	            }
	            rSet.close();
	            stmt.close();
		    } catch (Exception e) {
		    	layoutID = 0;
		    	System.out.println(e.getMessage());
		    }
		}
		return layoutID;
	}
	
	public long windowSave(Window window) {
		long windowID = 0;
		boolean sts = true;
		if (getDBConn() == false)
			sts = false;
		try {
			/* generate UNID for layout ID*/
			UNID myUNID = new UNID();
			myUNID.create("window");
			windowID = myUNID.getID();
			myUNID.save(myConn.conn);	

			stmt = conn.createStatement();
			sqlStr = new StringBuffer("INSERT INTO WINDOWS VALUES(");
			sqlStr.append(windowID);
			sqlStr.append(",'");
			sqlStr.append(window.getWindowName());
			sqlStr.append("','");
			sqlStr.append(window.getWindowUrl());
			sqlStr.append("',");
			sqlStr.append(window.getWidth());
			sqlStr.append(",");
			sqlStr.append(window.getHeight());
			sqlStr.append(",");
			sqlStr.append(window.getXaxis());
			sqlStr.append(",");
			sqlStr.append(window.getYaxis());
			sqlStr.append(",");
			sqlStr.append(window.getZaxis());
			sqlStr.append(",'");
			sqlStr.append(window.getState());
			sqlStr.append("',");
			sqlStr.append(window.getLayoutID());
			sqlStr.append(",");
			sqlStr.append(window.getPopID());
			sqlStr.append(",'");
			sqlStr.append(window.getBC());
			sqlStr.append("')");
            sts = stmt.execute(sqlStr.toString());
            stmt.close();
		} catch (Exception e) {
			sts = false;
			System.out.println(e.getMessage());
		}
		refreshLayouts(window.getLayoutID());
		return windowID;
	}

	public boolean layoutDelete(long layoutID) {
		boolean sts = true;
		if (getDBConn() == false) {
			sts = false;
		} else {
			try {
				windowDelete(layoutID);
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("DELETE FROM LAYOUTS WHERE LAYOUTID = ");
				sqlStr.append(layoutID);
				stmt.execute(sqlStr.toString());
				stmt.close();
			} catch (Exception e) {
				sts = false;
				System.out.println(e.getMessage());
			}
		}
		updateUnidStatus(layoutID, "deleted");
		return sts;
	}

	public boolean windowDelete(long layoutID) {
		if (windowDelete(layoutID, 0) == true)
			return refreshLayouts(layoutID);
		else
			return false;
	}

	public boolean windowDelete(long layoutID, long windowID) {
		boolean sts = true;
		if (getDBConn() == false) {
			sts = false;
		} else {
			try {
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("DELETE FROM WINDOWS WHERE LAYOUTID = ");
				sqlStr.append(layoutID);
				if (windowID > 0) {
					sqlStr.append(" AND WINDOWID = ");
					sqlStr.append(windowID);
				}
				stmt.execute(sqlStr.toString());
				stmt.close();
			} catch (Exception e) {
				sts = false;
				System.out.println(e.getMessage());
			}
		}
		refreshLayouts(layoutID);
		updateUnidStatus(windowID,"deleted");
		return sts;
	}

	public boolean layoutUpdate(Layout layout) {
		boolean sts = true;
		if (getDBConn() == false) {
			sts = false;
		} else {
			try {
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("UPDATE LAYOUTS SET NAME = '");
				if (layout.getLayoutName() != null && layout.getLayoutName().length() > 0) {
					sqlStr.append(layout.getLayoutName()+"', ");
				} else {
					sqlStr.append("', ");
				}
				sqlStr.append("WIN_COUNT = ");
				sqlStr.append(layout.getWinCount()+", ");
				sqlStr.append("POP_COUNT = ");
				sqlStr.append(layout.getPopCount());
				sqlStr.append("PERSONID = ");
				sqlStr.append(layout.getPersonID());
				sqlStr.append(" WHERE LAYOUTID = ");
				sqlStr.append(layout.getLayoutID());

				sts = stmt.execute(sqlStr.toString());
				stmt.close();
			} catch (Exception e) {
				sts = false;
				System.out.println(e.getMessage());
			}
		}
		return sts;
	}

	public boolean windowUpdate(Window window) {
		boolean sts = true;
		if (getDBConn() == false) {
			sts = false;
		} else {
			try {
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("UPDATE WINDOWS LAYOUTS SET ");
				if (window.getWindowName() != null && window.getWindowName().length() > 0) {
					sqlStr.append("NAME = '"+window.getWindowName()+"',");
				} else {
					sqlStr.append("NAME = '',");
				}
				if (window.getWindowUrl() != null && window.getWindowUrl().length() > 0) {
					sqlStr.append("URL = '"+window.getWindowUrl()+"',");
				}
				if (window.getWidth() > 0) {
					sqlStr.append("WIDTH = '"+window.getWidth()+"',");
				}
				if (window.getHeight() > 0) {
					sqlStr.append("HEIGHT = '"+window.getHeight()+"',");
				}
				if (window.getXaxis() > 0) {
					sqlStr.append("XAXIS = '"+window.getXaxis()+"',");
				}
				if (window.getYaxis() > 0) {
					sqlStr.append("YAXIS = '"+window.getYaxis()+"',");
				}
				if (window.getZaxis() > 0) {
					sqlStr.append("ZAXIS = '"+window.getZaxis()+"',");
				}
				if (window.getState() != null && window.getState().length() > 0) {
					sqlStr.append("STATE = '"+window.getState()+"',");
				} else {
					sqlStr.append("STATE = 'reg',");
				}
				if (window.getZaxis() > 0) {
					sqlStr.append("ZAXIS = '"+window.getZaxis()+"',");
				}
				if (window.getZaxis() > 0) {
					sqlStr.append("ZAXIS = '"+window.getZaxis()+"',");
				}
				if (window.getLayoutID() > 0) {
					sqlStr.append("LAYOUTID = '"+window.getLayoutID()+"',");
				}
				if (window.getPopID() > 0) {
					sqlStr.append("POPID = '"+window.getPopID()+"',");
				}
				if (window.getBC() != null && window.getBC().length() > 0) {
					sqlStr.append("BC = '"+window.getBC()+"',");
				}
				sqlStr.append(" WHERE WINDOWID = ");
				sqlStr.append(window.getWindowID());

				stmt.execute(sqlStr.toString());
				stmt.close();
			} catch (Exception e) {
				sts = false;
				System.out.println(e.getMessage());
			}
		}
		return sts;
	}

	public boolean refreshLayouts(long layoutID) {
		boolean sts = true;
		int winCnt = 0, popCnt = 0;
		StringBuffer sqlStr;
		if (getDBConn() == false) {
			sts = false;
		} else {
			try {
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("SELECT COUNT(*) FROM WINDOWS WHERE LAYOUTID = ");
				sqlStr.append(layoutID);
				sqlStr.append(" AND POPID = -1");
				ResultSet rSet;
				rSet = stmt.executeQuery(sqlStr.toString());
				
				if (rSet == null) {
					sts = false;
				} else {
					while(rSet.next()) {
						winCnt = rSet.getInt(1);
					}
					rSet.close();
					sqlStr = new StringBuffer("SELECT COUNT(*) FROM WINDOWS WHERE LAYOUTID = ");
					sqlStr.append(layoutID);
					sqlStr.append(" AND POPID != -1");
					rSet = stmt.executeQuery(sqlStr.toString());
					if (rSet == null) {
						sts = false;
					} else {
						while (rSet.next()) {
							popCnt = rSet.getInt(1);
						}
						rSet.close();
						sqlStr = new StringBuffer("UPDATE LAYOUTS SET WIN_COUNT = ");
						sqlStr.append(winCnt);
						sqlStr.append(", POP_COUNT = ");
						sqlStr.append(popCnt);
						sqlStr.append(" WHERE LAYOUTID = ");
						sqlStr.append(layoutID);
						rSet = stmt.executeQuery(sqlStr.toString());
						if (rSet == null) {
							sts = false;
						}
						rSet.close();
					}
				}
				stmt.close();
			} catch (Exception e) {
				sts = false;
				System.out.println(e.getMessage());
			}
		}
		return sts;
	}

	public Vector getWindowsFromDB(long layoutID) {
		Vector windows = new Vector();
		Window window;
		ResultSet rSet;
		if (getDBConn() != false) {
			try {
				stmt = conn.createStatement();
				sqlStr = new StringBuffer("SELECT * FROM WINDOWS WHERE LAYOUTID = "+layoutID);
				rSet = stmt.executeQuery(sqlStr.toString());
				if (rSet != null) {
					while (rSet.next()) {
						window = new Window(
								rSet.getInt("WINDOWID"),
								rSet.getString("NAME"),
								rSet.getString("URL"),
								rSet.getInt("WIDTH"),
								rSet.getInt("HEIGHT"),
								rSet.getInt("XAXIS"),
								rSet.getInt("YAXIS"),
								rSet.getInt("ZAXIS"),
								rSet.getString("STATE"),
								rSet.getInt("LAYOUTID"),
								rSet.getInt("POPID"),
								rSet.getString("BC")
						);
						windows.addElement(window);
					}
				}
				rSet.close();
				stmt.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return windows;
	}

	public boolean updateUnidStatus(long unidID, String status) {
		boolean sts = true;
		if (getDBConn() != false) {
			try {
				stmt = conn.createStatement();
				StringBuffer sqlStr;
				sqlStr = new StringBuffer("UPDATE UNID SET STATUS = '"+status+"' WHERE ID = "+unidID);
				stmt.execute(sqlStr.toString());
				stmt.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return sts;
	}

	public void closeConn() {
		myConn.release();
	}
	
}
