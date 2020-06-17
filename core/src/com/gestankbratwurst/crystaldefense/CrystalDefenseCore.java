package com.gestankbratwurst.crystaldefense;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.gestankbratwurst.crystaldefense.gamescreen.utils.DLog;
import com.gestankbratwurst.crystaldefense.mainscreen.LoadingScreen;
import lombok.Getter;


public class CrystalDefenseCore extends Game {

  @Getter
  private static CrystalDefenseCore instance;
  private final AssetManager assetManager;

  public CrystalDefenseCore() {
    if (instance != null) {
      throw new IllegalStateException("Cant instantiate more than one Game class.");
    }

    DLog.setEnabled(true);
    DLog.setLogStreamEnabled(true);

    instance = this;
    this.assetManager = new AssetManager();
  }

  @Override
  public void create() {
    this.setScreen(new LoadingScreen(assetManager));
  }

  @Override
  public void dispose() {
    super.dispose();
    this.screen.dispose();
    DLog.close();
  }

}
