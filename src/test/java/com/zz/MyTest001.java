package com.zz;

import com.zz.mapper.ProductInfoMapper;
import com.zz.pojo.ProductInfo;
import com.zz.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-dao.xml","classpath:applicationContext-service.xml"})
public class MyTest001 {
    @Autowired
    ProductInfoMapper productInfoMapper;
    @Test
    public void SelectCondition(){
        ProductInfoVo vo=new ProductInfoVo ();
        vo.setPname ("4");
        List<ProductInfo>list=productInfoMapper.selectCondition (vo);
        list.forEach (ProductInfo-> System.out.println (ProductInfo ));

    }
}
