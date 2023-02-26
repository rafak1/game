package com.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.game.utilities.GameTexture;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    private AtomicInteger x;
    private AtomicInteger y;
    protected final ArrayList<GameTexture> textures;
    static private int textureSizeX;
    static private int textureSizeY;

    public Entity(String [] texturePath,int x, int y){
        textures = new ArrayList<>();
        for ( String a : texturePath) {
            textures.add(new GameTexture(new Texture(a), 0, 0));
        }
        this.x = new AtomicInteger(x);
        this.y = new AtomicInteger(y);
    }

    public Entity(GameTexture texture, int x, int y){
        this.x = new AtomicInteger(x);
        this.y = new AtomicInteger(y);
        textures = new ArrayList<>();
        textures.add(texture);
    }

    public abstract void move(int x, int y);


    public int getX() {
        return x.get();
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public void dispose(){
        for( GameTexture a : textures ){
            a.texture.dispose();
        }
    }

    public Texture getTexture(){
        return textures.get(0).texture;
    }
}
