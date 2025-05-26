package com.example.coupon_backend.global.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

@Component
public class MessageHelper {

    private final MessageSource messageSource;

    private MessageHelper(MessageSource source) {
        messageSource = source;
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    public String getBindingErrorMessage(ObjectError error) {
        return getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage());
    }
}
