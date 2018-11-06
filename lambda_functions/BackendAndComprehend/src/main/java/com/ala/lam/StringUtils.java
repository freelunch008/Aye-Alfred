package com.ala.lam;

import java.util.Collection;

import com.google.gson.Gson;

public class StringUtils {

	public static String printStingArray(String[] strings) {
		StringBuffer buffer = new StringBuffer();
		int len = strings.length;
		for (int i = 0; i < len; i++) {
			if (i > 0)
				buffer.append(" ");
			buffer.append(strings[i]);
		}

		return buffer.toString();
	}

	public static boolean isEmpty(String s) {
		return (s == null) || s.isEmpty();
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String difference(String str1, String str2) {
		if (str1 == null) {
			return str2;
		}
		if (str2 == null) {
			return str1;
		}
		int at = indexOfDifference(str1, str2);
		if (at == -1) {
			return "";
		}
		return str2.substring(at);
	}

	public static int indexOfDifference(String str1, String str2) {
		if (str1 == str2) {
			return -1;
		}
		if (str1 == null || str2 == null) {
			return 0;
		}
		int i;
		for (i = 0; i < str1.length() && i < str2.length(); ++i) {
			if (str1.charAt(i) != str2.charAt(i)) {
				break;
			}
		}
		if (i < str2.length() || i < str1.length()) {
			return i;
		}
		return -1;
	}

	public static String convertListToString(Collection<Long> ids) {
		return ids.toString().replace("[", "").replace("]", "");
		// .replace(", ", "','");
	}

	public static String createQuestion(Collection<Long> ids) {
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (Object obj : ids) {
			count++;
			if (count > 1)
				buffer.append(",");
			buffer.append("?");
		}
		return buffer.toString();
	}
	
	public static boolean isLong(String str) 
	{
		try  
		  {  
		    long d =Long.parseLong(str);
		  }  
		  catch(Exception nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}
	

}
