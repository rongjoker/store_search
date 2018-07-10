package com.cyk.es.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;


/**
 * 品牌信息
 *
 * @author zhangshipeng
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsGoodsBrandEntity implements Serializable {
    /**
     * 品牌ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long brandId;
    /**
     * 品牌名称
     */
//    @Field(index= FieldIndex.not_analyzed,store=true,type= FieldType.String)
    @Field(index= true,store=true,type= FieldType.keyword)
    private String brandNickname;
    /**
     * 品牌名称
     */
    //    @Field(index= FieldIndex.not_analyzed,store=true,type= FieldType.String)
    @Field(index= true,store=true,type= FieldType.keyword)
    private String brandName;
    /**
     * 
     * 品牌URl
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandUrl;
    /**
     * LOGO
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandLogo;
    /**
     * INDEX
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandPromIndex;
    /**
     * 排序
     */
    @Field(index= false,store=true,type= FieldType.Integer)
    private Integer brandSort;
    /**
     * SEOtitle
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandSeoTitle;
    /**
     * SEOKeyWord
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandSeoKeyword;
    /**
     * SEO DESC
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandSeoDesc;
    /**
     * 是否删除
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandDelflag;
    /**
     * 创建
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandCreateName;
    /**
     * 创建时间
     */
    @Field(index= true,store=true,type= FieldType.Date)
    private Date brandCreateTime;
    /**
     * 修改时间
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandModifiedName;
    /**
     * 修改时间
     */
    private Date brandModifiedTime;
    /**
     * 删除时间
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandDelName;
    /**
     * 删除时间
     */
    @Field(index= true,store=true,type= FieldType.Date)
    private Date brandDelTime;
    /**
     * 品牌详细
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String brandDesc;

}