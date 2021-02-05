package pers.penglan.mail.mapper.user;

import pers.penglan.mail.model.user.User;

/**
 * @Author PENGL
 * 2020-03-27
 */
public interface UserMapper {

    User selectByUserIdOrMailAccount(String idOrMailAccount);

    int insertOne(User user);
}
