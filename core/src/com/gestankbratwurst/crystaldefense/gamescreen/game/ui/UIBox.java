package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 12.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@Data
@AllArgsConstructor
public class UIBox {

  private int x;
  private int y;
  private int width;
  private int height;

  public void translate(int x, int y) {
    this.x += x;
    this.y += y;
  }

  public void move(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean contains(int cx, int cy) {
    return cx >= x && cx <= x + width && cy >= y && cy <= y + height;
  }

  @Override
  public String toString() {
    return x + "|" + y + "|" + width + "|" + height;
  }

}
