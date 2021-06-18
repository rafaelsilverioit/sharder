package br.com.rafaelsilverio.sharder.impl;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import br.com.rafaelsilverio.sharder.HashAlgorithm;
import br.com.rafaelsilverio.sharder.Node;
import br.com.rafaelsilverio.sharder.Sharder;
import br.com.rafaelsilverio.sharder.impl.DigestAlgorithm.Algorithms;

public class ConsistentHash<T extends Node> 
	implements Sharder<T> {
	private final HashAlgorithm hashAlgorithm;
	
	private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();

	public ConsistentHash(Collection<T> nodes, Long vNodeCount)
		throws NoSuchAlgorithmException {
		this(nodes, vNodeCount, new DigestAlgorithm(Algorithms.MD5));
	}

	public ConsistentHash(Collection<T> nodes, Long vNodeCount, HashAlgorithm algorithm) {
		if(algorithm == null) {
			throw new IllegalArgumentException("Hash algorithm cannot be null.");
		}
		
		this.hashAlgorithm = algorithm;
		
		if (nodes != null) {
			for (T node : nodes) {
				add(node, vNodeCount);
			}
		}
	}

	@Override
	public void add(T node, Long vNodeCount) {
		if(vNodeCount < 0) {
			throw new IllegalArgumentException("Virtual nodes cannot be lesser than 0.");
		}
		
		Long existingReplicas = getExistingReplicas(node);
		
		for(Integer i = 0; i < vNodeCount; i++) {
			Long index = i + existingReplicas;
			VirtualNode<T> vNode = new VirtualNode<>(node, index);
			Long hash = hashAlgorithm.hash(vNode.getKey());
			
			ring.put(hash, vNode);
		}
	}

	@Override
	public void remove(T node) {
		Iterator<Long> it = ring.keySet().iterator();
		
		while(it.hasNext()) {
			Long key = it.next();
			VirtualNode<T> virtualNode = ring.get(key);
			
			if(virtualNode.isVirtualNodeOf(node)) {
				it.remove();
			}
		}
	}

	@Override
	public Optional<T> route(String key) {
		if(ring.isEmpty()) {
			return Optional.empty();
		}
		
		Long hashVal = hashAlgorithm.hash(key);
		SortedMap<Long, VirtualNode<T>> tailMap = ring.tailMap(hashVal);

		Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
		T physicalNode = ring.get(nodeHashVal).getPhysicalNode();
		
		return Optional.of(physicalNode);
	}

	@Override
	public Long getExistingReplicas(T node) {
		return ring
			.values()
			.stream()
			.filter(v -> v.isVirtualNodeOf(node))
			.count();
	}
}