/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2009, 2018 Jason Mehrens. All Rights Reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import com.sun.mail.util.logging.CollectorFormatter;
import com.sun.mail.util.logging.CompactFormatter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A compact formatter used to summarize an error report.
 *
 * @author Jason Mehrens
 */
public final class SummaryFormatter extends Formatter {

    /**
     * The line formatter.
     */
    private final CompactFormatter format;
    /**
     * The footer formatter.
     */
    private final CollectorFormatter footer;

    /**
     * Creates the formatter.
     */
    public SummaryFormatter() {
        format = new CompactFormatter("[%4$s]\t%5$s %6$s%n");
        footer = new CollectorFormatter("\nThese {3} messages occurred between "
                + "{7,time,EEE, MMM dd HH:mm:ss:S ZZZ yyyy} and "
                + "{8,time,EEE, MMM dd HH:mm:ss:S ZZZ yyyy}\n", format, null);
    }

    /**
     * Gets the header information.
     *
     * @param h the handler or null.
     * @return the header.
     */
    @Override
    public String getHead(Handler h) {
        footer.getHead(h);
        return format.getHead(h);
    }

    /**
     * Formats the given record.
     *
     * @param record the log record.
     * @return the formatted record.
     * @throws NullPointerException if record is null.
     */
    public String format(LogRecord record) {
        String data = format.format(record);
        footer.format(record); //Track record times for footer.
        return data;
    }

    /**
     * Gets and resets the footer information.
     *
     * @param h the handler or null.
     * @return the footer.
     */
    @Override
    public String getTail(Handler h) {
        format.getTail(h);
        return footer.getTail(h);
    }
}
