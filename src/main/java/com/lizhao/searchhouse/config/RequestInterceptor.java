package com.lizhao.searchhouse.config;

import feign.RequestTemplate;

public class RequestInterceptor implements feign.RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36");
        template.header("Accept-Language", "zh-CN,zh;q=0.9");
    }
}
