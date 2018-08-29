package com.lhm.filter;

import java.io.File;
import java.io.FileFilter;

public class TxtFilter implements FileFilter{

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".txt");
	}

}
