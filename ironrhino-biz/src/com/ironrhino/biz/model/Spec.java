package com.ironrhino.biz.model;

import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.metadata.NotInCopy;
import org.ironrhino.core.metadata.NotInJson;
import org.ironrhino.core.model.Entity;
import org.ironrhino.core.model.Ordered;
import org.ironrhino.core.util.NumberUtils;

@AutoConfig
public class Spec extends Entity<Long> implements Ordered {

	private static final long serialVersionUID = -5598869269986567651L;

	private Long id;

	private String basicPackName;

	private Double basicQuantity;

	private String basicMeasure;

	private Integer baleQuantity;

	private String balePackName;

	private int displayOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	@NotInJson
	public boolean isNew() {
		return id == null || id == 0;
	}

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

	@NotInCopy
	public String getUnit() {
		if (basicQuantity == null || basicQuantity <= 0)
			return basicPackName;
		StringBuilder sb = new StringBuilder();
		sb.append(basicPackName);
		sb.append('(');
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

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public int compareTo(Object object) {
		if (!(object instanceof Spec))
			return 0;
		Spec entity = (Spec) object;
		if (this.getDisplayOrder() != entity.getDisplayOrder())
			return this.getDisplayOrder() - entity.getDisplayOrder();
		return this.getName().compareTo(entity.getName());
	}

	@Override
	public String toString() {
		return getName();
	}

}
