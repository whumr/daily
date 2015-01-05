package com.test.down.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.test.down.constant.DownloadConstants.CONFIG;

public class DownloadUtil {

	private static final int NEW_FILE_START_INDEX = CONFIG.NEW_FILE_START_INDEX;
	
	public static int checkFile(String fileName, String urlName) {
		String filePath = CONFIG.DOWNLOAD_PATH + fileName;
		File file = new File(filePath);
		if(file.exists())
			return CONFIG.FILE_EXIST;
		file = new File(filePath + CONFIG.TEMP_FILE_SUFFIX);
		if(file.exists()) {
			file = new File(filePath + CONFIG.URL_FILE_SUFFIX);
			if(file.exists())
				return readFileContent(file).equals(urlName) ? CONFIG.TEMP_FILE_EXIST_URL_EQUAL : CONFIG.TEMP_FILE_EXIST_URL_NOT_EQUAL;
			else
				return CONFIG.TEMP_FILE_EXIST_URL_NOT_EQUAL;
		}
		return CONFIG.FILE_NOT_EXIST;
	}
	
	public static int getRemoteFileSize(URL url) {
		int size = -1;
		try {
			URLConnection urlConnection = url.openConnection();
			size = urlConnection.getContentLength();
			if(urlConnection instanceof HttpURLConnection)
				((HttpURLConnection)urlConnection).disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	}
	
	public static int getLocalFileSize(String file) {
		return (int)new File(file).length();
	}
	
	/**
	 * create temp files, if files exist, add a number.
	 * e.g:	if file "test.txt" exists, file "test(1).txt.tmp" and "test(1).txt.url" will be created.
	 * 		if file "test(1).txt.tmp" exist, file "test(2).txt.tmp" and "test(2).txt.url" will be created.
	 * 		and so on.
	 * @param fileName
	 * @param urlName
	 * @return
	 * @throws IOException 
	 */
	public static String prepareFile(String fileName, String urlName) throws IOException {
		File file = new File(CONFIG.DOWNLOAD_PATH + fileName);
		return createTempFile(fileName, urlName, file.exists());
	}
	
	private static String createTempFile(String fileName, String urlName, boolean exist) throws IOException {
		File directory = new File(CONFIG.DOWNLOAD_PATH);
		if(!directory.exists())
			directory.mkdirs();
		String filePath = CONFIG.DOWNLOAD_PATH + fileName;
		File tempFile;
		if(!exist) {
			tempFile = new File(filePath + CONFIG.TEMP_FILE_SUFFIX);
			if(!tempFile.exists()) {
				tempFile.createNewFile();
				writeFileContent(filePath + CONFIG.URL_FILE_SUFFIX, urlName);
				return filePath + CONFIG.TEMP_FILE_SUFFIX;
			}
		}
		String names[] = {fileName, ""};
		int suffixIndex = fileName.lastIndexOf(".");
		if(suffixIndex > 0) {
			names[0] = fileName.substring(0, suffixIndex);
			names[1] = fileName.substring(suffixIndex);
		}
		StringBuilder buffer;
		for (int i = NEW_FILE_START_INDEX; ; i++) {
			buffer = new StringBuilder();
			buffer.append(CONFIG.DOWNLOAD_PATH)
			.append(names[0])
			.append("(").append(i).append(")")
			.append(names[1]);
			tempFile = new File(buffer.toString() + CONFIG.TEMP_FILE_SUFFIX);
			if(!tempFile.exists()) {
				tempFile.createNewFile();
				writeFileContent(buffer.toString() + CONFIG.URL_FILE_SUFFIX, urlName);
				return buffer.toString() + CONFIG.TEMP_FILE_SUFFIX;
			}
		}
	}
	
	public static void finishDownload(String tempFile) {
		File file = new File(tempFile);
		if(file.exists()) {
			if(tempFile.endsWith(CONFIG.TEMP_FILE_SUFFIX)) {
				String path = tempFile.substring(0, tempFile.length() - CONFIG.TEMP_FILE_SUFFIX.length());
				//rename
				file.renameTo(new File(path));
				//delete url file
				file = new File(path + CONFIG.URL_FILE_SUFFIX);
				if(file.exists())
					file.delete();
			}
		}
	}
	
	public static void writeFileContent(String filePath, String content) throws IOException {
		FileWriter writer = new FileWriter(filePath);
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	public static String readFileContent(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuilder buffer = new StringBuilder();
			while((temp = reader.readLine()) != null)
				buffer.append(temp).append("\n");
			reader.close();
			if(buffer.length() > 0)
				return buffer.substring(0, buffer.length() - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
