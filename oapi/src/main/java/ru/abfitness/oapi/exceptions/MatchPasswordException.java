package ru.abfitness.oapi.exceptions;

public class MatchPasswordException extends RuntimeException {
    public MatchPasswordException(String message) {
        super(message);
    }
}
