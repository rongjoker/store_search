package com.cyk.es.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分类信息
 *
 * @author zhangshipeng
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsGoodsCategoryEntity implements Serializable {
    /**
     * 分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long catId;
    /**
     * 分类名称
     */
//    @Field(index= FieldIndex.not_analyzed,store=true,type= FieldType.String)
    @Field(index= true,store=true,type= FieldType.keyword)
    private String catName;
    /**
     * 父分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long catParentId;
    /**
     * 类型ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long typeId;
    /**
     * 排序
     */
    @Field(index= false,store=true,type= FieldType.Integer)
    private Integer catSort;
    /**
     * 层级
     */
    @Field(index= false,store=true,type= FieldType.Integer)
    private Integer catGrade;
    /**
     * SEO title
     */
    @Field(index= true,store=true,type= FieldType.text)
    private String catSeoTitle;
    /**
     * SEO keyword
     */
    @Field(index= true,store=true,type= FieldType.text)
    private String catSeoKeyword;
    /**
     * SEO desc
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catSeoDesc;
    /**
     * 是否显示
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catIsShow;
    /**
     * 是否删除
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catDelflag;
    /**
     * 创建
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catCreateName;
    /**
     * 创建时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    private Date catCreateTime;
    /**
     * 修改
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catModifiedName;
    /**
     * 修改时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date catModifiedTime;
    /**
     * 删除
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catDelName;
    /**
     * 删除时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date catDelTime;
    /**
     * 扣率
     */
//    @Field(index= FieldIndex.no,store=true,type= FieldType.Object)
    @Field(index= false,store=true,type= FieldType.keyword)
    private BigDecimal catRate;
    /**
     * MODEL
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String catModel;

}