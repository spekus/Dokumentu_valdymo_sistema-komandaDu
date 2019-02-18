package it.akademija.exceptions;

public class NoApproverAvailableException extends Exception {

    public NoApproverAvailableException(String message) {
        System.out.println("in NoApproverAvailableException: " + message);
    }



}
