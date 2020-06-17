package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class FontManager {

  public FontManager() {

  }

  public BitmapFont createFont(int size) {
    return createFont(size, Color.WHITE);
  }

  public BitmapFont createFont(int size, Color color) {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/retro.ttf"));
    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    parameter.size = size;
    parameter.color = color;
    BitmapFont font = generator.generateFont(parameter);
    generator.dispose();
    return font;
  }

}
