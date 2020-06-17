package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import lombok.Getter;
import lombok.Setter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 12.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class UIElement implements IDrawable {

  public UIElement(UIBox uiBox, Sprite defaultSprite) {
    this(uiBox, defaultSprite, null);
  }

  public UIElement(UIBox uiBox, Sprite defaultSprite, Sprite hoverSprite) {
    this.uiBox = uiBox;
    this.defaultSprite = defaultSprite;
    this.hoverSprite = hoverSprite;
  }

  @Getter
  private final UIBox uiBox;
  protected final Sprite defaultSprite;
  protected final Sprite hoverSprite;
  @Getter
  private boolean hovered = false;
  @Getter
  @Setter
  private boolean visible = true;

  public boolean isHoverable() {
    return hoverSprite != null;
  }

  public void setHovered(boolean value) {
    if (value) {
      onHover();
    } else if (hovered) {
      onUnHover();
    }
    this.hovered = value;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    if (!visible) {
      return;
    }
    Sprite sprite = hovered && isHoverable() ? hoverSprite : defaultSprite;
    sprite.setPosition(uiBox.getX(), uiBox.getY());
    sprite.setSize(uiBox.getWidth(), uiBox.getHeight());
    sprite.draw(batch);
    postDraw(delta, batch);
  }

  @Override
  public int getPriority() {
    return 9;
  }

  protected abstract void onHover();

  protected abstract void onUnHover();

  protected abstract void postDraw(float delta, SpriteBatch batch);

  public abstract void onClick(int relX, int relY, int button);

}
