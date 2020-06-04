package kr.co.korbit.gia.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(method = { RequestMethod.POST , RequestMethod.GET})
public @interface GetPostMapping {
	@AliasFor(annotation = RequestMapping.class)
	public abstract String[] value();
}
