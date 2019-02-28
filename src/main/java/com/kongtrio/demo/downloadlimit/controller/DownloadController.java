package com.kongtrio.demo.downloadlimit.controller;

import com.kongtrio.demo.downloadlimit.inputstream.BandwidthLimiter;
import com.kongtrio.demo.downloadlimit.inputstream.LimitInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yangjb
 * @since 2018-09-14 10:07
 * <p>
 * 文件下载器
 */
@RestController
public class DownloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    @GetMapping
    public void downloadFile(String file, HttpServletResponse response) throws IOException {
        LOGGER.info("download file");
        if (file == null) {
            file = "/tmp/test.txt";
        }
        File downloadFile = new File(file);
        FileInputStream fileInputStream = new FileInputStream(downloadFile);

        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(downloadFile.getName()
                .getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(downloadFile.length()));
        ServletOutputStream outputStream = null;
        try {
            long beginTime = System.currentTimeMillis();
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int read = fileInputStream.read(bytes, 0, 1024);
            while (read != -1) {
                outputStream.write(bytes);
                read = fileInputStream.read(bytes, 0, 1024);
            }
            LOGGER.info("download use {} ms", System.currentTimeMillis() - beginTime);
        } finally {
            fileInputStream.close();
            if (outputStream != null) {
                outputStream.close();
            }
            LOGGER.info("download success!");
        }
    }

    /**
     * 限速的下载器
     *
     * @param file
     * @param response
     * @throws IOException
     */
    @GetMapping("/limit")
    public void limitDownloadFile(String file, HttpServletResponse response) throws IOException {
        LOGGER.info("download file");
        if (file == null) {
            file = "/tmp/test.txt";
        }
        File downloadFile = new File(file);
        FileInputStream fileInputStream = new FileInputStream(downloadFile);

        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=" + new String(downloadFile.getName()
                .getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(downloadFile.length()));
        ServletOutputStream outputStream = null;
        try {
            LimitInputStream limitInputStream = new LimitInputStream(fileInputStream, new BandwidthLimiter(1024));

            long beginTime = System.currentTimeMillis();
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int read = limitInputStream.read(bytes, 0, 1024);
            while (read != -1) {
                outputStream.write(bytes);
                read = limitInputStream.read(bytes, 0, 1024);
            }
            LOGGER.info("download use {} ms", System.currentTimeMillis() - beginTime);
        } finally {
            fileInputStream.close();
            if (outputStream != null) {
                outputStream.close();
            }
            LOGGER.info("download success!");
        }
    }
}
