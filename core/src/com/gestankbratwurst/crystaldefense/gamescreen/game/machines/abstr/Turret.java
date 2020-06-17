package com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Turret extends Machine {

  public Turret(int maxHP, MachineType machineType, float range) {
    super(maxHP, machineType);
    this.baseRange = range;
    this.range = range;

  }

  @Getter
  @Setter
  private float range;
  @Getter
  private final float baseRange;

  public void displayRange(Tile tile, boolean bright, ShapeRenderer renderer) {
    float midX = tile.x - GameConstants.TILE_SIZE / 2F;
    float midY = tile.y - GameConstants.TILE_SIZE / 2F;

    Color color = new Color(bright ? 0.8F : 0.5F, 0.8F, 0.8F, 0.8F);

    renderer.setColor(color);
    renderer.circle(midX, midY, range, 20);
  }

}
