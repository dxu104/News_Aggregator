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
import java.util.List;

/*2.newsList  API was designed for showing  a list of news to readers.
When users click the channel such CNN or ABC.
 In addition, uses can choose the page size and page.
 The ID of channel was packaged to a Request object.
 The getParameterMap method will get the inputs and
 Integer.parseInt method will parse this the content of string
 and store them into news attributes.
 Then selectByChannel method in newsService class will use
 the ID, pageSize and page attributes as argument.
 In database part, we will find a list of news
 thumbnail whose channel is equal to the argument using SQL statement.
 *
* */

@WebServlet("/newsList")
public class newsListServlet extends HttpServlet {
   private newsService newsService = new newsService();
    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer channelID =1;
        Integer limit = 10;
        Integer offset = 0;

        if(request.getParameterMap().containsKey("channelID"))
            channelID= Integer.parseInt(request.getParameter("channelID"));
        if(request.getParameterMap().containsKey("pageSize"))
           limit = Integer.parseInt(request.getParameter("pageSize"));
        if(request.getParameterMap().containsKey("page")){

            Integer page = Integer.parseInt(request.getParameter("page"));
                    offset = limit * (page-1);
        }

        List<news> newsEntity=newsService.selectByChannelID(channelID,limit,offset);

        String jsonString= JSON.toJSONString(newsEntity);
        response.setContentType("application/json");
        response.getWriter().print(jsonString);



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
