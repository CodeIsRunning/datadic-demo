package com.lxf.datadic.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class WordUtils extends WordConstans {

    // 表结构信息
    @SuppressWarnings("serial")
    public static LinkedHashMap<String, String> tableRelation = new
            LinkedHashMap<String, String>() {
                {
                    this.put("COLUMN_NAME", "字段名称");
                    this.put("TYPE_NAME", "字段类型");
                    this.put("COLUMN_DEF", "默认值");
                    this.put("COLUMN_SIZE", "字段长度");
                    this.put("REMARKS", "备注信息");
                }
            };


	private static XWPFDocument document = new XWPFDocument();

	public void writeTableToWord(List<SqlTable> tableList) throws Exception {
		for (SqlTable mysqlTable : tableList) {
			if (TITLE_ADD_INDEX) {
				int index = tableList.indexOf(mysqlTable) + 1;
				mysqlTable.setTitle(index + ". " + mysqlTable.getTitle());
			}
			createSimpleTableNormal(mysqlTable);
		}
		saveDocument(EXPORT_FILE_PATH);
		log.info("文件写入成功.");
		log.info("成功将文件保存在>>>" + EXPORT_FILE_PATH);
	}

	// 往word插入一张表格
	private void createSimpleTableNormal(SqlTable mysqlTable)
			throws Exception {
		// 添加一个文档
		addNewPage(BreakType.TEXT_WRAPPING);

		// 设置标题
		setTableTitle(mysqlTable.getTitle());
		// 创建表格
		XWPFTable table = createTable(mysqlTable.getRows(),mysqlTable.getCols());
		// 设置表格中行列内容
		setRowText(table, mysqlTable);
		// 往表格中插入第一列标题内容
		setFirstRowText(table);
	}

	// 创建表格
	private XWPFTable createTable(int rowNum, int celNum) {
		XWPFTable table = document.createTable(rowNum, celNum);
		CTTbl ttbl = table.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl
				.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr
				.addNewTblW();
		CTJc cTJc = tblPr.addNewJc();
		cTJc.setVal(STJc.Enum.forString("center"));
		tblWidth.setW(new BigInteger(TABLE_WIDTH));
		tblWidth.setType(STTblWidth.DXA);
		return table;
	}

	// 设置表格标题
	private void setTableTitle(String tableTitle) {
		XWPFParagraph p2 = document.createParagraph();

		addCustomHeadingStyle(document, "标题 2", 2);

		p2.setAlignment(TITLE_ALIGNMENT);
		XWPFRun r2 = p2.createRun();
		p2.setStyle("标题 2");
		// r2.setTextPosition(5);
		r2.setText(tableTitle);
		if (TITLE_FONT_BOLD) {
			r2.setBold(TITLE_FONT_BOLD);
		}
		r2.setFontFamily(TITLE_FONT_FAMILY);
		r2.setFontSize(TITLE_FONT_SIZE);
		if (IS_RETURN_ROW) {
			r2.addCarriageReturn();// 是否换行
		}
	}

	// 设置表格第一行内容
	private void setFirstRowText(XWPFTable table) {
		XWPFTableRow firstRow = null;
		XWPFTableCell firstCell = null;
		firstRow = table.insertNewTableRow(0);
		firstRow.setHeight(FIRST_ROW_HEIGHT);
		// 表关系列
		for (String fieldValue : tableRelation.values()) {
			firstCell = firstRow.addNewTableCell();
			createVSpanCell(firstCell, fieldValue, FIRST_ROW_COLOR,
					FIRST_ROW_CEL_WIDTH, STMerge.RESTART);
		}
	}

	// 设置每行的内容
	private void setRowText(XWPFTable table, SqlTable mysqlTable) {
		XWPFTableRow firstRow = null;
		XWPFTableCell firstCell = null;

		List<String[]> fieldList = mysqlTable.getFieldList();
		String[] fieldValues = null;
		for (int i = 0, fieldListSize = fieldList.size(); i < fieldListSize; i++) {
			firstRow = table.getRow(i);
			firstRow.setHeight(ROW_HEIGHT);
			fieldValues = fieldList.get(i);
			for (int j = 0, fieldValuesSize = fieldValues.length; j < fieldValuesSize; j++) {
				firstCell = firstRow.getCell(j);
				setCellText(firstCell, fieldValues[j], ROW_COLOR, ROW_CEL_WIDTH);
			}
		}

	}

	// 添加单个列的内容
	private void setCellText(XWPFTableCell cell, String value, String bgcolor,
                             int width) {
		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		// 设置单元格颜色
		if (IS_ROW_COLOR)
		{
			cell.setColor(bgcolor);
		}


		XWPFParagraph p = getXWPFParagraph(ROW_ALIGNMENT, ROW_FONT_BOLD,
				ROW_FONT_FAMILY, ROW_FONT_SIZE, ROW_FONT_COLOR, value);
		cell.setParagraph(p);
	}

	/**
	 * 往第一行插入一列
	 * @param cell
	 * @param value
	 * @param bgcolor
	 * @param width
	 * @param stMerge
	 */
	private void createVSpanCell(XWPFTableCell cell, String value,
                                 String bgcolor, int width, STMerge.Enum stMerge) {
		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		// 设置颜色
		if (IS_FIRST_ROW_COLOR){
			cell.setColor(bgcolor);
		}
		XWPFParagraph p = getXWPFParagraph(FIRST_ROW_ALIGNMENT,
				FIRST_ROW_FONT_BOLD, FIRST_ROW_FONT_FAMILY,
				FIRST_ROW_FONT_SIZE, FIRST_ROW_FONT_COLOR, value);
		cell.setParagraph(p);
	}

	private XWPFParagraph getXWPFParagraph(ParagraphAlignment alignment,
                                           boolean isBold, String fontFamily, int fontSize, String fontColor,
                                           String celValue) {
		XWPFDocument doc = new XWPFDocument();
		XWPFParagraph p = doc.createParagraph();
		XWPFRun r2 = p.createRun();
		p.setAlignment(alignment);
		if (isBold){
			r2.setBold(isBold);}
		r2.setFontFamily(fontFamily);
		r2.setFontSize(fontSize);
		r2.setText(celValue);
		r2.setColor(fontColor);
		return p;
	}

	// 添加新的一个文档
	private void addNewPage(BreakType breakType) {
		XWPFParagraph xp = document.createParagraph();
		xp.createRun().addBreak(breakType);
	}

	// 输出文件
	public void saveDocument(String savePath) throws Exception {
		FileOutputStream fos = new FileOutputStream(savePath);
		document.write(fos);
		fos.close();
	}

	/**
	 * 增加自定义标题样式。这里用的是stackoverflow的源码
	 * 
	 * @param docxDocument
	 *            目标文档
	 * @param strStyleId
	 *            样式名称
	 * @param headingLevel
	 *            样式级别
	 */
	private static void addCustomHeadingStyle(XWPFDocument docxDocument,
			String strStyleId, int headingLevel) {

		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);

		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);

		CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
		indentNumber.setVal(BigInteger.valueOf(headingLevel));

		// lower number > style is more prominent in the formats bar
		ctStyle.setUiPriority(indentNumber);

		CTOnOff onoffnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onoffnull);

		// style shows up in the formats bar
		ctStyle.setQFormat(onoffnull);

		// style defines a heading of the given level
		CTPPr ppr = CTPPr.Factory.newInstance();
		ppr.setOutlineLvl(indentNumber);
		ctStyle.setPPr(ppr);

		XWPFStyle style = new XWPFStyle(ctStyle);

		// is a null op if already defined
		XWPFStyles styles = docxDocument.createStyles();

		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);

	}

}
