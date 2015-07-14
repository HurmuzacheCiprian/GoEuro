package go.euro.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import go.euro.utils.HttpValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that offers utility request methods for all 3 HTTP verbs GET,POST,DELETE
 * <p/>
 * Created by churmuzache on 7/14/15.
 */
public abstract class HttpCommunication {

    private Client clientRequest;

    private static final Logger logger = LoggerFactory.getLogger(HttpCommunication.class);

    public HttpCommunication(Client clientRequest) {
        this.clientRequest = clientRequest;
    }

    /**
     * Class for defining the query params*
     */
    public static class QueryParams {
        private String name;
        private Object[] params;

        public QueryParams(String name, List<Object> params) {
            this.name = name;
            this.params = params.toArray();
        }

        public QueryParams(String name, Object[] params) {
            this.name = name;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }
    }

    /**
     * Http verbs *
     */
    public static enum HttpVerbEnum {
        GET, POST, DELETE,PUT;

        public static class HttpVerbException extends RuntimeException {
            public HttpVerbException(String message) {
                super(message);
            }
        }
    }

    public <H extends MultivaluedMap> Response doGet(String uri, H headers, MediaType mediaType, List<QueryParams> queryParams, List<String> pathParams) {
        return requestMethod(uri, HttpVerbEnum.GET, mediaType, null, queryParams, pathParams,headers);
    }

    public <T, H extends MultivaluedMap> Response doPost(String uri, T body, H headers, MediaType mediaType, List<QueryParams> queryParams, List<String> pathParams) {
        return requestMethod(uri, HttpVerbEnum.POST, mediaType, body, queryParams, pathParams,headers);
    }

    public void doPut(String uri) {
        //To be implemented if needed
    }

    public void doDelete(String uri) {
        //To be implemented if needed
    }

    /**
     * @param uri
     * @param verb
     * @param mediaType
     * @param entity      - the request body
     * @param queryParams
     * @param pathParams
     * @return
     */
    private Response requestMethod(String uri, HttpVerbEnum verb, MediaType mediaType, Object entity, List<QueryParams> queryParams, List<String> pathParams,MultivaluedMap<String, Object> headers) {
        Response response = null;
        try {

            MediaType defaultMediaType =
                    mediaType == null ? MediaType.APPLICATION_JSON_TYPE : mediaType;
            String mediaTypeAsString = defaultMediaType.toString();
            Entity emptyEntity = Entity.entity("", mediaTypeAsString);
            Entity requestEntity = Entity.entity(entity, mediaTypeAsString);

            Entity callEntity = (entity == null ?
                    emptyEntity :
                    requestEntity);
            if (pathParams != null) {
                if (uri.endsWith("/")) {
                    uri = uri.substring(0, uri.lastIndexOf('/'));
                }
                //Do a concat because this does not affects the performance for the current test.
                //However if there are multiple path params it will be better to use a non thread safe StringBuilder to avoid concat
                for (String pathParam : pathParams) {
                    uri += "/" + pathParam;
                }
            }
            logger.info("Making request to ",uri);

            WebTarget webTarget = clientRequest.target(uri);
            if (queryParams != null) {
                for (QueryParams queryParam : queryParams) {
                    webTarget = webTarget
                            .queryParam(queryParam.getName(), queryParam.getParams());
                }
            }
            Invocation.Builder builder = webTarget.request(defaultMediaType);
            if (headers != null) {
                builder = builder.headers(headers);
            }

            switch(verb) {
                case GET:
                    response = builder.get();
                    break;
                case POST:
                    response = builder.post(callEntity);
                    break;
                case PUT:
                    response = builder.put(callEntity);
                    break;
                case DELETE:
                    response = builder.delete();
                    break;
                default:
                    throw new HttpVerbEnum.HttpVerbException("Unknown http request method invoked");
            }
            HttpValidator.validateResponse(response,String.class);
        } catch( HttpVerbEnum.HttpVerbException verbException) {
            logger.error(verbException.getMessage());
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        } catch(HttpValidator.HttpFamilyResponseException responseException) {
            logger.error("Error while validation the response");
            return Response.status(responseException.getResponseStatus()).entity(responseException.getErrorEntity()).build();
        } catch (Exception exception) {
            logger.error("Error while making request. Exception message ",exception.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return response;
    }

    public static MultivaluedMap<String, Object> createHeaders(Map<String,Object> headers) {
        MultivaluedMap<String, Object> headerAuth = new MultivaluedHashMap<String, Object>();

        for(String k : headers.keySet()) {
            headerAuth.putSingle(k,headers.get(k));
        }

        return headerAuth;
    }

}
