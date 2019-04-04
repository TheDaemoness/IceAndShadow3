package mod.iceandshadow3.compat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mod.iceandshadow3.cmd.*;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class AServerCmd implements ICommand {
	protected final IServerCmd cmd;
	protected String usage(String name) {
		List<String> usages = cmd.getArgsUsage(name);
		String retval = "";
		if(usages == null) {
			usages = new java.util.ArrayList<String>(1);
			usages.add(null);
		}
		for(String use : usages) {
			retval += "\nias-"+cmd.getName();
			if(use != null) retval += " "+use; 
		}
		return retval.stripLeading();
	}
	AServerCmd(IServerCmd cmd) {
		this.cmd = cmd;
	}

	@Override
	public String getName() {
		return "ias-"+cmd;
	}
	@Override
	public List<String> getAliases() {
		return null;
	}

	@Override
	public String getUsage(ICommandSender snder) {
		return usage(new CServerCmdSender(snder).getSenderName());
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender snder, String[] args,
			BlockPos targetPos) {
		//TODO: Don't discard the BlockPos.
		CServerCmdSender sender = new CServerCmdSender(server, snder);
		if(args.length < cmd.getArgsMax()) {
			return cmd.getAutocomplete(Arrays.asList(args), sender.getSenderName());
		}
		return null;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return new CServerCmdSender(server, sender).isAdmin();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		//TODO: i18n messages!
		List<String> subargs = Arrays.asList(args);
		CServerCmdSender cmder = new CServerCmdSender(server, sender);
		if(subargs.size() > cmd.getArgsMax() || subargs.size() < cmd.getArgsMin()) {
			throw new WrongUsageException(usage(cmder.getSenderName()));
		}
		Object parsed = cmd.parse(subargs);
		if(parsed == null) throw new WrongUsageException(usage(cmder.getSenderName()));
		String errmsg = cmd.run(parsed, cmder);
		if(errmsg != null) throw new CommandException(errmsg);
	}
	
	@Override
	public int compareTo(ICommand arg0) {
		return getName().compareTo(arg0.getName());
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return cmd.isUsername(Arrays.asList(args), index);
	}

}
