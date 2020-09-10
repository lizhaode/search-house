package com.lizhao.searchhouse.entity.beike;

import lombok.Data;

@Data
public class BaseResponse {

    private int errno;

    private String error;

    private DataInfo data;

}
