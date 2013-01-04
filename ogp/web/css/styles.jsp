<%
	String bgColor = "#ffffff"; 
	//String bgColor = "#cddbeb"; //#cddbeb for boys #ffccff for girls
	String bgImgURL = "../graphics/bg4.gif";
	//String bgImgURL = "../graphics/bg3.jpg";
	//String bgImgURL = "../graphics/bg3.gif";
	//String bgImgURL = "../../upload/shuziBG/bg2.gif";
	String winBorderColor = "#ffffff";
	String winFontColor = "#000000";
	String winHeadFontColor = "#ffffff";
%>
body {
    
    SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
    SCROLLBAR-SHADOW-COLOR: #cccccc;
    SCROLLBAR-FACE-COLOR: #ffffff;
    SCROLLBAR-3DLIGHT-COLOR: #ffffff;
    SCROLLBAR-DARKSHADOW-COLOR: #ffffff;
/*
    SCROLLBAR-FACE-COLOR: #6699CC;
    SCROLLBAR-3DLIGHT-COLOR: #669999;
    SCROLLBAR-DARKSHADOW-COLOR: #9999CC;
*/
    SCROLLBAR-ARROW-COLOR: silver;
    SCROLLBAR-TRACK-COLOR: silver;
    color: #000000;
	background-color : <%=bgColor%>; 
	background-image: url("<%=bgImgURL%>");
	height: 100%; overflow: auto; 
	background-repeat:repeat;
}

.sBox1 {
	color:#666666;
	background-color:#ffffff;
	/*
	color:#3c6076 ; 
    background-color: #e8f5ff;
    */
    font-family: verdana,Geneva, Arial, Sans-serif; 
    font-size: 12px; 
	border: #000000; border-style:hidden; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 0px; border-left-width: 0px;width:150px;
	/*
	display:none;
	visibility:hidden;
	*/
	display:block;
}
.sBox2 {
	color:#666666;
	background-color:#ffffff;
	/*
	color:#666600;
	background-color:#ECD553;
	*/
	font-family: verdana,Geneva, Arial, Sans-serif; 
	font-size: 12px; border: #000000; border-style:hidden; 
	border-top-width: 0px; border-right-width: 0px; 
	border-bottom-width: 0px; border-left-width: 0px;width:150px; 
	/*
	display:none;
	visibility:hidden;
	*/
	display:block;
}
.sBox3 {
	color:#666666;
	background-color:#ffffff;
	/*
	color:#D7FFFF;
	background-color:#73B9B9;
	*/
	font-family: verdana,Geneva, Arial, Sans-serif; 
	font-size: 12px; border: #000000; 
	border-style:hidden; border-top-width: 0px; 
	border-right-width: 0px; border-bottom-width: 0px; 
	border-left-width: 0px;width:150px; 
	/*
	display:none;
	*/
	display:block;
}

.nText {
	FONT-WEIGHT:lighter; FONT-SIZE: 11px; 
	/* COLOR:#4e6790; */ 
	COLOR:#666666;
	LEFT: 10px; TOP: 0px; HEIGHT: 14px; WIDTH: 200px;  BACKGROUND-COLOR: transparent; 
}

.bText {
	FONT-WEIGHT: bold; FONT-SIZE: 11px; COLOR:#4e6790; LEFT: 10px; TOP: 0px; HEIGHT: 14px; WIDTH: 65px;  BACKGROUND-COLOR: transparent; 
}

.clCMEvent {
	Z-INDEX: 300; LEFT: 0px; VISIBILITY: hidden; WIDTH: 100%; CLIP: rect(0px 100% 100% 0px); POSITION: absolute; TOP: 0px; HEIGHT: 100%
}
.clCMAbs {
	LEFT: 0px; VISIBILITY: hidden; WIDTH: 10px; POSITION: absolute; TOP: 0px; HEIGHT: 10px
}
.clT {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clT {
	PADDING-RIGHT: 4px; PADDING-LEFT: 4px; FONT-WEIGHT: bold; FONT-SIZE: 10px; PADDING-BOTTOM: 4px; PADDING-TOP: 4px; 
}
.clT {
	COLOR: white; background-color:#999999;
}
.clTover {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clTover {
	PADDING-RIGHT: 4px; PADDING-LEFT: 4px; FONT-WEIGHT: bold; FONT-SIZE: 10px; PADDING-BOTTOM: 4px; PADDING-TOP: 4px
}
.clTover {
	BACKGROUND-COLOR: transparent; layer-background-color: #6699CC
}
.clTover {
	COLOR: #ECD553
}
.clT2 {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clT2 {
	PADDING-RIGHT: 4px; PADDING-LEFT: 4px; FONT-WEIGHT: bold; FONT-SIZE: 12px; PADDING-BOTTOM: 4px; PADDING-TOP: 4px
}
.clT2 {
	COLOR: white; 
	background-color:#6699FF; 
}
.clTover2 {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clTover2 {
	PADDING-RIGHT: 4px; PADDING-LEFT: 4px; FONT-WEIGHT: bold; FONT-SIZE: 12px; PADDING-BOTTOM: 4px; PADDING-TOP: 4px
}
.clTover2 {
	BACKGROUND-COLOR: transparent; 
	layer-background-color: #6699CC;
}
.clTover2 {
	COLOR: #ECD553;
}
.clB2 {
	BACKGROUND-COLOR: transparent; layer-background-color: transparent;
}
.clS {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clS {
	PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-WEIGHT: bold; FONT-SIZE: 11px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
.clS {
	COLOR: #006699; BACKGROUND-COLOR: #cddbeb; layer-background-color: #cddbeb
}
.clS2 {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clSover {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clSover {
	PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-WEIGHT: bold; FONT-SIZE: 11px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
.clSover {
	COLOR: #ECD553
}
.clSover {
	BACKGROUND-COLOR: #6699CC; layer-background-color: #6699CC
}
.clS2over {
	COLOR: #ECD553
}
.clS2over {
	BACKGROUND-COLOR: #6699CC; layer-background-color: #6699CC
}
.clS2over {
	OVERFLOW: hidden; WIDTH: 130px; CURSOR: pointer; POSITION: absolute; HEIGHT: 25px
}
.clS2over {
	PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 11px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
.clS2 {
	PADDING-RIGHT: 2px; PADDING-LEFT: 2px; FONT-SIZE: 11px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px
}
.clS2 {
	COLOR: #006699; BACKGROUND-COLOR: #cddbeb; layer-background-color: #CDDBEB
}
.clSb {
	COLOR: #006699; BACKGROUND-COLOR: #cddbeb; layer-background-color: #cddbeb
}
.clS2b {
	COLOR: #006699; BACKGROUND-COLOR: #cddbeb; layer-background-color: #cddbeb
}
.clB {
	/* BACKGROUND-COLOR: #6699CC; layer-background-color: #6699CC */
	BACKGROUND-COLOR: #999999; layer-background-color: #999999
}
.clB {
	Z-INDEX: 300; VISIBILITY: hidden; WIDTH: 150px; HEIGHT: 20px
}
.clBar {
	BACKGROUND-COLOR: transparent; layer-background-color: #6699CC
}
.clBar {
	VISIBILITY: hidden; WIDTH: 10px; HEIGHT: 10px
}
#diva {
	Z-INDEX: 1000; COLOR: white; TOP: 300px; BACKGROUND-COLOR: red; layer-background-color: red
}
DIV {
	POSITION: absolute
}
TD {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
P {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
B {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
INPUT {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
DIV {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
A {
	COLOR: navy; TEXT-DECORATION: none
}
A:hover {
	TEXT-DECORATION: underline
}
.clWin {
	Z-INDEX: 2; VISIBILITY: hidden; POSITION: absolute; BACKGROUND-COLOR: #ccc;
/*
make this transparent to get a transparent window...
BACKGROUND-COLOR: #ccc;
BACKGROUND-COLOR: #6699CC; 
layer-background-color: #6699CC; 
*/
}
.clWindow {
	Z-INDEX: 15; LEFT: 1px; OVERFLOW: hidden; WIDTH: 250px; TOP: 14px; 
	BACKGROUND-COLOR: white;
/*	
make this transparent to get a transparent window...
BACKGROUND-COLOR: white;
layer-background-color: white; 
*/
}
.clWinTabContainer {
	Z-INDEX: 30;
	BACKGROUND-COLOR: transparent;
	HEIGHT: 18px;
	WIDTH:565px;CLIP: rect(0px 565px 18px 0px);
	/*
	 WIDTH:565px;CLIP: rect(0px 565px 18px 0px);
	*/
}
.clWinTabTbl{
	/* display:none; */
	/* change above attr to turn tabs on and off  */
}
.clWinTabTb {
	 height:18px; font-size:10px; 
	 font-family:Arial, Helvetica, sans-serif;border-right-width:1px;border-right-style:solid;
	 border-bottom-width:1px;border-bottom-color:<%=winBorderColor%>;border-bottom-style:solid;
	 border-top-width:1px;border-top-color:<%=winBorderColor%>;border-top-style:solid;
	 width:175px;text-align: center;BACKGROUND-COLOR:#ccc;color:transparent;
	 color:<%=winFontColor%>;border-color:<%=winBorderColor%>;	 border-right-color:<%=winBorderColor%>;
	 /*
	 border-color:transparent;
	 border-right-color:transparent;
	 */
}
.clWinResize {
	Z-INDEX: 30; WIDTH: 26px;  HEIGHT: 18px; CLIP: rect(0px 26px 18px 0px);
	BACKGROUND-COLOR: transparent;
}
.clWinHead {
	FONT-WEIGHT: bold; FONT-SIZE: 11px; COLOR: <%=winHeadFontColor%>; LEFT: 20px; TOP: 0px; HEIGHT: 14px; WIDTH: 200px;  
	BACKGROUND-COLOR: transparent; 
}
.clText {
	FONT-SIZE: 11px; Z-INDEX: 50; LEFT: 2px; TOP: 2px; BACKGROUND-COLOR: transparent; 
}
clFrameDiv{
	Z-INDEX: 49; LEFT: 2px; TOP: 25px; BACKGROUND-COLOR: transparent; 
}
.clUp {
	Z-INDEX: 60; WIDTH: 12px; HEIGHT: 12px; BACKGROUND-COLOR: #eef3f9; layer-background-color: #EEF3F9
}
.clDown {
	Z-INDEX: 60; WIDTH: 12px; HEIGHT: 12px; BACKGROUND-COLOR: #eef3f9; layer-background-color: #EEF3F9
}
.clHeading {
	FONT-WEIGHT: bold; FONT-SIZE: 20px; COLOR: black
}
.clHeading2 {
	FONT-WEIGHT: bold; FONT-SIZE: 13px; COLOR: white
}
.clWinHeading {
	FONT-WEIGHT: bold; FONT-SIZE: 15px; COLOR: black
}
.clWinBy {
	FONT-SIZE: 10px; COLOR: #777777
}
.clWinContent {
	BACKGROUND-COLOR: transparent; 
}
.clWinText {
	COLOR: black
}
.clWinText2 {
	PADDING-LEFT: 25px; COLOR: black
}
A.clWinLink {
	FONT-SIZE: 12px; FONT-FAMILY: arial,helvetica
}
A.clLinkSmall {
	FONT-SIZE: 10px
}
.clInSearch {
	WIDTH: 110px
}
.clInput {
	FONT-SIZE: 11px; BACKGROUND-COLOR: #cddbeb
}
#divLogo {
	Z-INDEX: 890; LEFT: 8px; POSITION: absolute; TOP: 5px
}
PRE {
	FONT-SIZE: 11px; COLOR: red; FONT-FAMILY: Courier new
}
CODE {
	FONT-SIZE: 12px; COLOR: red; FONT-FAMILY: Courier new
}
.clItems {
	VISIBILITY: hidden; POSITION: absolute
}
A.clM {
	FONT-WEIGHT: bold; COLOR: white; TEXT-DECORATION: none
}
A.clM:hover {
	COLOR: #ECD553; TEXT-DECORATION: none
}
#divMode {
	VISIBILITY: hidden; WIDTH: 80px; HEIGHT: 5px
}
.clMode {
	FONT-WEIGHT: bold; FONT-SIZE: 10px; COLOR: #6699CC
}
.clLine {
	Z-INDEX: 1; OVERFLOW: hidden; WIDTH: 100%; CLIP: rect(0px 100% 1px 0px); POSITION: relative; HEIGHT: 1px; BACKGROUND-COLOR: #ECD553; layer-background-color: #ECD553
}
.clErrTxt {
	FONT-SIZE: 9px; COLOR: red
}
#divTemp {
	Z-INDEX: 890; LEFT: 8px; WIDTH: 185px; POSITION: absolute; TOP: 84px; BACKGROUND-COLOR: silver
}
/*
.blogtext {
	PADDING-LEFT: 25px; LINE-HEIGHT: 16px
}
.blogtitle {
	FONT-SIZE: 14px
}
.blogdate {
	COLOR: #6699CC
}
*/









