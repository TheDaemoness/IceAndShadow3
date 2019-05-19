package mod.iceandshadow3.util;

/** Simplistic internal-use interface for resetable objects. Mainly used for
 * iterators/suppliers and the like to avoid having to repeatedly reconstruct
 * the same objects.
 */
public interface IResetable {
	boolean reset();
}
