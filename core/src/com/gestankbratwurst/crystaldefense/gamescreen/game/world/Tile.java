package com.gestankbratwurst.crystaldefense.gamescreen.game.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr.Machine;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import java.util.ArrayList;
import java.util.List;
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
public class Tile implements IDrawable, Informational {

  public Tile(GroundType groundType, int x, int y, Chunk chunk) {
    this.groundType = groundType;
    this.position = new Vector2(x * GameConstants.TILE_SIZE, y * GameConstants.TILE_SIZE);
    this.x = x;
    this.y = y;
    this.chunk = chunk;
  }

  public final int x;
  public final int y;
  @Getter
  private final GroundType groundType;
  @Getter
  private final Vector2 position;
  @Getter
  private final Chunk chunk;
  @Getter
  @Setter
  private Machine currentMachine = null;
  @Getter
  @Setter
  private ResourceDomain resourceDomain = null;

  public Tile getRelative(int dx, int dy) {
    return chunk.getWorld().getTile(x + dx, y + dy);
  }

  public List<Tile> getNearbyTiles(int radius) {
    List<Tile> tiles = new ArrayList<>();
    World world = chunk.getWorld();
    for (int cx = -radius; cx <= radius; cx++) {
      for (int cy = -radius; cy <= radius; cy++) {
        Tile tile = world.getTile(cx + x, cy + y);
        if (tile != null) {
          tiles.add(tile);
        }
      }
    }
    return tiles;
  }

  @Override
  public String getDisplay() {
    return "Tile: " + groundType.getDisplayName();
  }

  @Override
  public ArrayList<String> getDescription() {
    if (resourceDomain == null && currentMachine == null) {
      return null;
    }
    ArrayList<String> lines = new ArrayList<>();

    if (currentMachine != null) {
      lines.add("");
      lines.add(currentMachine.getDisplay());
      lines.addAll(currentMachine.getDescription());
    }

    if (resourceDomain != null) {
      lines.add("");
      lines.add(resourceDomain.getDisplay());
      lines.addAll(resourceDomain.getDescription());
    }

    return lines;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    groundType.draw(position, batch, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
    if (resourceDomain != null && !resourceDomain.isEmpty()) {
      resourceDomain.getResourceType().draw(position, batch, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
    }
    if (currentMachine != null) {
      currentMachine.draw(delta, batch);
    }
  }

  @Override
  public int getPriority() {
    return 2;
  }

}
