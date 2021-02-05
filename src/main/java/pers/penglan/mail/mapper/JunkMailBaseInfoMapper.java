package pers.penglan.mail.mapper;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.JunkMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface JunkMailBaseInfoMapper {

    public List<JunkMailBaseInfoVO> select(JunkMailBaseInfoVO junkMailBaseInfoVO);

    public int insertList(@Param("list") List<JunkMailBaseInfoVO> receivedJunkMailBaseInfoList);

    int deleteByPk_MailBaseInfo(String pk_mailBaseInfo);

    int update(JunkMailBaseInfoVO mailBaseInfoVO);
}
