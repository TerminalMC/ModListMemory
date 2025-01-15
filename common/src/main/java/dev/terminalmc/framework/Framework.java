/*
 * Framework by TerminalMC
 *
 * To the extent possible under law, the person who associated CC0 with
 * Framework has waived all copyright and related or neighboring rights
 * to Framework.
 *
 * You should have received a copy of the CC0 legalcode along with this
 * work. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.terminalmc.framework;

import com.mojang.blaze3d.platform.InputConstants;
import dev.terminalmc.framework.config.Config;
import dev.terminalmc.framework.gui.screen.ConfigScreenProvider;
import dev.terminalmc.framework.util.ModLogger;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static dev.terminalmc.framework.util.Localization.translationKey;

public class Framework {
    public static final String MOD_ID = "framework";
    public static final String MOD_NAME = "Framework";
    public static final ModLogger LOG = new ModLogger(MOD_NAME);
    public static final Component PREFIX = Component.empty()
            .append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY))
            .append(Component.literal(MOD_NAME).withStyle(ChatFormatting.GOLD))
            .append(Component.literal("] ").withStyle(ChatFormatting.DARK_GRAY))
            .withStyle(ChatFormatting.GRAY);
    public static final KeyMapping EXAMPLE_KEY = new KeyMapping(
            translationKey("key", "group.example"), InputConstants.Type.KEYSYM,
            InputConstants.UNKNOWN.getValue(), translationKey("key", "group"));

    public static void init() {
        Config.getAndSave();
    }

    public static void onEndTick(Minecraft mc) {
        while (EXAMPLE_KEY.consumeClick()) {
            mc.setScreen(ConfigScreenProvider.getConfigScreen(mc.screen));
        }
    }

    public static void onConfigSaved(Config config) {
        // If you are maintaining caches based on config values, update them here.
    }
}
