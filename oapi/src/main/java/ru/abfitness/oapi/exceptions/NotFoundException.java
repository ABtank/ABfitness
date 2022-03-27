package ru.abfitness.oapi.exceptions;

public class NotFoundException extends RuntimeException {

    private String className;
    private String errorMessage;
    private Integer id;

    public NotFoundException(String className, Integer id, String errorMessage) {
        this.className = className;
        this.errorMessage = errorMessage;
        this.id = id;
    }

    public NotFoundException() {
        this.errorMessage = "Resource Not Found";
    }

    public NotFoundException(String message) {
        this.errorMessage = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        StringBuilder sb = new StringBuilder("notFoundError{ ");
        if (className != null) sb.append("\"className\": \"").append(className).append("\", ");
        if (id != null) sb.append("\"id\": ").append(id).append(", ");
        sb.append("\"errorMessage\": \"").append(errorMessage).append("\" }");
        return sb.toString();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
