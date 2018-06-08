package com.app.yuqing.net;

public class URLUtils {

	private static final String SERVER = "http://new.sevencai.com:8000/";
	
//	private static final String SERVER = "http://121.41.77.80:8080/";

//	private static final String SERVER = "http://120.24.241.113:8080/";
	
	public static final String PIC_SERVER = "http://sevencai.cn";
	
	public static final String LOGIN = SERVER + "ucenter/app/login";

	public static final String PERSONALINFO = SERVER + "ucenter/app/query/personalInfo";

	public static final String QUERYUSER = SERVER + "ucenter/app/query/user";

	public static final String QUERYUPGRAGE = SERVER + "ucenter/app/query/upgrage";
		
	public static final String QUERYLASTVERSION = SERVER + "api/appm/appManager/queryLastVersion";
		
	public static final String TREEDATA = SERVER + "api/treeDataMobile";
	
	public static final String GETTOKEN = SERVER + "rong/user/getToken";
		
	public static final String QUERYUSERBYOFFICEID = SERVER + "api/appm/queryUserByOfficeId";
		
	public static final String MENULIST = SERVER + "api/menuList";
	
	public static final String UPDATEHEAD = SERVER + "api/updateHead";
	
	public static final String UPDATEPASSWORD = SERVER + "api/updatePassword";
	
	public static final String UPDATEMOBILE = SERVER + "api/updateMobile";
		
	public static final String QUERYBACKLOG = SERVER + "api/appm/queryBacklog";
		
	public static final String QUERYUSERBYID = SERVER + "api/appm/queryUserById";
	
	public static final String CREATEGROUP = SERVER + "rong/group/create";
	
	public static final String QUERYGROUP = SERVER + "rong/user/queryGroup";
	
	public static final String QUERYGROUPUSER = SERVER + "rong/group/user/query";
	
	public static final String JOINGROUP = SERVER + "rong/group/join";
	
	public static final String QUITGROUP = SERVER + "rong/group/quit";
	
	public static final String DISMISSGROUP = SERVER + "rong/group/dismiss";
	
	public static final String UPDATECLIENTID = SERVER + "api/push/updateClientId";
	
	public static final String SYNC = SERVER + "rong/group/sync";

	public static final String GETUITEST = SERVER + "api/pilotsend/getuiTest";
}

