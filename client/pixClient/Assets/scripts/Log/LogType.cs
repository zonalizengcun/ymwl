using UnityEngine;
using System.Collections;

/**
 * 日志级别类型
 * 当日志类型越高时，输出的日志级别越高
 */
public enum LogLocalType  {
	DEBUG 	= 0,	// 调试模式	
	BATTLE     ,    // 输出信息模式
	INFO 	   ,	// 输出信息模式
	WARNING    ,	// 警告模式
	ERROR 	   ,	// 错误模式
	NO		    	// 关闭输出
}

/***
 * 日志的输出类型
 */
public enum LogOutLocalType{
	CONSOLE,		// 控制台输出
	STDOUT			// 文本输出
}
