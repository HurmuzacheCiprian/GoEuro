package go.euro.service;

import go.euro.utils.HttpCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by churmuzache on 7/14/15.
 */
public class RequestService {

    /** Request URI **/
    private String uri;

    /** HTTP helper**/
    private HttpCommunication httpHelper;

    /** **/
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    /**
     * General request method for multiple headers and multiple path params.
     * @param headers
     * @param pathParams
     * @return
     */
    public Response getRequest(Map<String,String> headers, List<String> pathParams) {
        logger.info("Making GET request to {}",uri);
        MultivaluedMap<String,Object> headersMap = HttpCommunication.createHeaders(headers);
        return httpHelper.doGet(uri,headersMap, MediaType.APPLICATION_JSON_TYPE,null,pathParams);
    }

    public Response postRequest(Object entity,Map<String,String> headers, List<String> pathParams) {
        logger.info("Making POST request to {}",uri);
        MultivaluedMap<String,Object> headersMap = HttpCommunication.createHeaders(headers);
        return httpHelper.doPost(uri,entity,headersMap, MediaType.APPLICATION_JSON_TYPE,null,pathParams);
    }

    private void checkResponse() {

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpCommunication getHttpHelper() {
        return httpHelper;
    }

    public void setHttpHelper(HttpCommunication httpHelper) {
        this.httpHelper = httpHelper;
    }

}
