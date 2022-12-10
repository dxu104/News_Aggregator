package service;

import Mapper.channelMapper;
import Mapper.newsMapper;
import Mapper.newsViewCounterMapper;
import POJO.NewsViewCounter;
import POJO.news;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;



public class newsService {

public List<news> selectByChannelID(Integer channelID,Integer limit,Integer offset) throws IOException {
    //1.load mybatis-config.xml config file to get sqlSessionFactory
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//2. get sqlSession object to execute SQL statement
    SqlSession sqlSession = sqlSessionFactory.openSession();

     //3.get the agent object of ChannelMapper interface
    channelMapper ChannelMapper = sqlSession.getMapper(channelMapper.class);
    //4.By ChannelMapper interface,we jump into channelMapper interface in the Mapper package
    //5.And Mybatis will help us find a mapper file(XML) with same name as channelMapper.
    //6. we call selectByChannelID method, which is also a selectID in the ChannelMapper.xml
    //7. we could execute the SQL statement in this XML file.
    List<news> newsList =  ChannelMapper.selectByChannelID( channelID, limit, offset);

    sqlSession.close();
    return newsList;
}

    public news selectAll(Integer ID) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        newsMapper newsmapper = sqlSession.getMapper(newsMapper.class);
        news newsEntity =  newsmapper.selectAll(ID);

        sqlSession.close();
        return newsEntity;
    }

    public NewsViewCounter newsViewCounter(Integer count, Integer newsID) throws IOException {

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        newsViewCounterMapper newsViewCounterMapper = sqlSession.getMapper(newsViewCounterMapper.class);
        NewsViewCounter newsViewCounter = newsViewCounterMapper.newsViewCounter(count, newsID);
        sqlSession.close();
        return newsViewCounter;

    }

    public NewsViewCounter newsViewCounter_insert(Integer count, Integer newsID) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        newsViewCounterMapper newsViewCounterMapper = sqlSession.getMapper(newsViewCounterMapper.class);
        NewsViewCounter newsViewCounter = newsViewCounterMapper.newsViewCounter_insert(count, newsID);


        sqlSession.close();
        return newsViewCounter;

    }
}
