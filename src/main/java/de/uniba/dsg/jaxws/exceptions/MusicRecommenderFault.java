package de.uniba.dsg.jaxws.exceptions;

import javax.xml.ws.WebFault;

/**
 * Custom Fault with methods as required by the JAX-WS specification.
 * If the Exception does not follow this style, JAX-WS will generate a wrapper class 
 * and the Exception will be put in the FaultBean class. See java-exception example.
 */
@WebFault(name = "MusicRecommenderFault")
public class MusicRecommenderFault extends RuntimeException {
    /** Java type that goes as soapenv:Fault detail element. */
    private MusicRecommenderFaultBean faultInfo;

    public MusicRecommenderFault(String message, MusicRecommenderFaultBean faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    public MusicRecommenderFault(String message, String faultInfo) {
        super(message);
        MusicRecommenderFaultBean bean = new MusicRecommenderFaultBean();
        bean.setMessage(faultInfo);
        this.faultInfo = bean;
    }

    public MusicRecommenderFault(String message, MusicRecommenderFaultBean faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    public MusicRecommenderFaultBean getFaultInfo() {
        return faultInfo;
    }	
}