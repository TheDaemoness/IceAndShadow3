package mod.iceandshadow3;

import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.basics.BDomain;
import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;
import mod.iceandshadow3.compat.world.AModDimension;
import mod.iceandshadow3.world.*;

import java.util.LinkedList;
import java.util.List;

public final class Multiverse {
	private static final List<BDomain> domains = new LinkedList<>();
	private static final List<AModDimension> dimensions = new LinkedList<>();
	public static final DomainAlien$ ALIEN = DomainAlien$.MODULE$;
	public static final DomainNyx$ NYX = DomainNyx$.MODULE$;
	public static final DomainGaia$ GAIA = DomainGaia$.MODULE$;
	//public static final DimensionNyx$ DIM_NYX = DimensionNyx$.MODULE$;
	private static boolean sealDomains = false, autoRegisterDimension = false;

	/** Called by BDomains during construction to self-register.
	 */
	public static void addDomain(BDomain domain) {
		if(!sealDomains) domains.add(domain);
		else IaS3.bug(domain,"Domain constructed too late.");
	}
	/** Called by BDimensions during construction to self-register.
	 */
	public static boolean addDimension(BDimension dim) {
		AModDimension wrapped = new AModDimension(dim);
		if(autoRegisterDimension) {
			IaS3.logger().info("Adding late dimension "+dim.name());
			wrapped.register();
		} else dimensions.add(wrapped);
		return autoRegisterDimension;
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

	static void registerDimensions() {
		autoRegisterDimension = true;
		for(AModDimension dim : dimensions) registerDim(dim);
	}
	static private void registerDim(AModDimension what) {
		what.register();
	}
	static void initLate() {
		for(BDomain domain : domains) domain.initLate();
	}
}
