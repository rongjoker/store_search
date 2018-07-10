//package com.cyk.es.test;/**
// * Created by zhangshipeng on 2018/1/5.
// */
//
//import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
//import org.jasypt.encryption.StringEncryptor;
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.junit.Test;
//
///**
// * test jasypt
// *
// * @author Administrator
// * @ClassName: JasyptMain
// * @date 2018-01-05 15:55
// **/
//public class JasyptMain {
//
//
//    @Test
//    public void encryptPwd() {
//
//        //每次加密结果不一样
//        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        encryptor.setPassword("e9fbdb2d3b21");
//        String result = encryptor.encrypt("cyk%2017");
//        System.out.println(result);
//    }
//}
