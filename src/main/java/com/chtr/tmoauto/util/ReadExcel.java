package com.chtr.tmoauto.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/*import org.apache.poi.xssf.usermodel.XSSFSheet;*/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class is used to read from excel.
 * @since: 11/02/2016
 * @author: Sangram Pisal
 */

public class ReadExcel
{

	/**
	 * This function is to read data from excel.
	 * 
	 * @param: path
	 * @since: 11/02/2016
	 * @author: Sangram Pisal
	 */

	public void fw_ReadXLSX(String path) throws FileNotFoundException, IOException
	{

		FileInputStream inputStream = new FileInputStream(new File(path));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext())
		{
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext())
			{
				Cell cell = cellIterator.next();

				switch (cell.getCellType())
				{
				case Cell.CELL_TYPE_STRING:
					System.out.println(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.print(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					System.out.print(cell.getNumericCellValue());
					break;
				}
				System.out.println(" - ");
			}
			System.out.println("");
		}

		workbook.close();
		inputStream.close();

	}

}