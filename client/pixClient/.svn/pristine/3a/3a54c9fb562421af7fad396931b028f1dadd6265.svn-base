using UnityEngine;
using System.Collections;

public class MultiResolution : MonoBehaviour {
		// Singleton
		public static MultiResolution instance;
		static MultiResolution()
		{
			instance = new MultiResolution();
		}
		
		
		// Variables
		public float Width { get; private set; }                        // 640p 기준 width
		public float Height { get; private set; }                       // 640p 기준 height
		public float GameHeight { get; private set; }                       // 640p 기준 height
		public float OrigRatioWidth { get; private set; }               // 원본 가로 사이즈와의 비율
		public float OrigRatioHeight { get; private set; }              // 원본 세로 사이즈와의 비율
		public float OrigGameRatioHeight { get; private set; }          // 원본 세로 사이즈와의 비율
		public float ScreenRatio { get { return Height / Width; } }     // 가로 대비 세로
		public float ReverseOrigRatioWidth { get; private set; }        // 원본 세로 사이즈외의 비율의 역
		public float ReverseOrigRatioHeight { get; private set; }       // 원본 가로 사이즈외의 비율의 역
		
		
		// Constant
		public const float standardWidth = 1920.0f;
		public const float standardHeight = 1080.0f;
		
		public const float gameHeight = 1080.0f;
		
		public const float screeW = 19.2f;
		public const float screeH = 10.8f;
		///------------------------------------------------------------------------
		/// <summary>
		/// 생성자
		/// </summary>
		///------------------------------------------------------------------------
		public MultiResolution()
		{
			OrigRatioWidth = standardWidth / Screen.width;
			OrigRatioHeight = standardHeight / Screen.height;
			
			OrigGameRatioHeight = gameHeight / Screen.height;
			
			
			ReverseOrigRatioWidth = 1.0f / OrigRatioWidth;
			ReverseOrigRatioHeight = 1.0f / OrigRatioHeight;
			
			Width = Screen.width * OrigRatioHeight;
			Height = Screen.height * OrigRatioHeight;
			
			GameHeight = Screen.height * gameHeight;
			
			Debug.Log(Screen.width + ":" + Screen.height);
			Debug.Log(Width + ":" + Height);
			Debug.Log("OrigGameRatioHeight :" + OrigGameRatioHeight);
			Debug.Log("ReverseOrigRatioHeight :" + ReverseOrigRatioHeight);
		}
	}
