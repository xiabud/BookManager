package com.xiecode.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;


@ComponentScans({
        @ComponentScan("com.xiecode.service"),
        @ComponentScan("com.xiecode.service")
})
@Configuration
public class RootConfiguration {
}
