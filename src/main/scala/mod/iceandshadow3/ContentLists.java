package mod.iceandshadow3;

//Keep this in Java for convenience. This is used by tests written in Java.

import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.BStatusEffect;

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
