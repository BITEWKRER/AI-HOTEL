package AI.hotel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling //定时任务
@EnableAsync    //异步任务
@EnableCaching  //缓存
@MapperScan(value="AI.hotel.mapper")   //mapper 包扫描
@EnableSwagger2 // api快速生成
@EnableRabbit
public class HotelApplication {

    static {
        System.setProperty("mail.mime.splitlongparameters", "false");
    }

    public static void main(String[] args) {

        SpringApplication.run(HotelApplication.class, args);
    }

}
