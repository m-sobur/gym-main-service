package com.example.springcore.handler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        RestController classAnnotation = methodParameter.getContainingClass().getAnnotation(RestController.class);
        if (classAnnotation != null) {
            String packageName = methodParameter.getContainingClass().getPackage().getName();
            return packageName.contains(".controller.");
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (serverHttpRequest instanceof ServletServerHttpRequest &&
                serverHttpResponse instanceof ServletServerHttpResponse) {
            log.info(
                    "ServletServerHttpRequest: {}, ServletServerHttpResponse: {}, Object: {}",
                    ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getMethod(),
                    ((ServletServerHttpResponse) serverHttpResponse).getServletResponse().getStatus(),
                    o
            );
        }

        return o;
    }
}
