package gov.ymp.opg.mypages;

public class Window {

	long windowID;
	String windowName;
	String windowUrl;
	int width;
	int height;
	int xaxis;
	int yaxis;
	int zaxis;
	String state;
	long layoutID;
	int popID;
	String BC;

	public Window(long windowID, String windowName, String windowUrl, int width, int height, int xaxis, int yaxis, int zaxis, String state, long layoutID, int popID, String BC) {
		super();
		this.windowID = windowID;
		this.windowName = windowName;
		this.windowUrl = windowUrl;
		this.width = width;
		this.height = height;
		this.xaxis = xaxis;
		this.yaxis = yaxis;
		this.zaxis = zaxis;
		this.state = state;
		this.layoutID = layoutID;
		this.popID = popID;
		this.BC = BC;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPopID() {
		return popID;
	}

	public void setPopID(int popID) {
		this.popID = popID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public long getWindowID() {
		return windowID;
	}

	public void setWindowID(int windowID) {
		this.windowID = windowID;
	}

	public String getWindowName() {
		return windowName;
	}

	public void setWindowName(String windowName) {
		this.windowName = windowName;
	}

	public String getWindowUrl() {
		return windowUrl;
	}

	public void setWindowUrl(String windowUrl) {
		this.windowUrl = windowUrl;
	}

	public int getXaxis() {
		return xaxis;
	}

	public void setXaxis(int xaxis) {
		this.xaxis = xaxis;
	}

	public int getYaxis() {
		return yaxis;
	}

	public void setYaxis(int yaxis) {
		this.yaxis = yaxis;
	}

	public int getZaxis() {
		return zaxis;
	}

	public void setZaxis(int zaxis) {
		this.zaxis = zaxis;
	}

	public long getLayoutID() {
		return layoutID;
	}

	public void setLayoutID(long layoutID) {
		this.layoutID = layoutID;
	}

	public String getBC() {
		return BC;
	}

	public void setBC(String BC) {
		this.BC = BC;
	}

	public void setWindowID(long windowID) {
		this.windowID = windowID;
	}

}
