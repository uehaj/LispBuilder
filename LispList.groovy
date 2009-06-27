interface DotPair {}

class LispListIterator implements Iterator {
  LispList cursor

  LispListIterator(LispList list) {
    cursor = list
  }
  boolean hasNext() {
    cursor != null
  }
  Object next() {
    def result = cursor.first
    cursor = cursor.tail
    return result
  }
  void remove() {
    throw new UnsupportedOperationException("remove not supported");
  }
}

class LispList implements DotPair {
  private _first
  def getFirst() {
    _first instanceof Closure ? _first=_first.call() : _first
  }
  def replaceFirst(a) {
    _first = a
  }
  private _tail
  def getTail() {
    _tail instanceof Closure ? _tail=_tail.call() : _tail
  }
  def replaceTail(a) {
    _tail = a
  }

  LispList(a, b) {
    _first = a;
    _tail = b;
  }

  def getAt(n) {
    assert n >= 0
    n==0 ? first : tail[n-1]
  }

  String toString() {
    def result = new StringBuilder("(")
    def list
    for (list=this; list instanceof LispList; list=list.tail) {
      result << list.first.toString()+(list.tail == null?"":" ");
    }
    if (list != null) {
        result << ". " + list.toString();
    }
    result << ")";
  }

  int size() {
	if (this.tail == null) {
	  1
	}
	else {
	  1 + this.tail.size()
	}
  }

  LispList plus(LispList a) {
	if (this.size() == 0) { //この実装の場合ありえない
	  a
	}
	else if(this.size() == 1) {
	  [first, a] as DotPair
	}
	else {
	  [first, tail+a] as DotPair
	}
  }

  LispList append_(a) {
	if (this.size() == 0) { //この実装の場合ありえない
	  a
	}
	else if(this.size() == 1) {
	  replaceTail(new LispList(a, null))
	}
	else {
	  tail.append_(a)
	}
  }

  boolean equals(LispList a) {
	if (this.tail == a.tail) {
	  return true
	}
	if (this.tail == null && a.tail != null) {
	  return false
	}
	if (this.tail != null && a.tail == null) {
	  return false
	}
	return this.tail == a.tail
  }

  Iterator iterator() {
    return new LispListIterator(this)
  }

  def eval() {
    def evaluator = new LispEvaluator()
    return evaluator.eval(this)
  }

  static {
    println "LispList initialize"
	ExpandoMetaClass.enableGlobally()
	List.metaClass.asType = { Class c->
      if (c == LispList) {
        if (delegate.size() == 0) {
          return null
        }
        return new LispList(delegate.first(), delegate.tail() as LispList)
      }
      else if (c == DotPair) {
        return new LispList(delegate[0], delegate[1])
      }
    }

    Object.metaClass.eval = {
      return delegate
    }

  }

}
