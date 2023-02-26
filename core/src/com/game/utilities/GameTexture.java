package com.game.utilities;

import com.badlogic.gdx.graphics.Texture;

public class GameTexture {
    public Texture texture;
    public int width;
    public int height;

    public GameTexture(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }
}
