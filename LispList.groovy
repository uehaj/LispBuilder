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

    String.metaClass.getIsSymbol = { false } /* クラスごとメタクラス設定 */

    String.metaClass.eval = { env ->
      if (delegate.isSymbol) { /* シンボルの場合 */
        if (env.containsKey(delegate)) {
          return env[delegate]
        }
        throw new Exception("Symbol not found: "+delegate)
      }
      return delegate
    }

    Integer.metaClass.eval =
      Boolean.metaClass.eval = { env ->
      return delegate
    }

    Object.metaClass.eval = { env ->
      println "Object.eval; ${delegate}"
    }
  }

}

