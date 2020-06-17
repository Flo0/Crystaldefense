package com.gestankbratwurst.crystaldefense.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GameInputProcessor implements InputProcessor {

  public GameInputProcessor(GameManager gameManager) {
    this.gameManager = gameManager;
  }

  private final GameManager gameManager;

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.W:
        gameManager.player.acceleration.y += 1450F;
        break;
      case Input.Keys.S:
        gameManager.player.acceleration.y -= 1450F;
        break;
      case Input.Keys.D:
        gameManager.player.acceleration.x += 1450F;
        break;
      case Input.Keys.A:
        gameManager.player.acceleration.x -= 1450F;
        break;
      default:
        break;
    }
    return true;
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.W:
        gameManager.player.acceleration.y -= 1450F;
        break;
      case Input.Keys.S:
        gameManager.player.acceleration.y += 1450F;
        break;
      case Input.Keys.D:
        gameManager.player.acceleration.x -= 1450F;
        break;
      case Input.Keys.A:
        gameManager.player.acceleration.x += 1450F;
        break;
      default:
        break;
    }
    return true;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (gameManager.getUiManager().handleClick(screenX, Gdx.graphics.getHeight() - screenY, button)) {
      return true;
    }
    gameManager.player.onClick(screenX, screenY, button);
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    if (gameManager.getUiManager().handleMove(screenX, Gdx.graphics.getHeight() - screenY)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean scrolled(int amount) {

    if (gameManager.player.currentCam.zoom % 0.1F < 0.1F) {
      if (gameManager.player.currentCam.zoom >= 1.0F && amount > 0 && gameManager.player.currentCam.zoom < 2.0F) {
        gameManager.player.viewDistance += amount;
      } else if (gameManager.player.currentCam.zoom > 1.0F && amount < 0) {
        gameManager.player.viewDistance += amount;
      }
    }

    gameManager.player.currentCam.zoom += 0.1 * amount;
    if (gameManager.player.currentCam.zoom <= 0.2F) {
      gameManager.player.currentCam.zoom = 0.2F;
    } else if (gameManager.player.currentCam.zoom >= 2.0F) {
      gameManager.player.currentCam.zoom = 2.0F;
    }

    gameManager.player.currentCam.zoom = Math.round(gameManager.player.currentCam.zoom * 10F) / 10F;
    gameManager.player.currentCam.update();
    return true;
  }

}