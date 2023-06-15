package ru.sartfoms.mostat.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "MO_REPORT_STATUS", schema = "STATOWNER")
@IdClass(ReportStatusId.class)
public class ReportStatus {
	@Id
	@Column(name = "REP_TYPE_ID")
	private Long typeId;
	@Id
	@Column(name = "LPU_ID")
	private Integer lpuId;
	@Id
	@Column(name = "DT_REP")
	private LocalDate dtRep;
	
	@Column(name = "DT_INS")
	private LocalDate dtIns;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "DT_CLOS")
	private LocalDate dtClos;
	
	@Column(name = "USR_INS")
	private String usrIns;
	
	@Column(name = "USR_CLOS")
	private String usrClos;
	
	@OneToOne
	@NotFound(action = NotFoundAction.IGNORE) 
	@JoinColumn(name = "REP_TYPE_ID", referencedColumnName = "ID", insertable=false, updatable=false)
	private ReportType reportType;
	
	@OneToOne
	@NotFound(action = NotFoundAction.IGNORE) 
	@JoinColumn(name = "LPU_ID", referencedColumnName = "CD_LPU", insertable=false, updatable=false)
	private Lpu lpu;

	public Long getTypeId() {
		return typeId;
	}

	public Integer getLpuId() {
		return lpuId;
	}

	public LocalDate getDtRep() {
		return dtRep;
	}

	public LocalDate getDtIns() {
		return dtIns;
	}

	public LocalDate getDtClos() {
		return dtClos;
	}

	public String getUsrIns() {
		return usrIns;
	}

	public String getUsrClos() {
		return usrClos;
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

	public void setDtIns(LocalDate dtIns) {
		this.dtIns = dtIns;
	}

	public void setDtClos(LocalDate dtClos) {
		this.dtClos = dtClos;
	}

	public void setUsrIns(String usrIns) {
		this.usrIns = usrIns;
	}

	public void setUsrClos(String usrClos) {
		this.usrClos = usrClos;
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public Lpu getLpu() {
		return lpu;
	}

	public void setLpu(Lpu lpu) {
		this.lpu = lpu;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
