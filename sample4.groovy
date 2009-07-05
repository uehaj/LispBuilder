def bx = new LispBuilder()

bx.build{progn
         ${setq; x; $(new javax.swing.JFrame())}
         ${setq; pane; ${call; x; $"getContentPane"}}
         ${call; pane; $"add"; $(new javax.swing.JLabel("hello"))}
         ${call; x; $"show"}
}.eval()
