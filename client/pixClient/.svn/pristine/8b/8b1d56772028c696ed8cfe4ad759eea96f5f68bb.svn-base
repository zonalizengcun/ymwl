using UnityEngine;
using System.Collections;

public class logo : MonoBehaviour {

	// Use this for initialization
	public float delaytime = 0;
	void Start () {
		StartCoroutine (toMenu(delaytime));
	}
	
	// Update is called once per frame
	void Update () {
	}

	IEnumerator toMenu(float time){
		yield return new WaitForSeconds (time);
		Utils.m_instance.LoadLevelAsync ("input");
	}
}
