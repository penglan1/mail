package pers.penglan.mail.mapper.user.mail;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.user.mail.MailAccount;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-27
 */
public interface MailAccountMapper {

    List<MailAccount> selectByUserID(String userID);

    int insertOne(MailAccount mailAccount);

    //List<MailAccount> select(MailAccount deleteGoal);

    int deleteByPkMailAccount(String pk_mailAccount);

    MailAccount selectByUserIDAndAccount(@Param("pk_UserID") String pk_userID, @Param("account") String account);

    int update(MailAccount mailAccount);
}
