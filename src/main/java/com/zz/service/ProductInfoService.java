package com.zz.service;

import com.github.pagehelper.PageInfo;
import com.zz.pojo.ProductInfo;
import com.zz.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {
    //显示全部商品(不分页)
    List<ProductInfo> getAll();

    //分页功能实现
    PageInfo splitPage(int PageNum, int pageSize);

    //新增商品
    int save(ProductInfo info);

    //按主键ID查询商品
    ProductInfo getById(int id);

    //更新商品
    int update(ProductInfo info);

    //单个商品删除
    int delete(int pid);

    //批量删除商品
    int deleteBatch(String[] ids);

    //多条件查询商品
    List<ProductInfo> selectCondition(ProductInfoVo vo);

    //多条件查询分页
    PageInfo splitPageVo(ProductInfoVo vo, int pageSize);
}
