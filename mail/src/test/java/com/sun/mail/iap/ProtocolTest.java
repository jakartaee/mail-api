/*
 * Copyright (c) 2009, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.iap;

import com.sun.mail.test.NullOutputStream;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the Protocol class.
 */
public final class ProtocolTest {

    private static final byte[] noBytes = new byte[0];
    private static final PrintStream nullps =
				    new PrintStream(new NullOutputStream());
    private static final ByteArrayInputStream nullis =
				    new ByteArrayInputStream(noBytes);

    /**
     * Test that the tag prefix is computed properly.
     */
    @Test
    public void testTagPrefix() throws IOException, ProtocolException {
	Protocol.tagNum.set(0);		// reset for testing
	String tag = newProtocolTag();
	assertEquals("A0", tag);
	for (int i = 1; i < 26; i++)
	    tag = newProtocolTag();
	assertEquals("Z0", tag);
	tag = newProtocolTag();
	assertEquals("AA0", tag);
	for (int i = 26 + 1; i < (26*26 + 26); i++)
	    tag = newProtocolTag();
	assertEquals("ZZ0", tag);
	tag = newProtocolTag();
	assertEquals("AAA0", tag);
	for (int i = 26*26 + 26 + 1; i < (26*26*26 + 26*26 + 26); i++)
	    tag = newProtocolTag();
	assertEquals("ZZZ0", tag);
	tag = newProtocolTag();
	// did it wrap around?
	assertEquals("A0", tag);
    }

    private String newProtocolTag() throws IOException, ProtocolException {
	Properties props = new Properties();
	Protocol p = new Protocol(nullis, nullps, props, false);
	String tag = p.writeCommand("CMD", null);
	return tag;
    }

    /**
     * Test that the tag prefix is reused.
     */
    @Test
    public void testTagPrefixReuse() throws IOException, ProtocolException {
	Properties props = new Properties();
	props.setProperty("mail.imap.reusetagprefix", "true");
	Protocol p = new Protocol(nullis, nullps, props, false);
	String tag = p.writeCommand("CMD", null);
	assertEquals("A0", tag);
	p = new Protocol(nullis, nullps, props, false);
	tag = p.writeCommand("CMD", null);
	assertEquals("A0", tag);
    }

    @Test
    public void testLayer1Socket() throws IOException, ProtocolException {
        try (LayerAbstractSocket s = new Layer1of5()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testLayer2Socket() throws IOException, ProtocolException {
        try (LayerAbstractSocket s = new Layer2of5()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testLayer3Socket() throws IOException, ProtocolException {
        try (LayerAbstractSocket s = new Layer3of5()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testLayer4Socket() throws IOException, ProtocolException {
        try (LayerAbstractSocket s = new Layer4of5()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testLayer5Socket() throws IOException, ProtocolException {
        try (LayerAbstractSocket s = new Layer5of5()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    
    @Test
    public void testRenamed1Socket() throws IOException, ProtocolException {
        try (RenamedAbstractSocket s = new RenamedSocketLayer1of3()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testRenamed2Socket() throws IOException, ProtocolException {
        try (RenamedAbstractSocket s = new RenamedSocketLayer2of3()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testRenamed3Socket() throws IOException, ProtocolException {
        try (RenamedAbstractSocket s = new RenamedSocketLayer3of3()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testNullSocketsRenamed() throws IOException, ProtocolException {
        try (RenamedAbstractSocket s = new NullSocketsRenamedSocket()) {
            findSocketChannel(s);
            assertTrue(s.foundChannel());
        }
    }
    
    @Test
    public void testHidden1Socket() throws IOException, ProtocolException {
        try (HiddenAbstractSocket s = new HiddenSocket1of2()) {
            //This could be implemented to find the socket. 
            //However, we would have fetch field value to inspect the object.
            //Most reads will not be what we are looking for so best to give up.
            //Feel free to change the policy later if needed.
            findSocketChannel(s);
            assertFalse(s.foundChannel());
        }
    }
    
    @Test
    public void testHidden2Socket() throws IOException, ProtocolException {
        try (HiddenAbstractSocket s = new HiddenSocket2of2()) {
            //This could be implemented to find the socket. 
            //However, we would have fetch field value to inspect the object.
            //Most reads will not be what we are looking for so best to give up.
            //Feel free to change the policy later if needed.
            findSocketChannel(s);
            assertFalse(s.foundChannel());
        }
    }
    
    @Test
    public void testNamedNullAndHiddenSocket() throws IOException, ProtocolException {
        try (HiddenAbstractSocket s = new NamedNullAndHiddenSocket()) {
            findSocketChannel(s);
            assertFalse(s.foundChannel());
        }
    }
    
    private SocketChannel findSocketChannel(Socket s) throws IOException {
        try {
	    Method m = Protocol.class.getDeclaredMethod("findSocketChannel", Socket.class);
	    m.setAccessible(true);
	    return (SocketChannel) m.invoke((Object) null, s);
	} catch (RuntimeException re) {
	    throw re;
	} catch (Exception e) {
	    throw new IOException(e);
	}
    }
    
    private static class RenamedSocketLayer3of3 extends RenamedSocketLayer1of3 {
    }
    
    private static class RenamedSocketLayer2of3 extends RenamedSocketLayer1of3 {
    }
    
    private static class RenamedSocketLayer1of3 extends RenamedAbstractSocket {    
    }
    
    private static abstract class RenamedAbstractSocket extends Socket {
        private Socket tekcos = new WrappedSocket();
        
        public boolean foundChannel() {
            return WrappedSocket.foundChannel(tekcos);
        }
    }
    
    private static class NullSocketsRenamedSocket extends RenamedAbstractSocket {
        @SuppressWarnings("unused") //Reflective access
	private Socket socket;
        @SuppressWarnings("unused") //Reflective access
	private Socket tekcos;
        
    }
    
    private static class Layer5of5 extends Layer4of5 {
    }
    private static class Layer4of5 extends Layer3of5 {
    }
    private static class Layer3of5 extends Layer2of5 {
    }
    private static class Layer2of5 extends Layer1of5 {
    }
    private static class Layer1of5 extends LayerAbstractSocket {
    }
    
    private static abstract class LayerAbstractSocket extends Socket {
        private final Socket socket = new WrappedSocket();
        
        public boolean foundChannel() {
            return WrappedSocket.foundChannel(socket);
        }
    }
    
    
    private static class HiddenSocket2of2 extends HiddenSocket1of2 {
    }
    
    private static class HiddenSocket1of2 extends HiddenAbstractSocket {
    }
    
    private static abstract class HiddenAbstractSocket extends Socket {
        private final Object hidden = new WrappedSocket();
        
        public boolean foundChannel() {
            return WrappedSocket.foundChannel(hidden);
        }
    }
    
    private static class NamedNullAndHiddenSocket extends HiddenAbstractSocket {
    
        @SuppressWarnings("unused") //Reflective access
        private Socket socket;
    }
    
    private static class WrappedSocket extends Socket {
        private boolean found;
        
        public static boolean foundChannel(Object ws) {
            return ws instanceof WrappedSocket
                  && ((WrappedSocket) ws).found;
        }
        
        @Override
        public SocketChannel getChannel() {
            found = true;
            return null;
        }
    }
}
