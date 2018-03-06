package com.superspeed.common.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ftp上传下载工具类
 * @ClassName: FtpUtils
 * @Description: Ftp上传下载工具类
 * @author xc.yanww
 * @date 2017-5-26 下午3:35:52
 * @version 1.0
 */
public class FtpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(FtpUtils.class);
	
	//ftp服务器地址
	public static final String FTP_ADDRESS;
	//ftp默认端口号
	public static final int FTP_PORT = 21;
	//ftp服务器用户名
	public static final String FTP_USERNAME;
	//ftp服务器用户密码
	public static final String FTP_PASSWORD;
	//ftp传输编码
	public static final String FTP_CONTROL_ENCODE;
	//连接超时时间60s
	public static final int CONNECT_TIME_OUT = 60*1000;
	
	static {
		//ftp配置初始化
		FTP_ADDRESS = PropertieUtils.getValue("ftp.ipAddress");
		FTP_USERNAME = PropertieUtils.getValue("ftp.username");
		FTP_PASSWORD = PropertieUtils.getValue("ftp.password");
		String ftpEncoding = PropertieUtils.getValue("ftp.encoding");
		if (null == ftpEncoding || "".equals(ftpEncoding)) {
			FTP_CONTROL_ENCODE = "utf-8";
		} else {
			FTP_CONTROL_ENCODE = ftpEncoding;
		}
		if (ValidUtils.isEmpty(FTP_ADDRESS) || ValidUtils.isEmpty(FTP_USERNAME) || ValidUtils.isEmpty(FTP_PASSWORD)) {
			logger.error("ftp服务器初始化失败");
		} else {
			logger.info("ftp服务器初始化成功  ip地址:{} 编码:{}", FTP_ADDRESS, ftpEncoding);
		}
	}
	
	/**
	 * 私有构造方法，防止ftp工具类被实例化
	 */
	private FtpUtils() {}
	
	/**
	 * 连接ftp服务器
	 * @author xc.yanww
	 * @date 2017-2-27 下午5:25:25
	 * @param ftpClient ftp客户端
	 * @return
	 * @throws Exception
	 */
	private static boolean connect(FTPClient ftpClient) throws Exception {
		logger.info("正在连接ftp服务器:{}, 端口号:{}", FTP_ADDRESS, FTP_PORT);
		try {
			int reply;
			//设置传输编码，避免中文乱码以及中文名字无法上传的问题
			ftpClient.setControlEncoding(FTP_CONTROL_ENCODE);  
			//ftpClient.setConnectTimeout(CONNECT_TIME_OUT);   TODO
			/*ftpClient.setDefaultTimeout(30 * 1000);
			ftpClient.setConnectTimeout(30 * 1000);
			ftpClient.setDataTimeout(30 * 1000);*/
			//连接FTP服务器   如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.connect(FTP_ADDRESS, FTP_PORT);
			//登录ftp服务器
			ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
			//如果reply返回230就算成功了，如果返回530密码用户名错误或当前用户无权限下面有详细的解释。
			reply = ftpClient.getReplyCode();
			logger.info("ftp登陆返回码==>{}", reply);
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			//文件以二进制方式进行传输(必须打开ftp连接之后设置)
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			logger.info("ftp服务器连接成功");
		} catch (Exception e) {
			logger.error("ftp服务器连接失败:" + e.getMessage());
			throw e;
		}
		return true;
	}
	
	/**
	 * 关闭ftp连接
	 * @author xc.yanww
	 * @date 2017-3-1 上午9:55:42
	 * @param ftpClient ftp客户端
	 */
	private static void close(FTPClient ftpClient) {
		logger.info("ftp连接关闭");
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error("关闭ftp连接失败:" + e.getMessage());
			} 
		}
	}
	
	/**
	 * 创建指定目录
	 * @author xc.yanww
	 * @date 2017-3-1 下午12:34:23
	 * @param ftpClient ftp客户端
	 * @param remotePath ftp远程目录
	 * @throws Exception
	 * @version 1.0
	 */
	private static void makeDirectory(FTPClient ftpClient, String remotePath) throws Exception {
		logger.info("切换到ftp目录:{}", remotePath);
		if (!ftpClient.changeWorkingDirectory(remotePath)) {
			logger.info("创建ftp目录:{}", remotePath);
			ftpClient.makeDirectory(remotePath);
			ftpClient.changeWorkingDirectory(remotePath);
		}
	}
	
	/**
	 * 向FTP服务器上传文件
	 * @author xc.yanww
	 * @date 2017-3-1 上午10:18:34
	 * @param remotePath  FTP服务器保存目录(相对与ftp根目录的路径)
	 * @param fileName  上传到FTP服务器上的文件名
	 * @param localPath  上传文件的路径
	 * @return  上传成功返回true，否则返回false
	 */
	public static boolean uploadFile(String remotePath, String fileName, String localPath) {
		boolean isSuccess = false;
		FTPClient ftp = new FTPClient();
		FileInputStream in = null;
		try {
			//打开ftp连接
			if (!connect(ftp)) {
				logger.info("ftp服务器连接失败");
				return isSuccess;
			}
			logger.info("ftp开始上传  远程路径:{}, 文件名:{}", remotePath, fileName);
			in = new FileInputStream(new File(localPath));
			//切换ftp路径,不存在则创建
			makeDirectory(ftp, remotePath);
			//保存文件
			ftp.storeFile(fileName, in);
			in.close();
			//登出ftp
			ftp.logout();
			isSuccess = true;
			logger.info("ftp文件上传完成");
		} catch (Exception e) {
			logger.error("ftp文件上传失败," + e.getMessage());
		} finally {
			close(ftp);
		}
		return isSuccess;
	}
	
	/**
	 * 从FTP服务器下载文件 
	 * @author xc.yanww
	 * @date 2017-3-1 上午10:17:58
	 * @param remotePath FTP服务器上的相对路径(相对与根目录的路径)
	 * @param fileName  要下载的文件名 
	 * @param localPath  下载后保存到本地的路径 
	 * @return 下载成功返回true，否则返回false
	 */
	public static boolean downFile(String remotePath, String fileName, String localPath) {
		boolean isSuccess = false;
		FTPClient ftp = new FTPClient();
		OutputStream os = null;
		try {
			//打开ftp连接
			if (!connect(ftp)) {
				logger.info("ftp服务器连接失败");
				return isSuccess;
			}
			logger.info("ftp开始下载  远程路径:{}, 文件名:{}", remotePath, fileName);
			//转移到FTP服务器目录
			if (!ftp.changeWorkingDirectory(remotePath)) {
				throw new Exception("ftp远程目录:" + remotePath + "不存在");
			}
			int fileNum = 0;
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					fileNum++;
					File localFile = new File(localPath + "/" + ff.getName());
					os = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), os);
					os.close();
				}
			}
			if (fileNum==0) {
				throw new Exception("ftp远程目录:" + remotePath + "不存在文件:"+fileName);
			}
			ftp.logout();
			isSuccess = true;
			logger.info("ftp文件下载完成");
		} catch (Exception e) {
			logger.error("ftp文件下载失败," + e.getMessage());
		} finally {
			close(ftp);
		}
		return isSuccess;
	}
	
	/**
	 * 列出ftp服务器上指定文件夹下面的所有文件和子文件夹名称
	 * @author xc.yanww
	 * @date 2017-3-24 下午7:33:33
	 * @param remotePath ftp远程目录
	 * @return
	 */
	public static String[] listFiles(String remotePath) {
		return listFiles(remotePath, null);
	}
	
	/**
	 * 列出ftp服务器上指定文件夹下面的所有文件和子文件夹名称
	 * @author xc.yanww
	 * @date 2017-3-24 下午7:33:33
	 * @param remotePath ftp远程目录
	 * @param fileFormat 文件格式，不带点，例如txt
	 * @return
	 */
	public static String[] listFiles(String remotePath, String fileFormat) {
		FTPClient ftp = new FTPClient();
		String[] fileNames = null;
		FTPFile[] files = null;
				
		try {
			if (!connect(ftp)) {
				return null;
			}
			logger.info("开始列出ftp服务器文件夹下的文件名  远程路径:{}, 文件格式:{}", remotePath, fileFormat);
			// Use passive mode to pass firewalls.    
			ftp.enterLocalPassiveMode();
			//转移到FTP服务器目录
			if (!ftp.changeWorkingDirectory(remotePath)) {
				throw new Exception("ftp远程目录:" + remotePath + "不存在");
			}
		    //if (null != fileFormat && !"".equals(fileFormat)) {
			//	files = ftp.listFiles("", new MyFtpFileFilter(fileFormat));
			//} else {
				files = ftp.listFiles(remotePath);
				
			//}
			if (files.length > 0) {
				fileNames = ftpFileFilter(files, fileFormat);
			}
			ftp.logout();
			logger.info("ftp服务器文件名遍历完成");
		} catch (Exception e) {
			logger.error("列出ftp服务器文件夹下的文件名失败," + e.getMessage());
		} finally {
			close(ftp);
		}
		return fileNames;
	}
	
	/**
	 * ftp服务器文件重命名
	 * @author xc.yanww
	 * @date 2017-3-24 下午8:12:23
	 * @param remotePath ftp远程目录
	 * @param oldName 旧文件名
	 * @param newName 新文件名
	 * @return
	 */
	public static boolean rename(String remotePath, String oldName, String newName) {
		boolean isSuccess = false;
		FTPClient ftp = new FTPClient();
				
		try {
			if (!connect(ftp)) {
				return isSuccess;
			}
			logger.info("开始修改ftp服务器文件名称  远程路径:{}, 旧文件名:{}, 新文件名:{}", new String[] { remotePath, oldName, newName });
			//转移到FTP服务器目录
			if (!ftp.changeWorkingDirectory(remotePath)) {
				throw new Exception("ftp远程目录:" + remotePath + "不存在");
			}
			//修改文件名
			isSuccess = ftp.rename(oldName, newName);
			//退出登录
			ftp.logout();
			logger.info("ftp服务器文件名修改{}", isSuccess ? "完成" : "失败");
		} catch (Exception e) {
			logger.error("ftp服务器文件名修改失败," + e.getMessage());
		} finally {
			close(ftp);
		}
		return isSuccess;
	}
	
	/**
	 * ftp服务器文件删除
	 * @author xc.yanww
	 * @date 2017-3-24 下午8:12:23
	 * @param remotePath ftp远程目录，以“/”结尾
	 * @param fileName 文件名
	 * @return
	 */
	public static boolean delete(String remotePath, String fileName) {
		boolean isSuccess = false;
		FTPClient ftp = new FTPClient();
				
		try {
			if (!connect(ftp)) {
				return isSuccess;
			}
			logger.info("开始修改ftp服务器文件名称  远程路径:{}, 文件名:{}", remotePath, fileName);
			//转移到FTP服务器目录
			if (!ftp.changeWorkingDirectory(remotePath)) {
				throw new Exception("ftp远程目录:" + remotePath + "不存在");
			}
			//修改文件名
			isSuccess = ftp.deleteFile(remotePath + "/" + fileName);
			//退出登录
			ftp.logout();
			logger.info("ftp服务器文件删除{}", isSuccess ? "成功" : "失败");
		} catch (Exception e) {
			logger.error("ftp服务器文件删除失败," + e.getMessage());
		} finally {
			close(ftp);
		}
		return isSuccess;
	}
	
	/**
	 * 文件过滤
	 * @author xc.yanww
	 * @date 2017-4-7 下午8:34:06
	 * @param files
	 * @return
	 */
	private static String[] ftpFileFilter(FTPFile[] files, String fileFormat) {
		List<String> fileNameList = new ArrayList<String>();
		String[] fileNames = null;
		logger.info("ftp文件过滤");
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().toLowerCase().endsWith(fileFormat)) {
				fileNameList.add(files[i].getName());
			}
		}
		if (fileNameList.size() > 0) {
			fileNames = new String[fileNameList.size()];
			for (int j = 0; j < fileNameList.size(); j++) {
				fileNames[j] = fileNameList.get(j);
			}
		}
		return fileNames;
	}
	
}


