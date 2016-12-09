using UnityEngine;
using System.Collections;
using System;
using System.Collections.Generic;

public class Utils : MonoBehaviour {
	/// <summary>
	/// 全局
	/// </summary>
	public bool m_soundEnabled = true;
	public bool m_musicEnabled = true;

	public string m_useerTokey;

	public static Utils m_instance;

	//玩家id
	public int playerId;
	//玩家名字
	public string name;
	//服务器当前时间
	public int serverTime;

	/// <summary>
	/// 游戏中的对像管理加载过的对像缓存列表
	/// 
	/// 切换场景时释放
	/// 
	/// </summary>
	public Dictionary<string,GameObject> m_dict_gameObj;

	void Start () {

		m_dict_gameObj = new Dictionary<string, GameObject> ();

		m_useerTokey = "null";

		m_instance = this;
	
	}



	public GameObject load(string name){
		GameObject child;
		if (!m_dict_gameObj.ContainsKey (name)) {
			child = Resources.Load (name)as GameObject;
			m_dict_gameObj.Add (name, child);
		} else {
			m_dict_gameObj.TryGetValue(name,out child);
		}
		return child;
	}

	/// <summary>
	/// 加载场景
	/// </summary>
	/// <param name="level">Level.</param>
	public void LoadLevel(string level)
	{

		m_dict_gameObj.Clear ();

		Resources.UnloadUnusedAssets ();

		Application.LoadLevel (level);

	}

	/// <summary>
	/// 异步加载
	/// </summary>
	/// <param name="targetScene">Target scene.</param>
	public void LoadLevelAsync(string targetScene)
	{

		m_dict_gameObj.Clear ();

		//进度条加载
		Resources.UnloadUnusedAssets ();

		Application.LoadLevelAsync (targetScene);
	}

	// Update is called once per frame
	void Update () {

	}




	public void clear(Transform obj)
	{
		for (int i = obj.childCount - 1; i >= 0; i--)
		{
			MonoBehaviour.Destroy(obj.GetChild(i).gameObject);
		}
	}
	
	
	public void loadData(){
		m_soundEnabled = getBool ("m_soundEnabled");
	}
	public void saveData(){
//		return;
		//=======================   同步 ok ====================
		setBool ("m_soundEnabled", m_soundEnabled);

		PlayerPrefs.Save ();
	}
	public bool haveData(){
		if (PlayerPrefs.HasKey ("m_soundEnabled")) {
			return true;
		}
		return false;
	}
	public static void setInt(string key, int value){
		PlayerPrefs.SetInt (key,value);
	}
	public static int getInt(string key){
		return PlayerPrefs.GetInt(key);
	}
	public static void setBool(string key, bool value){
		PlayerPrefs.SetInt (key,value?1:0);
	}
	public static bool getBool(string key){
		return PlayerPrefs.GetInt(key) == 1;
	}
	public static void setString(string key, string value){
		PlayerPrefs.SetString (key,value);
	}
	public static string getString(string key){
		return PlayerPrefs.GetString(key);
	}
	public static void setFloat(string key, float value){
		PlayerPrefs.SetFloat (key,value);
	}
	public static float getFloat(string key){
		return PlayerPrefs.GetFloat(key);
	}
	public static bool SetBoolArray(string key, params bool[] boolArray)
	{
		if (boolArray.Length == 0) return false;
		
		System.Text.StringBuilder sb = new System.Text.StringBuilder();
		for (int i = 0; i < boolArray.Length - 1; i++)
			sb.Append(boolArray[i]).Append("|");
		sb.Append(boolArray[boolArray.Length - 1]);
		
		try { PlayerPrefs.SetString(key, sb.ToString()); }
		catch { return false; }
		return true;
	}
	public static bool[] GetBoolArray(string key)
	{
		if (PlayerPrefs.HasKey(key))
		{
			string[] stringArray = PlayerPrefs.GetString(key).Split("|"[0]);
			bool[] boolArray = new bool[stringArray.Length];
			for (int i = 0; i < stringArray.Length; i++)
				boolArray[i] = System.Convert.ToBoolean(stringArray[i]);
			return boolArray;
		}
		return new bool[0];
	}
	public void SetIntArray (string key, int[] intArray) {
		if (intArray.Length == 0) return;		
		var sb = new System.Text.StringBuilder();
		int i = 0;
		for (i = 0; i < intArray.Length-1; i++) {
			sb.Append(intArray[i]).Append("|");
		}
		sb.Append(intArray[i]);
		
		try {
			PlayerPrefs.SetString(key, sb.ToString());
		}
		catch {
			
		}
		return;
	}
	public int[] GetIntArray (string key) {
		if (PlayerPrefs.HasKey(key)) {
			var stringArray = PlayerPrefs.GetString(key).Split("|"[0]);
			var intArray = new int[stringArray.Length];
			int i = 0;
			for (i = 0; i < stringArray.Length; i++) {
				intArray[i] = int.Parse(stringArray[i]);
			}
			return intArray;
		}
		return null;
	}
	public static bool SetFloatArray(string key, params float[] floatArray)
	{
		if (floatArray.Length == 0) return false;
		
		System.Text.StringBuilder sb = new System.Text.StringBuilder();
		for (int i = 0; i < floatArray.Length - 1; i++)
			sb.Append(floatArray[i]).Append("|");
		sb.Append(floatArray[floatArray.Length - 1]);
		
		try
		{
			PlayerPrefs.SetString(key, sb.ToString());
		}
		catch
		{
			return false;
		}
		return true;
	}
	public static float[] GetFloatArray(string key)
	{
		if (PlayerPrefs.HasKey(key))
		{
			string[] stringArray = PlayerPrefs.GetString(key).Split("|"[0]);
			float[] floatArray = new float[stringArray.Length];
			for (int i = 0; i < stringArray.Length; i++)
				floatArray[i] = System.Convert.ToSingle(stringArray[i]);
			return floatArray;
		}
		return new float[0];
	}
	public static bool SetStringArray(string key, char separator, params string[] stringArray)
	{
		if (stringArray.Length == 0) return false;
		try
		{ PlayerPrefs.SetString(key, System.String.Join(separator.ToString(), stringArray)); }
		catch
		{ return false; }
		return true;
	}
	public static bool SetStringArray(string key, params string[] stringArray)
	{
		if (!SetStringArray(key, "\n"[0], stringArray))
			return false;
		return true;
	}
	public static string[] GetStringArray(string key)
	{
		if (PlayerPrefs.HasKey(key))
			return PlayerPrefs.GetString(key).Split("\n"[0]);
		return new string[0];
	}


	public static int BigtoLittle16(int value){
		return ((((UInt16)(value) & 0xff00) >> 8) | (((UInt16)(value) & 0x00ff) << 8));
	}

	public static int BigtoLittle32(int value){
		return (int)(((((UInt32)(value) & 0xff000000) >> 24) |
			(((UInt32)(value) & 0x00ff0000) >> 8) | 
			(((UInt32)(value) & 0x0000ff00) << 8) | 
			(((UInt32)(value) & 0x000000ff) << 24)));
	}
}
