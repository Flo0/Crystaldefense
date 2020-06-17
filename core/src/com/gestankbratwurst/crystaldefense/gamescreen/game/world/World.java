package com.gestankbratwurst.crystaldefense.gamescreen.game.world;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class World implements IDrawable {

  public World(int horizChunks, int vertChunks) {
    this.chunks = new Chunk[horizChunks][vertChunks];
    this.width = horizChunks;
    this.height = vertChunks;
  }

  private final int width;
  private final int height;
  private final Chunk[][] chunks;
  @Getter
  private Player player = null;

  public void setPlayer(Player player) {
    this.player = player;
    player.currentWorld = this;
  }

  public Tile getTileAtPos(int x, int y) {
    Tile tile = null;
    float dm = GameConstants.CHUNK_SIZE * GameConstants.TILE_SIZE;
    Chunk chunk = getChunk((int) (x / dm), (int) (y / dm));
    if (chunk != null) {
      tile = chunk.getTile((int) ((x % (int) dm) / GameConstants.TILE_SIZE), (int) ((y % (int) dm) / GameConstants.TILE_SIZE));
    }
    return tile;
  }

  public Tile getTile(int x, int y) {
    Tile tile = null;
    float dm = GameConstants.CHUNK_SIZE;
    Chunk chunk = getChunk((int) (x / dm), (int) (y / dm));
    if (chunk != null) {
      tile = chunk.getTile(((x % (int) dm)), ((y % (int) dm)));
    }
    return tile;
  }

  public void setChunk(int chunkX, int chunkY, Chunk chunk) {
    if (chunkX < 0 || chunkX >= width || chunkY < 0 || chunkY >= height) {
      throw new IllegalArgumentException("Chunk out of bounds");
    }
    chunks[chunkX][chunkY] = chunk;
  }

  public Chunk getChunk(int chunkX, int chunkY) {
    if (chunkX < 0 || chunkX >= width || chunkY < 0 || chunkY >= height) {
      return null;
    }
    return chunks[chunkX][chunkY];
  }

  private void renderWorld(Player player, float delta, SpriteBatch batch) {
    int midX = (int) (player.pos.x / (
        com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants.CHUNK_SIZE
            * com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants.TILE_SIZE));
    int midY = (int) (player.pos.y / (com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants.CHUNK_SIZE
        * GameConstants.TILE_SIZE));
    int dxy = player.viewDistance;
    for (int x = midX - dxy; x <= midX + dxy; x++) {
      for (int y = midY - dxy; y <= midY + dxy; y++) {
        Chunk chunk = getChunk(x, y);
        if (chunk != null) {
          chunk.draw(delta, batch);
        }
      }
    }
  }


  @Override
  public void draw(float delta, SpriteBatch batch) {
    if (player != null) {
      this.renderWorld(player, delta, batch);
    }
  }

  @Override
  public boolean reschedule() {
    return true;
  }

  @Override
  public int getPriority() {
    return 2;
  }

}
