package mod.iceandshadow3;

//Keep this in Java for convenience. This is used by tests written in Java.

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.BStatusEffect;

import java.util.LinkedList;
import java.util.List;

/** Static lists of IaS3's content objects.
 * Purged at the end of normal init, or kept indefinitely in tool mode.
 */
public class ContentLists {
	public static final List<BLogicItem> item = new LinkedList<>();
	public static final List<BLogicBlock> block = new LinkedList<>();
	public static final List<BStatusEffect> status = new LinkedList<>();
	public static final List<String> soundname = new LinkedList<>();
	static void purge() {
		item.clear();
		block.clear();
		status.clear();
		soundname.clear();
	}
}
