package com.zz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zz.mapper.ProductInfoMapper;
import com.zz.pojo.ProductInfo;
import com.zz.pojo.ProductInfoExample;
import com.zz.pojo.vo.ProductInfoVo;
import com.zz.service.ProductInfoService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    //切记业务逻辑中一定有数据库访问层对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample (new ProductInfoExample ( ));
    }

    @Override
    public PageInfo splitPage(int PageNum, int pageSize) {
        //分页插件使用pageHelper工具类完成分页设置
        PageHelper.startPage (PageNum, pageSize);

        //进行pageInfo的数据封装
        //进行有条件的查询操作，必须要创建ProductInfoExample
        ProductInfoExample example = new ProductInfoExample ( );
        example.setOrderByClause ("p_id desc");

        //设置玩排序后去集合，切记切记：一定要在去集合之前，设置pageHelper
        List<ProductInfo> list = productInfoMapper.selectByExample (example);
        //将查到的集合封装到PageInfo对象中
        PageInfo<ProductInfo> info = new PageInfo<> (list);
        return info;
    }

    @Override
    public int save(ProductInfo info) {

        return productInfoMapper.insert (info);
    }

    @Override
    public ProductInfo getById(int pid) {

        return productInfoMapper.selectByPrimaryKey (pid);
    }

    @Override
    public int update(ProductInfo info) {

        return productInfoMapper.updateByPrimaryKey (info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey (pid);
    }

    @Override
    public int deleteBatch(String[] ids) {

        return productInfoMapper.deleteBatch (ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {

        return productInfoMapper.selectCondition (vo);
    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //取出集合之前，先要配置pageHelper,starPage()属性
        PageHelper.startPage (vo.getPage ( ), pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition (vo);
        return new PageInfo<> (list);
    }
}
