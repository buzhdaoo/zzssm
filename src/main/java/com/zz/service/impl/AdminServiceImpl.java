package com.zz.service.impl;

import com.zz.mapper.AdminMapper;
import com.zz.pojo.Admin;
import com.zz.pojo.AdminExample;
import com.zz.service.AdminService;
import com.zz.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    //在业务层逻辑中，一定会有数据访问层的对象
    @Autowired
    AdminMapper adminMapper;

    @Override
    public Admin login(String name, String pwd) {
        //根据传入的用户或到DB中查询相应的对象
        //如果查询到用户对象，在进行密码的对比
        AdminExample example =new AdminExample ();

        example.createCriteria ().andANameEqualTo (name);

        List<Admin> list=adminMapper.selectByExample (example);
        System.out.println (name );
        if (list.size()>0){
            Admin admin=list.get (0);

            String miPwd= MD5Util.getMD5 (pwd);
            if (miPwd.equals (admin.getaPass ())){
                return admin;
            }
        }

        return null;
    }
}
