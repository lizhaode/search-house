package com.lizhao.searchhouse.entity.beike;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HouseInfo {

    // 地址
    private String address;

    // 平均房价
    @JsonProperty("average_price")
    private String averagePrice;

    // 行政街道
    @JsonProperty("bizcircle_name")
    private String bizCircleName;

    // 装修类型
    private String decoration;

    // 开发商
    @JsonProperty("developer_company")
    private List<String> developerCompany;

    // 区
    @JsonProperty("district_name")
    private String districtName;

    // 居室数量
    @JsonProperty("frame_rooms_desc")
    private String frameRoomsDesc;

    // 最大面积
    @JsonProperty("max_frame_area")
    private String maxFrameArea;

    // 最小面积
    @JsonProperty("min_frame_area")
    private String minFrameArea;

    // 销售状态
    @JsonProperty("sale_status")
    private String saleStatus;

    // 房子名字
    private String title;

    // 起始总价
    @JsonProperty("total_price_start")
    private String totalPriceStart;

    private String url;
}
