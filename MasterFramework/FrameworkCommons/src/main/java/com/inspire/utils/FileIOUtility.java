package com.inspire.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class FileIOUtility {
	public static Logger log = MasterLogger.getInstance();

	public static String[] readFileAsArray(String fileName, int no_of_lines) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		// FileInputStream fis=new FileInputStream(new File(fileName));
		String[] strArray = new String[no_of_lines];
		String line = "";
		int no_of_readLines = 1;
		while ((line = br.readLine()) != null || no_of_readLines < no_of_lines) {
			strArray[no_of_readLines - 1] = line;
			no_of_readLines++;
		}
		br.close();
		return strArray;

	}

	public static List<File> getAllFilesMatchingExp(String folderName, String regExp) {
		File dir = new File(folderName);
		List<File> files = new ArrayList<File>();
		File[] allFiles = dir.listFiles();
		for (File f : allFiles) {
			if (f.getName().endsWith(regExp)) {
				files.add(f);
			}
		}

		return files;
	}

	public static void deleteAllFilesMatchingPattern(String folderName, String regExp) {
		log.info("deleting all files matching " + regExp + " in folder " + folderName);
		List<File> files = getAllFilesMatchingExp(folderName, regExp);
		if (files.size() == 0) {
			log.info("nothing to delete");
		}
		for (int i = 0; i < files.size(); i++) {
			files.get(i).delete();
		}

	}

	public static String readFileAsString(String filePath) {
		String content = "";
		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			log.info(filePath + " File not found " + e.getMessage());
		}
		return content;
	}

	public static String checksum(File file) {
		try {
			InputStream fin = new FileInputStream(file);
			java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int read;
			do {
				read = fin.read(buffer);
				if (read > 0)
					md5er.update(buffer, 0, read);
			} while (read != -1);
			fin.close();
			byte[] digest = md5er.digest();
			if (digest == null)
				return null;
			String strDigest = "";
			for (int i = 0; i < digest.length; i++) {
				strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
			}
			return strDigest;
		} catch (Exception e) {
			return null;
		}
	}

	public static ArrayList<String> readFileAsArray(String fileName) {
		BufferedReader br;
		ArrayList<String> strArray=null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			strArray = new ArrayList<String>();
			String line = "";
			try {
				while ((line = br.readLine()) != null) {
					strArray.add(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			log.info(e.getMessage());
		}
		
		return strArray;

	}

	public static StringBuilder readFileAsCharStream(String fileName) throws IOException {
		FileReader fr = new FileReader(new File(fileName));
		StringBuilder str = new StringBuilder("");
		int c = 0;
		while ((c = fr.read()) != -1) {
			str.append(c);
		}
		fr.close();
		return str;

	}

	public static StringBuilder readFileAsByteStream(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(new File(fileName));
		StringBuilder str = new StringBuilder("");
		int c = 0;
		while ((c = fis.read()) != -1) {
			str.append(c);
		}
		fis.close();
		return str;

	}

	public static boolean createEmptyFile(String fileName) throws IOException {
		File file = new File(fileName);
		return file.createNewFile();
	}

	public static boolean createIfFolderNotExists(String folder) {
		if (isFileExists(folder) == false) {
			File fold = new File(folder);
			log.info("folder not exists will be created " + folder);
			return fold.mkdirs();
		}
		log.info("folder already exists " + folder);
		return false;
	}

	public static boolean isFileExists(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}

	public static boolean deleteFile(String fileName) {
		/*
		 * String[] fileNames=fileName.split("/"); String
		 * fileNameStr=fileNames[fileNames.length-1];
		 * 
		 * log.info(fileName);
		 */
		File f = new File(fileName);
		if (f.isFile()) {
			log.info(f.getAbsolutePath());
			log.info(f.getName());
			f.delete();
		} else {
			log.info(fileName + "is not a file");
		}

		return f.exists();
	}

	public static void deleteFileIfExists(String fileName) {
		if (isFileExists(fileName) == true) {
			File f = new File(fileName);
			f.delete();
		} else {
			log.info("file does not exists " + fileName);
		}
	}

	public static List<String> getAllFilesInFolder(String folderPath) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		List<String> fileList = new ArrayList<String>();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileList.add(file.getName());
			}
		}
		return fileList;
	}

	public static void deleteAllFilesNSubfolders(String directoryName) {
		log.info("deleting files from folder " + directoryName);
		File snapshotdir = new File(directoryName);
		if (!snapshotdir.exists()) {
			log.info("file does not exists");
		} else {
			for (File file : snapshotdir.listFiles()) {
				if (file.isDirectory()) {
					for (File f : file.listFiles()) {
						f.delete();
					}
				}
				if (file.exists())
					file.delete();
				snapshotdir.delete();
			}
		}
	}

	public static boolean isDirectory(String dirName) {
		File f = new File(dirName);
		return f.isDirectory();
	}

	public static boolean isFile(String dirName) {
		File f = new File(dirName);
		return f.isFile();
	}

	public static String getAbsolutePath(String fileName) {
		File f = new File(fileName);
		return f.getAbsolutePath();
	}

	public static String getPath(String fileName) {
		File f = new File(fileName);
		return f.getPath();
	}

	public static String getCanonicalPath(String fileName) throws IOException {
		File f = new File(fileName);
		return f.getCanonicalPath();
	}

	public static void copyFile(String srcFile, String destFile) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(srcFile));
			bw = new BufferedWriter(new FileWriter(destFile));
			String s = "";
			if (isFileExists(destFile)) {
				createEmptyFile(destFile);
			}
			while ((s = br.readLine()) != null) {
				bw.write(s);
			}
			bw.flush();
			br.close();

		} catch (FileNotFoundException e) {
			log.info("file not found " + e.getMessage());
		} catch (IOException e) {
			log.info("IO Exceptions " + e.getMessage());
		} finally {

		}
	}

	public static void mergeFiles(String srcFile1, String srcFile2, String destFile) {
		BufferedReader br1 = null, br2 = null;
		BufferedWriter bw = null;
		if (!isFileExists(srcFile1) || !isFileExists(srcFile2)) {
			return;
		}
		try {
			br1 = new BufferedReader(new FileReader(srcFile1));
			br2 = new BufferedReader(new FileReader(srcFile2));
			bw = new BufferedWriter(new FileWriter(destFile));
			String s = "";
			if (isFileExists(destFile)) {
				createEmptyFile(destFile);
			}
			while ((s = br1.readLine()) != null) {
				bw.write(s);
				bw.write("\n");
			}
			while ((s = br2.readLine()) != null) {
				bw.write(s);
				bw.write("\n");
			}
			bw.flush();
			br1.close();
			br2.close();

		} catch (FileNotFoundException e) {
			log.info("file not found " + e.getMessage());
		} catch (IOException e) {
			log.info("IO Exceptions " + e.getMessage());
		} finally {

		}
	}

	public static void copyBinaryFile(String srcFile, String destFile) {
		log.info("copying source file " + srcFile);
		log.info("destination " + destFile);
		Path sourceFile = Paths.get(srcFile);
		Path targetFile = Paths.get(destFile);

		try {

			Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException ex) {
			log.info("I/O Error when copying file " + srcFile + " " + destFile);
		}

	}

	public static void writeFile(File file) {
		FileWriter fr;
		try {
			fr = new FileWriter(file);
			fr.flush();
		} catch (IOException e) {
			log.info("IO Exceptions " + e.getMessage());
		}

	}

	public static void writeFile(File file, String s) {
		FileWriter fr;
		try {
			fr = new FileWriter(file);
			fr.write(s);
			fr.flush();
		} catch (IOException e) {
			log.info("IO Exceptions " + e.getMessage());
		}

	}

	public static long getFileSize(String strfile) {
		File file = new File(strfile);
		long fileSize = 0;
		if (file.exists()) {
			fileSize = file.length();
		}
		return fileSize;
	}

	public static void unzipFile(String inputZipFileName, String destFolder) throws IOException {
		int BUFFER = 2048;
		File file = new File(inputZipFileName);

		ZipFile zip = new ZipFile(file);
		String newPath = inputZipFileName.substring(0, inputZipFileName.length() - 4);

		new File(newPath).mkdir();
		Enumeration zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			// destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();
			}

		}
	}

	public static void downloadFile(String url, String destination) {

		File dest = new File(destination + "PresignedURLMetadata.zip");
		URL website = null;
		try {
			website = new URL(url);
		} catch (MalformedURLException e) {
			log.info("Malformed URL Exception occurred " + e.getMessage());
		}
		try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				FileOutputStream fos = new FileOutputStream(dest);) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			log.info("IO Exceptions " + e.getMessage());
		}
		log.info("file downloaded from " + url + "successfully at " + destination);
	}

	public static boolean renameFile(String oldFileName, String newFileName) {
		boolean isSuccess = false;
		File oldName = new File(oldFileName);
		File newName = new File(newFileName);
		if (newName.exists()) {
			log.info("rename failed file " + newFileName + " already exists");
			return false;
		}
		if (oldName.renameTo(newName)) {
			log.info("file renamed");
			isSuccess = true;
			return isSuccess;
		} else {
			log.info("Error while renaming the file " + oldName);
			return isSuccess;
		}
	}

}
