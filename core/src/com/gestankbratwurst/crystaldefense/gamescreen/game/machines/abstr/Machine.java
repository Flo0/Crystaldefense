package com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.ITickable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Machine implements ITickable, Informational, IDrawable {

  public Machine(int maxHP, MachineType machineType) {
    this.maxHP = maxHP;
    this.currentHP = maxHP;
    this.machineType = machineType;
    this.costs = new HashMap<>();
  }

  private final int maxHP;
  private int currentHP;
  @Getter
  protected final MachineType machineType;
  protected final Map<ResourceType, Integer> costs;

  public boolean hasResources(Player player) {
    for (ResourceType type : costs.keySet()) {
      if (player.getResource(type) < costs.get(type)) {
        return false;
      }
    }
    return true;
  }

  public void removeCosts(Player player) {
    for (Entry<ResourceType, Integer> entry : costs.entrySet()) {
      player.addResource(entry.getKey(), -entry.getValue());
    }
  }

  protected void registerTickable() {
    GameManager.getInstance().addTickable(this);
  }

  public boolean isDestroyed() {
    return currentHP <= 0;
  }

  public abstract boolean canBePlaced(Tile tile);

  public abstract void onHoverDraw(float delta, SpriteBatch batch, Tile hoverTile);

  public abstract void place(Tile tile);

  public abstract boolean isPlaced();

  @Override
  public boolean reschedule() {
    return !isDestroyed();
  }

}
