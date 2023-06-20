package ru.sartfoms.mostat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MO_CERT_NAME", schema = "STATOWNER")
public class Certificate {
	@Id
	@Column(name = "LPU_ID")
	private Integer lpuId;
	
	@Column(name = "CERT_NAME")
	private String cert;

	public Integer getLpuId() {
		return lpuId;
	}

	public String getCert() {
		return cert;
	}

	public void setLpuId(Integer lpuId) {
		this.lpuId = lpuId;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}
	
}
