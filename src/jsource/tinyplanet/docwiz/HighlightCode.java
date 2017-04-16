//
// This class is not copyrighted. It was written by Mr Nookala Satish Kumar
//  [javasatish@yahoo.com] and I downloaded it from http://java.isavvix.com.
// To verify the availability of the code for use here I emailed Mr. Kumar
//	and my query is followed below by his response:
//
//> Mr. Kumar,
//>
//> I saw your java code to HTML with highlights code on
//> iSavvix. (code-highlight.zip) Is this code copyrighted? Is it
//> available to inclusion in open source projects? Is it your code?
//
//Hi Lee Meador,
//
//It is purely my code. There is no copyright. You can
//use it in open source projects. I will be delighted if
//you put my name where you use this source code (this
//is optional).
//
//Regards,
//Satish.
//
// So, consider his request fulfilled. -- Lee Meador

// History:
//	7/26/2001 lmm	- Bring code in and adapt for use here. Original was used as JSP taglib.
//					- Add default values for the colors to match what I like.
//					- Add the list of classes and JDK words and have them appear in red.
//					- Use Set objects to hold and search the lists of key words.

package tinyplanet.docwiz;

import java.io.*;
import java.util.*;
//import javax.servlet.jsp.*;
//import javax.servlet.jsp.tagext.*;
//import javax.servlet.http.*;

public class HighlightCode
{
	private String keywordColor = "blue";
	private String commentColor ="green";
	private String quoteColor = "#662200";		// brown is for double quote strings
	private String stdlibColor = "red";

	static final String[] keywords = new String[] {
		"abstract", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
		"continue", "default", "do", "double", "else", "extends", "final", "finally", "float",
		"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
		"native", "new", "package", "private", "protected", "public", "return", "short",
		"static", "super", "switch", "synchronized", "this", "throw", "throws", "transient",
		"try", "void", "volatile", "while"
	};
	static final Set keywordSet = new HashSet(Arrays.asList(keywords));

	static final String[] stdlib = new String[]  {
		"Integer", "Float", "Double", "String", "Boolean", "StringBuffer", "Object", "Class",
		"NoSuchMethodException", "InstantiationException", "IllegalAccessException", "CloneNotSupportedException",
		"NumberFormatException", "CloneNotSupportedException", "IndexOutOfBoundsException", "ClassCastException", "",
		"NullPointerException", "ClassNotFoundException", "IllegalArgumentException", "ArrayIndexOutOfBoundsException",
		"Exception", "Throwable", "RuntimeException", "CreateException", "LinkageError", "ThreadDeath", "UnsupportedOperationException",
		"Cloneable",
		"ARG_IN", "ARG_INOUT", "ARG_OUT", "AWTError", "AWTEvent", "AWTEventListener", "AWTEventMulticaster", "AWTException", "AWTPermission", "AbstractBorder", "AbstractButton", "AbstractCellEditor", "AbstractCollection", "AbstractColorChooserPanel", "AbstractDocument", "AbstractLayoutCache",
		"AbstractList", "AbstractListModel", "AbstractMap", "AbstractSequentialList", "AbstractSet", "AbstractTableModel", "AbstractUndoableEdit", "AbstractWriter", "AccessControlContext", "AccessControlException", "AccessController", "AccessException", "Accessible", "AccessibleAction", "AccessibleBundle", "AccessibleComponent",
		"AccessibleContext", "AccessibleHyperlink", "AccessibleHypertext", "AccessibleIcon", "AccessibleObject", "AccessibleRelation", "AccessibleRelationSet", "AccessibleResourceBundle", "AccessibleRole", "AccessibleSelection", "AccessibleState", "AccessibleStateSet", "AccessibleTable", "AccessibleTableModelChange", "AccessibleText", "AccessibleValue",
		"Acl", "AclEntry", "AclNotFoundException", "Action", "ActionEvent", "ActionListener", "ActionMap", "ActionMapUIResource", "Activatable", "ActivateFailedException", "ActivationDesc", "ActivationException", "ActivationGroup", "ActivationGroupDesc", "ActivationGroupID", "ActivationID",
		"ActivationInstantiator", "ActivationMonitor", "ActivationSystem", "Activator", "ActiveEvent", "ActiveValue", "Adjustable", "AdjustmentEvent", "AdjustmentListener", "Adler32", "AffineTransform", "AffineTransformOp", "AlgorithmParameterGenerator", "AlgorithmParameterGeneratorSpi", "AlgorithmParameterSpec", "AlgorithmParameters",
		"AlgorithmParametersSpi", "AlignmentAction", "AllPermission", "AlphaComposite", "AlreadyBound", "AlreadyBoundException", "AlreadyBoundHelper", "AlreadyBoundHolder", "AncestorEvent", "AncestorListener", "Annotation", "Any", "AnyHolder", "AnySeqHelper", "AnySeqHolder", "Applet",
		"AppletContext", "AppletInitializer", "AppletStub", "ApplicationException", "Arc2D", "Area", "AreaAveragingScaleFilter", "Array", "ArrayList", "Arrays", "AsyncBoxView", "Attribute", "AttributeContext", "AttributeInUseException", "AttributeList", "AttributeModificationException",
		"AttributeSet", "AttributeUndoableEdit", "AttributedCharacterIterator", "AttributedString", "Attributes", "AudioClip", "AudioFileFormat", "AudioFileReader", "AudioFileWriter", "AudioFormat", "AudioInputStream", "AudioPermission", "AudioSystem", "AuthenticationException", "AuthenticationNotSupportedException", "Authenticator",
		"Autoscroll", "BAD_CONTEXT", "BAD_INV_ORDER", "BAD_OPERATION", "BAD_PARAM", "BAD_POLICY", "BAD_POLICY_TYPE", "BAD_POLICY_VALUE", "BAD_TYPECODE", "BCSIterator", "BCSSServiceProvider", "BadKind", "BadLocationException", "BandCombineOp", "BandedSampleModel", "BasicArrowButton",
		"BasicAttribute", "BasicAttributes", "BasicBorders", "BasicButtonListener", "BasicButtonUI", "BasicCaret", "BasicCheckBoxMenuItemUI", "BasicCheckBoxUI", "BasicColorChooserUI", "BasicComboBoxEditor", "BasicComboBoxRenderer", "BasicComboBoxUI", "BasicComboPopup", "BasicDesktopIconUI", "BasicDesktopPaneUI", "BasicDirectoryModel",
		"BasicEditorPaneUI", "BasicFileChooserUI", "BasicGraphicsUtils", "BasicHTML", "BasicHighlighter", "BasicIconFactory", "BasicInternalFrameTitlePane", "BasicInternalFrameUI", "BasicLabelUI", "BasicListUI", "BasicLookAndFeel", "BasicMenuBarUI", "BasicMenuItemUI", "BasicMenuUI", "BasicOptionPaneUI", "BasicPanelUI",
		"BasicPasswordFieldUI", "BasicPermission", "BasicPopupMenuSeparatorUI", "BasicPopupMenuUI", "BasicProgressBarUI", "BasicRadioButtonMenuItemUI", "BasicRadioButtonUI", "BasicRootPaneUI", "BasicScrollBarUI", "BasicScrollPaneUI", "BasicSeparatorUI", "BasicSliderUI", "BasicSplitPaneDivider", "BasicSplitPaneUI", "BasicStroke", "BasicTabbedPaneUI",
		"BasicTableHeaderUI", "BasicTableUI", "BasicTextAreaUI", "BasicTextFieldUI", "BasicTextPaneUI", "BasicTextUI", "BasicToggleButtonUI", "BasicToolBarSeparatorUI", "BasicToolBarUI", "BasicToolTipUI", "BasicTreeUI", "BasicViewportUI", "BatchUpdateException", "BeanContext", "BeanContextChild", "BeanContextChildComponentProxy",
		"BeanContextChildSupport", "BeanContextContainerProxy", "BeanContextEvent", "BeanContextMembershipEvent", "BeanContextMembershipListener", "BeanContextProxy", "BeanContextServiceAvailableEvent", "BeanContextServiceProvider", "BeanContextServiceProviderBeanInfo", "BeanContextServiceRevokedEvent", "BeanContextServiceRevokedListener", "BeanContextServices", "BeanContextServicesListener", "BeanContextServicesSupport", "BeanContextSupport", "BeanDescriptor",
		"BeanInfo", "Beans", "BeepAction", "BevelBorder", "BevelBorderUIResource", "Bias", "BigDecimal", "BigInteger", "BinaryRefAddr", "BindException", "Binding", "BindingHelper", "BindingHolder", "BindingIterator", "BindingIteratorHelper", "BindingIteratorHolder",
		"BindingIteratorOperations", "BindingListHelper", "BindingListHolder", "BindingType", "BindingTypeHelper", "BindingTypeHolder", "BitSet", "Blob", "BlockView", "BoldAction", "Book", "BooleanControl", "BooleanHolder", "BooleanSeqHelper", "BooleanSeqHolder", "Border",
		"BorderFactory", "BorderLayout", "BorderUIResource", "BoundedRangeModel", "Bounds", "Box", "BoxLayout", "BoxPainter", "BoxView", "BoxedValueHelper", "BreakIterator", "BufferedImage", "BufferedImageFilter", "BufferedImageOp", "BufferedInputStream", "BufferedOutputStream",
		"BufferedReader", "BufferedWriter", "Button", "ButtonAreaLayout", "ButtonBorder", "ButtonGroup", "ButtonModel", "ButtonUI", "ByteArrayInputStream", "ByteArrayOutputStream", "ByteHolder", "ByteLookupTable", "CMMException", "COMM_FAILURE", "CRC32", "CRL",
		"CRLException", "CSS", "CTX_RESTRICT_SCOPE", "Calendar", "CallableStatement", "CannotProceed", "CannotProceedException", "CannotProceedHelper", "CannotProceedHolder", "CannotRedoException", "CannotUndoException", "Canvas", "CardLayout", "Caret", "CaretEvent", "CaretListener",
		"CaretPolicy", "CellEditor", "CellEditorListener", "CellRendererPane", "Certificate", "CertificateEncodingException", "CertificateException", "CertificateExpiredException", "CertificateFactory", "CertificateFactorySpi", "CertificateNotYetValidException", "CertificateParsingException", "CertificateRep", "ChangeEvent", "ChangeListener", "ChangedCharSetException",
		"CharArrayReader", "CharArrayWriter", "CharConversionException", "CharHolder", "CharSeqHelper", "CharSeqHolder", "CharacterAttribute", "CharacterConstants", "CharacterIterator", "Checkbox", "CheckboxGroup", "CheckboxMenuItem", "CheckedInputStream", "CheckedOutputStream", "Checksum", "Choice",
		"ChoiceFormat", "ClassDesc", "Clip", "Clipboard", "ClipboardOwner", "Clob", "CodeSource", "CollationElementIterator", "CollationKey", "Collator", "Collection", "Collections", "Color", "ColorAttribute", "ColorChooserComponentFactory", "ColorChooserUI",
		"ColorConstants", "ColorConvertOp", "ColorModel", "ColorSelectionModel", "ColorSpace", "ColorType", "ColorUIResource", "ComboBoxEditor", "ComboBoxModel", "ComboBoxUI", "ComboPopup", "CommandEnvironment", "CommunicationException", "Comparator", "CompletionStatus", "CompletionStatusHelper",
		"Component", "ComponentAdapter", "ComponentColorModel", "ComponentEvent", "ComponentInputMap", "ComponentInputMapUIResource", "ComponentListener", "ComponentOrientation", "ComponentSampleModel", "ComponentUI", "ComponentView", "Composite", "CompositeContext", "CompositeName", "CompositeView", "CompoundBorder",
		"CompoundBorderUIResource", "CompoundControl", "CompoundEdit", "CompoundName", "ConcurrentModificationException", "ConfigurationException", "ConnectException", "ConnectIOException", "Connection", "Constructor", "Container", "ContainerAdapter", "ContainerEvent", "ContainerListener", "Content", "ContentHandler",
		"ContentHandlerFactory", "ContentModel", "Context", "ContextList", "ContextNotEmptyException", "ContextualRenderedImageFactory", "Control", "ControlFactory", "ControllerEventListener", "ConvolveOp", "CopyAction", "CropImageFilter", "CubicCurve2D", "Current", "CurrentHelper", "CurrentHolder",
		"CurrentOperations", "Cursor", "CustomMarshal", "CustomValue", "Customizer", "CutAction", "DATA_CONVERSION", "DGC", "DSAKey", "DSAKeyPairGenerator", "DSAParameterSpec", "DSAParams", "DSAPrivateKey", "DSAPrivateKeySpec", "DSAPublicKey", "DSAPublicKeySpec",
		"DTD", "DTDConstants", "DataBuffer", "DataBufferByte", "DataBufferInt", "DataBufferShort", "DataBufferUShort", "DataFlavor", "DataFormatException", "DataInput", "DataInputStream", "DataLine", "DataOutput", "DataOutputStream", "DataTruncation", "DatabaseMetaData",
		"DatagramPacket", "DatagramSocket", "DatagramSocketImpl", "DatagramSocketImplFactory", "Date", "DateFormat", "DateFormatSymbols", "DebugGraphics", "DecimalFormat", "DecimalFormatSymbols", "DefaultBoundedRangeModel", "DefaultButtonModel", "DefaultCaret", "DefaultCellEditor", "DefaultColorSelectionModel", "DefaultComboBoxModel",
		"DefaultDesktopManager", "DefaultEditorKit", "DefaultFocusManager", "DefaultHighlightPainter", "DefaultHighlighter", "DefaultKeyTypedAction", "DefaultListCellRenderer", "DefaultListModel", "DefaultListSelectionModel", "DefaultMenuLayout", "DefaultMetalTheme", "DefaultMutableTreeNode", "DefaultSelectionType", "DefaultSingleSelectionModel", "DefaultStyledDocument", "DefaultTableCellRenderer",
		"DefaultTableColumnModel", "DefaultTableModel", "DefaultTextUI", "DefaultTreeCellEditor", "DefaultTreeCellRenderer", "DefaultTreeModel", "DefaultTreeSelectionModel", "DefinitionKind", "DefinitionKindHelper", "Deflater", "DeflaterOutputStream", "Delegate", "DesignMode", "DesktopIconUI", "DesktopManager", "DesktopPaneUI",
		"DestinationType", "Dialog", "DialogType", "Dictionary", "DigestException", "DigestInputStream", "DigestOutputStream", "Dimension", "Dimension2D", "DimensionUIResource", "DirContext", "DirObjectFactory", "DirStateFactory", "DirectColorModel", "DirectoryManager", "DnDConstants",
		"Document", "DocumentEvent", "DocumentListener", "DocumentParser", "DomainCombiner", "DomainManager", "DomainManagerOperations", "DoubleHolder", "DoubleSeqHelper", "DoubleSeqHolder", "DragGestureEvent", "DragGestureListener", "DragGestureRecognizer", "DragSource", "DragSourceContext", "DragSourceDragEvent",
		"DragSourceDropEvent", "DragSourceEvent", "DragSourceListener", "Driver", "DriverManager", "DriverPropertyInfo", "DropTarget", "DropTargetAutoScroller", "DropTargetContext", "DropTargetDragEvent", "DropTargetDropEvent", "DropTargetEvent", "DropTargetListener", "DynAny", "DynArray", "DynEnum",
		"DynFixed", "DynSequence", "DynStruct", "DynUnion", "DynValue", "DynamicImplementation", "DynamicUtilTreeNode", "EOFException", "EditorKit", "Element", "ElementChange", "ElementEdit", "ElementIterator", "ElementSpec", "Ellipse2D", "EmptyBorder",
		"EmptyBorderUIResource", "EmptySelectionModel", "EmptyStackException", "EncodedKeySpec", "Encoding", "Entity", "Entry", "EnumControl", "Enumeration", "Environment", "EtchedBorder", "EtchedBorderUIResource", "Event", "EventContext", "EventDirContext", "EventListener",
		"EventListenerList", "EventObject", "EventQueue", "EventSetDescriptor", "EventType", "ExceptionList", "ExpandVetoException", "ExportException", "ExtendedRequest", "ExtendedResponse", "Externalizable", "FREE_MEM", "FeatureDescriptor", "Field", "FieldBorder", "FieldNameHelper",
		"FieldPosition", "FieldView", "File", "FileChooserUI", "FileDescriptor", "FileDialog", "FileFilter", "FileIcon16", "FileInputStream", "FileNameMap", "FileNotFoundException", "FileOutputStream", "FilePermission", "FileReader", "FileSystemView", "FileView",
		"FileWriter", "FilenameFilter", "Filler", "FilterInputStream", "FilterOutputStream", "FilterReader", "FilterWriter", "FilteredImageSource", "FixedHeightLayoutCache", "FixedHolder", "FlatteningPathIterator", "FlavorMap", "FloatControl", "FloatHolder", "FloatSeqHelper", "FloatSeqHolder",
		"FlowLayout", "FlowStrategy", "FlowView", "Flush3DBorder", "FocusAdapter", "FocusEvent", "FocusListener", "FocusManager", "FolderIcon16", "Font", "FontAttribute", "FontConstants", "FontFamilyAction", "FontFormatException", "FontMetrics", "FontRenderContext",
		"FontSizeAction", "FontUIResource", "ForegroundAction", "FormView", "Format", "FormatConversionProvider", "Frame", "GZIPInputStream", "GZIPOutputStream", "GapContent", "GeneralPath", "GeneralSecurityException", "GetField", "GlyphJustificationInfo", "GlyphMetrics", "GlyphPainter",
		"GlyphVector", "GlyphView", "GradientPaint", "GraphicAttribute", "Graphics", "Graphics2D", "GraphicsConfigTemplate", "GraphicsConfiguration", "GraphicsDevice", "GraphicsEnvironment", "GrayFilter", "GregorianCalendar", "GridBagConstraints", "GridBagLayout", "GridLayout", "Group",
		"Guard", "GuardedObject", "HTML", "HTMLDocument", "HTMLEditorKit", "HTMLFactory", "HTMLFrameHyperlinkEvent", "HTMLTextAction", "HTMLWriter", "HasControls", "HashMap", "HashSet", "Hashtable", "HierarchyBoundsAdapter", "HierarchyBoundsListener", "HierarchyEvent",
		"HierarchyListener", "Highlight", "HighlightPainter", "Highlighter", "HttpURLConnection", "HyperlinkEvent", "HyperlinkListener", "ICC_ColorSpace", "ICC_Profile", "ICC_ProfileGray", "ICC_ProfileRGB", "IDLEntity", "IDLType", "IDLTypeHelper", "IDLTypeOperations", "IMP_LIMIT",
		"INITIALIZE", "INTERNAL", "INTF_REPOS", "INVALID_TRANSACTION", "INV_FLAG", "INV_IDENT", "INV_OBJREF", "INV_POLICY", "IOException", "IRObject", "IRObjectOperations", "Icon", "IconUIResource", "IconView", "IdentifierHelper", "Identity",
		"IdentityScope", "IllegalComponentStateException", "IllegalPathStateException", "Image", "ImageConsumer", "ImageFilter", "ImageGraphicAttribute", "ImageIcon", "ImageObserver", "ImageProducer", "ImagingOpException", "InconsistentTypeCode", "IndexColorModel", "IndexedPropertyDescriptor", "IndirectionException", "InetAddress",
		"Inflater", "InflaterInputStream", "Info", "InitialContext", "InitialContextFactory", "InitialContextFactoryBuilder", "InitialDirContext", "InitialLdapContext", "Initializer", "InlineView", "InputContext", "InputEvent", "InputMap", "InputMapUIResource", "InputMethod", "InputMethodContext",
		"InputMethodDescriptor", "InputMethodEvent", "InputMethodHighlight", "InputMethodListener", "InputMethodRequests", "InputStream", "InputStreamReader", "InputSubset", "InputVerifier", "InsertBreakAction", "InsertContentAction", "InsertHTMLTextAction", "InsertTabAction", "Insets", "InsetsUIResource", "Instrument",
		"InsufficientResourcesException", "IntHolder", "InternalFrameAdapter", "InternalFrameBorder", "InternalFrameEvent", "InternalFrameListener", "InternalFrameUI", "InterruptedIOException", "InterruptedNamingException", "IntrospectionException", "Introspector", "Invalid", "InvalidAlgorithmParameterException", "InvalidAttributeIdentifierException", "InvalidAttributeValueException", "InvalidAttributesException",
		"InvalidClassException", "InvalidDnDOperationException", "InvalidKeyException", "InvalidKeySpecException", "InvalidMidiDataException", "InvalidName", "InvalidNameException", "InvalidNameHelper", "InvalidNameHolder", "InvalidObjectException", "InvalidParameterException", "InvalidParameterSpecException", "InvalidSearchControlsException", "InvalidSearchFilterException", "InvalidSeq", "InvalidTransactionException",
		"InvalidValue", "InvocationEvent", "InvocationHandler", "InvocationTargetException", "InvokeHandler", "IstringHelper", "ItalicAction", "ItemEvent", "ItemListener", "ItemSelectable", "Iterator", "JApplet", "JButton", "JCheckBox", "JCheckBoxMenuItem", "JColorChooser",
		"JComboBox", "JComponent", "JDesktopIcon", "JDesktopPane", "JDialog", "JEditorPane", "JFileChooser", "JFrame", "JInternalFrame", "JLabel", "JLayeredPane", "JList", "JMenu", "JMenuBar", "JMenuItem", "JOptionPane",
		"JPanel", "JPasswordField", "JPopupMenu", "JProgressBar", "JRadioButton", "JRadioButtonMenuItem", "JRootPane", "JScrollBar", "JScrollPane", "JSeparator", "JSlider", "JSplitPane", "JTabbedPane", "JTable", "JTableHeader", "JTextArea",
		"JTextComponent", "JTextField", "JTextPane", "JToggleButton", "JToolBar", "JToolTip", "JTree", "JViewport", "JWindow", "JarEntry", "JarException", "JarFile", "JarInputStream", "JarOutputStream", "JarURLConnection", "JobAttributes",
		"Kernel", "Key", "KeyAdapter", "KeyBinding", "KeyEvent", "KeyException", "KeyFactory", "KeyFactorySpi", "KeyListener", "KeyManagementException", "KeyPair", "KeyPairGenerator", "KeyPairGeneratorSpi", "KeySelectionManager", "KeySpec", "KeyStore",
		"KeyStoreException", "KeyStoreSpi", "KeyStroke", "Keymap", "Label", "LabelUI", "LabelView", "LastOwnerException", "LayerPainter", "LayeredHighlighter", "LayoutManager", "LayoutManager2", "LayoutQueue", "LazyInputMap", "LazyValue", "LdapContext",
		"LdapReferralException", "Lease", "LimitExceededException", "Line", "Line2D", "LineBorder", "LineBorderUIResource", "LineBreakMeasurer", "LineEvent", "LineListener", "LineMetrics", "LineNumberInputStream", "LineNumberReader", "LineUnavailableException", "LinkController", "LinkException",
		"LinkLoopException", "LinkRef", "LinkedList", "List", "ListCellRenderer", "ListDataEvent", "ListDataListener", "ListIterator", "ListModel", "ListPainter", "ListResourceBundle", "ListSelectionEvent", "ListSelectionListener", "ListSelectionModel", "ListUI", "ListView",
		"LoaderHandler", "Locale", "LocateRegistry", "LogStream", "LongHolder", "LongLongSeqHelper", "LongLongSeqHolder", "LongSeqHelper", "LongSeqHolder", "LookAndFeel", "LookAndFeelInfo", "LookupOp", "LookupTable", "MARSHAL", "MalformedLinkException", "MalformedURLException",
		"Manifest", "Map", "MarginBorder", "MarshalException", "MarshalledObject", "MatteBorder", "MatteBorderUIResource", "MediaTracker", "MediaType", "Member", "MemoryImageSource", "Menu", "MenuBar", "MenuBarBorder", "MenuBarUI", "MenuComponent",
		"MenuContainer", "MenuDragMouseEvent", "MenuDragMouseListener", "MenuElement", "MenuEvent", "MenuItem", "MenuItemBorder", "MenuItemUI", "MenuKeyEvent", "MenuKeyListener", "MenuListener", "MenuSelectionManager", "MenuShortcut", "MessageDigest", "MessageDigestSpi", "MessageFormat",
		"MetaEventListener", "MetaMessage", "MetalBorders", "MetalButtonUI", "MetalCheckBoxIcon", "MetalCheckBoxUI", "MetalComboBoxButton", "MetalComboBoxEditor", "MetalComboBoxIcon", "MetalComboBoxUI", "MetalDesktopIconUI", "MetalFileChooserUI", "MetalIconFactory", "MetalInternalFrameTitlePane", "MetalInternalFrameUI", "MetalLabelUI",
		"MetalLookAndFeel", "MetalPopupMenuSeparatorUI", "MetalProgressBarUI", "MetalRadioButtonUI", "MetalScrollBarUI", "MetalScrollButton", "MetalScrollPaneUI", "MetalSeparatorUI", "MetalSliderUI", "MetalSplitPaneUI", "MetalTabbedPaneUI", "MetalTextFieldUI", "MetalTheme", "MetalToggleButtonUI", "MetalToolBarUI", "MetalToolTipUI",
		"MetalTreeUI", "Method", "MethodDescriptor", "MidiChannel", "MidiDevice", "MidiDeviceProvider", "MidiEvent", "MidiFileFormat", "MidiFileReader", "MidiFileWriter", "MidiMessage", "MidiSystem", "MidiUnavailableException", "MimeTypeParseException", "MinimalHTMLWriter", "MissingResourceException",
		"Mixer", "MixerProvider", "ModificationItem", "Modifier", "MouseAdapter", "MouseDragGestureRecognizer", "MouseEvent", "MouseInputAdapter", "MouseInputListener", "MouseListener", "MouseMotionAdapter", "MouseMotionListener", "MultiButtonUI", "MultiColorChooserUI", "MultiComboBoxUI", "MultiDesktopIconUI",
		"MultiDesktopPaneUI", "MultiFileChooserUI", "MultiInternalFrameUI", "MultiLabelUI", "MultiListUI", "MultiLookAndFeel", "MultiMenuBarUI", "MultiMenuItemUI", "MultiOptionPaneUI", "MultiPanelUI", "MultiPixelPackedSampleModel", "MultiPopupMenuUI", "MultiProgressBarUI", "MultiScrollBarUI", "MultiScrollPaneUI", "MultiSeparatorUI",
		"MultiSliderUI", "MultiSplitPaneUI", "MultiTabbedPaneUI", "MultiTableHeaderUI", "MultiTableUI", "MultiTextUI", "MultiToolBarUI", "MultiToolTipUI", "MultiTreeUI", "MultiViewportUI", "MulticastSocket", "MultipleDocumentHandlingType", "MultipleMaster", "MutableAttributeSet", "MutableComboBoxModel", "MutableTreeNode",
		"NO_IMPLEMENT", "NO_MEMORY", "NO_PERMISSION", "NO_RESOURCES", "NO_RESPONSE", "NVList", "Name", "NameAlreadyBoundException", "NameClassPair", "NameComponent", "NameComponentHelper", "NameComponentHolder", "NameHelper", "NameHolder", "NameNotFoundException", "NameParser",
		"NameValuePair", "NameValuePairHelper", "NamedValue", "NamespaceChangeListener", "Naming", "NamingContext", "NamingContextHelper", "NamingContextHolder", "NamingContextOperations", "NamingEnumeration", "NamingEvent", "NamingException", "NamingExceptionEvent", "NamingListener", "NamingManager", "NamingSecurityException",
		"NetPermission", "NoInitialContextException", "NoPermissionException", "NoRouteToHostException", "NoSuchAlgorithmException", "NoSuchAttributeException", "NoSuchElementException", "NoSuchObjectException", "NoSuchProviderException", "NodeDimensions", "NoninvertibleTransformException", "NotActiveException", "NotBoundException", "NotContextException", "NotEmpty", "NotEmptyHelper",
		"NotEmptyHolder", "NotFound", "NotFoundHelper", "NotFoundHolder", "NotFoundReason", "NotFoundReasonHelper", "NotFoundReasonHolder", "NotOwnerException", "NotSerializableException", "NumberFormat", "OBJECT_NOT_EXIST", "OBJ_ADAPTER", "OMGVMCID", "ORB", "ObjID", "ObjectChangeListener",
		"ObjectFactory", "ObjectFactoryBuilder", "ObjectHelper", "ObjectHolder", "ObjectImpl", "ObjectInput", "ObjectInputStream", "ObjectInputValidation", "ObjectOutput", "ObjectOutputStream", "ObjectStreamClass", "ObjectStreamConstants", "ObjectStreamException", "ObjectStreamField", "ObjectView", "Observable",
		"Observer", "OctetSeqHelper", "OctetSeqHolder", "OpenType", "Operation", "OperationNotSupportedException", "Option", "OptionDialogBorder", "OptionPaneUI", "OptionalDataException", "OrientationRequestedType", "OriginType", "OutputStream", "OutputStreamWriter", "OverlayLayout", "Owner",
		"PERSIST_STORE", "PKCS8EncodedKeySpec", "PRIVATE_MEMBER", "PUBLIC_MEMBER", "PackedColorModel", "PageAttributes", "PageFormat", "Pageable", "Paint", "PaintContext", "PaintEvent", "PaletteBorder", "PaletteCloseIcon", "Panel", "PanelUI", "Paper",
		"ParagraphAttribute", "ParagraphConstants", "ParagraphView", "ParameterBlock", "ParameterDescriptor", "ParseException", "ParsePosition", "Parser", "ParserCallback", "ParserDelegator", "PartialResultException", "PasswordAuthentication", "PasswordView", "PasteAction", "Patch", "PathIterator",
		"Permission", "PermissionCollection", "Permissions", "PhantomReference", "PipedInputStream", "PipedOutputStream", "PipedReader", "PipedWriter", "PixelGrabber", "PixelInterleavedSampleModel", "PlainDocument", "PlainView", "Point", "Point2D", "Policy", "PolicyError",
		"PolicyHelper", "PolicyHolder", "PolicyListHelper", "PolicyListHolder", "PolicyOperations", "PolicyTypeHelper", "Polygon", "PopupMenu", "PopupMenuBorder", "PopupMenuEvent", "PopupMenuListener", "PopupMenuUI", "Port", "PortableRemoteObject", "PortableRemoteObjectDelegate", "Position",
		"PreparedStatement", "Principal", "PrincipalHolder", "PrintGraphics", "PrintJob", "PrintQualityType", "PrintStream", "PrintWriter", "Printable", "PrinterAbortException", "PrinterException", "PrinterGraphics", "PrinterIOException", "PrinterJob", "PrivateKey", "PrivilegedAction",
		"PrivilegedActionException", "PrivilegedExceptionAction", "ProfileDataException", "ProgressBarUI", "ProgressMonitor", "ProgressMonitorInputStream", "Properties", "PropertyChangeEvent", "PropertyChangeListener", "PropertyChangeSupport", "PropertyDescriptor", "PropertyEditor", "PropertyEditorManager", "PropertyEditorSupport", "PropertyPermission", "PropertyResourceBundle",
		"PropertyVetoException", "ProtectionDomain", "ProtocolException", "Provider", "ProviderException", "Proxy", "ProxyLazyValue", "PublicKey", "PushbackInputStream", "PushbackReader", "PutField", "QuadCurve2D", "RGBImageFilter", "RMIClassLoader", "RMIClientSocketFactory", "RMIFailureHandler",
		"RMISecurityException", "RMISecurityManager", "RMIServerSocketFactory", "RMISocketFactory", "RSAKey", "RSAKeyGenParameterSpec", "RSAPrivateCrtKey", "RSAPrivateCrtKeySpec", "RSAPrivateKey", "RSAPrivateKeySpec", "RSAPublicKey", "RSAPublicKeySpec", "RTFEditorKit", "RadioButtonBorder", "Random", "RandomAccessFile",
		"Raster", "RasterFormatException", "RasterOp", "Reader", "Receiver", "Rectangle", "Rectangle2D", "RectangularShape", "Ref", "RefAddr", "Reference", "ReferenceQueue", "Referenceable", "ReferralException", "ReflectPermission", "Registry",
		"RegistryHandler", "RemarshalException", "Remote", "RemoteCall", "RemoteException", "RemoteObject", "RemoteRef", "RemoteServer", "RemoteStub", "RenderContext", "RenderableImage", "RenderableImageOp", "RenderableImageProducer", "RenderedImage", "RenderedImageFactory", "Renderer",
		"RenderingHints", "RepaintManager", "ReplicateScaleFilter", "Repository", "RepositoryIdHelper", "Request", "RescaleOp", "ResolveResult", "Resolver", "ResourceBundle", "ResponseHandler", "Result", "ResultSet", "ResultSetMetaData", "ReverbType", "Robot",
		"RolloverButtonBorder", "RootPaneContainer", "RootPaneUI", "RoundRectangle2D", "RowMapper", "RuleBasedCollator", "RunTime", "RunTimeOperations", "SQLData", "SQLException", "SQLInput", "SQLOutput", "SQLPermission", "SQLWarning", "SampleModel", "SchemaViolationException",
		"ScrollBarUI", "ScrollPane", "ScrollPaneBorder", "ScrollPaneConstants", "ScrollPaneLayout", "ScrollPaneUI", "Scrollable", "Scrollbar", "SearchControls", "SearchResult", "SecureClassLoader", "SecureRandom", "SecureRandomSpi", "Security", "SecurityPermission", "Segment",
		"Separator", "SeparatorUI", "Sequence", "SequenceInputStream", "Sequencer", "Serializable", "SerializablePermission", "ServantObject", "ServerCloneException", "ServerError", "ServerException", "ServerNotActiveException", "ServerRef", "ServerRequest", "ServerRuntimeException", "ServerSocket",
		"ServiceDetail", "ServiceDetailHelper", "ServiceInformation", "ServiceInformationHelper", "ServiceInformationHolder", "ServiceUnavailableException", "Set", "SetOverrideType", "SetOverrideTypeHelper", "Shape", "ShapeGraphicAttribute", "ShortHolder", "ShortLookupTable", "ShortMessage", "ShortSeqHelper", "ShortSeqHolder",
		"SidesType", "Signature", "SignatureException", "SignatureSpi", "SignedObject", "Signer", "SimpleAttributeSet", "SimpleBeanInfo", "SimpleDateFormat", "SimpleTimeZone", "SinglePixelPackedSampleModel", "SingleSelectionModel", "SizeLimitExceededException", "SizeRequirements", "SizeSequence", "Skeleton",
		"SkeletonMismatchException", "SkeletonNotFoundException", "SliderUI", "Socket", "SocketException", "SocketImpl", "SocketImplFactory", "SocketOptions", "SocketPermission", "SocketSecurityException", "SoftBevelBorder", "SoftReference", "SortedMap", "SortedSet", "Soundbank", "SoundbankReader",
		"SoundbankResource", "SourceDataLine", "SplitPaneBorder", "SplitPaneUI", "Stack", "StateEdit", "StateEditable", "StateFactory", "Statement", "StreamCorruptedException", "StreamTokenizer", "Streamable", "StreamableValue", "StringBufferInputStream", "StringCharacterIterator", "StringContent",
		"StringHolder", "StringReader", "StringRefAddr", "StringSelection", "StringTokenizer", "StringValueHelper", "StringWriter", "Stroke", "Struct", "StructMember", "StructMemberHelper", "Stub", "StubDelegate", "StubNotFoundException", "Style", "StyleConstants",
		"StyleContext", "StyleSheet", "StyledDocument", "StyledEditorKit", "StyledTextAction", "SwingConstants", "SwingPropertyChangeSupport", "SwingUtilities", "SyncFailedException", "SyncMode", "Synthesizer", "SysexMessage", "SystemColor", "SystemException", "SystemFlavorMap", "TCKind",
		"TRANSACTION_REQUIRED", "TRANSACTION_ROLLEDBACK", "TRANSIENT", "TabExpander", "TabSet", "TabStop", "TabableView", "TabbedPaneUI", "TableCellEditor", "TableCellRenderer", "TableColumn", "TableColumnModel", "TableColumnModelEvent", "TableColumnModelListener", "TableHeaderBorder", "TableHeaderUI",
		"TableModel", "TableModelEvent", "TableModelListener", "TableUI", "TableView", "Tag", "TagElement", "TargetDataLine", "TextAction", "TextArea", "TextAttribute", "TextComponent", "TextEvent", "TextField", "TextFieldBorder", "TextHitInfo",
		"TextLayout", "TextListener", "TextMeasurer", "TextUI", "TexturePaint", "Tie", "TileObserver", "Time", "TimeLimitExceededException", "TimeZone", "Timer", "TimerTask", "Timestamp", "TitledBorder", "TitledBorderUIResource", "ToggleButtonBorder",
		"ToggleButtonModel", "TooManyListenersException", "ToolBarBorder", "ToolBarUI", "ToolTipManager", "ToolTipUI", "Toolkit", "Track", "TransactionRequiredException", "TransactionRolledbackException", "Transferable", "TransformAttribute", "Transmitter", "Transparency", "TreeCellEditor", "TreeCellRenderer",
		"TreeControlIcon", "TreeExpansionEvent", "TreeExpansionListener", "TreeFolderIcon", "TreeLeafIcon", "TreeMap", "TreeModel", "TreeModelEvent", "TreeModelListener", "TreeNode", "TreePath", "TreeSelectionEvent", "TreeSelectionListener", "TreeSelectionModel", "TreeSet", "TreeUI",
		"TreeWillExpandListener", "Type", "TypeCode", "TypeCodeHolder", "TypeMismatch", "Types", "UID", "UIDefaults", "UIManager", "UIResource", "ULongLongSeqHelper", "ULongLongSeqHolder", "ULongSeqHelper", "ULongSeqHolder", "UNKNOWN", "UNSUPPORTED_POLICY",
		"UNSUPPORTED_POLICY_VALUE", "URL", "URLClassLoader", "URLConnection", "URLDecoder", "URLEncoder", "URLStreamHandler", "URLStreamHandlerFactory", "UShortSeqHelper", "UShortSeqHolder", "UTFDataFormatException", "UndeclaredThrowableException", "UnderlineAction", "UndoManager", "UndoableEdit", "UndoableEditEvent",
		"UndoableEditListener", "UndoableEditSupport", "UnexpectedException", "UnicastRemoteObject", "UnionMember", "UnionMemberHelper", "UnknownException", "UnknownGroupException", "UnknownHostException", "UnknownObjectException", "UnknownServiceException", "UnknownTag", "UnknownUserException", "UnmarshalException", "UnrecoverableKeyException", "Unreferenced",
		"UnresolvedPermission", "UnsolicitedNotification", "UnsolicitedNotificationEvent", "UnsolicitedNotificationListener", "UnsupportedAudioFileException", "UnsupportedEncodingException", "UnsupportedFlavorException", "UnsupportedLookAndFeelException", "UserException", "Util", "UtilDelegate", "Utilities", "VMID", "VM_ABSTRACT", "VM_CUSTOM", "VM_NONE",
		"VM_TRUNCATABLE", "ValueBase", "ValueBaseHelper", "ValueBaseHolder", "ValueFactory", "ValueHandler", "ValueMember", "ValueMemberHelper", "VariableHeightLayoutCache", "Vector", "VersionSpecHelper", "VetoableChangeListener", "VetoableChangeSupport", "View", "ViewFactory", "ViewportLayout",
		"ViewportUI", "Visibility", "VisibilityHelper", "VoiceStatus", "WCharSeqHelper", "WCharSeqHolder", "WStringValueHelper", "WeakHashMap", "WeakReference", "Window", "WindowAdapter", "WindowConstants", "WindowEvent", "WindowListener", "WrappedPlainView", "WritableRaster",
		"WritableRenderedImage", "WriteAbortedException", "Writer", "WrongTransaction", "X509CRL", "X509CRLEntry", "X509Certificate", "X509EncodedKeySpec", "X509Extension", "ZipEntry", "ZipException", "ZipFile", "ZipInputStream", "ZipOutputStream", "ZoneView", "_BindingIteratorImplBase",
		"_BindingIteratorStub", "_IDLTypeStub", "_NamingContextImplBase", "_NamingContextStub", "_PolicyStub", "_Remote_Stub"
	};
	static final Set stdlibSet = new HashSet(Arrays.asList(stdlib));

	public void setKeywordColor(String keywordColor) {
		this.keywordColor = keywordColor;
	}
	public void setStdlibColor(String color) {
		this.stdlibColor = color;
	}
	public void setCommentColor(String commentColor) {
		this.commentColor = commentColor;
	}
	public void setQuoteColor(String quoteColor) {
		this.quoteColor = quoteColor;
	}

	public String getKeywordColor() { return keywordColor; }
	public String getStdlibColor() { return stdlibColor; }
	public String getCommentColor() { return commentColor; }
	public String getQuoteColor() { return quoteColor; }

	public String processCode(String body) throws IOException {
		PushbackReader in = new PushbackReader(new StringReader(body));
		CharArrayWriter out = new CharArrayWriter();

		out.write("<html>\n<pre>".toCharArray());
		out.write('\n');

		int b;

		while ((b = in.read()) != -1) {
			char c = (char) b;
			if (Character.isLetter(c)) {		// for keywords
				StringBuffer sb = new StringBuffer();
				sb.append(c);
				while ((b = in.read()) != -1) {
					c = (char) b;
					if (c == '\r')
						continue;

					if (!Character.isLetter(c)) {
						in.unread(b);
						break;
					} else {
						sb.append(c);
					}
				}

				if (sb.length() > 0) {
					String str = new String(sb);
					if (isKeyWord(str)) {
						String s = "<font color=\"" + keywordColor + "\">" + str + "</font>";
						out.write(s.toCharArray());
					}
					else if (isStdlib(str)) {
						String s = "<font color=\"" + stdlibColor + "\">" + str + "</font>";
						out.write(s.toCharArray());
					}
					else {
						out.write(str.toCharArray());
					}
				}
			} else if (c == '"') {		// for quoted strings
				StringBuffer sb = new StringBuffer();
				sb.append(c);

				// now read all characters until you encounter next double quote
				while ((b = in.read()) != -1) {
					c = (char) b;
					if (c == '\r')
						continue;

					if (c == '\\') {		// for escaped quotes
						sb.append(c);
						b = in.read();
						if (b != -1)
							sb.append((char) b);
						continue;
					}

					if (c == '"') {
						sb.append(c);
						break;
					} else {
						if (c == '<' || c == '>' || c == '&')
							sb.append("&#" + b + ";");
						else
							sb.append(c);
					}
				}

				if (sb.length() > 0) {
					String str = new String(sb);
					String s = "<font color=\"" + quoteColor + "\">" + str + "</font>";
					out.write(s.toCharArray());
				}
			} else if (c == '/') {		// for comments
				// read the next char, must be either '/' or '*'
				int b2 = in.read();
				c = (char) b2;
				if (c == '\r')
					continue;

				if (c != '/' && c != '*') {
					out.write((char) b);
					in.unread(b2);
				} else {
					if (c == '/') {		// read until next newline
						StringBuffer sb = new StringBuffer();
						sb.append((char) b);
						sb.append(c);

						while ((b = in.read()) != -1) {
							c = (char) b;
							if (c == '\r')
								continue;

							if (c == '\n') {
								in.unread(b);
								break;
							} else {
								sb.append(c);
							}
						}
						if (sb.length() > 0) {
							String str = new String(sb);
							String s = "<font color=\"" + commentColor + "\">" + str + "</font>";
							out.write(s.toCharArray());
						}
					} else if (c == '*') {		// read until next '*/'
						StringBuffer sb = new StringBuffer();
						sb.append((char) b);
						sb.append(c);

						while ((b = in.read()) != -1) {
							c = (char) b;
							if (c == '\r')
								continue;

							if (c == '*') {		// not checking for any errors in syntax
								sb.append(c);
								b2 = in.read();
								c = (char) b2;
								if (c == '\r')
									continue;

								if (c != '/') {
									sb.append(c);
									continue;
								} else {
									sb.append((char) b2);
									break;
								}
							} else {
								sb.append(c);
							}
						}
						if (sb.length() > 0) {
							String str = new String(sb);
							String s = "<font color=\"green\">" + str + "</font>";
							out.write(s.toCharArray());
						}
					}
				}

			} else if (c == '\'') {
				StringBuffer sb = new StringBuffer();
				sb.append(c);
				while ((b = in.read()) != -1) {
					c = (char) b;
					if (c == '\r')
						continue;

					if (c == '\'') {
						sb.append(c);
						break;
					} else {
						sb.append(c);
					}
				}

				if (sb.length() > 0) {
					String str = new String(sb);
					String s = "<font color=\"" + quoteColor + "\">" + str + "</font>";
					out.write(s.toCharArray());
				}

			} else if (c == '\r') {
				continue;
			} else {
				out.write(c);
			}
		}

		out.write("</pre>\n</html>".toCharArray());
		out.write('\n');

		String str = new String(out.toCharArray());

		in.close();
		out.close();

		return str;
	}

	boolean isKeyWord(String str) {
		return keywordSet.contains(str);
//		for (int i = 0; i < keywords.length; i++)
//			if (keywords[i].equals(str))
//				return true;
//		return false;
	}

	boolean isStdlib(String str) {
		return stdlibSet.contains(str);
//		for (int i = 0; i < stdlib.length; i++)
//			if (stdlib[i].equals(str))
//				return true;
//		return false;
	}
}
