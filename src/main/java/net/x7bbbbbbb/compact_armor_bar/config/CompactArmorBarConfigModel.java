package net.x7bbbbbbb.compact_armor_bar.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import net.x7bbbbbbb.compact_armor_bar.CompactArmorBar;

@Modmenu(modId = CompactArmorBar.MOD_ID)
@Config(name = "compact_armor_bar_config", wrapperName = "CompactArmorBarConfig")
public class CompactArmorBarConfigModel{
    public boolean betterMountHUD = false;
    public int rowOffset = 0;
}