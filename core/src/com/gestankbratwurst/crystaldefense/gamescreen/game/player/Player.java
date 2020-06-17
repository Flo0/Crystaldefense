package com.gestankbratwurst.crystaldefense.gamescreen.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.ITickable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.abstr.Machine;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.Tile;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.World;
import com.gestankbratwurst.crystaldefense.gamescreen.utils.MutableFloat;
import java.util.EnumMap;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class Player implements ITickable, IDrawable {

  public Player(OrthographicCamera currentCam, GameManager gameManager) {
    this.currentCam = currentCam;
    this.velocity = new Vector2();
    this.acceleration = new Vector2();
    this.pos = new Vector2(currentCam.position.x, currentCam.position.y);
    this.resources = new EnumMap<>(ResourceType.class);
    this.maxResources = new EnumMap<>(ResourceType.class);
    for (ResourceType resourceType : ResourceType.values()) {
      resources.put(resourceType, new MutableFloat(0F));
      maxResources.put(resourceType, new MutableFloat(0F));
    }
    TextureAtlas atlas = gameManager.getAssetManager().get("atlas/combined.atlas", TextureAtlas.class);
    this.hoverSprite = atlas.createSprite("tiles/hover");
    this.selectSprite = atlas.createSprite("tiles/select");
    this.maxHp = 250;
    this.hp = 250;
    this.addResource(ResourceType.IRON, 200);
  }

  public World currentWorld;
  public OrthographicCamera currentCam;
  public Vector2 pos;
  public Vector2 velocity;
  public Vector2 acceleration;
  public float maxSpeed = 1250F;
  public int viewDistance = 6;
  public Tile selectedTile = null;
  public Tile hoveringTile = null;
  private final Sprite hoverSprite;
  private final Sprite selectSprite;
  @Getter
  @Setter
  private int hp;
  @Getter
  @Setter
  private int maxHp;
  @Getter
  @Setter
  private Machine selectedMachine = null;
  @Getter
  private Informational currentInformational = null;

  public void onClick(int x, int y, int button) {
    if (button == 0) {
      if (selectedMachine != null && hoveringTile != null) {
        if (selectedMachine.canBePlaced(hoveringTile) && selectedMachine.hasResources(this)) {
          selectedMachine.removeCosts(this);
          selectedMachine.place(hoveringTile);
          selectedMachine = selectedMachine.getMachineType().supply();
          if (!selectedMachine.hasResources(this)) {
            selectedMachine = null;
          }
        }
      } else {
        selectedTile = hoveringTile;
      }
    } else {
      selectedMachine = null;
      selectedTile = null;
    }
  }

  public boolean hasResource(ResourceType type, float amount) {
    return getResource(type) >= amount;
  }

  public void setCurrentInformational(Informational info) {
    if (currentInformational == selectedTile) {
      selectedTile = null;
    }
    currentInformational = info;
  }

  private final EnumMap<ResourceType, MutableFloat> resources;
  private final EnumMap<ResourceType, MutableFloat> maxResources;

  public float getHpFill() {
    return 1F / maxHp * hp;
  }

  public float getResource(ResourceType type) {
    return resources.get(type).getFloatValue();
  }

  public float getResourceLimit(ResourceType type) {
    return maxResources.get(type).getFloatValue();
  }

  public void addResource(ResourceType type, float amount) {
    resources.get(type).add(amount);
  }

  public void addResourceLimit(ResourceType type, float amount) {
    maxResources.get(type).add(amount);
  }

  public void accelerate(Vector2 acceleration, float delta) {
    velocity.x += acceleration.x * delta;
    velocity.y += acceleration.y * delta;
    if (velocity.len2() > maxSpeed * maxSpeed) {
      velocity.setLength(maxSpeed);
    }
  }

  public void translate(Vector2 velocity, float delta) {
    pos.x += velocity.x * delta;
    pos.y += velocity.y * delta;
  }

  public void teleport(float x, float y) {
    pos.x = x;
    pos.y = y;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    accelerate(this.acceleration, delta);
    translate(this.velocity, delta);
    if (acceleration.x == 0F && acceleration.y == 0F) {
      velocity.scl(1F - delta * 10F);
      if (velocity.len2() <= 500F) {
        velocity.setLength(0F);
      }
    }
    currentCam.update();

//    if (hoveringTile != null) {
//      Vector2 tpos = hoveringTile.getPosition();
//      batch.draw(hoverSprite, tpos.x, tpos.y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
//    }

    if (selectedTile != null) {
      Vector2 tpos = selectedTile.getPosition();
      batch.draw(hoverSprite, tpos.x, tpos.y, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE);
    }

    if (selectedMachine != null) {
      if (!selectedMachine.isPlaced()) {
        if (hoveringTile != null) {
          selectedMachine.onHoverDraw(delta, batch, hoveringTile);
        }
      }
    }
  }

  @Override
  public void tick(float delta) {
    Vector3 mousePos = currentCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

    int screenX = (int) mousePos.x;
    int screenY = (int) mousePos.y;

    hoveringTile = currentWorld.getTileAtPos(screenX, screenY);

    if (selectedTile != null) {
      currentInformational = selectedTile;
    }
  }

  @Override
  public boolean reschedule() {
    return true;
  }

  @Override
  public int getPriority() {
    return 6;
  }

}