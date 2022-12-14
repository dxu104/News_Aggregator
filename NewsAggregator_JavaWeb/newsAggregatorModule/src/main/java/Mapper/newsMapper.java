package Mapper;


import POJO.news;
import POJO.userFavor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface newsMapper {
    POJO.news selectAll(Integer ID);
    List<news> newsSearch(String keyWord);
    List<news> selectByChannelID(@Param("channelID") Integer channelID, @Param("limit") Integer limit, @Param("offset") Integer offset);
    void newsViewCounter_updated(news news);

    void newsFavorInsert(userFavor userFavor);

    void newsFavorDelete(userFavor userFavor);

    List<news> userFavorList(Integer userID);
    userFavor isSaved(@Param("userID")Integer userID,@Param("newsID") Integer newsID);




}
