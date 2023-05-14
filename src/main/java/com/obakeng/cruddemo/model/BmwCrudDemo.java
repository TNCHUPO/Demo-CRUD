package com.obakeng.cruddemo.model;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class BmwCrudDemo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "car")
	private String car;

	@Column(name = "description")
	private String description;

	@Column(name = "settled")
	private boolean settled;

	public BmwCrudDemo() {

	}

	public BmwCrudDemo(String car, String description, boolean settled) {
		this.car = car;
		this.description = description;
		this.settled = settled;
	}

	public BmwCrudDemo(long id, String car, String description, boolean settled) {
		this.id = id;
		this.car = car;
		this.description = description;
		this.settled = settled;
	}

	public long getId() {
		return id;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSettled() {
		return settled;
	}

	public void setSettled(boolean settled) {
		this.settled = settled;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", car=" + car + ", desc=" + description + ", published=" + settled + "]";
	}

}
