package ru.sartfoms.mostat.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ReportDataId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long typeId;
	
	private Integer lpuId;
	
	private LocalDateTime dateTime;
	
	private Integer rowNum;
	
	public ReportDataId() {
	}

	public ReportDataId(Long typeId, Integer lpuId, LocalDateTime dateTime, Integer rowNum) {
		super();
		this.typeId = typeId;
		this.lpuId = lpuId;
		this.dateTime = dateTime;
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

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateTime, lpuId, rowNum, typeId);
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
		return Objects.equals(dateTime, other.dateTime) && Objects.equals(lpuId, other.lpuId)
				&& Objects.equals(rowNum, other.rowNum) && Objects.equals(typeId, other.typeId);
	}
}
