package com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr;

import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 17.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class Mine extends Machine {

  public Mine(int maxHP, MachineType machineType) {
    super(maxHP, machineType);
  }

  public abstract float getBaseDigRate();
  public abstract float getCurrentDigRate();
  public abstract void buffDigRate(float value);
  public abstract void debuffDigRate(float value);

}
