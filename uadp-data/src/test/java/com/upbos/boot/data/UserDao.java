package com.upbos.boot.data;

import com.upbos.data.mapper.annotations.Insert;
import com.upbos.data.mapper.annotations.Mapper;
import com.upbos.data.mapper.annotations.Select;
import com.upbos.data.mapper.annotations.Update;

import java.util.List;

@Mapper
public interface UserDao {

    @Select(sql = "select * from user where uid = #{uid}")
    public List<User> listUser(String uid);

    @Insert()
    public void insertUser(User user);

    @Update
    public void updateUser(User user);
}
