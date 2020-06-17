package com.gestankbratwurst.crystaldefense.gamescreen.game.logic;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface ITickable {

  void tick(float delta);

  default boolean reschedule() {
    return false;
  }

}
