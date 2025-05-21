package com.minispring.test;

import com.minispring.beans.BeanWrapper;
import com.minispring.beans.SimpleTypeConverter;
import com.minispring.beans.TypeConverter;
import com.minispring.beans.TypeMismatchException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Type converter test class
 */
public class TypeConverterTest {

    /**
     * Test basic type conversion
     */
    @Test
    public void testBasicTypeConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // Test string to integer conversion
        assertEquals(42, converter.convert("42", Integer.class));
        
        // Test string to long conversion
        assertEquals(123L, converter.convert("123", Long.class));
        
        // Test string to double conversion
        assertEquals(3.14, converter.convert("3.14", Double.class));
        
        // Test string to float conversion
        assertEquals(3.14f, converter.convert("3.14", Float.class));
        
        // Test string to boolean conversion
        assertTrue(converter.convert("true", Boolean.class));
        assertTrue(converter.convert("yes", Boolean.class));
        assertTrue(converter.convert("1", Boolean.class));
        assertFalse(converter.convert("false", Boolean.class));
        
        // Test string to character conversion
        assertEquals('A', converter.convert("A", Character.class));
        
        // Test number to string conversion
        assertEquals("42", converter.convert(42, String.class));
    }

    /**
     * Test date and time type conversion
     */
    @Test
    public void testDateTimeConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // Test string to LocalDate conversion
        assertEquals(LocalDate.parse("2023-12-25"), 
                converter.convert("2023-12-25", LocalDate.class));
        
        // Test string to LocalTime conversion
        assertEquals(LocalTime.parse("12:34:56"), 
                converter.convert("12:34:56", LocalTime.class));
        
        // Test string to LocalDateTime conversion (ISO format)
        assertEquals(LocalDateTime.parse("2023-12-25T12:34:56"), 
                converter.convert("2023-12-25T12:34:56", LocalDateTime.class));
        
        // Test string to LocalDateTime conversion (custom format)
        assertEquals(LocalDateTime.parse("2023-12-25T12:34:56"), 
                converter.convert("2023-12-25 12:34:56", LocalDateTime.class));
    }

    /**
     * Test invalid conversion
     */
    @Test
    public void testInvalidConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // Test invalid number format
        assertThrows(TypeMismatchException.class, () -> {
            converter.convert("not a number", Integer.class);
        });
        
        // Test invalid date format
        assertThrows(TypeMismatchException.class, () -> {
            converter.convert("not a date", LocalDate.class);
        });
    }

    /**
     * Test concurrent conversion
     */
    @Test
    public void testConcurrentConversion() throws InterruptedException {
        int threadCount = 10;
        int iterationsPerThread = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    TypeConverter localConverter = new SimpleTypeConverter();
                    for (int j = 0; j < iterationsPerThread; j++) {
                        assertEquals(j, localConverter.convert(String.valueOf(j), Integer.class));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executorService.shutdown();
    }

    /**
     * Test nested property
     */
    @Test
    public void testNestedProperty() {
        ParentBean parent = new ParentBean();
        BeanWrapper wrapper = new BeanWrapper(parent);
        
        // Use BeanWrapper to set nested property, child object will be created automatically
        wrapper.setPropertyValue("child.name", "Alice");
        
        assertNotNull(parent.getChild());
        assertEquals("Alice", parent.getChild().getName());
        
        // Test getting nested property
        Object value = wrapper.getPropertyValue("child.name");
        assertEquals("Alice", value);
    }
    
    /**
     * Test BeanWrapper with type conversion
     */
    @Test
    public void testBeanWrapperWithTypeConversion() {
        TestBean testBean = new TestBean();
        BeanWrapper wrapper = new BeanWrapper(testBean);
        
        // Set properties of various types, type conversion is needed
        wrapper.setPropertyValue("intValue", "123");
        wrapper.setPropertyValue("longValue", "123456789");
        wrapper.setPropertyValue("doubleValue", "123.456");
        wrapper.setPropertyValue("booleanValue", "true");
        wrapper.setPropertyValue("stringValue", 123);
        wrapper.setPropertyValue("dateValue", "2023-01-01");
        
        // Verify conversion results
        assertEquals(123, testBean.getIntValue());
        assertEquals(123456789L, testBean.getLongValue());
        assertEquals(123.456, testBean.getDoubleValue(), 0.0001);
        assertTrue(testBean.isBooleanValue());
        assertEquals("123", testBean.getStringValue());
        assertEquals(LocalDate.of(2023, 1, 1), testBean.getDateValue());
    }

    /**
     * Parent Bean class for testing nested properties
     */
    public static class ParentBean {
        private ChildBean child;

        public ChildBean getChild() {
            return child;
        }

        public void setChild(ChildBean child) {
            this.child = child;
        }
    }

    /**
     * Child Bean class for testing nested properties
     */
    public static class ChildBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    /**
     * Test Bean class for testing type conversion
     */
    public static class TestBean {
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private String stringValue;
        private LocalDate dateValue;
        
        public int getIntValue() {
            return intValue;
        }
        
        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
        
        public long getLongValue() {
            return longValue;
        }
        
        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }
        
        public double getDoubleValue() {
            return doubleValue;
        }
        
        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }
        
        public boolean isBooleanValue() {
            return booleanValue;
        }
        
        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }
        
        public String getStringValue() {
            return stringValue;
        }
        
        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
        
        public LocalDate getDateValue() {
            return dateValue;
        }
        
        public void setDateValue(LocalDate dateValue) {
            this.dateValue = dateValue;
        }
    }
} 