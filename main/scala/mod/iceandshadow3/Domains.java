package mod.iceandshadow3;

import java.util.LinkedList;
import java.util.List;

import mod.iceandshadow3.compat.CRegistryBlock;
import mod.iceandshadow3.compat.CRegistryItem;
import mod.iceandshadow3.world.nyx.DomainNyx$;

/**
 * Class exists for the sole reason of not having to type $.MODULE$ so many times.
 * That and triggering singleton constructors.
 */
public class Domains {
	private static final List<BDomain> domains = new LinkedList<>();
	public static final DomainNyx$ NYX = DomainNyx$.MODULE$;
	
	static final void addDomain(BDomain domain) {
		domains.add(domain);
	}
	static final void initEarly() {
		for(BDomain domain : domains) domain.initEarly();
	}
	static final void registerBlocks(CRegistryBlock reg) {
		for(BDomain domain : domains) domain.register(reg);
	}
	static final void registerItems(CRegistryItem reg) {
		for(BDomain domain : domains) domain.register(reg);
	}
	static final void initLate() {
		for(BDomain domain : domains) domain.initLate();
	}
}
