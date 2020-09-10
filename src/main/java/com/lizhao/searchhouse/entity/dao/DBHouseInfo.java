package com.lizhao.searchhouse.entity.dao;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "house_info")
@DynamicUpdate
public class DBHouseInfo {

    @Id
    private int id;

    // 小区名字
    @Column(name = "house_name")
    private String houseName;

    // 均价
    private String price;

    // 起始总价
    @Column(name = "total_price")
    private String totalPrice;

    // 小区户型最大面积
    @Column(name = "max_area")
    private String maxArea;

    // 小区户型最小面积
    @Column(name = "min_area")
    private String minArea;

    // 居室数量
    @Column(name = "room_scope")
    private String roomScope;

    // 比较详细的地址
    private String address;

    // 行政街道
    private String street;

    // 行政区
    private String district;

    // 开发商, 可能会存在多个, 逗号区隔
    @Column(name = "dev_company")
    private String devCompany;

    // 装修类型
    private String decoration;

    // 销售状态
    @Column(name = "sale_status")
    private String saleStatus;

    // 楼盘链接
    private String url;

}
