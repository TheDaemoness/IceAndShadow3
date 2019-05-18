package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.IceAndShadow3;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.BStateData;
import mod.iceandshadow3.compat.CNbtTree;
import mod.iceandshadow3.compat.ILogicStateProvider;
import mod.iceandshadow3.compat.entity.CRefPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class AItem extends Item implements ILogicStateProvider<BLogicItem, ItemStack> {

	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || il.isShiny(variant, new CNbtTree(stack.getTag()), new CRefItem(stack));
	}

	final BLogicItem il;
	final int variant;
	public AItem(BLogicItem itemlogic, int variant) {
		super(((BCompatLogicItem)itemlogic).toItemProperties(variant));
		il = itemlogic;
		this.setRegistryName(IceAndShadow3.MODID, il.getName(variant));
		this.variant = variant;
	}
	
	@Override
	public BLogicItem getLogic() {
		return il;
	}

	@Override
	public int getVariant() {
		return variant;
	}

	@Override
	public NBTTagCompound getNBT(ItemStack instance) {
		return instance.getOrCreateTag();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final boolean mainhand = handIn == EnumHand.MAIN_HAND;
		final ItemStack is = mainhand?playerIn.getHeldItemMainhand():playerIn.getHeldItemOffhand();
		final BStateData bsd = getStateData(is);
		final CRefItem cri = new CRefItem(is);
		EnumActionResult resultType;
		switch(il.onUse(variant, bsd, cri, new CRefPlayer(playerIn), mainhand)) {
			case TRUE: resultType = EnumActionResult.SUCCESS; break;
			case FALSE: resultType = EnumActionResult.FAIL; break;
			default: resultType = EnumActionResult.PASS; break; //Safety.
		}
		saveStateData(cri.is(), bsd);
		return new ActionResult<>(resultType, cri.is());
	}


}
