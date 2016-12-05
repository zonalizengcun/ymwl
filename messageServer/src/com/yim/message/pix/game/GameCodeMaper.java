package com.yim.message.pix.game;

import java.sql.SQLNonTransientConnectionException;

import com.yim.message.pix.game.MessagePtoto.BattleMatchReq;
import com.yim.message.pix.game.MessagePtoto.BattleMatchRes;
import com.yim.message.pix.game.MessagePtoto.ErrorMessage;
import com.yim.message.pix.game.MessagePtoto.FormationReq;
import com.yim.message.pix.game.MessagePtoto.FormationRes;
import com.yim.message.pix.game.MessagePtoto.FormationSaveReq;
import com.yim.message.pix.game.MessagePtoto.FormationSaveRes;
import com.yim.message.pix.game.MessagePtoto.HeartBeatReq;
import com.yim.message.pix.game.MessagePtoto.HeartBeatRes;
import com.yim.message.pix.game.MessagePtoto.LoginReq;
import com.yim.message.pix.game.MessagePtoto.LoginRes;
import com.yim.message.pix.game.MessagePtoto.StartBattleReq;
import com.yim.message.pix.game.MessagePtoto.StartBattleRes;
import com.yim.net.protocol.OpCodeMapper;

public class GameCodeMaper extends OpCodeMapper{
	
	public static final int Error = 0;
	
	public static final int HEARTBEATREQ = 100;
	public static final int HEARTBEATRES = 101;
	
	public static final int LOGINREQ = 102;
	public static final int LOGINRES = 103;
	
	public static final int FORMATIONREQ = 104;
	public static final int FORMATIONRES = 105;
	
	public static final int FORMATIONSAVEREQ = 106;
	public static final int FORMATIONSAVERES = 107;
	
	public static final int BATTLEMATCHREQ = 108;
	public static final int BATTLEMATCHRES = 109;
	public static final int BATTLEMATCHSYN = 110;
	
	public static final int STARTBATTLEREQ = 111;
	public static final int STARTBATTLERES = 112;
	
	
	
	public GameCodeMaper() {
		super();
	}

	@Override
	public void initProtocol() {
		this.codeToClazz.put(Error, ErrorMessage.class);
		this.codeToClazz.put(HEARTBEATREQ, HeartBeatReq.class);
		this.codeToClazz.put(HEARTBEATRES, HeartBeatRes.class);
		this.codeToClazz.put(LOGINREQ, LoginReq.class);
		this.codeToClazz.put(LOGINRES, LoginRes.class);
		this.codeToClazz.put(FORMATIONREQ, FormationReq.class);
		this.codeToClazz.put(FORMATIONRES, FormationRes.class);
		this.codeToClazz.put(FORMATIONSAVEREQ, FormationSaveReq.class);
		this.codeToClazz.put(FORMATIONSAVERES, FormationSaveRes.class);
		this.codeToClazz.put(BATTLEMATCHREQ,BattleMatchReq.class);
		this.codeToClazz.put(BATTLEMATCHRES, BattleMatchRes.class);
		this.codeToClazz.put(BATTLEMATCHSYN, BattleMatchRes.class);
		this.codeToClazz.put(STARTBATTLEREQ, StartBattleReq.class);
		this.codeToClazz.put(STARTBATTLERES, StartBattleRes.class);
	}

}
