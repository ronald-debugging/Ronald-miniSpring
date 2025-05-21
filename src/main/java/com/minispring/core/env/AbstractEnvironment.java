package com.minispring.core.env;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract base class for Environment
 * Provides basic implementation of Environment
 */
public abstract class AbstractEnvironment implements ConfigurableEnvironment {
    
    /**
     * Active profiles property name
     */
    public static final String ACTIVE_PROFILES_PROPERTY_NAME = "spring.profiles.active";
    
    /**
     * Default profiles property name
     */
    public static final String DEFAULT_PROFILES_PROPERTY_NAME = "spring.profiles.default";
    
    /**
     * Default profile name
     */
    protected static final String DEFAULT_PROFILE_NAME = "default";
    
    /**
     * Property sources collection
     */
    private final MutablePropertySources propertySources = new MutablePropertySources();
    
    /**
     * Active profiles
     */
    private final Set<String> activeProfiles = new LinkedHashSet<>();
    
    /**
     * Default profiles
     */
    private final Set<String> defaultProfiles = new LinkedHashSet<>(Collections.singleton(DEFAULT_PROFILE_NAME));
    
    /**
     * Constructor
     */
    public AbstractEnvironment() {
        customizePropertySources(this.propertySources);
        
        // Load profiles from system properties and environment variables
        String[] activeProfiles = getProfilesFromProperty(ACTIVE_PROFILES_PROPERTY_NAME);
        if (activeProfiles != null) {
            setActiveProfiles(activeProfiles);
        }
        String[] defaultProfiles = getProfilesFromProperty(DEFAULT_PROFILES_PROPERTY_NAME);
        if (defaultProfiles != null) {
            setDefaultProfiles(defaultProfiles);
        }
    }
    
    /**
     * Customize property sources
     * Subclasses can override this method to add custom property sources
     * 
     * @param propertySources property sources collection
     */
    protected void customizePropertySources(MutablePropertySources propertySources) {
        // Default empty, to be implemented by subclasses
    }
    
    /**
     * Get profiles from property
     * 
     * @param propertyName property name
     * @return profiles array, returns null if not exists
     */
    private String[] getProfilesFromProperty(String propertyName) {
        String profilesStr = getProperty(propertyName);
        if (profilesStr != null && !profilesStr.isEmpty()) {
            return profilesStr.split(",");
        }
        return null;
    }
    
    /**
     * Set active profiles
     * 
     * @param profiles active profiles array
     */
    @Override
    public void setActiveProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            this.activeProfiles.clear();
            return;
        }
        
        for (String profile : profiles) {
            validateProfile(profile);
        }
        
        this.activeProfiles.clear();
        Collections.addAll(this.activeProfiles, profiles);
    }
    
    /**
     * Add active profile
     * 
     * @param profile active profile
     */
    @Override
    public void addActiveProfile(String profile) {
        validateProfile(profile);
        this.activeProfiles.add(profile);
    }
    
    /**
     * Set default profiles
     * 
     * @param profiles default profiles array
     */
    @Override
    public void setDefaultProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            this.defaultProfiles.clear();
            return;
        }
        
        for (String profile : profiles) {
            validateProfile(profile);
        }
        
        this.defaultProfiles.clear();
        Collections.addAll(this.defaultProfiles, profiles);
    }
    
    /**
     * Get active profiles
     * 
     * @return active profiles array
     */
    @Override
    public String[] getActiveProfiles() {
        if (this.activeProfiles.isEmpty()) {
            return new String[0];
        }
        return this.activeProfiles.toArray(new String[0]);
    }
    
    /**
     * Get default profiles
     * 
     * @return default profiles array
     */
    @Override
    public String[] getDefaultProfiles() {
        if (this.defaultProfiles.isEmpty()) {
            return new String[0];
        }
        return this.defaultProfiles.toArray(new String[0]);
    }
    
    /**
     * Check if specified profile is active
     * 
     * @param profile profile name
     * @return returns true if active, false otherwise
     */
    @Override
    public boolean acceptsProfiles(String profile) {
        validateProfile(profile);
        
        // If active profiles is empty, use default profiles
        if (this.activeProfiles.isEmpty()) {
            return this.defaultProfiles.contains(profile);
        }
        
        return this.activeProfiles.contains(profile);
    }
    
    /**
     * Check if any of specified profiles is active
     * 
     * @param profiles profile names array
     * @return returns true if any is active, false otherwise
     */
    @Override
    public boolean acceptsProfiles(String... profiles) {
        if (profiles == null || profiles.length == 0) {
            return false;
        }
        
        for (String profile : profiles) {
            if (acceptsProfiles(profile)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate profile name
     * 
     * @param profile profile name
     * @throws IllegalArgumentException if profile name is invalid
     */
    private void validateProfile(String profile) {
        if (profile == null || profile.isEmpty()) {
            throw new IllegalArgumentException("Profile cannot be empty");
        }
    }
    
    /**
     * Get system properties
     * 
     * @return system properties
     */
    @Override
    public Map<String, Object> getSystemProperties() {
        // Convert Properties to Map
        Map<String, Object> map = Collections.emptyMap();
        try {
            map = (Map) System.getProperties();
        } catch (Exception ex) {
            // Ignore security exception
        }
        return map;
    }
    
    /**
     * Get system environment variables
     * 
     * @return system environment variables
     */
    @Override
    public Map<String, Object> getSystemEnvironment() {
        Map<String, Object> map = Collections.emptyMap();
        try {
            map = (Map) System.getenv();
        } catch (Exception ex) {
            // Ignore security exception
        }
        return map;
    }
    
    /**
     * Get property sources collection
     * 
     * @return property sources collection
     */
    @Override
    public MutablePropertySources getPropertySources() {
        return this.propertySources;
    }
    
    /**
     * Merge configuration from another Environment
     * 
     * @param parent parent Environment
     */
    @Override
    public void merge(ConfigurableEnvironment parent) {
        if (parent == null) {
            return;
        }
        
        // Merge property sources
        for (PropertySource<?> propertySource : parent.getPropertySources()) {
            if (!this.propertySources.contains(propertySource.getName())) {
                this.propertySources.addLast(propertySource);
            }
        }
        
        // Merge active profiles
        Set<String> parentActiveProfiles = new LinkedHashSet<>(Arrays.asList(parent.getActiveProfiles()));
        if (!parentActiveProfiles.isEmpty()) {
            this.activeProfiles.addAll(parentActiveProfiles);
        }
        
        // Merge default profiles
        Set<String> parentDefaultProfiles = new LinkedHashSet<>(Arrays.asList(parent.getDefaultProfiles()));
        if (!parentDefaultProfiles.isEmpty() && !parentDefaultProfiles.contains(DEFAULT_PROFILE_NAME)) {
            this.defaultProfiles.addAll(parentDefaultProfiles);
        }
    }
    
    /**
     * Get property value by name
     * 
     * @param key property name
     * @return property value, returns null if not exists
     */
    @Override
    public String getProperty(String key) {
        for (PropertySource<?> propertySource : this.propertySources) {
            Object value = propertySource.getProperty(key);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    /**
     * Get property value by name, use default value if not exists
     * 
     * @param key property name
     * @param defaultValue default value
     * @return property value, returns default value if not exists
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get property value by name and convert to specified type
     * 
     * @param key property name
     * @param targetType target type
     * @param <T> target type generic
     * @return property value, returns null if not exists
     */
    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return convertValueToTargetType(value, targetType);
    }
    
    /**
     * Get property value by name and convert to specified type, use default value if not exists
     * 
     * @param key property name
     * @param targetType target type
     * @param defaultValue default value
     * @param <T> target type generic
     * @return property value, returns default value if not exists
     */
    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        T value = getProperty(key, targetType);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Check if property exists by name
     * 
     * @param key property name
     * @return returns true if exists, false otherwise
     */
    @Override
    public boolean containsProperty(String key) {
        for (PropertySource<?> propertySource : this.propertySources) {
            if (propertySource.containsProperty(key)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Convert string value to target type
     * 
     * @param value string value
     * @param targetType target type
     * @param <T> target type generic
     * @return converted value
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValueToTargetType(String value, Class<T> targetType) {
        if (targetType == String.class) {
            return (T) value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return (T) Integer.valueOf(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return (T) Long.valueOf(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return (T) Double.valueOf(value);
        } else if (targetType == Float.class || targetType == float.class) {
            return (T) Float.valueOf(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (targetType == Short.class || targetType == short.class) {
            return (T) Short.valueOf(value);
        } else if (targetType == Byte.class || targetType == byte.class) {
            return (T) Byte.valueOf(value);
        } else if (targetType == Character.class || targetType == char.class) {
            if (value.length() == 1) {
                return (T) Character.valueOf(value.charAt(0));
            }
            throw new IllegalArgumentException("Cannot convert String [" + value + "] to target type [" + targetType.getName() + "]");
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
        }
    }
    
    /**
     * Override toString method
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " {activeProfiles=" + this.activeProfiles +
                ", defaultProfiles=" + this.defaultProfiles +
                ", propertySources=" + this.propertySources + "}";
    }
} 