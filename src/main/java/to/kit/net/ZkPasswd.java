package to.kit.net;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * ZKパスワード.
 * @author Hidetaka Sasai
 */
public final class ZkPasswd {
	public static final ZkPasswd Instance = new ZkPasswd();

	private Calendar trimWeekOfMonth(Calendar cal) {
		Calendar wk = (Calendar) cal.clone();
		int dayOfWeek = wk.get(Calendar.DAY_OF_WEEK); // 何曜日

		if (dayOfWeek == Calendar.SUNDAY) {
			// 日曜日の場合、wk を -1日する
			wk.add(Calendar.DAY_OF_MONTH, -1);
			dayOfWeek = Calendar.SATURDAY;
		}
		if (wk.get(Calendar.WEEK_OF_MONTH) == 1) {
			// 第1週の場合
			int day = wk.get(Calendar.DAY_OF_MONTH); // 月の日
			int week = (dayOfWeek + 35 - day) % 7 + 1; // 月の1日の曜日
			if (Calendar.TUESDAY <= week) {
				// 月の1日が火～土の場合
				wk.add(Calendar.DAY_OF_MONTH, -day);
			}
		}
		return wk;
	}

	public String getPassword(Calendar cal) {
		Calendar wk = trimWeekOfMonth(cal);
		int year = wk.get(Calendar.YEAR) %100;
		int month = wk.get(Calendar.MONTH) + 1;
		int weekOfMonth = wk.get(Calendar.WEEK_OF_MONTH); // 月の何週目か
		return String.format("%02d%02d%02d", Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(weekOfMonth));
	}

	public String getLastPassword() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1); // 1日前
		return getPassword(cal);
	}

	public String getCurrentPassword() {
		return getPassword(Calendar.getInstance());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ZkPasswd pass = new ZkPasswd();
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yy/MM/dd(E)");

//		for (int cnt = 0; cnt < 30; cnt++) {
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//		}
		for (int cnt = 0; cnt < 50; cnt++) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 何曜日
			String current = format.format(cal.getTime());
			StringBuilder buff = new StringBuilder();
			buff.append(cnt);
			buff.append(":");
			buff.append(current);
			buff.append(" ");
			buff.append(pass.getPassword(cal));
			if (dayOfWeek == Calendar.SUNDAY) {
				buff.append("\n");
			}
			System.out.println(buff);
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

}
