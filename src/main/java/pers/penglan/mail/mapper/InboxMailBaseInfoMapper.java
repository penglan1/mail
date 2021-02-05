package pers.penglan.mail.mapper;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.InboxMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface InboxMailBaseInfoMapper {

    public List<InboxMailBaseInfoVO> selectByOwner(String owner);

    public int insertList(@Param("list") List<InboxMailBaseInfoVO> inboxMailBaseInfoVOList);

    int update(InboxMailBaseInfoVO updateList);

    int deleteByPk_MailBaseInfo(String pk_mailBaseInfo);
}
