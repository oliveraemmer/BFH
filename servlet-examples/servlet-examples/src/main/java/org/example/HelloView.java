package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello-view")
public class HelloView extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String greeting = (String) request.getAttribute("greeting");
		response.setContentType("text/html");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("    <meta charset='utf-8'>");
			out.println("    <title>Hello View</title>");
			out.println("    <link rel='stylesheet' href='styles.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("    <h1>" + greeting + "</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}
}
