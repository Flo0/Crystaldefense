package com.gestankbratwurst.crystaldefense.gamescreen.utils;

import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 11.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class MutableFloat {

  public MutableFloat(float value) {
    this.floatValue = value;
  }

  @Getter
  private float floatValue;

  public void increment() {
    floatValue++;
  }

  public void decrement() {
    floatValue--;
  }

  public void add(float value) {
    floatValue += value;
  }

  public void subtract(float value) {
    floatValue -= value;
  }

  public void multiply(float value) {
    floatValue *= value;
  }

  public void divide(float value) {
    floatValue /= value;
  }

}
