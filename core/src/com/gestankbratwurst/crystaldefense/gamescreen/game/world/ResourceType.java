package com.gestankbratwurst.crystaldefense.gamescreen.game.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
public enum ResourceType {

  IRON("resources/iron_ore", "resources/iron_ore", true, 0.865D, 0.05D, 1000F, 10000F, "Iron"),
  COAL("resources/coal", "resources/coal", true, 0.82D, 0.07D, 4000F, 65000F, "Coal"),
  STEEL("resources/steel", null, false, 0.9D, 2D, 0F, 0F, "Steel");

  public static void init(TextureAtlas textureAtlas) {
    for (ResourceType resourceType : values()) {
      resourceType.sprite = textureAtlas.createSprite(resourceType.textureName);
      if (resourceType.generated) {
        resourceType.tileSprite = textureAtlas.createSprite(resourceType.tileTextureName);
      }
    }
  }

  ResourceType(String textureName, String tileTextureName, boolean generated, double thresh, double stretch, float min, float max,
      String display) {
    this.textureName = textureName;
    this.tileTextureName = tileTextureName;
    this.generated = generated;
    this.baseThreshold = thresh;
    this.baseStretch = stretch;
    this.minValue = min;
    this.maxValue = max;
    this.displayName = display;
  }

  public void draw(Vector2 position, SpriteBatch batch, float width, float height) {
    batch.draw(tileSprite, position.x, position.y, width, height);
  }

  private final String textureName;
  private final String tileTextureName;
  @Getter
  private final boolean generated;
  @Getter
  private final double baseThreshold;
  @Getter
  private final double baseStretch;
  @Getter
  private final float minValue;
  @Getter
  private final float maxValue;
  @Getter
  private final String displayName;
  @Getter
  private Sprite tileSprite;
  @Getter
  private Sprite sprite;


}
