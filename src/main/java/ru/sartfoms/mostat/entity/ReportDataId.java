package ru.sartfoms.mostat.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ReportDataId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long typeId;

	private Integer lpuId;

	private LocalDate dtRep;

	private Integer rowNum;

	public ReportDataId() {
	}

	public ReportDataId(Long typeId, Integer lpuId, LocalDate dtRep, Integer rowNum) {
		super();
		this.typeId = typeId;
		this.lpuId = lpuId;
		this.dtRep = dtRep;
		this.rowNum = rowNum;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Integer getLpuId() {
		return lpuId;
	}

	public void setLpuId(Integer lpuId) {
		this.lpuId = lpuId;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public LocalDate getDtRep() {
		return dtRep;
	}

	public void setDtRep(LocalDate dtRep) {
		this.dtRep = dtRep;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dtRep, lpuId, rowNum, typeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportDataId other = (ReportDataId) obj;
		return Objects.equals(dtRep, other.dtRep) && Objects.equals(lpuId, other.lpuId)
				&& Objects.equals(rowNum, other.rowNum) && Objects.equals(typeId, other.typeId);
	}

}
