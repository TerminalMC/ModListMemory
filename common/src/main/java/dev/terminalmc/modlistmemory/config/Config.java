/*
 * Copyright 2025 TerminalMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.terminalmc.modlistmemory.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.terminalmc.modlistmemory.ModListMemory;
import dev.terminalmc.modlistmemory.platform.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Path CONFIG_DIR = Services.PLATFORM.getConfigDir();
    private static final String FILE_NAME = ModListMemory.MOD_ID + ".json";
    private static final String BACKUP_FILE_NAME = ModListMemory.MOD_ID + ".unreadable.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Options

    public final Options options = new Options();

    public static Options options() {
        return Config.get().options;
    }

    public static class Options {
        public static final Mode modeDefault = Mode.REMEMBER_RECENT;
        public Mode mode = modeDefault;

        public static final int pinnedModsSizeDefault = 5;
        public int pinnedModsSize = pinnedModsSizeDefault;

        public static final int recentModsSizeDefault = 5;
        public int recentModsSize = recentModsSizeDefault;

        public static final Key pinKeyDefault = Key.SHIFT;
        public Key pinKey = pinKeyDefault;

        public static final Key unpinKeyDefault = Key.CONTROL;
        public Key unpinKey = unpinKeyDefault;

        public static final boolean showBadgesDefault = true;
        public boolean showBadges = showBadgesDefault;

        public static final String pinnedTextDefault = "I";
        public String pinnedText = pinnedTextDefault;
        
        public static final String recentTextDefault = "R";
        public String recentText = recentTextDefault;

        public static final boolean persistOnRestartDefault = true;
        public boolean persistOnRestart = persistOnRestartDefault;

        public static final boolean saveOnUpdateDefault = true;
        public boolean saveOnUpdate = saveOnUpdateDefault;

        // Not user-editable

        public static final List<String> pinnedModsDefault = new ArrayList<>();
        public List<String> pinnedMods = pinnedModsDefault;

        public static final List<String> recentModsDefault = new ArrayList<>();
        public List<String> recentMods = recentModsDefault;
        
        public static final double scrollAmountDefault = Double.MIN_VALUE;
        public double scrollAmount = scrollAmountDefault;
    }

    @SuppressWarnings("unused")
    public enum Mode {
        DISABLED,
        REMEMBER_RECENT,
        REMEMBER_SCROLL,
    }

    public enum Key {
        CONTROL,
        ALT,
        SHIFT,
        NONE,
    }

    // Instance management

    private static Config instance = null;

    public static Config get() {
        if (instance == null) {
            instance = Config.load();
        }
        return instance;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Config getAndSave() {
        get();
        save();
        return instance;
    }

    @SuppressWarnings("unused")
    public static Config resetAndSave() {
        instance = new Config();
        save();
        return instance;
    }

    // Validation

    private void validate() {
        // Called before config is saved
    }

    // Load and save

    public static @NotNull Config load() {
        Path file = CONFIG_DIR.resolve(FILE_NAME);
        Config config = null;
        if (Files.exists(file)) {
            config = load(file, GSON);
            if (config == null) {
                backup();
                ModListMemory.LOG.warn("Resetting config");
            }
        }
        return config != null ? config : new Config();
    }

    @SuppressWarnings("SameParameterValue")
    private static @Nullable Config load(Path file, Gson gson) {
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(file.toFile()), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            // Catch Exception as errors in deserialization may not fall under
            // IOException or JsonParseException, but should not crash the game.
            ModListMemory.LOG.error("Unable to load config", e);
            return null;
        }
    }
    
    private static void backup() {
        try {
            ModListMemory.LOG.warn("Copying {} to {}", FILE_NAME, BACKUP_FILE_NAME);
            if (!Files.isDirectory(CONFIG_DIR)) Files.createDirectories(CONFIG_DIR);
            Path file = CONFIG_DIR.resolve(FILE_NAME);
            Path backupFile = file.resolveSibling(BACKUP_FILE_NAME);
            Files.move(file, backupFile, StandardCopyOption.ATOMIC_MOVE, 
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ModListMemory.LOG.error("Unable to copy config file", e);
        }
    }

    public static void save() {
        if (instance == null) return;
        instance.validate();
        try {
            if (!Files.isDirectory(CONFIG_DIR)) Files.createDirectories(CONFIG_DIR);
            Path file = CONFIG_DIR.resolve(FILE_NAME);
            Path tempFile = file.resolveSibling(file.getFileName() + ".tmp");
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(tempFile.toFile()), StandardCharsets.UTF_8)) {
                writer.write(GSON.toJson(instance));
            } catch (IOException e) {
                throw new IOException(e);
            }
            Files.move(tempFile, file, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
            ModListMemory.onConfigSaved(instance);
        } catch (IOException e) {
            ModListMemory.LOG.error("Unable to save config", e);
        }
    }
}
