package utils;

import java.util.UUID;

public class TokenUtil {
	
	public static String generateToken(){
		return Md5Util.string2MD5(UUID.randomUUID().toString());
	}

	public static void main(String[] args){
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
		System.out.println(Md5Util.string2MD5(uuid.toString()));
	}
	
}
