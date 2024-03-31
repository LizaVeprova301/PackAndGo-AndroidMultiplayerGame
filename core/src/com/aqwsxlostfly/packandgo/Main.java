package com.aqwsxlostfly.packandgo;


import com.aqwsxlostfly.packandgo.Screens.WaitingSc;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




public class Main extends Game {

	public static String meId = "0909";

	public  InputState inputState;
	public MessageSender messageSender;

	public Main(InputState inputState) {
		this.inputState = inputState;
	}

	public static SpriteBatch batch;
	public static Texture img;
	public static Texture circle;
	public static Texture capibara;
	public static Texture bullet;
	public static Texture enemy;
	public static Texture ghost;
	public static int screenWidth;
	public static int screenHeight;
	public static int record;

	public boolean socketState;

//	public WaitingSc waitingSc;

	
	@Override
	public void create () {


		batch = new SpriteBatch();
		img = new Texture("packlogo.png");
		circle = new Texture("circle.png");
		capibara = new Texture("capibara.png");
		bullet = new Texture("burgerBullet.png");
		enemy = new Texture("enemy.png");
		ghost = new Texture("ghost.png");
		if (!Gdx.files.local("rec.txt").exists()){
			write("0");
		}
		record=read();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
//		this.waitingSc = new WaitingSc(this);
		setScreen(new WaitingSc(this));
	}



//	public void handleTimer() {
//		this.waitingSc.handleTimer();
//	}


	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	public static void write(String str){
		FileHandle file = Gdx.files.local("rec.txt");
		file.writeString(str,false);
	}
	public static int read(){
		FileHandle file = Gdx.files.local("rec.txt");
		return Integer.parseInt(file.readString());
	}

	public String getMeId() {
		return meId;
	}

	public void setMeId(String meId) {
		Main.meId = meId;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public boolean getSocketState() {
		return socketState;
	}

	public void setSocketState(boolean socketState) {
		this.socketState = socketState;
	}
}
