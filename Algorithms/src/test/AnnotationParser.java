package test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationParser {

	public static Map<String, Object> setAlias(Map<String, Object> data,Class... classes) {
		Map<String, Object> result = new HashMap<>();
		for (Class _class : classes) {
			Field[] fields = _class.getDeclaredFields();
			for (Field field : fields) {
				AliasAnnotation annotation = field.getAnnotation(AliasAnnotation.class);
				String fieldName = field.getName();
				Object fieldValue = data.get(fieldName);
				if(annotation != null && fieldValue != null) {
					result.put(annotation.alias(), fieldValue);
				}else {
					result.put(fieldName, fieldValue);
				}
			}
		}

		return result;
	}
}
