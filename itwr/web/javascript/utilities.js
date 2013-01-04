// javascript Utilities
//
// 

// set up global hash
var global_hash = new Object;

//function to disable edit on readonly fields
function on_readonly (what) {
  if (navigator.appName == 'Netscape') {
    what.blur();
  }
}

// A utility function that returns true if a string contains only
// whitespace characters.
function isblank(s)
{
    if (s.length == 0) return true;
    for(var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if ((c != ' ') && (c != '\n') && (c != '\t') && (c !='\r')) return false;
    }
    return true;
}

// function that returns true if a string contains only numbers
function isnumeric(s)
{
    if (s.length == 0) return false;
    for(var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if ((c < '0') || (c > '9')) return false;
    }

    return true;
}

// funtion to show the properties of an object (used mostly for debuging)
function show_props(obj, obj_name) {
          var result = ""
          var col = 1;
          for (var i in obj)
             if (i != 'innerHTML' && i != 'innerText' && i != 'outerHTML' && i != 'outerText') {
                    if (col == 1) {
                        result += obj_name + "." + i + " = " + obj[i];
                        col = col + 1;
                    } else {
                        result += "          " + obj_name + "." + i + " = " + obj[i] + "\n<br>";
                        col = 1;
                    }
             }
          return result
}

// Routine to pop open a new window
function PopIt(location,name){ 
  popup = window.open(location,name,"height=500,width=700,scrollbars=yes");  
}

// function returns true if the given year is a leap year
function isleapyear(year)
  {
  var returnvalue = ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0); 
  return (returnvalue);  
  }

// function returns null string "" if a date is valid or an error string if it is invalid describing the
// first encountered reason that the date is invalid.
// pastdateok should be true if dates from the past are valid
// futuredateok should be true if dates from the future are valid
// fulldatetime should be true if the hour, minute, second, millisecond are to be considered when testing for past and future dates
// the year must be  a 4 digit year (0026 = 26 AD)
function validate_date(year, month, day, hour, minute, second, millisecond, pastdateok, futuredateok, fulldatetime) {
   var returnvalue = "";
   var testdate;
   var months = new Array('', "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
   if (year.length < 4) {
      return ("Entered year must have four digits");
   }
   if ((month < 1) || (month > 12)) {
      return ("Entered month is invalid");
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
            returnvalue = months[month] + " only has 30 days";
         }
         break;
      case 2:
      case "2":
      case "02":
         if (isleapyear(year)) {
            returnvalue = (day > 29) ? "February " + year + " only has 29 days" : "";
         } else {
            returnvalue = (day > 28) ? "February " + year + " only has 28 days" : "";
         }
         break;
      default:
         if (day > 31) {
            returnvalue = months[month] + " only has 31 days";
         }
   }
   if (returnvalue != "") { // return because the following is invalid if we've determined we have an invalid date.
      return (returnvalue);
   }
   testdate = new Date(year, month-1, day, hour, minute, second, millisecond);
   today = new Date();
   if (!(fulldatetime)) {
      today = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 0,0,0,0);
   }
   if ((!(pastdateok)) && (testdate.getTime() < today.getTime())) {
      returnvalue = "Please enter a date in the future";
   }
   if ((!(futuredateok)) && (testdate.getTime() > today.getTime())) {
      returnvalue = "Please enter a date in the past";
   }
   return (returnvalue);
}

