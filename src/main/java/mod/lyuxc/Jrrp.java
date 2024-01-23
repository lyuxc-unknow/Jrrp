package mod.lyuxc;

import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.*;

@Mod("jrrp")
@Mod.EventBusSubscriber
public class Jrrp {
    private static ForgeConfigSpec.ConfigValue<String> tip;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_LOW;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_HIGH;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> PROMPT_WORD_HIGHEST;
    private static ForgeConfigSpec.ConfigValue<Boolean> ON_LOGGING_TIP;
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final String DAY_OF_YEAR = String.valueOf(CALENDAR.get(Calendar.DAY_OF_YEAR));
    private static final Random RANDOM_SOURCE = new Random();

    public Jrrp() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        tip = builder.define("Jrrp提示:","今日人品为:$v $w");
        PROMPT_WORD_LOW = builder.defineList("提示词[0-50]",List.of(""),obj -> true);
        PROMPT_WORD_HIGH = builder.defineList("提示词[51-90]",List.of(""),obj -> true);
        PROMPT_WORD_HIGHEST = builder.defineList("提示词[91-100]",List.of(""),obj -> true);
        ON_LOGGING_TIP = builder.define("进入世界时是否自动提示",false);
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
        player.sendMessage(new TextComponent(tipsText), ChatType.SYSTEM, UUID.randomUUID());
        return 0;
    }
}