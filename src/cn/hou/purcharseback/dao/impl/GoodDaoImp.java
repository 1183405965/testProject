package cn.hou.purcharseback.dao.impl;

import cn.hou.purcharseback.dao.BaseDao;
import cn.hou.purcharseback.dao.GoodDao;
import cn.hou.purcharseback.dao.tools.DBPool;
import cn.hou.purcharseback.pojo.Good;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class GoodDaoImp implements GoodDao {
    private BaseDao baseDao=new BaseDaoImp();
    @Override
    public void add(Good good) throws SQLException {
        String sql="insert into good(goodname,goodtype,price,pic) values(?,?,?,?)";
        Connection connection=new DBPool().getConnection();

        try {
            connection.setAutoCommit(false);
            baseDao.update(connection,sql,good.getGoodname(),good.getGoodtype(),good.getPrice(),good.getPic());
            connection.commit();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw e;
            }
            throw throwables;
        }finally {
            try {
                connection.close();
                connection=null;
            } catch (SQLException throwables) {
                throw throwables;
            }

        }
    }

    @Override
    public List<Good> queryByCri(String id, String goodname, String goodtype) throws SQLException {
        String sql="select id,goodname,goodtype,price,pic from good";
        ResultSet rs=null;
        StringBuilder where=new StringBuilder("");
        if (!id.trim().equals("")){
            where.append(" where id ="+id);
        }
            if (!goodname.trim().equals("")) {
            if (where.length() != 0)
                where = where.append(" and goodname like '%" + goodname + "%'");
            else  where.append(" where goodname like '%" + goodname + "%'");
        }


        if (!goodtype.trim().equals("")){
            if (where.length()!=0)
                where= where.append(" and goodtype like '%"+goodtype+"%'");
            else where.append(" where goodtype like '%"+goodtype+"%'");
        }


        sql+=where;
        Connection connection=new DBPool().getConnection();
        List<Good> list=new ArrayList<>();
        try {
           rs = baseDao.query(connection, sql, null);
            while (rs!=null&&rs.next()){
                Good good=new Good(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        Double.valueOf(rs.getString(4)),
                        rs.getString(5));
                list.add(good);
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return list;
    }

    @Override
    public List<Good> queryByPage(String pageNow, String pageSize) throws SQLException {
        ResultSet rs=null;
        int pageNow1 = Integer.valueOf(pageNow);
        int pageSize1 = Integer.valueOf(pageSize);
        String sql="select id,goodname,goodtype,price,pic from good limit "+(pageNow1-1)*pageSize1+","+pageSize;
        Connection connection=new DBPool().getConnection();
        List<Good> list=new ArrayList<>();
        try {
            rs = baseDao.query(connection, sql, null);
            while (rs!=null&&rs.next()){
                Good good=new Good(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        Double.valueOf(rs.getString(4)),
                        rs.getString(5));
                list.add(good);
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return list;
    }

    @Override
    public int queryTotalRow() throws SQLException {
        ResultSet rs=null;
        String sql="select count(*) from good";
        int totalCount=0;
        Connection connection=new DBPool().getConnection();

        try {
            rs = baseDao.query(connection, sql, null);
            if (rs!=null&&rs.next()){
                totalCount=rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return totalCount;
    }

    @Override
    public List<String> queryAllType() throws SQLException {
        ResultSet rs=null;
        String sql="select distinct goodtype from good";
        Connection connection=new DBPool().getConnection();
        List<String> list=new ArrayList<>();
        try {
            rs = baseDao.query(connection, sql, null);
            while (rs!=null&&rs.next()){

                list.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return list;
    }

    @Override
    public List<Good> findGoodByType(String type) throws SQLException {
        ResultSet rs=null;
        String sql="select id,goodname,goodtype,price,pic from good where goodtype=? limit 0,20";
        Connection connection=new DBPool().getConnection();
        List<Good> list=new ArrayList<>();
        try {
            rs = baseDao.query(connection, sql, type);
            while (rs!=null&&rs.next()){
                Good good=new Good(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        Double.valueOf(rs.getString(4)),
                        rs.getString(5));
                list.add(good);
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return list;
    }

    @Override
    public Good findGoodById(String id,int amount) throws SQLException {
        Good good=null;
        ResultSet rs=null;
        String sql="select id,goodname,goodtype,price,pic from good where id=?";
        Connection connection=new DBPool().getConnection();
        try {
            rs = baseDao.query(connection, sql, id);
            if (rs!=null&&rs.next()){
                good=new Good(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        Double.valueOf(rs.getString(4)),
                        rs.getString(5));
                good.setAmount(amount);
            }
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            rs.close();
            connection.close();
            connection=null;
        }
        return good;
    }

    @Override
    public void delGood(int id) throws SQLException {
        String sql="delete from good where id=?";
        Connection connection=new DBPool().getConnection();
        try {
            baseDao.update(connection, sql, id);
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            connection.close();
            connection=null;
        }
    }

    @Override
    public void modGood(Good good) throws SQLException {
        ResultSet rs=null;
        String sql="update good set goodname=?,goodtype=?,price=? where id =?;";
        Connection connection=new DBPool().getConnection();
        try {
            baseDao.update(connection,sql,good.getGoodname(),good.getGoodtype(),good.getPrice(),good.getId());
        } catch (SQLException throwables) {
            throw throwables;
        }finally {
            connection.close();
            connection=null;
        }
    }
}
