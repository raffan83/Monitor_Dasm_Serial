package it.lettoreSeriale.DTO;

import java.io.Serializable;
import java.math.BigDecimal;

public class SondaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850919143530936304L;
	
	
	private String id_sonda;
	private BigDecimal minScale;
	private BigDecimal maxScale;
	private String um;
	private BigDecimal precision;
	private String label;
	private boolean zero;
	
	public boolean isZero() {
		return zero;
	}
	public void setZero(boolean zero) {
		this.zero = zero;
	}
	public String getId_sonda() {
		return id_sonda;
	}
	public void setId_sonda(String id_sonda) {
		this.id_sonda = id_sonda;
	}
	public BigDecimal getMinScale() {
		return minScale;
	}
	public void setMinScale(BigDecimal minScale) {
		this.minScale = minScale;
	}
	public BigDecimal getMaxScale() {
		return maxScale;
	}
	public void setMaxScale(BigDecimal maxScale) {
		this.maxScale = maxScale;
	}
	public String getUm() {
		return um;
	}
	public void setUm(String um) {
		this.um = um;
	}
	public BigDecimal getPrecision() {
		return precision;
	}
	public void setPrecision(BigDecimal precision) {
		this.precision = precision;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	

}
