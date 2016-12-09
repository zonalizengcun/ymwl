using UnityEngine;
using System.Collections;
using Vitality;
using Message;

public class input : MonoBehaviour {

	// Use this for initialization

	public static input m_instance;

	public Transform layer_ui;

	void Start () {
		m_instance = this;
		initGameCommonBtn (layer_ui);

	}
	
	// Update is called once per frame
	void Update () {
	
	}


	/// <summary>
	/// /////// ui button//////////////////
	/// </summary>
	void initGameCommonBtn(Transform parent){
		//true 包括隐藏按键
		tk2dButton[] button = parent.GetComponentsInChildren<tk2dButton> (true);
		for (int i=0; i<button.Length; i++) {
			button[i].ButtonPressedEvent += event_btnUi;
		}
	}

	//游戏中界面通用ＵＩ按钮事件
	void event_btnUi(tk2dButton source){
		switch (source.name) {
		case "btn_login":
			VitNetworkScript.Instance.connect("127.0.0.1",10001,sendLogin());

			Utils.m_instance.LoadLevelAsync ("game");
			break;
		}
	}

	IEnumerator sendLogin ()
	{  
		string name = layer_ui.transform.FindChild ("inputName").GetComponent<tk2dUITextInput> ().Text;
		string racist = layer_ui.transform.FindChild ("inputRacist").GetComponent<tk2dUITextInput> ().Text;
		Debug.Log ("name:" + name +",   racist:" + racist);
		yield return new WaitForSeconds(0);
		ProtocoManager._Instance.sendLogintReq (name,int.Parse(racist));
	}

	public void LoginSuccess(LoginRes message){
		string name = message.name;
		int playerId = message.playerId;
		Utils.m_instance.playerId = playerId;
		Utils.m_instance.name = name;
	}

}
