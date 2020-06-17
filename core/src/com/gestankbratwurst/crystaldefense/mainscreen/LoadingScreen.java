package com.gestankbratwurst.crystaldefense.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.gestankbratwurst.crystaldefense.CrystalDefenseCore;
import com.gestankbratwurst.crystaldefense.gamescreen.GameScreen;
import com.gestankbratwurst.crystaldefense.gamescreen.game.machines.MachineType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.CursorType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.GroundType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.ResourceType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.generators.SimpleWorldGenerator;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.generators.WorldGenerator;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 09.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LoadingScreen extends ScreenAdapter {

  public LoadingScreen(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.load("atlas/combined.atlas", TextureAtlas.class);

    this.loadingSong = Gdx.audio.newMusic(Gdx.files.internal("music/idle/bits_and_beats.mp3"));
    this.barRenderer = new ShapeRenderer();
    this.batch = new SpriteBatch();
    this.worldGenerator = new SimpleWorldGenerator();
    worldGenerator.startRender(100L, 50, 50);

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/retro.ttf"));
    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    parameter.size = 15;
    parameter.color = Color.WHITE;
    this.font = generator.generateFont(parameter);
    generator.dispose();
    loadingSong.setLooping(true);
    loadingSong.setVolume(0.45F);
    loadingSong.play();
  }

  private final Music loadingSong;
  private final Texture background = new Texture("art/loading_background.png");
  private final BitmapFont font;
  private final AssetManager assetManager;
  private final ShapeRenderer barRenderer;
  private final WorldGenerator worldGenerator;
  private final SpriteBatch batch;

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.5F, 0.5F, 0.5F, 1);
    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

    batch.begin();
    batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    batch.end();

    float x0 = Gdx.graphics.getWidth() * 0.1F;
    float y0 = Gdx.graphics.getHeight() * 0.1F;
    float width = Gdx.graphics.getWidth() * 0.8F;
    float height = Gdx.graphics.getHeight() * 0.05F;

    barRenderer.begin(ShapeType.Line);
    barRenderer.setColor(Color.WHITE);
    barRenderer.rect(x0, y0, width, height);
    barRenderer.end();
    barRenderer.begin(ShapeType.Filled);
    barRenderer.setColor(Color.ORANGE);
    barRenderer.rect(x0, y0, assetManager.getProgress() * width / 2 + worldGenerator.getProgress() * width / 2, height - 1);
    barRenderer.end();

    String updateString;

    if (!assetManager.isFinished()) {
      updateString = "Loading Assets";
      if (assetManager.update()) {
        TextureAtlas atlas = assetManager.get("atlas/combined.atlas", TextureAtlas.class);
        GroundType.init(atlas);
        ResourceType.init(atlas);
        MachineType.init(atlas);
        CursorType.init();
      }
    } else {
      if (worldGenerator.advanceRender()) {
        loadingSong.stop();
        loadingSong.dispose();
        CrystalDefenseCore.getInstance().setScreen(new GameScreen(assetManager, worldGenerator.getRenderedWorld()));
      }
      updateString = "Generating Chunks " + worldGenerator.getChunksDone() + "/" + worldGenerator.getChunkAmount();
    }

    batch.begin();
    font.draw(batch, updateString, x0, y0 + height + 20);
    batch.end();
  }


}
