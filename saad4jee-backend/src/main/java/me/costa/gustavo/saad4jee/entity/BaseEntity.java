package me.costa.gustavo.saad4jee.entity;

import java.io.Serializable;

public abstract class BaseEntity <T extends Serializable>{
	public abstract T getId();
}
