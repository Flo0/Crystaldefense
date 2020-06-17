package com.gestankbratwurst.crystaldefense.gamescreen.game.logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface IDrawable extends Comparable<IDrawable> {

  void draw(float delta, SpriteBatch batch);

  default boolean reschedule() {
    return true;
  }

  int getPriority();

  default int compareTo(IDrawable other) {
    return getPriority() - other.getPriority();
  }

}
