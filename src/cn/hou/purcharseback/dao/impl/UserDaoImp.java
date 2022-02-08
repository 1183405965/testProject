package cn.hou.purcharseback.dao.impl;

import cn.hou.purcharseback.dao.BaseDao;
import cn.hou.purcharseback.dao.UserDao;
import cn.hou.purcharseback.dao.tools.DBPool;
import cn.hou.purcharseback.pojo.QtUser;
import cn.hou.purcharseback.pojo.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImp implements UserDao {
    private BaseDao baseDao=new BaseDaoImp();

    @Override
    public User login(User user) {
        String sql="select id,username,password from user where username=? and password=?";
        User user1=null;
        ResultSet rs=null;
        Connection connection=new DBPool().getConnection();
        try {
            rs = baseDao.query(connection,sql, user.getUsername(), user.getPassword());
            if (rs!=null&&rs.next()){
                 user1=new User(rs.getInt(1),rs.getString(2),rs.getString(3) );

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                rs.close();
                connection.close();
                connection=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
              }

        }

        return user1;
    }

    @Override
    public QtUser login(QtUser user) {
        String sql="select username,password from qtuser where username=? and password=?";
        QtUser user1=null;
        ResultSet rs=null;
        Connection connection=new DBPool().getConnection();
        try {
            rs = baseDao.query(connection,sql, user.getUsername(), user.getPassword());
            if (rs!=null&&rs.next()){
                user1=new QtUser(rs.getString(1),rs.getString(2),null,null);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                rs.close();
                connection.close();
                connection=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        return user1;
    }

    @Override
    public void Register(QtUser user) {
        String sql="insert into qtuser(username,password,email,qq) values(?,?,?,?)";
        Connection connection=new DBPool().getConnection();
        try {
             baseDao.update(connection,sql, user.getUsername(), user.getPassword(),user.getEmail(),user.getQq());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                connection.close();
                connection=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    @Override
    public boolean findUserName(String username) throws SQLException {
        ResultSet rs=null;
        String sql="select username from qtuser where username=?";
        Connection connection=new DBPool().getConnection();
        try {
            rs = baseDao.query(connection, sql, username);
            if(rs!=null&&rs.next()){
            }
            else
            {
                rs.close();
                rs=null;
            }
            if (rs==null){

                return true;
            }
            else return false;
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            connection.close();
            connection=null;
        }
    }

}
