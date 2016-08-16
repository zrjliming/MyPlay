package com.example.ruijie.myplay.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Java 异常的分类：
基类为：Throwable
Error 和 Exception 继承于Throwable
RuntimeException和IOException等继承Exception

其中，Error和RuntimeException及其子类属于unchecked exception, 
而其他异常为checked exception。

一个App上线或者投入到生产环境的时候崩溃了，还不知道是什么原因，
这肯定是开发者的痛…所以肯定要加入全局异常捕获，如果项目较大的话，
可以考虑加入第三方诸如友盟的崩溃统计插件，以达到异常捕获的效果！

Java的Thread中有一个UncaughtExceptionHandler接口，
该接口的作用主要是为了当Thread 因未捕获的异常而突然终止时，调用处理程序。
我们可以通过setDefaultUncaughtExceptionHandler方法，来改变异常默认处理程序。


public class App extends Application {
 
     public static Context mContext;
 
     @Override
     public void onCreate() {
          super.onCreate();
          CrashHandler crashHandler = CrashHandler.getInstance();
          crashHandler.init(getApplicationContext());
          mContext = this;
     }
}
这样的话，当程序代码中并未捕获异常，但发生了异常的时候，就会交由CrashHandler进行处理


UncaughtException接口的CrashHandler类的uncaughtException方法里面实现处理逻辑。
并且完成了自己的处理逻辑之后要执行系统原理默认对未捕获异常的处理。
 * 
 *   处理类,
 * 当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 * 
 * 因为toast里面有handler 所以你要在里面 你直接在looper调用hanler  toast不是uI页面
 * 
 * 就是当程序代码中并未捕获异常，但发生了异常的时候，就会交由CrashHandler
 * 然你自己先调用  自定义错误处理,收集错误信息 发送错误报告
 * 在退出程序，自己没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHandler";
	// 系统默认的UncaughtException处理类
	private UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler instance;
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();
	// 用于格式化日期,作为日志文件名的一部分
	@SuppressLint("SimpleDateFormat")
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}

	/**
	 * 初始化
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
//		!handleException(ex)  true:如果处理了该异常信息;否则返回false
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理，则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
//			杀死当前的进程
//            android.os.Process.killProcess(android.os.Process.myPid());
			AppManager.getAppManager().finishAllActivity();
			System.exit(0);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
//				因为toast里面有handler 所以你要在里面 你直接在looper调用hanler  toast不是uI页面
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 保存日志文件
		saveCatchInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	private String getFilePath() {
		String file_dir = "";
		// SD卡是否存在
		boolean isSDCardExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
		// Environment.getExternalStorageDirectory()相当于File file=new
		// File("/sdcard")
		boolean isRootDirExist = Environment.getExternalStorageDirectory().exists();
		if (isSDCardExist && isRootDirExist) {
			file_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crashlog/";
		} else {
			// MyApplication.getInstance().getFilesDir()返回的路劲为/data/data/PACKAGE_NAME/files，其中的包就是我们建立的主Activity所在的包
			file_dir = UnionApplcation.getInstance().getFilesDir().getAbsolutePath() + "/crashlog/";
		}
		return file_dir;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCatchInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			String file_dir = getFilePath();
			// if
			// (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			// {
			File dir = new File(file_dir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(file_dir + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes());
			// 发送给开发人员
			sendCrashLog2PM(file_dir + fileName);
			fos.close();
			// }
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	/**
	 * 将捕获的导致崩溃的错误信息发送给开发人员 目前只将log日志保存在sdcard 和输出到LogCat中，并未发送给后台。
	 */
	private void sendCrashLog2PM(String fileName) {
		// if (!new File(fileName).exists()) {
		// Toast.makeText(mContext, "日志文件不存在！", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// FileInputStream fis = null;
		// BufferedReader reader = null;
		// String s = null;
		// try {
		// fis = new FileInputStream(fileName);
		// reader = new BufferedReader(new InputStreamReader(fis, "GBK"));
		// while (true) {
		// s = reader.readLine();
		// if (s == null)
		// break;
		// //由于目前尚未确定以何种方式发送，所以先打出log日志。
		// Log.i("info", s.toString());
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally { // 关闭流
		// try {
		// reader.close();
		// fis.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
	}
}