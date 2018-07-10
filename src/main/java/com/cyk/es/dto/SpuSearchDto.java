package com.cyk.es.dto;/**
 * Created by zhangshipeng on 2017/9/25.
 */

import lombok.Data;

import java.io.Serializable;

/**
 * spu查询条件
 *
 * @author Administrator
 * @ClassName: SpuSearchDto
 * @date 2017-09-25 18:00
 **/
@Data
public class SpuSearchDto implements Serializable {

    private Long goodsId;

    private String goodsInfoName;

    private String goodsInfoAdded;

    private String goodsDelflag;

    private Long thirdId;

    private String thirdName;

    private Long typeId;

    private Long brandId;

    private String brandName;

    private Integer page;

    private Integer size;

//    private String sortFieldName;
//    private String sortOrder = "DESC";

    private String searchType;//test,goods,store

    private Boolean aggregation=false;

    private Boolean hl=false;//高亮

    private String priceMin;

    private String priceMax;

    private Long catId;

    private String[] params;

    private String sort;

}
