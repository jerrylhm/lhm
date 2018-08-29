package com.creator.db.userhandle;

import java.util.List;

public interface UserHandleDao {

	public Integer insert(UserHandle uh);
	public Integer update(UserHandle uh);
	public Integer delete(int id);
	public List<UserHandle> query();
	public UserHandle findByNodeId(int nodeId);
}
