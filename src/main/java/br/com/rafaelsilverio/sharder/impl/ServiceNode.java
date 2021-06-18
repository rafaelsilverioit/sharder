package br.com.rafaelsilverio.sharder.impl;

import br.com.rafaelsilverio.sharder.Node;

public class ServiceNode
	implements Node {
	private final String descriptor;
	
	private final String address;
	
	private final Integer port;

	public ServiceNode(String descriptor, String address, Integer port) {
		this.descriptor = descriptor;
		this.address = address;
		this.port = port;
	}

	@Override
	public String getKey() {
		return String.format("%s-%s:%d", descriptor, address, port);
	}

	@Override
	public String toString() {
		return getKey();
	}
}
