package web;

import POJO.NewsViewCounter;
import com.alibaba.fastjson.JSON;
import service.newsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/newsViewCount")
public class newsViewCounterServlet extends HttpServlet {
   private newsService newsService = new newsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer newsID =0;
        Integer count=0;
        NewsViewCounter newsViewCounter = null;

        if(request.getParameterMap().containsKey("ID")){
            newsID= Integer.parseInt(request.getParameter("ID"));
            count++;
             newsViewCounter= newsService.newsViewCounter(count,newsID);
        } else {
            newsViewCounter = newsService.newsViewCounter_insert(0,Integer.parseInt(request.getParameter("ID")));
        }

        String jsonString= JSON.toJSONString(newsViewCounter);
        response.setContentType("application/json");
        response.getWriter().print(jsonString);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
