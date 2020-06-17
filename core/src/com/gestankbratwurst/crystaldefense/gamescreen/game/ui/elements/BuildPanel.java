package com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineClass;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.UIBox;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.UIElement;
import java.util.ArrayList;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 15.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class BuildPanel extends UIElement {

  public static BuildPanel construct(TextureAtlas atlas, Player player) {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    int kh = (int) (320 * h / GameConstants.VIEW_HEIGHT);
    UIBox uiBox = new UIBox(0, 0, (int) (240 * w / GameConstants.VIEW_WIDTH), kh);
    return new BuildPanel(uiBox, atlas, player);
  }

  private BuildPanel(UIBox uiBox, TextureAtlas atlas, Player player) {
    super(uiBox, atlas.createSprite("ui/build_menu_none"));
    this.player = player;
    this.canonTexture = atlas.findRegion("ui/build_menu_canons");
    this.mineTexture = atlas.findRegion("ui/build_menu_mines");
    this.factoryTexture = atlas.findRegion("ui/build_menu_factories");
    this.selectSprite = atlas.createSprite("tiles/select");
    this.itemTypes = new ArrayList<>();
  }

  private final Player player;
  private int selRow = -1;
  private int selColumn = -1;
  private int selIndex = -1;
  private final AtlasRegion canonTexture;
  private final AtlasRegion mineTexture;
  private final AtlasRegion factoryTexture;
  private final ArrayList<MachineType> itemTypes;
  private final Sprite selectSprite;
  private MachineClass nextClassMenu;
  private MachineClass selectedClassMenu;
  private boolean folded = false;

  private void updatePlayerInfoSelection() {
    if (selIndex > -1 && selIndex < itemTypes.size()) {
      player.setSelectedMachine(itemTypes.get(selIndex).supply());
      player.setCurrentInformational(itemTypes.get(selIndex));
    }
  }

  @Override
  protected void onHover() {

  }

  @Override
  protected void onUnHover() {

  }

  @Override
  protected void postDraw(float delta, SpriteBatch batch) {

    if (selectedClassMenu != nextClassMenu) {
      selectedClassMenu = nextClassMenu;

      itemTypes.clear();
      switch (selectedClassMenu) {
        case BASIC_TURRET:
          defaultSprite.setRegion(canonTexture);
          break;
        case BASIC_MINE:
          defaultSprite.setRegion(mineTexture);
          break;
        case BASIC_FACTORY:
          defaultSprite.setRegion(factoryTexture);
          break;
      }

      if (selectedClassMenu != null) {
        for (MachineType type : MachineType.values()) {
          if (selectedClassMenu == type.getMachineClass()) {
            itemTypes.add(type);
          }
        }
      }

      updatePlayerInfoSelection();

    }

    UIBox uiBox = getUiBox();

    float xScale = Gdx.graphics.getWidth() / (float) GameConstants.VIEW_WIDTH;
    float yScale = Gdx.graphics.getHeight() / (float) GameConstants.VIEW_HEIGHT;

    float x = (uiBox.getX() + 4) * 2.5F * xScale;
    float y = (uiBox.getY() + 84) * 2.5F * yScale;
    float width = GameConstants.TILE_SIZE * 2.5F * xScale;
    float height = GameConstants.TILE_SIZE * 2.5F * yScale;
    float gapX = 5F * xScale;
    float gapY = 5F * yScale;
    float dx = width + gapX * xScale;
    float dy = height + gapY * yScale;

    if (selRow != -1 && selColumn != -1) {
      selectSprite.setPosition(x + ((width + gapX) * selColumn), y - ((height + gapY) * selRow));
      selectSprite.setSize(width, height);
      selectSprite.draw(batch);
    }

    if (selectedClassMenu != null) {
      for (MachineType type : MachineType.values()) {
        if (selectedClassMenu == type.getMachineClass()) {
          type.draw(new Vector2(x, y), batch, width, height);
          x += dx;
          y += dy;
        }
      }
    }

  }

  @Override
  public void onClick(int relX, int relY, int button) {
    UIBox box = getUiBox();
    float dx = box.getWidth() / 3F;
    float dyu = 20F * box.getHeight() / 320F;
    float dyl = 65F * box.getHeight() / 320F;

    float xScale = Gdx.graphics.getWidth() / (float) GameConstants.VIEW_WIDTH;
    float yScale = Gdx.graphics.getHeight() / (float) GameConstants.VIEW_HEIGHT;

    float mx = 2 * 2.5F * xScale;
    float my = 26 * 2.5F * yScale;

    if (relX > mx && relY > my && relX < box.getWidth() - mx && relY < box.getHeight() * 0.9F) {
      float width = GameConstants.TILE_SIZE * 2.5F * xScale;
      float height = GameConstants.TILE_SIZE * 2.5F * yScale;
      float gapX = 5F * xScale;
      float gapY = 5F * yScale;
      float dmx = width + gapX * xScale;
      float dmy = height + gapY * yScale;

      selRow = (int) ((relY - my) / dmy);
      selColumn = (int) ((relX - mx) / (dmx));
      selIndex = selRow * 5 + selColumn;

      if (selIndex < 0) {
        selIndex = 0;
      }
      updatePlayerInfoSelection();
    }

    if (relY < dyu) {
      if (folded) {
        box.translate(0, box.getHeight() - (int) dyu);
      } else {
        box.translate(0, -(box.getHeight() - (int) dyu));
      }
      folded = !folded;
    } else if (relY < dyl) {
      if (relX < dx) {
        nextClassMenu = MachineClass.BASIC_TURRET;
      } else if (relX < 2 * dx) {
        nextClassMenu = MachineClass.BASIC_MINE;
      } else {
        nextClassMenu = MachineClass.BASIC_FACTORY;
      }
    }
  }

}
