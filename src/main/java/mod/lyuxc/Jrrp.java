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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

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
    private static ModConfigSpec.ConfigValue<Boolean> ON_LOGGING_TIP;
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final String DAY_OF_YEAR = String.valueOf(CALENDAR.get(Calendar.DAY_OF_YEAR));
    private static final RandomSource RANDOM_SOURCE = RandomSource.create();

    public Jrrp() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        tip = builder.define("Jrrp提示:","今日人品为:$v $w");
        PROMPT_WORD_LOW = builder.defineList("提示词[0-50]",List.of(""),obj -> true);
        PROMPT_WORD_HIGH = builder.defineList("提示词[51-90]",List.of(""),obj -> true);
        PROMPT_WORD_HIGHEST = builder.defineList("提示词[91-100]",List.of(""),obj -> true);
        ON_LOGGING_TIP = builder.define("进入时是否自动提示",false);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("jrrp")
                .executes(context -> JrrpEvent(context.getSource().getPlayerOrException()))
        );
    }

    @SubscribeEvent
    public static void PlayerLogging(PlayerEvent.PlayerLoggedInEvent event) {
        if(ON_LOGGING_TIP.get()) JrrpEvent((ServerPlayer) event.getEntity());
    }

    public static int JrrpEvent(ServerPlayer player) {
        CompoundTag compoundTag = player.getPersistentData();
        if ((compoundTag.get("jrrpLaseTime") == null || compoundTag.get("jrrp") == null) || (!Objects.requireNonNull(compoundTag.get("jrrpLaseTime")).getAsString().equals(DAY_OF_YEAR))) {
            player.getPersistentData().putString("jrrpLaseTime", DAY_OF_YEAR);
            player.getPersistentData().putString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
            compoundTag.putString("jrrpLaseTime", DAY_OF_YEAR);
            compoundTag.putString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
            player.save(compoundTag);
        }
        int jrrpValue = Integer.parseInt(Objects.requireNonNull(compoundTag.get("jrrp")).getAsString());
        String tipsText;
        if (jrrpValue < 50) {
            tipsText = tip.get().replace("$w",PROMPT_WORD_LOW.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_LOW.get().size())));
        } else if(jrrpValue < 90) {
            tipsText = tip.get().replace("$w",PROMPT_WORD_HIGH.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_HIGH.get().size())));
        } else {
            tipsText = tip.get().replace("$w",PROMPT_WORD_HIGHEST.get().get(RANDOM_SOURCE.nextInt(PROMPT_WORD_HIGHEST.get().size())));
        }
        tipsText = tipsText.replace("$v",String.valueOf(jrrpValue));
        player.displayClientMessage(Component.literal(tipsText),false);
        return 0;
    }
}
