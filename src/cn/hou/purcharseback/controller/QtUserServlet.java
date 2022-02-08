package cn.hou.purcharseback.controller;

import cn.hou.purcharseback.pojo.QtUser;
import cn.hou.purcharseback.service.UserService;
import cn.hou.purcharseback.service.impl.UserServiceImp;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Random;

@WebServlet(name = "QtUserServlet", urlPatterns = "/qt/QtUserServlet")
public class QtUserServlet extends HttpServlet {
    private static String codeChars = "1234567890abcdefghijklmnopqrstuvwxyz";
    private UserService userService=new UserServiceImp();
    private static Color getRandomColor(int minColor, int maxColor){
        Random random = new Random();
        if(minColor>255)
            minColor = 255;
        if(maxColor>255)
            maxColor = 255;
        int red = minColor + random.nextInt(maxColor - minColor);
        int green = minColor + random.nextInt(maxColor - minColor);
        int blue = minColor + random.nextInt(maxColor - minColor);
        return new Color(red,green,blue);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bs=request.getParameter("bs");
        if ("validate".equals(bs)){
            generalCode(request,response);
        }
        if ("register".equals(bs)){
            registeredUsers(request,response);
        }
        if ("login".equals(bs)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");
        String code = request.getParameter("validate");
        HttpSession session = request.getSession();
        String validationCode = (String) session.getAttribute("validation_code");
        if (username==null||username==""||pwd==null||pwd==""){
            request.setAttribute("errinfo","用户名及密码不能空");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (!code.trim().equals(validationCode)){
            request.setAttribute("errinfo","验证码错误");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        QtUser user = new QtUser( username, pwd,null,null);
        //执行登录的业务逻辑
        QtUser user1 = userService.login(user);
        if (user1==null){
            //重新登录
            request.setAttribute("errinfo","用户名及密码不正确");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //登录成功
        request.getSession().setAttribute("user",user1);
        Cookie ck_username=new Cookie("username", user1.getUsername());
        Cookie ck_password=new Cookie("password", user1.getPassword());
        ck_username.setMaxAge(60*60*24*7);
        ck_password.setMaxAge(60*60*24*7);
        ck_username.setPath("/");
        ck_password.setPath("/");
        response.addCookie(ck_username);
        response.addCookie(ck_password);
        try {
            response.sendRedirect("../qiantai/flow.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registeredUsers(HttpServletRequest request, HttpServletResponse response) {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String email=request.getParameter("email");
        String qq=request.getParameter("qqcode");
        QtUser user=new QtUser(username,password,email,qq);
        //先检查数据库用是否有相同name，如果没有则注册
        try {
            boolean userNameis = userService.findUserName(username);
            if (!userNameis){
                request.setAttribute("errinfo","该用户名已被注册");
                try {
                    request.getRequestDispatcher("../qiantai/register.jsp").forward(request,response);
                    return;
                } catch (ServletException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //调用查询数据库方法 得到QtUser 如果不为空就返回注册页面提示用户名已经注册，最好进行数据回显提高用户体验

        //注册 通过数据库往里面添加数据
            userService.Register(user);
        //添加成功直接转跳到登录页面
        try {
            response.sendRedirect("../qiantai/login.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        doGet(request, response);
    }





    private void generalCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得验证码集合的长度
        int charsLength = codeChars.length();
        //下面3条是关闭客户端浏览器的缓冲区
        response.setHeader("ragma", "No-cache");
        response.setHeader("Cach-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //设置图形验证码的长宽
        int width = 90, height = 20;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();//获得输出文字的graphics对象
        Random random = new Random();
        g.setColor(getRandomColor(180, 250));//背景颜色
        g.fillRect(0, 0, width, height);
        //设置初始字体
        g.setFont(new Font("Times New Roman",Font.ITALIC,height));
        g.setColor(getRandomColor(120, 180));//字体颜色
        StringBuilder validationCode = new StringBuilder();
        //验证码的随机字体
        String[] fontNames = {"Times New Roman","Book antiqua","Arial"};
        //随机生成3-5个验证码
        for (int i = 0; i < 3+random.nextInt(3); i++) {
            //随机设置当前验证码的字符的字体
            g.setFont(new Font(fontNames[random.nextInt(3)],Font.ITALIC,height));
            //随机获得当前验证码的字符
            char codeChar = codeChars.charAt(random.nextInt(charsLength));
            validationCode.append(codeChar);
            //随机设置当前验证码字符的颜色
            g.setColor(getRandomColor(10, 100));
            //在图形上输出验证码字符，x y随机生成
            g.drawString(String.valueOf(codeChar), 16*i+random.nextInt(7), height-random.nextInt(6));
        }
        //获得session对象
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(5*60);
        //将验证码保存在session对象中，key为validation_code
        session.setAttribute("validation_code", validationCode.toString());
        g.dispose();
        OutputStream os = response.getOutputStream();
        ImageIO.write(image,"JPEG",os);//以JPEG格式向客户端发送图形验证码
    }



}