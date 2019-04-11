package mod.iceandshadow3.compat;

import mod.iceandshadow3.basics.Damage;
import mod.iceandshadow3.basics.EDamageShape;
import mod.iceandshadow3.basics.EDamageType;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

//TODO: Manually generated class stub.
public class CRefEntity implements IEffectSource {
	final Entity entity;
	
	CRefEntity(Entity e) {
		entity = e;
	}
	
	@Override
	public ITextComponent getNameTextComponent() {
		return entity.getDisplayName();
	}

	@Override
	public Entity getEffectSourceEntity() {
		return entity;
	}
	
	@Override
	public Damage getAttack() {
		if(entity instanceof IEffectSource) return ((IEffectSource)entity).getAttack();
		return null;
	}

	public void damage(Damage attack) {
		entity.attackEntityFrom(new ADamageSource(attack), attack.onDamage(this));
	}

}
