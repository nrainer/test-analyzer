package de.tum.in.niedermr.ta.extensions.analysis.workflows.coverage.parser;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

/** Node visitor. */
public interface INodeVisitor {

	/** Visit a node. */
	void visitNode(Node node, int nodeIndex) throws XPathExpressionException;
}
