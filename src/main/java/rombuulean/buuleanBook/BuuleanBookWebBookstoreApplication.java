package rombuulean.buuleanBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class BuuleanBookWebBookstoreApplication{

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BuuleanBookWebBookstoreApplication.class, args);
        System.out.println(context);
    }

//    @Bean
//    CatalogRepository catalogRepository(){
//    Random random = new Random();
//    if(random){
//        return new SchoolCatalogRepositoryImpl();
//    }else{
//        return new BestsellerCatalogRepositoryImpl();
//    }
//        return new SchoolCatalogRepositoryImpl();
//    }

}
