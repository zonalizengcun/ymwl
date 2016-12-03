package com.yim.net.protocol;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.MessageLite;

public abstract class OpCodeMapper {
	
	public static final int clientDisConnect = -1;
	
	protected Map<Integer,Class<? extends MessageLite>> codeToClazz = new HashMap<Integer, Class<? extends MessageLite>>();
	
//	protected Map<Class<? extends MessageLite>, Integer> clazzToCode = new HashMap<Class<? extends MessageLite>, Integer>();
	
	public OpCodeMapper() {
		this.initProtocol();
	}
	
	public abstract void initProtocol();

	public Class<? extends MessageLite> getClass(int opcode){
		return this.codeToClazz.get(opcode);
	}
	
	
}
