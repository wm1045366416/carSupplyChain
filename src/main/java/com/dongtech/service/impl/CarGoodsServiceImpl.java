package com.dongtech.service.impl;


import com.dongtech.dao.CarGoodsDao;
import com.dongtech.dao.impl.CarGoodsDaoImpl;
import com.dongtech.service.CarVGoodsService;
import com.dongtech.vo.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CarGoodsServiceImpl implements CarVGoodsService {

    CarGoodsDao dao = new CarGoodsDaoImpl();


    @Override
    public List<CarGoods> queryList(CarGoods carGoods) throws SQLException {
        return dao.queryList(carGoods);
    }

    @Override
    public List<CarOrders> queryOrders() {
        return dao.queryOrders();
    }

    @Override
    public List<CarOrderDetails> queryOrdersDetails(Integer id) {
        return dao.queryOrdersDetails(id);
    }
    @Override
    public boolean saveOrders(List<Cart> list){
        return dao.saveOrders(list);
    }
    @Override
    public TearDownDetails queryTearDownDetails(CarOrderDetails car){
        return dao.queryTearDownDetails(car);
    }
    @Override
    public boolean saveTeamDownDetail(TearDownDetails t){
        return dao.saveTeamDownDetail(t);
    }
    @Override
    public boolean updateTeamDownDetail(TearDownDetails t){
        return dao.updateTeamDownDetail(t);
    }
    @Override
    public boolean deleteOrders(Integer id){
        return dao.deleteOrders(id);
    }
    @Override
    public List<CarOrderDetails> queryDetails(){
        return dao.queryDetails();
    }
}
