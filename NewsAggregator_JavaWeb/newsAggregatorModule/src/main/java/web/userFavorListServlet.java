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
import java.util.Collections;
import java.util.List;

@WebServlet("/userFavorListServlet")
public class userFavorListServlet extends HttpServlet {
    private service.newsService newsService = new newsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer userID = Integer.parseInt(request.getParameter("userID"));
        List<news> userFavorList = newsService.userFavorList(userID);
        Collections.reverse(userFavorList);

        //2.将集合转换为JSON数据，序列化
        String jsonString= JSON.toJSONString(userFavorList);
        // System.out.println(jsonString);
        //3 响应数据
        // response.setContentType("text/json;charset=utf-8");
        response.setContentType("application/json");
        response.getWriter().print(jsonString);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
