package com.gestankbratwurst.crystaldefense.gamescreen.game.machines.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr.Mine;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceDomain;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import java.util.ArrayList;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 16.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BasicMine extends Mine {

  public BasicMine() {
    super(75, MachineType.MINE);
    this.area = GameManager.getInstance().getMainAtlas().createSprite("areas/3x3");
    this.sprite = GameManager.getInstance().getMainAtlas().createSprite("machines/mine");
    this.areaRed = GameManager.getInstance().getMainAtlas().createSprite("areas/3x3_red");
    this.costs.put(ResourceType.IRON, 50);
    this.player = GameManager.getInstance().player;
  }

  private Sprite sprite;
  private Sprite area;
  private Sprite areaRed;
  private final float baseDigRate = 0.02F / GameConstants.TPS;
  private float digRate = baseDigRate;
  private boolean placed = false;
  private Tile placedTile = null;
  private final Player player;

  @Override
  public boolean canBePlaced(Tile tile) {
    if (tile.getCurrentMachine() != null) {
      return false;
    }
    return !tile.getGroundType().isElevated() && !tile.getGroundType().isLiquid();
  }

  @Override
  public void onHoverDraw(float delta, SpriteBatch batch, Tile hoverTile) {
    Vector2 pos = new Vector2(hoverTile.getPosition().x - GameConstants.TILE_SIZE, hoverTile.getPosition().y - GameConstants.TILE_SIZE);
    boolean canPlace = canBePlaced(hoverTile) && hasResources(player);
    if (canPlace) {
      area.setPosition(pos.x, pos.y);
      area.draw(batch);
    } else {
      areaRed.setPosition(pos.x, pos.y);
      areaRed.draw(batch);
    }
    machineType.draw(hoverTile.getPosition(), batch, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, 0.66F);
  }

  @Override
  public void place(Tile tile) {
    tile.setCurrentMachine(this);
    this.placedTile = tile;
    placed = true;
    area = null;
    areaRed = null;
    registerTickable();
  }

  @Override
  public boolean isPlaced() {
    return placed;
  }

  @Override
  public String getDisplay() {
    return "Basic Mine";
  }

  @Override
  public ArrayList<String> getDescription() {
    ArrayList<String> list = new ArrayList<>();
    list.add("Produces 0.02 Ore/s");
    list.add("per vein.");
    return list;
  }

  @Override
  public void tick(float delta) {
    if (!placed) {
      return;
    }
    for (Tile tile : placedTile.getNearbyTiles(1)) {
      ResourceDomain resourceDomain = tile.getResourceDomain();
      if (resourceDomain != null && !resourceDomain.isEmpty()) {
        resourceDomain.subtract(digRate);
        player.addResource(resourceDomain.getResourceType(), digRate);
      }
    }
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    sprite.setPosition(placedTile.getPosition().x, placedTile.getPosition().y);
    sprite.setSize(GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
    sprite.draw(batch);
  }

  @Override
  public int getPriority() {
    return 5;
  }

  @Override
  public float getBaseDigRate() {
    return baseDigRate;
  }

  @Override
  public float getCurrentDigRate() {
    return digRate;
  }

  @Override
  public void buffDigRate(float value) {
    digRate += value;
  }

  @Override
  public void debuffDigRate(float value) {
    digRate -= value;
  }

}
