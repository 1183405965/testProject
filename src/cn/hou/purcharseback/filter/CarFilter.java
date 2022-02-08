package cn.hou.purcharseback.filter;

import cn.hou.purcharseback.pojo.QtUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CarFilter",urlPatterns = "/qiantai/flow.jsp")
public class CarFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        QtUser user = (QtUser) req.getSession().getAttribute("user");
        if (user==null){//ÓÃ»§Î´µÇÂ¼
            resp.sendRedirect("../qiantai/login.jsp");
        }else chain.doFilter(request, response);
    }
}
