package service;


import Mapper.newsMapper;
import POJO.news;
import POJO.userFavor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class newsService {
    public static SqlSession createSqlSessionObject() throws IOException {
        //1.load mybatis-config.xml config file to get sqlSessionFactory

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //2. get sqlSession object to execute SQL statement

        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }


    public List<news> selectByChannelID(Integer channelID, Integer limit, Integer offset) throws IOException {
        SqlSession sqlSession = createSqlSessionObject();


        //3.get the agent object of ChannelMapper interface
        newsMapper ChannelMapper = sqlSession.getMapper(newsMapper.class);

        //4.By ChannelMapper interface,we jump into channelMapper interface in the Mapper package
        //5.And Mybatis will help us find a mapper file(XML) with same name as channelMapper.
        //6. we call selectByChannelID method, which is also a selectID in the ChannelMapper.xml
        //7. we could execute the SQL statement in this XML file.

        List<news> newsList = ChannelMapper.selectByChannelID(channelID, limit, offset);

        sqlSession.close();
        return newsList;
    }
    public news selectAll(Integer ID) throws IOException {
        SqlSession sqlSession = createSqlSessionObject();


        newsMapper newsmapper = sqlSession.getMapper(newsMapper.class);

        news newsEntity =  newsmapper.selectAll(ID);



        sqlSession.commit();

        sqlSession.close();
        return newsEntity;
    }



    public List<POJO.news> newsSearch(String keyWord) throws IOException {

        SqlSession sqlSession = createSqlSessionObject();


        newsMapper newsSearchMapper = sqlSession.getMapper(newsMapper.class);
        List<news> newsSearchList = newsSearchMapper.newsSearch(keyWord);


        sqlSession.commit();

        sqlSession.close();
        return newsSearchList;

    }

    public void newsViewCounter_updated(news news) throws IOException {
        SqlSession sqlSession = createSqlSessionObject();


        newsMapper newsViewCounterMapper = sqlSession.getMapper(newsMapper.class);
        newsViewCounterMapper.newsViewCounter_updated(news);

        sqlSession.commit();


        sqlSession.close();


    }

    public void newsFavorInsert(userFavor userFavor) throws IOException {

        SqlSession sqlSession = createSqlSessionObject();


        newsMapper userFavorMapper = sqlSession.getMapper(newsMapper.class);
        userFavorMapper.newsFavorInsert(userFavor);

        sqlSession.commit();

        sqlSession.close();


    }

    public void newsFavorDelete(userFavor userFavor) throws IOException {

        SqlSession sqlSession = createSqlSessionObject();

        newsMapper userFavorMapper = sqlSession.getMapper(newsMapper.class);
        userFavorMapper.newsFavorDelete(userFavor);

        sqlSession.commit();


        sqlSession.close();


    }

    public List<news> userFavorList(Integer userID) throws IOException {

        SqlSession sqlSession = createSqlSessionObject();


        newsMapper userFavorListMapper = sqlSession.getMapper(newsMapper.class);
        List<news> userFavorList = userFavorListMapper.userFavorList(userID);

        sqlSession.commit();

        sqlSession.close();

        return  userFavorList;
    }

    public userFavor isSaved(Integer userID, Integer newsID) throws IOException {

        SqlSession sqlSession = createSqlSessionObject();

        newsMapper isSavedMapper = sqlSession.getMapper(newsMapper.class);
        userFavor isSaved = isSavedMapper.isSaved(userID,newsID);

        sqlSession.commit();
        sqlSession.close();

        return  isSaved;


    }

}