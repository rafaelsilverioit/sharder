package br.com.rafaelsilverio.sharder;

public interface HashAlgorithm {
    Long hash(String key);
}