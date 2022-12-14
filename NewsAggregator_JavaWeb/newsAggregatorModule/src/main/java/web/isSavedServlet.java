package web;

import POJO.news;
import POJO.userFavor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import service.newsService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/isSaved")
public class isSavedServlet extends HttpServlet {
    private service.newsService newsService = new newsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userID = Integer.parseInt(request.getParameter("userID"));
        Integer newsID = Integer.parseInt(request.getParameter("newsID"));

        userFavor isExist = newsService.isSaved(userID,newsID);
        if(isExist!=null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 1);

            String jsonString = jsonObject.toJSONString();

            response.setContentType("application/json");
            response.getWriter().print(jsonString);
        }
        else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", 2);

            String jsonString = jsonObject.toJSONString();

            response.setContentType("application/json");
            response.getWriter().print(jsonString);
        }




    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
