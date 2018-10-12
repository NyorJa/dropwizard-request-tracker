package com.serviceenabled.dropwizardrequesttracker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.MDC;

public class RequestTrackerServletFilterTest {
	@Rule public MockitoRule rule = MockitoJUnit.rule();

	private RequestTrackerServletFilter requestTrackerServletFilter;
	private RequestTrackerConfiguration configuration;

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private FilterChain chain;

	@Before
	public void setUp() {
		this.configuration = new RequestTrackerConfiguration();
		requestTrackerServletFilter = new RequestTrackerServletFilter(this.configuration);
	}

	@After
	public void tearDown() {
		MDC.clear();
	}

	@Test
	public void callsChainDoFilter() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(chain).doFilter(request, response);
	}

	@Test
	public void checksForHeader() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(request).getHeader(configuration.getHeaderName());
	}

	@Test
	public void setsIdWhenHeaderMissing() throws Exception {
		requestTrackerServletFilter.doFilter(request, response, chain);

		UUID.fromString(MDC.get(configuration.getMdcKey()));
		// Didn't throw an IllegalArgumentException so it worked!
	}

	@Test
	public void reusesIdWhenHeaderPresent() throws Exception {
		String headerId = UUID.randomUUID().toString();
		when(request.getHeader(configuration.getHeaderName())).thenReturn(headerId);

		requestTrackerServletFilter.doFilter(request, response, chain);

		String idInLog = MDC.get(configuration.getMdcKey());
		assertThat(idInLog, equalTo(headerId));
	}

	@Test
	public void addsResponseHeaderWhenConfigured() throws Exception {
		this.configuration.setAddResponseHeader(true);

		//Re-injecting the configuration to ensure the updated configuration is applied.
		requestTrackerServletFilter = new RequestTrackerServletFilter(this.configuration);
		requestTrackerServletFilter.doFilter(request, response, chain);

		verify(response).addHeader( configuration.getHeaderName(), MDC.get(configuration.getMdcKey()));
	}
}
