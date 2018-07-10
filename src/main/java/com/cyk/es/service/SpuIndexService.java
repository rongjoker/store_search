package com.cyk.es.service;

import com.cyk.es.dto.SpuSearchDto;
import com.cyk.es.entity.ESSpuIndexEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;

import java.util.List;
import java.util.Optional;

/**
 * Created by zhangshipeng on 2017/9/22.
 */
public interface SpuIndexService {

    Integer createSpuGoodsIndexToEs();

    ESSpuIndexEntity insertOneGoodsIndexToEs(Optional<Long> goodsId);

    Integer bulkInsertGoodsIndexToEs(String ids);


    AggregatedPage<ESSpuIndexEntity> querySpus(SpuSearchDto searchDto);
}
