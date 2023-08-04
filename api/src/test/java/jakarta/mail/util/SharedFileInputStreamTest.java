/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jakarta.mail.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.fail;

/**
 * Please note:
 * In version 2.1.2 Final Release, a difference in test results was observed based on the choice of assertion method.
 * Invoking stream.read() directly yielded distinct outcomes compared to using Assertions.assertDoesNotThrow() from JUnit 5.
 * This divergence is likely attributed to scope of the code.
 * After the patch, this issue did not reoccur. However, it remains essential to be attentive
 * If any changes are deemed necessary, please ensure a comprehensive review and thorough testing to uphold the original behavior.
 */
public class SharedFileInputStreamTest {

    @Test
    public void testChild() throws Exception {
        File file = File.createTempFile(SharedFileInputStreamTest.class.getName(), "testChild");

        try (InputStream childStream = new SharedFileInputStream(file).newStream(0, -1)) {
            System.gc();
            childStream.read();
        } catch (IOException e) {
            fail("IOException is not expected");
        } finally {
            file.delete();
        }
    }


    @Test
    public void testGrandChild() throws Exception {
        File file = File.createTempFile(SharedFileInputStreamTest.class.getName(), "testGrandChild");

        try (InputStream grandChild = ((SharedFileInputStream) new SharedFileInputStream(file).newStream(0, -1)).newStream(0, -1)) {
            System.gc();
            grandChild.read();
        } catch (IOException e) {
            fail("IOException is not expected");
        } finally {
            file.delete();
        }
    }
}