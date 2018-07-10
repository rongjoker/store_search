package com.cyk.es.service.impl;/**
 * Created by zhangshipeng on 2017/9/22.
 */

import com.cyk.es.dto.SpuSearchDto;
import com.cyk.es.entity.ESSpuIndexEntity;
import com.cyk.es.entity.EsGoodsCategoryEntity;
import com.cyk.es.mapper.SpuIndexMapper;
import com.cyk.es.service.SpuIndexService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.support.ValueType;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * SpuIndexService
 *
 * @author zhangshipeng
 * @ClassName: SpuIndexServiceImpl
 * @date 2017-09-22 14:57
 **/
@Service
public class SpuIndexServiceImpl implements SpuIndexService {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static int initSpuStatus = 0;

    @Autowired
    private SpuIndexMapper spuIndexMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

//    @Autowired
//    private EntityMapper entityMapper;

//    @Autowired
//    private SpuIndexRepository spuIndexRepository;


//    @Resource
//    private DefaultResultMapper resultsMapper;


    @Override
    public Integer createSpuGoodsIndexToEs() {


        if (initSpuStatus == 1)
            return 0;


        //        List<ESSource> sourceList = Lists.newArrayList();
        // 查询所有商品总数
        Integer listCount = spuIndexMapper.selectGoodsSpuListCount();
        LOGGER.info("elastic search 生成索引,查询SPU商品数据，总数为{}", listCount);

        int temp = 200;//默认为5，暂时修改
        int indexs = 0;

        try {

            initSpuStatus = 1;

            //刪除索引
            if (elasticsearchTemplate.indexExists(ESSpuIndexEntity.class)) {
                elasticsearchTemplate.deleteIndex(ESSpuIndexEntity.class);
                LOGGER.info("elastic search 删除{}索引数据", ESSpuIndexEntity.class);
            }

            //创建索引
            elasticsearchTemplate.createIndex(ESSpuIndexEntity.class);
            elasticsearchTemplate.putMapping(ESSpuIndexEntity.class);
            elasticsearchTemplate.refresh(ESSpuIndexEntity.class);

            LOGGER.info("elastic search 创建{}索引数据", ESSpuIndexEntity.class);

            //创建分类预处理数据
            List<EsGoodsCategoryEntity> cateList = this.spuIndexMapper.selectAllGoodsCateList();
            Map<Long, List<EsGoodsCategoryEntity>> cateIdListMap = Maps.newLinkedHashMap();
            cateList.forEach(cate -> cateIdListMap.put(cate.getCatId(), firstcateUtil(cate.getCatId())));

            for (int i = 0; i < listCount; i = i + temp) {
                int start = i;
                // 查询商品信息
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("start", start);
                paramMap.put("end", temp);
                List<ESSpuIndexEntity> spuGoodslist = spuIndexMapper.selectEsGoodsListByPage(paramMap);

                List<IndexQuery> queries = Lists.newLinkedList();

                spuGoodslist.forEach(spuGoodsEntity -> {

                            spuGoodsEntity.setCateList(cateIdListMap.get(spuGoodsEntity.getCatId()));
                            IndexQuery indexQuery = new IndexQueryBuilder().withId(spuGoodsEntity.getGoodsId().toString()).withObject(spuGoodsEntity).build();
                            queries.add(indexQuery);

                        }


                );

//            spuIndexRepository.save(spuGoodslist);

                this.elasticsearchTemplate.bulkIndex(queries);

                this.elasticsearchTemplate.refresh(ESSpuIndexEntity.class);

                indexs += spuGoodslist.size();

                LOGGER.info("elastic search 调用接口创建SPU商品索引..." + start + " - " + (start + spuGoodslist.size()));

            }

            LOGGER.info("elastic search 创建SPU商品索引成功...");


        }catch (Exception e){
            e.printStackTrace();
            throw new MapperParsingException("创建索引数据失败！message:"+e.getMessage());
        }finally {
            initSpuStatus = 0;

        }

        return indexs;
    }

    @Override
    public ESSpuIndexEntity insertOneGoodsIndexToEs(Optional<Long> goodsId) {

        ESSpuIndexEntity goods = spuIndexMapper.findGoodsById(goodsId.get());
        goods.setCateList(firstcateUtil(goods.getCatId()));
        LOGGER.info("elastic search 创建单条SPU索引文档,id:{}...", goodsId);
        IndexQuery indexQuery = new IndexQueryBuilder().withId(goods.getGoodsId().toString()).withObject(goods).build();
        this.elasticsearchTemplate.index(indexQuery);

        this.elasticsearchTemplate.refresh(ESSpuIndexEntity.class);

//        this.spuIndexRepository.save(goods);

        return goods;

    }

    @Override
    public Integer bulkInsertGoodsIndexToEs(String ids) {

        List<String> goodsIds = Splitter.on(",").splitToList(ids);
        List<ESSpuIndexEntity> list = spuIndexMapper.findGoodsByIds(goodsIds);

        List<IndexQuery> queries = Lists.newArrayList();

        list.forEach(entity -> {
            entity.setCateList(firstcateUtil(entity.getCatId()));
            queries.add(new IndexQueryBuilder().withId(entity.getGoodsId().toString()).withObject(entity).build());
        });

        this.elasticsearchTemplate.bulkIndex(queries);

        return list.size();
    }

    @Override
    public AggregatedPage<ESSpuIndexEntity> querySpus(SpuSearchDto searchDto) {

        String preTag = "<font color=‘#dd4b39‘>";//google的色值
        String postTag = "</font>";

//        Pageable pageable = new PageRequest(null == searchDto.getPage() ? 0 : searchDto.getPage(), null == searchDto.getSize() ? 10 : searchDto.getSize());
        Pageable pageable = PageRequest.of(null == searchDto.getPage() ? 0 : searchDto.getPage(), null == searchDto.getSize() ? 10 : searchDto.getSize());
        String sort = searchDto.getSort();

        List<FieldSortBuilder> fs = Lists.newArrayList();

        if(StringUtils.isNotBlank(sort)){
            String[] sortOrders = sort.split(",");
            for(String sortOrder:sortOrders){
                if(StringUtils.isNotBlank(sortOrder)){
                    String[] sortKV = sortOrder.split(" ");
                    if(sortKV.length==2){
                        fs.add(new FieldSortBuilder(sortKV[0]).order("ASC".equals(sortKV[1].toUpperCase()) ? SortOrder.ASC : SortOrder.DESC));
                    }
                }
            }
        }else{
//            fs.add(new FieldSortBuilder("_score").order(SortOrder.DESC));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        List<HighlightBuilder.Field> hlfs = Lists.newArrayList();

        if (StringUtils.isNotBlank( searchDto.getGoodsInfoName())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("goodsInfoName", searchDto.getGoodsInfoName()));
            HighlightBuilder.Field goodsInfoName_highlightBuilder = new HighlightBuilder.Field("goodsInfoName").preTags(preTag).postTags(postTag);
            hlfs.add(goodsInfoName_highlightBuilder);
        }

        if (StringUtils.isNotBlank( searchDto.getThirdName())) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("thirdName", searchDto.getThirdName()));
            HighlightBuilder.Field thirdName_highlightBuilder = new HighlightBuilder.Field("thirdName").preTags(preTag).postTags(postTag);
            hlfs.add(thirdName_highlightBuilder);
        }

        BoolQueryBuilder filter = QueryBuilders.boolQuery();
        if (null != searchDto.getThirdId()) {
            filter.filter(QueryBuilders.termQuery("thirdId", searchDto.getThirdId()));
        }

        if (null != searchDto.getBrandId()) {
            filter.filter(QueryBuilders.termQuery("brandId", searchDto.getBrandId()));
        }

        if (null != searchDto.getBrandName()) {
            filter.filter(QueryBuilders.termQuery("brand.brandName", searchDto.getBrandName()));
        }

        if(null!=searchDto.getGoodsInfoAdded()){
            filter.filter(QueryBuilders.termQuery("goodsInfoAdded", searchDto.getGoodsInfoAdded()));
        }

        if(null!=searchDto.getCatId()){
            filter.filter(QueryBuilders.termQuery("catId", searchDto.getCatId()));
        }

        if(null!=searchDto.getParams() && searchDto.getParams().length>0){
            for(String param :searchDto.getParams()){
                if(param.indexOf(":")<1)
                    continue;

                String[] paramNV=param.split(":");

                if(paramNV.length!=2)
                    continue;

                filter.filter(QueryBuilders.termQuery("paramList.expandparamName", paramNV[0]));
                filter.filter(QueryBuilders.termQuery("paramList.expandparamValueName", paramNV[1]));

            }

        }

        if(StringUtils.isNotBlank(searchDto.getPriceMax())  ||  StringUtils.isNotBlank(searchDto.getPriceMin())){
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("goodsInfoPreferPrice");
            if(StringUtils.isNotBlank(searchDto.getPriceMin())){
                rangeQueryBuilder.from(searchDto.getPriceMin());
            }
            if(StringUtils.isNotBlank(searchDto.getPriceMax())){
                rangeQueryBuilder.to(searchDto.getPriceMax());
            }

            filter.filter(rangeQueryBuilder);
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withFilter(filter)
                .withPageable(pageable)
//                .withHighlightFields(highlightBuilder)
//                .withSort(sortBuilder)
                ;


//        for(FieldSortBuilder f:fs){
//            nativeSearchQueryBuilder.withSort(f);
//        }


        fs.forEach(f ->nativeSearchQueryBuilder.withSort(f));


        if (searchDto.getHl() && !CollectionUtils.isEmpty(hlfs)) {
//            HighlightBuilder.Field goodsInfoName_highlightBuilder = new HighlightBuilder.Field("goodsInfoName").preTags(preTag).postTags(postTag);
//            HighlightBuilder.Field thirdName_highlightBuilder = new HighlightBuilder.Field("thirdName").preTags(preTag).postTags(postTag);
            nativeSearchQueryBuilder.withHighlightFields(hlfs.toArray(new HighlightBuilder.Field[hlfs.size()]));
        }


        if (searchDto.getAggregation()) {
//            AggregationBuilder aggregationParams = AggregationBuilders.terms("params").field("paramList.expandparamName").exclude("")
//                    .subAggregation(AggregationBuilders.terms("paramValues").field("paramList.expandparamValueName").exclude(""))
//                    .order(Terms.Order.count(false)).size(10);


            TermsAggregationBuilder aggregationParams = new TermsAggregationBuilder("params", ValueType.STRING);
            aggregationParams.size(10);
            aggregationParams.field("paramList.expandparamName");


//            AggregationBuilder aggregationParams = AggregationBuilders.terms("params").field("paramList.expandparamName")
////                    .exclude("")
//                    .subAggregation(AggregationBuilders.terms("paramValues").field("paramList.expandparamValueName")
////                            .exclude("")
//                            )
//                    .order(Terms.Order.count(false)).size(10);
//
//
//            AggregationBuilder aggregationBrand = AggregationBuilders.terms("brands").field("brand.brandName")
////                    .exclude("")
//                    .order(Terms.Order.count(false)).size(10);
//
//            AggregationBuilder aggregationcate = AggregationBuilders.terms("cates").field("cateList.catName")
////                    .exclude("")
//                    .order(Terms.Order.count(false)).size(10);


            nativeSearchQueryBuilder
                    .addAggregation(aggregationParams)
//                    .addAggregation(aggregationBrand)
//                    .addAggregation(aggregationcate)
 ;

        }

        SearchQuery searchQuery = nativeSearchQueryBuilder.build();



        AggregatedPage<ESSpuIndexEntity> aggregatedPage = this.elasticsearchTemplate.queryForPage(searchQuery, ESSpuIndexEntity.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> clazz, Pageable pageable) {

//                long totalHits = searchResponse.getHits().totalHits();
                long totalHits = searchResponse.getHits().totalHits;
//                List<T> results = new ArrayList();
                List<ESSpuIndexEntity> entities = Lists.newLinkedList();
                Iterator var7 = searchResponse.getHits().iterator();

                ObjectMapper mapper = new ObjectMapper();

                while (var7.hasNext()) {
                    SearchHit hit = (SearchHit) var7.next();
                    if (hit != null) {
//                        T result = null;

//                        if (StringUtils.isNotBlank(hit.sourceAsString())) {
                        if (StringUtils.isNotBlank(hit.getSourceAsString())) {
                            try {
                                ESSpuIndexEntity entity = new ESSpuIndexEntity();
                                entity = mapper.readValue(hit.getSourceAsString(), ESSpuIndexEntity.class);
                                entity.setDocuScore(hit.getScore());

                                if (searchDto.getHl()) {
                                    ESSpuIndexEntity finalEntity = entity;

//                                    Map<String, org.elasticsearch.search.highlight.HighlightField> maps = hit.getHighlightFields();
                                    Map<String, org.elasticsearch.search.fetch.subphase.highlight.HighlightField> maps = hit.getHighlightFields();



                                    if(null!=hit.getHighlightFields()){
                                        if(null!=hit.getHighlightFields().get("goodsInfoName")){
                                            finalEntity.setGoodsInfoName(hit.getHighlightFields().get("goodsInfoName").fragments()[0].toString());
                                        }
                                        if(null!=hit.getHighlightFields().get("thirdName")){
                                            finalEntity.setThirdName(hit.getHighlightFields().get("thirdName").fragments()[0].toString());
                                        }

                                    }


//                                    searchResponse.getHits().forEach(searchHit -> {
//
//                                        finalEntity.setGoodsInfoName(searchHit.getHighlightFields().get("goodsInfoName").fragments()[0].toString());
//
//                                    });

//                        this.setPersistentEntityId(result, hit.getId(), clazz);
//                        this.populateScriptFields(result, hit);
//                                results.add(result);
                                    entities.add(finalEntity);
                                } else {
                                    entities.add(entity);
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            result = mapEntity(hit.sourceAsString(), ESSpuIndexEntity.class);
                        } else {
//                            result = this.mapEntity(hit.getFields().values(), ESSpuIndexEntity.class);
                        }


                    }
                }

                return new AggregatedPageImpl(entities, pageable, totalHits, searchResponse.getAggregations());


            }
        });


        return aggregatedPage;


//        return this.elasticsearchTemplate.queryForPage(searchQuery,ESSpuIndexEntity.class);
    }

    /**
     * 分类格式
     *
     * @param catId
     * @return List
     */
    public List<EsGoodsCategoryEntity> cateUtil(Long catId) {
        if (catId != null) {
            List<EsGoodsCategoryEntity> list = new ArrayList<EsGoodsCategoryEntity>();
            // 查询当前分类ID 三级
            EsGoodsCategoryEntity es = spuIndexMapper.selectGoodsCateById(catId);

            if (es != null && es.getCatParentId() != null && es.getCatParentId() != 0) {
                // 查询上级分类ID 二级
                EsGoodsCategoryEntity es1 = spuIndexMapper.selectGoodsCateById(es.getCatParentId());
                if (es1 != null && es1.getCatParentId() != null && es1.getCatParentId() != 0) {
                    // 查询上级分类ID 一级
                    EsGoodsCategoryEntity es2 = spuIndexMapper.selectGoodsCateById(es1.getCatParentId());
                    if (es2 != null && es1.getCatParentId() != null) {
                        list.add(es2);
                    }
                }
                list.add(es1);
            }
            list.add(es);
            return list;
        } else {
            return new ArrayList<EsGoodsCategoryEntity>();
        }

    }

    public List<EsGoodsCategoryEntity> firstcateUtil(Long catId) {
        if (catId != null) {
            List<EsGoodsCategoryEntity> list = new ArrayList<EsGoodsCategoryEntity>();
            // 查询当前分类ID 三级
            EsGoodsCategoryEntity es = spuIndexMapper.selectGoodsCateById(catId);
            list.add(es);
            return list;
        } else {
            return new ArrayList<EsGoodsCategoryEntity>();
        }

    }


}
