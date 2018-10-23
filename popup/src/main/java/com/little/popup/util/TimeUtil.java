package com.little.popup.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {


	public static String getStamp(){
		long time = System.currentTimeMillis();
		String stamp = ""+time;
		return stamp;
	}

	/**
	 * 根据格式转换为时间戳
	 * @param format 时间格式 如yyyy-MM-dd HH:mm:ss
	 * @param time 时间
	 * @return
	 */
	public static String tranFormatTime(String time,String format){
		String re_time = "";
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(""+format);
			Date date = formatter.parse(time);
			long l = date.getTime();
			re_time = String.valueOf(l);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	public static long tranFormatTime2(String time,String format){
		long re_time = 0;
		try {
			SimpleDateFormat formatter   =   new SimpleDateFormat(""+format);
			Date date = formatter.parse(time);
			re_time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}


	/**
	 * 根据格式获取当前时间
	 * @param format 时间格式 如yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime(String format){
		SimpleDateFormat formatter   =   new SimpleDateFormat(""+format);
		Date curDate   =   new Date(System.currentTimeMillis());//获取当前时间
		String str   =   formatter.format(curDate);
		return str;
	}


	public static String formatServiceTime1(String time) {
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long loc_time = Long.valueOf(time);
			result = sdf.format(new Date(loc_time));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	public static String formatServiceTime2(String time) {
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long loc_time = Long.valueOf(time);
			result = sdf.format(new Date(loc_time));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	public static String formatServiceTime3(String time) {
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			long loc_time = Long.valueOf(time);
			result = sdf.format(new Date(loc_time));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	public static String formatServiceTime4(String time) {
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			long loc_time = Long.valueOf(time);
			result = sdf.format(new Date(loc_time));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	public static boolean isToday(String time){
		boolean isToday = false;
		if (getCurrentTime("yyyy-MM-dd").equals(formatServiceTime1(time))){
			isToday = true;
		}
		return isToday;
	}

	public static String formatTrackTime(String time) {
		String result = time;
		return result;
	}

	public static long msFormat(String time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long timeStart = 0;
		try {timeStart=sdf.parse(time).getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStart;
	}

	public static String formatInternalTime(String time) {
		String result = "";
		try {
			long t = Long.parseLong(time);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	public static String formatMsgTime(String time) {
		String result = time;
		try {
			result = time.split(" ")[0];
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return result;
		}
	}

	//判断当月存在几天
	public static String getDay(String date){
		return null;
	}

}