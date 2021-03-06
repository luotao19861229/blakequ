package com.hao.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

/**
 * 里面定义游戏常用的常量,以及常用的方法
 * @author Administrator
 *
 */
public class yarin
{
	/* 游戏状态常量 */
	public static final int	GAME_SPLASH		= 1;
	public static final int	GAME_MENU		= 2;
	public static final int	GAME_ABOUT		= 3;
	public static final int	GAME_HELP		= 4;
	public static final int	GAME_RUN		= 5;
	public static final int	GAME_CONTINUE	= 6;
	/* 按键常量 */
	public static final int	KEY_DPAD_UP		= KeyEvent.KEYCODE_DPAD_UP;
	public static final int	KEY_DPAD_DOWN	= KeyEvent.KEYCODE_DPAD_DOWN;
	public static final int	KEY_DPAD_LEFT	= KeyEvent.KEYCODE_DPAD_LEFT;
	public static final int	KEY_DPAD_RIGHT	= KeyEvent.KEYCODE_DPAD_RIGHT;
	public static final int	KEY_DPAD_OK		= KeyEvent.KEYCODE_DPAD_CENTER;	// 23
	/* 右软键需要自己定义，否则右软键则是退出 */
	public static final int	KEY_SOFT_RIGHT	= KeyEvent.KEYCODE_BACK;			// 4

	/* 游戏循环时间 */
	public static final int	GAME_LOOP		= 100;
	/* 屏幕的宽高,这个实际上应该根据情况实际获取 */
	public static final int	SCREENW			= 320;
	public static final int	SCREENH			= 480;
	/* 这个应该是地图的高宽*/
	public static final int	BORDERW			= 320;
	public static final int	BORDERH			= 352;
	/* 这是地图的初始坐标位置（x,y）*/
	public static final int	BORDERX			= (yarin.SCREENW - BORDERW) / 2;
	public static final int	BORDERY			= (yarin.SCREENH - BORDERH) / 2; //它在左上角位置，地图开始绘制的地方
	public static final int	MessageBoxH		= 70;

	/* 文字的尺寸 */
	public static final int	TextSize		= 16;

	/**
	 * 绘制一个填充矩形
	 * @param canvas
	 * @param rect
	 * @param paint
	 */
	public static void fillRect(Canvas canvas, Rect rect, Paint paint)
	{
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
	}


	/**
	 * 绘制空心矩形
	 * @param canvas
	 * @param rect
	 * @param paint
	 */
	public static void drawRect(Canvas canvas, Rect rect, Paint paint)
	{
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
	}


	
	public static void SETAEERECT(Rect rect, int x, int y, int w, int h)
	{
		rect.left = x;
		rect.top = y;
		rect.right = x + w;
		rect.bottom = y + h;
	}


	public static void fillRect(Canvas canvas, int x, int y, int w, int h, Paint paint)
	{
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(x, y, x + w, y + h, paint);
	}


	public static void drawRect(Canvas canvas, int x, int y, int w, int h, Paint paint)
	{
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, x + w, y + h, paint);
	}


	/**
	 * 绘制图片
	 *
	 * @param		x 屏幕上的x坐标	
	 * @param		y 屏幕上的y坐标
	 * @param		w 要绘制的图片的宽度	
	 * @param		h 要绘制的图片的高度
	 * @param		bx图片上的x坐标
	 * @param		by图片上的y坐标
	 * @return		null
	 */
	public static void drawImage(Canvas canvas, Bitmap blt, int x, int y, int w, int h, int bx, int by)
	{
		Rect src = new Rect();// 图片
		Rect dst = new Rect();// 屏幕
		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;
		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;
		canvas.drawBitmap(blt, src, dst, null);

		src = null;
		dst = null;
	}


	public static void drawImage(Canvas canvas, Bitmap bitmap, float x, float y)
	{
		canvas.drawBitmap(bitmap, x, y, null);
	}


	public static void drawString(Canvas canvas, String str, float x, float y, Paint paint)
	{
		canvas.drawText(str, x, y + paint.getTextSize(), paint);
	}
}
