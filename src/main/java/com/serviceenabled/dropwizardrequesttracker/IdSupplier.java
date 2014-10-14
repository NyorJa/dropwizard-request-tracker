package com.serviceenabled.dropwizardrequesttracker;

import com.google.common.base.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Supplies a random id
 */
public class IdSupplier implements Supplier<String> {
	private Logger logger = LoggerFactory.getLogger(IdSupplier.class);

	@Override
	public String get() {
		String id = UUID.randomUUID().toString();
		logger.debug("Supplying ID {}", id);
		return id;
	}
}
