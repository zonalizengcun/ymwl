package world.handler;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.Language;

import message.OpCodeEnum;
import message.protos.MessagePtoto.DayAward;
import message.protos.MessagePtoto.MonthRegistListRes;
import message.protos.MessagePtoto.MonthRegistRes;
import message.protos.MessagePtoto.WeekRegistListReq;
import message.protos.MessagePtoto.WeekRegistListRes;
import message.protos.MessagePtoto.WeekRegistRes;
import net.handler.PacketHandler;
import net.session.Packet;
import net.session.UserEntity;
import utils.DateUtil;
import world.World;
import world.dao.DaoManager;
import world.entity.MonthAwardEntity;
import world.entity.WeekAwardEntity;
import world.model.AwardModel;
import world.model.MonthAwardTemplate;
import world.model.WeekAwardTemplate;
import world.service.PlayerService;
import world.service.ServiceCache;

public class AwardHandler implements PacketHandler{

	@Override
	public void handle(Packet packet) {
		int opcode = packet.getOpcode();
		if (opcode == OpCodeEnum.monthRegistListReq.getOpcode()) {
			this.getMonthRegistList(packet);
		}else if(opcode == OpCodeEnum.monthRegistReq.getOpcode()){
			this.monthRegist(packet);
		}else if(opcode == OpCodeEnum.weekRegistListReq.getOpcode()){
			this.getWeekRgistList(packet);
		}else if(opcode == OpCodeEnum.weekRegistReq.getOpcode()){
			this.weekRegist(packet);
		}
	}

	@Override
	public int[] getOpcodes() {
		return new int[]{OpCodeEnum.monthRegistListReq.getOpcode(),OpCodeEnum.monthRegistReq.getOpcode(),
				OpCodeEnum.weekRegistListReq.getOpcode(),OpCodeEnum.weekRegistReq.getOpcode()};
	}
	
	
	public void getWeekRgistList(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		WeekRegistListReq request = (WeekRegistListReq)packet.getData();
		List<Integer> heroIdList = request.getHeroIdList();
		WeekAwardEntity awardEntity = DaoManager.getDefault().getWeekAward(userEntity.getId());
		AwardModel awardModel = World.getDefault().getAwardModel();
		Map<Integer, WeekAwardTemplate> templateMap = null;
		if (awardEntity.isFirst()&& awardEntity.getAwardHeroId() == -1) {
			if (!heroIdList.contains(0)) {
				templateMap = awardModel.getFirstWeekTemp(0);
				awardEntity.setAwardHeroId(0);
			}else if (!heroIdList.contains(1)) {
				templateMap = awardModel.getFirstWeekTemp(1);
				awardEntity.setAwardHeroId(1);
			}else if (!heroIdList.contains(2)) {
				templateMap = awardModel.getFirstWeekTemp(1);
				awardEntity.setAwardHeroId(2);
			}
			DaoManager.getDefault().saveWeekAward(awardEntity);
		}
		if (awardEntity.isFirst() && awardEntity.getAwardHeroId()!=-1) {
			templateMap = awardModel.getFirstWeekTemp(awardEntity.getAwardHeroId());
		}
		packet.setOpcode(OpCodeEnum.weekRegistListRes.getOpcode());
		WeekRegistListRes.Builder response = WeekRegistListRes.newBuilder();
		boolean todayGetAward = true;//今天是否已经领取
		int currentDay = awardEntity.getAwardTimes()-1;
		if (!DateUtil.isSameDay(awardEntity.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			todayGetAward = false;
			currentDay = awardEntity.getAwardTimes();
		}
		for(int i=0;i<7;i++){
			WeekAwardTemplate template = null;
			if (templateMap != null) {
				template = templateMap.get(i);
			}else{
				int index = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)%4;
				templateMap = awardModel.getWeekAwardTemp(index);
				template = templateMap.get(currentDay);
			}
			DayAward.Builder builder = DayAward.newBuilder();
			builder.setDay(i);
			builder.setAwardType(template.awardType);
			builder.setCount(template.count);
			int state = 0;
			if (i < currentDay) {
				state = 2;
			}else if (i == currentDay){
				if (todayGetAward) {
					state = 2;
				}else {
					state = 3;
				}
			}else if (i >currentDay) {
				state = 1;
			}
			builder.setState(state);
			response.addDayAwards(builder.build());
		}
		packet.setOpcode(OpCodeEnum.weekRegistListRes.getOpcode());
		packet.writeData(response.build());
	}
	
	/**
	 * 周签到
	 * @param packet
	 */
	public void weekRegist(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		
		WeekAwardEntity awardEntity = DaoManager.getDefault().getWeekAward(userEntity.getId());
		if (DateUtil.isSameDay(awardEntity.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			packet.sendError(Language.DAY_AWARD_REPEATED);
			return;
		}
		int currentDay = awardEntity.getAwardTimes();//应该领取第几天奖励
		AwardModel awardModel = World.getDefault().getAwardModel();
		Map<Integer, WeekAwardTemplate> templateMap = null;
		WeekAwardTemplate template = null;
		if (awardEntity.isFirst()&&awardEntity.getAwardHeroId()!=-1) {
			templateMap = awardModel.getFirstWeekTemp(awardEntity.getAwardHeroId());
			template = templateMap.get(currentDay);
		}else{
			int index = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)%4;
			templateMap = awardModel.getWeekAwardTemp(index);
			template = templateMap.get(currentDay);
		}
		awardEntity.setLastAwardTime(DateUtil.getSecondStamp());
		awardEntity.setAwardTimes(awardEntity.getAwardTimes()+1);
		if (awardEntity.getAwardTimes() >= 7 ) {
			awardEntity.setFirst(false);
			awardEntity.setAwardTimes(0);
		}
		DaoManager.getDefault().saveWeekAward(awardEntity);
		WeekRegistRes.Builder response = WeekRegistRes.newBuilder();
		response.setDay(awardEntity.getAwardTimes()-1);
		response.setAwardType(template.awardType);
		response.setCount(template.count);
		packet.setOpcode(OpCodeEnum.weekRegistRes.getOpcode());
		packet.writeData(response.build());
	}
	
	
	
	/**
	 * 月签到奖励列表
	 * @param packet
	 */
	public void getMonthRegistList(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		MonthAwardEntity awardEntity = DaoManager.getDefault().getMonthAward(userEntity.getId());
		if (!DateUtil.isSameMonth(awardEntity.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			awardEntity.getAwardDays().clear();
			awardEntity.setLastAwardTime(DateUtil.getSecondStamp());
			DaoManager.getDefault().saveMonthAward(awardEntity);
		}
		AwardModel awardModel = World.getDefault().getAwardModel();
		Calendar calendar = Calendar.getInstance();
		int monthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//当前月实际天数
		int today = calendar.get(Calendar.DAY_OF_MONTH) - 1;
		packet.setOpcode(OpCodeEnum.monthRegistListRes.getOpcode());
		MonthRegistListRes.Builder response = MonthRegistListRes.newBuilder();
		response.setToday(today);
		for(int i=0;i<monthDays;i++){
			MonthAwardTemplate template = awardModel.getMonthAwardTemp(i);
			DayAward.Builder builder = DayAward.newBuilder();
			builder.setDay(i);
			builder.setAwardType(template.award[0]);
			builder.setCount(template.award[1]);
			int state = 0;
			if (i < today) {
				if (awardEntity.getAwardDays().contains(i)) {
					state = 2;
				}else{
					state = 4;
				}
			}else if (i == today) {
				if (awardEntity.getAwardDays().contains(i)) {
					state = 2;
				}else {
					state = 3;
				}
			}else if (i > today) {
				state = 1;
			}
			builder.setState(state);
			response.addDayAwards(builder.build());
		}
		packet.writeData(response.build());
	}
	
	/**
	 * 月签到
	 * @param packet
	 */
	public void monthRegist(Packet packet){
		PlayerService playerService = ServiceCache.getDefault().getPlayerService();
		UserEntity userEntity = playerService.getUserEntity(packet.getToken());
		MonthAwardEntity awardEntity = DaoManager.getDefault().getMonthAward(userEntity.getId());
		if (!DateUtil.isSameMonth(awardEntity.getLastAwardTime()*1000L, System.currentTimeMillis())) {
			awardEntity.getAwardDays().clear();
			awardEntity.setLastAwardTime(DateUtil.getSecondStamp());
			DaoManager.getDefault().saveMonthAward(awardEntity);
		}
		AwardModel awardModel = World.getDefault().getAwardModel();
		Calendar calendar = Calendar.getInstance();
		int today = calendar.get(Calendar.DAY_OF_MONTH) - 1;
		if (awardEntity.getAwardDays().contains(today)) {
			packet.sendError(Language.DAY_AWARD_REPEATED);
			return;
		}
		MonthAwardTemplate awardTemplate = awardModel.getMonthAwardTemp(today);
		awardEntity.getAwardDays().add(today);
		awardEntity.setLastAwardTime(DateUtil.getSecondStamp());
		DaoManager.getDefault().saveMonthAward(awardEntity);
		MonthRegistRes.Builder response = MonthRegistRes.newBuilder();
		response.setDay(today);
		response.setAwardType(awardTemplate.award[0]);
		response.setCount(awardTemplate.award[1]);
		packet.setOpcode(OpCodeEnum.monthRegistRes.getOpcode());
		packet.writeData(response.build());
	}

}
