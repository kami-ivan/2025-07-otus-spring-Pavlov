package ru.otus.hw.security;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class RequestBuilderUtils {

    public static MockHttpServletRequestBuilder method2RequestBuilder(String method, String url) {
        return switch (method.toLowerCase()) {
            case "get" -> MockMvcRequestBuilders.get(url);
            case "post" -> MockMvcRequestBuilders.post(url);
            case "delete" -> MockMvcRequestBuilders.delete(url);
            case "patch" -> MockMvcRequestBuilders.patch(url);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }
}