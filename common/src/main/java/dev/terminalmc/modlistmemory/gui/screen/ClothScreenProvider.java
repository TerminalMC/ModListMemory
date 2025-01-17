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

package dev.terminalmc.modlistmemory.gui.screen;

import dev.terminalmc.modlistmemory.config.Config;
import me.shedaniel.clothconfig2.api.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Optional;

import static dev.terminalmc.modlistmemory.util.Localization.localized;

public class ClothScreenProvider {
    /**
     * Builds and returns a Cloth Config options screen.
     * @param parent the current screen.
     * @return a new options {@link Screen}.
     * @throws NoClassDefFoundError if the Cloth Config API mod is not
     * available.
     */
    static Screen getConfigScreen(Screen parent) {
        Config.Options options = Config.options();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(localized("name"))
                .setSavingRunnable(Config::save);
        ConfigEntryBuilder eb = builder.entryBuilder();

        // First category
        ConfigCategory main = builder.getOrCreateCategory(localized("option", "main"));

        // Enum cycling button
        main.addEntry(eb.startEnumSelector(
                        localized("option", "main.mode"),
                        Config.Mode.class, options.mode)
                .setTooltipSupplier((val) -> Optional.of(new Component[]{
                        localized("option", "main.mode.tooltip." + val.name())
                }))
                .setDefaultValue(Config.Options.modeDefault)
                .setSaveConsumer(val -> options.mode = val)
                .setEnumNameProvider(val -> localized("option", "main.mode." + val.name()))
                .build());

        // Integer slider with value text formatting
        main.addEntry(eb.startIntSlider(
                localized("option", "main.memorySize"), options.memorySize, 0, 200)
                .setTooltip(localized("option", "main.memorySize.tooltip"))
                .setDefaultValue(Config.Options.memorySizeDefault)
                .setSaveConsumer(val -> options.memorySize = val)
                .setTextGetter(val -> localized("option", "main.memorySize.value", val)) // op
                .build());

        // Yes/No button
        main.addEntry(eb.startBooleanToggle(
                        localized("option", "main.persistOnRestart"), options.persistOnRestart)
                .setTooltip(localized("option", "main.persistOnRestart.tooltip"))
                .setDefaultValue(Config.Options.persistOnRestartDefault)
                .setSaveConsumer(val -> options.persistOnRestart = val)
                .build());

        // Yes/No button
        main.addEntry(eb.startBooleanToggle(
                        localized("option", "main.saveOnUpdate"), options.saveOnUpdate)
                .setTooltip(localized("option", "main.saveOnUpdate.tooltip"))
                .setDefaultValue(Config.Options.saveOnUpdateDefault)
                .setSaveConsumer(val -> options.saveOnUpdate = val)
                .build());

        return builder.build();
    }
}
