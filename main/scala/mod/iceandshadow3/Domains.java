package mod.iceandshadow3;

import java.util.LinkedList;
import java.util.List;

import mod.iceandshadow3.compat.block.CRegistryBlock;
import mod.iceandshadow3.compat.item.CRegistryItem;

import mod.iceandshadow3.world.BDomain;
import mod.iceandshadow3.world.DomainAlien$;
import mod.iceandshadow3.world.nyx.DomainNyx$;
import mod.iceandshadow3.world.gaia.DomainGaia$;


/**
 * Class exists for the sole reason of not having to type $.MODULE$ so many times.
 * That and triggering singleton constructors.
 */
public class Domains {
	private static final List<BDomain> domains = new LinkedList<>();
	public static final DomainAlien$ ALIEN = DomainAlien$.MODULE$;
	public static final DomainNyx$ NYX = DomainNyx$.MODULE$;
	public static final DomainGaia$ GAIA = DomainGaia$.MODULE$;
	private static boolean seal = false;
	
	public static final void addDomain(BDomain domain) {
		if(!seal) {
			domains.add(domain);
		} else throw new IllegalStateException("Domain \""+domain.name()+"\" initialized too late.");
	}
	static final void initEarly() {
		for(BDomain domain : domains) domain.initEarly();
		seal = true;
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
