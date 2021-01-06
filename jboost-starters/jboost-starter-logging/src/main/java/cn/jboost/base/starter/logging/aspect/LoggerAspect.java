package cn.jboost.base.starter.logging.aspect;

import cn.jboost.base.starter.logging.util.LoggerConstants;
import cn.jboost.base.starter.logging.annotation.Log;
import cn.jboost.base.starter.logging.annotation.LogPoint;
import cn.jboost.base.starter.logging.provider.ILogProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * 切面日志
 *
 * @Author ronwxy
 * @Date 2020/5/28 18:35
 * @Version 1.0
 */
@Aspect
public class LoggerAspect {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private ILogProvider logService;
    private int depthThreshold;

    public LoggerAspect(ILogProvider logService, int depthThreshold) {
        this.logService = logService;
        this.depthThreshold = depthThreshold;
    }

    /**
     * 对调用方法进行切面日志记录
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "@within(cn.jboost.base.starter.logging.annotation.Log)"     // per class
            + " || @annotation(cn.jboost.base.starter.logging.annotation.Log)")  // per method
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = extractMethod(joinPoint);

        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        //将当前类名方法名存入MDC
        MDC.put(LoggerConstants.CLASS_NAME, className);
        MDC.put(LoggerConstants.METHOD_NAME, methodName);

        Log logAnnotation = getLogAnnotation(method);
        //参数名称
        String[] argNames = parameterNameDiscoverer.getParameterNames(method);
        //参数值
        Object[] argValues = joinPoint.getArgs();

        if (LogPoint.IN.equals(logAnnotation.logPoint()) || LogPoint.BOTH.equals(logAnnotation.logPoint())) {
            logService.logCall(method, argNames, argValues, depthThreshold);
        }

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            if (logAnnotation.logException()) {
                logService.logThrow(method, argValues.length, e, true);
            }
            throw e;
        }
        //执行时间存入MDC
        MDC.put(LoggerConstants.ELAPSED_TIME, Long.toString(System.currentTimeMillis() - startTime));

        if (LogPoint.OUT.equals(logAnnotation.logPoint()) || LogPoint.BOTH.equals(logAnnotation.logPoint())) {
            logService.logReturn(method, argValues.length, result, depthThreshold);
        }

        MDC.remove(LoggerConstants.CLASS_NAME);
        MDC.remove(LoggerConstants.METHOD_NAME);
        MDC.remove(LoggerConstants.ELAPSED_TIME);

        return result;
    }


    private Log getLogAnnotation(Method method) {
        Log logAnnotation = method.getAnnotation(Log.class);
        //方法上没有，则获取类上注解
        if (logAnnotation == null) {
            logAnnotation = method.getDeclaringClass().getAnnotation(Log.class);
        }
        return logAnnotation;
    }

    private Method extractMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // signature.getMethod() points to method declared in interface. it is not suit to discover arg names and arg
        // annotations
        // see AopProxyUtils: org.springframework.cache.interceptor.CacheAspectSupport#execute(CacheAspectSupport
        // .Invoker, Object, Method, Object[])
        Class<?> targetClass = joinPoint.getTarget().getClass();
        if (Modifier.isPublic(signature.getMethod().getModifiers())) {
            return targetClass.getMethod(signature.getName(), signature.getParameterTypes());
        } else {
            return ReflectionUtils.findMethod(targetClass, signature.getName(), signature.getParameterTypes());
        }
    }

}
