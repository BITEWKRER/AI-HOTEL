package AI.hotel.utils;

import AI.hotel.controller.FileController;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static AI.hotel.utils.FileUtils.getStaticPath;

@RestController
@Slf4j
public class Excel {

    public String init(Map<Integer, Map<String, Object>> data) throws UnsupportedEncodingException {
        String sheetName = DateFormat() + "酒店数据分析";
        String fileName = DateFormat() +"数据分析"+ ".xls";//new String((DateFormat() +"数据分析"+ ".xls").getBytes("gb2312"), "ISO-8859-1");


        try {
            //创建一个工作簿(Excel的文档对象)
            HSSFWorkbook hssfWorkbook = createDocumentInfo();

            //创建一个Sheet(Excel的表单)
            HSSFSheet hssfSheet = hssfWorkbook.createSheet(sheetName);

            // 设置样式
            this.excelStyleDesign(hssfSheet, hssfWorkbook);

            // 处理导出
            export(possess(hssfWorkbook, hssfSheet, data), fileName);
        } catch (Exception e) {
            return "false";
        }
        return fileName;
    }

    private HSSFWorkbook possess(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, Map<Integer, Map<String, Object>> data) {
        // 虚拟数据 generateDate();
        Map<Integer, Map<String, Object>> integerMapMap = data;

        int currency = 0;
        for (int i = 0; i < integerMapMap.size(); i++) {

            Object title = integerMapMap.get(i).get("bigTitle");
            ArrayList<String> header = (ArrayList<String>) integerMapMap.get(i).get("header");
            ArrayList<ArrayList<String>> values = (ArrayList<ArrayList<String>>) integerMapMap.get(i).get("value");
            boolean count = (boolean) integerMapMap.get(i).get("count");


            // 跨行
            hssfSheet.addMergedRegion(new CellRangeAddress(currency, currency, 0, header.size() - 1));
            // 大标题
            HSSFRow row = hssfSheet.createRow(currency);
            HSSFCell bigTitle = row.createCell(0);
            this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), bigTitle, "bigTitle");
            bigTitle.setCellValue((String) title);


            // 头部
            currency++;
            HSSFRow tmp = hssfSheet.createRow(currency);
            for (int j = 0; j < header.size(); j++) {
                HSSFCell cell = tmp.createCell(j);
                this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), cell, "header");
                cell.setCellValue(header.get(j));
            }

            // 统计求和
            if (count) {
                int field = (int) integerMapMap.get(i).get("field");
                double sum = 0.0;

                // 数据
                for (int j = 0; j < values.size(); j++) {
                    currency++;
                    HSSFRow value = hssfSheet.createRow(currency);
                    //行内元素循环
                    for (int k = 0; k < values.get(j).size(); k++) {
                        HSSFCell cell = value.createCell(k);
                        this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), cell, "value");
                        cell.setCellValue(values.get(j).get(k));
                    }
                    sum += Double.parseDouble(values.get(j).get(field));
                }
                // 添加数据
                currency++;
                HSSFRow value = hssfSheet.createRow(currency);
                HSSFCell hint = value.createCell(0);
                this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), hint, "result");
                hint.setCellValue("总计");
                HSSFCell cell = value.createCell(values.get(0).size() - 1);
                this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), cell, "result");
                cell.setCellValue(sum);
            } else {
                // 数据
                for (int j = 0; j < values.size(); j++) {
                    currency++;
                    HSSFRow value = hssfSheet.createRow(currency);
                    //行内元素循环
                    for (int k = 0; k < values.get(j).size(); k++) {
                        HSSFCell cell = value.createCell(k);
                        this.setStyle(hssfWorkbook.createFont(), hssfWorkbook.createCellStyle(), cell, "value");
                        cell.setCellValue(values.get(j).get(k));
                    }
                }
            }
            currency++;
        }

        return hssfWorkbook;
    }


    private void export(HSSFWorkbook workbook, String fileName) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String format = dateFormat.format(new Date());
        String path = getStaticPath("passengerAnalysis") + format;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(path + File.separator  + fileName);
            workbook.write(os);
        } catch (Exception e) {
            log.error("export error :" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                workbook.close();
                new FileController().update();
            } catch (Exception e) {
                log.error("export error :" + e.getMessage());
            }
        }
    }

    private void setStyle(HSSFFont hssfFont, HSSFCellStyle Style, HSSFCell hssfCell, String type) {
        switch (type) {
            case "bigTitle":
                //粗体
                hssfFont.setBold(true);
                //设置字体名称
                hssfFont.setFontName("楷体");
                //设置字体大小
                hssfFont.setFontHeightInPoints((short) 25);
                //字体颜色
                hssfFont.setColor(IndexedColors.DARK_RED.index);
                Style.setFont(hssfFont);
                //水平居中
                Style.setAlignment(HorizontalAlignment.CENTER);
                //垂直居中
                Style.setVerticalAlignment(VerticalAlignment.CENTER);
                //设置图案样式
                Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                //设置图案颜色
                Style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
                break;
            case "header":
                //粗体
                hssfFont.setBold(true);
                //设置字体名称
                hssfFont.setFontName("楷体");
                //设置字体大小
                hssfFont.setFontHeightInPoints((short) 15);
                //字体颜色
                hssfFont.setColor(IndexedColors.PALE_BLUE.index);
                Style.setFont(hssfFont);
                //水平居中
                Style.setAlignment(HorizontalAlignment.CENTER);
                //垂直居中
                Style.setVerticalAlignment(VerticalAlignment.CENTER);
                //设置图案样式
                Style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                //设置图案颜色
                Style.setFillForegroundColor(IndexedColors.AUTOMATIC.index);
                break;
            case "value":
                //粗体
                hssfFont.setBold(false);
                //设置字体名称
                hssfFont.setFontName("楷体");
                //设置字体大小
                hssfFont.setFontHeightInPoints((short) 15);
                //字体颜色
                hssfFont.setColor(IndexedColors.BLUE_GREY.index);
                Style.setFont(hssfFont);
                //水平居中
                Style.setAlignment(HorizontalAlignment.CENTER);
                //垂直居中
                Style.setVerticalAlignment(VerticalAlignment.CENTER);
                //设置图案颜色
                Style.setFillForegroundColor(IndexedColors.YELLOW.index);
                break;
            case "result":
                //粗体
                hssfFont.setBold(true);
                //设置字体名称
                hssfFont.setFontName("楷体");
                //设置字体大小
                hssfFont.setFontHeightInPoints((short) 15);
                //字体颜色
                hssfFont.setColor(IndexedColors.DARK_RED.index);
                Style.setFont(hssfFont);
                //水平居中
                Style.setAlignment(HorizontalAlignment.CENTER);
                //垂直居中
                Style.setVerticalAlignment(VerticalAlignment.CENTER);
                //设置图案颜色
                Style.setFillForegroundColor(IndexedColors.AUTOMATIC.index);
                break;
            default:
                break;
        }
        hssfCell.setCellStyle(Style);
    }

    private String DateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    /**
     * Excel文档摘要信息
     */
    private HSSFWorkbook createDocumentInfo() {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //创建文档信息
        hssfWorkbook.createInformationProperties();
        //摘要信息
        DocumentSummaryInformation information = hssfWorkbook.getDocumentSummaryInformation();
        //设置类别
        information.setCategory("客流分析报告");
        //设置文档管理者名称
        information.setManager("青城山下没头发酒店管理系统");
        //设置公司
        information.setCompany("青城山下没头发");
        SummaryInformation summaryInformation = hssfWorkbook.getSummaryInformation();
        //作者
        summaryInformation.setAuthor("青城山下没头发酒店管理系统");
        //备注
        summaryInformation.setComments(DateFormat() + "-客流分析报告");
        //主题
        summaryInformation.setSubject("酒店客流分析");
        //标题
        summaryInformation.setTitle("酒店客流分析");
        return hssfWorkbook;
    }


    /**
     * 表单样式设置
     *
     * @param hssfSheet
     * @return
     */
    private void excelStyleDesign(HSSFSheet hssfSheet, HSSFWorkbook hssfWorkbook) {
        // 设置默认宽高
        hssfSheet.setDefaultRowHeightInPoints(25);
        hssfSheet.setDefaultColumnWidth((short) 20);

        // 格子样式
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        // 字体样式
        HSSFFont hssfFont = hssfWorkbook.createFont();
        hssfFont.setBold(true);
        hssfFont.setFontName("楷体");
        hssfFont.setFontHeightInPoints((short) 15);

        hssfCellStyle.setFont(hssfFont);
        //设置水平居中
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        hssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private Map<Integer, Map<String, Object>> generateDate() {

        Map<Integer, Map<String, Object>> map = new HashMap<>();

        for (int j = 0; j < 3; j++) {
            Map<String, Object> item = new HashMap<>();
            //大标题
            item.put("bigTitle", "最高入住率:" + j);

            //字段
            ArrayList<String> strings = new ArrayList<>();
            strings.add("学号");
            strings.add("姓名");
            strings.add("班级");
            strings.add("班主任");
            strings.add("phone");
            strings.add("money");
            item.put("header", strings);

            //数据
            ArrayList<ArrayList<String>> value = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                ArrayList<String> values = new ArrayList<>();
                values.add("17310320633");
                values.add("蒋武君");
                values.add("天才一班");
                values.add("蒋武君");
                values.add("13281173089");
                values.add("1500");
                value.add(values);
            }
            item.put("value", value);

            //是否求和
            item.put("count", false);

            //求和字段,从零开始
            item.put("field", 5);

            map.put(j, item);
        }
        return map;
    }


}
