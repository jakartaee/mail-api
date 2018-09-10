/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.imap;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.UIDFolder;
import javax.mail.MessagingException;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test EXPUNGE responses during UID FETCH.
 */
public final class IMAPUidExpungeTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    public static interface IMAPTest {
	public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException;
    }

    @Test
    public void testUIDSingle() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message m = ((UIDFolder)folder).getMessageByUID(2);
		    m.getFlags();
		    assertEquals(1, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("1 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDSingle2() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message m = ((UIDFolder)folder).getMessageByUID(2);
		    m.getFlags();
		    assertEquals(2, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDRange() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(2, 4);
		    assertTrue(msgs[1] == null || msgs[1].isExpunged());
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 FETCH (UID 4)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDRange2() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(2, 4);
		    assertTrue(msgs[1] == null || msgs[1].isExpunged());
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    untagged("3 FETCH (UID 4)");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDRange3() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(2, 4);
		    // UID 3 is unknown and not returned
		    assertEquals(2, msgs.length);
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[1].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    untagged("3 FETCH (UID 4)");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDRange4() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(1, 3);
		    assertEquals(3, msgs.length);
		    msgs[0].getFlags();
		    assertEquals(1, handler.getSeqNum());
		    msgs[1].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("1 FETCH (UID 1)");
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDRange5() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(2, 4);
		    assertEquals(3, msgs.length);
		    msgs[0].getFlags();
		    assertEquals(1, handler.getSeqNum());
		    msgs[1].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 FETCH (UID 4)");
		    untagged("1 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDList() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(
							new long[] { 2, 3, 4 });
		    assertTrue(msgs[1] == null || msgs[1].isExpunged());
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 FETCH (UID 4)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDList2() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(
							new long[] { 2, 3, 4 });
		    assertTrue(msgs[1] == null || msgs[1].isExpunged());
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    untagged("3 FETCH (UID 4)");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDList3() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(
							new long[] { 2, 3, 4 });
		    assertTrue(msgs[1] == null || msgs[1].isExpunged());
		    msgs[0].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 EXPUNGE");
		    untagged("3 EXISTS");
		    untagged("3 FETCH (UID 4)");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDList4() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(
							new long[] { 1, 2, 3 });
		    assertEquals(3, msgs.length);
		    msgs[0].getFlags();
		    assertEquals(1, handler.getSeqNum());
		    msgs[1].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("1 FETCH (UID 1)");
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    @Test
    public void testUIDList5() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerExpunge handler)
				    throws MessagingException {
		    Message[] msgs = ((UIDFolder)folder).getMessagesByUID(
							new long[] { 2, 3, 4 });
		    assertEquals(3, msgs.length);
		    msgs[0].getFlags();
		    assertEquals(1, handler.getSeqNum());
		    msgs[1].getFlags();
		    assertEquals(2, handler.getSeqNum());
		    msgs[2].getFlags();
		    assertEquals(3, handler.getSeqNum());
		}
	    },
	    new IMAPHandlerExpunge() {
		@Override
		public void uidfetch(String line) throws IOException {
		    untagged("2 FETCH (UID 2)");
		    untagged("3 FETCH (UID 3)");
		    untagged("4 FETCH (UID 4)");
		    untagged("1 EXPUNGE");
		    untagged("3 EXISTS");
		    numberOfMessages--;
		    ok();
		}
	    });
    }

    public void testWithHandler(IMAPTest test, IMAPHandlerExpunge handler) {
        TestServer server = null;
        try {
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("imap");
	    Folder folder = null;
            try {
                store.connect("test", "test");
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
		test.test(folder, handler);
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
            } finally {
		if (folder != null)
		    folder.close(false);
                store.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
            }
        }
    }

    /**
     * Custom handler.
     */
    private static class IMAPHandlerExpunge extends IMAPHandler {
	// must be static because handler is cloned for each connection
	private static int seqnum;

	@Override
        public void select(String line) throws IOException {
	    numberOfMessages = 4;
	    super.select(line);
	}

	@Override
        public void fetch(String line) throws IOException {
	    StringTokenizer st = new StringTokenizer(line, " ");
	    String tag = st.nextToken();
	    String command = st.nextToken();
	    seqnum = Integer.parseInt(st.nextToken());
	    ok();
        }

	public int getSeqNum() {
	    return seqnum;
	}
    }
}
