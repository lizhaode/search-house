package com.lizhao.searchhouse.repository;

import com.lizhao.searchhouse.entity.dao.DBHouseInfo;
import org.springframework.data.repository.CrudRepository;

public interface HouseInfoRepository extends CrudRepository<DBHouseInfo, Integer> {

    DBHouseInfo findByHouseName(String houseName);
}
