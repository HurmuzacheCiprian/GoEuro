package go.euro.main;

import go.euro.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by churmuzache on 7/14/15.
 */
public class GoEuroTest {
    /** logger **/
    private static final Logger logger = LoggerFactory.getLogger(GoEuroTest.class);

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        RequestService requestService = context.getBean("requestService",RequestService.class);


        System.out.println("Hello world!");
    }

}
