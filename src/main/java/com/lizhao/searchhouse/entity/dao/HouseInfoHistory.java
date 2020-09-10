package com.lizhao.searchhouse.entity.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "house_info_history")
public class HouseInfoHistory {

    @Id
    private int id;

    @Column(name = "house_name")
    private String houseName;

    @Column(name = "change_string")
    private String changeString;
}
