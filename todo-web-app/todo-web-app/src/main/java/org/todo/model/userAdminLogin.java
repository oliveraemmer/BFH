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
import org.todo.model.user.InvalidCredentialsException;
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

@WebServlet(urlPatterns = {"/login"})

public class userAdminLogin extends HttpServlet{

    UserAdmin userAdmin = UserAdmin.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("loggedUser") == null) {
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/todoServ").forward(request, response);
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if(request.getParameter("register") != null){
            request.setAttribute("userAdmin", userAdmin);
            request.getRequestDispatcher("/register").forward(request, response);
        }
        if(request.getParameter("login") != null){
            boolean errorLogin = false;
            String user = request.getParameter("user");
            String password = request.getParameter("password");
            // Login User and create a session
            try{
                User loggedUser = userAdmin.loginUser(user, password);
                System.out.println("User logged in");
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", loggedUser);
            }
            // Return to login.jsp with errormessage if it didn't work
            catch (InvalidCredentialsException e) {
                e.printStackTrace();
                errorLogin = true;
                request.setAttribute("errorLogin", errorLogin);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

            request.getRequestDispatcher("/todoServ").forward(request, response);
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);

    }

}
