package dev.herrerao;

import java.io.Serializable;

public record User(int id, String name) implements Serializable { }
