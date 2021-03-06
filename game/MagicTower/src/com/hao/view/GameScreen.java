package com.hao.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.microedition.lcdui.game.LayerManager;

import com.hao.FightCalc;
import com.hao.FightScreen;
import com.hao.GameMap;
import com.hao.HeroSprite;
import com.hao.MagicTower;
import com.hao.R;
import com.hao.Task;
import com.hao.R.drawable;
import com.hao.abst.GameView;
import com.hao.abst.MainGame;
import com.hao.util.CMIDIPlayer;
import com.hao.util.TextUtil;
import com.hao.util.yarin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;

/**
 * 游戏主界面
 * @author Administrator
 *
 */
public class GameScreen extends GameView
{
	private Paint			paint		= null;
	public MainGame			mMainGame	= null;
	
	public static final int TEXT_COLOR = 0xffc800;
	public static final int BACK_COLOR = 0x000000;
	public static final int SMALL_FONT = 12;
	public static final int NORMAL_FONT = 15;
	public static final int LARGE_FONT = 16;
	public static final int UP = 0,DOWN = 1,LEFT = 2,RIGHT = 3;
	public static final int MILLIS_PER_TICK = 300;
	
	private LayerManager layerManager;
	private boolean mshowMessage = false;
	/**
	 * 标记是否显示对话框，为了方便逻辑处理调用(而不用在逻辑中直接绘制对话框)，决定是否显示对话框
	 */
	public boolean mshowDialog = false;
	public boolean mshowFight = false;
	private String strMessage = "";
	public FightScreen mFightScreen;
	
	private HeroSprite hero;
	private GameMap gameMap;
	private FightCalc fightCalc;
	private Task task;
	private static final int step = GameMap.TILE_WIDTH;
	public Canvas mcanvas;
	//各种图片
	public static final int IMAGE_HERO = 0,
	IMAGE_MAP = 1,
		IMAGE_DIALOG_HERO = 2,
	IMAGE_DIALOG_ANGLE = 3,
	IMAGE_DIALOG_THIEF = 4,
	IMAGE_BORDER = 5,
	IMAGE_DIALOGBOX = 6,
	IMAGE_MESSAGEBOX = 7,
	IMAGE_BORDER2 = 8,
	IMAGE_BORDER3 = 9,
	IMAGE_BORDER4 = 10,
	IMAGE_DIALOG_PRINCESS = 11,
	IMAGE_DIALOG_BOSS = 12,
	IMAGE_BLUE_GEEZER = 13,
	IMAGE_RED_GEEZER = 14,
	IMAGE_SPLASH = 15,
	IMAGE_GAMEOVER = 16;
	
	public int					borderX, borderY;	//地图上x和y的边界
	private int				winWidth, winHeight;	//地图的高宽
	private int				scrollX, scrollY;
	private int				curDialogImg;			//当前和hero对话的对象
	private MagicTower			magicTower;

	public TextUtil				tu				= null;
	/**
	 * 捡到的物品类型
	 */
	private int				miType			= -1;
	/**
	 * 
	 * @param context
	 * @param magicTower
	 * @param mainGame
	 * @param isNewGame是否是新游戏，如果是载入上次的则为false
	 */
	public GameScreen(Context context, MagicTower magicTower, MainGame mainGame, boolean isNewGame)
	{
		super(context);
		mMainGame = mainGame;
		paint = new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		this.magicTower = magicTower;
		winWidth = yarin.BORDERW;
		winHeight = yarin.BORDERH;
		borderX = yarin.BORDERX; //左上 地图x坐标
		borderY = yarin.SCREENH - yarin.BORDERH - 60;	//左上地图y坐标
		layerManager = new LayerManager();
		tu = new TextUtil();
		hero = new HeroSprite(BitmapFactory.decodeResource(this.getResources(), R.drawable.hero16), GameMap.TILE_WIDTH, GameMap.TILE_HEIGHT);
		hero.defineReferencePixel(GameMap.TILE_WIDTH / 2, GameMap.TILE_HEIGHT / 2);
		gameMap = new GameMap(hero, BitmapFactory.decodeResource(this.getResources(), R.drawable.map16));
		fightCalc = new FightCalc(hero);
		task = new Task(this, hero, gameMap);
		hero.setTask(task);
		if (isNewGame == false)
		{
			load();
		}
		//注意这里的图层添加顺序，绘制的顺序是按添加的反顺序，即先添加的后绘制
		//如添加顺序是hero，map 而绘制的顺序是map,hero;这很重要，因为如果先添加map在添加hero，则hero会被覆盖，显示不出来
		layerManager.append(hero);
		layerManager.append(gameMap.getFloorMap());
		setName("GameScreen");
	}


	protected void onDraw(Canvas canvas)
	{
		mcanvas = canvas;
		paint.setColor(Color.BLACK);
		yarin.fillRect(canvas, 0, 0, yarin.SCREENW, yarin.SCREENH, paint);

		drawAttr(canvas);
		gameMap.animateMap();
		
		//根据hero相对位置滑动地图（地图不能完全显示）
		scrollWin();
		//重设窗口坐标系,设置视窗的位置和大小
		layerManager.setViewWindow(scrollX, scrollY, winWidth, winHeight);
		//在(borderX, borderY)处绘制所有图层
		layerManager.paint(canvas, borderX, borderY);

		if (mshowMessage)
		{
			showMessage(strMessage);
		}
		if (mshowDialog)
		{
			dialog();
		}
		if (mFightScreen != null && mshowFight)
		{
			mFightScreen.onDraw(canvas);
		}
	}


	public boolean onKeyUp(int keyCode)
	{
		int type = 0;
		if (keyCode == KeyEvent.KEYCODE_1)
		{
			mMainGame.mCMIDIPlayer.StopMusic();
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_2)
		{
			mMainGame.mCMIDIPlayer.PlayMusic(CMIDIPlayer.MP3_RUN);
		}
		if (mFightScreen != null && mshowFight)
		{
			mFightScreen.onKeyUp(keyCode);
			return false;
		}
		if ((mshowMessage && keyCode != yarin.KEY_DPAD_OK) || (mshowDialog && keyCode != yarin.KEY_DPAD_OK))
		{
			return false;
		}
		switch (keyCode)
		{
			case yarin.KEY_SOFT_RIGHT:	//退出键
				save();
				mMainGame.controlView(yarin.GAME_MENU);
				if (mMainGame.mbMusic == 1)
				{
					mMainGame.mCMIDIPlayer.PlayMusic(CMIDIPlayer.MP3_MENU);
				}
				break;
			case yarin.KEY_DPAD_OK:	//主要处理ok键（显示消息等）
				if (mshowMessage)
				{
					// 直到无法翻页为止
					if (!tu.Key(yarin.KEY_DPAD_DOWN))
					{
						//如果是捡到物品的时候，先将弹出的消息框隐藏，然后将捡到的物品从地图中删除
						mshowMessage = false;
						if ((miType >= GameMap.MAP_YELLOW_KEY) && (miType <= GameMap.MAP_SHIELD3))
						{
							miType = -1;
							gameMap.remove();
						}
					}
				}
				else if (mshowDialog)
				{
					if (!tu.Key(yarin.KEY_DPAD_DOWN))
					{
						if (task.mbtask)
						{
							//已经完成任务时的对话
							if (task.curTask2 < Task.finishedDialog[task.curTask].length - 1)
							{
								task.curTask2++;
								tu.InitText(Task.finishedDialog[task.curTask][task.curTask2], 0, (yarin.SCREENH - yarin.MessageBoxH) / 2, yarin.SCREENW,
									yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
							}
							else
							{
								task.curTask2 = 0;
								mshowDialog = false;
							}
						}
						else
						{
							//接收任务时的对话
							if (task.curTask2 < Task.recieveDialog[task.curTask].length - 1)
							{
								task.curTask2++;
								tu.InitText(Task.recieveDialog[task.curTask][task.curTask2], 0, (yarin.SCREENH - yarin.MessageBoxH) / 2, yarin.SCREENW,
									yarin.MessageBoxH, 0x0, 0xffff00, 255, yarin.TextSize);
							}
							else
							{
								task.curTask2 = 0;
								mshowDialog = false;
							}
						}
					}
				}
				break;
			case yarin.KEY_DPAD_UP://向上键--上走
				hero.setFrame(9);
				if ((type = gameMap.canPass(UP)) == 1)
				{
					hero.move(0, -step);
				}
				break;
			case yarin.KEY_DPAD_DOWN:
				hero.setFrame(0);
				if ((type = gameMap.canPass(DOWN)) == 1)
				{
					hero.move(0, step);
				}
				break;
			case yarin.KEY_DPAD_LEFT:
				hero.setFrame(3);
				if ((type = gameMap.canPass(LEFT)) == 1)
				{
					hero.move(-step, 0);
				}
				break;
			case yarin.KEY_DPAD_RIGHT:
				hero.setFrame(6);
				if ((type = gameMap.canPass(RIGHT)) == 1)
				{
					hero.move(step, 0);
				}
				break;
			default:
				break;
		}
		if (type >= GameMap.MAP_LOCKED_BARRIER)
			dealType(type);
		return true;
	}


	public boolean onKeyDown(int keyCode)
	{
		return true;
	}


	public void refurbish()
	{

	}


	public void reCycle()
	{
		paint = null;
		System.gc();
	}


	/**
	 * 对不同类型的单元处理（该单元正处在hero的正前方）
	 * @param type
	 */
	private void dealType(int type)
	{
		//当上楼的时候，进入下一关
		if (type == GameMap.MAP_UPSTAIR)
		{
			gameMap.upstair();
			hero.setFrame(0);
		}
		//进入上一关
		else if (type == GameMap.MAP_DOWNSTAIR)
		{
			gameMap.downstair();
			hero.setFrame(0);
		}
		//遇到门
		else if ((type >= GameMap.MAP_YELLOW_DOOR) && (type <= GameMap.MAP_RED_DOOR))
		{
			if (hero.useKey(type))
			{
				gameMap.remove();
			}
		}
		//遇到障碍物
		else if (type == GameMap.MAP_BARRIER)
		{
			gameMap.remove();
		}
		//遇到商店（功能待扩展）
		else if ((type == GameMap.MAP_SHOP1) || (type == GameMap.MAP_SHOP2))
		{
			if (gameMap.curFloorNum == 3)
			{
//				 shop(ShopScreen.SHOP_3);
			}
			else
			{
				// shop(ShopScreen.SHOP_11);
			}
		}
		//遇到钥匙等宝物
		else if ((type >= GameMap.MAP_YELLOW_KEY) && (type <= GameMap.MAP_SHIELD3))
		{
			mshowMessage = true;//显示一个提示框信息
			miType = type;
			tu.InitText(hero.takeGem(type), 0, (yarin.SCREENH - yarin.MessageBoxH) / 2, yarin.SCREENW, yarin.MessageBoxH, 0x0, 0xff0000, 255, yarin.TextSize);
		}
		//遇到各种人物
		else if (type >= GameMap.MAP_ANGLE)
		{
			if (type > GameMap.MAP_ORGE31)
				type -= GameMap.SWITCH_OFFSET;
			//遇到天使，小偷等角色
			if (((type >= GameMap.MAP_ANGLE) && (type <= GameMap.MAP_RED_GEEZER)) || (type == GameMap.MAP_ORGE31))
			{
				/* 处理任务，注意不同层的老头的任务不同 */
				dealTask(type);
			}
			//遇到恶魔就要fight
			else if ((type >= GameMap.MAP_ORGE1) && (type <= GameMap.MAP_ORGE30))
			{
				fight(type);
			}
		}
	}


	/**
	 * 绘制消息框（如获取到宝石等）
	 * @param msg
	 */
	public void showMessage(String msg)
	{
		int w = yarin.SCREENW;
		int h = yarin.MessageBoxH;
		int x = 0;
		int y = (yarin.SCREENH - yarin.MessageBoxH) / 2;
		Paint ptmPaint = new Paint();
		ptmPaint.setARGB(255, Color.red(BACK_COLOR), Color.green(BACK_COLOR), Color.blue(BACK_COLOR));

		yarin.fillRect(mcanvas, x, y, w, h, ptmPaint);

		tu.DrawText(mcanvas);
		ptmPaint = null;
	}


	/**
	 * 绘制英雄的属性(在最下部)
	 * @param canvas
	 */
	private void drawAttr(Canvas canvas)
	{
		int iH = 17;
		Paint ptmPaint = new Paint();
		ptmPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		ptmPaint.setARGB(255, Color.red(0xffff00), Color.green(0xffff00), Color.blue(0xffff00));

		yarin.drawRect(canvas, 0, 420, 320, 60, ptmPaint);
		canvas.drawLine(70, 420, 70, 480, ptmPaint);
		canvas.drawLine(230, 420, 230, 480, ptmPaint);

		yarin.drawRect(canvas, 0, 0, 320, 68, ptmPaint);

		yarin.drawImage(canvas, getImage(IMAGE_HERO), 19, 424, 32, 32, 0, 0);

		ptmPaint.setTextSize(GameScreen.SMALL_FONT);
		ptmPaint.setARGB(255, Color.red(0xffffff), Color.green(0xffffff), Color.blue(0xffffff));
		// 等级
		yarin.drawString(canvas, "等级：" + hero.getLevel(), 70, 425, ptmPaint);
		yarin.drawString(canvas, "经验：" + hero.getExperience(), 70, 425 + 1 * iH, ptmPaint);
		yarin.drawString(canvas, "金币：" + hero.getMoney(), 70, 425 + 2 * +iH, ptmPaint);

		yarin.drawString(canvas, "生命：" + hero.getHp(), 150, 425, ptmPaint);
		yarin.drawString(canvas, "攻击：" + hero.getAttack(), 150, 425 + 1 * iH, ptmPaint);
		yarin.drawString(canvas, "防御：" + hero.getDefend(), 150, 425 + 2 * +iH, ptmPaint);

		yarin.drawString(canvas, "红钥匙：" + hero.getRedKey(), 230, 425, ptmPaint);
		yarin.drawString(canvas, "黄钥匙：" + hero.getYellowKey(), 230, 425 + 1 * iH, ptmPaint);
		yarin.drawString(canvas, "蓝钥匙：" + hero.getBlueKey(), 230, 425 + 2 * +iH, ptmPaint);

		String string = "《序章》";
		if (gameMap.curFloorNum != 0)
		{
			string = "《第" + gameMap.curFloorNum + "层》";
		}
		yarin.drawString(canvas, string, (70 - ptmPaint.measureText(string)) / 2, 460, ptmPaint);

		ptmPaint.setTextSize(17);
		string = "《魔塔Android版》谢谢使用！";
		yarin.drawString(canvas, string, (yarin.BORDERW - ptmPaint.measureText(string)) / 2, (68 - 17) / 2, ptmPaint);

		ptmPaint = null;
	}


	/**
	 * 由于屏幕不能完全显示地图，故而需要能够滑动(根据hero的位置)
	 */
	private void scrollWin()
	{
		//当英雄移动到地图的屏幕中央的时候判断是否要移动地图
		scrollX = hero.getRefPixelX() - winWidth / 2;	//当前hero的位置减去屏幕宽度一半
		scrollY = hero.getRefPixelY() - winHeight / 2;
		//如果scrollX<0说明地图需要左移
		if (scrollX < 0)
		{
			scrollX = 0;
		}
		else if ((scrollX + winWidth) > GameMap.MAP_WIDTH)
		{
			//水平移动的距离就是:地图的宽度-屏幕宽度
			scrollX = GameMap.MAP_WIDTH - winWidth;
		}
		if (scrollY < 0)
		{
			scrollY = 0;
		}
		else if ((scrollY + winHeight) > GameMap.MAP_HEIGHT)
		{
			scrollY = GameMap.MAP_HEIGHT - winHeight;
		}
	}

	/**
	 * 遇见恶魔开始战斗
	 * @param type
	 * @return
	 */
	public boolean fight(int type)
	{
		if (fightCalc.canAttack(type) == false)
			return false;
		mFightScreen = new FightScreen(this, fightCalc, hero, type);
		mshowFight = true;
		gameMap.remove();
		return true;
	}


	private void shop(int type)
	{
		// 商店
	}


	private void jump()
	{
		// 跳
	}


	private void lookup()
	{
		// 查看
	}


	/**
	 * 遇到不同的类型的角色，接受不同的任务
	 * @param type
	 */
	private void dealTask(int type)
	{
		int curTask = -1;
		switch (type)
		{
			case GameMap.MAP_ANGLE:
				curTask = Task.FIND_CROSS;
				curDialogImg = IMAGE_DIALOG_ANGLE;
				break;
			case GameMap.MAP_THIEF:
				curTask = Task.FIND_AX;
				curDialogImg = IMAGE_DIALOG_THIEF;
				break;
			case GameMap.MAP_PRINCESS:
				curTask = Task.RESCUE_PRINCESS;
				curDialogImg = IMAGE_DIALOG_PRINCESS;
				break;
			case GameMap.MAP_BLUE_GEEZER:
				switch (gameMap.curFloorNum)
				{
					case 2:
						curTask = Task.GET_QINGFEND_JIAN;
						curDialogImg = IMAGE_BLUE_GEEZER;
						mshowMessage = true;
						miType = type;
						tu.InitText(hero.takeGem(GameMap.MAP_SWORD3), 0, (yarin.SCREENH - yarin.MessageBoxH) / 2, yarin.SCREENW, yarin.MessageBoxH, 0x0,
							0xff0000, 255, yarin.TextSize);
						// showMessage(mcanvas,hero.takeGem(GameMap.MAP_SWORD3));
						break;
					case 5:
						// shop(ShopScreen.SHOP_5_1);
						break;
					case 13:
						// shop(ShopScreen.SHOP_13);
						break;
					case 15:
						curTask = Task.GET_SHENGGUANG_JIAN;
						curDialogImg = IMAGE_BLUE_GEEZER;
						break;
				}
				break;
			case GameMap.MAP_RED_GEEZER:
				switch (gameMap.curFloorNum)
				{
					case 2:
						curTask = Task.GET_HUANGJIN_DUN;
						curDialogImg = IMAGE_RED_GEEZER;
						mshowMessage = true;
						miType = type;
						tu.InitText(hero.takeGem(GameMap.MAP_SHIELD3), 0, (yarin.SCREENH - yarin.MessageBoxH) / 2, yarin.SCREENW, yarin.MessageBoxH, 0x0,
							0xff0000, 255, yarin.TextSize);
						// showMessage(mcanvas,hero.takeGem(GameMap.MAP_SHIELD3));
						break;
					case 5:
						// shop(ShopScreen.SHOP_5_2);
						break;
					case 12:
						// shop(ShopScreen.SHOP_12);
						break;
					case 15:
						curTask = Task.GET_XINGGUANG_DUN;
						curDialogImg = IMAGE_RED_GEEZER;
						break;
				}
				break;
			case GameMap.MAP_ORGE31:
				curTask = Task.FIGHT_BOSS;
				curDialogImg = IMAGE_DIALOG_BOSS;
				break;
		}
		if (curTask == -1)
			return;
		task.execTask(curTask);
	}


	/**
	 * 绘制对话框
	 */
	public void dialog()
	{
		int x, y, w, h;
		w = yarin.SCREENW;
		h = yarin.MessageBoxH;
		x = 0;
		y = (yarin.SCREENH - yarin.MessageBoxH) / 2;
		//一问一答，0,2,4是hero，1,3,5是其他角色
		if (task.curTask2 % 2 == 0)
		{
			//绘制hero
			drawDialogBox(IMAGE_DIALOG_HERO, x, y, w, h);
		}
		else
		{
			//绘制和hero对话的角色
			drawDialogBox(curDialogImg, x, y, w, h);
		}

		tu.DrawText(mcanvas);
	}


	/**
	 * 绘制对话上面的图片（头像）
	 * @param imgType
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void drawDialogBox(int imgType, int x, int y, int w, int h)
	{
		Paint ptmPaint = new Paint();
		ptmPaint.setARGB(255, Color.red(BACK_COLOR), Color.green(BACK_COLOR), Color.blue(BACK_COLOR));

		yarin.fillRect(mcanvas, x, y, w, h, ptmPaint);

		Bitmap img = getImage(imgType);

		yarin.drawRect(mcanvas, x, y, w, h, ptmPaint);
		if (img != null)
		{
			if (imgType == IMAGE_DIALOG_HERO)
			{
				yarin.drawImage(mcanvas, img, x, y - 64);
			}
			else
			{
				yarin.drawImage(mcanvas, img, yarin.SCREENW - 40, y - 64);
			}
		}
		ptmPaint = null;
	}


	protected void keyPressed(int keyCode)
	{

		// switch(keyCode){
		// case GameCanvas.KEY_NUM1: jump();break;
		// case GameCanvas.KEY_NUM3: lookup();break;
		// }
	}


	public void end()
	{
		// stop();
		// EndScreen end = new EndScreen(display,menu);
		// display.setCurrent(end);
		// end.start();
	}


	/**
	 * 根据类型获取图片
	 * @param type
	 * @return
	 */
	public Bitmap getImage(int type)
	{
		Bitmap result = null;
		switch (type)
		{
			case IMAGE_HERO:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.hero16);
				break;
			case IMAGE_MAP:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.map16);
				break;
			case IMAGE_DIALOG_HERO:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_hero);
				break;
			case IMAGE_DIALOG_ANGLE:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_angle);
				break;
			case IMAGE_DIALOG_THIEF:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_thief);
				break;
			case IMAGE_DIALOG_PRINCESS:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_princess);
				break;
			case IMAGE_DIALOG_BOSS:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_boss);
				break;
			case IMAGE_BLUE_GEEZER:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_bluegeezer);
				break;
			case IMAGE_RED_GEEZER:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.dialog_redgeezer);
				break;
			case IMAGE_GAMEOVER:
				result = BitmapFactory.decodeResource(this.getResources(), R.drawable.gameover);
				break;
		}
		return result;
	}


	/**
	 * 保存游戏状态
	 * @return
	 */
	boolean save()
	{
		int col = hero.getRefPixelX() / GameMap.TILE_WIDTH;
		int row = hero.getRefPixelY() / GameMap.TILE_HEIGHT;
		byte[] r1 = hero.encode();
		byte[] r2 = { (byte) gameMap.curFloorNum, (byte) gameMap.reachedHighest, (byte) row, (byte) col, (byte) hero.getFrame() };
		byte[] r3 = task.getTask();

		Properties properties = new Properties();

		properties.put("music", String.valueOf(mMainGame.mbMusic));

		properties.put("r1l", String.valueOf(r1.length));
		properties.put("r2l", String.valueOf(r2.length));
		properties.put("r3l", String.valueOf(r3.length));
		for (int i = 0; i < r1.length; i++)
		{
			properties.put("r1_" + i, String.valueOf(r1[i]));
		}
		for (int i = 0; i < r2.length; i++)
		{
			properties.put("r2_" + i, String.valueOf(r2[i]));
		}
		for (int i = 0; i < r3.length; i++)
		{
			properties.put("r3_" + i, String.valueOf(r3[i]));
		}

		for (int i = 0; i < GameMap.FLOOR_NUM; i++)
		{
			byte map[] = gameMap.getFloorArray(i);
			for (int j = 0; j < map.length; j++)
			{
				properties.put("map_" + i + "_" + j, String.valueOf(map[j]));
			}
		}

		try
		{
			FileOutputStream stream = magicTower.openFileOutput("save", Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}

		return true;
	}


	/**
	 * 载入上次保存的游戏
	 * @return
	 */
	boolean load()
	{
		Properties properties = new Properties();
		try
		{
			FileInputStream stream = magicTower.openFileInput("save");

			properties.load(stream);
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		//是否播放音乐
		mMainGame.mbMusic = Byte.valueOf(properties.get("music").toString());

		byte[] r1 = new byte[Byte.valueOf(properties.get("r1l").toString())];
		byte[] r2 = new byte[Byte.valueOf(properties.get("r2l").toString())];
		byte[] r3 = new byte[Byte.valueOf(properties.get("r3l").toString())];

		for (int i = 0; i < r1.length; i++)
		{
			r1[i] = Byte.valueOf(properties.get("r1_" + i).toString());
		}
		for (int i = 0; i < r2.length; i++)
		{
			r2[i] = Byte.valueOf(properties.get("r2_" + i).toString());
		}
		for (int i = 0; i < r3.length; i++)
		{
			r3[i] = Byte.valueOf(properties.get("r3_" + i).toString());
		}

		hero.decode(r1);
		gameMap.curFloorNum = r2[0];
		gameMap.reachedHighest = r2[1];
		hero.setFrame(r2[4]);
		task.setTask(r3);

		for (int i = 0; i < GameMap.FLOOR_NUM; i++)
		{
			byte[] map = new byte[GameMap.TILE_NUM];
			for (int j = 0; j < map.length; j++)
			{
				map[j] = Byte.valueOf(properties.get("map_" + i + "_" + j).toString());
			}

			gameMap.setFloorArray(i, map);
		}

		gameMap.setMap(gameMap.curFloorNum);

		hero.setRefPixelPosition(r2[3] * GameMap.TILE_WIDTH + GameMap.TILE_WIDTH / 2, r2[2] * GameMap.TILE_HEIGHT + GameMap.TILE_HEIGHT / 2);

		return true;
	}
}

