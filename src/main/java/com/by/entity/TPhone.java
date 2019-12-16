package com.by.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * (TPhone)实体类
 *
 * @author makejava
 * @since 2019-11-22 15:30:24
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TPhone implements Serializable {
    private static final long serialVersionUID = -21544079488914290L;
    @Field("id")
    private Integer phoneId;
    @Field("phone_name")
    private String phoneName;
    @Field("phone_inf")
    private String phoneInf;
    @Field("phone_kc")
    private Integer phoneKc;
    @Field("phone_cd")
    private String phoneCd;


}