package com.cyk.es.test;/**
 * Created by zhangshipeng on 2017/9/20.
 */

import com.cyk.es.EsStartController;
import com.cyk.es.dto.SpuSearchDto;
import com.cyk.es.entity.ESSpuIndexEntity;
import com.cyk.es.service.SpuIndexService;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @ClassName: TestMain
 * @date 2017-09-20 14:30
 **/
@RunWith(SpringJUnit4ClassRunner.class)   //1.
@SpringBootTest(classes = EsStartController.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT )
public class TestMain {



    @Resource
    private SpuIndexService spuIndexService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;







//    @Test
//    public void test3(){
//
//
//        Pageable pageable = new PageRequest(0, 10);
//
//
////        Sort sort=SortBuilders.fieldSort("fieldName").order(SortOrder.DESC);
//
//
//        Sort sort=new Sort(new Sort.Order(Sort.Direction.ASC,"goodsId"));
//
//        SortBuilder sortBuilder = new FieldSortBuilder("goodsId").order(SortOrder.ASC);
//
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//
////        boolQueryBuilder.must(QueryBuilders.matchQuery("goodsInfoName","衬衫"));
//
////        boolQueryBuilder.must(QueryBuilders.matchQuery("thirdName","雪阳"));
//
////        boolQueryBuilder.must(QueryBuilders.termQuery("thirdId",9));
//
////        boolQueryBuilder.filter(QueryBuilders.termQuery("thirdId",9));
////        boolQueryBuilder.filter(QueryBuilders.termQuery("brand.brandId",6));
//
//
//        List<AggregationBuilder> aggregationBuilderList =Lists.newArrayList();
//
//        AggregationBuilder ab=AggregationBuilders.terms("params").field("paramList.expandparamName").exclude("").subAggregation(AggregationBuilders.terms("paramValues").field("paramList.expandparamValueName").exclude(""))
//
//                .order(Terms.Order.count(false)).size(10);
//
//
//
//        TermsBuilder tb= AggregationBuilders.terms("brands")
//                .field("brand.brandName").order(Terms.Order.count(false)).size(10);    //yanan7890是为聚合取的名称,false降序,true升序
//
//
////        TermsBuilder tb2= AggregationBuilders.terms("params")
////                .field("paramList.expandparamName").order(Terms.Order.count(false)).size(10);
//
//
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(boolQueryBuilder)
////                .withQuery(QueryBuilders.termQuery("message", "message"))
////                .withFilter(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("id", documentId)))
//                .withFilter(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("brand.brandId",6)))
////                .withFilter(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("brandId",6)))
//                .addAggregation(tb)
//                .addAggregation(ab)
//                .withPageable(pageable)
//                .withSort(sortBuilder)
//                .build();
//
//
//
//
//        StringQuery query = new StringQuery(boolQueryBuilder.toString(),pageable,sort);
//
//
//        System.out.println( searchQuery.getFilter());
//
//        AggregatedPage<ESSpuIndexEntity> aggregatedPage=this.elasticsearchTemplate.queryForPage(searchQuery,ESSpuIndexEntity.class);
//
//        aggregatedPage.forEach(entity -> System.out.println(entity.getGoodsId()+":"+ entity.getBrand().getBrandId()+":"+entity.getBrand().getBrandName()+":"+entity.getGoodsInfoName()+":"+entity.getParamList()));
//
//        System.out.println("-------------------------------------------------------");
//
////        aggregatedPage.getAggregation("").get;
//
//        Aggregations aggregations = aggregatedPage.getAggregations();
//
////        LongTerms brands = (LongTerms)aggregations.asMap().get("brands");
//        StringTerms brands = (StringTerms)aggregations.asMap().get("brands");
//        if(null!=brands)
//            brands.getBuckets().forEach(bucket-> System.out.println(bucket.getKey()+":"+bucket.getDocCount()));
//
//
//        System.out.println("-------------------------------------------------------");
//
//
//
//        StringTerms params = (StringTerms)aggregations.asMap().get("params");
//        if(null!=params){
//
//            params.getBuckets().forEach(bucket-> {
//
//                System.out.println(bucket.getKey()+":"+bucket.getDocCount()+":");
//
//                StringTerms paramValues = (StringTerms)bucket.getAggregations().get("paramValues");
//
//                if(null!=paramValues){
//
//                    paramValues.getBuckets().forEach(bk->{
//
//                        System.out.println("--"+bk.getKey()+":"+bk.getDocCount());
//                    });
//
//
//                }
//
//
//
//            });
//
//        };
//
//
////        Page<ESSpuIndexEntity> list=this.elasticsearchTemplate.queryForPage(query,ESSpuIndexEntity.class);
////        Page<ESSpuIndexEntity> list=this.elasticsearchTemplate.queryForPage(searchQuery,ESSpuIndexEntity.class);
////        System.out.println(list.getTotalElements()+":"+list.getTotalPages());
////        list.forEach(entity -> System.out.println(entity.getGoodsId()+":"+ entity.getBrand().getBrandId()+":"+entity.getBrand().getBrandName()+":"+entity.getGoodsInfoName()));
//
//
//
//
//
//
//
//
//
//    }



    @Test
    public void  test2(){
        //cyk-goods-index
        elasticsearchTemplate.deleteIndex("cyk-goods-index");
//        elasticsearchTemplate.deleteIndex(ESSpuIndexEntity.class);


        ESSpuIndexEntity entity = new ESSpuIndexEntity();
        entity.setGoodsId(2L);
        entity.setGoodsName("firstOne");
        entity.setGoodsInfoName("红色的好看");
        entity.setGoodsAddedTime(new Date());

//        List<EsThirdCateEntity> cates = Lists.newArrayList();
//        EsThirdCateEntity cate = new EsThirdCateEntity();
//        cate.setCatId(155L);
//        cate.setCatName("长裤");
//        cates.add(cate);
//        entity.setThirdCateList(cates);


//        spuIndexService.save(entity);


//        System.out.println(elasticsearchTemplate.getSetting(ESSpuIndexEntity.class).toString());
        System.out.println("------------删除索引1---------------");
//        elasticsearchTemplate.deleteIndex(ESSpuIndexEntity.class);


        System.out.println("------------删除索引2---------------");






    }








    @Test
    public void test1(){

        List<String> list = Lists.newArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        List<String> list2 = Lists.newArrayList();

        list.forEach(str -> list2.add(str));

        System.out.println(list2);


        SpuSearchDto searchDto = new SpuSearchDto();
        searchDto.setPage(0);
        searchDto.setSize(10);
        searchDto.setGoodsInfoName("衬衫");
        searchDto.setThirdId(9L);;


        Page<ESSpuIndexEntity> lists =spuIndexService.querySpus(searchDto);

        System.out.println(lists.getTotalElements()+":"+lists.getTotalPages()+":"+lists.getNumber());

        lists.forEach(entity-> System.out.println(entity.toString()));





    }


    public static void main(String[] args) {

//        test1();
    }

}
