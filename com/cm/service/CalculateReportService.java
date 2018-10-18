package com.cm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cm.dao.CalculateReportDao;
import com.cm.dao.EnvclassesDao;
import com.cm.entity.AnalogStatement;
import com.cm.entity.EnvClasses;
import com.cm.entity.SensorReport;
import com.cm.entity.vo.NameTime;
import com.cm.entity.vo.SensorReportVo;

@Service("calculateReportService")
public class CalculateReportService {
	
	@Autowired
	private CalculateReportDao dao;
	
	@Autowired
	private AnalogStatementService statementService;
	
	@Autowired
	private EnvclassesDao envclassesDao;
	
	public List<SensorReport> getSensorReportbyDay() throws ParseException{
		String tablename = "t_analog_statistics_"+getDay();
		List<SensorReportVo> vos = dao.getallbyday(tablename);
		List<SensorReport> list = new ArrayList<SensorReport>();
		HashMap<String, SensorReport> map = new HashMap<String, SensorReport>();
		NameTime time = new NameTime();
		for(SensorReportVo s:vos){
			SensorReport report = map.get(s.getIp()+s.getDevid()+s.getType());
			if(report==null){
				report = new SensorReport();
				report.setSensor_id(s.getDev_id());
				report.setIp(s.getIp());
				report.setSensorId(s.getDevid());;
				report.setType(s.getType());
				report.setMaxvalues(s.getMaxvalues());;
				report.setMinvalue(s.getMinvalue());
				report.setAvgvalue(s.getAvgvalue());
				report.setMaxtime(s.getMaxtime());
				report.setFilltime(s.getFilltime());
				report.setRemark(0);
				time.setId(report.getSensor_id());
				time.setStarttime(report.getFilltime());
				List<AnalogStatement> list2 = statementService.getfeeds(time);
				if(list2.size()>0){
					report.setFeeabnums(list2.get(0).getAlerts());
					report.setFeedtime(list2.get(0).getAlertTimes());
				}
				map.put(s.getIp()+s.getDevid()+s.getType(), report);
				list.add(report);
			}
			if(s.getStatus()==2){
				report.setAlerts(s.getCnt());
				time.setId(s.getDev_id());
				time.setAtype(s.getStatus());
				time.setStarttime(s.getFilltime());
				report.setAlerttime(statementService.getTimecount(time));
			}else if(s.getStatus()==3){
				report.setPowerfres(s.getCnt());
				time.setId(s.getDev_id());
				time.setAtype(s.getStatus());
				time.setStarttime(s.getFilltime());
				report.setPowertime(statementService.getTimecount(time));
			}else if(s.getStatus()==5){
				report.setFaults(s.getCnt());
				time.setId(s.getDev_id());
				time.setAtype(s.getStatus());
				time.setStarttime(s.getFilltime());
				report.setFaulttime(statementService.getTimecount(time));
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<EnvClasses> classes = envclassesDao.getAll();
		if(classes!=null&&classes.size()>0){
			for(EnvClasses c:classes){
				String tablename2 = "t_analog_statistics_"+getDay2();
				long startlong = format.parse(getTime()+" "+c.getStart()+":00").getTime();
				long endlong = format.parse(getTime()+" "+c.getEnd()+":00").getTime();
				if(startlong<endlong){
					String starttime = getTime()+" "+c.getStart()+":00";
					String endtime = getTime()+" "+c.getEnd()+":00";
					List<SensorReportVo> list2 = dao.getbyclass(tablename, tablename2, starttime, endtime);
					Map<String, SensorReport> map2 = new HashMap<String, SensorReport>();
					for(SensorReportVo s:list2){
						SensorReport report = map2.get(s.getIp()+s.getDevid()+s.getType());
						if(report==null){
							report = new SensorReport();
							report.setSensor_id(s.getDev_id());
							report.setIp(s.getIp());
							report.setSensorId(s.getDevid());;
							report.setType(s.getType());
							report.setMaxvalues(s.getMaxvalues());;
							report.setMinvalue(s.getMinvalue());
							report.setAvgvalue(s.getAvgvalue());
							report.setMaxtime(s.getMaxtime());
							report.setFilltime(s.getFilltime());
							report.setRemark(c.getId());
							report.setClassname(c.getName());
							report.setClassstart(c.getStart()+":00");
							report.setClassend(c.getEnd()+":00");
							time.setId(report.getSensor_id());
							time.setStarttime(report.getFilltime());
							List<AnalogStatement> list3 = statementService.getfeeds(time);
							if(list3.size()>0){
								report.setFeeabnums(list3.get(0).getAlerts());
								report.setFeedtime(list3.get(0).getAlertTimes());
							}
							map2.put(s.getIp()+s.getDevid()+s.getType(), report);
							list.add(report);
						}
						if(s.getStatus()==2){
							report.setAlerts(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setAlerttime(statementService.getTimecount(time));
						}else if(s.getStatus()==3){
							report.setPowerfres(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setPowertime(statementService.getTimecount(time));
						}else if(s.getStatus()==5){
							report.setFaults(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setFaulttime(statementService.getTimecount(time));
						}
					}
				}
				if(startlong>endlong){
					String starttime = getTime2()+" "+c.getStart()+":00";
					String endtime = getTime()+" "+c.getEnd()+":00";
					List<SensorReportVo> list2 = dao.getbyclass(tablename, tablename2, starttime, endtime);
					Map<String, SensorReport> map2 = new HashMap<String, SensorReport>();
					for(SensorReportVo s:list2){
						SensorReport report = map2.get(s.getIp()+s.getDevid()+s.getType());
						if(report==null){
							report = new SensorReport();
							report.setSensor_id(s.getDev_id());
							report.setIp(s.getIp());
							report.setSensorId(s.getDevid());;
							report.setType(s.getType());
							report.setMaxvalues(s.getMaxvalues());;
							report.setMinvalue(s.getMinvalue());
							report.setAvgvalue(s.getAvgvalue());
							report.setMaxtime(s.getMaxtime());
							report.setFilltime(s.getFilltime());
							report.setRemark(c.getId());
							report.setClassname(c.getName());
							report.setClassstart(c.getStart()+":00");
							report.setClassend(c.getEnd()+":00");
							time.setId(report.getSensor_id());
							time.setStarttime(report.getFilltime());
							List<AnalogStatement> list3 = statementService.getfeeds(time);
							if(list3.size()>0){
								report.setFeeabnums(list3.get(0).getAlerts());
								report.setFeedtime(list3.get(0).getAlertTimes());
							}
							map2.put(s.getIp()+s.getDevid()+s.getType(), report);
							list.add(report);
						}
						if(s.getStatus()==2){
							report.setAlerts(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setAlerttime(statementService.getTimecount(time));
						}else if(s.getStatus()==3){
							report.setPowerfres(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setPowertime(statementService.getTimecount(time));
						}else if(s.getStatus()==5){
							report.setFaults(s.getCnt());
							time.setId(s.getDev_id());
							time.setAtype(s.getStatus());
							time.setStarttime(s.getFilltime());
							report.setFaulttime(statementService.getTimecount(time));
						}
					}
				}
			}
		}
		return list;
	}
	
	public int isexisting(SensorReport report){
		return dao.isexisting(report);
	}
	
	private String getDay() throws ParseException{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd");
		Date day = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		day = calendar.getTime();
		return format1.format(day);
		
	}
	private String getDay2() throws ParseException{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM_dd");
		Date day = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		day = calendar.getTime();
		return format1.format(day);
		
	}
	private String getTime() throws ParseException{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date day = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		day = calendar.getTime();
		return format1.format(day);
		
	}
	private String getTime2() throws ParseException{
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date day = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		day = calendar.getTime();
		return format1.format(day);
		
	}
}
