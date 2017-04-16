
// History:
//	6/15/2001 lmm	Change to require parameter in constructor.

package tinyplanet.docwiz;

import java.util.*;

public class PeekStringTokenizer extends StringTokenizer {
  protected String _nextToken = null;

  public PeekStringTokenizer(String str) {
  	super(str);
    _nextToken = nextToken();
  }

  public String nextToken() {
    //TODO: override this java.util.StringTokenizer method;
    _nextToken = super.nextToken();
    return _nextToken;
  }

  public String peek() {
    return _nextToken;
  }
}