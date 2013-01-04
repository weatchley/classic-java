function getItem(type,action,id){
	//alert(id);
	window.location = "item.jsp?type="+type+"&action="+action+"&id="+id;
}
function submitFormNew(action,scope,type){
	//alert('action:'+action+', scope:'+scope+', type:'+type);
	if (action=='reports'){
		generateReport(type);
	}else if(action=='browse'){
		//window.open ("doBrowse","browseWin"); 
		var porgId = document.main.porg.value;
		var orgId = document.main.org.value;
		var year = document.main.year.value;
		window.location = "browse.jsp?porgId="+porgId+"&orgId="+orgId+"&year="+year;
	}else{
		//
	}
	return false;
}
function generateReport(type){
	var porgId = document.main.porg.value;
	var orgId = document.main.org.value;
	var year = document.main.year.value;
	var sanumber = document.main. sanumber.checked;
	var teamlead = document.main.teamlead.checked;
	var satitle = document.main.satitle.checked;
	var purpose = document.main.purpose.checked;
	var scheduledate = document.main.scheduledate.checked;
	var rescheduledate = document.main.rescheduledate.checked;
	var signdate = document.main.signdate.checked;
	var canceldate = document.main.canceldate.checked;
	var cancelledrationale = document.main.cancelledrationale.checked;
	var comment = document.main.comment.checked;
	var condreport = document.main.condreport.checked;
	var asstype = document.main.asstype.checked;
	var assobj = document.main.assobj.checked;
	var crlevels = document.main.crlevels.checked;
	var crnums = document.main.crnums.checked;
	var llnums = document.main.llnums.checked;
	var colstr = "&sanumber="+sanumber+"&teamlead="+teamlead+"&satitle="+satitle+"&purpose="+purpose+"&scheduledate="+scheduledate+"&rescheduledate="+rescheduledate+"&signdate="+signdate+"&canceldate="+canceldate+"&cancelledrationale="+cancelledrationale+"&comment="+comment+"&condreport="+condreport+"&asstype="+asstype+"&assobj="+assobj+"&crlevels="+crlevels+"&crnums="+crnums+"&llnums="+llnums;
	if(type=="html"){
		alert("html - coming soon");
	}else if(type=="pdf"){
	window.open ("doPdfReport?porgId="+porgId+"&orgId="+orgId+"&year="+year+colstr,"reportWinPdf"); 
	}else if(type=="xsl"){
		window.open ("doXslReport","reportWinXsl"); 
	}else{
		alert("null - coming soon");
	}
	
}