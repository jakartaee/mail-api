/*
 * Copyright (c) 2021, 2024 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import jakarta.mail.util.SharedFileInputStream.SharedFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

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

    @Test
    public void testCloseMultipleTimes() throws Exception {
        File file = new File(SharedFileInputStreamTest.class.getResource("/jakarta/mail/util/sharedinputstream.txt").toURI());
        SharedFileInputStream in = new SharedFileInputStream(file);
        in.close();
        in.close();
    }

    @Test
    public void testOpenIfOneOpened() throws Exception {
        File file = new File(SharedFileInputStreamTest.class.getResource("/jakarta/mail/util/sharedinputstream.txt").toURI());
        SharedFileInputStream in0 = null;
        SharedFileInputStream in1 = null;
        try (SharedFileInputStream in = new SharedFileInputStream(file)) {
            in0 = (SharedFileInputStream) in.newStream(0, 6);
            in1 = (SharedFileInputStream) in.newStream(6, 12);
        }
        RandomAccessFile ra0 = getRandomAccessFile(in0);
        RandomAccessFile ra1 = getRandomAccessFile(in1);
        // It is the same instance
        assertEquals(ra0, ra1);
        // RandomAccessFile still be open
        in1.close();
        assertEquals(false, isClosed(ra1));
        in0.close();
        // All SharedFileInputStream are closed, so RandomAccessFile gets closed too
        assertEquals(true, isClosed(ra1));
    }

    @Test
    public void testGC() throws Exception {
        File file = new File(SharedFileInputStreamTest.class.getResource("/jakarta/mail/util/sharedinputstream.txt").toURI());
        SharedFileInputStream in = new SharedFileInputStream(file);
        GCUtil gcUtil = new GCUtil(in);
        SharedFileInputStream in0 = (SharedFileInputStream) in.newStream(0, 6);
        in.close();
        in = null;
        gcUtil.waitTillGCed(1000);
        gcUtil = new GCUtil(in0);
        SharedFileInputStream in1 = (SharedFileInputStream) in0.newStream(6, 12);
        assertEquals("test0\n", new String(in0.readAllBytes()));
        in0.close();
        in0 = null;
        gcUtil.waitTillGCed(1000);
        assertEquals("test1\n", new String(in1.readAllBytes()));
        in1.close();
    }

    private RandomAccessFile getRandomAccessFile(SharedFileInputStream in) throws Exception {
        Field f1 = SharedFileInputStream.class.getDeclaredField("sf");
        f1.setAccessible(true);
        SharedFile rin = (SharedFile) f1.get(in);
        RandomAccessFile rf = rin.in;
        return rf;
    }
    
    private boolean isClosed(RandomAccessFile rf) throws Exception {
        try {
            rf.readByte();
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    private static class GCUtil {

        private final ReferenceQueue<Object> rq = new ReferenceQueue<>();
        private final PhantomReference<Object> phantomRef;

        private GCUtil(Object ref) {
            phantomRef = new PhantomReference<>(ref, rq);
        }

        private void waitTillGCed(long timeout) throws Exception {
            Reference<? extends Object> gced;
            long time = 0;
            long sleep = 100;
            while ((gced = rq.poll()) != phantomRef) {
                Thread.sleep(sleep);
                time = time + sleep;
                if (time >= timeout) {
                    throw new TimeoutException("Instance not GCed after " + timeout + " millis");
                }
                System.gc();
            }
        }
    }
}