package com.cyk.es.controller;/**
 * Created by zhangshipeng on 2017/9/22.
 */

import com.cyk.es.dto.SpuSearchDto;
import com.cyk.es.entity.ESSpuIndexEntity;
import com.cyk.es.entity.EsThirdCateEntity;
import com.cyk.es.service.SpuIndexService;
import com.cyk.es.vo.HttpEntityVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.Subject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author zhangshipeng
 * @ClassName: 商品索引相关
 * @date 2017-09-22 14:37
 **/
@Controller
public class SpuEsController {

    Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

//    @Resource
//    private SpuIndexRepository spuIndexRepository;

    @Resource
    private  SpuIndexService spuIndexService;

    @RequestMapping(value = {"/delete"})
    @ResponseBody
    public String delete() {

        elasticsearchTemplate.deleteIndex("cyk-goods-index");

        return "delete";
    }

    @RequestMapping(value = {"/create"})
    @ResponseBody
    public String createIndex() {

        elasticsearchTemplate.createIndex(ESSpuIndexEntity.class);
        elasticsearchTemplate.putMapping(ESSpuIndexEntity.class);
        elasticsearchTemplate.refresh(ESSpuIndexEntity.class);

        return "create";
    }


    @RequestMapping(value = {"/clear"})
    @ResponseBody
    public String clearIndex() {

        elasticsearchTemplate.delete(new DeleteQuery(),ESSpuIndexEntity.class);
        elasticsearchTemplate.refresh(ESSpuIndexEntity.class);

        return "clear";
    }


//    @RequestMapping(value = {"/goods/{goodsId}"},method = RequestMethod.POST)
//    @ResponseBody
//    public HttpEntityVo addIndex(@PathVariable Long goodsId) {
//
//        HttpEntityVo httpEntityVo = new HttpEntityVo();
//        try {
//            ESSpuIndexEntity entity = spuIndexService.insertOneGoodsIndexToEs(Optional.ofNullable(goodsId));
//            if (entity == null){
//                httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                httpEntityVo.setMessage("查询不到相关数据，添加商品索引失败!");
//            }else{
//                httpEntityVo.setStatus(HttpStatus.OK.value());
//                httpEntityVo.putData("entity",entity);
//                httpEntityVo.setMessage("添加索引成功!");
//            }
//
//        } catch (Exception e) {
//            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            httpEntityVo.setMessage("添加商品索引失败!");
//            httpEntityVo.setErrorCode(e.getMessage());
//        }
//        return httpEntityVo;
//    }



    @RequestMapping(value = {"/goods"},method = RequestMethod.POST)
    @ResponseBody
    public HttpEntityVo bulkIndex( String goodsId) {

        HttpEntityVo httpEntityVo = new HttpEntityVo();
        try {

            if(StringUtils.isBlank(goodsId))
                throw new NullPointerException("商品ID为空");

            int size = spuIndexService.bulkInsertGoodsIndexToEs(goodsId);
            httpEntityVo.setStatus(HttpStatus.OK.value());
            httpEntityVo.putData("size",size);
            httpEntityVo.setMessage("添加索引成功!");

        } catch (Exception e) {
            e.printStackTrace();
            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpEntityVo.setMessage("添加商品索引失败!");
            httpEntityVo.setErrorCode(e.getMessage());
        }
        return httpEntityVo;
    }




    @RequestMapping(value = {"/reIndex"},method = RequestMethod.GET)
    @ResponseBody
    public HttpEntityVo reIndex() {

        Long startTime = System.currentTimeMillis();

        HttpEntityVo httpEntityVo = new HttpEntityVo();
        try {
            Integer num = spuIndexService.createSpuGoodsIndexToEs();

            Long endTime = System.currentTimeMillis();

            httpEntityVo.setStatus(HttpStatus.OK.value());
            httpEntityVo.putData("num",num);
            httpEntityVo.putData("index.time.second",(endTime-startTime)/1000);
            httpEntityVo.setMessage("添加索引成功!");

        } catch (Exception e) {
            e.printStackTrace();
            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpEntityVo.setMessage("添加商品索引失败!");
            httpEntityVo.setErrorCode(e.getMessage());
        }
        return httpEntityVo;
    }


    @RequestMapping(value = {"/goods/{goodsId}"},method = RequestMethod.GET)
    @ResponseBody
    public HttpEntityVo goods(@PathVariable Long goodsId) {


        HttpEntityVo httpEntityVo = new HttpEntityVo();
        try {

            String querys=QueryBuilders.boolQuery().must(QueryBuilders.termQuery("_id",  goodsId)).toString();

            LOG.info("querys:{}",querys);

            ESSpuIndexEntity entity=
//                    this.spuIndexRepository.findOne(goodsId);

//            this.elasticsearchTemplate.queryForObject(new StringQuery(querys,new PageRequest(0, 1)),ESSpuIndexEntity.class);
                    this.elasticsearchTemplate.queryForObject(new StringQuery(querys,PageRequest.of(0,1)),ESSpuIndexEntity.class);


            httpEntityVo.putData("entity",entity);
            httpEntityVo.setMessage("查询商品索引成功!");

        } catch (Exception e) {
            e.printStackTrace();
            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpEntityVo.setMessage("查询商品索引失败!");
            httpEntityVo.setErrorCode(e.getMessage());
        }
        return httpEntityVo;
    }

    @RequestMapping(value = {"/goods/all/{size}"},method = RequestMethod.GET)
    @ResponseBody
    public HttpEntityVo goods(@PathVariable Integer size) {


        HttpEntityVo httpEntityVo = new HttpEntityVo();
        try {

//            Pageable pageable = new PageRequest(0, size);
            Page<ESSpuIndexEntity> list=
//                    this.spuIndexRepository.findAll(pageable);
            this.elasticsearchTemplate.queryForPage(new StringQuery(QueryBuilders.boolQuery().toString(),PageRequest.of(0,1)),ESSpuIndexEntity.class);


            httpEntityVo.putData("content",list.getContent());
            httpEntityVo.putData("total",list.getTotalElements());
            httpEntityVo.putData("size",list.getContent().size());
            httpEntityVo.setMessage("查询商品索引成功!");

        } catch (Exception e) {
            e.printStackTrace();
            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpEntityVo.setMessage("查询商品索引失败!");
            httpEntityVo.setErrorCode(e.getMessage());
        }
        return httpEntityVo;
    }


    @RequestMapping(value = {"/spu/search"},method = RequestMethod.POST)
    @ResponseBody
    public HttpEntityVo search(@RequestBody SpuSearchDto searchDto) {


        HttpEntityVo httpEntityVo = new HttpEntityVo();
        try {

            AggregatedPage<ESSpuIndexEntity> list=this.spuIndexService.querySpus(searchDto);

            httpEntityVo.putData("content",list.getContent());
            httpEntityVo.putData("total",list.getTotalElements());
            httpEntityVo.putData("size",list.getContent().size());

            if(searchDto.getAggregation()){

                Aggregations aggregations = list.getAggregations();

                StringTerms brands = (StringTerms)aggregations.asMap().get("brands");
                if(null!=brands){
                    Map<String,Long> brandsMap = Maps.newLinkedHashMap();
                    brands.getBuckets().forEach(bucket-> brandsMap.put(bucket.getKey().toString(),bucket.getDocCount()));
                    httpEntityVo.putData("brandsCount",brandsMap);
                }



                StringTerms cates = (StringTerms)aggregations.asMap().get("cates");
                if(null!=cates){
                    Map<String,Long> catesMap = Maps.newLinkedHashMap();
                    cates.getBuckets().forEach(bucket-> catesMap.put(bucket.getKey().toString(),bucket.getDocCount()));
                    httpEntityVo.putData("catesCount",catesMap);
                }




                StringTerms params = (StringTerms)aggregations.asMap().get("params");
                if(null!=params){

                    Map<String,Object> paramsMap = Maps.newLinkedHashMap();
                    params.getBuckets().forEach(bucket-> {
                        paramsMap.put(bucket.getKey().toString(),bucket.getDocCount());

                        StringTerms paramValues = (StringTerms)bucket.getAggregations().get("paramValues");
                        if(null!=paramValues){
                            Map<String,Object> paramValuesMap = Maps.newLinkedHashMap();
                            paramValues.getBuckets().forEach(bk->paramValuesMap.put(bk.getKey().toString(),bk.getDocCount()));
                            paramsMap.put("paramValues",paramValuesMap);

                        }

                    });

                    httpEntityVo.putData("paramsCount",paramsMap);

                };

            }

            httpEntityVo.setMessage("查询商品索引成功!");

        } catch (Exception e) {
            e.printStackTrace();
            httpEntityVo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpEntityVo.setMessage("查询商品索引失败!");
            httpEntityVo.setErrorCode(e.getMessage());
        }
        return httpEntityVo;
    }



    @RequestMapping(value = {"/add2/{goodsId}"})
    @ResponseBody
    public String addIndex2(@PathVariable Long goodsId) {

        ESSpuIndexEntity entity = new ESSpuIndexEntity();
        entity.setGoodsId(goodsId);
        entity.setGoodsName("firstOne");
        entity.setGoodsInfoName("红色的好看");
        entity.setGoodsAddedTime(new Date());

        List<EsThirdCateEntity> cates = Lists.newArrayList();
        EsThirdCateEntity cate = new EsThirdCateEntity();
        cate.setCatId(155L);
        cate.setCatName("长裤");
        cates.add(cate);
        entity.setThirdCateList(cates);

//        spuIndexRepository.save(entity);

        return "add";
    }




}
