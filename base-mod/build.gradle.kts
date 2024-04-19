apply(plugin = "mod")

dotaMod {
    fileRenames {
        add("panorama/images/hud/reborn/statbranch_button_bg_png.vtex_c", "panorama/images/hud/reborn/statbranch_button_bg_psd.vtex_c")
        add("panorama/images/textures/startup_background_logo_png.vtex_c", "panorama/images/textures/startup_background_logo_psd.vtex_c")
    }
    stringReplacements {
        // Heroes
        any("The Ancient Apparition", "Ronnie Coleman")
        any("Ancient Apparition", "Ronnie Coleman")
        any("Treant Protectors", "Trent Protector of Minorities")
        any("Treant Protector", "Trent Protector of Minorities")

        // Items
        any("Black King Bar", "sadKEK Bar")
        any("Diffusal Blade", "Diffushal Blade")
        any("Mango", "FatMango")
        any("Mangoes", "FatMangoes")
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
        id("DOTA_Chat_Rune_Haste", "%s1 activated an <font color='#F62817'>s4</font> rune.")
        id("DOTA_Chat_Rune_Bottle_Haste", "%s1 has bottled an <font color='#F62817'>s4</font> rune.")
        id("DOTA_Chat_Rune_Haste_Spotted", "<font color='#F62817'>s4</font> rune here (%s1).")
        id("DOTA_HUD_Rune_Haste", "<font color='#F62817'>s4</font> rune")

        // General
        id("dota_play", "Dota sadKEK")
        id("DOTA_FindMatch", "Find Smurfs")
        id("dota_play_searching", "Finding Smurfs")
        id("DOTA_Friends_WindowTitle", "PAID ACTORS")
        id("DOTA_PlayerSalute_ChatMessage", "tipped %s1. Git gud scrub! GOTTEM")
        any("invulnerable", "ungillable")
        any("Invulnerable", "Ungillable")
        id("DOTA_Chat_PlayerAbandoned", "%s1 has rage quit and abandoned the game! KEKW 'I've had it with these fucking 2ks KEKW'")
        id("DOTA_Paused", "ResidentSleeper")
        id("DOTA_Hud_Paused", "ResidentSleeper")
        id("DOTA_Hud_Paused_Spectating", "ResidentSleeper")

        // Chat wheel: text
        id("dota_chatwheel_label_Stun", "STUN HIM!")
        id("dota_chatwheel_message_Stun", "STUN HIM! WHAT ARE YOU DOING?! ARE YOU KIDDING ME?! Are you kidding?!")
        id("dota_chatwheel_label_Crickets", "I might die")
        id("dota_chatwheel_message_Crickets", "If I go in I might die, if I go in I might die")

        // Chat wheel: audio
        id("dota_chatwheel_label_weaver_thank", "Ronnie Coleman")
        id("dota_chatwheel_message_weaver_thank", "Ronnie Coleman, here I come!")
        id("dota_chatwheel_label_Frog", "Bruh")
        id("dota_chatwheel_message_Frog", "Bruh")
        id("dota_chatwheel_label_Crash_and_Burn", "Oh no no")
        id("dota_chatwheel_message_Crash_and_Burn", "Oh no no no PepeLaugh")
        id("dota_chatwheel_label_Crybaby", "BabyRage")
        id("dota_chatwheel_message_Crybaby", "BabyRage")
        id("dota_chatwheel_label_Kiss", "Fuck you")
        id("dota_chatwheel_message_Kiss", "Fuck ♂ you")
        id("dota_chatwheel_label_Crowd_Groan", "Cover the exits")
        id("dota_chatwheel_message_Crowd_Groan", "Dude, cover the exits! Are you kidding me? Are you fucking kidding me?!")
        id("dota_chatwheel_label_Headshake", "STUN HIM!")
        id("dota_chatwheel_message_Headshake", "STUN HIM! WHAT ARE YOU DOING?! ARE YOU KIDDING ME?! Are you kidding?!")

        // Match-making regions
        id("dota_matchgroup_useast", "KKona East")
        id("dota_matchgroup_uswest", "KKona West")
        id("dota_matchgroup_sa", "Laden South America")
        id("dota_matchgroup_hk", "MINGLEE Hong Kong")
        id("dota_matchgroup_cn", "MINGLEE China")
        id("dota_matchgroup_sea", "MINGLEE SE Asia")
        id("dota_matchgroup_eu", "3Head West")
        id("dota_matchgroup_ru", "KKomrade Russia")
        id("dota_matchgroup_vie", "3Head East")
        id("dota_matchgroup_au", "KKrikey Australia")
        id("dota_matchgroup_icn", "South Korea") // TODO: What to replace with?
        id("dota_matchgroup_cpt", "ZULUL South Africa")
        id("dota_matchgroup_jnb", "ZULUL South Africa")
        id("dota_matchgroup_pw", "Perfect World (MINGLEE)")
        id("dota_matchgroup_pw_telecom", "MINGLEE TC")
        id("dota_matchgroup_pw_telecom_shanghai", "MINGLEE TC Shanghai")
        id("dota_matchgroup_pw_telecom_guangdong", "MINGLEE TC Guangdong")
        id("dota_matchgroup_pw_telecom_zhejiang", "MINGLEE TC Zhejiang")
        id("dota_matchgroup_pw_telecom_wuhan", "MINGLEE TC Wuhan")
        id("dota_matchgroup_pw_unicom", "MINGLEE UC")
        id("dota_matchgroup_pw_unicom_tianjin", "MINGLEE UC 2")
        id("dota_matchgroup_dubai", "ANELE Dubai")
        id("dota_matchgroup_chile", "Laden Chile")
        id("dota_matchgroup_peru", "Laden Peru")
        id("dota_matchgroup_india", "ANELE India")
        id("dota_matchgroup_japan", "AYAYA Japan")
        id("dota_matchgroup_taiwan", "MINGLEE Taiwan")

        // Server locations
        id("dota_region_us_west", "KKona West")
        id("dota_region_us_east", "KKona East")
        id("dota_region_europe", "3Head West")
        id("dota_region_hong_kong", "MINGLEE Hong Kong")
        id("dota_region_singapore", "MINGLEE Singapore")
        id("dota_region_shanghai", "MINGLEE Shanghai")
        id("dota_region_beijing", "MINGLEE Beijing")
        id("dota_region_malaysia", "MINGLEE Malaysia")
        id("dota_region_stockholm", "ANELE Stockholm")
        id("dota_region_austria", "DatSheffy Austria")
        id("dota_region_amsterdam", "Amsterdam") // TODO: What to replace with?
        id("dota_region_brazil", "Laden Brazil")
        id("dota_region_australia", "KKrikey Australia")
        id("dota_region_korea", "South Korea") // TODO: What to replace with?
        id("dota_region_southafrica", "ZULUL South Africa")
        id("dota_region_pw", "Perfect World (MINGLEE)")
        id("dota_region_pw_telecom", "MINGLEE TC")
        id("dota_region_pw_telecom_shanghai", "MINGLEE TC Shanghai")
        id("dota_region_pw_telecom_guangdong", "MINGLEE TC Guangdong")
        id("dota_region_pw_telecom_zhejiang", "MINGLEE TC Zhejiang")
        id("dota_region_pw_telecom_wuhan", "MINGLEE TC Wuhan")
        id("dota_region_pw_unicom", "MINGLEE UC")
        id("dota_region_pw_unicom_tianjin", "MINGLEE UC 2")
        id("dota_region_dubai", "ANELE Dubai")
        id("dota_region_chile", "Laden Chile")
        id("dota_region_peru", "Laden Peru")
        id("dota_region_india", "ANELE India")
        id("dota_region_japan", "AYAYA Japan")
        id("dota_region_taiwan", "MINGLEE Taiwan")

        // Abandon/leave dialog
        id("dota_abandon_dialog_title", "BabyRage")
        id("dota_abandon_dialog_confirm", "Are you sure you want to abandon this game? HYPERMALD")
        id("dota_abandon_dialog_yes", "Yes, get me out")
        id("dota_leave_dialog_title", "BabyRage")
        id("dota_leave_dialog_confirm", "Are you sure you want to leave this game? HYPERMALD")
        id("dota_leave_dialog_yes", "Yes, get me out")

        // Post-game survey: positive
        id("DOTA_PlayerMatchSurvey_Positive_Title", "Yes POGCRAZY")
        id("DOTA_PlayerMatchSurvey_Positive_Teamwork", "I got carried by my team but won't admit it")
        id("DOTA_PlayerMatchSurvey_Positive_MatchBalance", "Close game :)")
        id("DOTA_PlayerMatchSurvey_Positive_TeamBehavior", "Team was berry nice, berry cool")
        id("DOTA_PlayerMatchSurvey_Positive_IndividualPerformance", "Team was shit but I carried :)")
        id("DOTA_PlayerMatchSurvey_Positive_HeroChoice", "5Head hero choices")
        id("DOTA_PlayerMatchSurvey_Positive_Comeback", "We (I) made (carried) a comeback")

        // Post-game survey: negative
        id("DOTA_PlayerMatchSurvey_Negative_Title", "N OMEGALUL")
        id("DOTA_PlayerMatchSurvey_Negative_Teamwork", "Team was shit and I didn't carry")
        id("DOTA_PlayerMatchSurvey_Negative_MatchBalance", "We got fucken rekt Sadge")
        id("DOTA_PlayerMatchSurvey_Negative_TeamBehavior", "Toxic behavior D:")
        id("DOTA_PlayerMatchSurvey_Negative_IndividualPerformance", "Mom, he stole my hero! BabyRage")
        id("DOTA_PlayerMatchSurvey_Negative_HeroChoice", "WTF are those picks?!")
        id("DOTA_PlayerMatchSurvey_Negative_Smurf", "Damn smurfs man! HYPERMALD")
    }
}