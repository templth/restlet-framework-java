/*
 * Copyright 2005-2008 Noelios Consulting.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package com.noelios.restlet.component;

import java.util.logging.Logger;

import org.restlet.Context;

/**
 * Context allowing access to the component's connectors.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ComponentContext extends Context {
    /** The client dispatcher. */
    private volatile ComponentClientDispatcher clientDispatcher;

    /** The component helper. */
    private volatile ComponentHelper componentHelper;

    /** The server dispatcher. */
    private volatile ComponentServerDispatcher serverDispatcher;

    /**
     * Constructor.
     * 
     * @param componentHelper
     *                The component helper.
     * @param logger
     *                The logger instance of use.
     */
    public ComponentContext(ComponentHelper componentHelper, Logger logger) {
        super(logger);
        this.componentHelper = componentHelper;
        this.clientDispatcher = new ComponentClientDispatcher(this);
        this.serverDispatcher = new ComponentServerDispatcher(this);
    }

    @Override
    public ComponentClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }

    /**
     * Returns the component helper.
     * 
     * @return The component helper.
     */
    protected ComponentHelper getComponentHelper() {
        return this.componentHelper;
    }

    @Override
    public ComponentServerDispatcher getServerDispatcher() {
        return this.serverDispatcher;
    }

    /**
     * Sets the component helper.
     * 
     * @param componentHelper
     *                The component helper.
     */
    protected void setComponentHelper(ComponentHelper componentHelper) {
        this.componentHelper = componentHelper;
    }
}