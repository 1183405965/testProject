package cn.hou.purcharseback.controller;

import cn.hou.purcharseback.pojo.Good;
import cn.hou.purcharseback.service.GoodService;
import cn.hou.purcharseback.service.impl.GoodServiceImg;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GoodServlet", urlPatterns = "/good/GoodServlet",initParams = {@WebInitParam(name="pageSize",value = "10")})
public class GoodServlet extends HttpServlet {
    private GoodService goodService=new GoodServiceImg();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bs=request.getParameter("bs");
        if ("querybycri".equals(bs)){
            queryByCri(request,response);
        }
        if ("queryall".equals(bs)){
            queryAllByPage(request,response,null);
        }
        if ("del".equals(bs)){
            deleteById(request,response);
        }
        if ("modGood".equals(bs)){
            modGood(request,response);
        }
        if ("updateGoods".equals(bs)){
            modGood(request,response);
        }

    }

    private void modGood(HttpServletRequest request, HttpServletResponse response) {
        String pageNow=request.getParameter("pageNow");
        String bs=request.getParameter("bs");
        Good good=null;
        String id=request.getParameter("id");
        if ("modGood".equals(bs)){
            List<Good> goods=new ArrayList<>();
            //调用业务逻辑，修改商品
            //查询数据进行回显到修改页面
            try {
                good = goodService.findGoodById(id, 0);
                goods.add(good);
                request.setAttribute("goods",goods);
                request.setAttribute("pageNow",pageNow);
                request.getRequestDispatcher("../houtai/updateproduct.jsp").forward(request,response);
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }else{//回显的数据修改数据库
            try {
                String id1=request.getParameter("pid");
                String goodname= request.getParameter("pname");
                String goodtype = request.getParameter("ptype");
                String price=  request.getParameter("pprice");
                Good good1=new Good(Integer.valueOf(id1),goodname,goodtype,Double.valueOf(price),"");
                goodService.modGood(good1);
                //请求转发到到查询页面
                pageNow=request.getParameter("pageNow");
                queryAllByPage(request,response,pageNow);
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        }



    }

    private void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        String pageNow=request.getParameter("pageNow");
        //调用业务逻辑，删除商品
        try {
            goodService.delGood(Integer.valueOf(id));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //请求对象重定向到分页页面
        queryAllByPage(request,response,pageNow);
    }

    private void queryAllByPage(HttpServletRequest request, HttpServletResponse response,String pn) throws ServletException, IOException {
        String pageSize = getServletConfig().getInitParameter("pageSize");
        String pageNow=null;
        if (pn==null){
           pageNow= request.getParameter("pageNow");
        }else pageNow=pn;
        try {
            int totalCount= goodService.queryTotalRow();
            int pageCount=0;
            if (totalCount%Integer.valueOf(pageSize)==0){
                pageCount=totalCount/Integer.valueOf(pageSize);
            }else pageCount=totalCount/Integer.valueOf(pageSize)+1;
            //判断pageNow的值是否超限
            if (Integer.valueOf(pageNow)<1){
                pageNow="1";
            }
            if (Integer.valueOf(pageNow)>pageCount){
                pageNow=pageCount+"";
            }
            List<Good> goods = goodService.queryByPage(pageNow, pageSize);
            request.setAttribute("totalCount",totalCount);
            request.setAttribute("goods",goods);
            request.setAttribute("pageNow",pageNow);
            request.setAttribute("pageCount",pageCount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("../houtai/productListUI.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, UnsupportedEncodingException {
        String contentType = request.getContentType();
        String bs=request.getParameter("bs");
        int i = contentType.indexOf("multipart/form-data");
        if (i!=-1){
            upload(request,response);
            return;
        }

        doGet(request, response);
    }
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodname= (String) request.getAttribute("pname");
        String goodtype= (String) request.getAttribute("ptype");
        String price= (String) request.getAttribute("pprice");
        String pimg= (String) request.getAttribute("pimg");
        Good good=new Good(null,goodname,goodtype,Double.valueOf(price),pimg);
        try {
            goodService.add(good);
            response.sendRedirect("../houtai/addnewproduct.jsp");
            return;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        request.setAttribute("errinfo","添加商品失败");
        request.getRequestDispatcher("../houtai/addnewproduct.jsp").forward(request,response);

    }
private void upload(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
    request.setCharacterEncoding("utf-8");    //设置编码

//    创建 FileItem 对象的工厂
    DiskFileItemFactory factory = new DiskFileItemFactory();
    //获取文件需要上传到的路径
    String path = getServletContext().getRealPath("/WEB-INF/upload");
    //指定临时文件目录
    factory.setRepository(new File(path));
    //设置内存缓冲区的大小
    factory.setSizeThreshold(1024*1024) ;
    //负责处理上传的文件数据，并将表单中每个输入项封装成一个 FileItem 对象中
    ServletFileUpload upload = new ServletFileUpload(factory);
//    //ProgressListener显示上传进度
//    ProgressListener progressListener = new ProgressListener() {
//        public void update(long pBytesRead, long pContentLength, int pItems) {
//            System.out.println("到现在为止,  " + pBytesRead + " 字节已上传，总大小为 "
//                    + pContentLength);
//        }
//    };
//    upload.setProgressListener(progressListener);

    List<FileItem> list;
    try {
        //调用Upload.parseRequest方法解析request对象，得到一个保存了所有上传内容的List对象。
        list = (List<FileItem>)upload.parseRequest(request);
//      对list进行迭代，每迭代一个FileItem对象，调用其isFormField方法判断是否是上传文件
        for(FileItem item : list){
            String name = item.getFieldName();
            if(item.isFormField()){//为普通表单字段
                String value = new String(item.getString().getBytes("iso-8859-1"),"utf-8") ;
                request.setAttribute(name, value);
            }else{//为上传文件，则调用item.write方法写文件
                String value = item.getName() ;
                int start = value.lastIndexOf("\\");
                String filename = value.substring(start+1);
                request.setAttribute(name, filename);
                item.write(new File(path,filename));
            }
        }
        //保存普通表单的数据
        add(request,response);
    } catch (FileUploadException e) {
        e.printStackTrace();
    }catch (Exception e) {
        e.printStackTrace();
    }
}


    private void queryByCri(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("pid");
        String goodname=request.getParameter("pname");
        String goodtype=request.getParameter("ptype");
        List<Good> goods = null;
        try {
            goods = goodService.queryByCri(id, goodname, goodtype);
           request.setAttribute("goods",goods);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("../houtai/productListUIbycri.jsp").forward(request,response);
    }

}