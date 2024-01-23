package mod.lyuxc;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class JrrpCommand extends CommandBase implements ICommand {
    private static final Calendar CALENDAR = Calendar.getInstance();
    private static final String DAY_OF_YEAR = String.valueOf(CALENDAR.get(Calendar.DAY_OF_YEAR));
    private static final Random RANDOM_SOURCE = new Random();
    @Override
    public String getName() {
        return "jrrp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/jrrp";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        NBTTagCompound compoundTag = Objects.requireNonNull(sender.getCommandSenderEntity()).getEntityData();
        if (compoundTag.getString("jrrpLaseTime")==null||compoundTag.getString("jrrp") == null || !Objects.requireNonNull(compoundTag.getString("jrrpLaseTime")).equals(DAY_OF_YEAR)) {
            sender.getCommandSenderEntity().getEntityData().setString("jrrpLaseTime", DAY_OF_YEAR);
            sender.getCommandSenderEntity().getEntityData().setString("jrrp", String.valueOf(RANDOM_SOURCE.nextInt(101)));
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
        sender.sendMessage(new TextComponentString(tipsText));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
