/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-protobuf数据包处理(模板)
************************************************************************/
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace Vitality
{
	class VitProtobufCallback
	{
	    public virtual int OnMessage(object message)
	    {
	        return 1;
	    }
	};

	class VitProtobufCallbackT<T> : VitProtobufCallback
	{
	    public delegate int MessageCallback(T message);

	    public VitProtobufCallbackT(MessageCallback callback)
	    {
	        callback_ = callback;
	    }

	    public override int OnMessage(object message)
	    {
	        T m = (T)(message);
	        return callback_(m);
	    }

	    private MessageCallback callback_;
	};

	class VitProtobufDispatcher
	{
	    private Dictionary<int, VitProtobufCallback> CallbackMap = new Dictionary<int, VitProtobufCallback>();
	    private Dictionary<int, Type> ProtocolIdMap = new Dictionary<int, Type>();
		private static VitProtobufDispatcher m_instance;

	    //获取连接对象
		public static VitProtobufDispatcher Instance
	    {
			get
			{
		        if (m_instance == null)
		        {
		            //第一次调用的时候 创建单例对象 并进行初始化操作
		            m_instance = new VitProtobufDispatcher();
		        }

		        return m_instance;
			}
	    }

	    // 处理协议
	    public int OnMessage(VitSocketModel module)
	    {
	        int protocol = module.messageID;
	        if (!ProtocolIdMap.ContainsKey(protocol))
	        {
	            Debug.Log("OnMessage instance Error [" + protocol + "]");
	            return 0;
	        }
	        if (!CallbackMap.ContainsKey(protocol))
	        {
	            Debug.Log("OnMessage callback Error [" + protocol + "]");
	            return 0;
	        }
	        
	        // 通过反射 创建probuf对象
	        Type insType = ProtocolIdMap[protocol];
	        object insObj = Activator.CreateInstance(insType);
	        // 解析协议内容
	        ProtoModelSerializer serializer = new ProtoModelSerializer();
	        System.IO.MemoryStream memStream = new System.IO.MemoryStream(module.message);
	        insObj = serializer.Deserialize(memStream, insObj, insType);

	        // 调用协议处理回调函数
	        return CallbackMap[protocol].OnMessage(insObj);
	    }

	    // 注册协议
	    public void RegisterMessageCallback<M>(VitProtobufCallbackT<M>.MessageCallback callback, int protocol)
	    {
			//已经注册过了
			if (!CallbackMap.ContainsKey (protocol)) {
				VitProtobufCallbackT<M> obj = new VitProtobufCallbackT<M> (callback);
				CallbackMap.Add (protocol, obj);
				Type insType = typeof(M);
				ProtocolIdMap.Add (protocol, insType);
			}
	    }
	};
}//< Vitality