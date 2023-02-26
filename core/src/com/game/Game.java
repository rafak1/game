package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.Console;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture backGround;
	private Skin button;
	private ImageButton startButton;
	private Stage stage;
	private GameLoop gameLoop;
	private Music music;
	private BitmapFont font ;

	@Override
	public void create () {
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
		font = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"), false);
		music.setLooping(true);
		stage = new Stage(new ScreenViewport());
		gameLoop = new GameLoop(stage, Constants.startFrequency, Constants.increaseSpeedTime);
		Gdx.input.setInputProcessor(stage);
		batch = new SpriteBatch();
		backGround = new Texture("title_screen.png");
		button =  new Skin();
		button.add("button", new Texture("start.png"));
		startButton =  new ImageButton(button.getDrawable("button"));
		startButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("start.png")));
		startButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("start_hover.png"))));
		startButton.setSize(220, 80);
		startButton.setPosition(300,125);
		startButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int buttons) {
				Constants.goodSounds.get(0).play();
				gameLoop.start();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
				return true;
			}
		});
		stage.addActor(startButton);
		music.setVolume(0.5f);
		music.play();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		if(gameLoop.isRunning.get()){
			gameLoop.nextFrame();
		}else {
			batch.begin();
			batch.draw(backGround, 0, 0, Constants.SizeX, Constants.SizeY);
			font.getData().setScale(0.8f);
			int highscore = Constants.prefs.getInteger("highscore", 0);
			font.draw(batch, "HIGH SCORE:  " + highscore, 310, 100);
			Constants.prefs.putInteger("highscore", highscore);
			batch.end();
			stage.act();
			stage.draw();
		}
	}
	
	@Override
	public void dispose () {
		stage.dispose();
		batch.dispose();
		backGround.dispose();
		button.dispose();
		gameLoop.dispose();
	}
}
