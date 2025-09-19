/*
 * Copyright (c) 1997, 2025 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import jakarta.mail.util.LineInputStream;
import jakarta.mail.util.LineOutputStream;
import jakarta.mail.util.StreamProvider;

/**
 * This class models a Part that is contained within a Multipart.
 * This is an abstract class. Subclasses provide actual implementations.<p>
 *
 * BodyPart implements the Part interface. Thus, it contains a set of
 * attributes and a "content".
 *
 * @author John Mani
 * @author Bill Shannon
 */

public abstract class BodyPart implements Part {

    /**
     * The <code>Multipart</code> object containing this <code>BodyPart</code>,
     * if known.
     *
     * @since JavaMail 1.1
     */
    protected Multipart parent;

    /**
     * Instance of stream provider.
     *
     * @deprecated This field will be removed in a future release.
     *             Use {@link #getStreamProvider()} instead.
     *
     * @since JavaMail 2.1
     */
    @Deprecated(forRemoval = true, since = "2.2.0")
    protected final StreamProvider streamProvider = new DeprecatedStreamProviderWrapper(this.getClass(), StreamProvider.provider());

    /**
     * Creates a default {@code BodyPart}.
     */
    public BodyPart() {
    }

    /**
     * Return the containing <code>Multipart</code> object,
     * or <code>null</code> if not known.
     *
     * @return the parent Multipart
     */
    public Multipart getParent() {
        return parent;
    }

    /**
     * Set the parent of this <code>BodyPart</code> to be the specified
     * <code>Multipart</code>.  Normally called by <code>Multipart</code>'s
     * <code>addBodyPart</code> method.  <code>parent</code> may be
     * <code>null</code> if the <code>BodyPart</code> is being removed
     * from its containing <code>Multipart</code>.
     *
     * @since JavaMail 1.1
     */
    void setParent(Multipart parent) {
        this.parent = parent;
    }

    @Override
    public StreamProvider getStreamProvider() throws MessagingException {
        if (parent != null) {
            return parent.getStreamProvider();
        } else {
            return Part.super.getStreamProvider();
        }
    }

    /**
     * Wrapper around StreamProvider that logs a deprecation warning
     * whenever its methods are used.
     */
    @Deprecated
    static final class DeprecatedStreamProviderWrapper implements StreamProvider {

        private static final Logger LOGGER = Logger.getLogger(DeprecatedStreamProviderWrapper.class.getName());
        private final StreamProvider delegate;
        private final Class<?> owner;

        public DeprecatedStreamProviderWrapper(Class<?> owner, StreamProvider delegate) {
            this.owner = owner;
            this.delegate = delegate;
        }

        private void warn() {
            LOGGER.warning(
                    "[DEPRECATED] Accessing deprecated field 'streamProvider'. " +
                    "Use " + owner.getName() + "#getStreamProvider() instead."
                );
        }

        @Override
        public InputStream inputBase64(InputStream in) {
            warn();
            return delegate.inputBase64(in);
        }

        @Override
        public OutputStream outputBase64(OutputStream out) {
            warn();
            return delegate.outputBase64(out);
        }

        @Override
        public InputStream inputBinary(InputStream in) {
            warn();
            return delegate.inputBinary(in);
        }

        @Override
        public OutputStream outputBinary(OutputStream out) {
            warn();
            return delegate.outputBinary(out);
        }

        @Override
        public OutputStream outputB(OutputStream out) {
            warn();
            return delegate.outputB(out);
        }

        @Override
        public InputStream inputQ(InputStream in) {
            warn();
            return delegate.inputQ(in);
        }

        @Override
        public OutputStream outputQ(OutputStream out, boolean encodingWord) {
            warn();
            return delegate.outputQ(out, encodingWord);
        }

        @Override
        public LineInputStream inputLineStream(InputStream in, boolean allowutf8) {
            warn();
            return delegate.inputLineStream(in, allowutf8);
        }

        @Override
        public LineOutputStream outputLineStream(OutputStream out, boolean allowutf8) {
            warn();
            return delegate.outputLineStream(out, allowutf8);
        }

        @Override
        public InputStream inputQP(InputStream in) {
            warn();
            return delegate.inputQP(in);
        }

        @Override
        public OutputStream outputQP(OutputStream out) {
            warn();
            return delegate.outputQP(out);
        }

        @Override
        public InputStream inputSharedByteArray(byte[] buff) {
            warn();
            return delegate.inputSharedByteArray(buff);
        }

        @Override
        public InputStream inputUU(InputStream in) {
            warn();
            return delegate.inputUU(in);
        }

        @Override
        public OutputStream outputUU(OutputStream out, String filename) {
            warn();
            return delegate.outputUU(out, filename);
        }

    }
}
