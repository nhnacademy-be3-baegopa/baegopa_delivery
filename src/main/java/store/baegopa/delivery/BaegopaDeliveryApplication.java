package store.baegopa.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "store.baegopa.delivery.config")
public class BaegopaDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaegopaDeliveryApplication.class, args);
    }

}
