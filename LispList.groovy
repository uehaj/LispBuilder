class LispListIterator implements Iterator {
  LispList cursor

  LispListIterator(LispList list) {
    cursor = list
  }
  boolean hasNext() {
    cursor != null
  }
  Object next() {
    def result = cursor.car
    cursor = cursor.cdr
    return result
  }
  void remove() {
    throw new UnsupportedOperationException("remove not supported");
  }
}

class LispList {

  static {
    println "Cons initialize"
	ExpandoMetaClass.enableGlobally()
	List.metaClass.asType = { Class c->
      if (c == LispList) {
        if (delegate.size() == 0) {
          return null
        }
        return new Cons(delegate.first(), delegate.tail() as LispList)
      }
      else if (c == Cons) {
        return new Cons(delegate[0], delegate[1])
      }
    }

    String.metaClass.getIsSymbol = { false }

    String.metaClass.eval = { env ->
      if (delegate.isSymbol) {
        return env[delegate]
      }
      return delegate
    }

    Integer.metaClass.eval =
      Boolean.metaClass.eval = { env ->
      return delegate
    }

  }

}

