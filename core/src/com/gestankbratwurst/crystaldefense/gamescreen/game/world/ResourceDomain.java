package com.gestankbratwurst.crystaldefense.gamescreen.game.world;

import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 15.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class ResourceDomain implements Informational {

  @Getter
  private final ResourceType resourceType;
  @Getter
  private float amount;

  public void add(float value) {
    amount += value;
  }

  public void subtract(float value) {
    amount -= value;
  }

  public boolean isEmpty() {
    return amount <= 0.01F;
  }

  @Override
  public String getDisplay() {
    return "Resource: " + resourceType.getDisplayName();
  }

  @Override
  public ArrayList<String> getDescription() {
    ArrayList<String> desc = new ArrayList<>();

    desc.add("Amount: " + (int) amount);

    return desc;
  }
}
