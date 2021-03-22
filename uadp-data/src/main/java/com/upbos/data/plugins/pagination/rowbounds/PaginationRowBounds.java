package com.upbos.data.plugins.pagination.rowbounds;

import com.upbos.data.core.Pagination;
import org.apache.ibatis.session.RowBounds;

/**
 * <p>Title: PaginationRowBounds.java</p>
 * <p>Description: pagination row bound</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class PaginationRowBounds extends RowBounds {
	private Pagination pagination;
	private boolean hasTotal = true;
	
	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public boolean isHasTotal() {
		return hasTotal;
	}

	public void setHasTotal(boolean hasTotal) {
		this.hasTotal = hasTotal;
	}

	public PaginationRowBounds(int offset, int limit) {
		super(offset, limit);
	}
}
