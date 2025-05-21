package com.minispring.aop.aspectj;

import com.minispring.aop.ClassFilter;
import com.minispring.aop.MethodMatcher;
import com.minispring.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJ Expression Pointcut
 * Define pointcuts using AspectJ expression language
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
    
    // Pointcut primitives supported by AspectJ
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();
    
    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }
    
    // Pointcut expression
    private String expression;
    
    // AspectJ pointcut expression object
    private PointcutExpression pointcutExpression;
    
    // ClassLoader for expression parsing
    private final ClassLoader pointcutClassLoader;
    
    /**
     * Create a new AspectJExpressionPointcut
     */
    public AspectJExpressionPointcut() {
        this(null, null);
    }
    
    /**
     * Create a new AspectJExpressionPointcut
     * @param expression pointcut expression
     */
    public AspectJExpressionPointcut(String expression) {
        this(expression, null);
    }
    
    /**
     * Create a new AspectJExpressionPointcut
     * @param expression pointcut expression
     * @param pointcutClassLoader class loader
     */
    public AspectJExpressionPointcut(String expression, ClassLoader pointcutClassLoader) {
        this.expression = expression;
        this.pointcutClassLoader = (pointcutClassLoader != null ? pointcutClassLoader :
                AspectJExpressionPointcut.class.getClassLoader());
        if (expression != null) {
            buildPointcutExpression();
        }
    }
    
    /**
     * Set pointcut expression
     * @param expression pointcut expression
     */
    public void setExpression(String expression) {
        this.expression = expression;
        buildPointcutExpression();
    }
    
    /**
     * Get pointcut expression
     * @return pointcut expression
     */
    public String getExpression() {
        return this.expression;
    }
    
    /**
     * Build AspectJ pointcut expression
     */
    private void buildPointcutExpression() {
        if (this.expression == null) {
            throw new IllegalStateException("Expression must not be null");
        }
        
        PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                SUPPORTED_PRIMITIVES, this.pointcutClassLoader);
        this.pointcutExpression = parser.parsePointcutExpression(this.expression);
    }
    
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }
    
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
    
    @Override
    public boolean matches(Class<?> clazz) {
        checkReadyToMatch();
        return this.pointcutExpression.couldMatchJoinPointsInType(clazz);
    }
    
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        return shadowMatch.alwaysMatches();
    }
    
    @Override
    public boolean isRuntime() {
        checkReadyToMatch();
        return this.pointcutExpression.mayNeedDynamicTest();
    }
    
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        return shadowMatch.alwaysMatches();
    }
    
    /**
     * Check if ready to perform matching
     */
    private void checkReadyToMatch() {
        if (this.pointcutExpression == null) {
            throw new IllegalStateException("Must set expression before attempting to match");
        }
    }
} 