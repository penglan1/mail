package pers.penglan.mail.mapper;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.AttachmentVO;
import pers.penglan.mail.model.MailEntityVO;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-25
 */
public interface AttachmentMapper {

    public int insertList(@Param("list") List<AttachmentVO> attachmentVOList);

    List<AttachmentVO> selectByPk_MailEntity(String pk_mailEntity);

    int deleteByPk_MailEntity(String pk_mailEntity);
}
