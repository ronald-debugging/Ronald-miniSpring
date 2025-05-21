package com.minispring.core.convert.converter;

import java.util.Set;

/**
 * Generic Converter Interface
 * Supports more complex type conversion scenarios
 */
public interface GenericConverter {
    
    /**
     * Return the convertible type pairs supported by this converter
     * @return set of supported convertible type pairs, returns null if converter doesn't support any types
     */
    Set<ConvertiblePair> getConvertibleTypes();
    
    /**
     * Convert given source object to target type
     * @param source source object
     * @param sourceType source type
     * @param targetType target type
     * @return converted object
     */
    Object convert(Object source, Class<?> sourceType, Class<?> targetType);
    
    /**
     * Describes a source type to target type conversion pair
     */
    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;
        
        /**
         * Create a new conversion pair
         * @param sourceType source type
         * @param targetType target type
         */
        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }
        
        /**
         * Get source type
         * @return source type
         */
        public Class<?> getSourceType() {
            return this.sourceType;
        }
        
        /**
         * Get target type
         * @return target type
         */
        public Class<?> getTargetType() {
            return this.targetType;
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            ConvertiblePair otherPair = (ConvertiblePair) other;
            return this.sourceType.equals(otherPair.sourceType) && this.targetType.equals(otherPair.targetType);
        }
        
        @Override
        public int hashCode() {
            return this.sourceType.hashCode() * 31 + this.targetType.hashCode();
        }
    }
} 