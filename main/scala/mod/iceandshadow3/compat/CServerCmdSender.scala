package mod.iceandshadow3.compat;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Wrapper for a ICommandSender+MinecraftServer pair.
 * Fairly incomplete; will be extended when need be.
 */
class CServerCmdSender(
	private[compat] val server: MinecraftServer,
	private[compat] val sender: ICommandSender
) {
	def this(sender: ICommandSender) = this(sender.getServer(), sender)
	def getSenderName(): java.lang.String = sender.getName();
	def isAdmin(): Boolean = return sender.canUseCommand(3, "");
}
