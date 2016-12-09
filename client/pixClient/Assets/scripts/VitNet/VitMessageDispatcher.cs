/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-Tcp数据处理
************************************************************************/
using UnityEngine;
using System.Collections;

namespace Vitality
{
	public class VitMessageDispatcher : MonoBehaviour {

		// Use this for initialization
		void Start () {
		
		}
		
		// Update is called once per frame
		void Update () {
	        //每帧判断消息队列是否有消息
			VitNetworkScript.Instance.parseMessage();
		}
	}
}//< Vitality