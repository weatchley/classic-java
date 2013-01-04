package gov.ymp.opg.mypages;

import gov.ymp.opg.mypages.*;

public class LayoutsTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//System.out.println("LayoutsTest called!");
		
		long personID;
		personID = (long)1214;
		long layoutID; 
		layoutID = (long)4832;
		
		//Layouts layouts = new Layouts (1214);
		//long layoutID;
		//layoutID = (long)5;
		//System.out.println(layouts.getUserXML());
		//System.out.println(layouts.getPagesNamesXML());
			//System.out.println(layouts.getLayoutsXML());
			//System.out.println(layouts.getLayoutsXML(5));
			//System.out.println(layouts.getLayoutsXML(layoutID));
		
		LayoutsBean LB = new LayoutsBean();
		//testing LB.login
			//System.out.println(LB.login(personID));
		//testing LB.getPNames
			//System.out.println(LB.getPNames(personID));
		//testing LB.retrieve
			//System.out.println(LB.retrieve(personID, layoutID));
		//testing LB.saveLayout
			/*
			layoutID = LB.saveLayout("layout test",0,0,0);
			//System.out.println("LB.saveLayout returns: "+ layoutID);
			int winNum = 2; //let's save two windows
			for(int i=0;i<winNum;i++){
				//testing LB.saveWindow
					//System.out.println("LB.saveWindow: "+ LB.saveWindow("OCRWM Internet",
											layoutID,
											"http://www.ocrwm.doe.gov",
											514,
											482,
											16,
											46,
											161,
											"reg",
											-1,
											"divWin0||")
											);
			}
			*/
		//testing LB.layoutDelete
			//layoutID = (long)4838;
			//System.out.println("layoutDelete: "+LB.layoutDelete(layoutID));
		
			
		/*
		 * john's testing code as follows...     
		 */		
		/*
			MyPagesDBMS myPagesDB = new MyPagesDBMS();
			
			Layout layout = new Layout(0, (long)1214, "layout 3", 0,0);
			long lID = myPagesDB.layoutSave(layout);
				layout.setLayoutID(lID);
			Window window = new Window((long)0, "OCRWM Internet", "http://www.ocrwm.doe.gov", 514, 482, 16, 46, 101,
					"reg", layout.getLayoutID(), -1, "divWin0||"); 
			long wID = 0;
			wID = myPagesDB.windowSave(window);
				//window.setWindowID(wID);
					
			//myPagesDB.windowDelete(layout.getLayoutID(), window.getWindowID());
			//myPagesDB.layoutDelete(layout.getLayoutID());
			
			myPagesDB.closeConn();
			
				//myPagesDB.refreshLayouts(layout.getLayoutID());
		
		 */
	}

}