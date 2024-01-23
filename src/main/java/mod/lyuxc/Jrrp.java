package mod.lyuxc;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

@Mod(modid = "jrrp",name = "Jrrp",version = "1.0.0")
@Mod.EventBusSubscriber
public class Jrrp{
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final String DAY_OF_YEAR = String.valueOf(CALENDAR.get(Calendar.DAY_OF_YEAR));
    private static final Random RANDOM_SOURCE = new Random();
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new JrrpCommand());
    }
    public static void JrrpEvent(Entity entity) {
        NBTTagCompound compoundTag = entity.getEntityData();
        if (compoundTag.getString("jrrpLaseTime")==null||compoundTag.getString("jrrp") == null || !Objects.requireNonNull(compoundTag.getString("jrrpLaseTime")).equals(DAY_OF_YEAR)) {
            entity.getEntityData().setString("jrrpLaseTime", DAY_OF_YEAR);
            entity.getEntityData().setString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
            compoundTag.setString("jrrpLaseTime", DAY_OF_YEAR);
            compoundTag.setString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
        }
        int jrrpValue = Integer.parseInt(Objects.requireNonNull(compoundTag.getString("jrrp")));
        String tipsText = JrrpConfig.tipsText;
        if (jrrpValue < 50) {
            tipsText = tipsText.replace("$w",JrrpConfig.PROMPT_WORD_LOW[RANDOM_SOURCE.nextInt(JrrpConfig.PROMPT_WORD_LOW.length)]);
        } else if(jrrpValue < 90) {
            tipsText = tipsText.replace("$w",JrrpConfig.PROMPT_WORD_HIGH[RANDOM_SOURCE.nextInt(JrrpConfig.PROMPT_WORD_HIGH.length)]);
        } else {
            tipsText = tipsText.replace("$w",JrrpConfig.PROMPT_WORD_HIGHEST[RANDOM_SOURCE.nextInt(JrrpConfig.PROMPT_WORD_HIGHEST.length)]);
        }
        tipsText = tipsText.replace("$v",String.valueOf(jrrpValue)).replace("\\n","\n");
        entity.sendMessage(new TextComponentString(tipsText));
    }
}
