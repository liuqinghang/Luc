package com.lut.mapper;


import com.lut.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 路瞳
 */
@Mapper
public interface UserMapper {
    /**
     * get some user info
     * @param userId
     * @return
     */
    public User getUserInfo(Integer userId);

    /**
     * register new user
     * @param user
     * @return 是否插入成功
     */
    public Integer register(User user);

    /**
     * update user info
     * @param user
     * @return
     */
    public void updateUserInfo(User user);
    /**
     * 验证用户存在
     * @param username
     * @return
     */
    Integer verifyUserExit(String username);

    /**
     * 验证密码是否正确
     * @param user
     * @return
     */
    Integer verifyUser(User user);




}
