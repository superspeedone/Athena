package com.superspeed.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 文件下载工具类
 * @ClassName: FileDownloadUtils
 * @Description: 文件下载工具类
 * @author xc.yanww
 * @date 2017/8/14 17:09
 * @version 1.0
 */
public class FileDownloadUtils {

    private  static Logger logger = LoggerFactory.getLogger(FileDownloadUtils.class);

    /**
     * 文件下载
     * @author xc.yanww
     * @date 2017/8/15 11:04
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param downLoadFileName 下载文件名称
     * @param downLoadFilePath 下载文件绝对路径
     * @return void
     */
    public static void fileDownload(HttpServletRequest request, HttpServletResponse response, String downLoadFileName, String downLoadFilePath) {
        logger.info("文件下载 - 文件名:[{}]，文件路径：[{}]", downLoadFileName, downLoadFilePath);
        try {
            //新建输入和输出流
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            //获取文件的长度
            long downLoadFileLength = new File(downLoadFilePath).length();

            //设置文件输出类型
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(downLoadFileName.getBytes("utf-8"), "ISO8859-1"));
            //设置输出长度
            response.setHeader("Content-Length", String.valueOf(downLoadFileLength));
            //获取输入流
            bis = new BufferedInputStream(new FileInputStream(downLoadFilePath));
            //输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int br;
            while (-1 != (br = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, br);
            }
            //关闭流
            bis.close();
            bos.close();
        } catch (Exception e) {
            logger.error("下载文件出错,{}", e.getMessage(), e);
        }
    }

}
