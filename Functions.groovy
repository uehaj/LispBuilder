class Functions {

  static Map registerPredefined(Map map = [:]) {

    map.with {
      eq = { args, env ->
             assert args.size() == 2
             println "a0 = "+args[0]+" a1 = "+args[1];
             args[0]==args[1] }

      not = { args, env ->
              assert args.size() == 1
              args[0] ? false : true }

      $if = { args, env, no_automatic_eval_arg ->
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

      quote = { args, env, no_automatic_eval_arg ->
        assert args.size() == 1
        return args[0]
      }

      TRUE = true

      FALSE = null

      nil = null

      car = { args, env ->
              assert args.size() == 1
              args[0].car }

      cdr = { args, env ->
              assert args.size() == 1
              args[0].cdr }

      cons = { args, env ->
               assert args.size() == 2
               new Cons(args[0], args[1]) }

      setq = { args, env, no_automatic_eval_arg ->
               assert args.size() == 2
               env[args[0]] = args[1].eval(env)
      }

      and =  { args, env ->
               assert args.size() == 2;
               args[0] != null && args[1] != null
      }

      or =  { args, env ->
              assert args.size() == 2
              args[0] != null || args[1] != null
      }

      progn = {args, env, no_automatic_eval_arg ->
               def last
               args.each {
                 last = it.eval(env)
               }
               last
      }

    }
    return map
  }
}

