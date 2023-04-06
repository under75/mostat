package ru.sartfoms.mostat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mostat_users", schema = "BIGADMIN")
public class User {

	@Id
	@Column(name = "u_name")
	private String name;

	@Column(name = "u_type")
	private String type;

	@Column(name = "u_lpu")
	private Integer lpuId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLpuId() {
		return lpuId;
	}

	public void setLpuId(Integer lpuId) {
		this.lpuId = lpuId;
	}

}
