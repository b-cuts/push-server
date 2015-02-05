package com.cosmicpush.app.jaxrs.security;

import java.lang.annotation.*;
import javax.ws.rs.NameBinding;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface MngtAuthentication {
}
