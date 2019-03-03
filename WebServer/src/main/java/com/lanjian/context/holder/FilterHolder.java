package com.lanjian.context.holder;

import com.lanjian.filter.Filter;

public class FilterHolder {
	private Filter filter;
	private String filterClass;

	public FilterHolder(String filterClass) {
		this.filterClass = filterClass;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public String getFilterClass() {
		return filterClass;
	}

	public void setFilterClass(String filterClass) {
		this.filterClass = filterClass;
	}
}
