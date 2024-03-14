package com.hao.mybatis.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xuh
 * @date 2023/8/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;

    private String name;

    private Date bir;

    private String phoneNumber;

    private Class classInfo;
}
