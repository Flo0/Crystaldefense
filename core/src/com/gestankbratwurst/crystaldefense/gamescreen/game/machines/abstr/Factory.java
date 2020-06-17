package com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr;

import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Factory extends Machine {

  public Factory(int maxHP, MachineType machineType) {
    super(maxHP, machineType);
    this.productionMap = new HashMap<>();
    this.consumtionMap = new HashMap<>();
    this.player = GameManager.getInstance().player;
  }

  private boolean producing = false;
  private final Map<ResourceType, Float> productionMap;
  private final Map<ResourceType, Float> consumtionMap;
  private final Player player;

  @Override
  public void tick(float delta) {
    for (Entry<ResourceType, Float> entry : consumtionMap.entrySet()) {
      if (player.getResource(entry.getKey()) < entry.getValue()) {
        producing = false;
        return;
      }
    }

    producing = true;

    for (Entry<ResourceType, Float> entry : consumtionMap.entrySet()) {
      if (player.getResource(entry.getKey()) < entry.getValue()) {
        player.addResource(entry.getKey(), -entry.getValue());
      }
    }

    for (Entry<ResourceType, Float> entry : productionMap.entrySet()) {
      if (player.getResource(entry.getKey()) < entry.getValue()) {
        player.addResource(entry.getKey(), entry.getValue());
      }
    }
  }

  protected abstract void postTick(float delta);

}
