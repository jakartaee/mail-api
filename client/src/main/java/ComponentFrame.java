/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * this Frame provides a utility class for displaying a single
 * Component in a Frame.
 *
 * @author	Christopher Cotton
 */

public class ComponentFrame extends JFrame {
    
    /**
     * creates the frame
     * @param what	the component to display
     */
    public ComponentFrame(Component what) {
	this(what, "Component Frame");
    }

    /**
     * creates the frame with the given name
     * @param what	the component to display
     * @param name	the name of the Frame
     */
    public ComponentFrame(Component what, String name) {
	super(name);

	// make sure that we close and dispose ourselves when needed
	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	// default size of the frame
	setSize(700,600);

	// we want to display just the component in the entire frame
	if (what != null) {
	    getContentPane().add("Center", what);
	}
    }
}
