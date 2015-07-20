package go.euro.utils;

import go.euro.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

/**
 * Created by churmuzache on 7/14/15.
 */
public abstract class HttpValidator {

    private static final Logger logger = LoggerFactory.getLogger(HttpValidator.class);

    public static class HttpValidationException extends RuntimeException {
        public HttpValidationException(String message) {
            super(message);
        }
    }

    public static class HttpFamilyResponseException extends RuntimeException {

        private int responseStatus;
        private ErrorResponse errorEntity;

        public ErrorResponse getErrorEntity() {
            return errorEntity;
        }

        public void setErrorEntity(ErrorResponse errorEntity) {
            this.errorEntity = errorEntity;
        }

        public HttpFamilyResponseException(String message, int responseStatus) {
            super(message);
            this.responseStatus = responseStatus;
        }

        public int getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(int responseStatus) {
            this.responseStatus = responseStatus;
        }


        public HttpFamilyResponseException withEntity(ErrorResponse errorEntity) {
            this.setErrorEntity(errorEntity);
            return this;
        }

    }

    public static <T extends ErrorResponse> void validateResponse(Response response, Class<T> errorClazz) {
        logger.info("Validating the response that came...");
        int responseStatus=0;
        T responseEntity = null;
        try {
            responseStatus = response.getStatus();
            if (Family.familyOf(responseStatus) != Family.SUCCESSFUL) {
                logger.info("Response came with error code...");
                if (response.hasEntity()) {
                    responseEntity = response.readEntity(errorClazz);
                    throw new HttpFamilyResponseException("Response came with status " + responseStatus, responseStatus).withEntity(responseEntity);
                }

            }
        }catch (Exception exception) {
            throw new HttpFamilyResponseException("Response came with status " + responseStatus, responseStatus);
        }
    }

}
