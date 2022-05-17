/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 */

package ch.bfh.ti.academia.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The class LoggingFilter is used to log HTTP requests and their response status.
 */
@WebFilter(urlPatterns = "/api/*")
public class LoggingFilter implements Filter {

	private static final Logger logger = Logger.getLogger(LoggingFilter.class.getName());

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String message = httpRequest.getMethod() + " " + httpRequest.getRequestURI();
		if (httpRequest.getQueryString() != null) {
			message += "?" + httpRequest.getQueryString();
		}
		logger.info("Receiving request: " + message);
		chain.doFilter(request, response);
		logger.info("Returning response status: " + httpResponse.getStatus());
	}

	@Override
	public void destroy() {
	}
}
