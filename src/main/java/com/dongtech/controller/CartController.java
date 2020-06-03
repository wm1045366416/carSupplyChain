package com.dongtech.controller;

import com.dongtech.service.CarVGoodsService;
import com.dongtech.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: springboot-jsp
 * @description: ${description}
 */
@Controller
@RequestMapping("cartController")
public class CartController {


    @Resource
    private  CarVGoodsService carVGoodsService;
    @RequestMapping("/addGoodsToCart")
    @ResponseBody
    public ModelAndView addGoodsToCart (Integer goodsId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //从cookie中获取购物车列表
        ModelAndView modelAndView = new ModelAndView();
        List<Cart> carVos = getCartInCookie(response, request);
        Cookie cookie_2st;
        CarGoods carGoods = new CarGoods();
        try {
            CarGoods carGoods1 = new CarGoods();
            carGoods1.setId(Long.parseLong(goodsId + ""));
            List<CarGoods> cList = carVGoodsService.queryList(carGoods1);
            carGoods = cList.get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果购物车列表为空
        if (carVos.size() <= 0) {
            Cart carVo = new Cart();
            carVo.setNum(1);
            carVo.setPrice(carGoods.getPrice().intValue());
            carVo.setId(carGoods.getId());
            carVo.setType(carGoods.getType());
            carVo.setName(carGoods.getName());
            carVo.setProduce(carGoods.getProduce());
            carVo.setDescription(carGoods.getDescription());
            //将当前传来的商品添加到购物车列表
            carVos.add(carVo);
            //将当前传来的商品添加到购物车
            if (getCookie(request) == null) {
                //制作购物车cookie数据
                cookie_2st = new Cookie("cart", URLEncoder.encode(makeCookieValue(carVos), "UTF-8"));
                //设置在该项目下都可以访问该cookie
                cookie_2st.setPath("/");
                //设置cookie有效时间
                cookie_2st.setMaxAge(60 * 30);
                //添加cookie
                response.addCookie(cookie_2st);
            } else {
                cookie_2st = getCookie(request);
                cookie_2st.setPath("/");
                //设置cookie有效时间
                cookie_2st.setMaxAge(60 * 30);
                cookie_2st.setValue(URLEncoder.encode(makeCookieValue(carVos)));
                //添加cookie
                response.addCookie(cookie_2st);
            }
        } else {
            int bj = 0;
            for(Cart cart:carVos){
                if(String.valueOf(cart.getId()).equals(String.valueOf(goodsId))){
                    cart.setNum(cart.getNum()+1);
                    bj=1;
                    break;
                }
            }
            if(bj==0){
                Cart cartVo = new Cart();
                cartVo.setPrice(carGoods.getPrice().intValue());
                cartVo.setId(carGoods.getId());
                cartVo.setType(carGoods.getType());
                cartVo.setName(carGoods.getName());
                cartVo.setNum(1);
                cartVo.setProduce(carGoods.getProduce());
                cartVo.setDescription(carGoods.getDescription());
                carVos.add(cartVo);
            }
            cookie_2st = getCookie(request);
            //设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            //设置cookie有效时间
            cookie_2st.setMaxAge(60 * 30);
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(carVos)));
            //添加cookie
            response.addCookie(cookie_2st);
        }
        modelAndView.addObject("list",carVos);
        modelAndView.setViewName("carGoods/carlist");
        return modelAndView;
    }

    @RequestMapping("/getCart")
    public ModelAndView addGoodsToCart (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        ModelAndView modelAndView = new ModelAndView();
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",list);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/carlist");
        return modelAndView;
    }

    @RequestMapping("/addOrder")
    public ModelAndView addOrder (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        carVGoodsService.saveOrders(list);

        ModelAndView modelAndView = new ModelAndView();
        List<CarGoods> list1 = new ArrayList<>();
        try{
            CarGoods carGoods = new CarGoods();
            list1 = carVGoodsService.queryList(carGoods);
            Cookie cookie_2st = getCookie(request);
            cookie_2st.setPath("/");
            //设置cookie有效时间
            cookie_2st.setMaxAge(0);
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(list)));
            //添加cookie
            response.addCookie(cookie_2st);
        }catch (Exception e){
            e.printStackTrace();
        }
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",list1);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/list");
        return modelAndView;
    }
    @RequestMapping("/teardowndetail")
    public ModelAndView teardowndetail (Integer id,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        List<CarOrderDetails> carOrderDetails = carVGoodsService.queryOrdersDetails(id);
        for(CarOrderDetails item:carOrderDetails){
            TearDownDetails downDetails = carVGoodsService.queryTearDownDetails(item);
            if(downDetails!=null&&downDetails.getNum()>0){
                downDetails.setNum(downDetails.getNum()+item.getNum());
                carVGoodsService.updateTeamDownDetail(downDetails);
            }else{
                TearDownDetails t = new TearDownDetails();
                t.setNum(item.getNum());
                t.setCargoods_name(item.getGoodsname());
                t.setProduce(item.getProduce());
                t.setOrderId(item.getOrderId());
                carVGoodsService.saveTeamDownDetail(t);
            }
        }
        carVGoodsService.deleteOrders(id);
        ModelAndView modelAndView = new ModelAndView();
        List<CarOrders> list1 = new ArrayList<>();
        try{
            CarGoods carGoods = new CarGoods();
            list1 = carVGoodsService.queryOrders();
        }catch (Exception e){
            e.printStackTrace();
        }
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",list1);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/orderlist");
        return modelAndView;
    }

    @RequestMapping("/orderdetail")
    public ModelAndView orderdetail (Integer id,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        List<CarOrderDetails> carOrderDetails = carVGoodsService.queryOrdersDetails(id);
        ModelAndView modelAndView = new ModelAndView();
        List<CarOrderDetails> list1 = new ArrayList<>();
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",carOrderDetails);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/orderdetailslist");
        return modelAndView;
    }
    @RequestMapping("/deleteAllCookie")
    public ModelAndView deleteAllCookie (HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        ModelAndView modelAndView = new ModelAndView();
        List<Cart> list1 = new ArrayList<>();
        Cookie cookie_2st = null;
        try{
            cookie_2st = getCookie(request);
            //设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            //设置cookie有效时间
            cookie_2st.setMaxAge(0);
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(list)));
            //添加cookie
            response.addCookie(cookie_2st);
        }catch (Exception e){
            e.printStackTrace();
        }
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",list1);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/carlist");
        return modelAndView;
    }
    @RequestMapping("/deleteGoods")
    public ModelAndView deleteGoods (Integer goodsId,HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        List<Cart> list = getCartInCookie(response,request);
        List<Cart> list1 = new ArrayList();
        if (list.size() > 0) {
            for(Cart cart:list){
                if(String.valueOf(cart.getId()).equals(String.valueOf(goodsId))){

                }else{
                    list1.add(cart);
                }
            }
        }
        Cookie cookie_2st;
        if(list1!=null&&list1.size()>0){
            cookie_2st = getCookie(request);
            //设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            //设置cookie有效时间
            cookie_2st.setMaxAge(60 * 30);
            cookie_2st.setValue(URLEncoder.encode(makeCookieValue(list1)));
            //添加cookie
            response.addCookie(cookie_2st);
        }else{
            cookie_2st = getCookie(request);
            //设置在该项目下都可以访问该cookie
            cookie_2st.setPath("/");
            //设置cookie有效时间
            cookie_2st.setMaxAge(0);
            response.addCookie(cookie_2st);
        }
        ModelAndView modelAndView = new ModelAndView();
        //将返回的页面数据放入到模型和试图对象中
        modelAndView.addObject("list",list1);
        //指定返回的页面位置
        modelAndView.setViewName("carGoods/carlist");
        return modelAndView;
    }

    /**
     * 获取cookie中的购物车列表
     *
     * @param response
     * @param request
     * @return 购物车列表
     * @throws UnsupportedEncodingException 抛出异常
     */
    public List<Cart> getCartInCookie(HttpServletResponse response, HttpServletRequest request) throws
            UnsupportedEncodingException {
        // 定义空的购物车列表
        List<Cart> items = new ArrayList<>();
        String value_1st ;
        // 购物cookie
        Cookie cart_cookie = getCookie(request);
        // 判断cookie是否为空
        if (cart_cookie != null) {
            // 获取cookie中String类型的value,从cookie获取购物车
            value_1st = URLDecoder.decode(cart_cookie.getValue(), "utf-8");
            // 判断value是否为空或者""字符串
            if (value_1st != null && !"".equals(value_1st)) {
                // 解析字符串中的数据为对象并封装至list中返回给上一级
                String[] arr_1st = value_1st.split("==");
                for (String value_2st : arr_1st) {
                    String[] arr_2st = value_2st.split("=");
                    Cart item = new Cart();
                    item.setId(Long.parseLong(arr_2st[0])); //商品id
                    item.setType(arr_2st[1]); //商品类型ID
                    item.setName(arr_2st[2]); //商品名
                    item.setDescription(arr_2st[4]);//商品详情
                    item.setPrice(Integer.parseInt(arr_2st[3])); //商品市场价格
                    if(arr_2st.length>6){
                        item.setProduce(arr_2st[6]);
                    }
                    String num = "null".equals(arr_2st[5])?"1":arr_2st[5];
                    item.setNum(Integer.parseInt(num));//加入购物车数量
                    items.add(item);
                }
            }
        }
        return items;

    }

    /**
     * 获取名为"cart"的cookie
     *
     * @param request
     * @return cookie
     */
    public Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cart_cookie = null;
        for (Cookie cookie : cookies) {
            //获取购物车cookie
            if ("cart".equals(cookie.getName())) {
                cart_cookie = cookie;
            }
        }
        return cart_cookie;
    }

    /**
     * 制作cookie所需value
     *
     * @param cartVos 购物车列表
     * @return 解析为字符串的购物车列表，属性间使用"="相隔，对象间使用"=="相隔
     */
    public String makeCookieValue(List<Cart> cartVos) {
        StringBuffer buffer_2st = new StringBuffer();
        for (Cart item : cartVos) {
            buffer_2st.append(item.getId() + "=" + item.getType() + "=" + item.getName() + "="
                    + item.getPrice() + "=" + item.getDescription() +"="+ item.getNum() + "=" +item.getProduce()+ "==");
        }
        return buffer_2st.toString().substring(0, buffer_2st.toString().length() - 2);
    }


}
