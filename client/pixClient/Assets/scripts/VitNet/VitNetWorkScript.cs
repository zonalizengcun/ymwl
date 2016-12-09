/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-Tcp网络通讯
************************************************************************/
using UnityEngine;
using System;
using System.IO;
using System.Text;
using System.Threading;
using System.Collections;
using System.Collections.Generic;

using System.Net;
using System.Net.Sockets;

using System.Runtime.Remoting.Messaging;
using Message;

//using ProtoBuf;
//using VitServer;
namespace Vitality
{
	public class VitNetworkScript : MonoBehaviour, IDisposable
	{

		private static VitNetworkScript _instance;
		public static VitNetworkScript Instance
		{
			get{
				if (!_instance) {
					GameObject obj = new GameObject();
					obj.AddComponent<VitNetworkScript>();
					obj.name = "VitNetworkScript";
					UnityEngine.Object.DontDestroyOnLoad(obj);
					_instance = obj.GetComponent<VitNetworkScript>();
				}
				return _instance;
			}

		}

	    private Socket socket;
		private bool connected;

		private string m_host = "";				// 服务器IP地址
		private int m_port = -1;				// 服务器端口

	    private int timeOut = 2000;				// 连接超时时间

		private bool m_bHeartSend = false;
		private float m_heartDelta = 30f;		// Heart Beat Delta
		private float m_heartTimeOut = 2.0f;	// Heart Beat Time Out
		private float m_heartSend;
		private float m_heartRev;
		private HeartBeatReq m_heartBeatReq = null;

	    private const int ReciveBufferLength = 1024;
	    private const int MsgHeadSize = 4;

	    private byte[] readM = new byte[ReciveBufferLength];
		private byte[] Middle = null;

		private List<VitSocketModel> messages = new List<VitSocketModel>();

	    private int allCount = 0;
		private bool dispose;

		public Coroutine m_connectCallback = null;

		VitNetworkScript()
		{
			dispose = false;
			connected = false;

			m_heartSend = m_heartDelta;
			m_heartRev = 0.0f;

			m_heartBeatReq = new HeartBeatReq ();

//			VitProtobufDispatcher pDispatcher = VitProtobufDispatcher.Instance;
//			pDispatcher.RegisterMessageCallback<MsgHeartBeatRep>(parseHeartBeat, (int)ROOM_PROTOCOL_MSG_ID.ID_S2C_HEART_BEAT); 
		}

		~VitNetworkScript()
		{
			Dispose(false);
		}

		public void Dispose()
		{
			this.Dispose(true);
			GC.SuppressFinalize(this);
		}

		public void Close()
		{
			Dispose();
		}

		protected void Dispose(bool disposing)
		{
			if (dispose)
			{
				return;
			}

			if (disposing)
			{
				if (socket != null)
				{
					socket.Close();
					socket = null;

					messages.Clear();
					messages = null;
					readM = null;

					m_host = "";
					m_port = -1;
					connected = false;

					m_bHeartSend = false;
					m_heartSend = m_heartDelta;
					m_heartRev = 0.0f;
//					m_heartBeatMsg = null;

					if (m_connectCallback != null)
					{
						StopCoroutine(m_connectCallback);
						m_connectCallback = null;
					}
				}
			}

			dispose = true;
		}

		public bool Connected
		{
			get { return connected; }
		}

	    // 创建连接
		public bool connect(string ip, int port, IEnumerator callback = null)
	    {
	        try
	        {
				if (connected)
				{
					if (ip != m_host || port != m_port)
					{
						disconnect();
					}
					else
					{
						return true;
					}
				}

				m_host = ip;
				m_port = port;

	            //创建socket连接对象
	            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
	            //连接到服务器
	            //socket.Connect(host, port);
	            //服务器IP地址
	            IPAddress ipAddress = IPAddress.Parse(m_host);
	            //这是一个异步的建立连接，当连接建立成功时调用connectCallback方法
	            IAsyncResult result = socket.BeginConnect(ipAddress, port, new AsyncCallback(connectCallback), socket);

				if (callback != null)
				{
					m_connectCallback = StartCoroutine(callback);
				}

	            //超时
	            bool success = result.AsyncWaitHandle.WaitOne(timeOut, false);
				if (!success)
	            {
	                disconnect();
	            } 
	        }
	        catch (Exception e)
	        {
	            //连接失败 打印异常
	            Debug.Log("Connect error:" + e.Message);
				StopCoroutine(m_connectCallback);
				m_connectCallback = null;

	            return false;
	        }

	        return true;
	    }

	    /// 首次登录连接回调
	    private void connectCallback(IAsyncResult asyncConnect)
	    {
	        Debug.Log("socket connect success!");

	        bool connected = ((Socket)asyncConnect.AsyncState).Connected;
	        if (connected)
	        {
				this.connected = true;
	            socket.EndConnect(asyncConnect);
	            //连接后开始从服务器读取网络消息
	            socket.BeginReceive(readM, 0, ReciveBufferLength, SocketFlags.None, ReceiveCallBack, readM);
	        }
	        //else
	        //{
	        //    needExit = true;
	        //}
	    }

		private void reconnect()
		{
			Debug.Log ("reconnect");
			disconnect();
			connect(m_host, m_port);
		}

	    // 断开连接
	    public void disconnect()
	    {
	        if ( socket == null )
	        {
	            return;
	        }

	        socket.Close();
			socket = null;

			m_host = "";
			m_port = -1;
			connected = false;
			
			m_bHeartSend = false;
			m_heartSend = m_heartDelta;
			m_heartRev = 0.0f;

			if (m_connectCallback != null)
			{
				StopCoroutine(m_connectCallback);
				m_connectCallback = null;
			}
	    }

	    // 发送消息
	    public void sendMessage(object msg,int messageID)
	    {
	        byte[] data;
	        using (MemoryStream ms = new MemoryStream())
	        {
	            ProtoModelSerializer serializer = new ProtoModelSerializer();
	            serializer.Serialize(ms, msg);
	            data = new byte[ms.Length];
	            ms.Position = 0;
	            ms.Read(data, 0, data.Length);
	        }

	        VitByteArray arr = new VitByteArray();
	        arr.WriteInt(data.Length +4);
	        arr.WriteInt(messageID);

	        if (data != null)
	        {
	            arr.WriteBytes(data);
	        }

	        try
	        {
	            socket.Send(arr.Buffer);
	        }
	        catch (SocketException e)
	        {
	            Debug.Log(" " + e.ErrorCode + " " + e.Message);
	        }
	    }

	    // 接受消息
	    public void readMessage(byte[] bytes)
	    {
	        //消息读取完成后开始解析 
	        MemoryStream ms = new MemoryStream(bytes, 0, bytes.Length);
	        VitByteArray arr = new VitByteArray(ms);
	        while (arr.Postion < bytes.Length)
	        {
	            
				int messageLength = Utils.BigtoLittle32 (arr.ReadInt());

	            if ((arr.Postion + messageLength - 4 + MsgHeadSize) > bytes.Length)
	            {
	                int lastCount = arr.Length - arr.Postion + 4;
	                allCount -= lastCount;
	                Middle = new Byte[lastCount];
	                Buffer.BlockCopy(bytes, arr.Postion - 4, Middle, 0, lastCount);
	                break;
	            }
				int messageID =  Utils.BigtoLittle32( arr.ReadInt());
	            //转换为Socket消息模型
				VitSocketModel model = new VitSocketModel();
	            model.bodyLength = messageLength-4;
	            model.messageID = messageID;
	            byte[] data = new byte[messageLength-4];
	            arr.ReadBytes(data, 0, (uint)messageLength);
	            model.message = data;

				messages.Add(model);
	        }

	    }
	    //这是读取服务器消息的回调--当有消息过来的时候BgenReceive方法会回调此函数
	    private void ReceiveCallBack(IAsyncResult ar)
	    {
			if (socket == null)
			{
				return;
			}

	        int readCount = 0;
	        try
	        {
	            //读取消息长度
	            readCount = socket.EndReceive(ar);//调用这个函数来结束本次接收并返回接收到的数据长度。 
	            byte[] bytes;
	            if (Middle != null)
	            {
	                allCount += Middle.Length;
	                readCount += Middle.Length;
	                bytes = new byte[readCount];//创建长度对等的VitByteArray用于接收
	                Buffer.BlockCopy(Middle, 0, bytes, 0, Middle.Length);//拷贝读取的消息到 消息接收数组
	                Buffer.BlockCopy(readM, 0, bytes, Middle.Length, readCount - Middle.Length);//拷贝读取的消息到 消息接收数组
	                Middle = null;
	            }
	            else
	            {
	                bytes = new byte[readCount];//创建长度对等的VitByteArray用于接收
	                Buffer.BlockCopy(readM, 0, bytes, 0, readCount);//拷贝读取的消息到 消息接收数组
	            }
	            readMessage(bytes);//消息读取完成
	        }
	        catch (SocketException)//出现Socket异常就关闭连接 
	        {
	            Debug.Log("close socket");
	            socket.Close();//这个函数用来关闭客户端连接 
	            return;
	        }

	        socket.BeginReceive(readM, 0, ReciveBufferLength, SocketFlags.None, ReceiveCallBack, readM);
	    }

		void Update ()
		{
			if (connected)
			{
				float delta = Time.deltaTime;
				m_heartSend += delta;
				m_heartRev += delta;

				if (m_heartSend >= m_heartDelta)
				{
					Debug.Log ("send HeartBeat");
					m_bHeartSend = true;
					m_heartSend = 0.0f;
					m_heartRev = 0.0f;
					sendMessage(m_heartBeatReq, ProtocoManager.HEARTBEATREQ);
 			  	}

				if (m_bHeartSend && m_heartRev >= m_heartTimeOut)
				{
					reconnect();
				}
			}

			//每帧判断消息队列是否有消息
			parseMessage();
		}
			

		private bool CheckConnect ()
		{
			return true;
		}

//		private int parseHeartBeat(MsgHeartBeatRep rep)
//		{
//			m_bHeartSend = false;
//			m_heartRev = 0.0f;
//
//			return 1;
//		}

	    // 解析数据包
	    public void parseMessage()
	    {
			if (messages == null || messages.Count <= 0)
			{
				return;
			}

			//队列有消息的时候 循环读取消息进行处理 读取完当前消息后 清空消息队列 等待下帧的使用
			int count = messages.Count;
			for (int i = 0; i < count; i++)
			{
				VitSocketModel model = messages[i];
				VitProtobufDispatcher.Instance.OnMessage(model);
			}

			messages.RemoveRange (0, count);
	    }

		public void reciveHeartBeat(HeartBeatRes message){
			int serverTime = message.time;
			Utils.m_instance.serverTime = serverTime;
			m_bHeartSend = false;
			this.m_heartRev = 0.0f;
		}

	}

}//< Vitality