package com.dongtech.service;

import com.dongtech.vo.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
@Service
public interface CarVGoodsService {

    List<CarGoods> queryList(CarGoods carGoods) throws SQLException;



    List<CarOrders> queryOrders();

    List<CarOrderDetails> queryOrdersDetails(Integer id);

    boolean saveOrders(List<Cart> list);

    TearDownDetails queryTearDownDetails(CarOrderDetails car);
    boolean saveTeamDownDetail(TearDownDetails t);
    boolean deleteOrders(Integer id);
    public boolean updateTeamDownDetail(TearDownDetails t);
    public List<CarOrderDetails> queryDetails();
}
