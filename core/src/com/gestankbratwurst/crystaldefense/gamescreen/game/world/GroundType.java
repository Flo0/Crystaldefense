package com.gestankbratwurst.crystaldefense.gamescreen.game.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.awt.Color;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */

public enum GroundType {

  GRASS("tiles/grass_tile", false, false, false, new Color(0, 190, 0), "Grass"),
  STONE("tiles/cobble_tile", false, false, false, new Color(120, 120, 120), "Stone"),
  BRICKS("tiles/brick_tile", false, false, false, new Color(95, 95, 95), "Bricks"),
  DRY("tiles/dry_tile", false, false, false, new Color(191, 138, 16), "Dry"),
  STEEL("tiles/steel_tile", false, false, false, new Color(215, 215, 215), "Steel"),
  STONE_WALL("tiles/stone_wall", false, true, false, new Color(215, 215, 215), "Stone Wall"),
  GRANITE_WALL("tiles/granite_wall", false, true, false, new Color(215, 215, 215), "Granite Wall");

  public static void init(TextureAtlas tileAtlas) {
    for (GroundType groundType : values()) {
      if (groundType.animated) {
        groundType.animation = new Animation<>(0.25F,
            tileAtlas.findRegions(groundType.textureName), PlayMode.LOOP);
      } else {
        groundType.sprite = tileAtlas.createSprite(groundType.textureName);
      }
    }
  }

  GroundType(String textureName, boolean animated, boolean elevated, boolean liquid, Color mapColor, String display) {
    this.textureName = textureName;
    this.animated = animated;
    this.mapColor = mapColor;
    this.elevated = elevated;
    this.liquid = liquid;
    this.displayName = display;
  }

  @Getter
  private final Color mapColor;
  @Getter
  private final boolean elevated;
  @Getter
  private final boolean liquid;
  private final String textureName;
  private final boolean animated;
  @Getter
  private final String displayName;
  private Animation<TextureRegion> animation = null;
  private Sprite sprite = null;
  private float stateTime = 0F;

  public void tickAnimation(float delta) {
    if (animated) {
      stateTime += delta;
    }
  }

  public void draw(Vector2 position, SpriteBatch batch) {
    if (animated) {
      batch.draw(animation.getKeyFrame(stateTime), position.x, position.y);
    } else {
      batch.draw(sprite, position.x, position.y);
    }
  }

  public void draw(Vector2 position, SpriteBatch batch, float width, float height) {
    if (animated) {
      batch.draw(animation.getKeyFrame(stateTime), position.x, position.y, width, height);
    } else {
      batch.draw(sprite, position.x, position.y, width, height);
    }
  }

}
