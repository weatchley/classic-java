package gov.ymp.csi.shuzi;

/**
 * Launches Service (Shuzi Service wrapper) 
 * extends Launch implements ServDef.
 * 
 * <p>
 * <ul>
 * <li>Req. 4.1.1: user authentication/permission<br>
 * <li>Req. 4.1.2: data connection for the content  (or to List obj?)<br>
 * <li>Req. 4.1.3: trigger the creation of GUI<br>
 * 
 * @author Shuhei Higashi
 * @version 0.1

 */	

public class LaunchServ extends Launch implements ServDef {
	//private boolean cFlag = false;
	private boolean lStatus = false;
	private String response = "";
	
	public LaunchServ( long lType, String pId, String pName, String uId, String uIP )
	{
		super(lType,pId,pName,uId,uIP);
	 }
		
	public void doLaunch(){
		lStatus = checkPerm(_uId, _pId);
		if (lStatus){
			execPFrame();
			String[][] values = {
				{ _pName,	//URL
				_pId }
			};	
			execServ(values);
		}
		//UpdateLog ul = new UpdateLog();	//creates UpdateLog Class Constructor
		UpdateLog.setLogs(_lType, 
				lStatus, 
				_pId, 
				_pName, 
				_uId, 
				_uIP);	
	}
	
	public String execServ( String [][] values )
	{
		return response;
	}	

}
