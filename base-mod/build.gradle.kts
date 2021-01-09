apply(plugin = "mod")

configure<ModExtension> {
    fileRenames {
        add("panorama/images/hud/reborn/statbranch_button_bg_png.vtex_c", "panorama/images/hud/reborn/statbranch_button_bg_psd.vtex_c")
        add("panorama/images/textures/startup_background_logo_png.vtex_c", "panorama/images/textures/startup_background_logo_psd.vtex_c")
    }
    stringReplacements {
        // Items
        any("Black King Bar", "sadKEK Bar")
        any("Diffusal Blade", "Diffushal Blade")
        any("Mango", "FatMango")
        any("mango", "FatMango")
        any("Mask of Madness", "Mask of Maldness")
        any("Royal Jelly", "Royal Yelly")
        any("Scythe of Vyses", "Eggs")
        any("Scythe of Vyse", "Eggs")

        // Runes
        id("DOTA_Tooltip_rune_bounty_description", "DA ROONS haHAA")
        id("DOTA_Tooltip_rune_haste", "s4")
        id("DOTA_Tooltip_modifier_rune_haste", "s4")
        id("DOTA_Tooltip_Ability_item_bottle_haste", "Rune: s4")
        id("DOTA_Chat_Rune_Haste", "%s1 activated a <font color='#F62817'>s4</font> rune.")
        id("DOTA_Chat_Rune_Bottle_Haste", "%s1 has bottled a <font color='#F62817'>s4</font> rune.")
        id("DOTA_Chat_Rune_Haste_Spotted", "<font color='#F62817'>s4</font> rune here (%s1).")
        id("DOTA_HUD_Rune_Haste", "<font color='#F62817'>s4</font> rune")
    }
}