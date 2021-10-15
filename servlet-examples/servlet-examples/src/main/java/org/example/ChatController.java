package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/chat")
public class ChatController extends HttpServlet {

	private static final List<ChatMessage> messages = new ArrayList<>();

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String text = request.getParameter("message");
		messages.add(new ChatMessage(text));
		request.setAttribute("messages", messages);
		request.getRequestDispatcher("/chat.jsp").forward(request, response);
	}
}
