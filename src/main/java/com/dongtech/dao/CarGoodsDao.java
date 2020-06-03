package com.dongtech.dao;


import com.dongtech.vo.*;

import java.util.List;

public interface CarGoodsDao {
    List<CarGoods> queryList(CarGoods carGoods) ;

    List<CarOrders> queryOrders();
    boolean deleteOrders(Integer id);
    List<CarOrderDetails> queryOrdersDetails(Integer id);
    TearDownDetails queryTearDownDetails(CarOrderDetails car);
    boolean saveOrders(List<Cart> list);
    boolean saveTeamDownDetail(TearDownDetails t);
    public boolean updateTeamDownDetail(TearDownDetails t);
    public List<CarOrderDetails> queryDetails();
}
