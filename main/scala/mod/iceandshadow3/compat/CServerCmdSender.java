package mod.iceandshadow3.compat;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Wrapper for a ICommandSender+MinecraftServer pair.
 * Fairly incomplete; will be extended when need be.
 */
public class CServerCmdSender {
	final MinecraftServer server;
	final ICommandSender sender;
	
	public CServerCmdSender(MinecraftServer server, ICommandSender sender) {
		this.server = server;
		this.sender = sender;
	}
	public CServerCmdSender(ICommandSender sender) {
		this(sender.getServer(), sender);
	}
	public String getSenderName() {
		return sender.getName();
	}
	public boolean isAdmin() {
		return sender.canUseCommand(3, "");
	}
}
