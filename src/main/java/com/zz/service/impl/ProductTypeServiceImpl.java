package com.zz.service.impl;

import com.zz.mapper.ProductTypeMapper;
import com.zz.pojo.ProductType;
import com.zz.pojo.ProductTypeExample;
import com.zz.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //在业务逻辑层一定会有数据访问层
    @Autowired
    ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample (new ProductTypeExample ());
    }
}
