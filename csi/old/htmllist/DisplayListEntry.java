package gov.ymp.csi.htmllist;

public class DisplayListEntry {
	private long id;
	private String name;
	private String content;
	private String url;
	private String image;
	private String title;
	private String htmlClass;
	private String style;
	private String event;

	public DisplayListEntry(long id) {
		this.id = id;
		this.name = null;
		this.content = null;
		this.url = null;
		this.image = null;
		this.title = null;
		this.htmlClass = null;
		this.style = null;
		this.event = null;
	}

	public String getAttribute(String name) {
		String value;
		if (name.equals("id")) {
			value = String.valueOf(this.id);
		}
		else if (name.equals("name")) {
			value = this.name;
		}
		else if (name.equals("content")) {
			value = this.content;
		}
		else if (name.equals("url")) {
			value = this.url;
		}
		else if (name.equals("image")) {
			value = this.image;
		}
		else if (name.equals("title")) {
			value = this.title;
		}
		else if (name.equals("htmlClass")) {
			value = this.htmlClass;
		}
		else if (name.equals("style")) {
			value = this.style;
		}
		else if (name.equals("event")) {
			value = this.event;
		}
		else {
			value = null;
		}
		return value;
	}

	public void setAttribute(String name, String value) {
		if (name.equals("id")) {
			this.id = Long.valueOf(value).longValue();
		}
		else if (name.equals("name")) {
			this.name = value;
		}
		else if (name.equals("content")) {
			this.content = value;
		}
		else if (name.equals("url")) {
			this.url = value;
		}
		else if (name.equals("image")) {
			this.image = value;
		}
		else if (name.equals("title")) {
			this.title = value;
		}
		else if (name.equals("htmlClass")) {
			this.htmlClass = value;
		}
		else if (name.equals("style")) {
			this.style = value;
		}
		else if (name.equals("event")) {
			this.event = value;
		}
	}
}

/*
HTMLList Entry attributes
	id
	name
	text
	url
	image
	alt
	class
	style
	event
	parent
	children



HTMLList attributes
	class
	style
	style sheet
	javascript
	num of columns
	IsHierarchical
	IsSorted
	IsGroupBy
	filterOptions
*/