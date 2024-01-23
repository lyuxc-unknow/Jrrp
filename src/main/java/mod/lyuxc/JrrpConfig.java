package mod.lyuxc;

import net.minecraftforge.common.config.Config;

@Config(modid = "jrrp")
public class JrrpConfig {
    @Config.RequiresMcRestart
    @Config.Comment("Jrrp tips")
    public static String tipsText = "Your character value today is $v$w";
    @Config.RequiresMcRestart
    @Config.Comment("Jrrp prompt words [0~50]")
    public static String[] PROMPT_WORD_LOW = new String[]{""};
    @Config.RequiresMcRestart
    @Config.Comment("Jrrp prompt words [51~90]")
    public static String[] PROMPT_WORD_HIGH = new String[]{""};
    @Config.RequiresMcRestart
    @Config.Comment("Jrrp prompt words [91~100]")
    public static String[] PROMPT_WORD_HIGHEST = new String[]{""};
}
