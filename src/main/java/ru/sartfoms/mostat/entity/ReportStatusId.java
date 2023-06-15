package ru.sartfoms.mostat.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ReportStatusId implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long typeId;
	private Integer lpuId;
	private LocalDate dtRep;
	
	public ReportStatusId() {
	}

	public ReportStatusId(Long typeId, Integer lpuId, LocalDate dtRep) {
		super();
		this.typeId = typeId;
		this.lpuId = lpuId;
		this.dtRep = dtRep;
	}

	public Long getTypeId() {
		return typeId;
	}

	public Integer getLpuId() {
		return lpuId;
	}

	public LocalDate getDtRep() {
		return dtRep;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public void setLpuId(Integer lpuId) {
		this.lpuId = lpuId;
	}

	public void setDtRep(LocalDate dtRep) {
		this.dtRep = dtRep;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dtRep, lpuId, typeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportStatusId other = (ReportStatusId) obj;
		return Objects.equals(dtRep, other.dtRep) && Objects.equals(lpuId, other.lpuId)
				&& Objects.equals(typeId, other.typeId);
	}
	
}
