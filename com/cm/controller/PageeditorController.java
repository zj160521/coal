package com.cm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cm.entity.Pageeditor;
import com.cm.security.LoginManage;
import com.cm.service.PageeditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Scope
@Controller
@RequestMapping("/editor")
public class PageeditorController {

    @Autowired
    private LoginManage loginManage;

    @Autowired
    private ResultObj result;

    @Autowired
    private PageeditorService pageeditorService;

    @RequestMapping(value = "/addup", method = RequestMethod.POST)
    @ResponseBody
    public Object addup(@RequestBody Pageeditor editor, HttpServletRequest request){
        if(!loginManage.isUserLogin(request)){
            return result.setStatus(-1,"no login");
        }
        try{
            if (editor.getId()>0){
                List<Object> list = editor.getList();
                String jsonString = JSON.toJSONString(list);
                editor.setStr(jsonString);
                pageeditorService.update(editor);
                String remark = JSONObject.toJSONString(editor);
                String operation2 = "修改列表显示";
                loginManage.addLog(request, remark, operation2,167);
            }else {
                List<Object> list = editor.getList();
                String jsonString = JSON.toJSONString(list);
                editor.setStr(jsonString);
                pageeditorService.add(editor);
                String remark = JSONObject.toJSONString(editor);
                String operation2 = "增加列表显示";
                loginManage.addLog(request, remark, operation2,167);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.clean();
            return result.setStatus(-4,"exception");
        }
        result.clean();
        return result.setStatus(0,"ok");
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Object getById(HttpServletRequest request){
        /*if(!loginManage.isUserLogin(request)){
            return result.setStatus(-1,"no login");
        }*/
        try{
            List<Pageeditor> all = pageeditorService.getAll();
            if (null == all || all.isEmpty()) {
                Pageeditor edit = new Pageeditor();
                edit.setStr("[{\"title\":\"地点/名称/类型\",\"key\":\"position\"},{\"title\":\"报警门限\",\"key\":\"limit_alarm\",\"width\":85,\"sortable\":true},{\"title\":\"断电门限\",\"key\":\"limit_power\",\"width\":85,\"sortable\":true},{\"title\":\"复电门限\",\"key\":\"limit_repower\",\"width\":85,\"sortable\":true},{\"title\":\"实时值\",\"key\":\"now_value\",\"width\":85,\"value\":100,\"sortable\":true},{\"title\":\"状态\",\"key\":\"statusText\",\"now\":true},{\"title\":\"最大值\",\"key\":\"maxvalue\",\"width\":85,\"sortable\":true},{\"title\":\"最小值\",\"key\":\"minvalue\",\"width\":85,\"sortable\":true},{\"title\":\"平均值\",\"key\":\"avgvalue\",\"width\":85,\"sortable\":true},{\"title\":\"最后一次-报警/解除及时刻\",\"key\":\"alarmStarttime\"},{\"title\":\"最后一次-断电/复电及时刻\",\"key\":\"powerStarttime\"}][{\"title\":\"地点/名称/类型\",\"key\":\"position\"},{\"title\":\"报警门限\",\"key\":\"limit_alarm\",\"width\":85,\"sortable\":true},{\"title\":\"断电门限\",\"key\":\"limit_power\",\"width\":85,\"sortable\":true},{\"title\":\"复电门限\",\"key\":\"limit_repower\",\"width\":85,\"sortable\":true},{\"title\":\"实时值\",\"key\":\"now_value\",\"width\":85,\"value\":100,\"sortable\":true},{\"title\":\"状态\",\"key\":\"statusText\",\"now\":true},{\"title\":\"最大值\",\"key\":\"maxvalue\",\"width\":85,\"sortable\":true},{\"title\":\"最小值\",\"key\":\"minvalue\",\"width\":85,\"sortable\":true},{\"title\":\"平均值\",\"key\":\"avgvalue\",\"width\":85,\"sortable\":true},{\"title\":\"最后一次-报警/解除及时刻\",\"key\":\"alarmStarttime\"},{\"title\":\"最后一次-断电/复电及时刻\",\"key\":\"powerStarttime\"}]");
                edit.setType("sensorCall");
                all.add(edit);
                pageeditorService.add(edit);
                Pageeditor editor = new Pageeditor();
                editor.setStr("[{\"title\":\"地点/名称/类型\",\"key\":\"position\"},{\"title\":\"报警/断电状态\",\"key\":\"alarmstatus\"},{\"title\":\"当前状态\",\"key\":\"now_value\",\"value\":25},{\"title\":\"最近一次状态变动及时刻\",\"key\":\"statusChange\"},{\"title\":\"最后一次-报警/解除及时刻\",\"key\":\"alarmStarttime\"},{\"title\":\"最后一次-断电/复电及时刻\",\"key\":\"feedtime\"},{\"title\":\"断电区域-馈电状态及时刻\",\"key\":\"feedstastus\"}]");
                editor.setType("switchCall");
                all.add(editor);
                pageeditorService.add(editor);
                Pageeditor pageeditor = new Pageeditor();
                pageeditor.setStr("[{\"width\":105,\"sortable\":1,\"title\":\"设备编号\",\"key\":\"alais\"},{\"title\":\"设备信息\",\"key\":\"type\"},{\"title\":\"状态\",\"key\":\"statusText\"},{\"sortable\":1,\"title\":\"值\",\"key\":\"now_value\"}]");
                pageeditor.setType("nowtime");
                all.add(pageeditor);
                pageeditorService.add(pageeditor);
            }
            for (Pageeditor pageeditor : all) {
                String str = pageeditor.getStr();
                List<Object> list = JSONObject.parseArray(str, Object.class);
                pageeditor.setList(list);
            }
            result.clean();
            result.put("data",all);
        } catch (Exception e){
            e.printStackTrace();
            result.clean();
            return result.setStatus(-4,"exception");
        }
        return result.setStatus(0,"ok");
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public Object getByTypeAndName(@RequestBody Pageeditor pageeditor,HttpServletRequest request){
        /*if(!loginManage.isUserLogin(request)){
            result.clean();
            return result.setStatus(-1,"no login");
        }*/
        try{
            Pageeditor editor = pageeditorService.getByTypeAndName(pageeditor.getType());
            if(null == editor){
                result.clean();
                return result.setStatus(0,"no data");
            }
            String str = editor.getStr();
            List<Object> list = JSONObject.parseArray(str, Object.class);
            editor.setList(list);
            result.clean();
            result.put("data",editor);
        } catch (Exception e){
            e.printStackTrace();
            result.clean();
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
            pageeditorService.delete(id);
        } catch (Exception e){
            e.printStackTrace();
            return result.setStatus(-4,"exception");
        }
        return result.setStatus(0, "ok");
    }
}
