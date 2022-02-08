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
    //��ӹ��ﳵ
    public void addGood(Integer id){
        if (car==null){
            car=new HashMap<>();
        }
        car.put(id,1);
    }
    //ɾ�����ﳵ
    public void removeGood(Integer id){

        car.remove(id);
    }
    //��չ��ﳵ
    public void clear(){
        car.clear();
    }
    //�޸Ĺ��ﳵ
    public void modify(String[] ids,String[] amounts){
        for (int i = 0; i < ids.length; i++) {
            String id=ids[i];
            car.put(Integer.valueOf(id),Integer.valueOf(amounts[i]));
        }

    }
    //�ѹ��ﳵת��Ϊ��Ʒ���б�List<Good>
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
    //ͳ��������
    public int totalAmount(){
        int amount=0;
        for (Map.Entry<Integer,Integer> e :car.entrySet()) {
            amount+=e.getValue();
        }
        return amount;
    }
    //ͳ���ܽ��
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
