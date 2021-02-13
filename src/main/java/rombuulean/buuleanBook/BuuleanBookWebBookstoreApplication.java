package rombuulean.buuleanBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BuuleanBookWebBookstoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BuuleanBookWebBookstoreApplication.class, args);
        System.out.println(context);
    }

    /*
    Przyklad tworzenie Beana z logika w srodku, za pomoca @Bean
    @Bean
    CatalogRepository catalogRepository(){
    Random random = new Random();
    if(random){
        return new SchoolCatalogRepositoryImpl();
    }else{
        return new BestsellerCatalogRepositoryImpl();
    }
        return new SchoolCatalogRepositoryImpl();
    }
    */

//    @Bean
//    String query(){
//        return "Via";
//    }

}
