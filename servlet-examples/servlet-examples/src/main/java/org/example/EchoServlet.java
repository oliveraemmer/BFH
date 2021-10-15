package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet("/echo/*")
public class EchoServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("    <meta charset='utf-8'/>");
			out.println("    <title>Echo Servlet</title>");
			out.println("    <link rel='stylesheet' href='styles.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("    <h2>Request</h2>");
			out.println("    Request URL: " + request.getRequestURL() + "<br>");
			out.println("    Scheme: " + request.getScheme() + "<br>");
			out.println("    Server Name: " + request.getServerName() + "<br>");
			out.println("    Servlet Port: " + request.getServerPort() + "<br>");
			out.println("    <h2>Request Line</h2>");
			out.println("    Method: " + request.getMethod() + "<br>");
			out.println("    Request URI: " + request.getRequestURI() + "<br>");
			out.println("    Servlet Path: " + request.getServletPath() + "<br>");
			out.println("    Path Info: " + request.getPathInfo() + "<br>");
			out.println("    Query String: " + request.getQueryString() + "<br>");
			out.println("    Protocol: " + request.getProtocol() + "<br>");
			out.println("    <h2>Request Parameters</h2>");
			for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
				String name = e.nextElement();
				out.println("    " + name + ": " + request.getParameter(name) + "<br>");
			}
			out.println("    <h2>Request Headers</h2>");
			for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements(); ) {
				String name = e.nextElement();
				out.println("    " + name + ": " + request.getHeader(name) + "<br>");
			}
			out.println("</body>");
			out.println("</html>");
		}
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
