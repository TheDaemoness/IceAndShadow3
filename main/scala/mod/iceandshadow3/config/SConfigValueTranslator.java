package mod.iceandshadow3.config;

import java.util.HashMap;
import java.util.function.Function;

import mod.iceandshadow3.IceAndShadow3;

final class SConfigValueTranslator {
	@SuppressWarnings("rawtypes")
	private static final HashMap<Class, Function<String, Object>> parsers = new HashMap<>();
	
	static {
		parsers.put(Boolean.class, Boolean::valueOf);
		parsers.put(Integer.class, Integer::valueOf);
	}
	static <T> T read(Class<T> type, String value) throws BadConfigException, IllegalStateException {
		final Function<String, Object> reader = parsers.get(type);
		if(reader == null) {
			IceAndShadow3.bug("No config value parser found for " + type.getName() + '.');
			return null;
		}
		try {
			final Object retval = reader.apply(value);
			if(!type.isInstance(type)) {
				IceAndShadow3.bug("The parser function for " + type.getName() + " returns " + retval.getClass().getName() + '.');
				return null;
			}
			return (T)retval;
		} catch(Exception e) {
			throw new BadConfigException(e.getMessage());
		}
	}
	static <T> String write(T obj) {
		return obj.toString(); //Maybe fancier later.
	}
}
