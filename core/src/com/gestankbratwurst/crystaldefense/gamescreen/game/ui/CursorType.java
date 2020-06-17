package com.gestankbratwurst.crystaldefense.gamescreen.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.nio.file.attribute.UserPrincipal;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 12.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public enum CursorType {

  NORMAL("cursor/cursor_normal.png", 0, 0);

  CursorType(String cursorPath, int xHotspot, int yHotspot) {
    this.cursorPath = cursorPath;
    this.xHotspot = xHotspot;
    this.yHotspot = yHotspot;
  }

  public static void init() {
    for (CursorType cursorType : values()) {
      Pixmap pixmap = new Pixmap(Gdx.files.internal("textures/" + cursorType.cursorPath));
      cursorType.cursor = Gdx.graphics.newCursor(pixmap, cursorType.xHotspot, cursorType.yHotspot);
      pixmap.dispose();
    }
  }

  private final String cursorPath;
  private final int xHotspot;
  private final int yHotspot;
  private Cursor cursor;

  public void apply() {
    Gdx.graphics.setCursor(this.cursor);
  }

}
