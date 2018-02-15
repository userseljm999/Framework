package com.chtr.tmoauto.webservices;

import java.io.File;
import java.io.FileInputStream;
/*import java.io.IOException;
import java.util.Iterator;*/

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class is used for validating xml taf value with excel cell value.
 * @since: 11/04/2016
 * @author: Sangram Pisal
 */
public class ValidateSoapResponseXLS
{

	Boolean result = false;

	/**
	 * This function is used to check value in xml tag with value in particular
	 * cell in XLS and returns true/false.
	 * 
	 * @param: actualXmlFilePath
	 * @param: tagname
	 * @param: expectedExcelFilePath
	 * @param: cellLocator
	 * @since: 11/04/2016
	 * @author: Sangram Pisal
	 */
	public Boolean fw_ValidateSOAPResponseXLS(String actualXmlFilePath, String tagname, String expectedExcelFilePath,
			int cellLocator) throws Exception
	{
		FileInputStream inputStream = new FileInputStream(new File(expectedExcelFilePath));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);

		Row row = firstSheet.getRow(1);
		Cell cell = row.getCell(cellLocator);

		String value = cell.getStringCellValue();

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(actualXmlFilePath);

		Node nodeActual = document.getElementsByTagName(tagname).item(0);

		String nodeActualValue = nodeActual.getTextContent();
		System.out.println(nodeActualValue + "," + value);

		if (nodeActualValue == value)
		{
			result = true;
		}
		workbook.close();
		return result;
	}

}
