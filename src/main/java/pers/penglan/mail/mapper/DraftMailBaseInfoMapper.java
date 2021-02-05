package pers.penglan.mail.mapper;

import pers.penglan.mail.model.DraftMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface DraftMailBaseInfoMapper {

    public int insertOne(DraftMailBaseInfoVO draftMailBaseInfoVO);

    public List<DraftMailBaseInfoVO> selectByOwner(String owner);

    public int insertList(List<DraftMailBaseInfoVO> draftMailBaseInfoVOList);

    int deleteByPk_MailBaseInfo(String pk_mailBaseInfo);

    int update(DraftMailBaseInfoVO mailBaseInfoVO);
}
