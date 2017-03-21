package help;

import android.content.Context;
import android.util.Log;
import android.util.Range;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import getMainThings.ValuesStatic;

/**操作EXCEL文件
 * Created by Administrator on 2016/6/27.
 */

public class EXCELOpreate{
	private static POIFSFileSystem fs;
	private static HSSFWorkbook wb;
	private static HSSFSheet sheet;
	private static HSSFRow row;

	/**
	 * 获取城市ID和描述信息（中间有#隔开）
	 * @param district 城区名称
	 * @param context 输入流
	 * @return
	 */
	public static String getCityID_Descrip(String district,Context context)
	{
		String reS = "";
		//获取表头的数据
		String[] title = new String[0];
		try {
			title = readExcelTitle(context.getAssets().open(ValuesStatic.CityImageXLSFilePath));
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		for(int i = 0; i<title.length;i++)
		{
			if(title[i].equals("Area"))
			{
				try {
					fs = new POIFSFileSystem(context.getAssets().open(ValuesStatic.CityImageXLSFilePath));
					wb = new HSSFWorkbook(fs);
					sheet = wb.getSheetAt(0);
					// 得到总行数
					int rowNum = sheet.getLastRowNum();
					for(int j = 1;j<rowNum;j++) {
						// 正文内容应该从第二行开始,第一行为表头的标题
						row = sheet.getRow(j);
						if( row.getCell(i).toString().indexOf(district) != - 1 ) {
							//找到了，返回tID和城市介绍和气候背景
							HSSFRow tempRow = sheet.getRow(0);
							reS = tempRow.getCell(0).toString() + "=" + row.getCell(0).toString() + "#";
							reS += tempRow.getCell(i).toString() + "=" + row.getCell(i).toString() + "#";
							reS += tempRow.getCell(7).toString() + "=" + row.getCell(7).toString() + "#";
							reS += tempRow.getCell(8).toString() + "=";
							//Log.i("获取excle数据中：",row.getLastCellNum()+"");
							if(row.getLastCellNum()> 8) {
								reS += row.getCell(8).toString();
								}
								return reS;
						}
					}

				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		return reS;
	}

	/**
	 * 读取Excel表格表头的内容
	 * @param is
	 * @return String 表头内容的数组
	 */
	public static String[] readExcelTitle(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			//title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * @param is 输入流
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, String> readExcelContent(InputStream is) {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
				j++;
			}
			content.put(i, str);
			str = "";
		}
		return content;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 *
	 * @param cell Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				strCell = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				strCell = String.valueOf(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				strCell = "";
				break;
			default:
				strCell = "";
				break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}
	/**
	 * 根据HSSFCell类型设置数据
	 * @param cell
	 * @return
	 */
	private static String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
				// 如果当前Cell的Type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC:
				case HSSFCell.CELL_TYPE_FORMULA: {
					// 判断当前的cell是否为Date
					if ( HSSFDateUtil.isCellDateFormatted(cell)) {
						// 如果是Date类型则，转化为Data格式

						//方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
						//cellvalue = cell.getDateCellValue().toLocaleString();

						//方法2：这样子的data格式是不带带时分秒的：2011-10-12
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						cellvalue = sdf.format(date);

					}
					// 如果是纯数字
					else {
						// 取得当前Cell的数值
						cellvalue = String.valueOf(cell.getNumericCellValue());
					}
					break;
				}
				// 如果当前Cell的Type为STRIN
				case HSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串
					cellvalue = cell.getRichStringCellValue().getString();
					break;
				// 默认的Cell值
				default:
					cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

}
