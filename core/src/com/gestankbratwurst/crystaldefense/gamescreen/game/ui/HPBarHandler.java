package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gestankbratwurst.crystaldefense.gamescreen.GameManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class HPBarHandler implements IDrawable {

  public HPBarHandler(TextureAtlas atlas, GameManager gameManager) {
    this.midBarFul = atlas.createSprite("ui/bar_mid_full");
    this.leftBarFul = atlas.createSprite("ui/bar_side_full");
    this.rightBarFul = atlas.createSprite("ui/bar_side_full_rot");
    this.midBarEmpty = atlas.createSprite("ui/bar_mid_empty");
    this.leftBarEmpty = atlas.createSprite("ui/bar_side_empty");
    this.rightBarEmpty = atlas.createSprite("ui/bar_side_empty_rot");
    this.crystal = atlas.createSprite("ui/crystal_icon");

    this.font = gameManager.getFontManager().createFont((int) (Gdx.graphics.getHeight() / 40F));
    this.heightScale = Gdx.graphics.getHeight() / 180F;
    this.widthScale = heightScale / 4F;

    midBarFul.setScale(widthScale, heightScale);
    leftBarFul.setScale(heightScale);
    rightBarFul.setScale(heightScale);
    midBarEmpty.setScale(widthScale, heightScale);
    leftBarEmpty.setScale(heightScale);
    rightBarEmpty.setScale(heightScale);
    crystal.scale(heightScale);

    this.player = gameManager.player;
  }

  private final Player player;
  private final BitmapFont font;
  private final Sprite crystal;
  private final Sprite midBarFul;
  private final Sprite leftBarFul;
  private final Sprite rightBarFul;
  private final Sprite midBarEmpty;
  private final Sprite leftBarEmpty;
  private final Sprite rightBarEmpty;
  private final float heightScale;
  private final float widthScale;

  @Override
  public void draw(float delta, SpriteBatch batch) {
    float dx = midBarEmpty.getWidth() * widthScale;
    float hpPro = player.getHpFill();

    float x = (Gdx.graphics.getWidth() - GameConstants.HP_BAR_SIZE * dx) / 2F + dx / 4F;
    float y = Gdx.graphics.getHeight() * 0.05F;

    int fulls = (int) (hpPro * GameConstants.HP_BAR_SIZE);
    int empties = GameConstants.HP_BAR_SIZE - fulls;

    boolean left = fulls > 0;
    boolean right = fulls == GameConstants.HP_BAR_SIZE;
    if (!right) {
      fulls++;
      empties--;
    }

    if (!left) {
      empties--;
    }
    if (empties == GameConstants.HP_BAR_SIZE - 1) {
      empties--;
    }
    Sprite leftSprite = left ? leftBarFul : leftBarEmpty;
    Sprite rightSprite = right ? rightBarFul : rightBarEmpty;

    crystal.setPosition(x, y);
    crystal.draw(batch);
    crystal.setPosition(x + (GameConstants.HP_BAR_SIZE - 1) * dx, y);
    crystal.draw(batch);

    leftSprite.setPosition(x, y);
    leftSprite.draw(batch);
    x += dx;

    for (int i = 0; i < fulls - 2; i++) {
      midBarFul.setPosition(x, y);
      midBarFul.draw(batch);
      x += dx;
    }
    for (int i = 0; i < empties; i++) {
      midBarEmpty.setPosition(x, y);
      midBarEmpty.draw(batch);
      x += dx;
    }

    rightSprite.setPosition(x, y);
    rightSprite.draw(batch);

    String text = player.getHp() + "/" + player.getMaxHp();
    GlyphLayout layout = new GlyphLayout(font, text);
    float fontX = (Gdx.graphics.getWidth() - layout.width) / 2F;

    font.draw(batch, layout, fontX, y + layout.height / 1.6F);

  }

  @Override
  public int getPriority() {
    return 9;
  }

}