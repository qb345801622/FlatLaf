/*
 * Copyright 2019 FormDev Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.formdev.flatlaf.ui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

/**
 * Provides the Flat LaF UI delegate for {@link javax.swing.JToolBar}.
 *
 * <!-- BasicToolBarUI -->
 *
 * @uiDefault ToolBar.font								Font
 * @uiDefault ToolBar.background						Color
 * @uiDefault ToolBar.foreground						Color
 * @uiDefault ToolBar.border							Border
 * @uiDefault ToolBar.dockingBackground					Color
 * @uiDefault ToolBar.dockingForeground					Color
 * @uiDefault ToolBar.floatingBackground				Color
 * @uiDefault ToolBar.floatingForeground				Color
 * @uiDefault ToolBar.isRollover						boolean
 *
 * <!-- FlatToolBarUI -->
 *
 * @uiDefault ToolBar.focusableButtons					boolean
 *
 * @author Karl Tauber
 */
public class FlatToolBarUI
	extends BasicToolBarUI
{
	/** @since 1.4 */
	protected boolean focusableButtons;

	public static ComponentUI createUI( JComponent c ) {
		return new FlatToolBarUI();
	}

	@Override
	public void installUI( JComponent c ) {
		super.installUI( c );

		// disable focusable state of buttons (when switching from another Laf)
		if( !focusableButtons )
			setButtonsFocusable( false );
	}

	@Override
	public void uninstallUI( JComponent c ) {
		super.uninstallUI( c );

		// re-enable focusable state of buttons (when switching to another Laf)
		if( !focusableButtons )
			setButtonsFocusable( true );
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();

		focusableButtons = UIManager.getBoolean( "ToolBar.focusableButtons" );
	}

	@Override
	protected ContainerListener createToolBarContListener() {
		return new ToolBarContListener() {
			@Override
			public void componentAdded( ContainerEvent e ) {
				super.componentAdded( e );

				if( !focusableButtons ) {
					Component c = e.getChild();
					if( c instanceof AbstractButton )
						c.setFocusable( false );
				}
			}

			@Override
			public void componentRemoved( ContainerEvent e ) {
				super.componentRemoved( e );

				if( !focusableButtons ) {
					Component c = e.getChild();
					if( c instanceof AbstractButton )
						c.setFocusable( true );
				}
			}
		};
	}

	/**
	 * @since 1.4
	 */
	protected void setButtonsFocusable( boolean focusable ) {
		for( Component c : toolBar.getComponents() ) {
			if( c instanceof AbstractButton )
				c.setFocusable( focusable );
		}
	}

	// disable rollover border
	@Override protected void setBorderToRollover( Component c ) {}
	@Override protected void setBorderToNonRollover( Component c ) {}
	@Override protected void setBorderToNormal( Component c ) {}
	@Override protected void installRolloverBorders( JComponent c ) {}
	@Override protected void installNonRolloverBorders( JComponent c ) {}
	@Override protected void installNormalBorders( JComponent c ) {}
	@Override protected Border createRolloverBorder() { return null; }
	@Override protected Border createNonRolloverBorder() { return null; }

	@Override
	public void setOrientation( int orientation ) {
		if( orientation != toolBar.getOrientation() ) {
			// swap margins if orientation changes when floating
			Insets margin = toolBar.getMargin();
			Insets newMargin = new Insets( margin.left, margin.top, margin.right, margin.bottom );
			if( !newMargin.equals( margin ) )
				toolBar.setMargin( newMargin );
		}

		super.setOrientation( orientation );
	}
}
