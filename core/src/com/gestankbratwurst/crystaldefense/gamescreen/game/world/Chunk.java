package com.gestankbratwurst.crystaldefense.gamescreen.game.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class Chunk implements IDrawable {

  public Chunk(int x, int y, World world) {
    this.tiles = new Tile[GameConstants.CHUNK_SIZE][GameConstants.CHUNK_SIZE];
    this.x = x;
    this.y = y;
    this.world = world;
  }

  @Getter
  private final World world;
  private final int x;
  private final int y;
  private final Tile[][] tiles;

  public Tile getTile(int cx, int cy) {
    if (cx < 0 || cx >= GameConstants.CHUNK_SIZE || cy < 0 || cy >= GameConstants.CHUNK_SIZE) {
      return null;
    }
    return tiles[cx][cy];
  }

  public void setTile(int inChunkX, int inChunkY, Tile tile) {
    if (inChunkX < 0 || inChunkX >= GameConstants.CHUNK_SIZE || inChunkY < 0 || inChunkY >= GameConstants.CHUNK_SIZE) {
      throw new IllegalArgumentException("Chunk out of bounds.");
    }
    tiles[inChunkX][inChunkY] = tile;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    for (Tile[] line : tiles) {
      for (Tile tile : line) {
        tile.draw(delta, batch);
      }
    }
  }

  @Override
  public int getPriority() {
    return 2;
  }

}
