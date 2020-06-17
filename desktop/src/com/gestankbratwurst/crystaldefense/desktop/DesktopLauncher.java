package com.gestankbratwurst.crystaldefense.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.gestankbratwurst.crystaldefense.CrystalDefenseCore;


public class DesktopLauncher {

  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

    config.useOpenGL3(true, 3, 2);
    config.setWindowedMode(1280, 720);
    config.setResizable(false);
    config.setIdleFPS(30);
    config.setTitle("Crystal defense");
    config.setWindowIcon("icon.png");
    config.useVsync(false);
    //config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

    packTextures();
    new Lwjgl3Application(new CrystalDefenseCore(), config);
  }

  private static void packTextures() {
    TexturePacker.Settings settings = new TexturePacker.Settings();
    settings.maxHeight = 2048;
    settings.maxWidth = 2048;
    settings.edgePadding = true;
    settings.paddingX = 2;
    settings.paddingY = 2;
    settings.duplicatePadding = true;
    settings.silent = true;
    settings.filterMin = TextureFilter.MipMap;
    settings.filterMag = TextureFilter.MipMap;
    settings.silent = false;
    TexturePacker.process(settings, "textures", "atlas", "combined");
  }

}
