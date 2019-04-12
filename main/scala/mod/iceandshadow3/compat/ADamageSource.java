package mod.iceandshadow3.compat;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import mod.iceandshadow3.basics.Damage;
import mod.iceandshadow3.basics.EDamageShape;
import mod.iceandshadow3.basics.EDamageType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

/**
 * Damage source.
 * TODO: Split away the IaS additions into a new class that constructs one of these.
 */
public class ADamageSource extends DamageSource {
	
	final Damage damage;
	
	ADamageSource(final Damage dmg) {
		super("ias3damage");
		this.setDamageIsAbsolute().setDamageBypassesArmor();
		damage = dmg;
	}

	@Override
	public ITextComponent getDeathMessage(EntityLivingBase elb) {
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        if(I18n.canTranslate(s1)) {
        	return new TextComponentTranslation(s1, elb.getDisplayName(), damage.getSource().getNameTextComponent());
        } else return new TextComponentTranslation(s, elb.getDisplayName());
	}
}
