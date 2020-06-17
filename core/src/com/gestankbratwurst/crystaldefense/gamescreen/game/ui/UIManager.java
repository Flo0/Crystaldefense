package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements.BuildPanel;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements.InfoManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements.ResourcePanel;
import java.util.HashSet;
import java.util.Set;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 12.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UIManager implements IDrawable {

  public UIManager(GameManager gameManager) {
    this.uiElements = new HashSet<>();
    TextureAtlas atlas = gameManager.getAssetManager().get("atlas/combined.atlas", TextureAtlas.class);

    uiElements.add(ResourcePanel.construct(atlas, gameManager.player, gameManager.getFontManager()));
    uiElements.add(BuildPanel.construct(atlas, gameManager.player));
    this.hpBarHandler = new HPBarHandler(atlas, gameManager);
    this.infoManager = new InfoManager(atlas, gameManager.getFontManager(), gameManager.player);
  }

  private final Set<UIElement> uiElements;
  private final HPBarHandler hpBarHandler;
  private final InfoManager infoManager;

  public boolean handleMove(int x, int y) {
    boolean hit = false;
    for (UIElement element : uiElements) {
      if (element.getUiBox().contains(x, y)) {
        hit = true;
        if (!element.isHovered()) {
          element.setHovered(true);
        }
      } else if (element.isHovered()) {
        element.setHovered(false);
      }
    }
    return hit;
  }

  public boolean handleClick(int x, int y, int button) {
    boolean hit = false;
    for (UIElement element : uiElements) {
      UIBox box = element.getUiBox();
      if (box.contains(x, y)) {
        hit = true;
        element.onClick(x - box.getX(), -y + box.getY() + box.getHeight(), button);
      }
    }
    return hit;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    for (UIElement element : uiElements) {
      element.draw(delta, batch);
    }
    hpBarHandler.draw(delta, batch);
    infoManager.draw(delta, batch);
  }

  @Override
  public int getPriority() {
    return 9;
  }
}
