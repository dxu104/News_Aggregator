package web;

import POJO.news;
import com.alibaba.fastjson.JSON;
import service.newsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/newsViewCount")
public class newsViewCounterServlet extends HttpServlet {
    //这个对象要多次用的，所以我们提到成员的位置。
    private newsService newsService = new newsService();
    @Override
    //1.调用
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer ID = Integer.parseInt(request.getParameter("ID"));
        news news = newsService.selectAll(ID);
        Integer views = news.getViews()+1;
        Integer updatedTime = (int) new Date().getTime();
        news.setID(ID);
        news.setViews(views);
        news.setUpdatedTime(updatedTime);
        newsService.newsViewCounter_updated(news);

        String jsonString = JSON.toJSONString(news);

        response.setContentType("application/json");
        response.getWriter().print(jsonString);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
