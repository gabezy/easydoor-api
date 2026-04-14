package br.com.gabezy.easydoorapi.domain.annotations;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public @interface DateTimeFormat {
}
