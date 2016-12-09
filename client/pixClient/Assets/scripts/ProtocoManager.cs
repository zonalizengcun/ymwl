using UnityEngine;
using System.Collections;
using Vitality;
using Message;

public class ProtocoManager : MonoBehaviour {

	public const int ERRORMSG = 0;
	public const int HEARTBEATREQ = 100;
	public const int HEARTBEATRES = 101;
	public const int LOGINREQ = 102;
	public const int LOGINRES = 103;

	public static ProtocoManager _Instance;

	// Use this for initialization
	void Start () {
		_Instance = this;
		
		VitProtobufDispatcher.Instance.RegisterMessageCallback<ErrorMessage> (onErrorMsg,ERRORMSG);
		VitProtobufDispatcher.Instance.RegisterMessageCallback<HeartBeatRes> (onHeartBeat,HEARTBEATRES);
		VitProtobufDispatcher.Instance.RegisterMessageCallback<LoginRes> (onLoginRes,LOGINRES);
	

	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public int onErrorMsg(ErrorMessage message){
		Debug.Log ("errorMessage:"+message.errorReason);
		return 1;
	}

	//心跳
	public int onHeartBeat(HeartBeatRes message){
		int serverTime = message.time;
		Debug.Log ("current serverTime :"+serverTime);
		VitNetworkScript.Instance.reciveHeartBeat (message);
		return 1;
	}
	//登陆成功
	public int onLoginRes(LoginRes message){
		Debug.Log ("login ok");
		input.m_instance.LoginSuccess (message);
		return 1;
	}

	public void sendLogintReq(string name,int racist){
		LoginReq request = new LoginReq ();
		request.name = name;
		request.racist = racist;
		VitNetworkScript.Instance.sendMessage (request, LOGINREQ);
	}
}
