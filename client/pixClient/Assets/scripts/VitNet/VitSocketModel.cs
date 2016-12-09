/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-数据包结构
************************************************************************/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
//using ProtoBuf;
using UnityEngine;
   
namespace Vitality
	{
	public class VitSocketModel
	{
		public int bodyLength;//
		public int messageID;//消息类型
		public byte[] message;//消息体
		public string strData;//消息体

		public VitSocketModel()
		{ 

		}

		public VitSocketModel(int bodyLength, int messageID, string data) 
		{
		    this.bodyLength = bodyLength;
		    this.messageID = messageID;
			this.strData = data;	
		}

		public string toString() 
		{
		    return "bodyLength : " + this.bodyLength + "   msgID : " + this.messageID + "   msg : " + this.strData;
		}

		public void setBytesMessage(byte[] msg)
		{
			message = msg;
		}
	}
}//< Vitality