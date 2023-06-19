package cn.com.codes.userManager.service;

import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.framework.app.services.BaseService;
import cn.com.codes.object.User;
import cn.com.codes.userManager.dto.UserManagerDto;

public interface UserManagerService extends BaseService {

	public User login(User loginUser);

	public DrawHtmlListDateService getDrawHtmlListDateService();

	public void deleteUserFromGroup(String GroupId, String userIds);

	public User userUpdateInit(String userId);

	public String userMaintence(UserManagerDto dto);

	public void groupMaintence(UserManagerDto dto);

	public void updateUserSuperUserFlg(String userId, Integer superUserFlg);

	public void ldeleteUser(String userId, int delCount);

	public void importUsers(UserManagerDto dto);

	public void updateUserStatus(String userId, String status);

	public void reSetPwd(String userId);
}
