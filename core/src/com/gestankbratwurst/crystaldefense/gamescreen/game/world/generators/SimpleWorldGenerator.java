package com.gestankbratwurst.crystaldefense.gamescreen.game.world.generators;

import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Chunk;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.GroundType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceDomain;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.World;
import com.gestankbratwurst.crystaldefense.gamescreen.utils.SimplexNoise;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
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
public class SimpleWorldGenerator extends WorldGenerator {

  public SimpleWorldGenerator() {
    this.resourceNoises = new HashMap<>();
    for (ResourceType type : ResourceType.values()) {
      if (type.isGenerated()) {
        resourceNoises.put(type, new SimplexNoise(ThreadLocalRandom.current().nextInt()));
      }
    }
  }

  private final SimplexNoise noise = new SimplexNoise(100);

  private World renderWorld;
  private long renderSeed;
  @Getter
  private int chunkAmount;
  @Getter
  private int chunksDone;
  private int maxX;
  private int maxY;
  private int currentX;
  private int currentY;
  private final Map<ResourceType, SimplexNoise> resourceNoises;

  @Override
  public boolean advanceRender() {
    renderWorld.setChunk(currentX, currentY, generateChunk(renderSeed, currentX, currentY, renderWorld));
    if (++currentX == maxX) {
      currentX = 0;
      if (currentY == maxY) {
        throw new IllegalStateException("Render gone too far.");
      }
      currentY++;
    }
    return ++chunksDone == chunkAmount;
  }

  @Override
  public float getProgress() {
    return 1F / chunkAmount * chunksDone;
  }

  @Override
  public World getRenderedWorld() {
    return renderWorld;
  }

  @Override
  public void startRender(long seed, int chunkWidth, int chunkHeight) {
    this.renderWorld = new World(chunkWidth, chunkHeight);
    this.chunkAmount = chunkWidth * chunkHeight;
    this.maxX = chunkWidth;
    this.maxY = chunkHeight;
    this.currentX = 0;
    this.currentY = 0;
    this.renderSeed = seed;
  }

  @Override
  public World generateWorld(long seed, int width, int height) {
    World world = new World(width, height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        Chunk chunk = generateChunk(seed, x, y, world);
        world.setChunk(x, y, chunk);
      }
    }
    return world;
  }

  @Override
  public Chunk generateChunk(long seed, int chunkX, int chunkY, World world) {
    Chunk chunk = new Chunk(chunkX, chunkY, world);
    for (int x = 0; x < GameConstants.CHUNK_SIZE; x++) {
      for (int y = 0; y < GameConstants.CHUNK_SIZE; y++) {
        Tile tile = generateTile(seed, chunkX * GameConstants.CHUNK_SIZE + x, chunkY * GameConstants.CHUNK_SIZE + y, chunk);
        chunk.setTile(x, y, tile);
      }
    }
    return chunk;
  }

  @Override
  public Tile generateTile(long seed, int tileX, int tileY, Chunk chunk) {
    GroundType type = GroundType.GRASS;
    double ns = noise.noise(tileX / 20D, tileY / 20D);
    if (ns < -0.85) {
      type = GroundType.STONE;
    }
    if (ns > 0.4 && ns < 0.7) {
      type = GroundType.STONE_WALL;
    } else if (ns >= 0.7) {
      type = GroundType.GRANITE_WALL;
    }

    Tile tile = new Tile(type, tileX, tileY, chunk);

    if (!type.isElevated() && !type.isLiquid()) {
      for (Entry<ResourceType, SimplexNoise> entry : resourceNoises.entrySet()) {
        ResourceType rType = entry.getKey();
        double resNoiseVal = entry.getValue().noise(tileX * rType.getBaseStretch(), tileY * rType.getBaseStretch());
        if (resNoiseVal >= rType.getBaseThreshold()) {
          float resourceRange = rType.getMaxValue() - rType.getMinValue();
          double resourceDelta = 1D - rType.getBaseThreshold();
          double addUp = 1D / resourceDelta * (resNoiseVal - rType.getBaseThreshold());
          float amount = rType.getMinValue() + (float) (addUp * resourceRange);

          ResourceDomain domain = new ResourceDomain(rType, amount);
          tile.setResourceDomain(domain);
        }
      }
    }

    return tile;
  }
}
