package com.creator.db.backgroundmenumanamgment;

public class BackgroundMenuManagementDto extends BackgroundMenuManagment{

	private String node_name;

	public BackgroundMenuManagementDto(int bck_mm_id, String bck_mm_name,
			short bck_mm_treeid, short bck_mm_del_flag, short bck_mm_state,
			String node_name) {
		super(bck_mm_id, bck_mm_name, bck_mm_treeid, bck_mm_del_flag,
				bck_mm_state);
		this.node_name = node_name;
	}

	public BackgroundMenuManagementDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BackgroundMenuManagementDto(int bck_mm_id, String bck_mm_name,
			short bck_mm_treeid, short bck_mm_del_flag, short bck_mm_state) {
		super(bck_mm_id, bck_mm_name, bck_mm_treeid, bck_mm_del_flag, bck_mm_state);
		// TODO Auto-generated constructor stub
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	
	
}
