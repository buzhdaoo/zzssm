package com.zz.controller;

import com.zz.pojo.Admin;
import com.zz.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
    //创建业务逻辑层的对象
    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, String name, String pwd, Model model)
    {
        Admin admin = (Admin) adminService.login(name, pwd);
        if (admin != null)
        {
            request.setAttribute("admin", admin);
            model.addAttribute("name", admin.getaName());
            return "main";
        }
        else
        {
            model.addAttribute("errmsg", "用户名或密码不正确！");
            return "login";
        }
    }

}
