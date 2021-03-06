package go.euro.main;

import go.euro.enums.FileFormat;
import go.euro.model.ErrorResponse;
import go.euro.model.GoEuroModel;
import go.euro.model.GoEuroResponseModel;
import go.euro.service.RequestService;
import go.euro.utils.GoEuroFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by churmuzache on 7/14/15.
 */
public class GoEuroTest {
    /**
     * logger *
     */
    private static final Logger logger = LoggerFactory.getLogger(GoEuroTest.class);

    private static final Map<String, String> headers = new HashMap<>();
    private static final List<String> pathParams = new ArrayList<>(1); //only one path param, but could be used for many more.

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        RequestService requestService = context.getBean("requestService", RequestService.class);

        if (checkArgs(args)) {
            pathParams.add(args[0]);
            GoEuroModel[] response = makeRequest(requestService);
            GoEuroFileWriter.writeToFile("go_euro", FileFormat.CSV,response);
        }
    }

    /**
     * more additional checks could be made
     *
     * @param args
     * @return
     */
    private static boolean checkArgs(String[] args) {
        if (args.length <= 0) {
            logger.info("No param or more than one provided, please enter only one path param in order to make the request");
            return false;
        } else {
            return true;
        }
    }

    private static <K, V> Map<K, V> createHeaders(Map<K, V> headers, K key, V value) {
        if (!headers.containsKey(key)) {
            headers.put(key, value);
        }
        return headers;
    }

    private static <T> T readResponse(Response response, Class<T> clazz)
    {
        if (Response.Status.Family.familyOf(response.getStatus()) != Response.Status.Family.SUCCESSFUL) {
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            logger.info("Response came with error {}", response);
            return null;
        } else {
            T res = response.readEntity(clazz);
            headers.clear();    //clear the headers for a new response
            pathParams.clear();
            return res;
        }
    }

    private static GoEuroModel[] makeRequest(RequestService requestService) {
        createHeaders(headers, "Content-Type", "application/json");
        createHeaders(headers, "Accept", "application/json");

        Response response = requestService.getRequest(headers, pathParams);

        GoEuroModel[] responseModel = readResponse(response, GoEuroModel[].class);

        if (responseModel == null) {
            logger.info("Error response ");
            return null;
        }

       return responseModel;
    }
}
