package de.tum.in.niedermr.ta.core.analysis.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.core.ConstructorFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.MethodNameFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.NonEmptyMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.ValueGenerationSupportedFilter;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/**
 * Combines multiple {@link IMethodFilter}s.
 */
public final class MethodFilterList implements IMethodFilter {
	private static final Logger LOG = LogManager.getLogger(MethodFilterList.class);

	private final List<IMethodFilter> m_filterList;

	private MethodFilterList() {
		this.m_filterList = new ArrayList<>();
	}

	public static MethodFilterList createWithDefaultFilters() {
		MethodFilterList filterCollection = createEmpty();
		filterCollection.addDefaultFilters();
		return filterCollection;
	}

	public static MethodFilterList createEmpty() {
		return new MethodFilterList();
	}

	private void addDefaultFilters() {
		addFilter(new ConstructorFilter());
		addFilter(new NonEmptyMethodFilter());
	}

	public void addNameFilter(MethodIdentifier... methodIdentifiers) {
		addFilter(new MethodNameFilter(methodIdentifiers));
	}

	public void addValueGenerationSupportedFilter(IReturnValueGenerator returnValueGenerator) {
		addFilter(new ValueGenerationSupportedFilter(returnValueGenerator));
	}

	public void addFilter(IMethodFilter filter) {
		m_filterList.add(filter);
	}

	public void addFilters(IMethodFilter[] additionalFilters) {
		m_filterList.addAll(Arrays.asList(additionalFilters));
	}

	/** {@inheritDoc} */
	@Override
	public FilterResult apply(MethodIdentifier methodIdentifier, MethodNode methodNode) {
		FilterResult result = FilterResult.accepted();

		for (IMethodFilter filter : m_filterList) {
			try {
				result = filter.apply(methodIdentifier, methodNode);

				if (!result.isAccepted()) {
					return result;
				}
			} catch (Exception ex) {
				LOG.error("Error when deciding whether to filter a method with filter: " + filter.getClass(), ex);
				return FilterResult.reject(filter.getClass(), "Exception occurred: " + ex.getMessage());
			}
		}

		return result;
	}
}
