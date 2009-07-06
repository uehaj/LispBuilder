class LispList {

  static {
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

    String.metaClass.getIsSymbol = { false } /* set closure through metaclass for class */

    String.metaClass.eval = { env ->
      if (delegate.isSymbol) { /* the String is lisp symbol */
        if (env.containsKey(delegate)) {
          return env[delegate]
        }
        throw new Exception("Symbol not found: "+delegate)
      }
      return delegate
    }

    Object.metaClass.eval = { env ->
      delegate
    }
  }

}

