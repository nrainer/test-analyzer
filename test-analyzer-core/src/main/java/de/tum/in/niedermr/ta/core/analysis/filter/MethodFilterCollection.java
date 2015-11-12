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
public class MethodFilterCollection {
	private static final Logger LOG = LogManager.getLogger(MethodFilterCollection.class);

	private final List<IMethodFilter> m_filterList;

	private MethodFilterCollection() {
		this.m_filterList = new ArrayList<>();
	}

	public static MethodFilterCollection createCollectionWithDefaultFilters() {
		MethodFilterCollection filterCollection = new MethodFilterCollection();
		filterCollection.addDefaultFilters();
		return filterCollection;
	}

	public static MethodFilterCollection createEmptyCollection() {
		return new MethodFilterCollection();
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

	public void addFilterCollection(IMethodFilter[] additionalFilters) {
		m_filterList.addAll(Arrays.asList(additionalFilters));
	}

	public FilterResult acceptMethod(MethodIdentifier methodIdentifier, MethodNode method) {
		FilterResult result = FilterResult.accepted();

		for (IMethodFilter filter : m_filterList) {
			try {
				result = filter.acceptMethod(methodIdentifier, method);

				if (!result.isAccepted()) {
					return result;
				}
			} catch (Exception ex) {
				LOG.error("Error when deciding whether to filter a method", ex);
				return FilterResult.skip(filter.getClass(), "Exception occurred: " + ex.getMessage());
			}
		}

		return result;
	}
}
