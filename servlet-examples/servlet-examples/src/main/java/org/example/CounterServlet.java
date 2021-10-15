package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/counter")
public class CounterServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer counter = (Integer) session.getAttribute("counter");
		if (counter == null) counter = 0;
		String url = response.encodeURL("counter");
		response.setContentType("text/html");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("    <title>Counter Servlet</title>");
			out.println("    <link rel='stylesheet' href='styles.css'>");
			out.println("</head>");
			out.println("<body>");
			out.println("    <h1>Counter Servlet</h1>");
			out.println("    <form action='" + url + "' method='post'>");
			out.println("        <label>Counter: " + counter + "</label>");
			out.println("        <input type='submit' value='Increment'/>");
			out.println("    </form>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Integer counter = (Integer) session.getAttribute("counter");
		if (counter == null) counter = 0;
		counter = counter + 1;
		session.setAttribute("counter", counter);
		doGet(request, response);
	}
}
