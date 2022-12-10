package Mapper;


import POJO.NewsViewCounter;

public interface newsViewCounterMapper {
    NewsViewCounter newsViewCounter(Integer ID, Integer newsID);

    NewsViewCounter newsViewCounter_insert(Integer ID, Integer newsID);

}
