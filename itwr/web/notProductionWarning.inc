
<!-- begin production status waring ************************************************************************************************ -->
<%
Context myInitCtx = new InitialContext();
String MyProductionStatus = (String) myInitCtx.lookup("java:comp/env/ProductionStatus");
%>
<% if (!MyProductionStatus.equals("prod")) { %>

<div id="FloatProdStatusBox" style="position: absolute;
                           width: 180px;
                           hight: 50px;
                           border-width: 0;
                           background-color:#FFFFFF;
                           cursor:pointer;
                           filter:alpha(opacity=50);
                           -moz-opacity:.50;
                           opacity:.50;" onClick="hideFloatProdStatusBox(this)">
<p style="color:#FF0000;font-weight:900;font-size:150%;" align=center>Warning!<br>This is set up for development/review.</p>
</div>
<script>
var hX = -200;
var vY = 10;

/* Portions of this are from: Floating Mail-This-Link Script C.2004 by CodeLifter.com */
/* Used by permission from javascripts.com */
var nn=(navigator.appName.indexOf("Netscape")!=-1);
var dD=document,dH=dD.html,dB=dD.body,px=dD.layers?'':'px';
function floatProdStatusBox(iX,iY,id){
    var L=dD.getElementById?dD.getElementById(id):dD.all?dD.all[id]:dD.layers[id];
    this[id+'O']=L;if(dD.layers)L.style=L;L.nX=L.iX=iX;L.nY=L.iY=iY;
    L.P=function(x,y){this.style.left=x+px;this.style.top=y+px;};L.Fm=function(){var pX, pY;
    pX=(this.iX >=0)?0:nn?innerWidth:nn&&dH.clientWidth?dH.clientWidth:dB.clientWidth;
    pY=nn?pageYOffset:nn&&dH.scrollTop?dH.scrollTop:dB.scrollTop;
    if(this.iY<0)pY+=nn?innerHeight:nn&&dH.clientHeight?dH.clientHeight:dB.clientHeight;
    this.nX+=.1*(pX+this.iX-this.nX);this.nY+=.1*(pY+this.iY-this.nY);this.P(this.nX,this.nY);
    setTimeout(this.id+'O.Fm()',33);};
    return L;
}
function hideFloatProdStatusBox(box) {
    box.style.display='none';
}
floatProdStatusBox(hX,vY,'FloatProdStatusBox').Fm();
</script>


<% } %>

<!-- end production status waring ************************************************************************************************ -->

