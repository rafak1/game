package com.game.entities;

import com.badlogic.gdx.graphics.Texture;

public class Natalia extends Entity{
    public int speed;
    public int flowers;
    public int lives;

    public Natalia(int x, int y){
        super(new String[]{"natalia_0.png", "natalia_1.png", "natalia_2.png", "natalia_3.png"}, x, y);
        speed = 5;
        lives = 3;
    }

    @Override
    public void move(int x, int y) {

    }

    @Override
    public Texture getTexture() {
        if (flowers <= 10) {
            return textures.get(0).texture;
        } else if (flowers <= 20) {
            return textures.get(1).texture;
        } else if (flowers <= 30) {
            return textures.get(2).texture;
        } else {
            return textures.get(3).texture;
        }
    }
}
