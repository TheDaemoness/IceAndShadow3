package mod.iceandshadow3.config;

import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.data.ITextLineRW;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/** Base class of IaS3's JAVA-ONLY CONFIG CLASSES, where each config option is a runtime-annotated public field.
 * This class is designed to trivialize adding a new config option to IaS3, at the expense of not being able to enforce contracts.
 * These fields, despite not being final, should not be modified directly.
 * This class uses Java reflection, and subtypes of it should ONLY be written in Java.
 */
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
		IaS3.logger().debug(this.getClass().getName()+" has "+opts.size()+" config option(s).");
	}

	@Override
	final public Set<String> options() {
		return opts;
	}
	
	private static class ConfigEntry {
		public ITextLineRW entry;
		public Entry metadata;
		ConfigEntry(ITextLineRW e, Entry m) {
			entry = e;
			metadata = m;
		}
	}
	
	private ConfigEntry getField(String option) {
		if(opts.contains(option)) {
			try {
				Field field = this.getClass().getField(option);
				final Object entry = field.get(this);
				final Entry metadata = field.getAnnotation(Entry.class); 
				if(metadata != null) {
					//Don't try to dodge class cast issues because that's a bug in IaS3 if it happens.
					return new ConfigEntry((ITextLineRW)entry, metadata);
				}
			} catch (NoSuchFieldException | SecurityException | IllegalAccessException | ClassCastException e) {
				IaS3.bug(this, e.getMessage() + " while accessing config field reflectively. Was @Entry misapplied?");
				e.printStackTrace();
			}
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	final public boolean set(String option, String value) throws IllegalArgumentException, BadConfigException {
		final ConfigEntry cfge = getField(option);
		if(cfge.metadata.needsRestart() && isSealed()) return false;
		try {
			cfge.entry.fromLine(value);
		} catch (IllegalArgumentException e) {
			throw new BadConfigException(e.getMessage());
		}
		return true;
	}

	@Override
	final public String get(String option) throws IllegalArgumentException {
		return getField(option).entry.toLine();
	}

	@Override
	final public String getComment(String option) throws IllegalArgumentException {
		return getField(option).metadata.comment();
	}
}
