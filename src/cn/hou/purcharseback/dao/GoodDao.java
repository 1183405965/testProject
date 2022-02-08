package cn.hou.purcharseback.dao;

import cn.hou.purcharseback.pojo.Good;

import java.sql.SQLException;
import java.util.List;

public interface GoodDao {
    void add(Good good) throws SQLException;
    List<Good> queryByCri(String id, String goodname, String goodtype) throws SQLException;
    List<Good> queryByPage(String pageNow, String pageSize) throws SQLException;
    int queryTotalRow() throws SQLException;
    List<String> queryAllType() throws SQLException;
    public List<Good> findGoodByType(String type) throws SQLException;
    Good findGoodById(String id,int amount) throws SQLException;
    void delGood(int id) throws SQLException;
    void modGood(Good good) throws SQLException;
}
