package com.codeforces.graygoose.dao.cache;

import com.codeforces.graygoose.model.AbstractEntity;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.nocturne.main.ApplicationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    public static class Interceptor implements MethodInterceptor {
        private static final char SEPARATOR = 1;

        public Cache getCache() {
            return Internal.cache;
        }

        private static class Internal {
            private static Cache cache = ApplicationContext.getInstance().getInjector().getInstance(Cache.class);
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String hashKey = ensureArgumentsAreSupportedAndReturnHashKey(invocation);

            Object result = getCache().get(hashKey);
            if (result == null) {
                return invokeAndCache(invocation, hashKey);
            } else {
                return result;
            }
        }

        private Object invokeAndCache(MethodInvocation invocation, String hashKey) throws Throwable {
            Object result = invocation.proceed();
            getCache().put(hashKey, result);
            return result;
        }

        private String ensureArgumentsAreSupportedAndReturnHashKey(MethodInvocation invocation) {
            StringBuilder result = new StringBuilder();

            result.append(invocation.getMethod().getDeclaringClass().getName()).append(SEPARATOR);
            result.append(invocation.getMethod().getName()).append(SEPARATOR);

            for (Object o : invocation.getArguments()) {
                if (o == null) {
                    result.append("@null").append(SEPARATOR);
                } else {
                    if (!supportsArgumentClass(o.getClass())) {
                        throw new IllegalArgumentException("Type " + o.getClass() + " is not supported by cache.");
                    }

                    result.append(o).append(SEPARATOR);
                }
            }

            return result.append(SEPARATOR).toString();
        }

        private boolean supportsArgumentClass(Class<?> argumentClass) {
            return argumentClass.isPrimitive()
                    || argumentClass.equals(String.class)
                    || argumentClass.isEnum()
                    || Number.class.isAssignableFrom(argumentClass)
                    || AbstractEntity.class.isAssignableFrom(argumentClass);
        }
    }
}
