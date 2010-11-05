package com.codeforces.graygoose.dao.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.nocturne.main.ApplicationContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvalidateCache {
    public static class Interceptor implements MethodInterceptor {
        public Cache getCache() {
            return Internal.cache;
        }

        private static class Internal {
            private static final Cache cache = ApplicationContext.getInstance().getInjector().getInstance(Cache.class);
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // Clear cache before method call.
            getCache().clear();

            try {
                return invocation.proceed();
            } finally {
                // Clear cache after method call.
                getCache().clear();
            }
        }
    }
}
