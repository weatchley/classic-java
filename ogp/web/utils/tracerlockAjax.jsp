<%
	String newsUrl = "https://www.tracerlock.com/news-feed.cgi?id=";
	String nId = request.getParameter("nId");
	newsUrl = newsUrl + nId;
%>
<script id=newsscript src="<%=newsUrl%>"></script>