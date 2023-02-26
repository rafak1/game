package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.game.utilities.GameTexture;

public class Rafau extends Entity{
    public Rafau(int x, int y){
        super(new GameTexture(new Texture("rafał.png"),0,0), x,y );
    }

    @Override
    public void move(int x, int y) {
        setX(x);
    }
}
