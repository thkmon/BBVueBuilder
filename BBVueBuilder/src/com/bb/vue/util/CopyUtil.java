package com.bb.vue.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class CopyUtil {
	
	public static boolean copyFile(File originFile, File newFile) throws Exception {
		boolean bCopied = false;

		if (originFile == null) {
			System.err.println("originFile == null");
			return false;
		}

		if (newFile == null) {
			System.err.println("newFile == null");
			return false;
		}

		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			// 원본파일이 반드시 존재해야 복사 가능하다.
			if (!originFile.exists()) {
				System.err.println("!originFile.exists()");
				return false;
			}
			
			if (newFile.exists()) {
				newFile.delete();
			}

			// newFile 의 parentDir(부모 폴더)가 반드시 존재해야 한다. 존재하지 않을 경우 만들어줘야 함.
			boolean mkdir = makeParentDir(newFile.getAbsolutePath());
			if (!mkdir) {
				return false;
			}

			inputStream = new FileInputStream(originFile);
			outputStream = new FileOutputStream(newFile);

			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			long size = fcin.size();
			fcin.transferTo(0, size, fcout);

			bCopied = true;

		} catch (Exception e) {
			throw e;

		} finally {
			try {
				if (fcout != null) {
					fcout.close();
				}
			} catch (Exception e) {
				// ignore
			} finally {
				fcout = null;
			}

			try {
				if (fcin != null) {
					fcin.close();
				}
			} catch (Exception e) {
				// ignore
			} finally {
				fcin = null;
			}

			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				// ignore
			} finally {
				outputStream = null;
			}

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				// ignore
			} finally {
				inputStream = null;
			}
		}

		return bCopied;
	}
	
	
	/**
	 * 특정 파일패스의 부모 폴더가 없을 경우 만든다.
	 * 
	 * @param filePath
	 * @return
	 */
	private static boolean makeParentDir(String filePath) {
		boolean bResult = false;

		if (filePath == null || filePath.trim().length() == 0) {
			System.err.println("makeParentDir : filePath == null || filePath.length() == 0");
			return false;

		} else {
			filePath = filePath.trim();
		}

		// 역슬래시를 슬래시로 모두 변경
		if (filePath.indexOf("\\") > -1) {
			filePath = filePath.replace("\\", "/");
		}

		// 슬래시 2개를 슬래시 1개로 모두 변경
		while (filePath.indexOf("//") > -1) {
			filePath = filePath.replace("//", "/");
		}

		// 필요한 디렉토리 만들기
		int lastSlashPos = filePath.lastIndexOf("/");

		if (lastSlashPos > -1) {
			File d = new File(filePath.substring(0, lastSlashPos));
			if (d.exists()) {
				// 폴더 존재하면 생성할 필요없다.
				bResult = true;
			} else {
				bResult = d.mkdirs();
			}

		} else {
			System.err.println("makeParentDir : lastSlashPos not exists");
			return false;
		}

		return bResult;
	}
}
