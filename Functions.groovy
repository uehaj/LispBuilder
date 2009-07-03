class Functions {

  private static registerClosureFunctions(map) {
    map.with {
      eq = { args, env ->
             assert args.size() == 2
//             println "a0 = "+args[0]+" a1 = "+args[1];
             args[0]==args[1] }

      $if = { args, env, no_automatic_eval_arg ->
        assert args.size() in 2..3
        def cond = args[0].eval(env)
        def thenPart = args[1]
        if (cond) {
          return thenPart.eval(env)
        }
        else if (args.size() == 3) {
          def elsePart = args[2]
          return elsePart.eval(env)
        }
        return false
      }

      quote = { args, env, no_automatic_eval_arg ->
        assert args.size() == 1
        return args[0]
      }

      TRUE = true

      FALSE = false

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
               def result = true
               args.each {
                 result = result && (it != null) 
               }
               result
      }

      or =  { args, env ->
              def result = false
              args.each {
                result = result || (it != null) 
              }
              result
      }

      progn = {args, env, no_automatic_eval_arg ->
               def last
               args.each {
                 last = it.eval(env)
               }
               last
      }

      equals = { args, env ->
                 assert args.size() == 2
                 args[0].equals(args[1])
      }

    }
  }

  private static registerLambdaFunctions(map) {
    def bx = new LispBuilder()
    map.with {

      not = bx.build{lambda
                     ${x}
                     ${$if; x; FALSE; TRUE}
      }

      neq = {lambda
             ${x; y}
             ${not; ${eq; x; y}}
      }

      nullp = bx.build {lambda
                        ${x}
                        ${eq; nil; x}
      }

      append = bx.build {lambda
                         ${a; b}
                         ${$if;
                           ${nullp; a}
                           b;
                           ${cons; ${car; a}; ${append; ${cdr; a}; b}}}
      }
    }
  }

  static registerFunctions(map) {
    registerClosureFunctions(map)
    registerLambdaFunctions(map)
  }

}

