package ru.sartfoms.mostat.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import ru.sartfoms.mostat.model.MOReport;

@Entity
@Table(name = "V_MO_REPORT_DATA", schema = "STATOWNER")
@IdClass(ReportDataId.class)
public class VReportData implements MOReport {
	@Id
	@Column(name = "REP_TYPE_ID")
	private Long typeId;
	@Id
	@Column(name = "LPU_ID")
	private Integer lpuId;
	@Id
	@Column(name = "DT_REP")
	private LocalDate dtRep;
	@Id
	@Column(name = "REP_ROW_NUM")
	private Integer rowNum;

	@Column(name = "DT_INS")
	private LocalDateTime dtIns;

	@Column(name = "USR")
	private String userName;

	@Lob
	@Column(name = "FILE_")
	private byte[] file;

	@Column(name = "COL_E")
	private String e;

	@Column(name = "COL_F")
	private String f;

	@Column(name = "COL_G")
	private String g;

	@Column(name = "COL_H")
	private String h;

	@Column(name = "COL_I")
	private String i;

	@Column(name = "COL_J")
	private String j;

	@Column(name = "COL_K")
	private String k;

	@Column(name = "COL_L")
	private String l;

	@Column(name = "COL_M")
	private String m;

	@Column(name = "COL_N")
	private String n;

	@Column(name = "COL_O")
	private String o;

	@Column(name = "COL_P")
	private String p;

	@Column(name = "COL_Q")
	private String q;

	@Column(name = "COL_R")
	private String r;

	@Column(name = "COL_S")
	private String s;

	@Column(name = "COL_T")
	private String t;

	@Column(name = "COL_U")
	private String u;

	@Column(name = "COL_V")
	private String v;

	@Column(name = "COL_W")
	private String w;

	@Column(name = "COL_X")
	private String x;

	@Column(name = "COL_Y")
	private String y;

	@Column(name = "COL_Z")
	private String z;

	@Column(name = "COL_AA")
	private String aa;

	@Column(name = "COL_AB")
	private String ab;

	@Column(name = "COL_AC")
	private String ac;

	@Column(name = "COL_AD")
	private String ad;

	@Column(name = "COL_AE")
	private String ae;

	@Column(name = "COL_AF")
	private String af;

	@Column(name = "COL_AG")
	private String ag;

	@Column(name = "COL_AH")
	private String ah;

	@Column(name = "COL_AI")
	private String ai;

	@Column(name = "COL_AJ")
	private String aj;

	@Column(name = "COL_AK")
	private String ak;

	@Column(name = "COL_AL")
	private String al;

	@Column(name = "COL_AM")
	private String am;

	@Column(name = "COL_AN")
	private String an;

	@Column(name = "COL_AO")
	private String ao;

	@Column(name = "COL_AP")
	private String ap;

	@Column(name = "COL_AQ")
	private String aq;

	@Column(name = "COL_AR")
	private String ar;

	@Column(name = "COL_AS")
	private String as;

	@Column(name = "COL_AT")
	private String at;

	@Column(name = "COL_AU")
	private String au;

	@Column(name = "COL_AV")
	private String av;

	@Column(name = "COL_AW")
	private String aw;

	@Column(name = "COL_AX")
	private String ax;

	@Column(name = "COL_AY")
	private String ay;

	@Column(name = "COL_AZ")
	private String az;

	@Column(name = "COL_BA")
	private String ba;

	@Column(name = "COL_BB")
	private String bb;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getI() {
		return i;
	}

	public void setI(String i) {
		this.i = i;
	}

	public String getJ() {
		return j;
	}

	public void setJ(String j) {
		this.j = j;
	}

	public String getK() {
		return k;
	}

	public void setK(String k) {
		this.k = k;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getZ() {
		return z;
	}

	public void setZ(String z) {
		this.z = z;
	}

	public String getAa() {
		return aa;
	}

	public void setAa(String aa) {
		this.aa = aa;
	}

	public String getAb() {
		return ab;
	}

	public void setAb(String ab) {
		this.ab = ab;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public String getAe() {
		return ae;
	}

	public void setAe(String ae) {
		this.ae = ae;
	}

	public String getAf() {
		return af;
	}

	public void setAf(String af) {
		this.af = af;
	}

	public String getAg() {
		return ag;
	}

	public void setAg(String ag) {
		this.ag = ag;
	}

	public String getAh() {
		return ah;
	}

	public void setAh(String ah) {
		this.ah = ah;
	}

	public String getAi() {
		return ai;
	}

	public void setAi(String ai) {
		this.ai = ai;
	}

	public String getAj() {
		return aj;
	}

	public void setAj(String aj) {
		this.aj = aj;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getAl() {
		return al;
	}

	public void setAl(String al) {
		this.al = al;
	}

	public String getAm() {
		return am;
	}

	public void setAm(String am) {
		this.am = am;
	}

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getAo() {
		return ao;
	}

	public void setAo(String ao) {
		this.ao = ao;
	}

	public String getAp() {
		return ap;
	}

	public void setAp(String ap) {
		this.ap = ap;
	}

	public String getAq() {
		return aq;
	}

	public void setAq(String aq) {
		this.aq = aq;
	}

	public String getAr() {
		return ar;
	}

	public void setAr(String ar) {
		this.ar = ar;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public String getAu() {
		return au;
	}

	public void setAu(String au) {
		this.au = au;
	}

	public String getAv() {
		return av;
	}

	public void setAv(String av) {
		this.av = av;
	}

	public String getAw() {
		return aw;
	}

	public void setAw(String aw) {
		this.aw = aw;
	}

	public String getAx() {
		return ax;
	}

	public void setAx(String ax) {
		this.ax = ax;
	}

	public String getAy() {
		return ay;
	}

	public void setAy(String ay) {
		this.ay = ay;
	}

	public String getAz() {
		return az;
	}

	public void setAz(String az) {
		this.az = az;
	}

	public String getBa() {
		return ba;
	}

	public void setBa(String ba) {
		this.ba = ba;
	}

	public String getBb() {
		return bb;
	}

	public void setBb(String bb) {
		this.bb = bb;
	}

	public LocalDate getDtRep() {
		return dtRep;
	}

	public LocalDateTime getDtIns() {
		return dtIns;
	}

	public void setDtRep(LocalDate dtRep) {
		this.dtRep = dtRep;
	}

	public void setDtIns(LocalDateTime dtIns) {
		this.dtIns = dtIns;
	}

}
