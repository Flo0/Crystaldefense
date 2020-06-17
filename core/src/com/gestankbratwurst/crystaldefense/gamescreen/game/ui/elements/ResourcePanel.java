package com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.FontManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.UIBox;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.UIElement;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ResourcePanel extends UIElement {

  public static ResourcePanel construct(TextureAtlas atlas, Player player, FontManager fontManager) {
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    UIBox uiBox = new UIBox((int) (w * 0.9F), (int) (h * 0.6F), (int) (w * 0.1F), (int) (h * 0.4F));
    Sprite sprite = atlas.createSprite("ui/resource_panel");
    return new ResourcePanel(uiBox, sprite, player, fontManager.createFont((int) (h / 50F)));
  }

  private ResourcePanel(UIBox uiBox, Sprite defaultSprite, Player player, BitmapFont font) {
    super(uiBox, defaultSprite);
    this.buttonBox = new UIBox(0, 0, (int) (uiBox.getWidth() * 0.17), (int) (uiBox.getWidth() * 0.17));
    this.player = player;
    this.font = font;
  }

  private final BitmapFont font;
  private final Player player;
  private boolean folded = false;
  private final UIBox buttonBox;

  @Override
  protected void onHover() {

  }

  @Override
  protected void onUnHover() {

  }

  @Override
  protected void postDraw(float delta, SpriteBatch batch) {
    UIBox uiBox = getUiBox();
    font.draw(batch, "Resources", uiBox.getX() + uiBox.getWidth() * 0.22F, uiBox.getY() + uiBox.getHeight() * 0.965F);
    float dy = uiBox.getHeight() / 12F;
    float y = uiBox.getY() + uiBox.getHeight() * 0.82F;
    float x = uiBox.getX() + uiBox.getWidth() * 0.05F;
    float w = uiBox.getWidth() * 0.25F;
    float h = w;

    for (ResourceType resourceType : ResourceType.values()) {
      float amount = player.getResource(resourceType);
      float max = player.getResourceLimit(resourceType);
      String display = "" + (int) amount;
      Sprite sprite = resourceType.getSprite();
      sprite.setPosition(x, y);
      sprite.setSize(w, h);
      sprite.draw(batch);
      font.draw(batch, display, sprite.getX() + sprite.getWidth() * 1.05F, sprite.getY() + sprite.getHeight() / 1.5F);
      y -= dy;
    }
  }

  @Override
  public void onClick(int x, int y, int button) {
    if (buttonBox.contains(x, y)) {
      UIBox uiBox = getUiBox();
      if (folded) {
        uiBox.translate(-(int) (uiBox.getWidth() * 0.85), 0);
      } else {
        uiBox.translate((int) (uiBox.getWidth() * 0.85), 0);
      }
      folded = !folded;
    }
  }

}