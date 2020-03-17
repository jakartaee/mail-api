/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import jakarta.mail.*;
import java.util.Date;
import javax.swing.table.AbstractTableModel; 

/**
 * Maps the messages in a Folder to the Swing's Table Model
 *
 * @author	Christopher Cotton
 * @author	Bill Shannon
 */

public class FolderModel extends AbstractTableModel {
    
    Folder	folder;
    Message[]	messages;

    String[]	columnNames = { "Date", "From", "Subject"}; 
    Class[]	columnTypes = { String.class, String.class, String.class }; 

    public void setFolder(Folder what) throws MessagingException {
	if (what != null) {

	    // opened if needed
	    if (!what.isOpen()) {
		what.open(Folder.READ_WRITE);
	    }
    
	    // get the messages
	    messages = what.getMessages();
	    cached = new String[messages.length][];
	} else {
	    messages = null;
	    cached = null;
	}
	// close previous folder and switch to new folder
	if (folder != null)
	    folder.close(true);
	folder = what;
	fireTableDataChanged();
    }
    
    public Message getMessage(int which) {
	return messages[which];
    }

    //---------------------
    // Implementation of the TableModel methods
    //---------------------

    public String getColumnName(int column) {
	return columnNames[column];
    }
    
    public Class getColumnClass(int column) {
	return columnTypes[column];
    }
    

    public int getColumnCount() {
	return columnNames.length; 
    }

    public int getRowCount() {
	if (messages == null)
	    return 0;
	
	return messages.length;
    }
 
    public Object getValueAt(int aRow, int aColumn) {
	switch(aColumn) {
	case 0:	// date
	case 1: // From		String[] what = getCachedData(aRow);
	case 2: // Subject
	    String[] what = getCachedData(aRow);
	    if (what != null) {
		return what[aColumn];
	    } else {
		return "";
	    }
	    
	default:
	    return "";
	}
    }

    protected static String[][]	cached;
    
    protected String[] getCachedData(int row) {
	if (cached[row] == null) {
	    try{
		Message m = messages[row];
	    
		String[] theData = new String[4];
	    
		// Date
		Date date = m.getSentDate();
		if (date == null) {
		    theData[0] = "Unknown";
		} else {
		    theData[0] = date.toString();
		}
	    
		// From
		Address[] adds = m.getFrom();
		if (adds != null && adds.length != 0) {
		    theData[1] = adds[0].toString();	    
		} else {
		    theData[1] = "";
		}
		
		// Subject
		String subject = m.getSubject();
		if (subject != null) {
		    theData[2] = subject;
		} else {
		    theData[2] = "(No Subject)";
		}

		cached[row] = theData;
	    }
	    catch (MessagingException e) {
		e.printStackTrace();
	    }
	}
	
	return cached[row];
    }
}
