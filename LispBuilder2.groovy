class LispBuilder2 {

  private LispList dummy = [1] as LispList

  def build(Closure c) {
    c.delegate = this
    c.resolveStrategy = Closure.DELEGATE_FIRST;
//    c.resolveStrategy = Closure.DELEGATE_ONLY;
    return c.call()
  }

  def invokeMethod(String m, args) {
    if (m == '$') {
      if (args.length == 0) {
        return null
      }
      def result = new Cons(args[0], null)
      for (int i=1; i<args.length; i++) {
        result.append_(args[i])
      }
      return result
    }
    println "$m $args"
  }

  static makeSymbol(s) {
    def result = new String(s) /* new String instance is reqired (countermasure of 'interned' equality check) */
    result.metaClass.getIsSymbol = { true } /* set closure through metaclass for instance */
    result
  }

  def getProperty(String p) {
    return makeSymbol(p)
  }

}
