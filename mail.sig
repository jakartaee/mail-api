#Signature file v4.3
#Version 

CLSS public abstract jakarta.mail.Address
cons public <init>()
intf java.io.Serializable
meth public abstract boolean equals(java.lang.Object)
meth public abstract java.lang.String getType()
meth public abstract java.lang.String toString()
supr java.lang.Object
hfds serialVersionUID

CLSS public jakarta.mail.AuthenticationFailedException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract jakarta.mail.Authenticator
cons public <init>()
meth protected final int getRequestingPort()
meth protected final java.lang.String getDefaultUserName()
meth protected final java.lang.String getRequestingPrompt()
meth protected final java.lang.String getRequestingProtocol()
meth protected final java.net.InetAddress getRequestingSite()
meth protected jakarta.mail.PasswordAuthentication getPasswordAuthentication()
supr java.lang.Object
hfds requestingPort,requestingPrompt,requestingProtocol,requestingSite,requestingUserName

CLSS public abstract jakarta.mail.BodyPart
cons public <init>()
fld protected jakarta.mail.Multipart parent
intf jakarta.mail.Part
meth public jakarta.mail.Multipart getParent()
supr java.lang.Object

CLSS public abstract interface jakarta.mail.EncodingAware
meth public abstract java.lang.String getEncoding()

CLSS public jakarta.mail.FetchProfile
cons public <init>()
innr public static Item
meth public boolean contains(java.lang.String)
meth public boolean contains(jakarta.mail.FetchProfile$Item)
meth public java.lang.String[] getHeaderNames()
meth public jakarta.mail.FetchProfile$Item[] getItems()
meth public void add(java.lang.String)
meth public void add(jakarta.mail.FetchProfile$Item)
supr java.lang.Object
hfds headers,specials

CLSS public static jakarta.mail.FetchProfile$Item
 outer jakarta.mail.FetchProfile
cons protected <init>(java.lang.String)
fld public final static jakarta.mail.FetchProfile$Item CONTENT_INFO
fld public final static jakarta.mail.FetchProfile$Item ENVELOPE
fld public final static jakarta.mail.FetchProfile$Item FLAGS
fld public final static jakarta.mail.FetchProfile$Item SIZE
meth public java.lang.String toString()
supr java.lang.Object
hfds name

CLSS public jakarta.mail.Flags
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(jakarta.mail.Flags$Flag)
cons public <init>(jakarta.mail.Flags)
innr public final static Flag
intf java.io.Serializable
intf java.lang.Cloneable
meth public boolean contains(java.lang.String)
meth public boolean contains(jakarta.mail.Flags$Flag)
meth public boolean contains(jakarta.mail.Flags)
meth public boolean equals(java.lang.Object)
meth public boolean retainAll(jakarta.mail.Flags)
meth public int hashCode()
meth public java.lang.Object clone()
meth public java.lang.String toString()
meth public java.lang.String[] getUserFlags()
meth public jakarta.mail.Flags$Flag[] getSystemFlags()
meth public void add(java.lang.String)
meth public void add(jakarta.mail.Flags$Flag)
meth public void add(jakarta.mail.Flags)
meth public void clearSystemFlags()
meth public void clearUserFlags()
meth public void remove(java.lang.String)
meth public void remove(jakarta.mail.Flags$Flag)
meth public void remove(jakarta.mail.Flags)
supr java.lang.Object
hfds ANSWERED_BIT,DELETED_BIT,DRAFT_BIT,FLAGGED_BIT,RECENT_BIT,SEEN_BIT,USER_BIT,serialVersionUID,system_flags,user_flags

CLSS public final static jakarta.mail.Flags$Flag
 outer jakarta.mail.Flags
fld public final static jakarta.mail.Flags$Flag ANSWERED
fld public final static jakarta.mail.Flags$Flag DELETED
fld public final static jakarta.mail.Flags$Flag DRAFT
fld public final static jakarta.mail.Flags$Flag FLAGGED
fld public final static jakarta.mail.Flags$Flag RECENT
fld public final static jakarta.mail.Flags$Flag SEEN
fld public final static jakarta.mail.Flags$Flag USER
supr java.lang.Object
hfds bit

CLSS public abstract jakarta.mail.Folder
cons protected <init>(jakarta.mail.Store)
fld protected int mode
fld protected jakarta.mail.Store store
fld public final static int HOLDS_FOLDERS = 2
fld public final static int HOLDS_MESSAGES = 1
fld public final static int READ_ONLY = 1
fld public final static int READ_WRITE = 2
intf java.lang.AutoCloseable
meth protected void finalize() throws java.lang.Throwable
meth protected void notifyConnectionListeners(int)
meth protected void notifyFolderListeners(int)
meth protected void notifyFolderRenamedListeners(jakarta.mail.Folder)
meth protected void notifyMessageAddedListeners(jakarta.mail.Message[])
meth protected void notifyMessageChangedListeners(int,jakarta.mail.Message)
meth protected void notifyMessageRemovedListeners(boolean,jakarta.mail.Message[])
meth public abstract boolean create(int) throws jakarta.mail.MessagingException
meth public abstract boolean delete(boolean) throws jakarta.mail.MessagingException
meth public abstract boolean exists() throws jakarta.mail.MessagingException
meth public abstract boolean hasNewMessages() throws jakarta.mail.MessagingException
meth public abstract boolean isOpen()
meth public abstract boolean renameTo(jakarta.mail.Folder) throws jakarta.mail.MessagingException
meth public abstract char getSeparator() throws jakarta.mail.MessagingException
meth public abstract int getMessageCount() throws jakarta.mail.MessagingException
meth public abstract int getType() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getFullName()
meth public abstract java.lang.String getName()
meth public abstract jakarta.mail.Flags getPermanentFlags()
meth public abstract jakarta.mail.Folder getFolder(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Folder getParent() throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Folder[] list(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Message getMessage(int) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Message[] expunge() throws jakarta.mail.MessagingException
meth public abstract void appendMessages(jakarta.mail.Message[]) throws jakarta.mail.MessagingException
meth public abstract void close(boolean) throws jakarta.mail.MessagingException
meth public abstract void open(int) throws jakarta.mail.MessagingException
meth public boolean isSubscribed()
meth public int getDeletedMessageCount() throws jakarta.mail.MessagingException
meth public int getMode()
meth public int getNewMessageCount() throws jakarta.mail.MessagingException
meth public int getUnreadMessageCount() throws jakarta.mail.MessagingException
meth public java.lang.String toString()
meth public jakarta.mail.Folder[] list() throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder[] listSubscribed() throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder[] listSubscribed(java.lang.String) throws jakarta.mail.MessagingException
meth public jakarta.mail.Message[] getMessages() throws jakarta.mail.MessagingException
meth public jakarta.mail.Message[] getMessages(int,int) throws jakarta.mail.MessagingException
meth public jakarta.mail.Message[] getMessages(int[]) throws jakarta.mail.MessagingException
meth public jakarta.mail.Message[] search(jakarta.mail.search.SearchTerm) throws jakarta.mail.MessagingException
meth public jakarta.mail.Message[] search(jakarta.mail.search.SearchTerm,jakarta.mail.Message[]) throws jakarta.mail.MessagingException
meth public jakarta.mail.Store getStore()
meth public jakarta.mail.URLName getURLName() throws jakarta.mail.MessagingException
meth public void addConnectionListener(jakarta.mail.event.ConnectionListener)
meth public void addFolderListener(jakarta.mail.event.FolderListener)
meth public void addMessageChangedListener(jakarta.mail.event.MessageChangedListener)
meth public void addMessageCountListener(jakarta.mail.event.MessageCountListener)
meth public void close() throws jakarta.mail.MessagingException
meth public void copyMessages(jakarta.mail.Message[],jakarta.mail.Folder) throws jakarta.mail.MessagingException
meth public void fetch(jakarta.mail.Message[],jakarta.mail.FetchProfile) throws jakarta.mail.MessagingException
meth public void removeConnectionListener(jakarta.mail.event.ConnectionListener)
meth public void removeFolderListener(jakarta.mail.event.FolderListener)
meth public void removeMessageChangedListener(jakarta.mail.event.MessageChangedListener)
meth public void removeMessageCountListener(jakarta.mail.event.MessageCountListener)
meth public void setFlags(int,int,jakarta.mail.Flags,boolean) throws jakarta.mail.MessagingException
meth public void setFlags(int[],jakarta.mail.Flags,boolean) throws jakarta.mail.MessagingException
meth public void setFlags(jakarta.mail.Message[],jakarta.mail.Flags,boolean) throws jakarta.mail.MessagingException
meth public void setSubscribed(boolean) throws jakarta.mail.MessagingException
supr java.lang.Object
hfds connectionListeners,folderListeners,messageChangedListeners,messageCountListeners,q

CLSS public jakarta.mail.FolderClosedException
cons public <init>(jakarta.mail.Folder)
cons public <init>(jakarta.mail.Folder,java.lang.String)
cons public <init>(jakarta.mail.Folder,java.lang.String,java.lang.Exception)
meth public jakarta.mail.Folder getFolder()
supr jakarta.mail.MessagingException
hfds folder,serialVersionUID

CLSS public jakarta.mail.FolderNotFoundException
cons public <init>()
cons public <init>(java.lang.String,jakarta.mail.Folder)
cons public <init>(jakarta.mail.Folder)
cons public <init>(jakarta.mail.Folder,java.lang.String)
cons public <init>(jakarta.mail.Folder,java.lang.String,java.lang.Exception)
meth public jakarta.mail.Folder getFolder()
supr jakarta.mail.MessagingException
hfds folder,serialVersionUID

CLSS public jakarta.mail.Header
cons public <init>(java.lang.String,java.lang.String)
fld protected java.lang.String name
fld protected java.lang.String value
meth public java.lang.String getName()
meth public java.lang.String getValue()
supr java.lang.Object

CLSS public jakarta.mail.IllegalWriteException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract interface !annotation jakarta.mail.MailSessionDefinition
 anno 0 java.lang.annotation.Repeatable(java.lang.Class<? extends java.lang.annotation.Annotation> value=class jakarta.mail.MailSessionDefinitions)
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String description() value= ""
meth public abstract !hasdefault java.lang.String from() value= ""
meth public abstract !hasdefault java.lang.String host() value= ""
meth public abstract !hasdefault java.lang.String password() value= ""
meth public abstract !hasdefault java.lang.String storeProtocol() value= ""
meth public abstract !hasdefault java.lang.String transportProtocol() value= ""
meth public abstract !hasdefault java.lang.String user() value= ""
meth public abstract !hasdefault java.lang.String[] properties() value= []
meth public abstract java.lang.String name()

CLSS public abstract interface !annotation jakarta.mail.MailSessionDefinitions
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation
meth public abstract jakarta.mail.MailSessionDefinition[] value()

CLSS public abstract jakarta.mail.Message
cons protected <init>()
cons protected <init>(jakarta.mail.Folder,int)
cons protected <init>(jakarta.mail.Session)
fld protected boolean expunged
fld protected int msgnum
fld protected jakarta.mail.Folder folder
fld protected jakarta.mail.Session session
innr public static RecipientType
intf jakarta.mail.Part
meth protected void setExpunged(boolean)
meth protected void setMessageNumber(int)
meth public abstract java.lang.String getSubject() throws jakarta.mail.MessagingException
meth public abstract java.util.Date getReceivedDate() throws jakarta.mail.MessagingException
meth public abstract java.util.Date getSentDate() throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Address[] getFrom() throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Address[] getRecipients(jakarta.mail.Message$RecipientType) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Flags getFlags() throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Message reply(boolean) throws jakarta.mail.MessagingException
meth public abstract void addFrom(jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public abstract void addRecipients(jakarta.mail.Message$RecipientType,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public abstract void saveChanges() throws jakarta.mail.MessagingException
meth public abstract void setFlags(jakarta.mail.Flags,boolean) throws jakarta.mail.MessagingException
meth public abstract void setFrom() throws jakarta.mail.MessagingException
meth public abstract void setFrom(jakarta.mail.Address) throws jakarta.mail.MessagingException
meth public abstract void setRecipients(jakarta.mail.Message$RecipientType,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public abstract void setSentDate(java.util.Date) throws jakarta.mail.MessagingException
meth public abstract void setSubject(java.lang.String) throws jakarta.mail.MessagingException
meth public boolean isExpunged()
meth public boolean isSet(jakarta.mail.Flags$Flag) throws jakarta.mail.MessagingException
meth public boolean match(jakarta.mail.search.SearchTerm) throws jakarta.mail.MessagingException
meth public int getMessageNumber()
meth public jakarta.mail.Address[] getAllRecipients() throws jakarta.mail.MessagingException
meth public jakarta.mail.Address[] getReplyTo() throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder getFolder()
meth public jakarta.mail.Session getSession()
meth public void addRecipient(jakarta.mail.Message$RecipientType,jakarta.mail.Address) throws jakarta.mail.MessagingException
meth public void setFlag(jakarta.mail.Flags$Flag,boolean) throws jakarta.mail.MessagingException
meth public void setRecipient(jakarta.mail.Message$RecipientType,jakarta.mail.Address) throws jakarta.mail.MessagingException
meth public void setReplyTo(jakarta.mail.Address[]) throws jakarta.mail.MessagingException
supr java.lang.Object

CLSS public static jakarta.mail.Message$RecipientType
 outer jakarta.mail.Message
cons protected <init>(java.lang.String)
fld protected java.lang.String type
fld public final static jakarta.mail.Message$RecipientType BCC
fld public final static jakarta.mail.Message$RecipientType CC
fld public final static jakarta.mail.Message$RecipientType TO
intf java.io.Serializable
meth protected java.lang.Object readResolve() throws java.io.ObjectStreamException
meth public java.lang.String toString()
supr java.lang.Object
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.MessageAware
meth public abstract jakarta.mail.MessageContext getMessageContext()

CLSS public jakarta.mail.MessageContext
cons public <init>(jakarta.mail.Part)
meth public jakarta.mail.Message getMessage()
meth public jakarta.mail.Part getPart()
meth public jakarta.mail.Session getSession()
supr java.lang.Object
hfds part

CLSS public jakarta.mail.MessageRemovedException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public jakarta.mail.MessagingException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
meth public boolean setNextException(java.lang.Exception)
meth public java.lang.Exception getNextException()
meth public java.lang.String toString()
meth public java.lang.Throwable getCause()
supr java.lang.Exception
hfds next,serialVersionUID

CLSS public jakarta.mail.MethodNotSupportedException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract jakarta.mail.Multipart
cons protected <init>()
fld protected java.lang.String contentType
fld protected java.util.Vector<jakarta.mail.BodyPart> parts
fld protected jakarta.mail.Part parent
meth protected void setMultipartDataSource(jakarta.mail.MultipartDataSource) throws jakarta.mail.MessagingException
meth public abstract void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException
meth public boolean removeBodyPart(jakarta.mail.BodyPart) throws jakarta.mail.MessagingException
meth public int getCount() throws jakarta.mail.MessagingException
meth public java.lang.String getContentType()
meth public jakarta.mail.BodyPart getBodyPart(int) throws jakarta.mail.MessagingException
meth public jakarta.mail.Part getParent()
meth public void addBodyPart(jakarta.mail.BodyPart) throws jakarta.mail.MessagingException
meth public void addBodyPart(jakarta.mail.BodyPart,int) throws jakarta.mail.MessagingException
meth public void removeBodyPart(int) throws jakarta.mail.MessagingException
meth public void setParent(jakarta.mail.Part)
supr java.lang.Object

CLSS public abstract interface jakarta.mail.MultipartDataSource
intf jakarta.activation.DataSource
meth public abstract int getCount()
meth public abstract jakarta.mail.BodyPart getBodyPart(int) throws jakarta.mail.MessagingException

CLSS public jakarta.mail.NoSuchProviderException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.Part
fld public final static java.lang.String ATTACHMENT = "attachment"
fld public final static java.lang.String INLINE = "inline"
meth public abstract boolean isMimeType(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract int getLineCount() throws jakarta.mail.MessagingException
meth public abstract int getSize() throws jakarta.mail.MessagingException
meth public abstract java.io.InputStream getInputStream() throws java.io.IOException,jakarta.mail.MessagingException
meth public abstract java.lang.Object getContent() throws java.io.IOException,jakarta.mail.MessagingException
meth public abstract java.lang.String getContentType() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getDescription() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getDisposition() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getFileName() throws jakarta.mail.MessagingException
meth public abstract java.lang.String[] getHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<jakarta.mail.Header> getAllHeaders() throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<jakarta.mail.Header> getMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<jakarta.mail.Header> getNonMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public abstract jakarta.activation.DataHandler getDataHandler() throws jakarta.mail.MessagingException
meth public abstract void addHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void removeHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setContent(java.lang.Object,java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setContent(jakarta.mail.Multipart) throws jakarta.mail.MessagingException
meth public abstract void setDataHandler(jakarta.activation.DataHandler) throws jakarta.mail.MessagingException
meth public abstract void setDescription(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setDisposition(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setFileName(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setText(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException

CLSS public final jakarta.mail.PasswordAuthentication
cons public <init>(java.lang.String,java.lang.String)
meth public java.lang.String getPassword()
meth public java.lang.String getUserName()
supr java.lang.Object
hfds password,userName

CLSS public jakarta.mail.Provider
cons public <init>(jakarta.mail.Provider$Type,java.lang.String,java.lang.String,java.lang.String,java.lang.String)
innr public static Type
meth public java.lang.String getClassName()
meth public java.lang.String getProtocol()
meth public java.lang.String getVendor()
meth public java.lang.String getVersion()
meth public java.lang.String toString()
meth public jakarta.mail.Provider$Type getType()
supr java.lang.Object
hfds className,protocol,type,vendor,version

CLSS public static jakarta.mail.Provider$Type
 outer jakarta.mail.Provider
fld public final static jakarta.mail.Provider$Type STORE
fld public final static jakarta.mail.Provider$Type TRANSPORT
meth public java.lang.String toString()
supr java.lang.Object
hfds type

CLSS public jakarta.mail.Quota
cons public <init>(java.lang.String)
fld public java.lang.String quotaRoot
fld public jakarta.mail.Quota$Resource[] resources
innr public static Resource
meth public void setResourceLimit(java.lang.String,long)
supr java.lang.Object

CLSS public static jakarta.mail.Quota$Resource
 outer jakarta.mail.Quota
cons public <init>(java.lang.String,long,long)
fld public java.lang.String name
fld public long limit
fld public long usage
supr java.lang.Object

CLSS public abstract interface jakarta.mail.QuotaAwareStore
meth public abstract jakarta.mail.Quota[] getQuota(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setQuota(jakarta.mail.Quota) throws jakarta.mail.MessagingException

CLSS public jakarta.mail.ReadOnlyFolderException
cons public <init>(jakarta.mail.Folder)
cons public <init>(jakarta.mail.Folder,java.lang.String)
cons public <init>(jakarta.mail.Folder,java.lang.String,java.lang.Exception)
meth public jakarta.mail.Folder getFolder()
supr jakarta.mail.MessagingException
hfds folder,serialVersionUID

CLSS public jakarta.mail.SendFailedException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.Exception)
cons public <init>(java.lang.String,java.lang.Exception,jakarta.mail.Address[],jakarta.mail.Address[],jakarta.mail.Address[])
fld protected jakarta.mail.Address[] invalid
fld protected jakarta.mail.Address[] validSent
fld protected jakarta.mail.Address[] validUnsent
meth public jakarta.mail.Address[] getInvalidAddresses()
meth public jakarta.mail.Address[] getValidSentAddresses()
meth public jakarta.mail.Address[] getValidUnsentAddresses()
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract jakarta.mail.Service
cons protected <init>(jakarta.mail.Session,jakarta.mail.URLName)
fld protected boolean debug
fld protected jakarta.mail.Session session
fld protected volatile jakarta.mail.URLName url
intf java.lang.AutoCloseable
meth protected boolean protocolConnect(java.lang.String,int,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth protected void finalize() throws java.lang.Throwable
meth protected void notifyConnectionListeners(int)
meth protected void queueEvent(jakarta.mail.event.MailEvent,java.util.Vector<? extends java.util.EventListener>)
meth protected void setConnected(boolean)
meth protected void setURLName(jakarta.mail.URLName)
meth public boolean isConnected()
meth public java.lang.String toString()
meth public jakarta.mail.URLName getURLName()
meth public void addConnectionListener(jakarta.mail.event.ConnectionListener)
meth public void close() throws jakarta.mail.MessagingException
meth public void connect() throws jakarta.mail.MessagingException
meth public void connect(java.lang.String,int,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void connect(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void connect(java.lang.String,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void removeConnectionListener(jakarta.mail.event.ConnectionListener)
supr java.lang.Object
hfds connected,connectionListeners,q

CLSS public final jakarta.mail.Session
meth public boolean getDebug()
meth public java.io.PrintStream getDebugOut()
meth public java.lang.String getProperty(java.lang.String)
meth public java.util.Properties getProperties()
meth public jakarta.mail.Folder getFolder(jakarta.mail.URLName) throws jakarta.mail.MessagingException
meth public jakarta.mail.PasswordAuthentication getPasswordAuthentication(jakarta.mail.URLName)
meth public jakarta.mail.PasswordAuthentication requestPasswordAuthentication(java.net.InetAddress,int,java.lang.String,java.lang.String,java.lang.String)
meth public jakarta.mail.Provider getProvider(java.lang.String) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Provider[] getProviders()
meth public jakarta.mail.Store getStore() throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Store getStore(java.lang.String) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Store getStore(jakarta.mail.Provider) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Store getStore(jakarta.mail.URLName) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Transport getTransport() throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Transport getTransport(java.lang.String) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Transport getTransport(jakarta.mail.Address) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Transport getTransport(jakarta.mail.Provider) throws jakarta.mail.NoSuchProviderException
meth public jakarta.mail.Transport getTransport(jakarta.mail.URLName) throws jakarta.mail.NoSuchProviderException
meth public static jakarta.mail.Session getDefaultInstance(java.util.Properties)
meth public static jakarta.mail.Session getDefaultInstance(java.util.Properties,jakarta.mail.Authenticator)
meth public static jakarta.mail.Session getInstance(java.util.Properties)
meth public static jakarta.mail.Session getInstance(java.util.Properties,jakarta.mail.Authenticator)
meth public void addProvider(jakarta.mail.Provider)
meth public void setDebug(boolean)
meth public void setDebugOut(java.io.PrintStream)
meth public void setPasswordAuthentication(jakarta.mail.URLName,jakarta.mail.PasswordAuthentication)
meth public void setProtocolForAddress(java.lang.String,java.lang.String)
meth public void setProvider(jakarta.mail.Provider) throws jakarta.mail.NoSuchProviderException
supr java.lang.Object
hfds addressMap,authTable,authenticator,confDir,debug,defaultSession,logger,out,props,providers,providersByClassName,providersByProtocol,q

CLSS public abstract jakarta.mail.Store
cons protected <init>(jakarta.mail.Session,jakarta.mail.URLName)
meth protected void notifyFolderListeners(int,jakarta.mail.Folder)
meth protected void notifyFolderRenamedListeners(jakarta.mail.Folder,jakarta.mail.Folder)
meth protected void notifyStoreListeners(int,java.lang.String)
meth public abstract jakarta.mail.Folder getDefaultFolder() throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Folder getFolder(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Folder getFolder(jakarta.mail.URLName) throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder[] getPersonalNamespaces() throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder[] getSharedNamespaces() throws jakarta.mail.MessagingException
meth public jakarta.mail.Folder[] getUserNamespaces(java.lang.String) throws jakarta.mail.MessagingException
meth public void addFolderListener(jakarta.mail.event.FolderListener)
meth public void addStoreListener(jakarta.mail.event.StoreListener)
meth public void removeFolderListener(jakarta.mail.event.FolderListener)
meth public void removeStoreListener(jakarta.mail.event.StoreListener)
supr jakarta.mail.Service
hfds folderListeners,storeListeners

CLSS public jakarta.mail.StoreClosedException
cons public <init>(jakarta.mail.Store)
cons public <init>(jakarta.mail.Store,java.lang.String)
cons public <init>(jakarta.mail.Store,java.lang.String,java.lang.Exception)
meth public jakarta.mail.Store getStore()
supr jakarta.mail.MessagingException
hfds serialVersionUID,store

CLSS public abstract jakarta.mail.Transport
cons public <init>(jakarta.mail.Session,jakarta.mail.URLName)
meth protected void notifyTransportListeners(int,jakarta.mail.Address[],jakarta.mail.Address[],jakarta.mail.Address[],jakarta.mail.Message)
meth public abstract void sendMessage(jakarta.mail.Message,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public static void send(jakarta.mail.Message) throws jakarta.mail.MessagingException
meth public static void send(jakarta.mail.Message,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public static void send(jakarta.mail.Message,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public static void send(jakarta.mail.Message,jakarta.mail.Address[],java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void addTransportListener(jakarta.mail.event.TransportListener)
meth public void removeTransportListener(jakarta.mail.event.TransportListener)
supr jakarta.mail.Service
hfds transportListeners

CLSS public abstract interface jakarta.mail.UIDFolder
fld public final static long LASTUID = -1
fld public final static long MAXUID = 4294967295
innr public static FetchProfileItem
meth public abstract jakarta.mail.Message getMessageByUID(long) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Message[] getMessagesByUID(long,long) throws jakarta.mail.MessagingException
meth public abstract jakarta.mail.Message[] getMessagesByUID(long[]) throws jakarta.mail.MessagingException
meth public abstract long getUID(jakarta.mail.Message) throws jakarta.mail.MessagingException
meth public abstract long getUIDNext() throws jakarta.mail.MessagingException
meth public abstract long getUIDValidity() throws jakarta.mail.MessagingException

CLSS public static jakarta.mail.UIDFolder$FetchProfileItem
 outer jakarta.mail.UIDFolder
cons protected <init>(java.lang.String)
fld public final static jakarta.mail.UIDFolder$FetchProfileItem UID
supr jakarta.mail.FetchProfile$Item

CLSS public jakarta.mail.URLName
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.String,int,java.lang.String,java.lang.String,java.lang.String)
cons public <init>(java.net.URL)
fld protected java.lang.String fullURL
meth protected void parseString(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public int getPort()
meth public int hashCode()
meth public java.lang.String getFile()
meth public java.lang.String getHost()
meth public java.lang.String getPassword()
meth public java.lang.String getProtocol()
meth public java.lang.String getRef()
meth public java.lang.String getUsername()
meth public java.lang.String toString()
meth public java.net.URL getURL() throws java.net.MalformedURLException
supr java.lang.Object
hfds caseDiff,doEncode,dontNeedEncoding,file,hashCode,host,hostAddress,hostAddressKnown,password,port,protocol,ref,username

CLSS public abstract jakarta.mail.event.ConnectionAdapter
cons public <init>()
intf jakarta.mail.event.ConnectionListener
meth public void closed(jakarta.mail.event.ConnectionEvent)
meth public void disconnected(jakarta.mail.event.ConnectionEvent)
meth public void opened(jakarta.mail.event.ConnectionEvent)
supr java.lang.Object

CLSS public jakarta.mail.event.ConnectionEvent
cons public <init>(java.lang.Object,int)
fld protected int type
fld public final static int CLOSED = 3
fld public final static int DISCONNECTED = 2
fld public final static int OPENED = 1
meth public int getType()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.ConnectionListener
intf java.util.EventListener
meth public abstract void closed(jakarta.mail.event.ConnectionEvent)
meth public abstract void disconnected(jakarta.mail.event.ConnectionEvent)
meth public abstract void opened(jakarta.mail.event.ConnectionEvent)

CLSS public abstract jakarta.mail.event.FolderAdapter
cons public <init>()
intf jakarta.mail.event.FolderListener
meth public void folderCreated(jakarta.mail.event.FolderEvent)
meth public void folderDeleted(jakarta.mail.event.FolderEvent)
meth public void folderRenamed(jakarta.mail.event.FolderEvent)
supr java.lang.Object

CLSS public jakarta.mail.event.FolderEvent
cons public <init>(java.lang.Object,jakarta.mail.Folder,int)
cons public <init>(java.lang.Object,jakarta.mail.Folder,jakarta.mail.Folder,int)
fld protected int type
fld protected jakarta.mail.Folder folder
fld protected jakarta.mail.Folder newFolder
fld public final static int CREATED = 1
fld public final static int DELETED = 2
fld public final static int RENAMED = 3
meth public int getType()
meth public jakarta.mail.Folder getFolder()
meth public jakarta.mail.Folder getNewFolder()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.FolderListener
intf java.util.EventListener
meth public abstract void folderCreated(jakarta.mail.event.FolderEvent)
meth public abstract void folderDeleted(jakarta.mail.event.FolderEvent)
meth public abstract void folderRenamed(jakarta.mail.event.FolderEvent)

CLSS public abstract jakarta.mail.event.MailEvent
cons public <init>(java.lang.Object)
meth public abstract void dispatch(java.lang.Object)
supr java.util.EventObject
hfds serialVersionUID

CLSS public jakarta.mail.event.MessageChangedEvent
cons public <init>(java.lang.Object,int,jakarta.mail.Message)
fld protected int type
fld protected jakarta.mail.Message msg
fld public final static int ENVELOPE_CHANGED = 2
fld public final static int FLAGS_CHANGED = 1
meth public int getMessageChangeType()
meth public jakarta.mail.Message getMessage()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.MessageChangedListener
intf java.util.EventListener
meth public abstract void messageChanged(jakarta.mail.event.MessageChangedEvent)

CLSS public abstract jakarta.mail.event.MessageCountAdapter
cons public <init>()
intf jakarta.mail.event.MessageCountListener
meth public void messagesAdded(jakarta.mail.event.MessageCountEvent)
meth public void messagesRemoved(jakarta.mail.event.MessageCountEvent)
supr java.lang.Object

CLSS public jakarta.mail.event.MessageCountEvent
cons public <init>(jakarta.mail.Folder,int,boolean,jakarta.mail.Message[])
fld protected boolean removed
fld protected int type
fld protected jakarta.mail.Message[] msgs
fld public final static int ADDED = 1
fld public final static int REMOVED = 2
meth public boolean isRemoved()
meth public int getType()
meth public jakarta.mail.Message[] getMessages()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.MessageCountListener
intf java.util.EventListener
meth public abstract void messagesAdded(jakarta.mail.event.MessageCountEvent)
meth public abstract void messagesRemoved(jakarta.mail.event.MessageCountEvent)

CLSS public jakarta.mail.event.StoreEvent
cons public <init>(jakarta.mail.Store,int,java.lang.String)
fld protected int type
fld protected java.lang.String message
fld public final static int ALERT = 1
fld public final static int NOTICE = 2
meth public int getMessageType()
meth public java.lang.String getMessage()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.StoreListener
intf java.util.EventListener
meth public abstract void notification(jakarta.mail.event.StoreEvent)

CLSS public abstract jakarta.mail.event.TransportAdapter
cons public <init>()
intf jakarta.mail.event.TransportListener
meth public void messageDelivered(jakarta.mail.event.TransportEvent)
meth public void messageNotDelivered(jakarta.mail.event.TransportEvent)
meth public void messagePartiallyDelivered(jakarta.mail.event.TransportEvent)
supr java.lang.Object

CLSS public jakarta.mail.event.TransportEvent
cons public <init>(jakarta.mail.Transport,int,jakarta.mail.Address[],jakarta.mail.Address[],jakarta.mail.Address[],jakarta.mail.Message)
fld protected int type
fld protected jakarta.mail.Address[] invalid
fld protected jakarta.mail.Address[] validSent
fld protected jakarta.mail.Address[] validUnsent
fld protected jakarta.mail.Message msg
fld public final static int MESSAGE_DELIVERED = 1
fld public final static int MESSAGE_NOT_DELIVERED = 2
fld public final static int MESSAGE_PARTIALLY_DELIVERED = 3
meth public int getType()
meth public jakarta.mail.Address[] getInvalidAddresses()
meth public jakarta.mail.Address[] getValidSentAddresses()
meth public jakarta.mail.Address[] getValidUnsentAddresses()
meth public jakarta.mail.Message getMessage()
meth public void dispatch(java.lang.Object)
supr jakarta.mail.event.MailEvent
hfds serialVersionUID

CLSS public abstract interface jakarta.mail.event.TransportListener
intf java.util.EventListener
meth public abstract void messageDelivered(jakarta.mail.event.TransportEvent)
meth public abstract void messageNotDelivered(jakarta.mail.event.TransportEvent)
meth public abstract void messagePartiallyDelivered(jakarta.mail.event.TransportEvent)

CLSS public jakarta.mail.internet.AddressException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.String)
cons public <init>(java.lang.String,java.lang.String,int)
fld protected int pos
fld protected java.lang.String ref
meth public int getPos()
meth public java.lang.String getRef()
meth public java.lang.String toString()
supr jakarta.mail.internet.ParseException
hfds serialVersionUID

CLSS public jakarta.mail.internet.ContentDisposition
cons public <init>()
cons public <init>(java.lang.String) throws jakarta.mail.internet.ParseException
cons public <init>(java.lang.String,jakarta.mail.internet.ParameterList)
meth public java.lang.String getDisposition()
meth public java.lang.String getParameter(java.lang.String)
meth public java.lang.String toString()
meth public jakarta.mail.internet.ParameterList getParameterList()
meth public void setDisposition(java.lang.String)
meth public void setParameter(java.lang.String,java.lang.String)
meth public void setParameterList(jakarta.mail.internet.ParameterList)
supr java.lang.Object
hfds disposition,list

CLSS public jakarta.mail.internet.ContentType
cons public <init>()
cons public <init>(java.lang.String) throws jakarta.mail.internet.ParseException
cons public <init>(java.lang.String,java.lang.String,jakarta.mail.internet.ParameterList)
meth public boolean match(java.lang.String)
meth public boolean match(jakarta.mail.internet.ContentType)
meth public java.lang.String getBaseType()
meth public java.lang.String getParameter(java.lang.String)
meth public java.lang.String getPrimaryType()
meth public java.lang.String getSubType()
meth public java.lang.String toString()
meth public jakarta.mail.internet.ParameterList getParameterList()
meth public void setParameter(java.lang.String,java.lang.String)
meth public void setParameterList(jakarta.mail.internet.ParameterList)
meth public void setPrimaryType(java.lang.String)
meth public void setSubType(java.lang.String)
supr java.lang.Object
hfds list,primaryType,subType

CLSS public jakarta.mail.internet.HeaderTokenizer
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.String)
cons public <init>(java.lang.String,java.lang.String,boolean)
fld public final static java.lang.String MIME = "()<>@,;:\u005c\u0022\u0009 []/?="
fld public final static java.lang.String RFC822 = "()<>@,;:\u005c\u0022\u0009 .[]"
innr public static Token
meth public java.lang.String getRemainder()
meth public jakarta.mail.internet.HeaderTokenizer$Token next() throws jakarta.mail.internet.ParseException
meth public jakarta.mail.internet.HeaderTokenizer$Token next(char) throws jakarta.mail.internet.ParseException
meth public jakarta.mail.internet.HeaderTokenizer$Token next(char,boolean) throws jakarta.mail.internet.ParseException
meth public jakarta.mail.internet.HeaderTokenizer$Token peek() throws jakarta.mail.internet.ParseException
supr java.lang.Object
hfds EOFToken,currentPos,delimiters,maxPos,nextPos,peekPos,skipComments,string

CLSS public static jakarta.mail.internet.HeaderTokenizer$Token
 outer jakarta.mail.internet.HeaderTokenizer
cons public <init>(int,java.lang.String)
fld public final static int ATOM = -1
fld public final static int COMMENT = -3
fld public final static int EOF = -4
fld public final static int QUOTEDSTRING = -2
meth public int getType()
meth public java.lang.String getValue()
supr java.lang.Object
hfds type,value

CLSS public jakarta.mail.internet.InternetAddress
cons public <init>()
cons public <init>(java.lang.String) throws jakarta.mail.internet.AddressException
cons public <init>(java.lang.String,boolean) throws jakarta.mail.internet.AddressException
cons public <init>(java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
cons public <init>(java.lang.String,java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
fld protected java.lang.String address
fld protected java.lang.String encodedPersonal
fld protected java.lang.String personal
intf java.lang.Cloneable
meth public boolean equals(java.lang.Object)
meth public boolean isGroup()
meth public int hashCode()
meth public java.lang.Object clone()
meth public java.lang.String getAddress()
meth public java.lang.String getPersonal()
meth public java.lang.String getType()
meth public java.lang.String toString()
meth public java.lang.String toUnicodeString()
meth public jakarta.mail.internet.InternetAddress[] getGroup(boolean) throws jakarta.mail.internet.AddressException
meth public static java.lang.String toString(jakarta.mail.Address[])
meth public static java.lang.String toString(jakarta.mail.Address[],int)
meth public static java.lang.String toUnicodeString(jakarta.mail.Address[])
meth public static java.lang.String toUnicodeString(jakarta.mail.Address[],int)
meth public static jakarta.mail.internet.InternetAddress getLocalAddress(jakarta.mail.Session)
meth public static jakarta.mail.internet.InternetAddress[] parse(java.lang.String) throws jakarta.mail.internet.AddressException
meth public static jakarta.mail.internet.InternetAddress[] parse(java.lang.String,boolean) throws jakarta.mail.internet.AddressException
meth public static jakarta.mail.internet.InternetAddress[] parseHeader(java.lang.String,boolean) throws jakarta.mail.internet.AddressException
meth public void setAddress(java.lang.String)
meth public void setPersonal(java.lang.String) throws java.io.UnsupportedEncodingException
meth public void setPersonal(java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
meth public void validate() throws jakarta.mail.internet.AddressException
supr jakarta.mail.Address
hfds allowUtf8,ignoreBogusGroupName,rfc822phrase,serialVersionUID,specialsNoDot,specialsNoDotNoAt,useCanonicalHostName

CLSS public jakarta.mail.internet.InternetHeaders
cons public <init>()
cons public <init>(java.io.InputStream) throws jakarta.mail.MessagingException
cons public <init>(java.io.InputStream,boolean) throws jakarta.mail.MessagingException
fld protected java.util.List<jakarta.mail.internet.InternetHeaders$InternetHeader> headers
innr protected final static InternetHeader
meth public java.lang.String getHeader(java.lang.String,java.lang.String)
meth public java.lang.String[] getHeader(java.lang.String)
meth public java.util.Enumeration<java.lang.String> getAllHeaderLines()
meth public java.util.Enumeration<java.lang.String> getMatchingHeaderLines(java.lang.String[])
meth public java.util.Enumeration<java.lang.String> getNonMatchingHeaderLines(java.lang.String[])
meth public java.util.Enumeration<jakarta.mail.Header> getAllHeaders()
meth public java.util.Enumeration<jakarta.mail.Header> getMatchingHeaders(java.lang.String[])
meth public java.util.Enumeration<jakarta.mail.Header> getNonMatchingHeaders(java.lang.String[])
meth public void addHeader(java.lang.String,java.lang.String)
meth public void addHeaderLine(java.lang.String)
meth public void load(java.io.InputStream) throws jakarta.mail.MessagingException
meth public void load(java.io.InputStream,boolean) throws jakarta.mail.MessagingException
meth public void removeHeader(java.lang.String)
meth public void setHeader(java.lang.String,java.lang.String)
supr java.lang.Object
hfds ignoreWhitespaceLines
hcls MatchEnum,MatchHeaderEnum,MatchStringEnum

CLSS protected final static jakarta.mail.internet.InternetHeaders$InternetHeader
 outer jakarta.mail.internet.InternetHeaders
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.String)
meth public java.lang.String getValue()
supr jakarta.mail.Header
hfds line

CLSS public jakarta.mail.internet.MailDateFormat
cons public <init>()
meth public java.lang.StringBuffer format(java.util.Date,java.lang.StringBuffer,java.text.FieldPosition)
meth public java.util.Date get2DigitYearStart()
meth public java.util.Date parse(java.lang.String,java.text.ParsePosition)
meth public jakarta.mail.internet.MailDateFormat clone()
meth public void applyLocalizedPattern(java.lang.String)
meth public void applyPattern(java.lang.String)
meth public void set2DigitYearStart(java.util.Date)
meth public void setCalendar(java.util.Calendar)
meth public void setDateFormatSymbols(java.text.DateFormatSymbols)
meth public void setNumberFormat(java.text.NumberFormat)
supr java.text.SimpleDateFormat
hfds LEAP_SECOND,LOGGER,PATTERN,UNKNOWN_DAY_NAME,UTC,serialVersionUID
hcls AbstractDateParser,Rfc2822LenientParser,Rfc2822StrictParser

CLSS public jakarta.mail.internet.MimeBodyPart
cons public <init>()
cons public <init>(java.io.InputStream) throws jakarta.mail.MessagingException
cons public <init>(jakarta.mail.internet.InternetHeaders,byte[]) throws jakarta.mail.MessagingException
fld protected byte[] content
fld protected java.io.InputStream contentStream
fld protected java.lang.Object cachedContent
fld protected jakarta.activation.DataHandler dh
fld protected jakarta.mail.internet.InternetHeaders headers
intf jakarta.mail.internet.MimePart
meth protected java.io.InputStream getContentStream() throws jakarta.mail.MessagingException
meth protected void updateHeaders() throws jakarta.mail.MessagingException
meth public boolean isMimeType(java.lang.String) throws jakarta.mail.MessagingException
meth public int getLineCount() throws jakarta.mail.MessagingException
meth public int getSize() throws jakarta.mail.MessagingException
meth public java.io.InputStream getInputStream() throws java.io.IOException,jakarta.mail.MessagingException
meth public java.io.InputStream getRawInputStream() throws jakarta.mail.MessagingException
meth public java.lang.Object getContent() throws java.io.IOException,jakarta.mail.MessagingException
meth public java.lang.String getContentID() throws jakarta.mail.MessagingException
meth public java.lang.String getContentMD5() throws jakarta.mail.MessagingException
meth public java.lang.String getContentType() throws jakarta.mail.MessagingException
meth public java.lang.String getDescription() throws jakarta.mail.MessagingException
meth public java.lang.String getDisposition() throws jakarta.mail.MessagingException
meth public java.lang.String getEncoding() throws jakarta.mail.MessagingException
meth public java.lang.String getFileName() throws jakarta.mail.MessagingException
meth public java.lang.String getHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public java.lang.String[] getContentLanguage() throws jakarta.mail.MessagingException
meth public java.lang.String[] getHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getAllHeaderLines() throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getNonMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getAllHeaders() throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getNonMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public jakarta.activation.DataHandler getDataHandler() throws jakarta.mail.MessagingException
meth public void addHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void addHeaderLine(java.lang.String) throws jakarta.mail.MessagingException
meth public void attachFile(java.io.File) throws java.io.IOException,jakarta.mail.MessagingException
meth public void attachFile(java.io.File,java.lang.String,java.lang.String) throws java.io.IOException,jakarta.mail.MessagingException
meth public void attachFile(java.lang.String) throws java.io.IOException,jakarta.mail.MessagingException
meth public void attachFile(java.lang.String,java.lang.String,java.lang.String) throws java.io.IOException,jakarta.mail.MessagingException
meth public void removeHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public void saveFile(java.io.File) throws java.io.IOException,jakarta.mail.MessagingException
meth public void saveFile(java.lang.String) throws java.io.IOException,jakarta.mail.MessagingException
meth public void setContent(java.lang.Object,java.lang.String) throws jakarta.mail.MessagingException
meth public void setContent(jakarta.mail.Multipart) throws jakarta.mail.MessagingException
meth public void setContentID(java.lang.String) throws jakarta.mail.MessagingException
meth public void setContentLanguage(java.lang.String[]) throws jakarta.mail.MessagingException
meth public void setContentMD5(java.lang.String) throws jakarta.mail.MessagingException
meth public void setDataHandler(jakarta.activation.DataHandler) throws jakarta.mail.MessagingException
meth public void setDescription(java.lang.String) throws jakarta.mail.MessagingException
meth public void setDescription(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setDisposition(java.lang.String) throws jakarta.mail.MessagingException
meth public void setFileName(java.lang.String) throws jakarta.mail.MessagingException
meth public void setHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException
supr jakarta.mail.BodyPart
hfds allowutf8,cacheMultipart,decodeFileName,encodeFileName,ignoreMultipartEncoding,setContentTypeFileName,setDefaultTextCharset
hcls EncodedFileDataSource,MimePartDataHandler

CLSS public jakarta.mail.internet.MimeMessage
cons protected <init>(jakarta.mail.Folder,int)
cons protected <init>(jakarta.mail.Folder,java.io.InputStream,int) throws jakarta.mail.MessagingException
cons protected <init>(jakarta.mail.Folder,jakarta.mail.internet.InternetHeaders,byte[],int) throws jakarta.mail.MessagingException
cons public <init>(jakarta.mail.Session)
cons public <init>(jakarta.mail.Session,java.io.InputStream) throws jakarta.mail.MessagingException
cons public <init>(jakarta.mail.internet.MimeMessage) throws jakarta.mail.MessagingException
fld protected boolean modified
fld protected boolean saved
fld protected byte[] content
fld protected java.io.InputStream contentStream
fld protected java.lang.Object cachedContent
fld protected jakarta.activation.DataHandler dh
fld protected jakarta.mail.Flags flags
fld protected jakarta.mail.internet.InternetHeaders headers
innr public static RecipientType
intf jakarta.mail.internet.MimePart
meth protected java.io.InputStream getContentStream() throws jakarta.mail.MessagingException
meth protected jakarta.mail.internet.InternetHeaders createInternetHeaders(java.io.InputStream) throws jakarta.mail.MessagingException
meth protected jakarta.mail.internet.MimeMessage createMimeMessage(jakarta.mail.Session) throws jakarta.mail.MessagingException
meth protected void parse(java.io.InputStream) throws jakarta.mail.MessagingException
meth protected void updateHeaders() throws jakarta.mail.MessagingException
meth protected void updateMessageID() throws jakarta.mail.MessagingException
meth public boolean isMimeType(java.lang.String) throws jakarta.mail.MessagingException
meth public boolean isSet(jakarta.mail.Flags$Flag) throws jakarta.mail.MessagingException
meth public int getLineCount() throws jakarta.mail.MessagingException
meth public int getSize() throws jakarta.mail.MessagingException
meth public java.io.InputStream getInputStream() throws java.io.IOException,jakarta.mail.MessagingException
meth public java.io.InputStream getRawInputStream() throws jakarta.mail.MessagingException
meth public java.lang.Object getContent() throws java.io.IOException,jakarta.mail.MessagingException
meth public java.lang.String getContentID() throws jakarta.mail.MessagingException
meth public java.lang.String getContentMD5() throws jakarta.mail.MessagingException
meth public java.lang.String getContentType() throws jakarta.mail.MessagingException
meth public java.lang.String getDescription() throws jakarta.mail.MessagingException
meth public java.lang.String getDisposition() throws jakarta.mail.MessagingException
meth public java.lang.String getEncoding() throws jakarta.mail.MessagingException
meth public java.lang.String getFileName() throws jakarta.mail.MessagingException
meth public java.lang.String getHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public java.lang.String getMessageID() throws jakarta.mail.MessagingException
meth public java.lang.String getSubject() throws jakarta.mail.MessagingException
meth public java.lang.String[] getContentLanguage() throws jakarta.mail.MessagingException
meth public java.lang.String[] getHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public java.util.Date getReceivedDate() throws jakarta.mail.MessagingException
meth public java.util.Date getSentDate() throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getAllHeaderLines() throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<java.lang.String> getNonMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getAllHeaders() throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public java.util.Enumeration<jakarta.mail.Header> getNonMatchingHeaders(java.lang.String[]) throws jakarta.mail.MessagingException
meth public jakarta.activation.DataHandler getDataHandler() throws jakarta.mail.MessagingException
meth public jakarta.mail.Address getSender() throws jakarta.mail.MessagingException
meth public jakarta.mail.Address[] getAllRecipients() throws jakarta.mail.MessagingException
meth public jakarta.mail.Address[] getFrom() throws jakarta.mail.MessagingException
meth public jakarta.mail.Address[] getRecipients(jakarta.mail.Message$RecipientType) throws jakarta.mail.MessagingException
meth public jakarta.mail.Address[] getReplyTo() throws jakarta.mail.MessagingException
meth public jakarta.mail.Flags getFlags() throws jakarta.mail.MessagingException
meth public jakarta.mail.Message reply(boolean) throws jakarta.mail.MessagingException
meth public jakarta.mail.Message reply(boolean,boolean) throws jakarta.mail.MessagingException
meth public void addFrom(jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public void addHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void addHeaderLine(java.lang.String) throws jakarta.mail.MessagingException
meth public void addRecipients(jakarta.mail.Message$RecipientType,java.lang.String) throws jakarta.mail.MessagingException
meth public void addRecipients(jakarta.mail.Message$RecipientType,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public void removeHeader(java.lang.String) throws jakarta.mail.MessagingException
meth public void saveChanges() throws jakarta.mail.MessagingException
meth public void setContent(java.lang.Object,java.lang.String) throws jakarta.mail.MessagingException
meth public void setContent(jakarta.mail.Multipart) throws jakarta.mail.MessagingException
meth public void setContentID(java.lang.String) throws jakarta.mail.MessagingException
meth public void setContentLanguage(java.lang.String[]) throws jakarta.mail.MessagingException
meth public void setContentMD5(java.lang.String) throws jakarta.mail.MessagingException
meth public void setDataHandler(jakarta.activation.DataHandler) throws jakarta.mail.MessagingException
meth public void setDescription(java.lang.String) throws jakarta.mail.MessagingException
meth public void setDescription(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setDisposition(java.lang.String) throws jakarta.mail.MessagingException
meth public void setFileName(java.lang.String) throws jakarta.mail.MessagingException
meth public void setFlags(jakarta.mail.Flags,boolean) throws jakarta.mail.MessagingException
meth public void setFrom() throws jakarta.mail.MessagingException
meth public void setFrom(java.lang.String) throws jakarta.mail.MessagingException
meth public void setFrom(jakarta.mail.Address) throws jakarta.mail.MessagingException
meth public void setHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setRecipients(jakarta.mail.Message$RecipientType,java.lang.String) throws jakarta.mail.MessagingException
meth public void setRecipients(jakarta.mail.Message$RecipientType,jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public void setReplyTo(jakarta.mail.Address[]) throws jakarta.mail.MessagingException
meth public void setSender(jakarta.mail.Address) throws jakarta.mail.MessagingException
meth public void setSentDate(java.util.Date) throws jakarta.mail.MessagingException
meth public void setSubject(java.lang.String) throws jakarta.mail.MessagingException
meth public void setSubject(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void setText(java.lang.String,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException
meth public void writeTo(java.io.OutputStream,java.lang.String[]) throws java.io.IOException,jakarta.mail.MessagingException
supr jakarta.mail.Message
hfds allowutf8,answeredFlag,mailDateFormat,strict

CLSS public static jakarta.mail.internet.MimeMessage$RecipientType
 outer jakarta.mail.internet.MimeMessage
cons protected <init>(java.lang.String)
fld public final static jakarta.mail.internet.MimeMessage$RecipientType NEWSGROUPS
meth protected java.lang.Object readResolve() throws java.io.ObjectStreamException
supr jakarta.mail.Message$RecipientType
hfds serialVersionUID

CLSS public jakarta.mail.internet.MimeMultipart
cons public !varargs <init>(java.lang.String,jakarta.mail.BodyPart[]) throws jakarta.mail.MessagingException
cons public !varargs <init>(jakarta.mail.BodyPart[]) throws jakarta.mail.MessagingException
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(jakarta.activation.DataSource) throws jakarta.mail.MessagingException
fld protected boolean allowEmpty
fld protected boolean complete
fld protected boolean ignoreExistingBoundaryParameter
fld protected boolean ignoreMissingBoundaryParameter
fld protected boolean ignoreMissingEndBoundary
fld protected boolean parsed
fld protected java.lang.String preamble
fld protected jakarta.activation.DataSource ds
meth protected jakarta.mail.internet.InternetHeaders createInternetHeaders(java.io.InputStream) throws jakarta.mail.MessagingException
meth protected jakarta.mail.internet.MimeBodyPart createMimeBodyPart(java.io.InputStream) throws jakarta.mail.MessagingException
meth protected jakarta.mail.internet.MimeBodyPart createMimeBodyPart(jakarta.mail.internet.InternetHeaders,byte[]) throws jakarta.mail.MessagingException
meth protected void initializeProperties()
meth protected void parse() throws jakarta.mail.MessagingException
meth protected void updateHeaders() throws jakarta.mail.MessagingException
meth public boolean isComplete() throws jakarta.mail.MessagingException
meth public boolean removeBodyPart(jakarta.mail.BodyPart) throws jakarta.mail.MessagingException
meth public int getCount() throws jakarta.mail.MessagingException
meth public java.lang.String getPreamble() throws jakarta.mail.MessagingException
meth public jakarta.mail.BodyPart getBodyPart(int) throws jakarta.mail.MessagingException
meth public jakarta.mail.BodyPart getBodyPart(java.lang.String) throws jakarta.mail.MessagingException
meth public void addBodyPart(jakarta.mail.BodyPart) throws jakarta.mail.MessagingException
meth public void addBodyPart(jakarta.mail.BodyPart,int) throws jakarta.mail.MessagingException
meth public void removeBodyPart(int) throws jakarta.mail.MessagingException
meth public void setPreamble(java.lang.String) throws jakarta.mail.MessagingException
meth public void setSubType(java.lang.String) throws jakarta.mail.MessagingException
meth public void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException
supr jakarta.mail.Multipart

CLSS public abstract interface jakarta.mail.internet.MimePart
intf jakarta.mail.Part
meth public abstract java.lang.String getContentID() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getContentMD5() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getEncoding() throws jakarta.mail.MessagingException
meth public abstract java.lang.String getHeader(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public abstract java.lang.String[] getContentLanguage() throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<java.lang.String> getAllHeaderLines() throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<java.lang.String> getMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public abstract java.util.Enumeration<java.lang.String> getNonMatchingHeaderLines(java.lang.String[]) throws jakarta.mail.MessagingException
meth public abstract void addHeaderLine(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setContentLanguage(java.lang.String[]) throws jakarta.mail.MessagingException
meth public abstract void setContentMD5(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setText(java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setText(java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public abstract void setText(java.lang.String,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException

CLSS public jakarta.mail.internet.MimePartDataSource
cons public <init>(jakarta.mail.internet.MimePart)
fld protected jakarta.mail.internet.MimePart part
intf jakarta.activation.DataSource
intf jakarta.mail.MessageAware
meth public java.io.InputStream getInputStream() throws java.io.IOException
meth public java.io.OutputStream getOutputStream() throws java.io.IOException
meth public java.lang.String getContentType()
meth public java.lang.String getName()
meth public jakarta.mail.MessageContext getMessageContext()
supr java.lang.Object
hfds context

CLSS public jakarta.mail.internet.MimeUtility
fld public final static int ALL = -1
meth public static java.io.InputStream decode(java.io.InputStream,java.lang.String) throws jakarta.mail.MessagingException
meth public static java.io.OutputStream encode(java.io.OutputStream,java.lang.String) throws jakarta.mail.MessagingException
meth public static java.io.OutputStream encode(java.io.OutputStream,java.lang.String,java.lang.String) throws jakarta.mail.MessagingException
meth public static java.lang.String decodeText(java.lang.String) throws java.io.UnsupportedEncodingException
meth public static java.lang.String decodeWord(java.lang.String) throws java.io.UnsupportedEncodingException,jakarta.mail.internet.ParseException
meth public static java.lang.String encodeText(java.lang.String) throws java.io.UnsupportedEncodingException
meth public static java.lang.String encodeText(java.lang.String,java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
meth public static java.lang.String encodeWord(java.lang.String) throws java.io.UnsupportedEncodingException
meth public static java.lang.String encodeWord(java.lang.String,java.lang.String,java.lang.String) throws java.io.UnsupportedEncodingException
meth public static java.lang.String fold(int,java.lang.String)
meth public static java.lang.String getDefaultJavaCharset()
meth public static java.lang.String getEncoding(jakarta.activation.DataHandler)
meth public static java.lang.String getEncoding(jakarta.activation.DataSource)
meth public static java.lang.String javaCharset(java.lang.String)
meth public static java.lang.String mimeCharset(java.lang.String)
meth public static java.lang.String quote(java.lang.String,java.lang.String)
meth public static java.lang.String unfold(java.lang.String)
supr java.lang.Object
hfds ALL_ASCII,MOSTLY_ASCII,MOSTLY_NONASCII,allowUtf8,decodeStrict,defaultJavaCharset,defaultMIMECharset,encodeEolStrict,foldEncodedWords,foldText,ignoreUnknownEncoding,java2mime,mime2java,nonAsciiCharsetMap

CLSS public jakarta.mail.internet.NewsAddress
cons public <init>()
cons public <init>(java.lang.String)
cons public <init>(java.lang.String,java.lang.String)
fld protected java.lang.String host
fld protected java.lang.String newsgroup
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.String getHost()
meth public java.lang.String getNewsgroup()
meth public java.lang.String getType()
meth public java.lang.String toString()
meth public static java.lang.String toString(jakarta.mail.Address[])
meth public static jakarta.mail.internet.NewsAddress[] parse(java.lang.String) throws jakarta.mail.internet.AddressException
meth public void setHost(java.lang.String)
meth public void setNewsgroup(java.lang.String)
supr jakarta.mail.Address
hfds serialVersionUID

CLSS public jakarta.mail.internet.ParameterList
cons public <init>()
cons public <init>(java.lang.String) throws jakarta.mail.internet.ParseException
meth public int size()
meth public java.lang.String get(java.lang.String)
meth public java.lang.String toString()
meth public java.lang.String toString(int)
meth public java.util.Enumeration<java.lang.String> getNames()
meth public void combineSegments()
meth public void remove(java.lang.String)
meth public void set(java.lang.String,java.lang.String)
meth public void set(java.lang.String,java.lang.String,java.lang.String)
supr java.lang.Object
hfds applehack,decodeParameters,decodeParametersStrict,encodeParameters,hex,lastName,list,multisegmentNames,parametersStrict,slist,splitLongParameters,windowshack
hcls LiteralValue,MultiValue,ParamEnum,ToStringBuffer,Value

CLSS public jakarta.mail.internet.ParseException
cons public <init>()
cons public <init>(java.lang.String)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public jakarta.mail.internet.PreencodedMimeBodyPart
cons public <init>(java.lang.String)
meth protected void updateHeaders() throws jakarta.mail.MessagingException
meth public java.lang.String getEncoding() throws jakarta.mail.MessagingException
meth public void writeTo(java.io.OutputStream) throws java.io.IOException,jakarta.mail.MessagingException
supr jakarta.mail.internet.MimeBodyPart
hfds encoding

CLSS public abstract interface jakarta.mail.internet.SharedInputStream
meth public abstract java.io.InputStream newStream(long,long)
meth public abstract long getPosition()

CLSS public abstract jakarta.mail.search.AddressStringTerm
cons protected <init>(java.lang.String)
meth protected boolean match(jakarta.mail.Address)
meth public boolean equals(java.lang.Object)
supr jakarta.mail.search.StringTerm
hfds serialVersionUID

CLSS public abstract jakarta.mail.search.AddressTerm
cons protected <init>(jakarta.mail.Address)
fld protected jakarta.mail.Address address
meth protected boolean match(jakarta.mail.Address)
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public jakarta.mail.Address getAddress()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.AndTerm
cons public <init>(jakarta.mail.search.SearchTerm,jakarta.mail.search.SearchTerm)
cons public <init>(jakarta.mail.search.SearchTerm[])
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.search.SearchTerm[] getTerms()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID,terms

CLSS public final jakarta.mail.search.BodyTerm
cons public <init>(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.StringTerm
hfds serialVersionUID

CLSS public abstract jakarta.mail.search.ComparisonTerm
cons public <init>()
fld protected int comparison
fld public final static int EQ = 3
fld public final static int GE = 6
fld public final static int GT = 5
fld public final static int LE = 1
fld public final static int LT = 2
fld public final static int NE = 4
meth public boolean equals(java.lang.Object)
meth public int hashCode()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID

CLSS public abstract jakarta.mail.search.DateTerm
cons protected <init>(int,java.util.Date)
fld protected java.util.Date date
meth protected boolean match(java.util.Date)
meth public boolean equals(java.lang.Object)
meth public int getComparison()
meth public int hashCode()
meth public java.util.Date getDate()
supr jakarta.mail.search.ComparisonTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.FlagTerm
cons public <init>(jakarta.mail.Flags,boolean)
meth public boolean equals(java.lang.Object)
meth public boolean getTestSet()
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.Flags getFlags()
supr jakarta.mail.search.SearchTerm
hfds flags,serialVersionUID,set

CLSS public final jakarta.mail.search.FromStringTerm
cons public <init>(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.AddressStringTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.FromTerm
cons public <init>(jakarta.mail.Address)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.AddressTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.HeaderTerm
cons public <init>(java.lang.String,java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public java.lang.String getHeaderName()
supr jakarta.mail.search.StringTerm
hfds headerName,serialVersionUID

CLSS public abstract jakarta.mail.search.IntegerComparisonTerm
cons protected <init>(int,int)
fld protected int number
meth protected boolean match(int)
meth public boolean equals(java.lang.Object)
meth public int getComparison()
meth public int getNumber()
meth public int hashCode()
supr jakarta.mail.search.ComparisonTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.MessageIDTerm
cons public <init>(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.StringTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.MessageNumberTerm
cons public <init>(int)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.IntegerComparisonTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.NotTerm
cons public <init>(jakarta.mail.search.SearchTerm)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.search.SearchTerm getTerm()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID,term

CLSS public final jakarta.mail.search.OrTerm
cons public <init>(jakarta.mail.search.SearchTerm,jakarta.mail.search.SearchTerm)
cons public <init>(jakarta.mail.search.SearchTerm[])
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.search.SearchTerm[] getTerms()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID,terms

CLSS public final jakarta.mail.search.ReceivedDateTerm
cons public <init>(int,java.util.Date)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.DateTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.RecipientStringTerm
cons public <init>(jakarta.mail.Message$RecipientType,java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.Message$RecipientType getRecipientType()
supr jakarta.mail.search.AddressStringTerm
hfds serialVersionUID,type

CLSS public final jakarta.mail.search.RecipientTerm
cons public <init>(jakarta.mail.Message$RecipientType,jakarta.mail.Address)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
meth public int hashCode()
meth public jakarta.mail.Message$RecipientType getRecipientType()
supr jakarta.mail.search.AddressTerm
hfds serialVersionUID,type

CLSS public jakarta.mail.search.SearchException
cons public <init>()
cons public <init>(java.lang.String)
supr jakarta.mail.MessagingException
hfds serialVersionUID

CLSS public abstract jakarta.mail.search.SearchTerm
cons public <init>()
intf java.io.Serializable
meth public abstract boolean match(jakarta.mail.Message)
supr java.lang.Object
hfds serialVersionUID

CLSS public final jakarta.mail.search.SentDateTerm
cons public <init>(int,java.util.Date)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.DateTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.SizeTerm
cons public <init>(int,int)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.IntegerComparisonTerm
hfds serialVersionUID

CLSS public abstract jakarta.mail.search.StringTerm
cons protected <init>(java.lang.String)
cons protected <init>(java.lang.String,boolean)
fld protected boolean ignoreCase
fld protected java.lang.String pattern
meth protected boolean match(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean getIgnoreCase()
meth public int hashCode()
meth public java.lang.String getPattern()
supr jakarta.mail.search.SearchTerm
hfds serialVersionUID

CLSS public final jakarta.mail.search.SubjectTerm
cons public <init>(java.lang.String)
meth public boolean equals(java.lang.Object)
meth public boolean match(jakarta.mail.Message)
supr jakarta.mail.search.StringTerm
hfds serialVersionUID

CLSS public jakarta.mail.util.ByteArrayDataSource
cons public <init>(byte[],java.lang.String)
cons public <init>(java.io.InputStream,java.lang.String) throws java.io.IOException
cons public <init>(java.lang.String,java.lang.String) throws java.io.IOException
intf jakarta.activation.DataSource
meth public java.io.InputStream getInputStream() throws java.io.IOException
meth public java.io.OutputStream getOutputStream() throws java.io.IOException
meth public java.lang.String getContentType()
meth public java.lang.String getName()
meth public void setName(java.lang.String)
supr java.lang.Object
hfds data,len,name,type
hcls DSByteArrayOutputStream

CLSS public jakarta.mail.util.SharedByteArrayInputStream
cons public <init>(byte[])
cons public <init>(byte[],int,int)
fld protected int start
intf jakarta.mail.internet.SharedInputStream
meth public java.io.InputStream newStream(long,long)
meth public long getPosition()
supr java.io.ByteArrayInputStream

CLSS public jakarta.mail.util.SharedFileInputStream
cons public <init>(java.io.File) throws java.io.IOException
cons public <init>(java.io.File,int) throws java.io.IOException
cons public <init>(java.lang.String) throws java.io.IOException
cons public <init>(java.lang.String,int) throws java.io.IOException
fld protected int bufsize
fld protected java.io.RandomAccessFile in
fld protected long bufpos
fld protected long datalen
fld protected long start
intf jakarta.mail.internet.SharedInputStream
meth protected void finalize() throws java.lang.Throwable
meth public boolean markSupported()
meth public int available() throws java.io.IOException
meth public int read() throws java.io.IOException
meth public int read(byte[],int,int) throws java.io.IOException
meth public java.io.InputStream newStream(long,long)
meth public long getPosition()
meth public long skip(long) throws java.io.IOException
meth public void close() throws java.io.IOException
meth public void mark(int)
meth public void reset() throws java.io.IOException
supr java.io.BufferedInputStream
hfds defaultBufferSize,master,sf
hcls SharedFile

