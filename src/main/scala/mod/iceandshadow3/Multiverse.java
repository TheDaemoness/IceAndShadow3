package mod.iceandshadow3;

import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.basics.BDomain;
import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;
import mod.iceandshadow3.compat.dimension.AModDimension;
import mod.iceandshadow3.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.LinkedList;
import java.util.List;

public final class Multiverse {
	private static final List<BDomain> domains = new LinkedList<>();
	private static final List<AModDimension> dimensions = new LinkedList<>();
	public static final DomainAlien$ ALIEN = DomainAlien$.MODULE$;
	public static final DomainNyx$ NYX = DomainNyx$.MODULE$;
	public static final DomainGaia$ GAIA = DomainGaia$.MODULE$;
	public static final DimensionNyx$ DIM_NYX = DimensionNyx$.MODULE$;
	private static boolean sealDomains = false, sealDimensions = false;

	/** Called by BDomains during construction to self-enable.
	 */
	public static void addDomain(BDomain domain) {
		if(!sealDomains) domains.add(domain);
		else IaS3.bug(domain,"Domain constructed too late.");
	}
	/** Called by BDimensions during construction to self-enable.
	 */
	public static void addDimension(BDimension dim) {
		if(!sealDimensions) dimensions.add(new AModDimension(dim));
		else IaS3.bug(dim,"Dimension constructed too late.");
	}
	static void initEarly() {
		for(BDomain domain : domains) domain.initEarly();
		sealDomains = true;
	}
	static void registerBlocks(CRegistryBlock reg) {
		for(BDomain domain : domains) domain.register(reg);
	}
	static void registerItems(CRegistryItem reg) {
		for(BDomain domain : domains) domain.register(reg);
	}

	static void registerDimensions(IForgeRegistry<ModDimension> reg) {
		sealDimensions = true;
		for(AModDimension dim : dimensions) reg.register(dim);
	}
	static void registerBiomes(IForgeRegistry<Biome> reg) {
		sealDimensions = true;
		for(AModDimension dim : dimensions) reg.register(dim.dimbiome);
	}

	static void enableDimensions() {
		for(AModDimension dim : dimensions) dim.enable();
	}

	static void initLate() {
		for(BDomain domain : domains) domain.initLate();
	}
}
