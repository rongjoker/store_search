package com.cyk.es.mapper;/**
 * Created by zhangshipeng on 2017/9/22.
 */

import com.cyk.es.entity.ESSpuIndexEntity;
import com.cyk.es.entity.EsGoodsCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * spu-mapper
 *
 * @author zhangshipeng
 * @ClassName: SpuIndexMapper
 * @date 2017-09-22 14:49
 **/
@Mapper
public interface SpuIndexMapper {


    Integer selectGoodsSpuListCount();

    List<ESSpuIndexEntity> selectEsGoodsListByPage(Map<String, Object> paramMap);

    ESSpuIndexEntity findGoodsById(Long goodsId);

    List<ESSpuIndexEntity> findGoodsByIds(List<String> ids);

    EsGoodsCategoryEntity selectGoodsCateById(Long catId);

    List<EsGoodsCategoryEntity> selectAllGoodsCateList();



}
