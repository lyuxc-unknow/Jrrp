package mod.lyuxc;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public class JrrpCommand extends CommandBase implements ICommand {
    @Override
    public String getName() {
        return "jrrp";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/jrrp";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)  {
        Jrrp.JrrpEvent(Objects.requireNonNull(sender.getCommandSenderEntity()));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
