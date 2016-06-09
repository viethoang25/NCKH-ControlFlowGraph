package manager;

import java.text.*;
import java.util.*;

public class DateManager {

	private static int[] dayOfMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	private static boolean leapYear(int year) {
		return ((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0);
	}

	public static boolean checkValidate(String date) {
		boolean check = true;
		try {
			if (date.length() != 10)
				check = false;
			String[] item = date.trim().split("/");
			if (item.length < 3)
				check = false;
			int day, month, year;
			day = Integer.parseInt(item[0]);
			month = Integer.parseInt(item[1]);
			year = Integer.parseInt(item[2]);
			if (month < 1 || month > 12)
				check = false;
			if (leapYear(year) && month == 2 && day != 29)
				check = false;
			if (dayOfMonth[month] < day || day < 1)
				check = false;

		} catch (Exception e) {
			check = false;
		}
		return check;
	}

	public static String getStringDatetime(Date date) {
		if (date == null)
			return null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return dateFormat.format(cal.getTime());
	}

	public static Date getDateTime(String stringDate) {
		if (stringDate == null)
			return null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date = df.parse(stringDate);
			return date;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static java.sql.Date getSqlDate(Date normalDate) {
		if (normalDate == null)
			return null;
		return new java.sql.Date(normalDate.getTime());
	}

	public static Date getNormalDate(java.sql.Date sqlDate) {
		if (sqlDate == null)
			return null;
		return new Date(sqlDate.getTime());
	}

	public static void main(String[] args) {
		// System.out.println(DateManager.getDateTime("25/1 1/1994"));
		// System.out.println(DateManager.getDateTime("28/02/1994"));
		System.out.println(DateManager.getDateTime("29/02/199 4"));
		System.out.println(DateManager.getDateTime("31/11/1994"));
		System.out.println(DateManager.getDateTime("25/1/19 94"));
	}
}
