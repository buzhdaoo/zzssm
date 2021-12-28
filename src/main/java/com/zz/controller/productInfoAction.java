package com.zz.controller;

import com.github.pagehelper.PageInfo;
import com.zz.pojo.ProductInfo;
import com.zz.pojo.vo.ProductInfoVo;
import com.zz.service.ProductInfoService;
import com.zz.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class productInfoAction {
    //设置每页显示的记录数
    public static final int PAGE_SIZE = 5;
    private String saveFileName = "";
    //切记在界面中，一定会有业务逻辑的对象
    @Autowired
    ProductInfoService productInfoService;

    @RequestMapping("/getAll")
    public String getALL(HttpServletRequest request) {

        List<ProductInfo> list = productInfoService.getAll ( );
        request.setAttribute ("list", list);

        return "product";
    }

    //显示第一页的5条信息
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {

        PageInfo info = null;

        Object objectVo = request.getSession ( ).getAttribute ("prodVo");

        if (objectVo != null) {
            productInfoService.splitPageVo ((ProductInfoVo) objectVo, PAGE_SIZE);
            request.getSession ().removeAttribute ("prodVo");
        } else {
            //得到第一页的数据
            info = productInfoService.splitPage (1, PAGE_SIZE);
        }

        request.setAttribute ("info", info);

        return "product";
    }

    //ajax分页翻页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        //取得当前page参数的页面数据
        PageInfo info = productInfoService.splitPageVo (vo, PAGE_SIZE);

        session.setAttribute ("info", info);

    }

    @ResponseBody
    @RequestMapping("/condition")
    public void Condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition (vo);
        session.setAttribute ("list", list);

    }

    //异步ajax文件上传
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(HttpServletRequest request, MultipartFile pimage) {
        // 提取生成文件名(UUID+文件名后缀)
        saveFileName = FileNameUtil.getUUIDFileName ( ) + FileNameUtil.getFileType (pimage.getOriginalFilename ( ));
        // 得到项目中图片存储的路径
        String path = request.getServletContext ( ).getRealPath ("/image_big");
        System.out.println ("path:" + path);
        // 转存
        try {
            pimage.transferTo (new File (path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace ( );
        }

        // 返回给客户端JSON对象，封装图片的路径，为了在页面实现立即回显
        JSONObject object = new JSONObject ( );
        object.put ("imgurl", saveFileName);

        return object.toString ( );
    }

    //商品增加
    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage (saveFileName);
        info.setpDate (new Date ( ));
        //info对象中的表单提交上来的5个数据，有异步ajax上来的图片名称，有上架的的数据
        int num = -1;
        try {
            num = productInfoService.save (info);
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        if (num > 0) {
            request.setAttribute ("msg", "增加成功!!");
        } else {
            request.setAttribute ("msg", "增加失败!!");
        }

        //清空saveFileName变量中的内容，为了下次增加或修改的异步ajax的上传处理
        saveFileName = "";

        //增加成功后应该重新访问数据库，所以跳转到显示的action上
        return "forward:/prod/split.action";//请求转发
    }

    @RequestMapping("/one")
    public String One(int pid, ProductInfoVo vo, Model model, HttpSession session) {

        ProductInfo info = productInfoService.getById (pid);

        model.addAttribute ("prod", info);
        //将多条件及页码放在session中，更新处理结束后分页是读取条件和页码进行处理
        session.setAttribute ("prodVo", vo);

        return "update";
    }

    //商品更新
    @RequestMapping("/update")
    public String update(ProductInfo info, HttpServletRequest request) {
        //因为ajax的异步图片上传，如果有过上传，则saveFileName里上传上来的图片的名称，
        // 如果没有使用异步ajax上传，则saveFileName="",
        // 实体类info使用隐藏表单域提供上来的pImage原始图片的名称
        if (!saveFileName.equals ("")) {
            info.setpImage (saveFileName);
        }
        //完成更新操作
        int num = -1;
        try {
            num = productInfoService.update (info);
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        if (num > 0) {
            //更新成功
            request.setAttribute ("msg", "更新成功!!");
        } else {
            //更新失败
            request.setAttribute ("msg", "更新失败!!");
        }
        //处理完更新后，saveFileName里有可能有数据
        //而下一次更新时要使用这个变量作为判断的依据，就会出错，所以必须清空saveFileName
        saveFileName = "";
        //重定向
        return "forward:/prod/split.action";//redirect重定向
    }

    //单个删除商品
    @RequestMapping("/delete")
    public String delete(int pid, HttpServletRequest request,ProductInfoVo vo) {
        int num = -1;
        try {
            num = productInfoService.delete (pid);
        } catch (Exception e) {
            e.printStackTrace ( );
        }
        if (num > 0) {
            request.setAttribute ("msg", "删除成功!!!");
            request.getSession ().setAttribute ("deleteProdVo",vo);
        } else {
            request.setAttribute ("msg", "删除失败!!!");
        }
        //删除成功后跳到分页
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request) {
        PageInfo info=null;
        Object vo=request.getSession ().getAttribute ("deleteProdVo");
        if (vo !=null){
            info=productInfoService.splitPageVo ((ProductInfoVo)vo,PAGE_SIZE);
        }else {
         info = productInfoService.splitPage (1, PAGE_SIZE);
        }
        request.getSession ( ).setAttribute ("info", info);
        return request.getAttribute ("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpServletRequest request) {
        //将上传上来的字符串截开，形成商品的id的字符数组
        String[] ps = pids.split (",");
        try {
            int num = productInfoService.deleteBatch (ps);
            System.out.println (num + "----------------------");

            if (num > 0) {
                request.setAttribute ("msg", "批量删除成功!!!");
            } else {
                request.setAttribute ("msg", "批量删除失败!!!");
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        } finally {
            request.setAttribute ("msg", "商品不可删除!!!");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

}
