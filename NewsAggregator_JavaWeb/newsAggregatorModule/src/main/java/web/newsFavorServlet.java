package web;

import POJO.userFavor;
import com.alibaba.fastjson.JSON;
import service.newsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/userFavor")
public class newsFavorServlet extends HttpServlet {
    private service.newsService newsService = new newsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer userID = Integer.parseInt(request.getParameter("userID"));
        Integer newsID = Integer.parseInt(request.getParameter("newsID"));
        userFavor userFavor = new userFavor();
        userFavor.setUserID(userID);
        userFavor.setNewsID(newsID);

        Integer collect = Integer.parseInt(request.getParameter("collect"));
        if (collect==1&&newsService.isSaved(userID,newsID)==null){


            newsService.newsFavorInsert(userFavor);

        }
        if (collect==2&&newsService.isSaved(userID, newsID) != null) {


            newsService.newsFavorDelete(userFavor);

        }




        String jsonString = JSON.toJSONString(userFavor);

        response.setContentType("application/json");
        response.getWriter().print(jsonString);


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
