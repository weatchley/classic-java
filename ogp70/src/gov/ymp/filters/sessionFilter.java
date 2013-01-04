package gov.ymp.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

/**
* sessionFilter is a component for winzi session manager.
* 
* 1. keeps track of session-related user actions
* 2. generates gateway web-use statistics 
*     
* @author   S. Higashi
*/

public class sessionFilter implements Filter
{
	private FilterConfig filterConfig;
	public void doFilter(final ServletRequest request,final ServletResponse response, FilterChain chain)
			throws IOException,ServletException
			{
			chain.doFilter(request,response);
			}
	public FilterConfig getFilterConfig()
	{
		return filterConfig;
	}
	public void init(FilterConfig config){
		this.filterConfig = config;
	}
	public void destroy(){
		this.filterConfig = null;
	}
}