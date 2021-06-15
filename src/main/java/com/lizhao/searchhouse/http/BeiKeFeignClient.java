package com.lizhao.searchhouse.http;

import com.lizhao.searchhouse.entity.beike.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "beike", url = "https://nj.fang.ke.com")
public interface BeiKeFeignClient {

    @GetMapping("/loupan/nht1pg{page}/?_t=1")
    BaseResponse houseWithPage(@PathVariable("page") int page);
}
