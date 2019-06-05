package com.wpc.webapp.controller.vo;

import java.util.ArrayList;
import java.util.List;

import com.rongji.dfish.engines.util.Utils;
import com.wpc.utils.thread.ThreadLocalFactory;

/**
 * 表格数据对象
 * 
 * @author imlzw
 * 
 */
public class TableData {
	private List<Object[]> data;//表数据
	private int draw = 0;//绘制序号
	private long recordsTotal = 0;
	private long recordsFiltered = 0;

	/**
	 * 默认构造函数
	 */
	public TableData() {
		data = new ArrayList<Object[]>();
		String draw = ThreadLocalFactory.getThreadLocalRequest().getParameter("draw");
		if (Utils.notEmpty(draw)) {
			try {
				this.draw = Integer.parseInt(draw);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public List<Object[]> getData() {
		return data;
	}

	public void setData(List<Object[]> data) {
		this.data = data;
	}
	public void addDataItem(Object... item){
		if(Utils.notEmpty(item)){
			this.data.add(item);	
		}
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	/**
	 * 清除数据
	 */
	public void clear(){
		this.data.clear();
		this.draw=0;
		this.recordsFiltered=0;
		this.recordsTotal=0;
	}
	
}
