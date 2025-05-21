package com.minispring.beans;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Simple Type Converter
 * Implements basic type conversion functionality
 */
public class SimpleTypeConverter implements TypeConverter {

    // Converter cache for performance
    private static final Map<Class<?>, Function<Object, Object>> CONVERTERS = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Object> convertCache = new ConcurrentHashMap<>();

    // Initialize converter mappings
    static {
        CONVERTERS.put(String.class, Object::toString);
        CONVERTERS.put(Integer.class, SimpleTypeConverter::convertToInteger);
        CONVERTERS.put(int.class, SimpleTypeConverter::convertToInteger);
        CONVERTERS.put(Long.class, SimpleTypeConverter::convertToLong);
        CONVERTERS.put(long.class, SimpleTypeConverter::convertToLong);
        CONVERTERS.put(Double.class, SimpleTypeConverter::convertToDouble);
        CONVERTERS.put(double.class, SimpleTypeConverter::convertToDouble);
        CONVERTERS.put(Float.class, SimpleTypeConverter::convertToFloat);
        CONVERTERS.put(float.class, SimpleTypeConverter::convertToFloat);
        CONVERTERS.put(Boolean.class, SimpleTypeConverter::convertToBoolean);
        CONVERTERS.put(boolean.class, SimpleTypeConverter::convertToBoolean);
        CONVERTERS.put(Character.class, SimpleTypeConverter::convertToCharacter);
        CONVERTERS.put(char.class, SimpleTypeConverter::convertToCharacter);
        CONVERTERS.put(Byte.class, SimpleTypeConverter::convertToByte);
        CONVERTERS.put(byte.class, SimpleTypeConverter::convertToByte);
        CONVERTERS.put(Short.class, SimpleTypeConverter::convertToShort);
        CONVERTERS.put(short.class, SimpleTypeConverter::convertToShort);
        
        // Add date-time type conversions
        CONVERTERS.put(LocalDate.class, SimpleTypeConverter::convertToLocalDate);
        CONVERTERS.put(LocalTime.class, SimpleTypeConverter::convertToLocalTime);
        CONVERTERS.put(LocalDateTime.class, SimpleTypeConverter::convertToLocalDateTime);
    }

    /**
     * Get list of supported types
     * @return set of supported types
     */
    public Set<Class<?>> getSupportedTypes() {
        return Collections.unmodifiableSet(CONVERTERS.keySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, Class<T> requiredType) throws TypeMismatchException {
        if (value == null) {
            return null;
        }
        
        // If already target type, return directly
        if (requiredType.isInstance(value)) {
            return (T) value;
        }
        
        // Check cache
        String cacheKey = value.toString() + "_" + requiredType.getName();
        Object cachedValue = convertCache.get(cacheKey);
        if (cachedValue != null) {
            return (T) cachedValue;
        }
        
        try {
            Object convertedValue;
            
            // String conversion
            if (value instanceof String) {
                String stringValue = (String) value;
                convertedValue = convertFromString(stringValue, requiredType);
            }
            // Number type conversions
            else if (value instanceof Number) {
                convertedValue = convertNumber((Number) value, requiredType);
            }
            // Other type to string conversion
            else {
                convertedValue = convertToString(value, requiredType);
            }
            
            // Cache conversion result
            if (convertedValue != null) {
                convertCache.put(cacheKey, convertedValue);
            }
            
            return (T) convertedValue;
            
        } catch (Exception e) {
            throw new TypeMismatchException(value, requiredType, e);
        }
    }
    
    /**
     * Convert from string to target type
     */
    private Object convertFromString(String value, Class<?> requiredType) {
        // Basic type conversions
        if (requiredType == Integer.class || requiredType == int.class) {
            return Integer.parseInt(value);
        }
        if (requiredType == Long.class || requiredType == long.class) {
            return Long.parseLong(value);
        }
        if (requiredType == Double.class || requiredType == double.class) {
            return Double.parseDouble(value);
        }
        if (requiredType == Float.class || requiredType == float.class) {
            return Float.parseFloat(value);
        }
        if (requiredType == Boolean.class || requiredType == boolean.class) {
            String stringValue = value.toLowerCase().trim();
            if ("true".equals(stringValue) || 
                "yes".equals(stringValue) || 
                "1".equals(stringValue) || 
                "on".equals(stringValue) ||
                "y".equals(stringValue)) {
                return true;
            } else if ("false".equals(stringValue) || 
                      "no".equals(stringValue) || 
                      "0".equals(stringValue) || 
                      "off".equals(stringValue) ||
                      "n".equals(stringValue)) {
                return false;
            }
            throw new IllegalArgumentException("Cannot convert string [" + value + "] to Boolean");
        }
        if (requiredType == Byte.class || requiredType == byte.class) {
            return Byte.parseByte(value);
        }
        if (requiredType == Short.class || requiredType == short.class) {
            return Short.parseShort(value);
        }
        if (requiredType == Character.class || requiredType == char.class) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("Cannot convert string [" + value + "] to Character, string length must be 1");
            }
            return value.charAt(0);
        }
        
        // Date-time type conversions
        if (requiredType == LocalDate.class) {
            return LocalDate.parse(value);
        }
        if (requiredType == LocalTime.class) {
            return LocalTime.parse(value);
        }
        if (requiredType == LocalDateTime.class) {
            try {
                // Try parsing with ISO format
                return LocalDateTime.parse(value);
            } catch (DateTimeParseException e) {
                // Try parsing with common formats
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(value, formatter);
                    } catch (DateTimeParseException ignored) {
                        // Continue trying next format
                    }
                }
                
                // If pure date format, try converting to start of day
                try {
                    return LocalDate.parse(value).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // Continue trying other formats
                }
                
                throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalDateTime, supported formats: " +
                    "ISO, yyyy-MM-dd HH:mm:ss, yyyy/MM/dd HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss, " +
                    "yyyy.MM.dd HH:mm:ss, yyyy-MM-dd HH:mm, yyyy/MM/dd HH:mm, yyyy-MM-dd");
            }
        }
        
        // If target type is String, return directly
        if (requiredType == String.class) {
            return value;
        }
        
        throw new TypeMismatchException("Unsupported type conversion: " + value.getClass().getName() + 
                " -> " + requiredType.getName());
    }
    
    /**
     * Number type conversions
     */
    private Object convertNumber(Number value, Class<?> requiredType) {
        if (requiredType == Integer.class || requiredType == int.class) {
            return value.intValue();
        }
        if (requiredType == Long.class || requiredType == long.class) {
            return value.longValue();
        }
        if (requiredType == Double.class || requiredType == double.class) {
            return value.doubleValue();
        }
        if (requiredType == Float.class || requiredType == float.class) {
            return value.floatValue();
        }
        if (requiredType == Byte.class || requiredType == byte.class) {
            return value.byteValue();
        }
        if (requiredType == Short.class || requiredType == short.class) {
            return value.shortValue();
        }
        if (requiredType == String.class) {
            return value.toString();
        }
        
        throw new TypeMismatchException("Unsupported number type conversion: " + value.getClass().getName() + 
                " -> " + requiredType.getName());
    }
    
    /**
     * Convert to string
     */
    private Object convertToString(Object value, Class<?> requiredType) {
        if (requiredType != String.class) {
            throw new TypeMismatchException("Unsupported type conversion: " + value.getClass().getName() + 
                    " -> " + requiredType.getName());
        }
        return value.toString();
    }
    
    private static Integer convertToInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Integer");
    }
    
    private static Long convertToLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Long");
    }
    
    private static Double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Double");
    }
    
    private static Float convertToFloat(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            return Float.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Float");
    }
    
    private static Boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = ((String) value).toLowerCase().trim();
            if ("true".equals(stringValue) || 
                "yes".equals(stringValue) || 
                "1".equals(stringValue) || 
                "on".equals(stringValue) ||
                "y".equals(stringValue)) {
                return true;
            } else if ("false".equals(stringValue) || 
                      "no".equals(stringValue) || 
                      "0".equals(stringValue) || 
                      "off".equals(stringValue) ||
                      "n".equals(stringValue)) {
                return false;
            }
            throw new IllegalArgumentException("Cannot convert string [" + value + "] to Boolean");
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Boolean");
    }
    
    private static Character convertToCharacter(Object value) {
        if (value instanceof Character) {
            return (Character) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.length() == 1) {
                return stringValue.charAt(0);
            }
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Character");
    }
    
    private static Byte convertToByte(Object value) {
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        } else if (value instanceof String) {
            return Byte.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Byte");
    }
    
    private static Short convertToShort(Object value) {
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        } else if (value instanceof String) {
            return Short.valueOf((String) value);
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to Short");
    }
    
    // Date-time type conversion methods
    
    private static LocalDate convertToLocalDate(Object value) {
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // Try parsing with ISO format
                return LocalDate.parse(stringValue);
            } catch (DateTimeParseException e) {
                // Try parsing with common formats
                try {
                    return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e1) {
                    try {
                        return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                    } catch (DateTimeParseException e2) {
                        throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalDate, supported formats: ISO, yyyy-MM-dd, yyyy/MM/dd", e2);
                    }
                }
            }
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalDate();
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalDate");
    }
    
    private static LocalTime convertToLocalTime(Object value) {
        if (value instanceof LocalTime) {
            return (LocalTime) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // Try parsing with ISO format
                return LocalTime.parse(stringValue);
            } catch (DateTimeParseException e) {
                // Try parsing with common formats
                try {
                    return LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm:ss"));
                } catch (DateTimeParseException e1) {
                    try {
                        return LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (DateTimeParseException e2) {
                        throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalTime, supported formats: ISO, HH:mm:ss, HH:mm", e2);
                    }
                }
            }
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalTime();
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalTime");
    }
    
    private static LocalDateTime convertToLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // Try parsing with ISO format
                return LocalDateTime.parse(stringValue);
            } catch (DateTimeParseException e) {
                // Try parsing with common formats
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(stringValue, formatter);
                    } catch (DateTimeParseException ignored) {
                        // Continue trying next format
                    }
                }
                
                // If pure date format, try converting to start of day
                try {
                    return LocalDate.parse(stringValue).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // Continue trying other formats
                }
                
                throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalDateTime, supported formats: " +
                    "ISO, yyyy-MM-dd HH:mm:ss, yyyy/MM/dd HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss, " +
                    "yyyy.MM.dd HH:mm:ss, yyyy-MM-dd HH:mm, yyyy/MM/dd HH:mm, yyyy-MM-dd");
            }
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay();
        }
        throw new IllegalArgumentException("Cannot convert [" + value + "] to LocalDateTime");
    }
} 