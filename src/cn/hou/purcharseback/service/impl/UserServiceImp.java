package cn.hou.purcharseback.service.impl;

import cn.hou.purcharseback.dao.UserDao;
import cn.hou.purcharseback.dao.impl.UserDaoImp;
import cn.hou.purcharseback.pojo.QtUser;
import cn.hou.purcharseback.pojo.User;
import cn.hou.purcharseback.service.UserService;

import java.sql.SQLException;

public class UserServiceImp implements UserService {
    private UserDao userDao=new UserDaoImp();
    @Override
    public User login(User user) {
        //通过查询User表来确定用户是否合法
        return userDao.login(user);
    }

    @Override
    public QtUser login(QtUser user) {
        return userDao.login(user);
    }

    @Override
    public void Register(QtUser user) {
        userDao.Register(user);
    }

    @Override
    public boolean findUserName(String username) throws SQLException {

        return userDao.findUserName(username);
    }
}
