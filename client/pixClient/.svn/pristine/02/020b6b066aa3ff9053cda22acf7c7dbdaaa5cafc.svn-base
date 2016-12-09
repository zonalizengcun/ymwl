/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-http网络通讯
************************************************************************/
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;

using LitJson;
//using ProtoBuf;
namespace Vitality
{
	public delegate void Fn(JSONClass args);

	public class VitHttpScript : MonoBehaviour
	{
		private static VitHttpScript _instance;
		public static VitHttpScript Instance
		{
			get{
				if (!_instance) {
					GameObject obj = new GameObject();
					obj.AddComponent<VitHttpScript>();
					obj.name = "VitHttpScript";
					Object.DontDestroyOnLoad(obj);
					_instance = obj.GetComponent<VitHttpScript>();
					VitHandlerManager.Instance.registerHandler ();
				}
				return _instance;
			}
		}
		VitHttpScript(){
			addHttpHost ("Account","192.168.1.101", 10000);//"192.168.1.101"
		}
		// 服务器IP列表
		public Dictionary<string, string> httpHostAry = new Dictionary<string, string> ();

	    private const int MsgHeadSize = 8;

		public void init(){
		
		}

		// 注册服务器IP
		public void addHttpHost(string server, string host, int port)
	    {
	        string httpHost = "http://" + host + ":" + port;
			httpHostAry.Add (server, httpHost);
	    }

		public void setHttpHost(string server, string httpHost)
	    {
			httpHostAry.Add (server, httpHost);
	    }

	    // 发送普通的post请求    
		public void request(string server, string url, Dictionary<string, string> post, Fn callback )
	    {
			string httpHost = httpHostAry [server];
			StartCoroutine(POST(httpHost + url, post, callback));
	    }

	    // 发送protobuf的post请求
		public void request(string server, string url, object msg, int messageID,bool showloading)
	    {
//			Debug.Log ("url == " + url);
			string httpHost = httpHostAry [server];
			Log.DebugInfo (httpHost);
	        StartCoroutine(POST(httpHost + url, msg, messageID));
	    }

	    // POST请求 - json
	    IEnumerator POST(string url, Dictionary<string, string> post, Fn callback)
	    {
	        WWWForm form = new WWWForm();
	        foreach (KeyValuePair<string, string> post_arg in post)
	        {
	            form.AddField(post_arg.Key, post_arg.Value);
	        }

	        WWW www = new WWW(url, form);
	        yield return www;

	        if (www.error != null)
	        {
	            //POST请求失败
	            Debug.Log("error is :" + www.error);
	        }
	        else
	        {
	            //POST请求成功
	            JSONNode obj = JSON.Parse(www.text);
				JSONClass args = obj.AsObject;
				if (args == null || !args.hasKey( "querySuccess" ))
				{
					Debug.LogError("Network Error: Http Response is not Json!" + www.text);
					yield break;
				}
				int querySuccess = args["querySuccess"].AsInt;
	            if (querySuccess == 1) //successs
	            {
	                callback(args);
	            }
	            else
	            {
	                int code = System.Int32.Parse(args["code"].ToString());
	                Debug.LogError(code);
	            }
	        }
	    }

	    // POST请求 - protobuf
	    IEnumerator POST(string url, object msg, int messageID)
	    {
			Log.DebugInfo ("发送请求................... messageID == " + messageID);
	        byte[] data;
	        using (MemoryStream ms = new MemoryStream())
	        {
	            ProtoModelSerializer serializer = new ProtoModelSerializer();
	            serializer.Serialize(ms, msg);
				Log.DebugInfo ("serializer...");
	            data = new byte[ms.Length];
	            ms.Position = 0;
	            ms.Read(data, 0, data.Length);
	        }
			System.Text.UTF8Encoding converter = new System.Text.UTF8Encoding();

//			string token = "555566";
			byte[] tokeyarray = converter.GetBytes(Utils.m_instance.m_useerTokey);

			VitByteArray arr = new VitByteArray();			
			arr.WriteInt(messageID);
			arr.WriteShort (tokeyarray.Length);//token
			arr.WriteBytes (tokeyarray);

//	        arr.WriteInt(data.Length);
	        if (data != null)
	        {
	            arr.WriteBytes(data);
	        }

//			Debug.Log ("send...." + url + "  " + arr.Length + "   " + data.Length + "   tokeyarray " + tokeyarray.Length);

	        WWW www = new WWW(url, arr.Buffer);

	        yield return www;

	        if (www.error != null)
	        {
	            //POST请求失败
				Log.DebugInfo("error is :" + www.error);
	        }
	        else
	        {
	            parseMessage(www.bytes);
	        }
	    }

	    // 解析消息
	    void parseMessage(byte[] bytes)
	    {
	        //消息读取完成后开始解析 
	        MemoryStream ms = new MemoryStream(bytes, 0, bytes.Length);
	        VitByteArray arr = new VitByteArray(ms);
	        while (arr.Postion < bytes.Length)
	        {
	            int messageLength = arr.ReadInt();
				messageLength = Utils.BigtoLittle32(messageLength);
	            if ((arr.Postion + messageLength - 4 + MsgHeadSize) > bytes.Length)
	            {
	                Debug.LogError("解析消息长度不一致[ " + messageLength + " ][ " +  bytes.Length + " ]");
	                break;
	            }
	            int messageID = arr.ReadInt();
				messageID = Utils.BigtoLittle32(messageID);
	            //转换为Socket消息模型
	            VitSocketModel model = new VitSocketModel();
	            model.bodyLength = messageLength;
	            model.messageID = messageID;
	            byte[] pdata = new byte[messageLength];
				arr.ReadBytes(pdata, 0, (uint)messageLength);
				model.message = pdata;
	            // 解析proto对象
				VitProtobufDispatcher.Instance.OnMessage(model);
	        }
	    }
	}
}//< Vitality