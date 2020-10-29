package com.bld;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动程序
 * 
 * @author bld
 */
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//public class NumberOneApplication {
//
//    public static void main(String[] args) {
//        // System.setProperty("spring.devtools.restart.enabled", "false");
//        SpringApplication.run(NumberOneApplication.class, args);
//        System.out.println("\n" +
//                "                        __________ .____     ________   \n" +
//                "                        \\______   \\|    |    \\______ \\  \n" +
//                "                         |    |  _/|    |     |    |  \\ \n" +
//                "                         |    |   \\|    |___  |    `   \\\n" +
//                "                         |______  /|_______ \\/_______  /\n" +
//                "                                \\/         \\/        \\/ \n" +
//                "                ____________________ _____  __________ ___________\n" +
//                "               /   _____/\\__    ___//  _  \\ \\______   \\\\__    ___/\n" +
//                "               \\_____  \\   |    |  /  /_\\  \\ |       _/  |    |   \n" +
//                "               /        \\  |    | /    |    \\|    |   \\  |    |   \n" +
//                "              /_______  /  |____| \\____|__  /|____|_  /  |____|   \n" +
//                "                      \\/                  \\/        \\/            \n" +
//                "    _________ ____ ___ _________  _________  ___________  _________  _________\n" +
//                "   /   _____/|    |   \\\\_   ___ \\ \\_   ___ \\ \\_   _____/ /   _____/ /   _____/\n" +
//                "   \\_____  \\ |    |   //    \\  \\/ /    \\  \\/  |    __)_  \\_____  \\  \\_____  \\ \n" +
//                "   /        \\|    |  / \\     \\____\\     \\____ |        \\ /        \\ /        \\\n" +
//                "  /_______  /|______/   \\______  / \\______  //_______  //_______  //_______  /\n" +
//                "          \\/                   \\/         \\/         \\/         \\/         \\/ \n" +
//                "http://localhost:8010/"
//            );
//    }
//}
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ServletComponentScan

//@ComponentScan("com.bld.project.newlyadded")
public class BldApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder){
        return springApplicationBuilder.sources(BldApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(BldApplication.class, args);
        System.out.println("\n" +
                "                        __________ .____     ________   \n" +
                "                        \\______   \\|    |    \\______ \\  \n" +
                "                         |    |  _/|    |     |    |  \\ \n" +
                "                         |    |   \\|    |___  |    `   \\\n" +
                "                         |______  /|_______ \\/_______  /\n" +
                "                                \\/         \\/        \\/ \n" +
                "                ____________________ _____  __________ ___________\n" +
                "               /   _____/\\__    ___//  _  \\ \\______   \\\\__    ___/\n" +
                "               \\_____  \\   |    |  /  /_\\  \\ |       _/  |    |   \n" +
                "               /        \\  |    | /    |    \\|    |   \\  |    |   \n" +
                "              /_______  /  |____| \\____|__  /|____|_  /  |____|   \n" +
                "                      \\/                  \\/        \\/            \n" +
                "    _________ ____ ___ _________  _________  ___________  _________  _________\n" +
                "   /   _____/|    |   \\\\_   ___ \\ \\_   ___ \\ \\_   _____/ /   _____/ /   _____/\n" +
                "   \\_____  \\ |    |   //    \\  \\/ /    \\  \\/  |    __)_  \\_____  \\  \\_____  \\ \n" +
                "   /        \\|    |  / \\     \\____\\     \\____ |        \\ /        \\ /        \\\n" +
                "  /_______  /|______/   \\______  / \\______  //_______  //_______  //_______  /\n" +
                "          \\/                   \\/         \\/         \\/         \\/         \\/ \n" +
                "http://localhost:8010/"
        );
    }
}