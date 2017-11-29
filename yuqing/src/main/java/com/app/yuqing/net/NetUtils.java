package com.app.yuqing.net;

import com.app.yuqing.net.httprunner.CreateGroupHttpRunner;
import com.app.yuqing.net.httprunner.DismissGroupHttpRunner;
import com.app.yuqing.net.httprunner.GetTokenHttpRunner;
import com.app.yuqing.net.httprunner.JoinGroupHttpRunner;
import com.app.yuqing.net.httprunner.LoginHttpRunner;
import com.app.yuqing.net.httprunner.MenuListHttpRunner;
import com.app.yuqing.net.httprunner.QueryBacklogHttpRunner;
import com.app.yuqing.net.httprunner.QueryGroupHttpRunner;
import com.app.yuqing.net.httprunner.QueryGroupUserHttpRunner;
import com.app.yuqing.net.httprunner.QueryLastVersionHttpRunner;
import com.app.yuqing.net.httprunner.QueryUserByIdHttpRunner;
import com.app.yuqing.net.httprunner.QueryUserByOfficeIdHttpRunner;
import com.app.yuqing.net.httprunner.QuitGroupHttpRunner;
import com.app.yuqing.net.httprunner.SyncHttpRunner;
import com.app.yuqing.net.httprunner.TreeDataHttpRunner;
import com.app.yuqing.net.httprunner.UpdateClientIdHttpRunner;
import com.app.yuqing.net.httprunner.UpdateHeadHttpRunner;
import com.app.yuqing.net.httprunner.UpdateMobileHttpRunner;
import com.app.yuqing.net.httprunner.UpdatePasswordHttpRunner;

public class NetUtils {
	
	public static void init() {
		final AndroidEventManager eventManager = AndroidEventManager.getInstance();
		eventManager.registerEventRunner(EventCode.HTTP_LOGIN, new LoginHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYLASTVERSION, new QueryLastVersionHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_TREEDATA, new TreeDataHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_GETTOKEN, new GetTokenHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYUSERBYOFFICEID, new QueryUserByOfficeIdHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_MENULIST, new MenuListHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_UPDATEHEAD, new UpdateHeadHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_UPDATEPASSWORD, new UpdatePasswordHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_UPDATEMOBILE, new UpdateMobileHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYBACKLOG, new QueryBacklogHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYUSERBYID, new QueryUserByIdHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_CREATEGROUP, new CreateGroupHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYGROUP, new QueryGroupHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUERYGROUPUSER, new QueryGroupUserHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_JOINGROUP, new JoinGroupHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_QUITGROUP, new QuitGroupHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_DISMISSGROUP, new DismissGroupHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_UPDATECLIENTID, new UpdateClientIdHttpRunner());
		eventManager.registerEventRunner(EventCode.HTTP_SYNC, new SyncHttpRunner());
	}
}


