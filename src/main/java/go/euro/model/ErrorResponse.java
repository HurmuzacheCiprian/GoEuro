package go.euro.model;

import java.util.List;

/**
 * Created by churmuzache on 7/20/15.
 */
public class ErrorResponse {
    private String message;
    private String errorClass;
    private String description;
    private List items;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
