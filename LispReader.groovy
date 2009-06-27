class LispReader {
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
          def LispList savedReadBuffer = readBuffer
          readStart()
          elem.call()
          elem = readResult()
          readBuffer = savedReadBuffer
        }
        if (readBuffer == null) {
          readBuffer = [elem] as LispList
        }
        else {
          readBuffer.append_(elem)
        }
      }
      else {
        getProperty(m)
        def elem = args[0]
        if (elem instanceof Closure) {
          def LispList savedReadBuffer = readBuffer
          readStart()
          elem.call()
          def subList = readResult()
          readBuffer = savedReadBuffer
          readBuffer.append_(subList)
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
      if (p.startsWith('$')) {
        p = Integer.parseInt(p.substring(1,p.size()))
      }
      if (readBuffer == null) {
        readBuffer = [p] as LispList
      }
      else {
        readBuffer.append_(p)
      }
    }
    catch (Exception e) {
      e.printStackTrace()
    }
  }

  static {
    println "LispReader initialize"
	ExpandoMetaClass.enableGlobally()
	Integer.metaClass.positive = { it->
      println "it=${it}"
    }
  }

}
