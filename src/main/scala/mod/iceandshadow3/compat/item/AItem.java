package mod.iceandshadow3.compat.item;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.item.BItemProperty;
import mod.iceandshadow3.basics.util.LogicPair;
import mod.iceandshadow3.compat.CNbtTree;
import mod.iceandshadow3.compat.ILogicItemProvider;
import mod.iceandshadow3.compat.entity.CRefEntity;
import mod.iceandshadow3.compat.entity.CRefPlayer;
import mod.iceandshadow3.compat.world.CWorld;
import mod.iceandshadow3.util.L3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AItem extends Item implements ILogicItemProvider {

	private EnumActionResult toEActionResult(L3 in) {
		switch(in) {
			case TRUE: return EnumActionResult.SUCCESS;
			case FALSE: return EnumActionResult.FAIL;
			default: return EnumActionResult.PASS;
		}
	}
	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || logic.isShiny(
			variant,
			new CNbtTree(stack.getTag()),
			new CRefItem(stack, null)
		);
	}

	private final BLogicItem logic;
	private final int variant;
	public AItem(BLogicItem itemlogic, int variant) {
		super(itemlogic.toItemProperties(variant));
		logic = itemlogic;
		this.setRegistryName(IaS3.MODID, logic.getName(variant));
		this.variant = variant;
		for(BItemProperty bpo : logic.propertyOverrides()) {
			this.addPropertyOverride(new ResourceLocation(IaS3.MODID, bpo.name()), new IItemPropertyGetter() {
				final BItemProperty impl = bpo;

				@OnlyIn(Dist.CLIENT)
				@Override
				public float call(@Nonnull ItemStack is, @Nullable World world, @Nullable EntityLivingBase owner) {
					return impl.call(new CRefItem(is, owner), new CWorld(world));
				}
			});
		}
	}

	@Nonnull
	@Override
	public LogicPair<BLogicItem> getLogicPair() {
		return new LogicPair<>(logic, variant);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		final boolean mainhand = handIn == EnumHand.MAIN_HAND;
		final ItemStack is = mainhand?playerIn.getHeldItemMainhand():playerIn.getHeldItemOffhand();
		final CRefItem cri = new CRefItem(is, playerIn);
		final CRefPlayer plai = CRefEntity.wrap(playerIn);
		final L3 result = logic.onUse(variant, cri.exposeStateData(getLogicPair()), cri, plai, mainhand);
		return new ActionResult<>(toEActionResult(result), cri.exposeItems());
	}
}