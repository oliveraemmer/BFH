package org.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.todo.controller.ObjectMapperFactory;

@WebServlet(urlPatterns = {"/api/todos"})

public class TodoListServlet extends HttpServlet{

    private static TodoList todoList = new TodoList();
    private LocalDate localDate = LocalDate.parse("2021-11-20");
    private Todo todo1 = new Todo("test1", "category1", localDate);
    private Todo todo2 = new Todo("test2", "category1", localDate);
    private Todo todo3 = new Todo("test3", "category2", localDate);
    private String json = "";
    private String category = "";
    private List<Todo> jsonTodos = new ArrayList<Todo>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Initialize todoList
        if(todoList.getTodos().size() < 1){
            todoList.addTodo(todo1);
            todoList.addTodo(todo2);
            todoList.addTodo(todo3);
        }

        category = request.getParameter("category");
        if(category.isEmpty()){
            jsonTodos = todoList.getTodos();
            System.out.println(todoList.getTodos().get(0));
        } else {
            for(int i = 0; i < todoList.getTodos().size(); i++){
                if(todoList.getTodos().get(i).getCategory() == category){
                    jsonTodos.add(todoList.getTodos().get(i));
                    System.out.println(todoList.getTodos().get(i));
                }
            }
        }

        ObjectMapper om = ObjectMapperFactory.createObjectMapper();

        for (int i = 0; i < jsonTodos.size(); i++) {
            if (json.isEmpty()) {
                json = om.writeValueAsString(jsonTodos.get(i));
            } else {
                json += om.writeValueAsString(jsonTodos.get(i));
            }
            json += "\n";
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

}
