/************************************************************************
 * Copyright (c) 2016 VitGame
 * Author	: Jxhgzs
 * Mail		: jxhgzs@126.com
 * Date		: 2016-04-19
 * Use      : Network-通讯协议统一注册管理
************************************************************************/
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace Vitality
{
	public class VitHandlerManager
	{
		private static VitHandlerManager m_instance;
		public static VitHandlerManager Instance
	    {
			get
			{
		        if (m_instance == null)
		        {
		            //第一次调用的时候 创建单例对象 并进行初始化操作
		            m_instance = new VitHandlerManager();
		        }

		        return m_instance;
			}
	    }
		private bool m_bIsLoad = false;

	    // 注册协议
	    public void registerHandler()
	    {
			if (m_bIsLoad) {
				return;
			}
			m_bIsLoad = true;
	    }

	}
}//< Vitality