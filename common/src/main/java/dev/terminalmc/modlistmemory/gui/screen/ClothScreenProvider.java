/*
 * Copyright 2026 TerminalMC
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
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Optional;

import static dev.terminalmc.modlistmemory.util.Localization.localized;

public class ClothScreenProvider {

    /**
     * Builds and returns a Cloth Config options screen.
     *
     * @param parent the current screen.
     * @return a new options {@link Screen}.
     * @throws NoClassDefFoundError if the Cloth Config API mod is not available.
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
                        Config.Mode.class, options.mode
                )
                .setTooltipSupplier((val) -> Optional.of(new Component[]{
                        localized("option", "main.mode.tooltip." + val.name())
                }))
                .setDefaultValue(Config.Options.modeDefault)
                .setSaveConsumer(val -> options.mode = val)
                .setEnumNameProvider(val -> localized("option", "main.mode." + val.name()))
                .build());

        // Integer slider with value text formatting
        main.addEntry(eb.startIntSlider(
                        localized("option", "main.pinnedModsSize"),
                        options.pinnedModsSize, 0, 200
                )
                .setTooltip(localized("option", "main.pinnedModsSize.tooltip"))
                .setDefaultValue(Config.Options.pinnedModsSizeDefault)
                .setSaveConsumer(val -> options.pinnedModsSize = val)
                .setTextGetter(val -> localized("option", "main.pinnedModsSize.value", val)) // op
                .build());

        // Integer slider with value text formatting
        main.addEntry(eb.startIntSlider(
                        localized("option", "main.recentModsSize"),
                        options.recentModsSize, 0, 200
                )
                .setTooltip(localized("option", "main.recentModsSize.tooltip"))
                .setDefaultValue(Config.Options.recentModsSizeDefault)
                .setSaveConsumer(val -> options.recentModsSize = val)
                .setTextGetter(val -> localized("option", "main.recentModsSize.value", val)) // op
                .build());

        // Enum cycling button
        main.addEntry(eb.startEnumSelector(
                        localized("option", "main.pinKey"),
                        Config.Key.class, options.pinKey
                )
                .setTooltip(localized("option", "main.pinKey.tooltip"))
                .setDefaultValue(Config.Options.pinKeyDefault)
                .setSaveConsumer(val -> options.pinKey = val)
                .setEnumNameProvider(val -> localized("option", "main.key." + val.name()))
                .build());

        // Enum cycling button
        main.addEntry(eb.startEnumSelector(
                        localized("option", "main.unpinKey"),
                        Config.Key.class, options.unpinKey
                )
                .setTooltip(localized("option", "main.unpinKey.tooltip"))
                .setDefaultValue(Config.Options.unpinKeyDefault)
                .setSaveConsumer(val -> options.unpinKey = val)
                .setEnumNameProvider(val -> localized("option", "main.key." + val.name()))
                .build());

        // Yes/No button
        main.addEntry(eb.startBooleanToggle(
                        localized("option", "main.showBadges"),
                        options.showBadges
                )
                .setTooltip(localized("option", "main.showBadges.tooltip"))
                .setDefaultValue(Config.Options.showBadgesDefault)
                .setSaveConsumer(val -> options.showBadges = val)
                .build());

        // String field (lenient)
        main.addEntry(eb.startStrField(
                        localized("option", "main.pinnedText"),
                        options.pinnedText
                )
                .setDefaultValue(Config.Options.pinnedTextDefault)
                .setSaveConsumer(val -> options.pinnedText = val)
                .build());

        // String field (lenient)
        main.addEntry(eb.startStrField(
                        localized("option", "main.recentText"),
                        options.recentText
                )
                .setDefaultValue(Config.Options.recentTextDefault)
                .setSaveConsumer(val -> options.recentText = val)
                .build());

        // Yes/No button
        main.addEntry(eb.startBooleanToggle(
                        localized("option", "main.persistOnRestart"),
                        options.persistOnRestart
                )
                .setTooltip(localized("option", "main.persistOnRestart.tooltip"))
                .setDefaultValue(Config.Options.persistOnRestartDefault)
                .setSaveConsumer(val -> options.persistOnRestart = val)
                .build());

        // Yes/No button
        main.addEntry(eb.startBooleanToggle(
                        localized("option", "main.saveOnUpdate"),
                        options.saveOnUpdate
                )
                .setTooltip(localized("option", "main.saveOnUpdate.tooltip"))
                .setDefaultValue(Config.Options.saveOnUpdateDefault)
                .setSaveConsumer(val -> options.saveOnUpdate = val)
                .build());

        return builder.build();
    }
}
