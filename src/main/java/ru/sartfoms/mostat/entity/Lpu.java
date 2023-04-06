package ru.sartfoms.mostat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ЛПУ", schema = "LPUOWNER")
public class Lpu {
	@Id
	@Column(name = "CD_LPU")
	private Integer id;
	
	@Column(name = "NM_LPU")
	private String name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
