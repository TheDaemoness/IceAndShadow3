package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WIdBlock;
import net.minecraft.block.SlabBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ABlockSlab extends SlabBlock implements IABlock {
	private final BLogicBlock logic;
	public ABlockSlab(BLogicBlock source) {
		super(LogicToProperties$.MODULE$.toProperties(source));
		logic = source;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WIdBlock id() {
		return logic.id();
	}
}
