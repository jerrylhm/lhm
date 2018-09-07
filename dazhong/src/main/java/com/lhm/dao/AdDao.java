package com.lhm.dao;

import com.lhm.entity.Ad;

public interface AdDao {
    
    Ad findById(Integer id);
    
    int insert(Ad ad);
}