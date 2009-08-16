package com.ironrhino.biz.model;

import org.apache.commons.lang.StringUtils;
import org.ironrhino.common.util.NumberUtils;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.model.BaseEntity;

@AutoConfig
public class Spec extends BaseEntity {

	private String basicPackName;

	private Double basicQuantity;

	private String basicMeasure;

	private Integer baleQuantity;

	private String balePackName;

	private String purity;

	public String getBasicPackName() {
		return basicPackName;
	}

	public void setBasicPackName(String basicPackName) {
		this.basicPackName = basicPackName;
	}

	public Double getBasicQuantity() {
		return basicQuantity;
	}

	public void setBasicQuantity(Double basicQuantity) {
		this.basicQuantity = basicQuantity;
	}

	public String getBasicMeasure() {
		return basicMeasure;
	}

	public void setBasicMeasure(String basicMeasure) {
		this.basicMeasure = basicMeasure;
	}

	public Integer getBaleQuantity() {
		return baleQuantity;
	}

	public void setBaleQuantity(Integer baleQuantity) {
		this.baleQuantity = baleQuantity;
	}

	public String getBalePackName() {
		return balePackName;
	}

	public void setBalePackName(String balePackName) {
		this.balePackName = balePackName;
	}

	public String getPurity() {
		return purity;
	}

	public void setPurity(String purity) {
		this.purity = purity;
	}

	@NotInCopy
	public String getUnit() {
		if (basicQuantity == null || basicQuantity <= 0)
			return basicPackName;
		StringBuilder sb = new StringBuilder();
		sb.append(basicPackName);
		sb.append('(');
		if (StringUtils.isNotBlank(purity))
			sb.append(purity);
		if (basicQuantity.doubleValue() - basicQuantity.intValue() == 0)
			sb.append(NumberUtils.format(basicQuantity, 0));
		else
			sb.append(basicQuantity);
		sb.append(basicMeasure);
		sb.append(')');
		return sb.toString();

	}

	@NotInCopy
	public String getName() {
		if (basicQuantity == null || basicQuantity <= 0)
			return basicPackName;
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(purity))
			sb.append(purity);
		if (basicQuantity.doubleValue() - basicQuantity.intValue() == 0)
			sb.append(NumberUtils.format(basicQuantity, 0));
		else
			sb.append(basicQuantity);
		sb.append(basicMeasure);

		if (baleQuantity != null && baleQuantity > 0) {
			sb.append('*');
			sb.append(baleQuantity);
			sb.append(basicPackName);
			sb.append('/');
			sb.append(balePackName);
		} else {
			sb.append('/');
			sb.append(basicPackName);
		}
		return sb.toString();
	}

	public String toString() {
		return getName();
	}

}
