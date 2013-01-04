package gov.ymp.utils;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;
import java.util.*;
import javax.naming.*;
import gov.ymp.utils.GetRSSObj;

public class doScheduleRSS extends HttpServlet {  
	  
	Timer t;	
		
	protected void doGet (HttpServletRequest req, HttpServletResponse res) throws 
	ServletException, IOException {
		doPost(req,res);
	}	  
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws 
	ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("doScheduleRSS servlet!");
	}
	
	public void init(){
		RSSTTask task=new RSSTTask();
		Timer t=new Timer();
		t.scheduleAtFixedRate(task, new Date(), 1800000); //1000 = 1 sec., 60000 = 1 min., 1800000 = 30 mins
		//System.out.println("Executing doScheduleRSS servlet...(Every 30 minutes)");
	}
	
	int counter = 0;
	
	class RSSTTask extends TimerTask {
		private DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
		public void run() {
			try{
				Context initCtx = new InitialContext();
				String pathToRoot = (String) initCtx.lookup("java:comp/env/PathToRoot");
				String sUrl = "";
				String filePath = pathToRoot + "/cached-content/ogp-new/";
				String sPath = "";
				
				if(counter==0){				
					sUrl = "https://blogs.ocrwm.doe.gov/crest/feed/entries/rss";
					sPath = filePath + "thecrest.xml";
					counter++;
				}else if(counter==1){
					sUrl = "https://blogs.ocrwm.doe.gov/director/feed/entries/rss";
					sPath = filePath + "director.xml";
					counter++;
				}else if(counter==2){
					sUrl = "https://blogs.ocrwm.doe.gov/fyi/feed/entries/rss?cat=%2Fcurrent";
					sPath = filePath + "fyi.xml";
					counter=0;
				}
												
				GetRSSObj gro = new GetRSSObj();
				gro.save(sUrl,sPath);
				//System.out.println(formatter.format(new Date()));
				
			}catch(Exception e){
				//System.out.println("Exception -------->"+e);
			}
		}
	}
	
}
