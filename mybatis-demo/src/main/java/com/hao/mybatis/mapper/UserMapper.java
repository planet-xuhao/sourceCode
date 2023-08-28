package com.hao.mybatis.mapper;

import com.hao.mybatis.po.User;

import java.util.List;

/**
 * @author xuh
 * @date 2023/8/19
 */
public interface UserMapper {

    List<User> queryAllUser();
}
