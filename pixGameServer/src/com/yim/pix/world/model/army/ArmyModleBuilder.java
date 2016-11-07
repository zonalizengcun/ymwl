package com.yim.pix.world.model.army;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.math.NumberUtils;

import com.yim.pix.world.model.RacistType;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ArmyModleBuilder {

	private ArmyModel model = new ArmyModel();
	
	public ArmyModleBuilder() {
		
	}
	
	public ArmyModel getArmyModel(){
		return this.model;
	}
	
	public void build() throws BiffException, IOException{
		this.initArmyTemp();
	}
	
	private void initArmyTemp() throws BiffException, IOException{
		FileInputStream inputStream = new FileInputStream("excel/army.xls");
		Workbook workbook = Workbook.getWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(0);
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			ArmyTemplate template = new ArmyTemplate();
			template.id = NumberUtils.toInt(sheet.getCell(0,i).getContents().trim());
			template.racist = RacistType.values()[NumberUtils.toInt(sheet.getCell(1,i).getContents().trim())];
			template.armyType = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim());
			template.armyName = sheet.getCell(3,i).getContents().trim();
			template.atk = NumberUtils.toInt(sheet.getCell(4,i).getContents().trim());
			template.def = NumberUtils.toInt(sheet.getCell(5,i).getContents().trim());
			template.round = NumberUtils.toInt(sheet.getCell(6,i).getContents().trim());
			template.space = NumberUtils.toInt(sheet.getCell(7,i).getContents().trim());
			template.level = NumberUtils.toInt(sheet.getCell(8,i).getContents().trim());
			template.skill = NumberUtils.toInt(sheet.getCell(9,i).getContents().trim());
			template.icon = sheet.getCell(10,i).getContents().trim();
			template.describe = sheet.getCell(11,i).getContents().trim();
			template.productLimit = NumberUtils.toInt(sheet.getCell(12,i).getContents().trim());
			this.model.addArmyTemp(template);
		}
		inputStream.close();
		workbook.close();
	}
	
}
