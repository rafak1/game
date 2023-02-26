package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.game.utilities.GameTexture;

public class FallingEntity extends Entity {
    public boolean isFlower = false;
    public FallingEntity(GameTexture texture, int x, int y, boolean isFlower){
        super(texture, x ,y);
        this.isFlower = isFlower;
    }

    @Override
    public void move(int x, int y) {
        setY(getY());
    }


    public GameTexture getGameTexture(){
        return textures.get(0);
    }
}
