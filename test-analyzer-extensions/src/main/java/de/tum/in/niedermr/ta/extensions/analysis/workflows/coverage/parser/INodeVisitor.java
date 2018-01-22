package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import de.tum.in.niedermr.ta.core.analysis.result.receiver.IResultReceiver;

/** Node visitor. */
public interface INodeVisitor {

	/** Visit a node. */
	void visitNode(Node node, int nodeIndex, IResultReceiver resultReceiver) throws XPathExpressionException;
}
