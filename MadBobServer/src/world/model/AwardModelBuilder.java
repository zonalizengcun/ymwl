package world.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class AwardModelBuilder {
	private AwardModel model = new AwardModel();
	
	public void build() throws BiffException, IOException{
		this.initAwards();
	}
	
	public AwardModel getAwardModel(){
		return this.model;
	}
	
	private void initAwards() throws BiffException, IOException{
		FileInputStream inputStream = new FileInputStream("excel/registAward.xls");
		Workbook workbook = Workbook.getWorkbook(inputStream);
		Sheet sheet = workbook.getSheet(0);
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			MonthAwardTemplate template = new MonthAwardTemplate();
			template.day = NumberUtils.toInt(sheet.getCell(0,i).getContents().trim());
			String awardContent = sheet.getCell(1,i).getContents().trim();
			template.award = new int[]{NumberUtils.toInt(awardContent.split(",")[0]),NumberUtils.toInt(awardContent.split(",")[1])};
			this.model.addMonthAward(template);
		}
		sheet = workbook.getSheet(1);
		Map<Integer, WeekAwardTemplate> templateMap1 = new HashMap<>();
		Map<Integer, WeekAwardTemplate> templateMap2 = new HashMap<>();
		Map<Integer, WeekAwardTemplate> templateMap3 = new HashMap<>();
		Map<Integer, WeekAwardTemplate> templateMap4 = new HashMap<>();
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			WeekAwardTemplate template1 = new WeekAwardTemplate();
			WeekAwardTemplate template2 = new WeekAwardTemplate();
			WeekAwardTemplate template3 = new WeekAwardTemplate();
			WeekAwardTemplate template4 = new WeekAwardTemplate();
			int day = NumberUtils.toInt(sheet.getCell(0,i).getContents().trim());
			template1.day = day;
			template2.day = day;
			template3.day = day;
			template4.day = day;
			template1.awardType = NumberUtils.toInt(sheet.getCell(1,i).getContents().trim().split(",")[0]);
			template1.count = NumberUtils.toInt(sheet.getCell(1,i).getContents().trim().split(",")[1]);
			template2.awardType = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim().split(",")[0]);
			template2.count = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim().split(",")[1]);
			template3.awardType = NumberUtils.toInt(sheet.getCell(3,i).getContents().trim().split(",")[0]);
			template3.count = NumberUtils.toInt(sheet.getCell(3,i).getContents().trim().split(",")[1]);
			template4.awardType = NumberUtils.toInt(sheet.getCell(4,i).getContents().trim().split(",")[0]);
			template4.count = NumberUtils.toInt(sheet.getCell(4,i).getContents().trim().split(",")[1]);
			templateMap1.put(template1.day, template1);
			templateMap2.put(template2.day, template2);
			templateMap3.put(template3.day, template3);
			templateMap4.put(template4.day, template4);
		}
		this.model.addWeekAward(templateMap1);
		this.model.addWeekAward(templateMap2);
		this.model.addWeekAward(templateMap3);
		this.model.addWeekAward(templateMap4);
		
		sheet = workbook.getSheet(2);
		Map<Integer, WeekAwardTemplate> tempMap1 = new HashMap<>();
		Map<Integer, WeekAwardTemplate> tempMap2 = new HashMap<>();
		Map<Integer, WeekAwardTemplate> tempMap3 = new HashMap<>();
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			WeekAwardTemplate template1 = new WeekAwardTemplate();
			WeekAwardTemplate template2 = new WeekAwardTemplate();
			WeekAwardTemplate template3 = new WeekAwardTemplate();
			int day = NumberUtils.toInt(sheet.getCell(0,i).getContents().trim());
			template1.day = day;
			template2.day = day;
			template3.day = day;
			template1.awardType = NumberUtils.toInt(sheet.getCell(1,i).getContents().trim().split(",")[0]);
			template1.count = NumberUtils.toInt(sheet.getCell(1,i).getContents().trim().split(",")[1]);
			template2.awardType = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim().split(",")[0]);
			template2.count = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim().split(",")[1]);
			template3.awardType = NumberUtils.toInt(sheet.getCell(3,i).getContents().trim().split(",")[0]);
			template3.count = NumberUtils.toInt(sheet.getCell(3,i).getContents().trim().split(",")[1]);
			tempMap1.put(template1.day, template1);
			tempMap2.put(template2.day, template2);
			tempMap3.put(template3.day, template3);
		}
		
		this.model.addFirstWeekTemp(tempMap1);
		this.model.addFirstWeekTemp(tempMap2);
		this.model.addFirstWeekTemp(tempMap3);
		
		
		//竞技场排行榜奖励
		sheet = workbook.getSheet(3);
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			String content = sheet.getCell(1,i).getContents().trim();
			String[] awardInfos = content.split(";");
			int[][] awards = new int[awardInfos.length][];
			for(int j=0;j<awardInfos.length;j++){
				awards[j] = new int[]{NumberUtils.toInt(awardInfos[j].split(",")[0]),NumberUtils.toInt(awardInfos[j].split(",")[1])};
			}
			int gameScore = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim());
			this.model.arenaGameRankScore.add(gameScore);
			this.model.addArenaRankAward(awards);		
		}
		
		sheet = workbook.getSheet(4);
		
		for(int i=1;i<sheet.getRows();i++){
			if(sheet.getCell(0, i).getContents().trim().equals("")){
				break;
			}
			PvpRobotTemplate template = new PvpRobotTemplate();
			template.mapId = NumberUtils.toInt(sheet.getCell(0,i).getContents().trim());
			template.bossId = NumberUtils.toInt(sheet.getCell(1,i).getContents().trim());
			template.money = NumberUtils.toInt(sheet.getCell(2,i).getContents().trim());
			String[] monsterInfos = sheet.getCell(3,i).getContents().trim().split(";");
			template.monsters = new ArrayList<>(monsterInfos.length);
			for(String monsterInfo:monsterInfos){
				template.monsters.add(new int[]{NumberUtils.toInt(monsterInfo.split(",")[0]),NumberUtils.toInt(monsterInfo.split(",")[1])});
			}
			String[] adorInfos = sheet.getCell(4,i).getContents().trim().split(";");
			template.adornments = new ArrayList<>(adorInfos.length);
			for(String adorInfo:adorInfos){
				template.adornments.add(new int[]{NumberUtils.toInt(adorInfo.split(",")[0]),NumberUtils.toInt(adorInfo.split(",")[1])});
			}
			template.name = sheet.getCell(5,i).getContents().trim();
			template.headId = NumberUtils.toInt(sheet.getCell(6,i).getContents().trim());
			this.model.addPvpRobotTemp(template);
		}
		
		
		inputStream.close();
		workbook.close();
	}
}
