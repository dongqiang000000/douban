package fm.douban;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication(scanBasePackages = {"fm.douban.app","fm.douban.service","fm.douban.spider"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}


