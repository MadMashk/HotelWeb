package spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan({ "hibernate", "model", "service","web","spring", "exeptions"})
@PropertySource("classpath:properties/data.properties")
public class SpringConfig {

}
