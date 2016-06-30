package com.yuanluesoft.bidding.project.pojo;


/**
 * KC值抽签公示(bidding_project_kc)
 * @author linchuan
 *
 */
public class BiddingProjectKC extends BiddingProjectComponent {
	private String basePrice; //标底价
	private String controlPrice; //控制价
	private String coefficient; //采用系数
	private String budgetPrice; //预算价
	private String preferentialPrice; //优惠价
	private String k1; //K1
	private String k2; //K2
	private String q; //Q
	/**
	 * @return the basePrice
	 */
	public String getBasePrice() {
		return basePrice;
	}
	/**
	 * @param basePrice the basePrice to set
	 */
	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}
	/**
	 * @return the budgetPrice
	 */
	public String getBudgetPrice() {
		return budgetPrice;
	}
	/**
	 * @param budgetPrice the budgetPrice to set
	 */
	public void setBudgetPrice(String budgetPrice) {
		this.budgetPrice = budgetPrice;
	}
	/**
	 * @return the coefficient
	 */
	public String getCoefficient() {
		return coefficient;
	}
	/**
	 * @param coefficient the coefficient to set
	 */
	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}
	/**
	 * @return the controlPrice
	 */
	public String getControlPrice() {
		return controlPrice;
	}
	/**
	 * @param controlPrice the controlPrice to set
	 */
	public void setControlPrice(String controlPrice) {
		this.controlPrice = controlPrice;
	}
	/**
	 * @return the k1
	 */
	public String getK1() {
		return k1;
	}
	/**
	 * @param k1 the k1 to set
	 */
	public void setK1(String k1) {
		this.k1 = k1;
	}
	/**
	 * @return the k2
	 */
	public String getK2() {
		return k2;
	}
	/**
	 * @param k2 the k2 to set
	 */
	public void setK2(String k2) {
		this.k2 = k2;
	}
	/**
	 * @return the preferentialPrice
	 */
	public String getPreferentialPrice() {
		return preferentialPrice;
	}
	/**
	 * @param preferentialPrice the preferentialPrice to set
	 */
	public void setPreferentialPrice(String preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}
	/**
	 * @return the q
	 */
	public String getQ() {
		return q;
	}
	/**
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}
}
