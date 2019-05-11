package mod.iceandshadow3.config;

import mod.iceandshadow3.IceAndShadow3;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public abstract class ConfigReflective extends BConfig {
	@Retention(RetentionPolicy.RUNTIME)
	protected static @interface Entry {
		boolean needsRestart() default false;
		String comment();
	}
	private final Set<String> opts;
	protected ConfigReflective() {
		opts = new HashSet<>();
		for(Field f : this.getClass().getFields()) {
			if(f.getAnnotation(Entry.class) != null) opts.add(f.getName());
		}
		IceAndShadow3.logger().debug(this.getClass().getName()+" has "+opts.size()+" config option(s).");
	}

	@Override
	final public Set<String> options() {
		return opts;
	}
	
	private Field getField(String option) {
		if(opts.contains(option)) {
			try {
				Field retval = this.getClass().getField(option);
				if(retval.getAnnotation(Entry.class) != null) return retval;
			} catch (NoSuchFieldException | SecurityException e) {
				IceAndShadow3.bug(e.getMessage() + " while accessing config field reflectively.");
				e.printStackTrace();
			}
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	final public boolean set(String option, String value) throws IllegalArgumentException, BadConfigException {
		final Field f = getField(option);
		Object result = SConfigValueTranslator.read(f.getType(), value);
		if(result == null) return true;
		if(f.getAnnotation(Entry.class).needsRestart() && isSealed()) return false;
		try {
			f.set(this, result);
		} catch (IllegalAccessException e) {
			IceAndShadow3.bug(e.getMessage() + " - @Entry applied to inaccessible config field: "+option);
			throw new IllegalArgumentException();
		}
		return true;
	}

	@Override
	final public String get(String option) throws IllegalArgumentException {
		try {
			return SConfigValueTranslator.write(getField(option).get(this));
		} catch (IllegalAccessException e) {
			IceAndShadow3.bug(e.getMessage() + " - @Entry applied to inaccessible config field: "+option);
			throw new IllegalArgumentException();
		}
	}

	@Override
	final public String getComment(String option) throws IllegalArgumentException {
		final Field f = getField(option);
		return f.getAnnotation(Entry.class).comment();
	}
}
