package com.gestankbratwurst.crystaldefense.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gestankbratwurst.crystaldefense.gamescreen.game.audio.AudioManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.GroundType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.World;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 09.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GameScreen extends ScreenAdapter {

  public GameScreen(AssetManager assetManager, World world) {
    this.gameBatch = new SpriteBatch();
    this.uiBatch = new SpriteBatch();
    AudioManager audioManager = new AudioManager();
    this.gameManager = new GameManager(assetManager, world, audioManager);
  }

  private final GameManager gameManager;
  private final SpriteBatch gameBatch;
  private final SpriteBatch uiBatch;

  @Override
  public void show() {
    super.show();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
      Gdx.app.exit();
    }

    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

    for (GroundType groundType : GroundType.values()) {
      groundType.tickAnimation(delta);
    }

    gameBatch.begin();
    gameManager.drawGameLayer(delta, gameBatch);
    gameBatch.end();

    uiBatch.begin();
    gameManager.drawUILayer(delta, uiBatch);
    uiBatch.end();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
  }

  @Override
  public void pause() {
    super.pause();
  }

  @Override
  public void resume() {
    super.resume();
  }

  @Override
  public void hide() {
    super.hide();
  }

  @Override
  public void dispose() {
    super.dispose();
    gameManager.dispose();
  }

}
