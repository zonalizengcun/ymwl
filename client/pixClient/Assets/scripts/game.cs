using UnityEngine;
using System.Collections;
using DG.Tweening;
public class game : MonoBehaviour {

	// Use this for initialization
	public static game m_instance;

	/// <summary>
	/// 游戏状态
	/// </summary>
	public int m_state = 0;

	/// <summary>
	/// UI button
	/// </summary>
	public Transform layer_ui;
	/// <summary>
	/// 显示地图
	/// </summary>
	public Transform layer_map;

	/// <summary>
	/// 地图块层
	/// </summary>
	public Transform layer_tiledMap;

	/// <summary>
	/// 精灵层
	/// </summary>
	public Transform layer_actor;

	/// <summary>
	/// 地图2   Z坐标  偏移
	/// </summary>
	float m_offSet = 100.0f;
	//======== map 相关 ================================
	[HideInInspector]
	public int mapW = 8;
	[HideInInspector]
	public int mapH = 6;
	[HideInInspector]
	/// <summary>
	/// 地图块大小
	/// </summary>
	public float m_tiledMapW = 1.2f;
	void Start () {
		m_instance = this;
		initGameCommonBtn (layer_ui);
		initMap ();

		//test 获取更新数据data
//		this.GetComponent<tk2dTextMesh>().text = value;
		//修改显示图集图片ID
//		this.GetComponent<tk2dSprite>().SetSprite();
	}
	
	// Update is called once per frame
	void Update () {
		switch (m_state) {

		}
	}


	void initMap(){
		float x = 0;
		float y = 0;
		//对家间的坐标差
		float offsetH = 0.25f;

		int curId = mapW * mapH * 2 - 1;

		GameObject role = null;
		for(int i=0;i<mapW * mapH;i++){
			//玩家地图  生成位置id
			x = layer_tiledMap.transform.position.x - ((mapW-1) * m_tiledMapW) / 2.0f + (i % mapW)* m_tiledMapW;
			y = layer_tiledMap.transform.position.y - (i / mapW) * m_tiledMapW - m_tiledMapW/2 - offsetH;
			GameObject mapback = Instantiate (Utils.m_instance.load ("tiledmap")) as GameObject;
			mapback.transform.parent = layer_tiledMap;
			mapback.transform.position = new Vector3(x,y,layer_tiledMap.transform.position.z);
			mapback.transform.GetComponent<tk2dButton> ().ButtonDownEvent += btn_selectMap;
			mapback.transform.GetComponent<tk2dButton> ().messageName = i.ToString ();
			mapback.name = i.ToString ();


			//测试  玩家动画
			if(i<4){
				role = Instantiate (Utils.m_instance.load ("spine/role" + i % 4)) as GameObject;
				role.transform.parent = layer_actor;
				role.transform.position = new Vector3(x,y - m_tiledMapW / 2,layer_actor.transform.position.z);
				role.name = i.ToString ();
			}




			//对家地图  生成位置id
			x = layer_tiledMap.transform.position.x - ((mapW-1) * m_tiledMapW) / 2.0f + (i % mapW)* m_tiledMapW;
			y = layer_tiledMap.transform.position.y - (i / mapW) * m_tiledMapW - m_tiledMapW/2 + offsetH + mapH * m_tiledMapW;
			mapback = Instantiate (Utils.m_instance.load ("tiledmap")) as GameObject;
			mapback.transform.parent = layer_tiledMap;
			mapback.transform.position = new Vector3(x,y,layer_tiledMap.transform.position.z);
			mapback.transform.GetComponent<tk2dButton> ().ButtonDownEvent += btn_selectMap;
			mapback.transform.GetComponent<tk2dButton> ().messageName = curId.ToString ();
			mapback.name = curId.ToString ();
			curId--;

		}
		//测试动画
		role.transform.transform.DOMoveX (layer_tiledMap.FindChild("" + (mapW - 1)).position.x,1.0f).SetLoops(-1,LoopType.Yoyo).SetEase(Ease.Linear);
	}


	//屏幕移动
	void OnDrag(DragGesture gesture) {		
		// 当前识别器阶段 (Started/Updated/Ended)
		//ContinuousGesturePhase phase = gesture.Phase;
		// 最后一帧的拖拽/移动数据
		Vector2 deltaMove = gesture.DeltaMove;
		//完整的拖拽数据
		Vector2 totalMove = gesture.TotalMove;

	}
	void OnTap(TapGesture gesture) {
		
	}

	void OnFingerUp(FingerUpEvent e){
		
	}

	//=======游戏中的MAP============================
	public void btn_selectMap(tk2dButton source){
		int curSelectMapId = int.Parse (source.messageName);
		Debug.Log ("curSelectMapId " + curSelectMapId);
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
		case "btn_test":
			Debug.Log ("btn_test....");
			break;
		}
	}
}
