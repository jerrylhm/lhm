package com.lhm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhm.entity.Ad;
import com.lhm.entity.Page;

public interface AdDao {
    
    Ad findById(@Param("id") Integer id);
    
    List<Ad> query(Page page);
    
    List<Ad> queryByLike(@Param("title") String like, Page page);
    
    int insert(Ad ad);
}