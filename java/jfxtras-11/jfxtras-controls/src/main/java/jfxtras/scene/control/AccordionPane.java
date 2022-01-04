/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class AccordionPane extends Control {
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public AccordionPane()
	{
		construct();
	}
	
	/**
	 * 
	 */
	public AccordionPane(Tab... tabs)
	{
		construct();
		
		// add tabs
		for (Tab lTab : tabs) {
			this.tabs.add(lTab);
		}
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(AccordionPane.class.getSimpleName());
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override public String getUserAgentStylesheet()
	{
		return AccordionPane.class.getResource("/jfxtras/internal/scene/control/" + AccordionPane.class.getSimpleName() + ".css").toExternalForm();
	}
	
	@Override public Skin<?> createDefaultSkin() {
		return new jfxtras.internal.scene.control.skin.AccordionSkin(this); 
	}

	// ==================================================================================================================
	// PROPERTIES

	/** Id: for a fluent API */
	public AccordionPane withId(String value) { setId(value); return this; }

	/** tabs */
	public ObservableList<Tab> tabs() { return tabs; }
	final private ObservableList<Tab> tabs =  javafx.collections.FXCollections.observableArrayList();
	
	/**
	 * Convenience method
	 */
	public AccordionPane addTab(String name, Node node) {
		Tab lTab = new Tab().withText(name).withNode(node);
		tabs.add(lTab);
		return this;
	}
	
	/**
	 * Convenience method
	 */
	public AccordionPane addTab(String name, Node icon, Node node) {
		Tab lTab = new Tab().withText(name).withIcon(icon).withNode(node);
		tabs.add(lTab);
		return this;
	}
	
	/**
	 * Convenience method
	 */
	public AccordionPane addTab(Node icon, Node node) {
		Tab lTab = new Tab().withIcon(icon).withNode(node);
		tabs.add(lTab);
		return this;
	}
	
	/** visibleTab */
	public ObjectProperty<Tab> visibleTabProperty() { return visibleTabObjectProperty; }
	volatile private ObjectProperty<Tab> visibleTabObjectProperty = new SimpleObjectProperty<Tab>(this, "visibleTab", null);
	public Tab getVisibleTab() { return visibleTabObjectProperty.getValue(); }
	public void setVisibleTab(Tab value) { visibleTabObjectProperty.setValue(value); }
	public AccordionPane withVisibleTab(Tab value) { setVisibleTab(value); return this; }

	// ==================================================================================================================
	// TAB

	public static class Tab {

		/** text */
		public ObjectProperty<String> textProperty() { return textObjectProperty; }
		volatile private ObjectProperty<String> textObjectProperty = new SimpleObjectProperty<String>(this, "text", null);
		public String getText() { return textObjectProperty.getValue(); }
		public void setText(String value) { textObjectProperty.setValue(value); }
		public Tab withText(String value) { setText(value); return this; }
		
		/** icon */
		public ObjectProperty<Node> iconProperty() { return iconObjectProperty; }
		volatile private ObjectProperty<Node> iconObjectProperty = new SimpleObjectProperty<Node>(this, "icon", null);
		public Node getIcon() { return iconObjectProperty.getValue(); }
		public void setIcon(Node value) { iconObjectProperty.setValue(value); }
		public Tab withIcon(Node value) { setIcon(value); return this; }
		
		/** node */
		public ObjectProperty<Node> nodeProperty() { return nodeObjectProperty; }
		volatile private ObjectProperty<Node> nodeObjectProperty = new SimpleObjectProperty<Node>(this, "node", null);
		public Node getNode() { return nodeObjectProperty.getValue(); }
		public void setNode(Node value) { nodeObjectProperty.setValue(value); }
		public Tab withNode(Node value) { setNode(value); return this; }
		
		public String toString() {
			return super.toString() 
			     + ", text=" + getText()
			     ;
		}
	}
}
