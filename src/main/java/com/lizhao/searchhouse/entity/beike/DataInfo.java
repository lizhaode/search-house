package com.lizhao.searchhouse.entity.beike;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataInfo {

    @JsonProperty("no_more_data")
    private int noMoreData;

    private String total;

    private List<HouseInfo> list;
}
