package mod.lyuxc;

import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Calendar;
import java.util.Objects;

@Mod("jrrp")
@Mod.EventBusSubscriber
public class Jrrp {
    public static ModConfigSpec.ConfigValue<String> tip;
    public static String tipsText;
    public Jrrp(IEventBus iEventBus) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        tip = builder
                .comment("tips of Jrrp")
                .define("Jrrp Tip:","今日人品为:$v");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build());
    }
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("jrrp")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CompoundTag compoundTag = player.getPersistentData();
                    if ((compoundTag.get("jrrpLaseTime") == null || compoundTag.get("jrrp") == null) || (!Objects.requireNonNull(compoundTag.get("jrrpLaseTime")).getAsString().equals(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR))))) {
                        player.getPersistentData().putString("jrrpLaseTime", String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)));
                        player.getPersistentData().putString("jrrp", String.valueOf(RandomSource.create().nextInt(101)));
                        compoundTag.putString("jrrpLaseTime", String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)));
                        compoundTag.putString("jrrp", String.valueOf(RandomSource.create().nextInt(101)));
                        player.save(compoundTag);
                    }
                    tipsText=tip.get().replace("$v",Objects.requireNonNull(compoundTag.get("jrrp")).getAsString());
                    player.sendSystemMessage(Component.literal(tipsText));
                    return 0;
                })
        );
    }
}
