package com.lizhao.searchhouse.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhao.searchhouse.entity.beike.BaseResponse;
import com.lizhao.searchhouse.entity.beike.HouseInfo;
import com.lizhao.searchhouse.entity.dao.DBHouseInfo;
import com.lizhao.searchhouse.entity.dao.HouseInfoHistory;
import com.lizhao.searchhouse.http.BeiKeFeignClient;
import com.lizhao.searchhouse.repository.HouseInfoHistoryRepository;
import com.lizhao.searchhouse.repository.HouseInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeikeHouse {

    private final BeiKeFeignClient client;

    private final HouseInfoRepository infoRepository;

    private final HouseInfoHistoryRepository historyRepository;

    private ObjectMapper om = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public BeikeHouse(BeiKeFeignClient client, HouseInfoRepository infoRepository, HouseInfoHistoryRepository historyRepository) {
        this.client = client;
        this.infoRepository = infoRepository;
        this.historyRepository = historyRepository;
    }


    @Scheduled(fixedDelay = 86400000L)
    public void start() {
        int stop;
        int startPage = 1;
        BaseResponse response;
        log.info("start");
        do {
            response = this.client.houseWithPage(startPage);
            startPage += 1;
            stop = response.getData().getNoMoreData();
            for (HouseInfo info : response.getData().getList()) {
                this.handleResponse(info);
            }
        } while (stop != 1);
        log.info("end");
    }

    private void handleResponse(HouseInfo info) {
        // 转换到数据库字段
        DBHouseInfo db = new DBHouseInfo();
        db.setAddress(info.getAddress());
        db.setDecoration(info.getDecoration());
        StringBuilder companyName = new StringBuilder();
        for (String company : info.getDeveloperCompany()) {
            companyName.append(company).append(",");
        }
        db.setDevCompany(companyName.substring(0, companyName.length() - 1));
        db.setDistrict(info.getDistrictName());
        db.setHouseName(info.getTitle());
        db.setMaxArea(info.getMaxFrameArea());
        db.setMinArea(info.getMinFrameArea());
        db.setPrice(info.getAveragePrice());
        db.setRoomScope(info.getFrameRoomsDesc());
        db.setSaleStatus(info.getSaleStatus());
        db.setStreet(info.getBizCircleName());
        db.setTotalPrice(info.getTotalPriceStart());
        db.setUrl("https://tj.fang.ke.com" + info.getUrl());

        // 如果数据库里边有数据，就更新，并且记录 history 表
        DBHouseInfo dataInDB = this.infoRepository.findByHouseName(info.getTitle());
        if (dataInDB == null) {
            this.infoRepository.save(db);
        } else {
            db.setId(dataInDB.getId());
            HouseInfoHistory history = this.checkDifferent(dataInDB, db);
            if (history != null) {
                this.historyRepository.save(history);
                this.infoRepository.save(db);
            }
        }
    }

    private HouseInfoHistory checkDifferent(DBHouseInfo oldInfo, DBHouseInfo newInfo) {
        DBHouseInfo result = new DBHouseInfo();
        result.setUrl(this.returnDifferent(oldInfo.getUrl(), newInfo.getUrl()));
        result.setTotalPrice(this.returnDifferent(oldInfo.getTotalPrice(), newInfo.getTotalPrice()));
        result.setStreet(this.returnDifferent(oldInfo.getStreet(), newInfo.getStreet()));
        result.setSaleStatus(this.returnDifferent(oldInfo.getSaleStatus(), newInfo.getSaleStatus()));
        result.setRoomScope(this.returnDifferent(oldInfo.getRoomScope(), newInfo.getRoomScope()));
        result.setPrice(this.returnDifferent(oldInfo.getPrice(), newInfo.getPrice()));
        result.setMinArea(this.returnDifferent(oldInfo.getMinArea(), newInfo.getMinArea()));
        result.setMaxArea(this.returnDifferent(oldInfo.getMaxArea(), newInfo.getMaxArea()));
        result.setDistrict(this.returnDifferent(oldInfo.getDistrict(), newInfo.getDistrict()));
        result.setDevCompany(this.returnDifferent(oldInfo.getDevCompany(), newInfo.getDevCompany()));
        result.setDecoration(this.returnDifferent(oldInfo.getDecoration(), newInfo.getDecoration()));
        result.setAddress(this.returnDifferent(oldInfo.getAddress(), newInfo.getAddress()));

        try {
            String resultString = om.writeValueAsString(result);
            if (!resultString.equals("{\"id\":0}")) {
                HouseInfoHistory history = new HouseInfoHistory();
                history.setHouseName(oldInfo.getHouseName());
                history.setChangeString(resultString);
                return history;
            }
        } catch (JsonProcessingException e) {
            log.error("parse json error", e);
        }
        return null;
    }

    private String returnDifferent(String oldInfo, String newInfo) {
        if (oldInfo.equals(newInfo)) {
            return null;
        } else {
            return oldInfo;
        }
    }

}
