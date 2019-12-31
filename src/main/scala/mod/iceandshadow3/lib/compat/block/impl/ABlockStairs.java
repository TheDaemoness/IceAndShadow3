package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.common.LogicBlockMateria;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.WIdBlock;
import net.minecraft.block.StairsBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ABlockStairs extends StairsBlock implements IABlock {
	private final BLogicBlock logic;
	public ABlockStairs(LogicBlockMateria source) {
		super(
			//Note that we very much want an exception to be thrown here.
			() -> source.relative().unapply().get().typeDefault().exposeBS(),
			LogicToProperties$.MODULE$.toProperties(source)
		);
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
