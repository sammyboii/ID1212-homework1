package shared;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType type;
    private final Object data;
    private final String message;

    public Response(Object data, ResponseType type, String message) {
        this.type = type;
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
