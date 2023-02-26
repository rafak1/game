package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.entities.FallingEntity;
import com.game.entities.Natalia;
import com.game.entities.Rafau;
import com.game.utilities.GameTexture;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameLoop {
    private Stage stage;
    public AtomicBoolean isRunning;
    private Natalia natalia;
    private Rafau rafau;
    private ArrayList<FallingEntity> fallingEntities;
    private ArrayList<GameTexture> flowerTextures;
    private ArrayList<GameTexture> badTextures;
    private int intervalTime;
    private  int increaseSpeedTime;
    private int counter;
    private int increaseSpeedCounter;
    private SpriteBatch batch;
    Animation<TextureRegion> background;
    float elapsed;
    private boolean flipNatalia = false;
    Random rand = new Random();
    private int highScore;
    private int highScoreThisRound;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"), false);
    private Texture heart;
    private boolean displayLostScreen;
    private int loseScreenCount;

    private int fallingSpeed = 2;


    public GameLoop(Stage stage, int startIntervalTime, int increaseSpeedTime){
        this.intervalTime = startIntervalTime;
        this.increaseSpeedTime = increaseSpeedTime;
        this.stage = stage;
        font.getData().setScale(0.7f);
        isRunning = new AtomicBoolean(false);
        background = com.holidaystudios.tools.GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("background.gif").read());

        heart = new Texture("heart.png");

        flowerTextures = new ArrayList<>(Arrays.asList(new GameTexture(new Texture("flower_3.png"),128,18),new GameTexture(new Texture("flower_1.png"),128,18),new GameTexture(new Texture("flower_2.png"),128,18),new GameTexture(new Texture("flower_4.png"),128,18)));
        badTextures = new ArrayList<>(Arrays.asList(new GameTexture(new Texture("bad_3.png"),100,33),new GameTexture(new Texture("bad_1.png"),80,80),new GameTexture(new Texture("bad_2.png"),85,44),new GameTexture(new Texture("bad_4.png"),90,80)));
    }

    public void start(){
        batch = new SpriteBatch();
        fallingSpeed = 2;
        natalia = new Natalia(400 - Constants.NataliaWidth/2,0);
        rafau = new Rafau(400 - Constants.RafauWidth/2 ,600 - Constants.RafauHeight);
        fallingEntities = new ArrayList<>();
        highScoreThisRound = -1;
        highScore = Constants.prefs.getInteger("highscore", 0);
        isRunning.set(true);
        counter = 0;
        increaseSpeedCounter = 0;
        displayLostScreen = false;
    }

    public void nextFrame(){
        drawFrame();
        if(!displayLostScreen) {
            handleControls();
            moveEntities();

            counter++;
            increaseSpeedCounter++;
            if (counter == intervalTime) {
                spawnFallingEntity();
                counter = 0;
            }
            if (increaseSpeedCounter == increaseSpeedTime) {
                fallingSpeed += 1;
                natalia.speed += 1;
                increaseSpeedCounter = 0;
            }
        }
    }

    public void stop(){
        Constants.prefs.putInteger("highscore", highScore);
        isRunning.set(false);
    }

    public void spawnFallingEntity(){
        int isBad = rand.nextInt(3);
        int textureNumber = rand.nextInt(4);
        int position = rand.nextInt(800 - 2*Constants.RafauWidth) + Constants.RafauWidth;
        if(isBad == 0){
            fallingEntities.add(new FallingEntity(badTextures.get(textureNumber), position,600 - Constants.RafauHeight, false));
        }else{
            fallingEntities.add(new FallingEntity(flowerTextures.get(textureNumber), position,600 - Constants.RafauHeight , true));
        }
        rafau.setX(position);
    }

    public void moveEntities(){
        ArrayList<FallingEntity> toRemove = new ArrayList<>();
        int nataliaMiddle = natalia.getX() + Constants.NataliaWidth/2;
        for (FallingEntity a: fallingEntities) {
            if(a.getY() < 0 ) {
                if(a.isFlower) pickUpEntity(-1);
                toRemove.add(a);
            }
            a.setY(a.getY() - fallingSpeed);
            int aMiddle = a.getX() + a.getGameTexture().width/2;
            if(a.isFlower){
                if(a.getY() <= Constants.NataliaHeight && a.getY() >= Constants.NataliaHeight - Constants.flowerPickUpHeight){
                    if(aMiddle >= nataliaMiddle - Constants.flowerPickUpHalfWidth && aMiddle <= nataliaMiddle + Constants.flowerPickUpHalfWidth){
                        pickUpEntity(1);
                        toRemove.add(a);
                    }
                }
            }else{
                if(a.getY() <= Constants.NataliaHeight && a.getY() >= Constants.NataliaHeight - Constants.badPickUpHeight){
                    if(aMiddle >= nataliaMiddle - Constants.badPickUpHalfWidth && aMiddle <= nataliaMiddle + Constants.badPickUpHalfWidth){
                        pickUpEntity(-10);
                        toRemove.add(a);
                    }
                }
            }
        }
        for(FallingEntity a : toRemove){
            fallingEntities.remove(a);
        }
    }

    public void pickUpEntity(int a){
        natalia.flowers += a;
        if(natalia.flowers <=0) natalia.flowers =0;
        if(a < 0){
            Constants.badSounds.get(rand.nextInt(3)).play();
            if(natalia.lives == 1){
                displayLostScreen = true;
                loseScreenCount = 0;
                return;
            }
            natalia.lives --;
        }else{
            Constants.goodSounds.get(rand.nextInt(3)).play();
        }
    }


    public void drawFrame(){
        elapsed += Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(background.getKeyFrame(elapsed), 0.0f, 0.0f, 800, 600);
        if(!displayLostScreen) {
            batch.draw(natalia.getTexture(), natalia.getX(), natalia.getY(), Constants.NataliaWidth, Constants.NataliaHeight, 0, 0, 32, 107, flipNatalia, false);
            batch.draw(rafau.getTexture(), rafau.getX(), rafau.getY(), Constants.RafauWidth, Constants.RafauHeight);
            font.getData().setScale(0.7f);
            font.draw(batch, "HIGH SCORE:  " + natalia.flowers, 10, Constants.SizeY - 10);
            highScore = Math.max(natalia.flowers, highScore);
            highScoreThisRound = Math.max(highScoreThisRound, natalia.flowers);
            font.draw(batch, "YOUR SCORE:  " + highScore, 10, Constants.SizeY - 30);

            for (FallingEntity a : fallingEntities) {
                batch.draw(a.getTexture(), a.getX(), a.getY(), a.getGameTexture().width, a.getGameTexture().height);
            }
            for (int i = 1; i <= natalia.lives; i++) {
                batch.draw(heart, Constants.SizeX - (i * 50), Constants.SizeY - 50, 42, 38);
            }
        }else{
            loseScreenCount++;
            if(loseScreenCount >= Constants.loseScreenTime){
                displayLostScreen = false;
                stop();
                batch.end();
                return;
            }
            font.getData().setScale(3);
            font.draw(batch, "YOU LOST", 50, Constants.SizeY-50);
            font.getData().setScale(2f);
            font.draw(batch, "YOUR SCORE:  " + highScoreThisRound, 50, Constants.SizeY/2 - 100);
            batch.draw(rafau.getTexture(), Constants.SizeX-Constants.RafauWidth*2-10, Constants.SizeY - Constants.RafauHeight*2, Constants.RafauWidth*2, Constants.RafauHeight*2);
            if(highScoreThisRound >= highScore) {
                font.getData().setScale(1.5f);
                font.draw(batch, "YOU SET A NEW HIGH SCORE !", 50, 100);
            }
        }

        batch.end();
    }

    public void handleControls(){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            if(natalia.getX() - natalia.speed > 0) {
                natalia.setX(natalia.getX() - natalia.speed);
            }
            flipNatalia = true;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(natalia.getX() + natalia.speed < Constants.SizeX - Constants.NataliaWidth) {
                natalia.setX(natalia.getX() + natalia.speed);
            }
            flipNatalia = false;
        }
    }


    public void dispose(){
        rafau.dispose();
        natalia.dispose();
        for( FallingEntity a : fallingEntities){
            a.dispose();
        }
    }
}
