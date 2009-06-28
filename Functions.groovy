class Functions {
  static registerPredefined(Map map, Map special) {

    map.with {
      eq = { arg -> arg[0]==arg[1] }
      not = { arg -> arg[0] ? false : true }
      delegate.'IF' = { arg ->
                        this.metaClass.getIsSpecial = { true }
                        assert arg.size() in 2..3
                        println "if[0]=>"+arg[0].class
                        println "if[1]=>"+arg[0].eval().class
                        if (arg[0].eval()) {
                          return arg[1].eval()
                        }
                        else if (arg.size() == 3) {
                          return arg[2].eval()
                        }
                        return false
      }
      TRUE = true
      FALSE = false
      car = { arg -> arg.first }
      cdr = { arg -> arg.tail }

      
    }
    special.with {
      delegate.'IF' = true
      define = true
    }
  }
}

