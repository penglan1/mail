package pers.penglan.mail.mapper;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.SentMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface SentMailBaseInfoMapper {

    public int insertList(@Param("list") List<SentMailBaseInfoVO> sentMailBaseInfoVOList);

    public int insertOne(SentMailBaseInfoVO sentMailBaseInfoVO);

    public List<SentMailBaseInfoVO> selectList(SentMailBaseInfoVO sentMailBaseInfoVO);

    int deleteByPk_MailBaseInfo(String pk_mailBaseInfo);

    int update(SentMailBaseInfoVO mailBaseInfoVO);
}
