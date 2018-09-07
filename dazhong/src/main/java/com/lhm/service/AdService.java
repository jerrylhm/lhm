package com.lhm.service;

import java.util.List;

import com.lhm.dto.AdDto;

public interface AdService {
    /**
     * 新增广告
     * @param adDto
     * @return 是否新增成功：true-成功;fale-失败
     */
    boolean add(AdDto adDto);
}
