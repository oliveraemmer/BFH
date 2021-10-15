package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hello-controller")
public class HelloController extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			name = "World";
		}
		String greeting = "Hello " + name + "!";
		request.setAttribute("greeting", greeting);
		request.getRequestDispatcher("/hello-view").forward(request, response);
	}
}
