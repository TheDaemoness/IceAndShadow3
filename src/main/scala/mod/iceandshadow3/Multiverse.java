package mod.iceandshadow3;

import mod.iceandshadow3.basics.BDimension;
import mod.iceandshadow3.basics.BDomain;
import mod.iceandshadow3.compat.block.ABlock;
import mod.iceandshadow3.compat.block.BinderBlock$;
import mod.iceandshadow3.compat.client.AParticleType;
import mod.iceandshadow3.compat.client.BinderParticle$;
import mod.iceandshadow3.compat.dimension.AModDimension;
import mod.iceandshadow3.compat.dimension.WDimensionCoord;
import mod.iceandshadow3.compat.entity.AStatusEffect;
import mod.iceandshadow3.compat.entity.BinderStatusEffect$;
import mod.iceandshadow3.compat.item.AItem;
import mod.iceandshadow3.compat.item.AItemBlock;
import mod.iceandshadow3.compat.item.BinderItem$;
import mod.iceandshadow3.forge.Teleporter$;
import mod.iceandshadow3.world.DimensionNyx$;
import mod.iceandshadow3.world.DomainAlien$;
import mod.iceandshadow3.world.DomainGaia$;
import mod.iceandshadow3.world.DomainNyx$;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistry;
import scala.Tuple2;

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
	private static Tuple2<ABlock, AItemBlock>[][] blockBindings;

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
		blockBindings = BinderBlock$.MODULE$.freeze();
	}
	static void registerBlocks(IForgeRegistry<Block> reg) {
		for(Tuple2<ABlock, AItemBlock>[] bindings : blockBindings) {
			for(Tuple2<ABlock, AItemBlock> binding : bindings) {
				reg.register(binding._1());
			}
		}
	}
	static void registerItems(IForgeRegistry<Item> reg) {
		for(AItem[] items : BinderItem$.MODULE$.freeze()) {
			for(AItem item: items) reg.register(item);
		}
		for(Tuple2<ABlock, AItemBlock>[] bindings : blockBindings) {
			for(Tuple2<ABlock, AItemBlock> binding : bindings) {
				if(binding._2() != null) reg.register(binding._2());
			}
		}
	}

	static void registerDimensions(IForgeRegistry<ModDimension> reg) {
		sealDimensions = true;
		for(AModDimension dim : dimensions) reg.register(dim);
	}
	static void registerBiomes(IForgeRegistry<Biome> reg) {
		sealDimensions = true;
		for(AModDimension dim : dimensions) reg.register(dim.dimbiome);
	}
	static void registerPots(IForgeRegistry<Effect> reg) {
		for(Effect fx : BinderStatusEffect$.MODULE$.freeze()) {
			if(fx instanceof AStatusEffect) reg.register(fx);
		}
	}
	static void registerParticles(IForgeRegistry<ParticleType<?>> reg) {
		for(IParticleData fx : BinderParticle$.MODULE$.freeze()) {
			if(fx instanceof AParticleType) reg.register((AParticleType)fx);
		}
	}

	public static BDimension lookup(DimensionType where) {
		for(AModDimension dim: dimensions) {
			WDimensionCoord wdc = dim.getIaSDimension().coord();
			if(wdc != null && wdc.dimtype() == where) return dim.getIaSDimension();
		}
		return null;
	}

	static void enableDimensions() {
		for(AModDimension dim : dimensions) dim.enable();
	}

	static void primeDimensions(MinecraftServer server) {
		//TODO: ALL OF THIS IS A TEMPORARY HACK. COME UP WITH SOMETHING BETTER!
		class RestartRequired extends RuntimeException {
			private RestartRequired() {
				super("Please restart the dedicated server.");
			}
		}

		boolean foundOne = false;
		for(AModDimension dim : dimensions) {
			final BDimension iasdim = dim.getIaSDimension();
			if(iasdim.coord() == null) {
				dim.enable();
				foundOne = true;
			}
		}
		if(foundOne) throw new RestartRequired();
	}

	static void initLate() {
		Teleporter$.MODULE$.registerSelf();
		for(BDomain domain : domains) domain.initLate();
	}
}
