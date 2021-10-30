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
import org.todo.model.user.UserAdmin;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/login"})

public class userAdminLogin extends HttpServlet{

    UserAdmin userAdmin = UserAdmin.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String registerButton = request.getParameter("register");
        if(registerButton != null){
            request.setAttribute("userAdmin", userAdmin);
            request.getRequestDispatcher("/register").forward(request, response);
        }

    }

}
