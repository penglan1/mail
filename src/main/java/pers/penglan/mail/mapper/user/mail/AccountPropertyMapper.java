package pers.penglan.mail.mapper.user.mail;

import org.apache.ibatis.annotations.Param;
import pers.penglan.mail.model.user.mail.AccountProperty;

import java.util.List;

/**
 * @Author PENGL
 * 2020-03-27
 */
public interface AccountPropertyMapper {
    int insertList(@Param("list") List<AccountProperty> accountPropertyList);

    int deleteByPkMailAccount(String pk_mailAccount);

    List<AccountProperty> selectByPKMailAccount(String pk_mailAccount);
}
