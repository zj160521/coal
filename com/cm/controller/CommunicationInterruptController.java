package com.cm.controller;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cm.entity.Analoginfo;
import com.cm.entity.vo.CommunicationVo;
import com.cm.entity.vo.NameTime;
import com.cm.security.LoginManage;
import com.cm.service.CommunicationInterruptService;

/**
 * 通讯中断记录
 * @author hetaiyun
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/communication")
public class CommunicationInterruptController {
	
	@Autowired
	private ResultObj result;
	
	@Autowired
	private LoginManage loginManage;
	
	@Autowired
	private CommunicationInterruptService service;
	
	
	@RequestMapping(value="/getall",method=RequestMethod.POST)
	@ResponseBody
	public Object getallAnaloginfo(@RequestBody NameTime nameTime,HttpServletRequest request){
			if(!loginManage.isUserLogin(request)){
				return result.setStatus(-1, "no login");
			}
			try {
				boolean start = valiDateTimeWithLongFormat(nameTime.getStarttime());
				boolean end = valiDateTimeWithLongFormat(nameTime.getEndtime());
				if(start==true&&end==true){
					nameTime.setAtype(nameTime.getAtype()+4);
					if(nameTime.getCur_page()<=0){
						List<Analoginfo> list2 = service.getbySensors(nameTime);
						result.put("alldata", list2);
					}else{
						nameTime.setStart_row((nameTime.getCur_page()-1)*nameTime.getPage_rows());
						List<CommunicationVo> list = service.getbySensor(nameTime);
						nameTime.setTotal_rows(service.getcount(nameTime));
						result.put("namTime", nameTime);
						result.put("data",list);
					}
				}else{
					return result.setStatus(-2, "时间输入不正确，请重新输入");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return result.setStatus(-4, "exception");
			}
			return result.setStatus(0, "ok");
		}
	public static boolean valiDateTimeWithLongFormat(String timeStr) {
			String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
					+ "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
			Pattern pattern = Pattern.compile(format);
			Matcher matcher = pattern.matcher(timeStr);
			if (matcher.matches()) {
				pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
				matcher = pattern.matcher(timeStr);
				if (matcher.matches()) {
					int y = Integer.valueOf(matcher.group(1));
					int m = Integer.valueOf(matcher.group(2));
					int d = Integer.valueOf(matcher.group(3));
					if (d > 28) {
						Calendar c = Calendar.getInstance();
						c.set(y, m-1, 1);
						int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
						return (lastDay >= d);
					}
				}
				return true;
			}
			return false;
	}
}
