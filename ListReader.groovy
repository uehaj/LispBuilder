import LispList

class ListReader {
  private LispList readBuffer

  def readStart() {
    readBuffer = null
  }

  def readResult() {
    readBuffer
  }

  def invokeMethod(String m, args) {
    try {
      if (m == '$') {
        def elem = args[0]
        if (elem instanceof Closure) {
          ListReader reader = new ListReader()
          elem.delegate = reader
          elem.resolveStrategy = Closure.DELEGATE_FIRST;
          elem.call()
          elem = reader.readResult()
        }
        if (readBuffer == null) {
          readBuffer = [elem] as Cons
        }
        else {
          readBuffer.append_(elem)
        }
      }
      else {
        getProperty(m)
        def elem = args[0]
        if (elem instanceof Closure) {
          ListReader reader = new ListReader()
          elem.delegate = reader
          elem.resolveStrategy = Closure.DELEGATE_FIRST;
          elem.call()
          elem = reader.readResult()
          readBuffer.append_(elem)
        }
        else throw new Error("list syntax error: elem=$elem")
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

  def getProperty(String p) {
    try {
      def value = p
      if (p.startsWith('$')) {
        value = Integer.parseInt(p.substring(1,p.size()))
      }
      if (readBuffer == null) {
        readBuffer = [value] as Cons
      }
      else {
        readBuffer.append_(value)
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

  static {
    println "ListReader initialize"
	ExpandoMetaClass.enableGlobally()
	Integer.metaClass.positive = { it->
      println "it=${it}"
    }
  }

}
