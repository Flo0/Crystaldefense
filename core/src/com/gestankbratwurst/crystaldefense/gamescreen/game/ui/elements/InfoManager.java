package com.gestankbratwurst.crystaldefense.gamescreen.game.ui.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.FontManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.Informational;
import java.util.ArrayList;
import lombok.Data;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 16.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class InfoManager implements IDrawable {

  public InfoManager(TextureAtlas atlas, FontManager fontManager, Player player) {
    this.player = player;
    this.top = atlas.createSprite("ui/info_top");
    this.mid = atlas.createSprite("ui/info_mid");
    this.bot = atlas.createSprite("ui/info_bot");
    this.font = fontManager.createFont((int) (14F * Gdx.graphics.getHeight() / GameConstants.VIEW_HEIGHT));
  }

  private final BitmapFont font;
  private final Sprite top;
  private final Sprite mid;
  private final Sprite bot;
  private final Player player;
  private Informational currentInformational;

  @Data
  private class InfoDraw {

    private final Vector2 pos;
    private final String value;
  }

  @Override
  public void draw(float delta, SpriteBatch batch) {
    if (currentInformational != player.getCurrentInformational()) {
      currentInformational = player.getCurrentInformational();
    }
    if (currentInformational == null) {
      return;
    }

    ArrayList<String> desc = currentInformational.getDescription();
    ArrayList<InfoDraw> draws = new ArrayList<>();

    int descSize = desc == null ? 0 : desc.size();

    float xScale = Gdx.graphics.getWidth() / (float) GameConstants.VIEW_WIDTH;
    float yScale = Gdx.graphics.getHeight() / (float) GameConstants.VIEW_HEIGHT;

    float width = 96F * xScale * 3;
    float height = 8F * yScale * 3;

    float xPos = Gdx.graphics.getWidth() * 0.975F - width;
    float yPos = (2 + descSize) * height;

    top.setPosition(xPos, yPos);
    top.setSize(width, height);
    top.draw(batch);

    draws.add(new InfoDraw(new Vector2(xPos + (width * 0.05F), yPos + (height * 0.25F)), currentInformational.getDisplay()));

    yPos -= height;

    for (int i = 0; i < descSize; i++) {
      mid.setPosition(xPos, yPos);
      mid.setSize(width, height);
      mid.draw(batch);
      draws.add(new InfoDraw(new Vector2(xPos + (width * 0.05F), yPos + (height * 0.25F)), desc.get(i)));
      yPos -= height;
    }

    bot.setPosition(xPos, yPos);
    bot.setSize(width, height);
    bot.draw(batch);

    for (InfoDraw draw : draws) {
      font.draw(batch, draw.value, draw.pos.x, draw.pos.y);
    }

  }

  @Override
  public int getPriority() {
    return 9;
  }
}
