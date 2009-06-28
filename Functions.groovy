class Functions {
  static registerPredefined(Map map) {

    map.with {
      eq = { args ->
             assert args.size() == 2
             args[0]==args[1] }

      not = { args ->
              assert args.size() == 1
              args[0] ? false : true }

      $if = { args, no_automatic_eval_arg ->
        assert args.size() in 2..3
        def cond = args[0].eval(map)
        def thenPart = args[1]
        if (cond) {
          return thenPart.eval(map)
        }
        else if (args.size() == 3) {
          def elsePart = args[2]
          return elsePart.eval(map)
        }
        return false
      }

      quote = { args, no_automatic_eval_arg ->
        assert args.size() == 1
        return args[0]
      }

      TRUE = true

      FALSE = false

      car = { args ->
              assert args.size() == 1
              args.first }

      cdr = { args ->
              assert args.size() == 1
              args.tail }
    }

  }
}

