package com.lizhao.searchhouse.subscribe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhao.searchhouse.entity.beike.HouseInfo;
import com.lizhao.searchhouse.entity.dao.DBHouseInfo;
import com.lizhao.searchhouse.entity.dao.HouseInfoHistory;
import com.lizhao.searchhouse.repository.HouseInfoHistoryRepository;
import com.lizhao.searchhouse.repository.HouseInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BeikeSub implements MessageListener {

    private ObjectMapper om = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    private final HouseInfoRepository repository;

    private final HouseInfoHistoryRepository infoRepository;

    public BeikeSub(HouseInfoRepository repository, HouseInfoHistoryRepository infoRepository) {
        this.repository = repository;
        this.infoRepository = infoRepository;
    }

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        try {
            HouseInfo info = om.readValue(message.getBody(), HouseInfo.class);
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
            DBHouseInfo dataInDB = this.repository.findByHouseName(info.getTitle());
            if (dataInDB == null) {
                this.repository.save(db);
            } else {
                db.setId(dataInDB.getId());
                this.repository.save(db);
                this.infoRepository.save(this.checkDifferent(dataInDB, db));
            }
        } catch (IOException e) {
            log.error("parse house info error", e);
        }
    }

    private HouseInfoHistory checkDifferent(DBHouseInfo oldInfo, DBHouseInfo newInfo) throws JsonProcessingException {
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

        HouseInfoHistory history = new HouseInfoHistory();
        history.setHouseName(oldInfo.getHouseName());
        history.setChangeString(om.writeValueAsString(result));
        return history;
    }

    private String returnDifferent(String oldInfo, String newInfo) {
        if (oldInfo.equals(newInfo)) {
            return null;
        } else {
            return newInfo;
        }
    }
}
