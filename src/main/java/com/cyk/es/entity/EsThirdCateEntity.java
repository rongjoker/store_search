package com.cyk.es.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 第三方分类
 * @author zhangshipeng
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EsThirdCateEntity implements Serializable {

    /**
     * 分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long catId;

    /**
     * 父亲分类ID
     */
    @Field(index= true,store=true,type= FieldType.Long)
    private Long catParentId;

    /**
     * 分类名称
     */
    @Field(index= true,store=true,type= FieldType.keyword)
    private String catName;

}
