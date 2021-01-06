package cn.jboost.base.common.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yuxk
 * @version V1.0
 * @Title: easypoi工具类
 * @Description:
 * @date 2020/1/17 15:44
 */
public final class EasyPoiUtils {

    private static void downLoadExcel(String fileName, HttpServletRequest request, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            if (isIE(request)) {
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            ExceptionUtil.rethrowClientSideException("下载文件失败");
        }
    }

    private static <T> void defaultExport(List<T> dataList, Class<?> clz, String fileName, HttpServletRequest request, HttpServletResponse response, ExportParams exportParams) {
        dataList = dataList == null ? new ArrayList<>(1) : dataList;
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clz, dataList);
        if (workbook != null) {
            downLoadExcel(fileName, request, response, workbook);
        }
    }

    public static <T> void exportExcel(List<T> dataList, String title, Class<?> clz, boolean isCreateHeader, HttpServletRequest request, HttpServletResponse response) {
        String secondTitle = "导出时间:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss"));
        String fileName = title + ".xls";
        ExportParams exportParams = new ExportParams(title, secondTitle, title);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(dataList, clz, fileName, request, response, exportParams);
    }

    public static <T> void exportExcel(List<T> dataList, String title, Class<?> clz, HttpServletRequest request, HttpServletResponse response) {
        String secondTitle = "导出时间:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss"));
        String fileName = title + ".xls";
        defaultExport(dataList, clz, fileName, request, response, new ExportParams(title, secondTitle, title));
    }

    private static void defaultExport(List<Map<String, Object>> dataList, String fileName, HttpServletRequest request, HttpServletResponse response) {
        dataList = dataList == null ? new ArrayList<>(1) : dataList;
        Workbook workbook = ExcelExportUtil.exportExcel(dataList, ExcelType.HSSF);
        if (workbook != null) {
            downLoadExcel(fileName, request, response, workbook);
        }
    }

    public static void exportExcel(List<Map<String, Object>> dataList, String fileName, HttpServletRequest request, HttpServletResponse response) {
        defaultExport(dataList, fileName, request, response);
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        try {
            return ExcelImportUtil.importExcel(new File(filePath), clz, params);
        } catch (Exception e) {
            ExceptionUtil.rethrowClientSideException("导入文件失败");
        }
        return null;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);

        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
        } catch (Exception e) {
            ExceptionUtil.rethrowClientSideException("导入文件失败");
        }
        return null;
    }

    public static List<T> importExcel(MultipartFile file, Class<T> clz) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        try {
            return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
        } catch (Exception e) {
            ExceptionUtil.rethrowClientSideException("导入文件失败");
        }
        return null;
    }

    /**
     * 判断是否为IE浏览器
     * @param request
     * @return
     */
    private static boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 || request.getHeader("USER-AGENT").toLowerCase().indexOf("rv:11.0") > 0;
    }

}
