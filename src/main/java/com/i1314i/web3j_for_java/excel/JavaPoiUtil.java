package com.i1314i.web3j_for_java.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel文件的工具类
 * @author 平行时空
 * @created 2018-03-23 15:16
 *
 *     Excel导出用法：
 *      list 数据 <map>第一行  map.put("sheetName", "名字");
 *      mapKeys list中map的key数组集合 String keys[] = {"uid","username", "password"}
 *      colNames excel的列名  String colNames[]={"uid", "账号", "密码"} 与keys长度对应
 *      int type 2003 .xls  2007 .xlsx 不填写默认2003 .xls
 *     Excel getExcelName(String filename,int type) 用法
 *          获取中文excel名字
 *          type==2003 /2007  2003 .xls 2007 .xlsx   type可不填写默认设置为2003
 *       Excel导入用法： List<String[]> getExcel  和 List<List<Object>> getExcelList 用法类似于导入
 *
 *       新版接口用法：JavaPoiUtil.createWorkBook(Studentlogin.class,studentlogins, keys, columnNames, 2003).write(outputStream);
 */


public class JavaPoiUtil {


    public static void  setDownType(HttpServletResponse response,String name,Integer excelType) throws UnsupportedEncodingException {
        response.reset();
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", "attachment;filename=" + getExcelName(name, excelType));
    }


    //Excel导出(单Sheet)
    public static  <T> Workbook createWorkBookWithSheet(List<List<Map<String,Object>>>allDataList, String[] mapKeys, String[]colNames, int type,List<String>sheetNames){

        //创建WorkBook
        Workbook workbook=null;
        if(type==2003){
            workbook=new HSSFWorkbook(); //2003版本 .xls
        }else if(type==2007){
            workbook= new XSSFWorkbook();//2007版本 .xlsx
        }else {
            workbook=new HSSFWorkbook();
        }

        for (int index=0;index<sheetNames.size();index++){
            //创建sheet
            Sheet sheet=workbook.createSheet(sheetNames.get(index));
            //设置宽度
            for (int i=0;i<mapKeys.length;i++){
                sheet.setColumnWidth(i,(int)(37.5*100));
            }
            //第一行表头
            Row row=sheet.createRow(0);
            // 创建两种单元格格式
            CellStyle cs = workbook.createCellStyle();
            CellStyle cs2 = workbook.createCellStyle();

            // 创建两种字体
            Font f = workbook.createFont();
            Font f2 = workbook.createFont();

            // 创建第一种字体样式（用于列名）
            f.setFontHeightInPoints((short)10);
            f.setColor(IndexedColors.BLACK.getIndex());
            //设置列名
            for(int i=0;i<colNames.length;i++){
                Cell cell = row.createCell(i);
                cell.setCellValue(colNames[i]);
                cell.setCellStyle(cs);
            }

            //设置每行每列的值
            for (int i = 1; i < allDataList.get(index).size(); i++) {
                // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
                // 创建一行，在页sheet上
                Row row1 = sheet.createRow(i);
                // 在row行上创建一个方格
                for(int j=0;j<mapKeys.length;j++){
                    Cell cell = row1.createCell(j);
                    cell.setCellValue(allDataList.get(index).get(i).get(mapKeys[j]) == null?" ": allDataList.get(index).get(i).get(mapKeys[j]).toString());
                    cell.setCellStyle(cs2);
                }
            }
        }

        return workbook;
    }

    //Excel导出(单Sheet)
    public static  <T> Workbook createWorkBook(List<Map<String,Object>>list, String[] mapKeys, String[]colNames, int type){

        //创建WorkBook
        Workbook workbook=null;
        if(type==2003){
            workbook=new HSSFWorkbook(); //2003版本 .xls
        }else if(type==2007){
            workbook= new XSSFWorkbook();//2007版本 .xlsx
        }else {
            workbook=new HSSFWorkbook();
        }

        //创建sheet
        Sheet sheet=workbook.createSheet(list.get(0).get("sheetName").toString());
        //设置宽度
        for (int i=0;i<mapKeys.length;i++){
            sheet.setColumnWidth(i,(int)(37.5*100));
        }
        //第一行表头
        Row row=sheet.createRow(0);
        // 创建两种单元格格式
        CellStyle cs = workbook.createCellStyle();
        CellStyle cs2 = workbook.createCellStyle();

        // 创建两种字体
        Font f = workbook.createFont();
        Font f2 = workbook.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short)10);
        f.setColor(IndexedColors.BLACK.getIndex());
//        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
//
//        // 创建第二种字体样式（用于值）
//        f2.setFontHeightInPoints((short)10);
//        f2.setColor(IndexedColors.BLACK.getIndex());
//
//        // 设置第一种单元格的样式（用于列名）
//        cs.setFont(f);
//        cs.setBorderLeft(CellStyle.BORDER_THIN);
//        cs.setBorderRight(CellStyle.BORDER_THIN);
//        cs.setBorderTop(CellStyle.BORDER_THIN);
//        cs.setBorderBottom(CellStyle.BORDER_THIN);
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//
//        // 设置第二种单元格的样式（用于值）
//        cs2.setFont(f2);
//        cs2.setBorderLeft(CellStyle.BORDER_THIN);
//        cs2.setBorderRight(CellStyle.BORDER_THIN);
//        cs2.setBorderTop(CellStyle.BORDER_THIN);
//        cs2.setBorderBottom(CellStyle.BORDER_THIN);
//        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<colNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(colNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            // 在row行上创建一个方格
            for(int j=0;j<mapKeys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(mapKeys[j]) == null?" ": list.get(i).get(mapKeys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return workbook;
    }

    //Excel导出(单Sheet)
    public static  <T> Workbook createWorkBook(List<List<Object>>list,String[]colNames,String sheetName, int type)
    {

        //创建WorkBook
        Workbook workbook=null;
        if(type==2003){
            workbook=new HSSFWorkbook(); //2003版本 .xls
        }else if(type==2007){
            workbook= new XSSFWorkbook();//2007版本 .xlsx
        }else {
            workbook=new HSSFWorkbook();
        }

        //创建sheet
        Sheet sheet=workbook.createSheet(sheetName);
        //设置宽度
        for (int i=0;i<list.get(0).size();i++){
            sheet.setColumnWidth(i,(int)(37.5*100));
        }
        //第一行表头
        Row row=sheet.createRow(0);
        // 创建两种单元格格式
        CellStyle cs = workbook.createCellStyle();
        CellStyle cs2 = workbook.createCellStyle();

        // 创建两种字体
        Font f = workbook.createFont();
        Font f2 = workbook.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short)10);
        f.setColor(IndexedColors.BLACK.getIndex());
//        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
//
//        // 创建第二种字体样式（用于值）
//        f2.setFontHeightInPoints((short)10);
//        f2.setColor(IndexedColors.BLACK.getIndex());
//
//        // 设置第一种单元格的样式（用于列名）
//        cs.setFont(f);
//        cs.setBorderLeft(CellStyle.BORDER_THIN);
//        cs.setBorderRight(CellStyle.BORDER_THIN);
//        cs.setBorderTop(CellStyle.BORDER_THIN);
//        cs.setBorderBottom(CellStyle.BORDER_THIN);
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//
//        // 设置第二种单元格的样式（用于值）
//        cs2.setFont(f2);
//        cs2.setBorderLeft(CellStyle.BORDER_THIN);
//        cs2.setBorderRight(CellStyle.BORDER_THIN);
//        cs2.setBorderTop(CellStyle.BORDER_THIN);
//        cs2.setBorderBottom(CellStyle.BORDER_THIN);
//        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<colNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(colNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            // 在row行上创建一个方格
            for(int j=0;j<list.get(0).size();j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(j) == null?" ": list.get(i).get(j).toString());
                cell.setCellStyle(cs2);
            }
        }
        return workbook;
    }

    public static  <T> Workbook createWorkBooks(List<Map<String,Object>>list, String[] mapKeys, String[]colNames, int type){

        //创建WorkBook
        Workbook workbook=null;
        if(type==2003){
            workbook=new HSSFWorkbook(); //2003版本 .xls
        }else if(type==2007){
            workbook= new XSSFWorkbook();//2007版本 .xlsx
        }else {
            workbook=new HSSFWorkbook();
        }

        //创建sheet
        Sheet sheet=workbook.createSheet("sheet1");
        //设置宽度
        for (int i=0;i<mapKeys.length;i++){
            sheet.setColumnWidth(i,(int)(37.5*100));
        }
        //第一行表头
        Row row=sheet.createRow(0);
        // 创建两种单元格格式
        CellStyle cs = workbook.createCellStyle();
        CellStyle cs2 = workbook.createCellStyle();

        // 创建两种字体
        Font f = workbook.createFont();
        Font f2 = workbook.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short)10);
        f.setColor(IndexedColors.BLACK.getIndex());
//        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
//
//        // 创建第二种字体样式（用于值）
//        f2.setFontHeightInPoints((short)10);
//        f2.setColor(IndexedColors.BLACK.getIndex());
//
//        // 设置第一种单元格的样式（用于列名）
//        cs.setFont(f);
//        cs.setBorderLeft(CellStyle.BORDER_THIN);
//        cs.setBorderRight(CellStyle.BORDER_THIN);
//        cs.setBorderTop(CellStyle.BORDER_THIN);
//        cs.setBorderBottom(CellStyle.BORDER_THIN);
//        cs.setAlignment(CellStyle.ALIGN_CENTER);
//
//        // 设置第二种单元格的样式（用于值）
//        cs2.setFont(f2);
//        cs2.setBorderLeft(CellStyle.BORDER_THIN);
//        cs2.setBorderRight(CellStyle.BORDER_THIN);
//        cs2.setBorderTop(CellStyle.BORDER_THIN);
//        cs2.setBorderBottom(CellStyle.BORDER_THIN);
//        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<colNames.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(colNames[i]);
            cell.setCellStyle(cs);
        }
        //设置每行每列的值
        for (int i = 1; i < list.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow(i);
            // 在row行上创建一个方格
            for(int j=0;j<mapKeys.length;j++){
                Cell cell = row1.createCell(j);
                cell.setCellValue(list.get(i).get(mapKeys[j]) == null?" ": list.get(i).get(mapKeys[j]).toString());
                cell.setCellStyle(cs2);
            }
        }
        return workbook;
    }

    public static  <T> Workbook createWorkBook(List<Map<String,Object>>list, int type){
        String[] headers=null;
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);

            headers = new String[map.size()];
            int i = 0;
            for (String key : map.keySet()) {
                headers[i++] = key;
            }
        }
        return createWorkBooks(list,headers,headers,type);
    }

    //无type 默认返回2003格式
    public static <T> Workbook createWorkBook(List<Map<String,Object>>list, String[] mapKeys, String[]colNames) throws Exception {
        return JavaPoiUtil.createWorkBook(list,mapKeys,colNames,2003);
    }


    public static  <T> Workbook createWorkBooks(Class<T> clazz, List<T> datalist, String[] mapKeys, String[]colNames, int type) throws Exception {
        List<Map<String,Object>>list=transfterBeanToListMapList(clazz,datalist,mapKeys);
        return JavaPoiUtil.createWorkBook(list,mapKeys,colNames,type);
    }


    public static  <T> Workbook createWorkBooksWithSheet(Class<T> clazz, List<List<T>>datalists, String[] mapKeys, String[]colNames, int type,List<String>sheetNames) throws Exception {
        List<List<Map<String,Object>>>allDataList=new ArrayList<>();

        for (int i=0;i<datalists.size();i++){
            List<Map<String,Object>>list=transfterBeanToListMapList(clazz,datalists.get(i),mapKeys);
            allDataList.add(list);
        }

        return JavaPoiUtil.createWorkBookWithSheet(allDataList,mapKeys,colNames,type,sheetNames);
    }



    //无type 默认返回2003格式 反射
    public static <T> Workbook createWorkBooks(Class<T> clazz, List<T> datalist, String[] mapKeys, String[]colNames) throws Exception {
        return JavaPoiUtil.createWorkBooks(clazz,datalist,mapKeys,colNames,2003);
    }


    //无type 默认返回2003格式 反射 不传入clazz
    public static <T> Workbook createWorkBooks(List<T> datalist, String[] mapKeys, String[]colNames) throws Exception {
        T data=null;
        Class clazz=data.getClass();
        return JavaPoiUtil.createWorkBooks(clazz,datalist,mapKeys,colNames,2003);
    }

    //无type 默认返回2003格式 反射 不传入clazz
    public static <T> Workbook createWorkBooks(List<T> datalist, String[]colNames) throws Exception {
        return JavaPoiUtil.createWorkBooks(datalist,colNames,2003);
    }

    /**
     * 利用反射导出List<T>数据
     * @param datalist
     * @param colNames
     * @param type
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Workbook createWorkBooks(List<T> datalist, String[]colNames, int type) throws Exception {
        T data=datalist.get(0);
        Class clazz=data.getClass();

        Field[] fields = clazz.getDeclaredFields();//取得所有类成员变量
        String[]mapKeys=new String[fields.length];
        //取消每个属性的安全检查
        for(Field f:fields){
            f.setAccessible(true);
        }

        for(int i=0;i<fields.length;i++){
            Field field=fields[i];
            mapKeys[i]=field.getName();
        }

        return JavaPoiUtil.createWorkBooks(clazz,datalist,mapKeys,colNames,type);
    }



    /**
     * 通过反射转换转list<map>
     * @param clazz
     * @param values
     * @param names
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<Map<String, Object>> transfterBeanToListMapList(Class<T> clazz, List<T> values, String[] names) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> value = null;
        value=new HashMap<>();
        value.put("sheetName", "sheet1");
        result.add(value);
        Object bean = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();//取得所有类成员变量
        //取消每个属性的安全检查
        for(Field f:fields){
            f.setAccessible(true);
        }
        //传入的每个对象的所需要的类成员属性值
        for (int j = 0; j < values.size(); j++) {
            value=new HashMap<>();
            for (int i=0;i<names.length;i++){
                value.put(names[i],getValue(fields,names[i],values.get(j)));
            }
            result.add(value);
        }
        return result;
    }


    /***
     * 根据对象名获取反射变量值
     * @param fields
     * @param name
     * @param value
     * @return
     */
    protected static Object getValue(Field[] fields,String name,Object value){
        for (int i = 0; i < fields.length; i++) {
            try {

                if (name.equalsIgnoreCase(fields[i].getName())){
//                    System.out.println(fields[i].getName() + ":"+ fields[i].get(value));

                    return fields[i].get(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "空";
    }


    //设置获取Excel名称
    public static String getExcelName(String filename,int type) throws UnsupportedEncodingException {
        if(type==2007){
            return new String((filename+".xlsx").getBytes(),"iso-8859-1");
        }else {
            return new String((filename+".xls").getBytes(),"iso-8859-1");
        }

    }

    //无type默认返回 .xls
    public static String getExcelName(String filename) throws UnsupportedEncodingException {
        return JavaPoiUtil.getExcelName(filename,2003);
    }





    //读取xls xlsx 2003/2007  sheetType==1 需要输入要获取的sheet名  FileType==1需要输入文件  ==0 输入文件路径
    public static List<String[]> getExcel(String filePath,File file,String sheetName,int sheetType,int FileType) throws Exception {
        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = filePath.toLowerCase().endsWith("xls") ? true : false;
        System.out.println("2003版本");
        Workbook workbook=JavaPoiUtil.getWorkbook(filePath,file,FileType);
        // 在Excel文档中，第一张工作表的缺省索引是0
        // 其语句为：
        // HSSFSheet sheet = wookbook.getSheetAt(0);
        Sheet sheet=null;
        if(sheetType==0){
            sheet= workbook.getSheet(sheetName);
        }else{
            sheet = workbook.getSheetAt(0);
        }

        // 获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();
        System.out.println("rows =" + rows);
        // 遍历行
        List<String[]> list_excel = new ArrayList<String[]>();
        for (int i = 1; i <= rows; i++) {
            // 读取左上端单元格
            Row row = sheet.getRow(i);
            // 行不为空
            if (row != null) {
                // 获取到Excel文件中的所有的列
                int cells = row.getPhysicalNumberOfCells();
                System.out.println("cells =" + cells);
                String value = "";
                // 遍历列
                for (int j = 0; j <= cells; j++) {
                    // 获取到列的值
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_FORMULA:
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                value += cell.getNumericCellValue() + ",";
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value += cell.getStringCellValue() + ",";
                                break;
                            default:
                                value += "0";
                                break;
                        }
                    }
                }
                String[] val = value.split(",");
                list_excel.add(val);
            }
        }
        return list_excel;
    }


    //读取xls xlsx 2003/2007
    public static List<String[]> getExcel(String filePath) throws Exception {
        return getExcel(filePath,null,"",0,0);
    }

    //读取xls xlsx 2003/2007
    public static List<String[]> getExcel(String filePath,String sheetName) throws Exception {
        return getExcel(filePath,null,sheetName,1,0);
    }

    //读取xls xlsx 2003/2007
    public static List<String[]> getExcel(File file,String sheetName) throws Exception {
      return getExcel(null,file,sheetName,1,1);
    }

    //读取xls xlsx 2003/2007  默认读取第一个sheet
    public static List<String[]> getExcel(File file) throws Exception {
        return getExcel(null,file,null,0,1);
    }




    //读取xls xlsx 2003/2007 返回listObject
    public static List<List<Object>> getExcelList(File file,String sheetName,int sheetType)
            throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        String fileName=file.getName();
        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = fileName.toLowerCase().endsWith("xls") ? true : false;
        System.out.println(isExcel2003);
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } else {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        }

        Sheet sheet=null;
        // 读取第一章表格内容
        if(sheetType==1){
            sheet = workbook.getSheet(sheetName);
        }else{
            sheet = workbook.getSheetAt(0);
        }

        Object value = null;
        Row row = null;
        Cell cell = null;
        int counter = 0;
        int LastCellNum=0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            if(i==0){
                //跳过第一行
                row = sheet.getRow(i);
                LastCellNum= row.getLastCellNum();
                continue;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <LastCellNum; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    value=null;//导入不能为空
                    linked.add(value);
                    //System.out.println(value);
                    continue;
                }
                //System.out.println(value);
                DecimalFormat df = new DecimalFormat("0.00");// 格式化 number String
                // 字符
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");// 格式化日期字符串
                DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        //System.out.println(i + "行" + j + " 列 is String type");
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        //  System.out.println(i + "行" + j
                        //  + " 列 is Number type ; DateFormt:"
                        //  + cell.getCellStyle().getDataFormatString());
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle()
                                .getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                    .getNumericCellValue()));
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_BLANK://空格，空白
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                if (value == null || "".equals(value)) {
                    value=null;//导入不能为空
                }
                //System.out.println(value);
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }


    //读取xls xlsx 2003/2007 返回listObject
    public static Map<String,List<List<Object>>> getExcelListMap(InputStream inputStream,String fileName,String sheetName,int sheetType)
            throws IOException {
        Map<String,List<List<Object>>>dataMap=new HashMap<>();
        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = fileName.toLowerCase().endsWith("xls") ? true : false;
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook(inputStream);
        }

        int sheetsNum=workbook.getNumberOfSheets();

        for (int sheetIndex=0;sheetIndex<sheetsNum;sheetIndex++) {
            List<List<Object>> list = new LinkedList<List<Object>>();
            Sheet sheet=null;
            // 读取第一章表格内容
            sheet = workbook.getSheetAt(sheetIndex);
            String selsheetName=sheet.getSheetName();
            Object value = null;
            Row row = null;
            Cell cell = null;
            int counter = 0;
            int LastCellNum=0;
            for (int i = sheet.getFirstRowNum(); counter < sheet
                    .getPhysicalNumberOfRows(); i++) {
                if(i==0){
                    //跳过第一行
                    row = sheet.getRow(i);
                    LastCellNum= row.getLastCellNum();
                    continue;
                }
                row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                List<Object> linked = new LinkedList<Object>();
                for (int j = 0; j <LastCellNum; j++) {
                    cell = row.getCell(j);
                    if (cell == null) {
                        value=null;//导入不能为空
                        linked.add(value);
                        //System.out.println(value);
                        continue;
                    }
                    value = getCellValueByCell(workbook,cell);

                    if (value == null || "".equals(value)) {
                        value=null;//导入不能为空
                    }
                    //System.out.println(value);
                    linked.add(value);
                }
                list.add(linked);
            }
            dataMap.put(selsheetName,list);
        }
        return dataMap;
    }

    //读取xls xlsx 2003/2007 返回listObject
    public static List<List<Object>> getExcelList(InputStream inputStream,String fileName,String sheetName,int sheetType)
            throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();

        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = fileName.toLowerCase().endsWith("xls") ? true : false;
        System.out.println(isExcel2003);
        Workbook workbook = null;
        if (isExcel2003) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            workbook = new XSSFWorkbook(inputStream);
        }

        Sheet sheet=null;
        // 读取第一章表格内容
        if(sheetType==1){
            sheet = workbook.getSheet(sheetName);
        }else{
            sheet = workbook.getSheetAt(0);
        }

        Object value = null;
        Row row = null;
        Cell cell = null;
        int counter = 0;
        int LastCellNum=0;
        for (int i = sheet.getFirstRowNum(); counter < sheet
                .getPhysicalNumberOfRows(); i++) {
            if(i==0){
                //跳过第一行
                row = sheet.getRow(i);
                LastCellNum= row.getLastCellNum();
                continue;
            }
            row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = row.getFirstCellNum(); j <LastCellNum; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    value=null;//导入不能为空
                    linked.add(value);
                    //System.out.println(value);
                    continue;
                }



                value = getCellValueByCell(workbook,cell);

                /**
                 *
                 *
                 *
                 //System.out.println(value);
                 DecimalFormat df = new DecimalFormat("0.00");// 格式化 number String
                 // 字符
                 SimpleDateFormat sdf = new SimpleDateFormat(
                 "yyyy-MM-dd");// 格式化日期字符串
                 DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        //System.out.println(i + "行" + j + " 列 is String type");
                        value = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        //  System.out.println(i + "行" + j
                        //  + " 列 is Number type ; DateFormt:"
                        //  + cell.getCellStyle().getDataFormatString());
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());
                        } else if ("General".equals(cell.getCellStyle()
                                .getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                    .getNumericCellValue()));
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case Cell.CELL_TYPE_BLANK://空格，空白
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }

                 **/
                if (value == null || "".equals(value)) {
                    value=null;//导入不能为空
                }
                //System.out.println(value);
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }


    //获取单元格各类型值，返回字符串类型
    private static String getCellValueByCell(Workbook wk,Cell cell) {
        FormulaEvaluator evaluator=wk.getCreationHelper().createFormulaEvaluator();
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType=cell.getCellType();
        if(cellType==Cell.CELL_TYPE_FORMULA){ //表达式类型
            cellType=evaluator.evaluate(cell).getCellType();
        }

        switch (cellType) {
            case Cell.CELL_TYPE_STRING: //字符串类型
                cellValue= cell.getStringCellValue().trim();
                cellValue= StringUtils.isEmpty(cellValue) ? "" : cellValue;
                break;
            case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC: //数值类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

                    cellValue = format.format(cell.getDateCellValue());
                } else {  //否
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            default: //其它类型，取空串吧
                cellValue = "";
                break;
        }
        return cellValue;
    }






    /**
     * 导入未写完
     * @param clazz
     * @param file
     * @param sheetName
     * @param sheetType
     * @param <T>
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T>List<T>getExcelList(Class<T> clazz,File file,String sheetName,int sheetType) throws IOException, IllegalAccessException, InstantiationException {

        List<List<Object>>objects=JavaPoiUtil.getExcelList(file,sheetName,sheetType);

        List<T>list=new ArrayList<>();
        for(int i=0;i<objects.size();i++){
            List object=objects.get(i);
            Field[] fields = clazz.getDeclaredFields();//取得所有类成员变量
            //取消每个属性的安全检查
            for(Field f:fields){
                f.setAccessible(true);
            }


//            System.out.println(fields.);
            T classto=clazz.newInstance();
            for(int j=0;j<fields.length;j++){
                Field field=fields[j];
                System.out.println(object.get(i));
//                String[]objects1= (String[]) object.get(i);
//                field.set(classto,field.get(objects1[i]));
            }
            list.add(classto);
//            for(int j=0;j<object.size();j++){
//                System.out.println(object.get(j));
//            }

        }

        return list;
    }

    //输入file 跟sheetName 默认读取出输入的sheet
    public  static List<List<Object>> getExcelList(File file,String sheetName) throws IOException {
        return getExcelList(file,sheetName,1);
    }

    //只输入file 默认读取出第一个sheet
    public  static List<List<Object>> getExcelList(File file) throws Exception {
        return getExcelList(file,null,0);
    }

    //只输入file 默认读取出第一个sheet
    public  static List<List<Object>> getExcelList(InputStream inputStream,String filenametype) throws Exception {
        return getExcelList(inputStream,filenametype,null,0);
    }

    public  static Map<String,List<List<Object>>> getExcelListMap(InputStream inputStream,String filenametype) throws Exception {
        return getExcelListMap(inputStream,filenametype,null,0);
    }

    public static List<Map<String,Object>> getExcels(String filePath,File file,String sheetName,int sheetType,int FileType) throws IOException {
        //返回结果集
        List<Map<String,Object>> valueList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            Workbook workbook=JavaPoiUtil.getWorkbook(filePath,file,FileType);
            // 在Excel文档中，第一张工作表的缺省索引是0
            // 其语句为：
            // HSSFSheet sheet = wookbook.getSheetAt(0);
            Sheet sheet = null;
            if (sheetType == 0) {
                sheet = workbook.getSheet(sheetName);
            } else {
                sheet = workbook.getSheetAt(0);
            }
            // 获取到Excel文件中的所有行数
            int rows = sheet.getPhysicalNumberOfRows();
            System.out.println("rows =" + rows);
            // 遍历行
            List<String[]> list_excel = new ArrayList<String[]>();
            Map<String, Object> maps=null;
            for (int i = 0; i <= rows; i++) {
                // 读取左上端单元格
                Row row = sheet.getRow(i);
                // 行不为空
                if (row != null) {
                    maps=new HashMap<>();
                    // 获取到Excel文件中的所有的列
                    int cells = row.getPhysicalNumberOfCells();
                    System.out.println("cells =" + cells);
                    String value = "";
                    // 遍历列
                    for (int j = 0; j <= cells; j++) {
                        // 获取到列的值
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_FORMULA:
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    value += cell.getNumericCellValue() + ",";
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    value += cell.getStringCellValue() + ",";
                                    break;
                                default:
                                    value += "0";
                                    break;
                            }
                        }
                    }
                    String[] val = value.split(",");
                    for(int p=0;p<val.length;p++){
                        maps.put(String.valueOf(p),val[p]);
                    }
                }

                valueList.add(maps);
            }
        }catch (Exception e){
            throw  new  RuntimeException(e);
        }
        return valueList;
    }


    private static Workbook getWorkbook(String filePath, File file, int FileType) throws IOException {

        // 创建对Excel工作簿文件的引用
        boolean isExcel2003 = filePath.toLowerCase().endsWith("xls") ? true : false;
        System.out.println("2003版本");
        Workbook workbook = null;
        if (isExcel2003) {
            if (FileType == 1) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new HSSFWorkbook(new FileInputStream(new File(filePath)));
            }
        } else {

            if (FileType == 1) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } else {
                workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            }
        }
        return workbook;
    }



}