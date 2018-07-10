package com.cyk.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * list
 *
 * @author zhangshipeng
 * @ClassName: GoodsColorController
 * @date 2017-08-26 19:05
 **/
@SpringBootApplication
//@RestController
@EnableAutoConfiguration
@Controller
public class EsStartController extends SpringBootServletInitializer {

    Logger LOG = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = {"/", "index"})
    @ResponseBody
    public String index() {

//        elasticsearchTemplate.deleteIndex("cyk-goods-index");
//        elasticsearchTemplate.createIndex(ESSpuIndexEntity.class);


        return "index";
    }





    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EsStartController.class);
    }


    public static void main(String[] args) {
        System.out.println("devtools：是spring boot的一个热部署工具");
        // devtools：是spring boot的一个热部署工具
        //设置 spring.devtools.restart.enabled 属性为false，可以关闭该特性.
        //System.setProperty("spring.devtools.restart.enabled","false");

        // 启动Sprign Boot
        ApplicationContext ctx = SpringApplication.run(EsStartController.class, args);
        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
//            System.out.println("beanName:"+beanName);
        }
    }


}
