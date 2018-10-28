package com.serviceenabled.dropwizardrequesttracker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RequestTrackerConfigurationTest {

    private RequestTrackerConfiguration sut;
    private static final String ANY = "ANY";

    @Test
    public void testSettersGetters() {
        sut = new RequestTrackerConfiguration();
        sut.setAddResponseHeader(Boolean.FALSE);
        sut.setHeaderName(ANY);
        sut.setMdcKey(ANY);

        assertEquals(ANY, sut.getMdcKey());
        assertEquals(ANY, sut.getHeaderName());
        assertEquals(Boolean.FALSE, sut.getAddResponseHeader());
    }

    @Test
    public void testConstructor() {
        sut = new RequestTrackerConfiguration(ANY, ANY);

        assertEquals(ANY, sut.getHeaderName());
        assertEquals(ANY, sut.getMdcKey());
    }
}
