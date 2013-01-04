package gov.ymp.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import javax.servlet.Filter;
import java.io.IOException;
import javax.servlet.ServletException;
import java.util.*;
import org.apache.oro.text.*;
import org.apache.oro.text.regex.*;

public class IpFilter extends GenericFilter{
	private String rHost;
	private String rAddress;
	private String coastName;
	private String rCoast;
	public void doFilter(final ServletRequest request, final ServletResponse response,FilterChain chain)
	throws IOException, ServletException
	{
		//System.out.println("Entering FIlter");
		rHost = request.getRemoteHost();
		rAddress = request.getRemoteAddr();
		rCoast = coast2Coast(rHost, rAddress);
		request.setAttribute("rAddressString","your remote address is: "+ rAddress);
		request.setAttribute("rCoastString",rCoast);
		chain.doFilter(request,response);
		//System.out.println("Exiting Filter");
	}
	private String coast2Coast(String rHost, String rAddress){
		coastName = "west";
		//jakarta ORO pattern matching sequence		
		//rAddress = "132.172.196.106"; //east coast ip
		//rAddress = "204.140.46.39"; //west coast ip
		//System.out.println("remote address to be parsed: " + rAddress);
		String regexp="(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
		PatternCompiler compiler = new Perl5Compiler();
		PatternMatcher matcher=new Perl5Matcher();
        try {
			Pattern pattern=compiler.compile(regexp);
			if (matcher.contains(rAddress,pattern)) {
	            MatchResult result=matcher.getMatch();
	            //System.out.println(result.group(1));
	            //System.out.println(result.group(2));
	            //System.out.println(result.group(3));
	            //System.out.println(result.group(4));
	            coastName = ipFilter(result.group(1),result.group(2),result.group(3),result.group(4));
	        }else{
	        	//System.out.println("no patterns match...");
	        }
			return coastName;
		} catch (MalformedPatternException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "coast2COast failed";
		}				
	}
	
	private String ipFilter(String group1,String group2,String group3,String group4){
		//System.out.println("ipFilter called with: "+ group1 +", "+ group2 +", "+ group3 +", "+ group4);
		coastName = "west";
		int i = Integer.parseInt(group4);
		if(group1.equals("192")){
			if(group2.equals("84")&&group3.equals("216")&&group3.equals("0")){
				if(i>=0){
				coastName = "east";
				}
			}else if(group2.equals("245")&& group3.equals("237")&&group3.equals("0")){
				if(i>=0){
					coastName = "east";
					}
			}else if(group2.equals("168")&& group3.equals("144")&&group3.equals("0")){
				if(i>=0){
					coastName = "east";
					}
			}
		}else if(group1.equals("132")&&group2.equals("172")&&group3.equals("196")){
			if(i>=105){
				coastName = "east";
				}
		}
		return coastName;
	}
}

