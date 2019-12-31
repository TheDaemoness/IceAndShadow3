package mod.iceandshadow3.lib.compat.block.impl;

import mod.iceandshadow3.lib.util.collect.OrdSet;
import net.minecraft.state.IProperty;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public class AProperty implements IProperty<Integer> {
	private final BVarBlockNew logic;
	private final Collection<Integer> values;

	AProperty(BVarBlockNew<?> bbv) {
		logic = bbv;
		values = new OrdSet(logic.size()).toJava();
	}

	@Override
	@Nonnull
	public String getName() {
		return logic.name();
	}

	@Override
	@Nonnull
	public Collection<Integer> getAllowedValues() {
		return values;
	}

	@Override
	@Nonnull
	public Class<Integer> getValueClass() {
		return Integer.class;
	}

	@Override
	@Nonnull
	public Optional<Integer> parseValue(@Nonnull String value) {
		final int idx = logic.fromString(value);
		return idx != -1 ? Optional.of(idx) : Optional.empty();
	}

	@Override
	@Nonnull
	public String getName(@Nonnull Integer value) {
		return logic.toString(value);
	}
}
