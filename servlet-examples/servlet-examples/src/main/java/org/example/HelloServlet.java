package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			name = "World";
		}
		String greeting = "Hello " + name + "!";
		response.setContentType("text/html");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("    <meta charset='utf-8'>");
			out.println("    <title>Hello Servlet</title>");
			out.println("    <link rel='stylesheet' href='styles.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("    <h1>" + greeting + "</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}
}
