package com.cm.entity.vo;

public class TimeInAreaVo {

	private int rfcard_id;
	private String name;
	private int area_id;
	private String areaname;
	private String starttime;
	private String endtime;
	private String wellduration;
	private long longDuration;
	private String departname;
	private String worktypename;
	
	
	public int getRfcard_id() {
		return rfcard_id;
	}
	public void setRfcard_id(int rfcard_id) {
		this.rfcard_id = rfcard_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getDepartname() {
		return departname;
	}
	public void setDepartname(String departname) {
		this.departname = departname;
	}
	public String getWorktypename() {
		return worktypename;
	}
	public void setWorktypename(String worktypename) {
		this.worktypename = worktypename;
	}
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getWellduration() {
		return wellduration;
	}
	public void setWellduration(String wellduration) {
		this.wellduration = wellduration;
	}
	public long getLongDuration() {
		return longDuration;
	}
	public void setLongDuration(long longDuration) {
		this.longDuration = longDuration;
	}
	
}
