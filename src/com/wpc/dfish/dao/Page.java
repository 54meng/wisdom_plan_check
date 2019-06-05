package com.wpc.dfish.dao;

public class Page {
	  //当前页数
	  private int currentPage  = 0;

	  //每页大小
	  private int pageSize = 0;

	  //总行数
	  private int rowCount = 0;

	  //总页数
	  private int pageCount = 0;
	  
	  private Object list;

	  public Page() {
	  }
	  /**
	   * 设置当前页号
	   * @param currentPage
	   */
	  public void setCurrentPage(int currentPage) {
	    this.currentPage = currentPage;
	  }

	  /**
	   * 获取当前页号
	   * @return
	   */
	  public int getCurrentPage() {
	    return currentPage;
	  }

	  /**
	   * 设置页大小
	   * @param PageSize
	   */
	  public void setPageSize(int pageSize) {
	    this.pageSize = pageSize;
	  }

	  /**
	   * 获取页大小
	   * @return
	   */
	  public int getPageSize() {
	    return pageSize;
	  }

	  /**
	   * 设置总行数
	   * @param rowCount
	   */
	  public void setRowCount(int rowCount) {
	    this.rowCount = rowCount;
	  }

	  /**
	   * 获取总行数
	   * @return
	   */
	  public int getRowCount() {
	    return rowCount;
	  }

	  /**
	   * 通过计算算出总页数
	   * @return
	   */
	  public int getPageCount() {
	    if (pageCount != 0) {
	      return pageCount;
	    }
	    else {
	      if ( (rowCount != 0) && (pageSize != 0)) {
	        pageCount = ( (rowCount + pageSize) - 1) / pageSize;
	      }

	      return pageCount;
	    }
	  }
	  public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}
	  
	public Object getList() {
		return list;
	}
	public void setList(Object list) {
		this.list = list;
	}
	

}
