package pers.penglan.mail.mapper;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.DeletedMailBaseInfoVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface DeletedMailBaseInfoMapper {
    List<DeletedMailBaseInfoVO> select(DeletedMailBaseInfoVO deletedMailBaseInfoVO);

    int insertList(@Param("list") List<DeletedMailBaseInfoVO> receivedDeletedMailList);

    int deleteByPk_MailBaseInfo(String pk_mailBaseInfo);
}
