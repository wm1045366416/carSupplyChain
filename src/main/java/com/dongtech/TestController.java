package com.dongtech;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: dongbao
 * @Date: 2018/9/3 10:25
 * @Description:
 */
@Controller
public class TestController {

    //http://localhost:8081/index
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    public static void main(String[] args){
        List list = new ArrayList();
        System.out.println(list+"--"+System.currentTimeMillis());
    }
}
