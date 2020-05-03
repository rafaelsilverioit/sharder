package br.com.rafaelsilverio.sharder.impl;

import br.com.rafaelsilverio.sharder.Node;

public class VirtualNode<T extends Node>
	implements Node {
    private final T physicalNode;

    private final Long replicaIndex;

    public VirtualNode(T physicalNode, Long index) {
    	this.physicalNode = physicalNode;
        this.replicaIndex = index;
    }

    @Override
    public String getKey() {
    	return String.format("%s-%d", physicalNode.getKey(), replicaIndex);
    }
    
    public T getPhysicalNode() {
        return physicalNode;
    }

    public boolean isVirtualNodeOf(T node) {
        String key = node.getKey();
		return physicalNode.getKey().equals(key);
    }
}