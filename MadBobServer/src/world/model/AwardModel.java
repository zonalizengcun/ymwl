package world.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class AwardModel {

	private Map<Integer, MonthAwardTemplate> monthawards = new HashMap<>();
	
	private List<Map<Integer, WeekAwardTemplate>> weekawards = new ArrayList<>();
	
	//新手奖励
	private List<Map<Integer, WeekAwardTemplate>> firstWeekAwards = new ArrayList<>();
	
	private List<int[][]> arenaRankAwards = new ArrayList<>();
	
	public List<Integer> arenaGameRankScore = new ArrayList<>();
	
	public List<PvpRobotTemplate> pvpRobotTemplates = new ArrayList<>();
	
	public void addMonthAward(MonthAwardTemplate monthAwardTemplate){
		if (this.monthawards.containsKey(monthAwardTemplate.day)) {
			throw new IllegalArgumentException();
		}
		this.monthawards.put(monthAwardTemplate.day, monthAwardTemplate); 
	}
	
	public MonthAwardTemplate getMonthAwardTemp(int day){
		return this.monthawards.get(day);
	}
	
	
	public void addWeekAward(Map<Integer, WeekAwardTemplate> tempMap){
		this.weekawards.add(tempMap);
	}
	
	public Map<Integer, WeekAwardTemplate> getWeekAwardTemp(int index){
		return this.weekawards.get(index);
	}
	
	public void addFirstWeekTemp(Map<Integer, WeekAwardTemplate> tempMap){
		this.firstWeekAwards.add(tempMap);
	}
	
	public Map<Integer, WeekAwardTemplate> getFirstWeekTemp(int heroId){
		return this.firstWeekAwards.get(heroId);
	}
	
	public void addArenaRankAward(int[][] award){
		this.arenaRankAwards.add(award);
	}
	
	public int[][] getArenaRankAward(int rank){
		return this.arenaRankAwards.get(rank);
	}
	
	public List<int[][]> getArenaRankList(){
		return this.arenaRankAwards;
	}
	
	public void addPvpRobotTemp(PvpRobotTemplate template){
		this.pvpRobotTemplates.add(template);
	}
	
	public List<PvpRobotTemplate> getPvpRobotTemps(){
		return this.pvpRobotTemplates;
	}
	
}
