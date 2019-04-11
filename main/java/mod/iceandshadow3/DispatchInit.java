package mod.iceandshadow3;

import mod.iceandshadow3.compat.CDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class DispatchInit {
	static void initEarly(Side side) {
		CDimension.NYX.register(Config.nyx, "Nyx", "_ias3_nyx");
	}
	static void initRegistries() {}
	static void initLate(Side side) {}
	static void fillRegister(RegistryEvent.Register r) {}
	static void addCommands(FMLServerStartingEvent event) {}
}
