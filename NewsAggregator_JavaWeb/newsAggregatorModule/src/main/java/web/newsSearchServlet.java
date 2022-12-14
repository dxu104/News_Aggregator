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

@WebServlet("/newsSearch")
public class newsSearchServlet extends HttpServlet {
    //这个对象要多次用的，所以我们提到成员的位置。
    private newsService newsService = new newsService();
    @Override
    //1.调用
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String keyWord = "World";

        if(request.getParameterMap().containsKey("keyWord"))
            keyWord = request.getParameter("keyWord");


        List<news> newsList=newsService.newsSearch(keyWord);


        //2.将集合转换为JSON数据，序列化
        String jsonString= JSON.toJSONString(newsList);
        // System.out.println(jsonString);
        //3 响应数据
        // response.setContentType("text/json;charset=utf-8");
        response.setContentType("application/json");
        response.getWriter().print(jsonString);





        // request.setAttribute("newsList",newsList);
        //3.转发到newslist.jsp.
        //request.getRequestDispatcher("/newsList.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
