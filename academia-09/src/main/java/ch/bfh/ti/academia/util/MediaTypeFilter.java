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

/**
 * The class MediaTypeFilter is used to check the media type of HTTP requests.
 */
@WebFilter(urlPatterns = "/api/*")
public class MediaTypeFilter implements Filter {

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String acceptHeader = httpRequest.getHeader("Accept");
		if (acceptHeader != null && !acceptHeader.contains("*/*") && !acceptHeader.contains("application/json")) {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		if (httpRequest.getMethod().equals("PUT")) {
			if (request.getContentType() == null || !request.getContentType().contains("application/json")) {
				httpResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
