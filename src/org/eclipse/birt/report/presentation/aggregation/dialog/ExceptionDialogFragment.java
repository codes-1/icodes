/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.presentation.aggregation.dialog;

import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;

/**
 * Fragment for report tool bar.
 * <p>
 * 
 * @see BaseFragment
 */
public class ExceptionDialogFragment extends BaseDialogFragment
{

	/**
	 * Get unique id of the corresponding UI gesture.
	 * 
	 * @return id
	 */
	public String getClientId( )
	{
		return "exceptionDialog"; //$NON-NLS-1$
	}

	/**
	 * Get name of the corresponding UI gesture.
	 * 
	 * @return id
	 */
	public String getClientName( )
	{
		return "Exception"; //$NON-NLS-1$
	}

	/**
	 * Gets the title ID for the html page.
	 * 
	 * @return title id
	 */

	public String getTitle( )
	{
		return BirtResources
				.getMessage( ResourceConstants.EXCEPTION_DIALOG_TITLE );
	}
}
