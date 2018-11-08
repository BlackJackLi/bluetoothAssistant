package com.gizwits.energy.android.lib.utils;


import com.gizwits.energy.android.lib.base.AbstractConstantClass;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by black-Gizwits on 2015/10/12.
 */
public final class FileUtils extends AbstractConstantClass {

	private FileUtils() {
		super();
	}

	public static final long UNIT_KB = 1L << 10;//1024B,1KB
	public static final long UNIT_MB = 1L << 20;//1024KB,1MB
	public static final long UNIT_GB = 1L << 30;//1024MB,1GB
	public static final long UNIT_TB = 1L << 40;//1024GB/1TB
	public static final long UNIT_PB = 1L << 50;//1024TB/1PB;
	private static final DecimalFormat FILE_SIZE_FORMAT = new DecimalFormat("0.##");

	/**
	 * 递归计算文件夹下的文件总大小,也可以计算单个文件的大小,如果预期目录比较大,请使用线程
	 *
	 * @param file
	 * @return
	 */
	public static long calculateFileSize(File file) {
		long totalSize = 0;
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File subFile : files) {
					totalSize += calculateFileSize(subFile);
				}
			} else {
				totalSize += file.length();
			}
		}
		return totalSize;
	}

	/**
	 * 递归计算文件夹下的文件总大小,也可以计算单个文件的大小,如果预期目录比较大,请使用线程
	 *
	 * @param filePath
	 * @return
	 */
	public static long calculateFileSize(String filePath) {
		return calculateFileSize(new File(filePath));
	}

	/**
	 * 清除文件夹内所有文件(不包括此文件夹)
	 *
	 * @param dir
	 */
	public static void cleanDir(File dir) {
		if (dir != null && dir.exists()) {
			File[] files = dir.listFiles();
			for (File subFile : files) {
				deleteFile(subFile);
			}
		}
	}

	/**
	 * 清除文件夹内所有文件(不包括此文件夹)
	 *
	 * @param dirPath
	 */
	public static void cleanDir(String dirPath) {
		cleanDir(new File(dirPath));
	}

	/**
	 * 递归删除文件夹下的所有文件(包括此文件夹),也可以删除单个文件,如果预期目录比较大,请使用线程
	 *
	 * @param file
	 * @return
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File subFile : files) {
					deleteFile(subFile);
				}
			}
			file.delete();
		}
	}

	/**
	 * 递归删除文件夹下的所有文件(包括此文件夹),也可以删除单个文件的,如果预期目录比较大,请使用线程
	 *
	 * @param filePath
	 * @return
	 */
	public static void deleteFile(String filePath) {
		deleteFile(new File(filePath));
	}

	public static String formatSize(long size) {
		String formatString = "0KB";
		if (0 < size && size < UNIT_KB) {
			formatString = FILE_SIZE_FORMAT.format(size) + "B";
		} else if (UNIT_KB < size && size < UNIT_MB) {
			formatString = FILE_SIZE_FORMAT.format(size * 1.0 / UNIT_KB) + "KB";
		} else if (UNIT_MB < size && size < UNIT_GB) {
			formatString = FILE_SIZE_FORMAT.format(size * 1.0 / UNIT_MB) + "MB";
		} else if (UNIT_GB < size && size < UNIT_TB) {
			formatString = FILE_SIZE_FORMAT.format(size * 1.0 / UNIT_GB) + "GB";
		} else if (UNIT_TB < size) {
			formatString = FILE_SIZE_FORMAT.format(size * 1.0 / UNIT_TB) + "TB";
		} else if (UNIT_PB < size) {
			formatString = FILE_SIZE_FORMAT.format(size * 1.0 / UNIT_PB) + "PB";
		}
		return formatString;
	}

	public static String formatSize(long size, long unit, String unitString) {
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(2);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format((double)size / (double)unit) + " " + unitString;
	}

}
