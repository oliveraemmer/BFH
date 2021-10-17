package org.todo.model;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.todo.model.todo.Todo;
import org.todo.model.todo.TodoList;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/todoServ2")

public class todoListServlet2 extends HttpServlet{
    private static final TodoList todoList = new TodoList();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = response.encodeURL("todoServ2");
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("   <meta charset=\"UTF-8\">");
            out.println("    <title>todoList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("    <form action='" + url + "' method='post'>");
            out.println("       <label>Add ToDo Entry:</label>");
            out.println("       <label>Title: </label>");
            out.println("       <input type=\"text\" name=\"title\" required><br>");
            out.println("       <label>Category: </label>");
            out.println("       <input type=\"text\" name=\"category\" required><br>");
            out.println("       <label>Due Date: </label>");
            out.println("       <input type=\"date\" name=\"dueDate\" required><br>");
            for (int i=todoList.getTodos().size(); i > 0; i--){
                out.println("        <p>todoList.get(i).title<br>" +
                        "            todoList.get(i).category<br>" +
                        "            todoList.get(i).dueDate5</p>");
            }
            out.println("    </form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String dueDate = request.getParameter("dueDate");
        LocalDate localDate = LocalDate.parse(dueDate);

        Todo todo = new Todo(title, category, localDate);

        todoList.addTodo(todo);

        request.setAttribute("todoList", todoList.getTodos());
        doGet(request, response);
    }

}
