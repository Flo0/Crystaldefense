package com.gestankbratwurst.crystaldefense.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.GameConstants;
import com.gestankbratwurst.crystaldefense.gamescreen.game.audio.AudioManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.IDrawable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.logic.ITickable;
import com.gestankbratwurst.crystaldefense.gamescreen.game.player.Player;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.CursorType;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.FontManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.ui.UIManager;
import com.gestankbratwurst.crystaldefense.gamescreen.game.world.World;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 10.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class GameManager implements Runnable, Disposable {

  @Getter
  private static GameManager instance;

  public GameManager(AssetManager assetManager, World world, AudioManager audioManager) {
    GameManager.instance = this;
    int cores = Math.min(Math.max(Runtime.getRuntime().availableProcessors() - 1, 1), 4);
    this.threadPoolExecutor = new ScheduledThreadPoolExecutor(cores);
    this.mainQueue = new ArrayDeque<>();
    this.gameDrawQueues = new HashMap<>();
    this.uiDrawQueues = new HashMap<>();

    this.assetManager = assetManager;
    this.mainAtlas = assetManager.get("atlas/combined.atlas", TextureAtlas.class);

    this.audioManager = audioManager;
    audioManager.changeMusic("training_mode", true, 0.1F);

    this.fontManager = new FontManager();
    this.mainFont = fontManager.createFont(15);

    GameInputProcessor inputProcessor = new GameInputProcessor(this);
    Gdx.input.setInputProcessor(inputProcessor);

    this.mainCamera = new OrthographicCamera(GameConstants.VIEW_WIDTH, GameConstants.VIEW_HEIGHT);
    this.player = new Player(mainCamera, this);

    this.addGameDrawable(player);
    this.mainQueue.add(player);

    this.world = world;
    this.world.setPlayer(player);
    this.addGameDrawable(world);

    this.uiManager = new UIManager(this);

    CursorType.NORMAL.apply();

    long micros = 1000000L / GameConstants.TPS;
    this.deltas = new float[GameConstants.TPS];
    this.threadPoolExecutor.scheduleAtFixedRate(this, 0L, micros, TimeUnit.MICROSECONDS);
  }

  @Getter
  private final FontManager fontManager;
  @Getter
  private final World world;
  @Getter
  private final TextureAtlas mainAtlas;
  @Getter
  private final AssetManager assetManager;
  private final AudioManager audioManager;
  @Getter
  private final UIManager uiManager;
  private final BitmapFont mainFont;
  private final ScheduledThreadPoolExecutor threadPoolExecutor;
  private final ArrayDeque<ITickable> mainQueue;
  private final Map<Integer, ArrayDeque<IDrawable>> gameDrawQueues;
  private final Map<Integer, ArrayDeque<IDrawable>> uiDrawQueues;
  private final OrthographicCamera mainCamera;
  private float delta = 1F;
  private long lastTick = System.nanoTime();
  private final float[] deltas;
  private int tpsIndex = 0;
  @Getter
  private float ticksPerSecond = GameConstants.TPS;

  public final Player player;

  public void calcTPS() {
    float sum = 0F;
    for (float v : deltas) {
      sum += v;
    }
    ticksPerSecond = deltas.length / sum;
  }

  private void calcDelta() {
    long now = System.nanoTime();
    this.delta = (float) ((now - lastTick) / 1E9D);
    if (++tpsIndex == deltas.length) {
      tpsIndex = 0;
      calcTPS();
    }
    deltas[tpsIndex] = delta;
    lastTick = now;
  }

  public void addGameDrawable(IDrawable drawable) {
    if (!gameDrawQueues.containsKey(drawable.getPriority())) {
      gameDrawQueues.put(drawable.getPriority(), new ArrayDeque<>());
    }
    gameDrawQueues.get(drawable.getPriority()).add(drawable);
  }

  public void addTickable(ITickable tickable) {
    this.mainQueue.add(tickable);
  }

  @Override
  public void run() {
    calcDelta();
    ITickable last = mainQueue.peekLast();
    if (last == null) {
      return;
    }
    ITickable current;
    do {
      current = mainQueue.poll();
      current.tick(delta);
      if (current.reschedule()) {
        mainQueue.add(current);
      }
    } while (current != last);
  }

  @Override
  public void dispose() {
    threadPoolExecutor.shutdown();
  }

  private void drawGameObjects(float delta, SpriteBatch batch) {
    for (int i = 0; i < GameConstants.MAX_PRIORITY; i++) {
      ArrayDeque<IDrawable> queue = gameDrawQueues.get(i);
      if (queue != null) {
        IDrawable last = queue.peekLast();
        if (last == null) {
          return;
        }
        IDrawable current;
        do {
          current = queue.poll();
          current.draw(delta, batch);
          if (current.reschedule()) {
            queue.add(current);
          }
        } while (current != last);
      }
    }
  }

  public void drawGameLayer(float delta, SpriteBatch batch) {
    drawGameObjects(delta, batch);
    player.currentCam.position.x = (float) Math.round(player.pos.x * 100F) / 100F;
    player.currentCam.position.y = (float) Math.round(player.pos.y * 100F) / 100F;
    batch.setProjectionMatrix(player.currentCam.combined);
  }

  public void drawUILayer(float delta, SpriteBatch batch) {
    uiManager.draw(delta, batch);
    int h = Gdx.graphics.getHeight();
    mainFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, h - 20);
    mainFont.draw(batch, "TPS: " + this.getTicksPerSecond(), 10, h - 40);
    mainFont.draw(batch, "Pos: " + player.currentCam.position, 10, h - 60);
    mainFont.draw(batch, "Zoom: " + player.currentCam.zoom, 10, h - 80);
    mainFont.draw(batch, "Acceleration: " + player.acceleration.len(), 10, h - 100);
    mainFont.draw(batch, "Speed: " + player.velocity.len(), 10, h - 120);
    mainFont.draw(batch, "Viewdistance: " + player.viewDistance, 10, h - 140);
//    ShapeRenderer sr = new ShapeRenderer();
//    sr.begin(ShapeType.Line);
//    sr.line(Gdx.graphics.getWidth() / 2F, 0F, Gdx.graphics.getWidth() / 2F, Gdx.graphics.getHeight());
//    sr.end();
  }

}
