package com.hao.mybatis.mapper;

import com.hao.mybatis.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuh
 * @date 2023/8/19
 */
public interface UserMapper {

    List<User> queryAllUser();

    User queryUserByIdOnCondition(@Param("id") Integer id);
    User queryUserById(@Param("id") Integer id);

    User queryUserByName(@Param("name") String name);

    String getUserNameById(Integer id);

    User queryUserByUserId(Integer id);


}
