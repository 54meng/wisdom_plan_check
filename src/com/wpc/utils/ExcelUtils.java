package com.wpc.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wpc.dfish.util.Utils;
import com.wpc.persistence.CiticEnt;
import com.wpc.service.ServiceLocator;

public class ExcelUtils {
	/*public static void readXlsx(String path) throws IOException {
		String ext = path.substring(path.lastIndexOf(".")+1);
		InputStream is = new FileInputStream(path);
		Workbook xssfWorkbook = new XSSFWorkbook(is);
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			Sheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				Row xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					String s = getValue(xssfRow.getCell(0));
					String s1 = getValue(xssfRow.getCell(1));
					String s2 = getValue(xssfRow.getCell(2));
					String s3 = getValue(xssfRow.getCell(3));
					String s4 = getValue(xssfRow.getCell(4));
					String s5 = getValue(xssfRow.getCell(5));
					String s6 = getValue(xssfRow.getCell(6));
					String s7 = getValue(xssfRow.getCell(7));
					String s8 = getValue(xssfRow.getCell(8));
					String s9 = getValue(xssfRow.getCell(9));
					String s10 = getValue(xssfRow.getCell(10));
					String s11 = getValue(xssfRow.getCell(11));
					System.out.println(s+" "+s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+s7+" "+s8+" "+s9+" "+s10+" "+s11);
				}
			}
		}
	}*/
	
	public static void readExcel(String path, String belongBank) throws IOException {
		String ext = path.substring(path.lastIndexOf(".")+1);
		InputStream is = new FileInputStream(path);
		Workbook workbook = null;
		if("xls".equals(ext)){
			workbook = new HSSFWorkbook(is);
		}else if("xlsx".equals(ext)){
			workbook = new XSSFWorkbook(is);
		}
		// Read the Sheet
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			Sheet sheet = workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row != null) {			
					int i = 0;
					CiticEnt citicEnt = new CiticEnt();
					citicEnt.setEntrustBatch(getValue(row.getCell(i++), null));
					//citicEnt.setCommissionOrgCode(getValue(row.getCell(i++)));
					citicEnt.setEntrustCode(getValue(row.getCell(i++), null));
					citicEnt.setApplyNum(getValue(row.getCell(i++), null));
					citicEnt.setCardNum(getValue(row.getCell(i++), null));
					citicEnt.setAccountNumber(getValue(row.getCell(i++), null));
					//citicEnt.setAccountLayerFlag(getValue(row.getCell(i++)));
					//citicEnt.setCardLayerFlag(getValue(row.getCell(i++)));
					citicEnt.setCardholderName(getValue(row.getCell(i++), null));
					citicEnt.setLastBillDate(getValue(row.getCell(i++), null));
					try {
						citicEnt.setBalance(Utils.formatDecimal(Double.parseDouble(getValue(row.getCell(i++), null)), "#0.00"));
					} catch (Exception e) {
						// TODO: handle exception
					}
					citicEnt.setCardType(getValue(row.getCell(i++), null));
					//citicEnt.setRate(getValue(row.getCell(i++)));
					//citicEnt.setBalanceRmb(getValue(row.getCell(i++)));
					citicEnt.setPrimarySecondCard(getValue(row.getCell(i++), null));
					citicEnt.setOverdueStateDesc(getValue(row.getCell(i++), null));
					citicEnt.setSex(getValue(row.getCell(i++), null));
					citicEnt.setProvince(getValue(row.getCell(i++), null));
					citicEnt.setCity(getValue(row.getCell(i++), null));
					citicEnt.setCityChange(getValue(row.getCell(i++), null));
					citicEnt.setOpenAccountDate(getValue(row.getCell(i++), null));
					citicEnt.setCertificateNum(getValue(row.getCell(i++), null));
					citicEnt.setCardholderCompanyName(getValue(row.getCell(i++), null));
					citicEnt.setCardholderCompanyAddress(getValue(row.getCell(i++), null));
					citicEnt.setCardholderJob(getValue(row.getCell(i++), null));
					citicEnt.setCardholderHomeAddress(getValue(row.getCell(i++), null));
					citicEnt.setCardholderCompanyPhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setCardholderHomePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setCardholderMobilePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setContactsName(getValue(row.getCell(i++), null));
					citicEnt.setContactsCompanyName(getValue(row.getCell(i++), null));
					citicEnt.setContactsCompanyAddress(getValue(row.getCell(i++), null));
					citicEnt.setContactsHomeAddress(getValue(row.getCell(i++), null));
					citicEnt.setContactsCompanyPhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setContactsHomePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setContactsMobilePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setRelativesName(getValue(row.getCell(i++), null));
					citicEnt.setRelationship(getValue(row.getCell(i++), null));
					citicEnt.setRelativesOfficePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setRelativesHomePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setRelativesMobilePhone(getValue(row.getCell(i++), "phone"));
					citicEnt.setCardholderPostalAddress(getValue(row.getCell(i++), null));
					citicEnt.setCardholderZipCode(getValue(row.getCell(i++), null));
					//citicEnt.setInletChannel(getValue(row.getCell(i++)));
					//citicEnt.setExtensionPersonCode(getValue(row.getCell(i++)));
					citicEnt.setLastPayDate(getValue(row.getCell(i++), null));
					citicEnt.setLastPayMoney(getValue(row.getCell(i++), null));
					citicEnt.setRemarks(getValue(row.getCell(i++), null));
					citicEnt.setLastRetailDate(getValue(row.getCell(i++), null));
					citicEnt.setLastCashDate(getValue(row.getCell(i++), null));
					citicEnt.setPrimaryCardholderCode(getValue(row.getCell(i++), null));
					citicEnt.setBillDay(getValue(row.getCell(i++), null));
					try {
						citicEnt.setPrincipalRmb(Utils.formatDecimal(Double.parseDouble(getValue(row.getCell(i++), null)), "#0.00"));
					} catch (Exception e) {
						// TODO: handle exception
					}				
					citicEnt.setCreditLimit(getValue(row.getCell(i++), null));
					citicEnt.setPoliceAddress(getValue(row.getCell(i++), null));
					citicEnt.setServicePlace(getValue(row.getCell(i++), null));
					citicEnt.setJurisdictionPolice(getValue(row.getCell(i++), null));
					citicEnt.setEmail(getValue(row.getCell(i++), null));
					citicEnt.setBelongBank(belongBank);	
					citicEnt.setOperStatus("0");
					try {
						//ServiceLocator.getCollectionService().save(citicEnt);
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
			}
		}
	}
	
	public static void readExcelByJxl(String path){
		jxl.Workbook readwb = null;
		try{
			//构建Workbook对象, 只读Workbook对象   

			//直接从本地文件创建Workbook   

			InputStream instream = new FileInputStream(path);
			readwb = jxl.Workbook.getWorkbook(instream);
			//Sheet的下标是从0开始   

			//获取第一张Sheet表   

			jxl.Sheet readsheet = readwb.getSheet(0);

			//获取Sheet表中所包含的总列数   

			int rsColumns = readsheet.getColumns();

			//获取Sheet表中所包含的总行数   

			int rsRows = readsheet.getRows();

			//获取指定单元格的对象引用   

			for (int i = 0; i < rsRows; i++){
				for (int j = 0; j < rsColumns; j++){
					jxl.Cell cell = readsheet.getCell(j, i);
					System.out.print(cell.getContents() + " ");
				}
				System.out.println();
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			readwb.close();

		}
	}
	
	@SuppressWarnings("static-access")
	private static String getValue(Cell cell, String columnName) {
		try{
			if(null == cell){
				return "";
			}
			if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
				if(DateUtil.isCellDateFormatted(cell)){
					return new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue());
				}
				if("phone".equals(columnName)){
					return Utils.formatDecimal(cell.getNumericCellValue(), "0");
				}
				return String.valueOf(cell.getNumericCellValue());
			} else {
				return String.valueOf(cell.getStringCellValue());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	/* 
     * 导出数据 
     * */  
    public static void exportExcel(HttpServletResponse response, String title, String[] rowName, List<Object[]> dataList) throws Exception{  
        try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
            HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表  
              
            // 产生表格标题行  
            HSSFRow rowm = sheet.createRow(0);  
            HSSFCell cellTiltle = rowm.createCell(0);  
              
            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
            HSSFCellStyle columnTopStyle = getColumnTopStyle(workbook);//获取列头样式对象  
            HSSFCellStyle style = getStyle(workbook);                  //单元格样式对象  
              
            /*sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, rowName.length-1));    
            cellTiltle.setCellStyle(columnTopStyle);  
            cellTiltle.setCellValue(title);  */
              
            // 定义所需列数  
            int columnNum = rowName.length;  
            HSSFRow rowRowName = sheet.createRow(0);                // 在索引2的位置创建行(最顶端的行开始的第二行)  
              
            // 将列头设置到sheet的单元格中  
            for(int n=0;n<columnNum;n++){  
                HSSFCell  cellRowName = rowRowName.createCell(n);               //创建列头对应个数的单元格  
                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型  
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);  
                cellRowName.setCellValue(text);                                 //设置列头单元格的值  
                cellRowName.setCellStyle(columnTopStyle);                       //设置列头单元格样式  
            }  
              
            //将查询出的数据设置到sheet对应的单元格中  
            for(int i=0;i<dataList.size();i++){  
                  
                Object[] obj = dataList.get(i);//遍历每个对象  
                HSSFRow row = sheet.createRow(i+1);//创建所需的行数  
                  
                for(int j=0; j<obj.length; j++){  
                    HSSFCell  cell = null;   //设置单元格的数据类型  
                    if(j == 0){  
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);  
                        cell.setCellValue(i+1);   
                    }else{  
                        cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);  
                        if(!"".equals(obj[j]) && obj[j] != null){  
                            cell.setCellValue(obj[j].toString());                       //设置单元格的值  
                        }  
                    }  
                    cell.setCellStyle(style);                                   //设置单元格样式  
                }  
            }  
            //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnNum; colNum++) {  
                int columnWidth = sheet.getColumnWidth(colNum) / 256;  
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {  
                    HSSFRow currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {  
                        currentRow = sheet.createRow(rowNum);  
                    } else {  
                        currentRow = sheet.getRow(rowNum);  
                    }  
                    if (currentRow.getCell(colNum) != null) {  
                        HSSFCell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                            int length = currentCell.getStringCellValue().getBytes().length;  
                            if (columnWidth < length) {  
                                columnWidth = length;  
                            }  
                        }  
                    }  
                }  
                if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);  
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);  
                }  
            }  
              
            if (workbook != null) {
				try {
					String fileName = new String(title.getBytes("UTF-8"),"ISO8859-1") + ".xls";
					String headStr = "attachment; filename=\"" + fileName+ "\"";
					response.setContentType("APPLICATION/OCTET-STREAM");
					response.setHeader("Content-Disposition", headStr);
					OutputStream out = response.getOutputStream();
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}  
  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }  
      
    /*
	 * 列头单元格样式
	 */      
    public static HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {  
          
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          font.setFontHeightInPoints((short)11);  
          //字体加粗  
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
            
          return style;  
            
    }  
      
    /*   
     * 列数据信息单元格样式 
     */    
    public static HSSFCellStyle getStyle(HSSFWorkbook workbook) {  
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          //font.setFontHeightInPoints((short)10);  
          //字体加粗  
          //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
           
          return style;  
      
    }
	
	public static void main(String[] args) throws Exception{
		//readExcel("W:/java/信用卡催收系统/DJJ浦发2015-11-02新导入.xls");		
	}   
}
