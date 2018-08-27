package com.creator.db.user;

/**
 * 获取全部用户数据时的中间数据传输封装类
 * @author ilongli
 *
 */
public class GetUserDto {
	private Integer page;
	private String query;
	private String date;
	private String role;
	private Integer status;
	private Integer ug_id;
	private String orderBy;
	private Boolean isDESC;
		
	{
		query = "";
		date = "";
		role = "0";
		status = -1;
		ug_id = -1;
		orderBy = "";
		isDESC = false;
	}
	
	public GetUserDto() {}
	
	public GetUserDto(Integer page, String query, String date, String role, Integer status, Integer ug_id,
			String orderBy, Boolean isDESC) {
		super();
		this.page = page;
		this.query = query;
		this.date = date;
		this.role = role;
		this.status = status;
		this.ug_id = ug_id;
		this.orderBy = orderBy;
		this.isDESC = isDESC;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUg_id() {
		return ug_id;
	}

	public void setUg_id(Integer ug_id) {
		this.ug_id = ug_id;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getIsDESC() {
		return isDESC;
	}

	public void setIsDESC(Boolean isDESC) {
		this.isDESC = isDESC;
	}

	@Override
	public String toString() {
		return "GetUserDto [page=" + page + ", query=" + query + ", date=" + date + ", role=" + role + ", status="
				+ status + ", ug_id=" + ug_id + ", orderBy=" + orderBy + ", isDESC=" + isDESC + "]";
	}
}
