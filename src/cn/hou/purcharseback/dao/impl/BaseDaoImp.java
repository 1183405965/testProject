package cn.hou.purcharseback.dao.impl;

import cn.hou.purcharseback.dao.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDaoImp implements BaseDao {

    @Override
    public ResultSet query(Connection connection,String sql, Object... params) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(sql);
        if (params!=null)
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i+1,params[i]);
            }
        ResultSet rs = pst.executeQuery();
//            pst.close();
        return rs;
    }

    @Override
    public void update(Connection connection,String sql, Object... params) throws SQLException {
        PreparedStatement pst = connection.prepareStatement(sql);
        if (params!=null)
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i+1,params[i]);
        }
        pst.execute();

    }


}
