package com.cyk.es.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;


/**
 * 属性参数
 * 
 * @author ggn
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsExpandparamEntity implements Serializable{

    /**
     * 主键ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long releExpandparamId;
    /**
     * 商品ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long goodsId;
    /**
     * 属性ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long expandparamId;
    /**
     * 属性名称
     */
    @Field(index= true,store=true,type= FieldType.keyword)
    private String expandparamName;
    /**
     * 属性值ID
     */
    @Field(index= true,store=true,type= FieldType.text)
    private String expandparamValueId;
    /**
     * 属性值Name
     */
    @Field(index= true,store=true,type= FieldType.keyword)
    private String expandparamValueName;


}