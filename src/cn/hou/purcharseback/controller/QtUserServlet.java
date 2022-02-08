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
            request.setAttribute("errinfo","�û��������벻�ܿ�");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (!code.trim().equals(validationCode)){
            request.setAttribute("errinfo","��֤�����");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        QtUser user = new QtUser( username, pwd,null,null);
        //ִ�е�¼��ҵ���߼�
        QtUser user1 = userService.login(user);
        if (user1==null){
            //���µ�¼
            request.setAttribute("errinfo","�û��������벻��ȷ");
            try {
                request.getRequestDispatcher("/qiantai/login.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //��¼�ɹ�
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
        //�ȼ�����ݿ����Ƿ�����ͬname�����û����ע��
        try {
            boolean userNameis = userService.findUserName(username);
            if (!userNameis){
                request.setAttribute("errinfo","���û����ѱ�ע��");
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

        //���ò�ѯ���ݿⷽ�� �õ�QtUser �����Ϊ�վͷ���ע��ҳ����ʾ�û����Ѿ�ע�ᣬ��ý������ݻ�������û�����

        //ע�� ͨ�����ݿ��������������
            userService.Register(user);
        //��ӳɹ�ֱ��ת������¼ҳ��
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
        //�����֤�뼯�ϵĳ���
        int charsLength = codeChars.length();
        //����3���ǹرտͻ���������Ļ�����
        response.setHeader("ragma", "No-cache");
        response.setHeader("Cach-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //����ͼ����֤��ĳ���
        int width = 90, height = 20;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();//���������ֵ�graphics����
        Random random = new Random();
        g.setColor(getRandomColor(180, 250));//������ɫ
        g.fillRect(0, 0, width, height);
        //���ó�ʼ����
        g.setFont(new Font("Times New Roman",Font.ITALIC,height));
        g.setColor(getRandomColor(120, 180));//������ɫ
        StringBuilder validationCode = new StringBuilder();
        //��֤����������
        String[] fontNames = {"Times New Roman","Book antiqua","Arial"};
        //�������3-5����֤��
        for (int i = 0; i < 3+random.nextInt(3); i++) {
            //������õ�ǰ��֤����ַ�������
            g.setFont(new Font(fontNames[random.nextInt(3)],Font.ITALIC,height));
            //�����õ�ǰ��֤����ַ�
            char codeChar = codeChars.charAt(random.nextInt(charsLength));
            validationCode.append(codeChar);
            //������õ�ǰ��֤���ַ�����ɫ
            g.setColor(getRandomColor(10, 100));
            //��ͼ���������֤���ַ���x y�������
            g.drawString(String.valueOf(codeChar), 16*i+random.nextInt(7), height-random.nextInt(6));
        }
        //���session����
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(5*60);
        //����֤�뱣����session�����У�keyΪvalidation_code
        session.setAttribute("validation_code", validationCode.toString());
        g.dispose();
        OutputStream os = response.getOutputStream();
        ImageIO.write(image,"JPEG",os);//��JPEG��ʽ��ͻ��˷���ͼ����֤��
    }



}