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
/*1.	newsEntity API was designed for showing the news to readers.
 When users click the news, the ID of news was packaged to a Request object.
 The getParameterMap method will get this ID string and
 Integer.parseInt method will parse this the content of ID string
 and store it into ID attribute.
 Then selectALL method in newsService class will use the ID attribute as argument.
 In database part, we will find a news object whose ID is equal to the argument using SQL statement.
*
* */

@WebServlet("/newsEntity")
public class newsEntityServlet extends HttpServlet {
   private newsService newsService = new newsService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Integer ID =1;

        if(request.getParameterMap().containsKey("ID"))
            ID= Integer.parseInt(request.getParameter("ID"));

        news newsEntity=newsService.selectAll(ID);


        String jsonString= JSON.toJSONString(newsEntity);

        response.setContentType("application/json");
        response.getWriter().print(jsonString);



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
