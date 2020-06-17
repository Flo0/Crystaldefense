package com.gestankbratwurst.crystaldefense.gamescreen.game.world.generators;

import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Chunk;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class WorldGenerator {

  public WorldGenerator() {

  }

  public abstract int getChunkAmount();
  public abstract int getChunksDone();
  public abstract boolean advanceRender();
  public abstract float getProgress();
  public abstract World getRenderedWorld();
  public abstract void startRender(long seed, int chunkWidth, int chunkHeight);
  public abstract World generateWorld(long seed, int chunkWidth, int chunkHeight);
  public abstract Chunk generateChunk(long seed, int chunkX, int chunkY, World world);
  public abstract Tile generateTile(long seed, int tileX, int tileY, Chunk chunk);

}