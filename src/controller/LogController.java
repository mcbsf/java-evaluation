package controller;

import java.io.File;

public class LogController {
	
	public static boolean log_file_exception(Exception e, File file) {
		return true;
	}
	
	public static boolean log_line_exception(Exception e, File file, int line_index) {
		return true;
	}
	
	public static boolean log_exception(Exception e) {
		return true;
	}
}
