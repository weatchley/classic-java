function runFx(f){
	f();
}

function ajaxCall(domainName,statusObj){
				  
				  statusObj.innerHTML="Loading..."
			  	  
			  	  var sUrl = "/adminCSI/doSync?domainName="+domainName;
			  	  		//alert(sUrl);
			  	  var parameters = "";
	              var ajaxRequest = new Ajax.Request(sUrl,
	              {
	                     method: 'get',
	                     parameters: parameters,
	                     onLoading: function() {
	                     	statusObj.innerHTML="Ajax call placed..."
	            		 },
	                     onSuccess: function(transport){
	      				 var response = transport.responseText || "no response text";
	      				 	//alert("ajaxRefresh: Success! \n\n" + response);
	      				 	statusObj.innerHTML="Ajax Call Received"
	      				 	statusObj.innerHTML = response;
	    				 },
	    				 onFailure: function(){ alert('ajaxRefresh: Something went wrong...') }
	              });
	              

}
