package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static Preferences prefs = Gdx.app.getPreferences("Kwiaty_Natlii_HighScore");
    static public int SizeX = 800;
    static public int SizeY = 600;
    static public int NataliaWidth = 111;
    static public int NataliaHeight = 321;
    static public int RafauWidth = 128;
    static public int RafauHeight = 84;

    static public int flowerPickUpHalfWidth = 50;
    static public int flowerPickUpHeight = 100;

    static public int badPickUpHalfWidth = 35;
    static public int badPickUpHeight = 80;


    static public int increaseSpeedTime = 1000;
    static public int startFrequency = 100;
    static public int loseScreenTime = 320;

    static ArrayList<Music> goodSounds;
    static ArrayList<Music> badSounds;
    static {
        goodSounds = new ArrayList<>(Arrays.asList(Gdx.audio.newMusic(Gdx.files.internal("sounds/good_0.mp3")),Gdx.audio.newMusic(Gdx.files.internal("sounds/good_1.mp3")),Gdx.audio.newMusic(Gdx.files.internal("sounds/good_2.mp3"))));
        badSounds = new ArrayList<>(Arrays.asList(Gdx.audio.newMusic(Gdx.files.internal("sounds/bad_0.mp3")),Gdx.audio.newMusic(Gdx.files.internal("sounds/bad_1.mp3")),Gdx.audio.newMusic(Gdx.files.internal("sounds/bad_2.mp3"))));
        for (Music a: goodSounds) {
            a.setVolume(1f);
        }
        for (Music a: badSounds) {
            a.setVolume(1f);
        }
    }

}
