
package tinyplanet.docwiz;

import javax.swing.table.*;
import javax.swing.event.*;
import java.util.Vector;
import java.util.Enumeration;

// History:
//	6/21/2001 lmm	- Almost completely rewritten. Uses a new algorithym that has two Vectors
//					holding the tags in order but that point into the various tagsets where
//					the tags and values lie.
//	7/17/2001 lmm	- unknown tags now include all tags not shown on the edit screen. This includes
//					extra return tags, param and exception tags for non-parameters, since tags on fields,
//					author tags on methods, etc. Any tag that javadoc ignores (for that code item) is
//					unknown as well as the few that are allowed but not on the entry screen.
//					- fix bugs related to changing an unknown tags name to become known. This now moves
//					the tag to the other screen but not until you move off the current screen.
//					- a few tags appear on the normal screen and here (eg. param tags for non-parameters)
//					so they can be deleted.

/**
 * Class to display all unknown tags in a single table.
 * @author Simon Arthur
 * @author Lee Meador
 * @version $Id: UnknownTagModel.java,v 1.3 2001/07/17 17:11:18 lmeador Exp $
 */
public class UnknownTagModel extends AbstractTableModel implements TableModel
{
// Attributes:

	/** The default tag name for any new tag being added.
	 */
	public static final String NEW_TAG_TAG = "NewTag";

	/** The default tag value for any new tag being added.
	 */
	public static final String NEW_TAG_VALUE = "...";

  /** This holds all the TagSet's that have been added to this TableModel.
   */
  private Vector tagSets = new Vector();

  /** Names of all unknown tags used within this model.
   *
   */
  Vector tagNames = new Vector();

  /** This holds the information for the table. tagTagSet points to the TagSet that contains
   *	this row in the table's information. tagTagSetLocation points within that TagSet to
   *	identify the individual tag. Together they allow us to provide the JTable with the
   *	column and row information.
   */
  private Vector tagTagSet = new Vector();

  /** This holds the information for the table. tagTagSet points to the TagSet that contains
   *	this row in the table's information. tagTagSetLocation points within that TagSet to
   *	identify the individual tag. Together they allow us to provide the JTable with the
   *	column and row information.
   */
  private Vector tagTagSetLocation = new Vector();

	/**
	 * The CodeComment object upon which this model is based.
	 * @since  06/21/2001 3:42:00 PM
	 */
	private CodeComment codeComment;

	/**
	 * When this is false we are not allowed to fire table change events. Notice the
	 *		non-standard get/set methods.
	 * @since  06/21/2001 4:07:24 PM
	 * @see #allowFireTableChanges
	 * @see #dontFireTableChanges
	 * @see #fireTableChanges
	 */
	protected boolean allowTableChangeToFire = true;

  private static final String TAG_COLUMN_HEADER = "Tag";
  private static final String VALUE_COLUMN_HEADER = "Value";

// Constructors:

	/**
	 * Constructor adds all the unknown tags from the given CodeComment to the model.
	 */
	public UnknownTagModel(CodeComment theCodeComment)
	{
		setCodeComment(theCodeComment);

		Enumeration enum = getCodeComment().getTagEntries();
		while (enum.hasMoreElements()) {
			Object o = enum.nextElement();
			TagSet ts = (TagSet)o;
			addTagSet(ts);		// This filters out the "known" tags but keeps track of
								//	their TagSet's so they can be moved into the right
								//	tagset if the tag is changed to, for example, 'param'
		}
	}

// Methods:

  /**
   * Implements TableModel - How many rows in the JTable.
   *
   * @return the number of rows in the table.
   */
  public int getRowCount() {
//		System.out.println("UnknownTagModel.getRowCount: " + tagTagSet.size());
	return tagTagSet.size();
  }

  /**
   * Implements TableModel - How many columns in the JTable.
   *
   * @return the number of columns in the table.
   */
  public int getColumnCount() {
	return 2;
  }

  /**
   * Implements TableModel - What is the name to go at the top of a column in the JTable.
   *
   * @param columnIndex The table column to check.
   * @return The string to appear in the column heading.
   */
  public String getColumnName(int columnIndex) {
	switch (columnIndex) {
	  case 0:
		return TAG_COLUMN_HEADER;
	  case 1:
	   return VALUE_COLUMN_HEADER;
	}
	return "";
  }

  /**
   * Implements TableModel - what class are the objects in this column.
   *
   * @param columnIndex The table column to check.
   * @return The class of the objects in this column.
   */
  public java.lang.Class getColumnClass(int columnIndex) {
	return String.class;
  }

  /**
   * Implements TableModel - tell if the given cell may be edited.
   *
   * @param rowIndex The table row to check.
   * @param columnIndex The table column to check.
   * @return true if the cell is allowed to be edited.
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
	return true;
  }

  /**
   * Implements TableModel - get the value for the specified cell.
   *
   * @param rowIndex The table row to retrieve.
   * @param columnIndex The table column to retrieve.
   * @return The contents of the cell.
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
//		System.out.println("UnknownTagModel.getValueAt: Entry " + rowIndex + ", " + columnIndex);
	String retVal = "";
	try	 {
		TagSet theSet = (TagSet)tagTagSet.elementAt(rowIndex);
		switch (columnIndex) {
			case 0:
				retVal = theSet.getTagName();
				break;
			case 1:
				retVal = theSet.tagAt(getTagIndex(rowIndex));
				break;
			default:
			   break;
		}
	}
	catch (ArrayIndexOutOfBoundsException e)  {
		/* return the blank string */
	}
//		System.out.println("UnknownTagModel.getValueAt: Exit: " + retVal);
	return retVal;
  }

  /**
   * Implements TableModel - store the value into the specified cell.
   *
   * @param aValue The new value for the cell.
   * @param rowIndex The table row to change.
   * @param columnIndex The table column to change.
   */
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
//		System.out.println("UnknownTagModel.setValueAt: Entry");
		dontFireTableChanges();

		while (rowIndex >= getRowCount()) {
			addTag();
		}

		switch (columnIndex) {
			case 0:
				changeTagName(rowIndex, aValue.toString());
				break;
			case 1:
				TagSet theSet = (TagSet)tagTagSet.elementAt(rowIndex);
				theSet.setTagAt(getTagIndex(rowIndex), aValue.toString());
				break;
			default:
			   break;
		}

		fireTableChanges();
//		System.out.println("UnknownTagModel.setValueAt: Exit");
		return;
  }

  /**
   * Add all the tags in the specified TagSet into this TableModel. The TagSet may not
   *	add any actual tags if it is empty or if it contains "known" tags.
   */
  public void addTagSet(TagSet ts) {
//		System.out.println("UnknownTagModel.addTagSet: Entry");
	if (!tagSets.contains(ts)) {
	  tagSets.insertElementAt(ts, 0);
	  int startTagIndex = firstUnknownTag(ts);
	  for (int i = startTagIndex; (i < ts.size()) ; ++i) {
		addTagToModel(ts, i);
	  }
	}
	if (allowFireTableChanges()) {
//		System.out.println("UnknownTagModel.addTagSet: fire data changed");
		fireTableDataChanged();
	}
//		System.out.println("UnknownTagModel.addTagSet: Exit");
  }

  /**
   * Check whether the supplied TagSet is an unknown tag. "known" tags are those defined by
   *		the JavaDoc specs. They are:
   * <ul>
   *	<li>param
   *	<li>exception
   *	<li>throws
   *	<li>see
   *	<li>since
   *	<li>version
   *	<li>author
   *	<li>deprecated
   *	<li>return
   * </ul>
   *	Any other tag is an "unknown" tag. Return the index of the first tag in the set that
   *	is unknown. For example, if there are multiple "@return" tags, the first one is
   *	considered known and the rest are unknown. For "@param" tags, the ones that match
   *	the function parameters are known and the rest are unknown.
   * <p>
   *	The actual values returned reflect these facts:
   * <pre>
   *                Class or
   *                Interface   Field   Method  Constructor
   *    param                             y(d)      y(d)
   *    exception                         y(d)      y(d)
   *    throws                            y(d)      y(d)
   *    see             y(*)      y(*)    y(*)      y(*)
   *    since           y(1)      y(X)    y(1)      y(1)
   *    version         y(1)
   *    author          y(*)
   *    deprecated      y(1)      y(1)    y(1)      y(1)
   *    return                            y(0/1)
   *    serial                    y(X)
   *    serialData                        y(X)      y(X)
   *    serialField               y(X)
   *
   *    y - JavaDoc spec says code item allows the tag.
   *    * - all tags of this type are handled on normal DocWiz screen.
   *    1 - one tag of this type is handled on the normal DocWiz screen.
   *    0/1 - one tag of this type is handled on the normal DocWiz screen
   *            if the method does not 'return' void. If 'void', no tags
   *            of this type are handled there.
   *    d - tags for defined parameters and exceptions are handled on the
   *            normal DocWiz screen. (The comment but not the name of other,
   *            non-defined tags are also handled there.)
   *    X - tag is not handled on normal DocWiz screen.
   * </pre>
   *
   * @param ts The TagSet to test.
   * @return the index into the tag set of the first tag that is unknown. If all are known
   *	then the return value is greater than the index of the last one. (For example, it
   *	would be the size of the tag set or greater.)
   */
  public static int firstUnknownTag(TagSet ts)
  {
	String tagName = ts.getTagName();
	CommentableCode theCode = ts.getCodeToTag();
	if (theCode instanceof tinyplanet.docwiz.Field) {
		// First rule out the ones that we handle fully on the normal screen
		if ("see".equals(tagName) ||
			/* "since".equals(tagName) || */	// leave in case it gets added later
			"deprecated".equals(tagName)
			) {
		   return ts.size();					// since all are known
		}
	}
	else if (theCode instanceof tinyplanet.docwiz.Class ||
			 theCode instanceof tinyplanet.docwiz.Interface) {
		// First rule out the ones that we handle fully on the normal screen
		if ("see".equals(tagName) ||
			"author".equals(tagName)
		 ) {
		   return ts.size();					// since all are known
		}
		// Handle the ones that we handle only ONE tag of that type.
		else if ("version".equals(tagName) ||
			"since".equals(tagName) ||
			"deprecated".equals(tagName)
			) {
			return 1;							// since only the first one is known
		}
	}
	else if (theCode instanceof tinyplanet.docwiz.Method) {
		// First rule out the ones that we handle fully on the normal screen
		if ("see".equals(tagName)) {
		   return ts.size();					// since all are known
		}
		// Handle the ones that we handle only ONE tag of that type.
		else if ("since".equals(tagName) ||
			"deprecated".equals(tagName)
			) {
			return 1;							// since only the first one is known
		}
		// Handle @return as a special case since it may or may-not be handled on the
		//	normal screen. One is handled on the normal screen if the return type is
		//	not "void".
		else if ("return".equals(tagName)) {
			if ("void".equals(((Method)theCode).getType())) {
			   return 0;					// since none are known
			}
			else  {
				return 1;					// since one is known
			}
		}
		// Handle the ones that have a variable number of "real" entries. We need to
		//	show the ones that are beyond that point.
		else if ("param".equals(tagName) ||
			"exception".equals(tagName) ||
			"throws".equals(tagName)
			)  {
			return ((TwoPartTagModel)ts).getNameVector().size();	// since the real parameters are known
		}
	}
	else if (theCode instanceof tinyplanet.docwiz.Constructor) {
		// First rule out the ones that we handle fully on the normal screen
		if ("see".equals(tagName)) {
		   return ts.size();					// since all are known
		}
		// Handle the ones that we handle only ONE tag of that type.
		else if ("since".equals(tagName) ||
			"deprecated".equals(tagName)
			) {
			return 1;							// since only the first one is known
		}
		// Handle the ones that have a variable number of "real" entries. We need to
		//	show the ones that are beyond that point.
		else if ("param".equals(tagName) ||
			"exception".equals(tagName) ||
			"throws".equals(tagName)
			)  {
			return ((TwoPartTagModel)ts).getNameVector().size();	// since the real parameters are known
		}
	}
	return 0;									// since all are others are unknown
  }

	/**
	 * Add a new tag to this model using the default name and value.
	 *
	 * @since 06/21/2001 3:49:36 PM
	 * @see #addTag(String, String)
	 */
	public void addTag()
	{
		addTag(NEW_TAG_TAG, NEW_TAG_VALUE);
		return;
	}

	/**
	 * Add a new tag to this model. This implies that we must either find the correct TagSet
	 *		and add it to that TagSet or we must create a new TagSet, hook it into the right
	 *		CommentableCode object and then add the new tag to it. Note that if the new tag
	 *		is a "known" tag it will still be added here. But when we come back later the
	 *		"known" tag will be filtered out of here.
	 *
	 * @since 06/21/2001 1:22:15 PM
	 * @param
	 * @return
	 */
	public void addTag(String type, String value)
	{
//		System.out.println("UnknownTagModel.addTag(" + type + ", " + value + "): Entry");
		TagSet theSet = addTagAndTagSet(type, value);
		if (theSet != null && theSet.size() > 0) {
			// Find added tag and, if found, remove old one and hook in new one.
			for (int i = 0 ; (i < theSet.size()) ; ++i) {
				if (theSet.tagAt(i).equals(value)) {
					addTagToModel(theSet, i);
					if (allowFireTableChanges()) {
//						System.out.println("UnknownTagModel.addTag: fire insertion(" + i);
						fireTableRowsInserted(i, i);
					}
					break;
				}
			}
		}
//		System.out.println("UnknownTagModel.addTag: Exit");
		return;
	}

	/**
	 * Add the tag at the given index in the given TagSet to the model arrays.
	 *
	 * @since 06/21/2001 3:21:43 PM
	 * @param theSet The TagSet that holds the tag in question. This should already be
	 *		linked into the array of tagsets in this class and the code object.
	 * @param index The index in <code>theSet</code> of the tag to be added.
	 */
	private void addTagToModel(TagSet theSet, int index)
	{
//		System.out.println("UnknownTagModel.addTagToModel: Entry: " + theSet.getTagName() + " = " + theSet.tagAt(index));
		tagTagSet.addElement(theSet);			// Add an entry to the Vectors for my model
		tagTagSetLocation.addElement(new Integer(index));
//		System.out.println("UnknownTagModel.addTagToModel: Exit");
		return;
	}

	/**
	 * Change the tag name for a tag in this model. This implies that the entry may have to
	 *		be removed from one TagSet and either added to another or have a new TagSet
	 *		created to hold it.
	 *
	 * @since 06/21/2001 2:06:42 PM
	 * @param rowIndex The row in which the tag resides in the model.
	 * @param newType The new name for the tag.
	 */
	private void changeTagName(int rowIndex, String newType)
	{
//		System.out.println("UnknownTagModel.changeTagName: Entry rowIndex = " + rowIndex + ", type = " + newType);
		TagSet oldSet = (TagSet)tagTagSet.elementAt(rowIndex);
		String oldType = oldSet.getTagName();
		if (!newType.equals(oldType)) {

			String value = oldSet.tagAt(getTagIndex(rowIndex));

			TagSet theSet = addTagAndTagSet(newType, value);
			if (theSet != null && theSet.size() > 0) {

				// Find added tag and, if found, remove old one and hook in new one.
				for (int i = 0 ; (i < theSet.size()) ; ++i) {
					System.out.println("UnknownTagModel.changeTagName: tag["+i+" = <" + theSet.tagAt(i) + ">");
					if (theSet.tagAt(i).trim().equals(value.trim())) {
						removeTagSetOrElementAt(oldSet, rowIndex);

						tagTagSet.setElementAt(theSet, rowIndex);	// Replace entry in the Vectors for my model
						tagTagSetLocation.setElementAt(new Integer(i), rowIndex);
						System.out.println("UnknownTagModel.changeTagName: change at " + i);
						break;
					}
				}

				System.out.println("UnknownTagModel.changeTagName: " + oldType + " --> " + newType);
				System.out.println("UnknownTagModel.changeTagName: oldValue = " + value);
//				System.out.println("UnknownTagModel.changeTagName: theSet.size() = " + theSet.size());
//				System.out.println("UnknownTagModel.changeTagName: new index = " + getTagIndex(rowIndex));
//				System.out.println("UnknownTagModel.changeTagName: newValue = " + theSet.tagAt(getTagIndex(rowIndex)));
			}
//			else  {
//				System.out.println("UnknownTagModel.changeTagName: " + oldType + " --> " + newType);
//				System.out.println("UnknownTagModel.changeTagName: oldValue = " + value);
//				System.out.println("UnknownTagModel.changeTagName: newValue = none due to error adding");
//			}
		}

//		System.out.println("UnknownTagModel.changeTagName: Exit");
		return;
	}

	/**
	 * Add the value to the TagSet for the given type. If the TagSet is missing,
	 *		make a new TagSet, add it to the correct code object and then add
	 *		the value to it.
	 *
	 * @since 06/21/2001 2:40:07 PM
	 * @param type The tag name to be added.
	 * @param value The tag value to be added.
	 * @return The TagSet that was created or found and added to or null if an error occurred
	 *		while trying to create a new TagSet.
	 */
	private TagSet addTagAndTagSet(String type, String value)
	{
//		System.out.println("UnknownTagModel.addTagAndTagSet: Entry");
		TagSet theSet = getTagSet(type);			// Find the correct TagSet if it exists.
		if (theSet == null) {
			theSet = TagModelFactory.tagSetCreateFromLine(getCodeComment().getCodeToComment(), type + " " + value);
			if (theSet != null) {
														// Create a new TagSet with the new tag in it
				theSet.addValue(value);					// Add new value
				if (theSet.size() > 0) {				// .. and see if it got added.
					tagSets.insertElementAt(theSet, 0);
														// Add it to my list of all used TagSets
					getCodeComment().addTagSet(type, theSet);
														// Add it to the object for the whole javadoc comment
														//	 on the method, class, etc.
				}
				else  {
					theSet = null;		// signal failure to add
				}
			}
		}
		else  {
			theSet.addValue(value);					// or add new value to existing TagSet.
		}
//		System.out.println("UnknownTagModel.addTagAndTagSet: Exit = " + theSet);
		return theSet;
	}

	/**
	 * Find the TagSet matching the supplied tag name from the list of TagSet's that
	 *		are used in this model and return it.
	 *
	 * @since 06/21/2001 2:10:43 PM
	 * @param type The name of the tags we are looking for.
	 * @return The TagSet containing the tags with the given name or null if none currently
	 *		exists.
	 */
	private TagSet getTagSet(String type)
	{
		Enumeration enum = tagSets.elements();
		while (enum.hasMoreElements()) {
			TagSet ts = (TagSet)enum.nextElement();
			if (ts.getTagName().equals(type)) {
				return ts;
			}
		}
		return null;
	}

  public void removeElementAt(int rowIndex) {
		TagSet theSet = (TagSet)tagTagSet.elementAt(rowIndex);
		removeTagSetOrElementAt(theSet, rowIndex);

		tagTagSet.removeElementAt(rowIndex);	// Remove entry in the Vectors for my model
		tagTagSetLocation.removeElementAt(rowIndex);

		if (allowFireTableChanges()) {
//			System.out.println("UnknownTagModel.removeElementAt: fire deleted " + rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}

		return;
  }

	/**
	 * Remove the specified element from the specified TagSet or, if there is only one
	 *		element currently in that TagSet, remove the whole TagSet.
	 *
	 * @since 06/21/2001 2:33:25 PM
	 * @param theSet The TagSet to remove an element from or to remove in entirity.
	 * @param location The index, within the TagSet, of the element to remove.
	 */
	private void removeTagSetOrElementAt(TagSet theSet, int rowIndex)
	{
//		System.out.println("UnknownTagModel.removeTagSetOrElementAt: Entry rowIndex = " + rowIndex);
		if (theSet.size() == 1) {
			// remove the whole set from here and from the code
			tagSets.removeElement(theSet);
			getCodeComment().clearTagEntries(theSet.getTagName());
		}
		else  {
			int removedIndex = getTagIndex(rowIndex);
			int oldSize = theSet.size();
			theSet.removeTagAt(removedIndex);
			if (oldSize > theSet.size()) {		// If it really went away ... some don't
				// Now, find everything in the same tag set with indexes higher and move them down
				//	to account for the removed one.
				for (int i = 0 ; (i < getRowCount()) ; ++i) {
					int elementIndex = getTagIndex(i);
					if (tagTagSet.elementAt(i) == theSet &&
							elementIndex > removedIndex) {
						tagTagSetLocation.setElementAt(new Integer(elementIndex-1), i);
					}
				}
			}
		}
//		System.out.println("UnknownTagModel.removeTagSetOrElementAt: Exit");
		return;
	}

	/**
	 * Convert a rowIndex in the model to a tag index into the associated TagSet.
	 *
	 * @since 06/21/2001 3:14:07 PM
	 * @param The row index to convert.
	 * @return the converted row index to use as an index into the associated TagSet.
	 */
	private int getTagIndex(int rowIndex)
	{
//		System.out.println("UnknownTagModel.getTagIndex: Entry rowIndex = " + rowIndex);
//		if (rowIndex >= tagTagSetLocation.size()) {
//			System.out.println("UnknownTagModel.getTagIndex: rowIndex is "+ rowIndex + " which is too large");
//		}
		int retVal = ((Integer)tagTagSetLocation.elementAt(rowIndex)).intValue();
//		System.out.println("UnknownTagModel.getTagIndex: Exit			= " + retVal);
		return retVal;
	}

//	/**	   *
//	 * @param l
//	 */
//	public synchronized void removeTableModelListener(TableModelListener l) {
//	  if (tableModelListeners != null && tableModelListeners.contains(l)) {
//		Vector v = (Vector) tableModelListeners.clone();
//		v.removeElement(l);
//		tableModelListeners = v;
//	  }
//	}
//
//	/**	   *
//	 * @param l
//	 */
//	public synchronized void addTableModelListener(TableModelListener l) {
//	  Vector v = tableModelListeners == null ? new Vector(2) : (Vector) tableModelListeners.clone();
//	  if (!v.contains(l)) {
//		v.addElement(l);
//		tableModelListeners = v;
//	  }
//	}
//	 /**	 */
//	 private transient Vector tableModelListeners;

// get/set methods:

	/** Get the CodeComment object to which all these are attached.
	 *
	 * @return The CodeComment for the current entity. The current value of the attribute {@link codeComment }.
	 * @since  06/21/2001 3:42:06 PM
	 */
	protected CodeComment getCodeComment()
	{
		return this.codeComment;
	}

	/**
	 * Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link codeComment }.
	 * @since  06/21/2001 3:42:06 PM
	 */
	protected void setCodeComment(CodeComment val)
	{
		this.codeComment = val;
	}


	/**
	 * NON-Standard getter method retrieves current value of the attribute.
	 *
	 * @return The current value of the attribute {@link allowTableChangeToFire }.
	 * @since  06/21/2001 4:07:34 PM
	 */
	protected boolean allowFireTableChanges()
	{
		return this.allowTableChangeToFire;
	}

	/**
	 * NON-Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link allowTableChangeToFire }.
	 * @since  06/21/2001 4:07:34 PM
	 */
	protected void fireTableChanges()
	{
		this.allowTableChangeToFire = true;
	}

	/**
	 * NON-Standard setter method changes the current value of the attribute.
	 *
	 * @param val The new value of the attribute {@link allowTableChangeToFire }.
	 * @since  06/21/2001 4:07:34 PM
	 */
	protected void dontFireTableChanges()
	{
		this.allowTableChangeToFire = false;
	}

}