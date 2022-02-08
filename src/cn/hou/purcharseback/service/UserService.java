package cn.hou.purcharseback.service;

import cn.hou.purcharseback.pojo.QtUser;
import cn.hou.purcharseback.pojo.User;

import java.sql.SQLException;

public interface UserService {
    User login(User user);
    QtUser login(QtUser user);
    void Register(QtUser user);
    boolean findUserName(String username) throws SQLException;
}
