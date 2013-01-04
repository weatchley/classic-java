function validateinput (f,pdok) {
    var msg = "";
    var empty_fields = "";
    var errors = "false";
    var howmany = f.howmany.value;
    for (var i = 0; i < f.length; i++) {
        var e = f.elements[i];
        if (((e.type == "text") || (e.type == "textarea")) && !e.optional) {
            if ((e.value == null) || (e.value == "") || isblank(e.value)) {
		var tempname = "";
		var namesuffix = "";
		if (e.name.indexOf ("name") || e.name.indexOf ("affiliation")) {
 		    var namesubstr = e.name.slice(-2);
		    if (namesubstr.indexOf("e") > -1) {
			namesuffix = e.name.slice(-1);
		    }
		    else if (namesubstr.indexOf("n") > -1) {
			namesuffix = e.name.slice(-1);
		    }
		    else {
			namesuffix = namesubstr;
		    }
		}
		if (e.name == "purpose") {
		    tempname = "Purpose of visit";
		    errors = "true";
		}
		else if (e.name == "howmany") {
		    tempname = "Number of visitors";
		    errors = "true";
		}
		else if (e.name.indexOf("firstname") > -1) {
		    tempname = "First name of visitor " + namesuffix;
		    errors = "true";
		}
		else if (e.name.indexOf ("lastname") > -1) {
		    tempname = "Last name of visitor " + namesuffix;
		    errors = "true";
		}
		else if (e.name.indexOf ("affiliation") > -1) {
		    tempname = "Affiliation of visitor " + namesuffix;
		    errors = "true";
		}
			
                empty_fields += "\n        " + tempname;
                continue;
            }
        }
        else if (e.type == "checkbox" && e.name.indexOf("iscitizen") > -1 && !(e.checked)) {
var visitornum = e.name.slice (-2);
            msg += "All visitors must be US citizens. If visitor " + (visitornum.indexOf("n") ? visitornum : e.name.slice (-1)) + " is not a citizen, submit his/her information separately.\n"; 
        }
        
        
    }
//	    if (!empty_fields && !errors) return true;
    //if (empty_fields) {
    if (errors == "true") {   
        msg += "\nPlease fill out the following required fields: " + empty_fields + "\n\n";
    }
    msg += validate_date(f.syear.value, f.smonth.value, f.sday.value, 0, 0, 0, 0, pdok, 1, 0);
    msg += validate_date(f.eyear.value, f.emonth.value, f.eday.value, 0, 0, 0, 0, pdok, 1, 0);
   
    var startDate = parseInt(f.syear.value + pad(f.smonth.value) + pad(f.sday.value));
    var endDate = parseInt(f.eyear.value + pad(f.emonth.value) + pad(f.eday.value));

    if (endDate < startDate) {    
        msg += "\nThe end date cannot precede the start date.\n";
    }
    
    if (msg != "") {
        alert (msg);
        return false;
    }
    else {
    	f.submit();
   }
}

function validatelength(param){
	//error checking for length of fields
	if(param.name == "purpose" && param.value.length >= 1000)
	        alert("Purpose of Visit should be less than 1000 characters in length!  Please reenter data in Purpose of Visit field.");
	     else if(param.name == "comment" && param.value.length >= 1000)
	          alert("Comment should be less than 1000 characters in length!  Please reenter data in Comment field.");
}

function pad(value) {
	if (value.length == 1) {
		value = "0" + value;
	}
	return value;
}


function drawdate (prefix, defaultdate) {
    var todaysdate;
    var da = 1;
    var mo = "January";
    var yr = 2006;
    var monthNames = new Array("","January","February","March","April","May","June","July","August","September","October","November","December");
    var datestring = "";
    var month = "<select name=" + prefix + "month>";
    var day = "<select name=" + prefix + "day>";
    if (defaultdate == 'today') {
	todaysdate = new Date();
	da = todaysdate.getDate();
	mo = todaysdate.getMonth();
	yr = todaysdate.getFullYear();
    }
    var monthcount = 1;
    var selected = "";
    while (monthcount <= 12) {
        selected = ((monthcount - 1) == mo) ? " selected" : "";
	month = month + "<option value=" + monthcount + selected + ">" + monthNames[monthcount] + "\n";
	monthcount++;
    }
	
    var daycount = 1;
    while (daycount <= 31) {
        selected = (daycount == da) ? " selected" : "";
        day = day + "<option value=" + daycount + selected + ">" + daycount + "\n";
        daycount++;
    }
	
    month = month + "</select>";
    day = day + "</select>";
	
    var year = "<input type=text name=" + prefix + "year value=" + yr + " size=4>";
    datestring = month + " " + day + ", " + year;
    document.write (datestring);
}

function view_report2 (type) {
    document.report.rtype.value = type;
    document.report.action = 'hvr_displayreport.jsp';
    document.report.submit();
}
	
//function view_report (username, authenticated) {
//    document.enter.username = username;
//    document.enter.authenticated = authenticated;
//    document.enter.action = 'hvr_displayreport.jsp';
//    document.enter.submit();
//}

// function returns true if the given year is a leap year
function isleapyear(year) {
    var returnvalue = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
    return (returnvalue);
}


//function returns null string "" if a date is valid or an error string if it is invalid describing the
//first encountered reason that the date is invalid.
//pastdateok should be true if dates from the past are valid
//futuredateok should be true if dates from the future are valid
//fulldatetime should be true if the hour, minute, second, millisecond are to be considered when testing for
//  past and future dates
//the year must be  a 4 digit year (0026 = 26 AD)
function validate_date(year, month, day, hour, minute, second, millisecond, pastdateok, futuredateok, fulldatetime) {
    var returnvalue = "";
    var testdate;
    var months = new Array('', "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    if (year.length < 4) {
        return ("You must enter a 4 digit year.");
    }
    if ((day < 1) || (day > 31)) {
        return ("You have entered an invalid day.");
    }
    if ((month < 1) || (month > 12)) {
        return ("You have entered an invalid month.");
    }
    switch (month) {
        case 4:
        case "4":
        case "04":
        case 6:
        case "6":
        case "06":
        case 9:
        case "9":
        case "09":
        case 11:
        case "11":
        if (day > 30) {
            returnvalue = "Invalid day (31st) for the month of " + months[month] + ".";
        }
        break;
        case 2:
        case "2":
        case "02":
        if (isleapyear(year)) {
            returnvalue = (day > 29) ? "Cannot have more than 29 days in February of " + year + "." : "";
        }
        else {
            returnvalue = (day > 28) ? "Cannot have more than 28 days in February of " + year + "." : "";
        }
        break;
        default:
        if (day > 31) {
            returnvalue = "Cannot have more than 31 days in the month of " + months[month] + ".";
        }
    }
    if (returnvalue != "") {
        // we do a return here because the following is invalid if we've determined we have an invalid date.
        return (returnvalue);
    }
    testdate = new Date(year, month-1, day, hour, minute, second, millisecond);
    today = new Date();
    if (!(fulldatetime)) {
        today = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 0,0,0,0);
    }
    if ( (!(pastdateok)) && (testdate.getTime() < today.getTime() ) ) {
        returnvalue = "You have selected a date from the past and the system will not accept past dates.";
    }
    if ( (!(futuredateok)) && (testdate.getTime() > today.getTime() ) ) {
        returnvalue = "You have selected a date from the future and the system will not accept future dates.";
    }
    return (returnvalue);
}

// A utility function that returns true if a string contains only
// whitespace characters.
function isblank(s) {
    if (s == null) return true;
    for(var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if ((c != ' ') && (c != '\n') && (c != '\t') && (c !='\r')) return false;
    }
    return true;
}

// function that returns true if a string contains only numbers
function isnumeric(s) {
    for(var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if ((c < '0') || (c > '9')) return false;
    }
    return true;
}


function addvisitor () {
    var howmany = document.enter.howmany.value;
    var i = 0;
    document.all.visitorinfo.innerHTML = "";
    for (i = 2; i <= howmany; i++) {
        document.all.visitorinfo.innerHTML = document.all.visitorinfo.innerHTML + 
             "<hr width=100>" +
             "<table>" +
             "<tr><td colspan=3>First Name:&nbsp;&nbsp;<input type=text name=visitorfirstname" + i + " size=20 maxlength=45>&nbsp&nbsp;Last Name:&nbsp&nbsp;<input type=text name=visitorlastname" + i + " size=20 maxlength=45></td></tr>" +
             "<tr><td colspan=3>Affiliation:&nbsp;&nbsp;<input type=text name=visitoraffiliation" + i + " size=40 maxlength=60 value=><font>(organization or employer)</font></td></tr>" +
             "<tr><td colspan=3>Destination:&nbsp;&nbsp;<select name='destination" + i + "'><option value='DOE Hillshire Facility'>DOE Hillshire Facility</option><option value='M&O Facilities'>M&O Facilities</option><option value='Lead Lab Facilities'>Lead Lab Facilities</option></select>" +
             "<input type='checkbox' name='otherbox" + i + "' onClick='javascript:otherChecked(" + i + ");'><input type='hidden' name='otherchecked" + i + "' value=''>Other:&nbsp;<input type=text name='destinationother" + i + "' size='25' style='display:none'></td></tr>" +
             "<tr><td>US Citizen?&nbsp;<input type=checkbox name=iscitizen" + i + " value=T></td>" +
             "<td width=10><li></td>" + 
             "<td>Requires&nbsp;&nbsp;(Escort <input type=radio name=escortorproxy" + i + " value=0 checked>)&nbsp;&nbsp;or&nbsp;&nbsp;(Visitor Badge <input type=radio name=escortorproxy" + i + " value=1>)</td></tr>" + 
             "</table>";
    }
}

function editRequest (id, command) {
    document.report.visitid.value = id;
    document.report.command.value = command;
    document.report.action = 'hvr_editrequest.jsp';
    document.report.submit();
}

function getTodaysDate (whataction) {
	var dayNames = new Array("Sunday","Monday","Tuesday","Wednesday",
          					 "Thursday","Friday","Saturday");

	var monthNames = new Array("January","February","March","April",
	                           "May","June","July","August","September",
	                           "October","November","December");

	var now = new Date();
	var da = now.getDate();
	var mo = now.getMonth();
	var yr = now.getFullYear();
	if (whataction == 'print') {
		document.write(dayNames[now.getDay()] + ", " +
			monthNames[mo] + " " +
			da + ", " + yr);
	}
	else {
		return (da, mo, yr);
	}
}

function gotologin () {
    document.report.action = "index.html";
    document.report.submit();

}


function header (h2) {
    var theheader = "";
    theheader += "<script src=js/hvr.js></script>\n";
    theheader += "<link type=\"text/css\" rel=\"stylesheet\" href=\"css/hvr.css\" />\n";
    theheader += "</head>\n<body>\n";
    theheader += "<table border=20 bordercolor=#000fab bordercolorlight=#b2b7e5 bordercolordark=#00095f align=center>\n";
    //theheader += "<table border=20 bordercolor=#000fab bordercolordark=#000fab bordercolorlight=#fab000 align=center>\n";
    theheader += "<tr><td>\n";
    theheader += "<table width=550 align=center>\n";
    theheader += "<tr align=left><td colspan=2 nowrap>\n";
    theheader += "<h1>OCRWM Las Vegas Visitor Requests Database</h1>\n<h2>" + h2 + "</h2>\n";
    theheader += "</td></tr>\n";
    document.write(theheader);
}

function footer () {
    var thefooter = "</table>\n<br>\n</td></tr>\n</table>\n";
    thefooter += "</body>\n</html>\n";
    document.write(thefooter);
}

function addVisitor() {
// add an entry to the subcomment table
    var cmts = document.enter.visitorcount.value;
    var i = document.enter.visitorcount.value;
    var cmts2 = cmts;
var myParent;
var myDiv;
    cmts2++;
    document.enter.visitorcount.value = cmts2;
    var newVisitor = "";
    newVisitor += "<hr width=100>\n" +
                  "<table>\n" +
                  "<tr><td colspan=3>First Name:&nbsp;&nbsp;<input type=text name=visitorfirstname" + i + " size=20 maxlength=45>&nbsp&nbsp;Last Name:&nbsp&nbsp;<input type=text name=visitorlastname" + i + " size=20 maxlength=45></td></tr>\n" +
                  "<tr><td colspan=3>Affiliation:&nbsp;&nbsp;<input type=text name=visitoraffiliation" + i + " size=40 maxlength=60 value=><font>(organization or employer)</font></td></tr>\n" +
                  "<tr><td colspan=3>Destination:&nbsp;&nbsp;<select name='destination" + i + "'><option value='DOE Hillshire Facility'>DOE Hillshire Facility</option><option value='M&O Facilities'>M&O Facilities</option><option value='Lead Lab Facilities'>Lead Lab Facilities</option></select>" +
                  "<input type='checkbox' name='otherbox" + i + "' onClick='javascript:otherChecked(" + i + ");'><input type='hidden' name='otherchecked" + i + "' value=''>Other:&nbsp;<input type=text name='destinationother" + i + "' size='25' style='display:none'></td></tr>" +
                  "<tr><td>US Citizen?&nbsp;<input type=checkbox name=iscitizen" + i + " value=T></td>\n" +
                  "<td width=10><li></td>\n" + 
                  "<td>Requires&nbsp;&nbsp;(Escort <input type=radio name=escortorproxy" + i + " value=0 checked>)&nbsp;&nbsp;or&nbsp;&nbsp;(Visitor Badge <input type=radio name=escortorproxy" + i + " value=1>)</td></tr>\n" + 
                  "</table>\n";

    mySection = document.getElementById('addnewvisitor');
    
    myParent = document.getElementById('addnewvisitortable');
    myDiv = document.createElement("DIV");
    myParent.insertBefore(myDiv, mySection);
    myDiv.innerHTML = newVisitor;
}

function otherChecked (number) {

   if (document.getElementById('otherbox'+number+'').checked==true){
       document.getElementById('destinationother'+number+'').style.display="inline";
       document.getElementById('destination'+number+'').style.display="none";
       document.getElementById('otherchecked'+number+'').value = "true";
       }
       else{
          document.getElementById('destinationother'+number+'').style.display="none";
          document.getElementById('destination'+number+'').style.display="inline";
          }
 }
