package cn.hou.purcharseback.controller;

import cn.hou.purcharseback.pojo.User;
import cn.hou.purcharseback.service.UserService;
import cn.hou.purcharseback.service.impl.UserServiceImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet",urlPatterns = "/user/UserServlet")
public class UserServlet extends HttpServlet {
    private UserService userService=new UserServiceImp();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("user");
        String pwd = req.getParameter("pwd");
        if (username==null||username==""||pwd==null||pwd==""){
            req.setAttribute("errinfo","用户名及密码不能空");
            req.getRequestDispatcher("/houtai/index.jsp").forward(req,resp);

        }
        User user = new User(null, username, pwd);
        //执行登录的业务逻辑
        User user1 = userService.login(user);
        if (user1==null){
            //重新登录
            req.setAttribute("errinfo","用户名及密码不正确");
            req.getRequestDispatcher("/houtai/index.jsp").forward(req,resp);
        }
        //登录成功
        req.getSession().setAttribute("user",user1);
        Cookie ck_username=new Cookie("username", user1.getUsername());
        Cookie ck_password=new Cookie("password", user1.getPassword());
        ck_username.setMaxAge(60*60*24*7);
        ck_password.setMaxAge(60*60*24*7);
        ck_username.setPath("/");
        ck_password.setPath("/");
        resp.addCookie(ck_username);
        resp.addCookie(ck_password);
        resp.sendRedirect("../houtai/main.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doGet(req, resp);
    }
}