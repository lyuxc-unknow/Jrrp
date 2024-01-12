package mod.lyuxc;

import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Mod("jrrp")
@Mod.EventBusSubscriber
public class Jrrp {
    private static ModConfigSpec.ConfigValue<String> tip;
    private static ModConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_LOW;
    private static ModConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_HIGH;
    private static ModConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_HIGHEST;
    private static final Calendar calendar = Calendar.getInstance();
    private static final String DAY_OF_YEAR = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();
    private static String tipsText;
    private static int JrrpValue = 0;
    public Jrrp() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        tip = builder.define("Jrrp提示:","今日人品为:$v $w");
        PROMPT_WORD_LOW = builder.defineList("提示词[0-50]",List.of(""),obj -> true);
        PROMPT_WORD_HIGH = builder.defineList("提示词[51-90]",List.of(""),obj -> true);
        PROMPT_WORD_HIGHEST = builder.defineList("提示词[91-100]",List.of(""),obj -> true);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("jrrp")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompoundTag compoundTag = player.getPersistentData();
                    if ((compoundTag.get("jrrpLaseTime") == null || compoundTag.get("jrrp") == null) || (!Objects.requireNonNull(compoundTag.get("jrrpLaseTime")).getAsString().equals(DAY_OF_YEAR))) {
                        player.getPersistentData().putString("jrrpLaseTime", DAY_OF_YEAR);
                        player.getPersistentData().putString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
                        compoundTag.putString("jrrpLaseTime", DAY_OF_YEAR);
                        compoundTag.putString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
                        player.save(compoundTag);
                    }
                    JrrpValue = Integer.parseInt(Objects.requireNonNull(compoundTag.get("jrrp")).getAsString());
                    if (JrrpValue < 50) {
                        tipsText = tip.get().replace("$w",PROMPT_WORD_LOW.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_LOW.get().size())));
                    } else if(JrrpValue < 90) {
                        tipsText = tip.get().replace("$w",PROMPT_WORD_HIGH.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_HIGH.get().size())));
                    } else {
                        tipsText = tip.get().replace("$w",PROMPT_WORD_HIGHEST.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_HIGHEST.get().size())));
                    }
                    tipsText=tipsText.replace("$v",String.valueOf(JrrpValue));
                    player.sendSystemMessage(Component.literal(tipsText));
                    return 0;
                })
        );
    }
}
