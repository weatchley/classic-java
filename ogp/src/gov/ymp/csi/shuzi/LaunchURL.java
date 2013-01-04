package gov.ymp.csi.shuzi;

/**
 * Launches URL (Shuzi URL wrapper) 
 * extends Launch implements UrlDef.
 * 
 * <p>
 * <ul>
 * <li>Req. 5.1: Captures the URLs user accesses<br>
 * <li>Req. 5.2: calls updateLog() class<br>
 * <li>Req. 5.3: Launches Web Browser within Shuzi Framework<br>
 *  
 * @author Shuhei Higashi
 * @version 0.1

 */	

public class LaunchURL extends Launch implements UrlDef {
	//private boolean cFlag = false;
	private boolean lStatus = false;
	private String response = "";
	
	public LaunchURL( long lType, String pId, String pName, String uId, String uIP ) //constructor
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
		execURL(values);
		}
		//UpdateLog ul = new UpdateLog();	//creates UpdateLog Class Constructor
		UpdateLog.setLogs(_lType, 
				lStatus, 
				_pId, 
				_pName,  //URL for launchURL component
				_uId, 
				_uIP);		
	}
	
	public String execURL( String [][] values )
	{
		return response;
	}		
}
