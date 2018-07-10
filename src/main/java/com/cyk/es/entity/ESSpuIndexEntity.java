package com.cyk.es.entity;/**
 * Created by zhangshipeng on 2017/9/18.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * spu_index
 *
 * @author zhangshipeng
 * @ClassName: SpuIndexVo
 * @date 2017-09-18 15:04
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(indexName = "cyk-goods-index", type = "EsGoods")
public class ESSpuIndexEntity implements Serializable {


//    @Field(index= FieldIndex.no,store=true,type= FieldType.Long)
    @Id
    private Long goodsId;

    /**
     * 默认图片
     */
    @Field(index= false,store=true,type= FieldType.keyword)
    private String goodsImgId;


    /**
     * 商品名称
     */
//    @Field(index= true,store=true,type= FieldType.String)
//    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true, analyzer = "ik_smart")
    @Field(type = FieldType.text, index = true, store = true, analyzer = "ik_smart")
    private String goodsName;

    /**
     * 商品名称
     */
//    @Field(index= FieldIndex.analyzed,store=true,type= FieldType.String, analyzer = "ik_smart")
    @Field(type = FieldType.text, index = true, store = true, analyzer = "ik_smart")
    private String goodsInfoName;

    /**
     * 商品副标题
     */
//    @Field(index= FieldIndex.analyzed,store=true,type= FieldType.String, analyzer = "ik_smart")
    @Field(type = FieldType.text, index = true, store = true, analyzer = "ik_smart")
    private String goodsSubtitle;

    /**
     * 分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long catId;


    /**
     * 商品价格
     */
    @Field(index= true,store=true,type= FieldType.Double)
    private BigDecimal goodsInfoPreferPrice;

    /**
     * 是否上架
     */
    @Field(index= true,store=true,type= FieldType.keyword)
    private String goodsInfoAdded;

    /**
     * 修改时间
     */
//    @Field(format= DateFormat.date_time,index= FieldIndex.no,store=true,type= FieldType.Object)
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Auto)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date goodsAddedTime;

    /**
     * 是否删除
     */
    @Field(index= true,store=true,type= FieldType.keyword)
    private String goodsDelflag;


    @Field(index= true,store=true,type= FieldType.Long)
    private Long thirdId;

    /**
     * 第三商家名称
     */
    @Field(index= true,store=true,type= FieldType.text,  analyzer = "ik_smart")
    private String thirdName;


    /**
     * 分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long thirdCateId;

    /**
     * 创建时间
     */
    @Field(index= true,store=true,type= FieldType.Date)
    private Date goodsCreateTime;

    /**
     * 创建时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date goodsInfoCreateTime;

    /**
     * 修改时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date goodsModifiedTime;

    /**
     * 删除时间
     */
//    @Field(format= DateFormat.date_time,store=true,type= FieldType.Object)
    @Field(index= true,store=true,type= FieldType.Date)
    private Date goodsDelTime;

    /**
     * 类型ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long typeId;
    /**
     * 分类
     */
    @Field(type=FieldType.Nested, store=true, index = true, includeInParent = true)
    private List<EsGoodsCategoryEntity> cateList;
    /**
     * 商家分类
     */
    @Field(type=FieldType.Nested, store=true, index = true, includeInParent = true)
    private List<EsThirdCateEntity> thirdCateList;

    /**
     * 品牌
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long brandId;
    /**
     * 品牌信息
     */
//    @Field(index= FieldIndex.no,store=true,type= FieldType.Nested)
//    @Field(type= FieldType.Nested)
    @Field(type=FieldType.Nested, store=true, index = true, includeInParent = true)
    private EsGoodsBrandEntity brand;
    /**
     * 扩展参数列表
     */
    @Field(type=FieldType.Nested, store=true, index = true, includeInParent = true)
    private List<EsExpandparamEntity> paramList;

    /**
     * 销量
     */
    @Field(index= true,store=true,type= FieldType.Integer)
    private Integer saleCount;

    /**
     * 收藏数量（人气）
     */
    @Field(index= true,store=true,type= FieldType.Integer)
    private Integer collectionCount;


    /**
     * 浏览量
     */
    @Field(index= true,store=true,type= FieldType.Integer)
    private Integer visitNum;

    @Field(index = true,store = false,type = FieldType.Float)
    private float docuScore;

}
