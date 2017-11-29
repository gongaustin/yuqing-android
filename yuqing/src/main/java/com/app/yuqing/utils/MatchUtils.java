package com.app.yuqing.utils;

public class MatchUtils {
	
//	private static final String PATTERN_PHONE = "1[3|4|5|6|7|8|9][0-9]{9}";
	
	private static final String PATTERN_PHONE = "[0-9]{7,20}";
	
	private static final String PATTERN_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	
	private static final String PATTERN_PWD = "^[\\w[~!@#%&*()_+-=:'<>,.?]]{6,20}$";
	
//	private static final String PATTERN_PAYPWD = "^[0-9]{6,10}$";
	
	private static final String PATTERN_NICKNAME = "[\\w[\u4E00-\u9FA5]-]{2,16}";

	private static final String PATTERN_HANZI = "[\u4E00-\u9FA5]";
	
	private static final String PATTERN_SFZ = "([0-9]{17}([0-9]|X|x))";
	
	public static boolean matchSFZ(String sfz) {
		return sfz.matches(PATTERN_SFZ);
	}

	public static boolean matchPhone(String phone) {
	 	return phone.matches(PATTERN_PHONE);
	}
	
	public static boolean matchEmail(String email) {
		return email.matches(PATTERN_EMAIL);
	}
	
	public static boolean matchPwd(String pwd) {
		return pwd.matches(PATTERN_PWD);
	}
	
//	public static boolean matchPayPwd(String pwd) {
//		return pwd.matches(PATTERN_PAYPWD);
//	}	
	
	public static boolean matchNickName(String nickName) {
		return nickName.matches(PATTERN_NICKNAME);
	}

	public static boolean matchHanZi(String str) {
		char[] arry = str.toCharArray();
		for (int i = 0;i<arry.length;i++) {
			String newStr = String.valueOf(arry[i]);
			if (!newStr.matches(PATTERN_HANZI)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取字符字节长度
	 * @param s
	 * @return
	 */
	public static int getWordCount(String s) {
		int length = 0;
		for(int i = 0; i < s.length(); i++)
		{
			int ascii = Character.codePointAt(s, i);
			if(ascii >= 0 && ascii <=255)
				length++;
			else
				length += 2;

		}
		return length;
	}
}
