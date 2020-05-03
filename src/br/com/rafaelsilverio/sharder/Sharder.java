package br.com.rafaelsilverio.sharder;

import java.util.Optional;

public interface Sharder<T extends Node> {
	void add(T node, Long vNodeCount);
	
	void remove(T node);
	
	Optional<T> route(String key);
	
	Long getExistingReplicas(T node);
}
