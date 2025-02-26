package cn.newness.imip.framework.excel.core.util;

import cn.newness.imip.framework.excel.core.handler.MyCellWriteHandler;
import cn.newness.imip.framework.excel.core.handler.SelectSheetWriteHandler;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 *
 * @author 新奇源码
 */
public class ExcelUtils {

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
//                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new SelectSheetWriteHandler(head)) // 基于固定 sheet 实现下拉框
                .registerConverter(new LongStringConverter()) // 避免 Long 类型丢失精度
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }

    /**
     * 按模板导出excel表格
     *
     * @param response  响应
     * @param filename  文件名
     * @param head      Excel head 头
     * @param dataMap      数据
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void writeByModel(HttpServletResponse response,
                                        String filename,
                                        Class<T> head,
                                        Map<String,Object> dataMap,
                                        List<T> dataList,
                                        String templateFileName,
                                        List<Integer> mergeColumn,
                                        List<Integer> allBroderBlankColumn) throws IOException {
        // 输出 Excel
        WriteSheet writeSheet = EasyExcel.writerSheet().build();//创建excel
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();//开启插入新行时新创建一行，而不是直接套用下一行
        EasyExcel.write(response.getOutputStream(), head)
                .withTemplate(
                        new ClassPathResource(templateFileName).getInputStream()
                ).registerWriteHandler(ExcelUtils.getHorizontalCellStyleStrategy((short) 12))
                .registerWriteHandler(new MyCellWriteHandler(mergeColumn,allBroderBlankColumn))
                .build().fill(dataList,fillConfig,writeSheet).fill(dataMap,writeSheet).finish();
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }
    /**
    * 单元格样式设定
    *
    * @param [fontHeightInPoints]
    * @author machuran
    * @date 8/29/2024
    * @Return com.alibaba.excel.write.style.HorizontalCellStyleStrategy
    */
    public static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy(Short fontHeightInPoints) {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.NONE);
        // 配置字体
        WriteFont contentWriteFont = new WriteFont();
        // 字体
        contentWriteFont.setFontName("宋体");
        // 字体大小
        contentWriteFont.setFontHeightInPoints(fontHeightInPoints);
        // 设置加粗
        contentWriteFont.setBold(false);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 【水平居中需要使用以下两行】
        // 设置文字左右居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置文字上下居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置 自动换行
        contentWriteCellStyle.setWrapped(true);
        // 样式策略
        return new HorizontalCellStyleStrategy(null, contentWriteCellStyle);
    }
}
