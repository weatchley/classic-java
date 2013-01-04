package gov.ymp.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;

public class GenericFilter implements Filter
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