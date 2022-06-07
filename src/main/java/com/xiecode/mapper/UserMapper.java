package com.xiecode.mapper;

import com.xiecode.entity.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    AuthUser getPassWordByUserName(String username);

}
