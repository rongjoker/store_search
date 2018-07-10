package com.cyk.es.test;/**
 * Created by zhangshipeng on 2017/9/28.
 */

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.time.Clock;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangshipeng
 * @ClassName: TestReference
 * @date 2017-09-28 14:42
 **/
public class TestReference {


    @Test
    public void test1(){

        SoftReference<String> stringSoftReference = new SoftReference<String>(new String("i am soft Reference"));

        System.out.println(stringSoftReference.get());


        WeakReference<List<String>> weakReference = new WeakReference<List<String>>(new LinkedList<String>(){

            {


                add("joker123");
            }



        });

        System.out.println(weakReference.get());

        System.gc();

        System.out.println(weakReference.get());


        WeakReference<String> weakReference1 = new WeakReference<String>(new String("rongjoker"));


        System.out.println(weakReference1.get());

        System.gc();

        System.out.println(weakReference1.get());








    }

    @Test
    public void testblank(){
        String u = null;
        String u2 = "";
        String u3 = " ";
        String u4 = " ";

        System.out.println(StringUtils.isNotBlank(u));
        System.out.println(StringUtils.isNotBlank(u2));
        System.out.println(StringUtils.isNotBlank(u3));
        System.out.println(StringUtils.isNotBlank(u4));



    }



    @Test
    public void test2(){

        Clock.systemDefaultZone().millis();
        Clock.system(ZoneId.of("Asia/Shanghai"));


        Runnable runnable = () -> System.out.println("i am now color");


        runnable.run();


        new Thread(()-> System.out.println("i am here")).start();






    }


    public void test3(String u){
        System.out.println("u:"+u);
    }


    @Test
    public void test4(){

        List<String> uu = new ArrayList<String>(){

            {

                add("u2");
                add("u3");

            }

        };

        List<String> result =uu.stream().filter(line -> !"u2".equals(line)).collect(Collectors.toList());


        result.forEach(System.out::print);



    }

}
