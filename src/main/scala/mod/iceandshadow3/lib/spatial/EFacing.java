package mod.iceandshadow3.lib.spatial;

public enum EFacing {
	DOWN(EAxis.DOWN_UP, false),
	UP(EAxis.DOWN_UP, true),
	WEST(EAxis.WEST_EAST, false),
	EAST(EAxis.WEST_EAST, true),
	NORTH(EAxis.NORTH_SOUTH, false),
	SOUTH(EAxis.NORTH_SOUTH, true);

	public final EAxis axis;
	public final boolean positive;

	EFacing(EAxis axis, boolean positive) {
		this.axis = axis;
		this.positive = positive;
	}

	public EFacing mirrored() {
		return axis.facing(!positive);
	}

	/** Return the result of rotating this direction COUNTER-CLOCKWISE. */
	public EFacing rotated(EAxis axis) {
		final EAxis newaxis = this.axis.rotated(axis);
		return newaxis.facing(positive ^ this.axis.shouldInvert(newaxis));
	}
	/** Return the result of rotating this direction COUNTER-CLOCKWISE. */
	public EFacing rotated(EFacing vec) {
		final EFacing rotated = this.rotated(vec.axis);
		return vec.positive ? rotated : rotated.mirrored();
	}
	/** Return the result of rotating this direction CLOCKWISE. */
	public EFacing rotatedCw(EFacing vec) {
		final EFacing rotated = this.rotated(vec.axis);
		return vec.positive ? rotated.mirrored() : rotated;
	}
	/** Return the result of rotating this direction CLOCKWISE. */
	public EFacing rotatedCw(EAxis axis) {
		return rotated(axis).mirrored();
	}
}
