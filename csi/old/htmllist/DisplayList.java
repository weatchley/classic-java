package gov.ymp.csi.htmllist;

import java.io.*;
import java.util.*;

public class DisplayList {
	private ArrayList nodeList;
	private int numOfCol;
	private int width;

	public DisplayList() {
		this(1);
	}

	public DisplayList(int numOfCol) {
		this.numOfCol = numOfCol;
		this.width = 0;
		this.nodeList = new ArrayList();
	}

	public DisplayList(int numOfCol, int width) {
		this(numOfCol);
		this.width = width;
	}

	public void addNode(DisplayListEntry node) {
		nodeList.add((DisplayListEntry) node);
	}

	public void deleteNode(String value) {

	}

	public String printList() {
		String out = "<div";
		DisplayListEntry node;
		int count = 0;
		try {
			if (this.width > 0) {
				out += " style=\"width: " + this.width;
			}
			out += ">";
			for (Iterator iterator = nodeList.iterator(); iterator.hasNext();) {
				node = (DisplayListEntry) iterator.next();
				count++;
				//out += "count = " + count + "mod = " + (count % numOfCol) + "\n";
				if (count % numOfCol == 1) {
					out += "\n<div>\n";
				}
				if (node.getAttribute("url") != null && !(node.getAttribute("url").equals(""))) {
					out += "<a href=\"" + node.getAttribute("url") + "\">";
				}
				out += "<span id=\"" + node.getAttribute("id") + "\"";
				if (node.getAttribute("name") != null && !(node.getAttribute("name").equals(""))) {
					out += " name=\"" + node.getAttribute("name") + "\"";
				}
				if (node.getAttribute("title") != null && !(node.getAttribute("title").equals(""))) {
					out += " title=\"" + node.getAttribute("title") + "\"";
				}
				if (node.getAttribute("event") != null && !(node.getAttribute("event").equals(""))) {
					out += " " + node.getAttribute("event");
				}
				out += " style=\"";
				if (node.getAttribute("image") != null && !(node.getAttribute("image").equals(""))) {
					out += "background-image: url(" + node.getAttribute("image") + ");";
				}
				if (node.getAttribute("style") != null && !(node.getAttribute("style").equals(""))) {
					out += node.getAttribute("style");
				}
				out += "\"";
				if (node.getAttribute("className") != null && !(node.getAttribute("className").equals(""))) {
					out += " class=" + node.getAttribute("className");
				}
				out += ">";
				if (node.getAttribute("content") != null && !(node.getAttribute("content").equals(""))) {
					out += node.getAttribute("content");
				}
				out += "</span>";
				if (node.getAttribute("url") != null && !(node.getAttribute("url").equals(""))) {
					out += "</a>";
				}
				if (count % numOfCol == 0) {
					out += "\n</div>\n";
				}
			}
			out += "</div>";
		}
		catch (Exception e) {
			System.err.println ("DisplayList error: " + e);
		}
		return out;
	}
}