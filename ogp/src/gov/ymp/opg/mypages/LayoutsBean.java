package gov.ymp.opg.mypages;

import java.io.Serializable;
import java.util.*;

public class LayoutsBean implements Serializable {
	private static final long serialVersionUID = 0;
	private Layouts layouts;
	private String layoutsXML;
	private String tString;
	private long personID;
	private int numLayouts;

	public String login(long personID){
		//public String login(long pid){
		//setPersonID(pid);
		//layouts = getLayouts();
		this.personID = personID;
		layouts = new Layouts(personID);
		tString = layouts.getUserXML();
		numLayouts = layouts.getLayoutsSize();
		return tString;
	}

	public String getPNames(long personID){
		//setPersonID(pid);
		this.personID = personID;
		layouts = new Layouts(personID);
		tString = layouts.getPagesNamesXML();
		return tString;
	}

	public String retrieve(long personID, long pageNum){
		//setPersonID(pid);
		this.personID = personID;
		layouts = new Layouts(personID);
		tString = layouts.getLayoutsXML(pageNum);
		return tString;
	}

	public Layouts getLayouts() {
		layouts = new Layouts(personID); 
		return layouts;
	}

	public void setLayouts(Layouts layouts) {
		this.layouts = layouts;
	}

	public long getPersonID() {
		return personID;
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public long saveLayout(String name, int winCnt, int popID, long personID) {
		long layoutID = 0;
		Layout layout = new Layout(0, personID, name, winCnt, popID);
		if (layout != null) {
			MyPagesDBMS myPagesDBMS = new MyPagesDBMS();
			layoutID = myPagesDBMS.layoutSave(layout);
		}
		return layoutID;
	}

	public boolean saveWindow(String windowName, long layoutID, String windowUrl,
			int width, int height, int xaxis, int yaxis, int zaxis, String state,
			int popID, String BC) {
		boolean sts = true;
		Window window = new Window(0, windowName, windowUrl, width, height, xaxis,
				yaxis, zaxis, state, layoutID, popID, BC);
		if (window != null) {
			MyPagesDBMS myPagesDBMS = new MyPagesDBMS();
			myPagesDBMS.windowSave(window);
			return sts;
		} else {
			sts = false;
		}
		return sts;
	}

	public boolean layoutDelete(long layoutID) {
		boolean sts = true;
		MyPagesDBMS myPagesDBMS = new MyPagesDBMS();
		//first delete windows
		sts = deleteWindows(layoutID);
		if(sts){
			if (myPagesDBMS.layoutDelete(layoutID) != true) {
				sts = false;
			}
		}else{
			System.out.println("(layoutDelete) there is error deleting windows...");
		}
		return sts;
	}

	public boolean deleteWindows(long layoutID) {
		boolean sts = true;
		MyPagesDBMS myPagesDBMS = new MyPagesDBMS();
		if (myPagesDBMS.windowDelete(layoutID,0) != true) {
			sts = false;
		}
		sts = myPagesDBMS.refreshLayouts(layoutID);
		return sts;
	}

	public boolean deleteWindow(long windowID) {
		boolean sts = true;
		MyPagesDBMS myPagesDBMS = new MyPagesDBMS();
		if (myPagesDBMS.windowDelete(0,windowID) != true) {
			sts = false;
		}
		return sts;
	}

	public String getUserXML () {
		StringBuffer stringXML = new StringBuffer("<userInfo>" +
				"<userId>"+personID+"</userId>" +
				"<userName>"+layouts.getUserName()+"</userName>"+
				"<winnum>"+layouts.getLayoutsSize()+"</winnum>" +
				"</userInfo>\n");
		return stringXML.toString();
	}

	public int getNumLayouts() {
		return numLayouts;
	}

	public void setNumLayouts(int numLayouts) {
		this.numLayouts = numLayouts;
	}

	public int getLayoutWinCount(int layoutIdx) {
		int winCnt = 0;
		layouts.getLayoutWinCount(layoutIdx);
		return winCnt;
	}

	public int getLayoutPopWinCount(int layoutIdx) {
		int winCnt = 0;
		layouts.getLayoutPopWinCount(layoutIdx);
		return winCnt;
	}
	
}
