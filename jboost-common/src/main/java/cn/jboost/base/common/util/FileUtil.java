package cn.jboost.base.common.util;


import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class FileUtil extends cn.hutool.core.io.FileUtil {
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static final int GB = 1073741824;
    private static final int MB = 1048576;
    private static final int KB = 1024;
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public FileUtil() {
    }

    public static File toFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = "." + getExtensionName(fileName);
        File file = null;
        try {
            file = File.createTempFile(IdUtil.simpleUUID(), extension);
            multipartFile.transferTo(file);
        } catch (IOException var5) {
            log.error("fail to transfer multipartFile[{}] to file", fileName, var5);
        }
        return file;
    }

    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getFileName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length()) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getSize(int size) {
        String resultSize;
        if (size / GB >= 1) {
            resultSize = DF.format((float) size / 1.07374182E9F) + "GB   ";
        } else if (size / MB >= 1) {
            resultSize = DF.format((float) size / 1048576.0F) + "MB   ";
        } else if (size / KB >= 1) {
            resultSize = DF.format((float) size / 1024.0F) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    /**
     * 获取项目根路径
     *
     * @return
     */
    public static String getResourceBasePath() {
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }
        String pathStr = path.getAbsolutePath();
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        pathStr = pathStr.replace("\\target\\classes", "");
        return pathStr;
    }

    /**
     * 保证拷贝的文件的目录一定要存在
     *
     * @param filePath
     *            文件路径
     */
    public static void ensureDirectory(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        //将符号“\\”和“\”替换成“/”,有时候便于统一的处理路径的分隔符,避免同一个路径出现两个或三种不同的分隔符
        filePath = filePath.replace("\\", "/").replace("\\\\", "/");
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
