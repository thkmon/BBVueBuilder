package com.bb.vue;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BBVueBuilder {

	public static void main(String[] args) {
		
		// 0. 시작
		System.out.println("start");
		
		try {
			String projectDirPath = "";
			
			// 1. 프로젝트 폴더 경로 지정
			// 아규먼트가 있을 경우 프로젝트 폴더 경로로 지정한다.
			// 아규먼트가 없을 경우 현재 디렉토리부터 상위 디렉토리로 올라가면서 프로젝트 폴더를 찾는다.
			if (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) {
				projectDirPath = args[0];
				
			} else {
				// 디렉토리를 찾는다. 현재 디렉토리부터 상위 디렉토리로 올라가면서 frontend 폴더를 갖고 있는 폴더를 찾으면 프로젝트 폴더로 간주한다.
				System.out.println("");
				System.out.println("ready to find project folder");
				
				File curDirObj = new File("");
				String curDirPath = curDirObj.getAbsolutePath();
				
				if (curDirPath.indexOf("/") > -1) {
					curDirPath = curDirPath.replace("/", "\\");
				}
				
				while (curDirPath.indexOf("\\\\") > -1) {
					curDirPath = curDirPath.replace("\\\\", "\\");
				}
				
				if (!curDirPath.endsWith("\\")) {
					curDirPath = curDirPath + "\\";
				}
				
				int axisIdx = curDirPath.length();
				while (true) {
					int delimiterIdx = curDirPath.lastIndexOf("\\", axisIdx);
					if (delimiterIdx < 0) {
						break;
					}
					
					String tempDirPath = curDirPath.substring(0, delimiterIdx);
					File tempDirObj = new File(tempDirPath);
					if (tempDirObj.exists()) {
						File[] fileArr = tempDirObj.listFiles();
						if (fileArr != null && fileArr.length > 0) {
							int fileCount = fileArr.length;
							for (int i=0; i<fileCount; i++) {
								if (fileArr[i].getName().equalsIgnoreCase("frontend") && fileArr[i].isDirectory()) {
									projectDirPath = tempDirPath;
									break;
								}
							}
						}
						
					}
					axisIdx = delimiterIdx - 1;
				}
			}
			
			if (projectDirPath == null || projectDirPath.length() == 0) {
				System.err.println("[ERROR] Unknown projectDirPath");
				return;
			}
			
			File projectDirObj = new File(projectDirPath);
			if (!projectDirObj.exists()) {
				System.err.println("[ERROR] Folder not exists. (" + projectDirObj.getAbsolutePath() + ")");
				return;
			}
			
			System.out.println("projectDirPath : " + projectDirPath);
			
			String distDirPath = projectDirPath + "\\frontend\\dist";
			
			
			// 2. 기존 배포 파일들 삭제
			System.out.println("");
			System.out.println("ready to delete distribution files");
			
			ArrayList<String> fileNameList = new ArrayList<String>();
			fileNameList.add("build.min.js");
			fileNameList.add("js_lib.min.js");
			fileNameList.add("style.css");
			
			int fileNameCount = fileNameList.size();
			for (int i=0; i<fileNameCount; i++) {
				String oneFilePath = distDirPath + "\\" + fileNameList.get(i);
				File oneFileObj = new File(oneFilePath);
				if (oneFileObj.exists()) {
					System.out.println("deleted : " + oneFilePath);
					oneFileObj.delete();
				}
			}
			
			for (int i=0; i<fileNameCount; i++) {
				String oneFilePath = distDirPath + "\\" + fileNameList.get(i);
				File oneFileObj = new File(oneFilePath);
				if (oneFileObj.exists()) {
					System.err.println("[ERROR] File deletion failed");
					return;
				}
			}
			
			
			// 3. npm 빌드 수행
			System.out.println("");
			System.out.println("ready to build vue files");
			
			String dirPath = projectDirPath + "\\frontend";
			
			ProcessBuilder processBuilder = null;
			Process process = null;
			
			InputStreamReader inputReader = null;
			BufferedReader bufferedReader = null;
			
			try {
				processBuilder = new ProcessBuilder();
				
				ArrayList<String> valueList = new ArrayList<String>();
				valueList.add("cmd");
				valueList.add("/C");
				valueList.add("npm run build");
				
				processBuilder.redirectErrorStream(true);
				processBuilder.command(valueList);
				processBuilder.directory(new File(dirPath));
				process = processBuilder.start();
	
				inputReader = new InputStreamReader(process.getInputStream());
				bufferedReader = new BufferedReader(inputReader);
				String line = null;
	
				while ((line = bufferedReader.readLine()) != null) {
					// System.out.println(line);
				}
				
			} catch (Exception e) {
				throw e;
				
			} finally {
				close(inputReader);
				close(bufferedReader);
				destory(process);
			}
			
			
			// 4. 빌드된 파일 복사
			System.out.println("");
			System.out.println("ready to copy builded files");
			
			String destCssDirPath = projectDirPath + "\\src\\main\\webapp\\resources\\css";
			String destJsDirPath = projectDirPath + "\\src\\main\\webapp\\resources\\js";
			
			int copiedCount = 0;
			
			for (int i=0; i<fileNameCount; i++) {
				String oneFilePath = distDirPath + "\\" + fileNameList.get(i);
				File oneFileObj = new File(oneFilePath);
				if (oneFileObj.exists()) {
					File destFileObj = null;
					if (oneFilePath.endsWith(".css")) {
						destFileObj = new File(destCssDirPath + "\\" + fileNameList.get(i));
						
					} else if (oneFilePath.endsWith(".js")) {
						destFileObj = new File(destJsDirPath + "\\" + fileNameList.get(i));
					}
					
					if (CopyUtil.copyFile(oneFileObj, destFileObj)) {
						System.out.println("copied : " + oneFileObj.getAbsolutePath() + " => " + destFileObj.getAbsolutePath());
						copiedCount++;
					}
				}
			}
			
			System.out.println("copied file count : " + copiedCount + "/" + fileNameCount);
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
			
		} finally {
		}
		
		// 5. 종료
		System.out.println("");
		System.out.println("end");
	}
	
	
	private static void close(InputStreamReader isr) {
		try {
			if (isr != null) {
				isr.close();	
			}
			
		} catch (Exception e) {
			// 무시
		} finally {
			isr = null;
		}
	}
	
	
	private static void close(BufferedReader br) {
		try {
			if (br != null) {
				br.close();
			}
			
		} catch (Exception e) {
			// 무시
		} finally {
			br = null;
		}
	}
	
	
	private static void destory(Process process) {
		try {
			if (process != null) {
				process.destroy();
			}
			
		} catch (Exception e) {
			// 무시
		} finally {
			process = null;
		}
	}
}