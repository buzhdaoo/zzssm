package com.zz.Listener;
import com.zz.pojo.ProductType;
import com.zz.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

// 监听器的注解
@WebListener
public class ProductTypeListener implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        // 手工从Spring容器中取出ProductTypeServiceImpl对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-*.xml");
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList = productTypeService.getAll();
        // 嵌入全局作用域中
        servletContextEvent.getServletContext().setAttribute("typeList", typeList);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {

    }
}