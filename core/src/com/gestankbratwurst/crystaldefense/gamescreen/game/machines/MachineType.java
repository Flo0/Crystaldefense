package com.gestankbratwurst.crystaldefense.gamescreen.game.machines;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr.Machine;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.impl.BasicMine;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Supplier;
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
public enum MachineType implements Informational {

  CANON(Color.DARK_GRAY, "machines/canon", MachineClass.BASIC_TURRET, () -> null, "Simple Canon"),
  MINE(Color.DARK_GRAY, "machines/mine", MachineClass.BASIC_MINE, BasicMine::new, "Basic Mine"),
  STEEL_FACTORY(Color.DARK_GRAY, "machines/steel_factory", MachineClass.BASIC_FACTORY, () -> null, "Steel Factory");

  MachineType(Color mapColor, String textureName, MachineClass machineClass, Supplier<Machine> supplier, String display) {
    this.textureName = textureName;
    this.mapColor = mapColor;
    this.machineClass = machineClass;
    this.instanceSupplier = supplier;
    this.displayName = display;
  }

  public static void init(TextureAtlas tileAtlas) {
    for (MachineType machineType : values()) {
      machineType.sprite = tileAtlas.createSprite(machineType.textureName);
    }
  }

  @Getter
  private final Color mapColor;
  @Getter
  private final MachineClass machineClass;
  private final Supplier<Machine> instanceSupplier;
  private final String textureName;
  private final String displayName;
  private Sprite sprite;

  public Machine supply() {
    return instanceSupplier.get();
  }

  public void draw(Vector2 position, SpriteBatch batch, float width, float height) {
    draw(position, batch, width, height, 1F);
  }

  public void draw(Vector2 position, SpriteBatch batch, float width, float height, float opac) {
    sprite.setPosition(position.x, position.y);
    sprite.setSize(width, height);
    sprite.setAlpha(opac);
    sprite.draw(batch);
  }

  @Override
  public String getDisplay() {
    return "Machine: " + displayName;
  }

  @Override
  public ArrayList<String> getDescription() {
    return null;
  }

}
