package com.yigou.service.goods;

import com.yigou.entity.PageResult;
import com.yigou.pojo.goods.Sku;

import java.util.*;

/**
 * sku业务逻辑层
 */
public interface SkuService {

    public List<Sku> findAll();

    public PageResult<Sku> findPage(int page, int size);

    public List<Sku> findList(Map<String, Object> searchMap);

    public PageResult<Sku> findPage(Map<String, Object> searchMap, int page, int size);

    public Sku findById(String id);

    public void add(Sku sku);

    public void update(Sku sku);

    public void delete(String id);

}
