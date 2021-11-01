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
import org.todo.model.user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/todoServ")

public class todoListServlet extends HttpServlet{
    //private static final TodoList todoList = new TodoList();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        TodoList todoList = loggedUser.getTodoList();
        request.setAttribute("todoList", todoList);
        request.getRequestDispatcher("/todo.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        TodoList todoList = loggedUser.getTodoList();

        if(request.getParameter("logout") != null){
            session.invalidate();
            request.getRequestDispatcher("/login").forward(request, response);
        }
        if(request.getParameter("entry") != null) {
            String title = request.getParameter("title");
            String category = request.getParameter("category");
            String dueDate = request.getParameter("dueDate");
            LocalDate localDate = LocalDate.parse(dueDate);

            Todo todo = new Todo(title, category, localDate);
            todoList.addTodo(todo);

            request.setAttribute("todoList", todoList.getTodos());
        } else {
            request.setAttribute("todoList", todoList.getTodos());
        }
        request.getRequestDispatcher("todo.jsp").forward(request, response);
    }

}
