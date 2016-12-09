using UnityEngine;
using System.Collections;

public class Log : MonoBehaviour {
	
	public static LogLocalType logType = LogLocalType.NO;

	public static LogOutLocalType logOutType = LogOutLocalType.CONSOLE;

	public static void DebugInfo(object message){
		if(logType != LogLocalType.NO){
			Debug.Log(message);	 
		}
	}


//	public static void DebugInfo(object message) {
//		if(logType > LogLocalType.DEBUG)
//			return;
//		if(logOutType == LogOutLocalType.CONSOLE){
//			Debug.Log(message);	 
//		}
//	}
	
	public static void Warning(object message) {
		if(logType > LogLocalType.WARNING)
			return;   
		if(logOutType == LogOutLocalType.CONSOLE){
			Debug.LogWarning(message);	
		}
	}
	
	public static void Info(object message) {
		if(logType > LogLocalType.INFO)
			return;
		if(logOutType == LogOutLocalType.CONSOLE){
			Debug.Log(message);	
		}
	}
	
	public static void Error(object message) {
		if(logType > LogLocalType.ERROR)
			return;
		if(logOutType == LogOutLocalType.CONSOLE){
			Debug.LogError(message);	
		}
	}

	public static void BattleLog(object message) {
		return;
		if(logOutType == LogOutLocalType.CONSOLE){
			Debug.LogWarning(message);	
		}
	}
}
