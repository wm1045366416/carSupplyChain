package com.dongtech.dao.impl;


import com.dongtech.dao.CarGoodsDao;
import com.dongtech.util.CommonUtil;
import com.dongtech.util.JDBCUtil;
import com.dongtech.vo.*;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据层，只负责与数据库的数据交互，将数据进行存储读取操作
 */
public class CarGoodsDaoImpl implements CarGoodsDao {


    @Override
    public List<CarGoods> queryList(CarGoods carGoods) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarGoods> bookList = new ArrayList<CarGoods>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM cargoods where 1=1");
            if(!StringUtils.isEmpty(carGoods.getId())){
                sql.append(" and id =").append(carGoods.getId());
            }
            if(!StringUtils.isEmpty(carGoods.getName())){
                sql.append("  and name like '%").append(carGoods.getName()).append("%'");
            }
            if(!StringUtils.isEmpty(carGoods.getType())){
                sql.append("  and type='").append(carGoods.getType()).append("'");
            }
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarGoods vo = new CarGoods(rs.getLong("id"),
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("produce"),
                        rs.getBigDecimal("price"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("num")

                );
                bookList.add(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return bookList;
    }

    /**
     * @Author gzl
     * @Description：查询订单信息
     * @Exception
     * @Date： 2020/4/20 12:04 AM
     */
    @Override
    public List<CarOrders> queryOrders() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrders> carOrdersList = new ArrayList<CarOrders>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM car_orders where 1=1 and state !='0' ");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrders vo = new CarOrders(rs.getLong("id"),
                        rs.getString("number"),
                        rs.getBigDecimal("price")

                );
                carOrdersList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrdersList;
    }
    public boolean deleteOrders(Integer id){
        Connection conn = null;
        PreparedStatement ps = null;
        boolean rs = true;
        List<CarOrders> carOrdersList = new ArrayList<CarOrders>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("update car_orders set state='0' where id=? ");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1,id);
            rs = ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close( ps, conn);
        }
        return rs;
    }

    /**
     * @Author gzl
     * @Description：查询订单详情
     * @Exception
     * @Date： 2020/4/20 12:17 AM
     */
    @Override
    public List<CarOrderDetails> queryOrdersDetails(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrderDetails> carOrderDetailsList = new ArrayList<CarOrderDetails>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM car_orders_details where 1=1");
            if(!StringUtils.isEmpty(id)){
                sql.append(" and order_id =").append(id);
            }
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrderDetails vo = new CarOrderDetails(rs.getLong("id"),
                        rs.getString("goods_name"),
                        rs.getInt("num"),
                        rs.getString("produce"),
                        rs.getBigDecimal("price"),
                        rs.getInt("order_id")

                );
                carOrderDetailsList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrderDetailsList;
    }
    @Override
    public boolean saveOrders(List<Cart> list){
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement p1 = null;
        PreparedStatement p2 = null;
        ResultSet resultSet = null;
        double price = 0d;
        for(Cart cart:list){
            double d = cart.getPrice()*cart.getNum();
            price+=d;
        }
        String number = CommonUtil.getNumber("ORDER");
        int id = 0;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            String str = "select max(id) as id from car_orders";
            p2 = conn.prepareStatement(str);
            resultSet = p2.executeQuery();
            while (resultSet.next()){
                id = resultSet.getInt("id");
                id++;
            }
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO car_orders (id,number,price,state)");
            sql.append(" VALUES (?,?,?,'1')");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            ps.setObject(1,id);
            ps.setObject(2,number);
            ps.setObject(3,price);
            ps.execute();
            //插入订单详情表
            String sql1 = "insert into car_orders_details (goods_name,num,produce,price,order_id) values (?,?,?,?,?)";
            p1 = conn.prepareStatement(sql1);
            for(Cart cart:list){
                p1.setObject(1,cart.getName());
                p1.setObject(2,cart.getNum());
                p1.setObject(3,cart.getProduce());
                p1.setObject(4,cart.getPrice());
                p1.setObject(5,id);
                p1.addBatch();
            }
            int[] row = p1.executeBatch();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close( ps, conn);
        }
        //return bookList;
        return true;
    }



    public void saveOrdersDetails(String goods_name,int num,String produce ,int order_id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
                String sql = "INSERT INTO jk_pro_db.car_orders_details(goods_name, num,produce,order_id) values (?,?,?,?)";
                ps = conn.prepareStatement(sql);
                long randomNum = System.currentTimeMillis();
                ps.setString(1, goods_name);
                ps.setInt(2,num);
                ps.setString(3, produce);
                ps.setInt(4,order_id);
                ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
    }
    @Override
    public TearDownDetails queryTearDownDetails(CarOrderDetails car){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TearDownDetails downDetails = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
            String sql = "select * from  jk_pro_db.tear_down_details where order_id=? and produce=? and cargoods_name=?";
            ps = conn.prepareStatement(sql);
            long randomNum = System.currentTimeMillis();
            ps.setInt(1, car.getOrderId());
            ps.setObject(2,car.getProduce());
            ps.setObject(3, car.getGoodsname());
            //ps.addBatch();
            rs = ps.executeQuery();
            while (rs!=null&&rs.next()){
                downDetails = new TearDownDetails();
                downDetails.setOrderId(rs.getInt("order_id"));
                downDetails.setNum(rs.getInt("num"));
                downDetails.setId(rs.getLong("id"));
            }
            return downDetails;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return downDetails;
    }
    @Override
    public boolean saveTeamDownDetail(TearDownDetails t){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TearDownDetails downDetails = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
            String sql = "insert into jk_pro_db.tear_down_details (order_id,produce,cargoods_name,num) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            long randomNum = System.currentTimeMillis();
            ps.setInt(1, t.getOrderId());
            ps.setObject(2,t.getProduce());
            ps.setObject(3, t.getCargoods_name());
            ps.setInt(4, t.getNum());
            ps.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return true;
    }
    @Override
    public boolean updateTeamDownDetail(TearDownDetails t){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TearDownDetails downDetails = null;
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            final int[] totalprice = {0};
            String sql = "update jk_pro_db.tear_down_details set num = ? where id = ?";
            ps = conn.prepareStatement(sql);
            long randomNum = System.currentTimeMillis();
            ps.setInt(1, t.getNum());
            ps.setLong(2,t.getId());
            ps.execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return true;
    }
    @Override
    public List<CarOrderDetails> queryDetails() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CarOrderDetails> carOrderDetailsList = new ArrayList<CarOrderDetails>();
        try {
            //1 加载数据库驱动  2 获取数据库连接
            conn = JDBCUtil.getMysqlConn();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT goods_name,sum(num) num,sum(price) price FROM car_orders_details group by goods_name");
            //3 操作数据库——查询一条数据记录
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            //4 处理返回数据——将返回的一条记录封装到一个JavaBean对象
            while (rs.next()) {
                CarOrderDetails vo = new CarOrderDetails();
                vo.setGoodsname(rs.getString("goods_name"));
                vo.setNum(rs.getInt("num"));
                vo.setPrice(rs.getBigDecimal("price"));
                carOrderDetailsList.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5 关闭连接
            JDBCUtil.close(rs, ps, conn);
        }
        return carOrderDetailsList;
    }
}
