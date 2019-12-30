package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WId;
import net.minecraft.block.WallBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ABlockWall extends WallBlock implements IABlock {
	private final BLogicBlock logic;
	public ABlockWall(BLogicBlock blb) {
		super(LogicToProperties$.MODULE$.toProperties(blb));
		logic = blb;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
