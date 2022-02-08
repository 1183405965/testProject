package cn.hou.purcharseback.controller;

import cn.hou.purcharseback.pojo.CarBean;
import cn.hou.purcharseback.pojo.Good;
import cn.hou.purcharseback.service.GoodService;
import cn.hou.purcharseback.service.impl.GoodServiceImg;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "QtGoodServlet", urlPatterns = "/qt/QtGoodServlet")
public class QtGoodServlet extends HttpServlet {
    private GoodService goodService=new GoodServiceImg();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bs=request.getParameter("bs");
        if ("open".equals(bs)){
            open(request,response);
        }
        if ("getImg".equals(bs)){
            getImg(request,response);
        }
        if ("addGood".equals(bs)){
            addGood(request,response);
        }
        if ("updateShoppingCart".equals(bs)){
            updateShoppingCart(request,response);
        }
        if ("delShoppingCart".equals(bs)){
            delShoppingCart(request,response);
        }
        if ("delCarGood".equals(bs)){
            delCarGood(request,response);
        }

    }

    private void delCarGood(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("ID");
        CarBean car= (CarBean) request.getSession().getAttribute("car");

        car.removeGood(Integer.valueOf(id));
        List<Good> goods = car.toList();

        request.setAttribute("goods",goods);
        try {
            request.getRequestDispatcher("../qiantai/flow.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delShoppingCart(HttpServletRequest request, HttpServletResponse response) {
        CarBean car= (CarBean) request.getSession().getAttribute("car");
        car.clear();
        List<Good> goods = car.toList();
        request.setAttribute("goods",goods);
        try {
            request.getRequestDispatcher("../qiantai/flow.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateShoppingCart(HttpServletRequest request, HttpServletResponse response) {
        double totalAccount=0;
        double totalAmount=0.0;
        //第一个元素为空串
//        String[] ids=request.getParameterValues("id");
        String[] amounts=request.getParameterValues("amount");
        CarBean car= (CarBean) request.getSession().getAttribute("car");
        List<Good> goods = car.toList();
        int i = 0;
        for (Iterator<Good> iterator = goods.iterator(); iterator.hasNext(); i++) {
            Good next = iterator.next();
            next.setAmount(Integer.valueOf(amounts[i+1]));
            totalAccount += next.getAmount() * next.getPrice();
        }
        request.setAttribute("goods",goods);
        request.getSession().setAttribute("car",car);
        request.setAttribute("totalAccount",totalAccount);
        request.setAttribute("totalAmount",car.totalAmount());
        request.getSession().setAttribute("totalAmount",car.totalAmount());
        request.getSession().setAttribute("totalAccount",totalAccount);

        try {
            request.getRequestDispatcher("../qiantai/flow.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addGood(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double totalAccount=0.0;
        double totalAmount=0.0;
        String id=request.getParameter("id");
        CarBean car= (CarBean) request.getSession().getAttribute("car");
        if (car==null){
            car=new CarBean();
        }
        car.addGood(Integer.valueOf(id));
        request.getSession().setAttribute("car",car);
        //去购物车页面
        List<Good> goods=car.toList();
        request.setAttribute("totalAccount",car.totalAccount());
        request.setAttribute("totalAmount",car.totalAmount());
        request.setAttribute("goods",goods);
        if (car!=null){
             totalAccount = car.totalAccount();
             totalAmount=car.totalAmount();
            request.getSession().setAttribute("totalAmount",totalAmount);
            request.getSession().setAttribute("totalAccount",totalAccount);}
        request.getRequestDispatcher("../qiantai/flow.jsp").forward(request,response);
    }

    private void getImg(HttpServletRequest request, HttpServletResponse response) {
        String picName=request.getParameter("pic");
        String path=request.getServletContext().getRealPath("/WEB-INF/upload/"+picName);
        OutputStream outputStream=null;
        FileInputStream fis=null;
        try {
            fis=new FileInputStream(path);
            outputStream= response.getOutputStream();
            int len=-1;
            byte[] b=new byte[1024];
            while ((len=fis.read(b))!=-1){
                outputStream.write(b,0,len);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void open(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String type=request.getParameter("type");
        try {
            List<Good> goods=null;
            List<String> types = goodService.queryAllType();
            request.setAttribute("types",types);
            if (types.size()>0){ //第一个分类商品列表查询
                if("".trim().equals(type)){
                    goods = goodService.findGoodByType(types.get(0));
                }else  goods = goodService.findGoodByType(type);

            }
            request.setAttribute("goods",goods);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        request.getRequestDispatcher("../qiantai/index.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        doGet(request, response);
    }

}