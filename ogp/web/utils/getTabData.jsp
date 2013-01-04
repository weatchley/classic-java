<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="../_plugins/tabs/tabs.css" />
<script src="../_plugins/utils/prototype.js" type="text/javascript"></script>
<script src="../_plugins/json/json.js" type="text/javascript"></script>
<!-- <link rel="stylesheet" type="text/css" href="../css/containers.css" /> -->
<link rel="stylesheet" type="text/css" href="../_plugins/rtmenu/rtmenu.css">
<script src="../_plugins/tabs/tabs.js" type="text/javascript"></script>


</head>
<body onload="create()">

<ul class='shadetabs' id="maintab">
    <li> <a href='http://localhost:9090'> wow </a> </li>
</ul>

<script language="javascript">
 var jString = "";
</script>



<script language="javascript">



 



 function create()
 {
   
   
  var url = 'https://ydappdev.ocrwm.doe.gov/ogp65/utils/tabSetNew.jsp';
  //var url = 'http://localhost:8080/ocrwmgateway/utils/tabSetNew.jsp';
   
  var ajaxRequest = new Ajax.Request(url,
				  {
						 method: 'get',
						 
						 onLoading: function()
						 {
						 
						 },
						 onSuccess: function(transport)
						 {
						     response = transport.responseText || "no response text";
						     var jString = response;
						 try
						 {
						   //alert("What is the value of jString " + jString);
                           var myJ = jString.parseJSON();
                           jString = myJ;
                           //alert("WHAT IS jString " + myJ);
                           var len = myJ.bindings.length;
						   var val = "";
						   var t = document.getElementById('maintab');
						   t.className = "shadetabs";
						   
						   for(var count = 0; count < len; count++)
						   {
						   
						      var text  = myJ.bindings[count].tabName;
						      var link  = myJ.bindings[count].tabLink;
						      var r = "Values " + myJ.bindings[count].tabName + " " +  myJ.bindings[count].tabLink + "\n";
						      val += r; 
						      var anchor  = document.createElement("A");
						      var li      = document.createElement("LI");
						      //li.className = "shadetabs";
						      var tNode   = document.createTextNode(text);
						      anchor.appendChild(tNode);
						      anchor.href = link;
						      li.appendChild(anchor);
						      t.appendChild(li);
						      //shadetabs
						      
						   } 
						  
						  
						  

						  initTabs();
						  
						 //  document.writeln(val);
						     
						 
						 }
						 catch(e)
						 {
						    alert("An error occured from json " + e.message);
						    
						 }
						 try{
						  //alert("Value 1 " + rString.bindings.length); /// + " " + rString.bindings[0].tabLink);
						  
						 }
						 catch(e)
						 {
						      alert("An error occured from json 22222222222222222 " + e.message);
						 }
						 //var oList = $('menuList');
						 //oList.innerHTML += "<li><a href='containers/home.html?3' target='mainiframe'>Resources</a>"+response+"</li>";
						 //alert(oList.innerHTML);
						 //return oList;
						 },
						 onFailure: function(){ alert('callAjax: Something went wrong...') }
				  });
				  
				  
  //   alert("LOADING .... end " + jString);				  
  
 }


</script>



</body>
</html>