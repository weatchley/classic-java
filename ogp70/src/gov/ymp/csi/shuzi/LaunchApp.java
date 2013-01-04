package gov.ymp.csi.shuzi;


/**
 * Launches App (Shuzi App wrapper) 
 * extends Launch implements AppDef.
 * 
 * <p>
 * <ul>
 * <li>Req. 3.1.1: user authentication/permission<br>
 * <li>Req. 3.1.2: data connection for the content (or to List obj?)<br>
 * <li>Req. 3.1.3: trigger the creation of GUI<br>
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */	

public class LaunchApp extends Launch implements AppDef {
	//private boolean cFlag = false;
	private boolean lStatus = false;
	private String response = "";
	
	public LaunchApp( long lType, String pId, String pName, String uId, String uIP )
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
			execApp(values);
		}
		UpdateLog ul = new UpdateLog();	//creates UpdateLog Class Constructor
		UpdateLog.setLogs(_lType, 
				lStatus, 
				_pId, 
				_pName, 
				_uId, 
				_uIP);		
	}
	
	public String execApp( String [][] values )
	{
		return response;
	}	
}
