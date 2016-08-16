package com.example.ruijie.myplay.uitls;

/**
 * Created by yjx on 15/5/9.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.ruijie.myplay.global.Constants;

/**
 * User: YJX Date: 2015-05-09 Time: 20:57 SP
 */
public final class SPUtils {

	private final static String TAG = SPUtils.class.getSimpleName();

	public static boolean clear(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.edit().clear().commit();
	}

	/** 是否第一次打开 */
	public static boolean isFirstOpen(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("isFirstOpen", true);
	}

	public static void setFirstOpen(Context context, boolean isFirstOpen) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean("isFirstOpen", isFirstOpen).commit();
	}

	/**
	 * 获取电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getTelPhone(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getString("telPhone", "");
	}

	/**
	 * 保存电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static void setTelPhone(Context context, String telPhone) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("telPhone", telPhone).commit();
	}
	
	
	
	
	/**
	 * 获取terminalCode
	 * 
	 * @param context
	 * @return
	 */
	public static String getTerminalCode(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getString("terminalCode", "");
	}

	/**
	 * 保存电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static void setTerminalCode(Context context, String terminalCode) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("terminalCode", terminalCode).commit();
	}
	
	
	
	
	
	/**
	 * 获取token
	 * 
	 * @param context
	 * @return
	 */
	public static String getToken(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getString("token", "");
	}

	/**
	 * 保存token
	 * 
	 * @param context
	 * @return
	 */
	public static void setToken(Context context, String token) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("token", token).commit();
	}
/**
	 * 获取pwd
	 * 
	 * @param context
	 * @return*/

	public static String getPassword(Context context) {
		String pwd = "";
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
			String pwdBase = sharedPreferences.getString("password", "");
			if(!TextUtils.isEmpty(pwdBase)){
				pwd = AESEncryptor.decrypt(getTelPhone(context), pwdBase);
			}
		} catch (Exception e) {
			Log.e(TAG, "AESEncryptor 解密失败");
			e.printStackTrace();
		}

		return pwd;
	}

/**
	 * 保存password
	 * 
	 * @param context
	 * @return*/

	public static void setPassword(Context context, String password) {
		try {
			String enpwd = AESEncryptor.encrypt(getTelPhone(context), password);
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
			sharedPreferences.edit().putString("password", enpwd).commit();
		} catch (Exception e) {
			Log.e(TAG, "AESEncryptor 加密失败");
			e.printStackTrace();
		}
	}

	/**
	 * 是否已经登录
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isRecorded(Context context) {
		String telPhone = getTelPhone(context);
		String passwrod = getPassword(context);
		return (TextUtils.isEmpty(telPhone) || TextUtils.isEmpty(passwrod)) ? false : true;
	}

	/**
	 * 得到2g/3g下是否可以下载图片
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShowImg(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("isShowImg", true);
	}

	/**
	 * 设置2g/3g下是否可以下载图片
	 * 
	 * @param context
	 * @return
	 */
	public static void setShowImg(Context context, boolean is) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean("isShowImg", is).commit();
	}

	/**
	 * 得到wifi下是否可以自动缓存
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiDown(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("isWifiDown", true);
	}

	/**
	 * 设置wifi下是否可以自动缓存
	 * 
	 * @param context
	 * @return
	 */
	public static void setWifiDown(Context context, boolean is) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOGTAG, Context.MODE_PRIVATE);
		sharedPreferences.edit().putBoolean("isWifiDown", is).commit();
	}
}
