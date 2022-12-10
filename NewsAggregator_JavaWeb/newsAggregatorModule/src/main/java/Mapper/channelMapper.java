package Mapper;



import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface channelMapper {

     List<POJO.news> selectByChannelID(@Param("channelID") Integer channelID, @Param("limit") Integer limit, @Param("offset") Integer offset);

}
