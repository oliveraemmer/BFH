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
import org.todo.model.user.UserAdmin;
import org.todo.model.user.UserAlreadyExistsException;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/register")

public class userAdminRegister extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        boolean errorRegister = false;
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        UserAdmin userAdmin = (UserAdmin) request.getAttribute("userAdmin");
        // Try to register User
        try{User newUser = userAdmin.registerUser(user, password);}
        // Return to login.jsp if it didn't work
        catch (UserAlreadyExistsException e) {
            e.printStackTrace();
            errorRegister = true;
            request.setAttribute("errorRegister", errorRegister);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
        String registeredMessage = "User " + user + " registered\n";
        request.setAttribute("registeredMessage", registeredMessage);

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

}
