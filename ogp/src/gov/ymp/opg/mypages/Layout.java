package gov.ymp.opg.mypages;

import java.util.*;

public class Layout {

	long personID;
	long layoutID;
	String layoutName;
	int winCount;
	int popCount;
	Vector windows;

	public Layout() {
		super();
		windows = new Vector();
	}

	public Layout(long layoutID, long personID, String layoutName, int winCount, int popCount) {
		super();
		this.layoutID = layoutID;
		this.personID = personID;
		this.layoutName = layoutName;
		this.winCount = winCount;
		this.popCount = popCount;
		windows = new Vector();
	}

	public long getLayoutID() {
		return layoutID;
	}

	public void setLayoutID(long layoutID) {
		this.layoutID = layoutID;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public long getPersonID() {
		return personID;
	}

	public void setPersonID(long personID) {
		this.personID = personID;
	}

	public int getPopCount() {
		return popCount;
	}

	public void setPopCount(int popCount) {
		this.popCount = popCount;
	}

	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

}