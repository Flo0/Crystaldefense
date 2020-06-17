package com.gestankbratwurst.crystaldefense.gamescreen.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 11.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class AudioManager {

  public AudioManager() {
    this.musicMap = new HashMap<>();
    this.soundMap = new HashMap<>();

    musicMap.put("training_mode", Gdx.audio.newMusic(Gdx.files.internal("music/idle/training_mode.mp3")));
    musicMap.put("bits_and_beats", Gdx.audio.newMusic(Gdx.files.internal("music/idle/bits_and_beats.mp3")));
    musicMap.put("blacklist", Gdx.audio.newMusic(Gdx.files.internal("music/fight/blacklist.mp3")));
    musicMap.put("shape_shift", Gdx.audio.newMusic(Gdx.files.internal("music/fight/shape_shift.mp3")));

  }

  private final ThreadLocalRandom random = ThreadLocalRandom.current();

  private final Map<String, Music> musicMap;
  private final Map<String, Sound> soundMap;

  @Getter
  private Music activeMusic = null;

  public void changeMusic(String name, boolean loop, float volume) {
    if (activeMusic != null) {
      activeMusic.pause();
    }
    activeMusic = musicMap.get(name);
    activeMusic.setPosition(0F);
    activeMusic.play();
    activeMusic.setLooping(loop);
    activeMusic.setVolume(volume);
  }

}
