package com.cm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cm.entity.SensorReport;
import com.cm.entity.vo.SensorReportVo;


public interface CalculateReportDao {
	
	public List<SensorReportVo> getallbyday(@Param("tablename")String tablename);
	
	public void addReportbyday(@Param("list") List<SensorReport> list);
	
	public List<SensorReportVo> getbyclass(@Param("tablename")String tablename,@Param("tablename2")String tablename2,@Param("starttime")String starttime,@Param("endtime")String endtime);
	
	public int isexisting(SensorReport report);
	

}
