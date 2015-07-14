package go.euro.service;

import go.euro.utils.HttpCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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

    public void getRequest(Map<String,Object> headers, List<String> pathParams) {
        MultivaluedMap<String,Object> headersMap = HttpCommunication.createHeaders(headers);
        httpHelper.doGet(uri,headersMap, MediaType.APPLICATION_JSON_TYPE,null,pathParams);
    }

    public void postRequest(Object entity,Map<String,Object> headers, List<String> pathParams) {
        MultivaluedMap<String,Object> headersMap = HttpCommunication.createHeaders(headers);
        httpHelper.doPost(uri,entity,headersMap, MediaType.APPLICATION_JSON_TYPE,null,pathParams);
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
