package de.uniba.dsg.jaxws.exceptions;

/** Java type that goes as soapenv:Fault detail element. */
public class MusicRecommenderFaultBean {
    private String message;

    public MusicRecommenderFaultBean() { }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}