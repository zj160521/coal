package com.cm.controller;

import com.alibaba.fastjson.JSONObject;
import com.cm.entity.CurveColor;
import com.cm.security.LoginManage;
import com.cm.service.CurvecolorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope
@RequestMapping("/curve")
public class CurvecolorController {

    @Autowired
    private LoginManage loginManage;

    @Autowired
    private ResultObj result;

    @Autowired
    private CurvecolorService service;

    @RequestMapping(value = "/addup", method = RequestMethod.POST)
    @ResponseBody
    public Object addup(@RequestBody CurveColor color, HttpServletRequest request){
        if(!loginManage.isUserLogin(request)){
            return result.setStatus(-1,"no login");
        }
        try{
            if(color.getId()>0){
                service.update(color);
                String remark = JSONObject.toJSONString(color);
                String operation2 = "修改曲线颜色";
                loginManage.addLog(request, remark, operation2, 168);
            } else {
                service.add(color);
                String remark = JSONObject.toJSONString(color);
                String operation2 = "增加曲线颜色";
                loginManage.addLog(request, remark, operation2, 168);
            }
        } catch (Exception e){
            e.printStackTrace();
            return result.setStatus(-4,"exception");
        }
        return result.setStatus(0,"ok");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Object getAll(HttpServletRequest request){
        if(!loginManage.isUserLogin(request)){
            return result.setStatus(-1,"no login");
        }
        try{
            CurveColor color = service.getAll();
            if (null == color) {
                color = new CurveColor();
                color.setAvgvalue("#ACBED0");
                color.setRealvalue("#367B21");
                color.setMaxvalues("#EE4B46");
                color.setMinvalue("#83E492");
                color.setCbvalue("#CFB3B3");
                color.setFeedvalue("#088AE7");
                color.setLevel1("#F40303");
                color.setLevel2("#EF8D04");
                color.setLevel3("#F4C65A");
                color.setLevel4("#259DE7");
                color.setSupplyvalue("#E0C6C6");
                color.setInitialColor("#E08F8F");
                color.setCalibratevalue("#CFB3B3");
                color.setUnusualvalue("#D6B0B0");
                color.setChanging2value("#E0B1B1");
                color.setChanging3value("#DAA6A6");
                service.add(color);
            }
            result.put("data",color);
        } catch (Exception e){
            e.printStackTrace();
            return result.setStatus(-4,"exception");
        }
        return result.setStatus(0,"ok");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Object delete(@PathVariable int id,HttpServletRequest request){
        if(!loginManage.isUserLogin(request)){
            return result.setStatus(-1,"no login");
        }
        try{
            service.delete(id);
        } catch (Exception e){
            e.printStackTrace();
            return result.setStatus(-4,"exception");
        }
        return result.setStatus(0,"ok");
    }
}
