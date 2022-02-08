package cn.hou.purcharseback.pojo;

import cn.hou.purcharseback.service.GoodService;
import cn.hou.purcharseback.service.impl.GoodServiceImg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarBean {
    private Map<Integer,Integer> car;
    private GoodService goodService=new GoodServiceImg();
    public Map<Integer, Integer> getCar() {
        return car;
    }

    public void setCar(Map<Integer, Integer> car) {
        this.car = car;
    }
    //添加购物车
    public void addGood(Integer id){
        if (car==null){
            car=new HashMap<>();
        }
        car.put(id,1);
    }
    //删除购物车
    public void removeGood(Integer id){

        car.remove(id);
    }
    //清空购物车
    public void clear(){
        car.clear();
    }
    //修改购物车
    public void modify(String[] ids,String[] amounts){
        for (int i = 0; i < ids.length; i++) {
            String id=ids[i];
            car.put(Integer.valueOf(id),Integer.valueOf(amounts[i]));
        }

    }
    //把购物车转化为商品的列表List<Good>
    public List<Good> toList(){
        List<Good> goods=new ArrayList<>();
        for (Map.Entry<Integer,Integer> e :car.entrySet()) {
            String id=e.getKey()+"";
            String amount=e.getValue()+"";
            try {
                Good good=goodService.findGoodById(id,e.getValue());
                goods.add(good);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return goods;
    }
    //统计总数量
    public int totalAmount(){
        int amount=0;
        for (Map.Entry<Integer,Integer> e :car.entrySet()) {
            amount+=e.getValue();
        }
        return amount;
    }
    //统计总金额
    public int totalAccount(){
        int account=0;
        for (Map.Entry<Integer,Integer> e :car.entrySet()) {
            String id=e.getKey()+"";
            try {
                Good good=goodService.findGoodById(id,0);
                account+=good.getPrice()*e.getValue();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return account;
    }
}
