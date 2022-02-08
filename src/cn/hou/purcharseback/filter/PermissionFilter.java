package cn.hou.purcharseback.filter;

import cn.hou.purcharseback.pojo.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class PermissionFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        System.out.println("filter已运行");
        HttpServletRequest req= (HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        User user=(User)req.getSession().getAttribute("user");
        if (user==null){
            resp.sendRedirect("index.jsp");
            return;
        }
        chain.doFilter(request, response);
    }
}
