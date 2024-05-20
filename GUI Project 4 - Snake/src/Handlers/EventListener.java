package Handlers;

public interface EventListener {

    // GameManager -> UserPanel:
    void updateScore(ConsumptionEvent evt);
    void startGameNoButton();                   // Start game without Start button (by pressing movement key)


    // Game -> GameManager:
    void gameOver(ImpactEvent evt);


    // UserPanel -> GameManager:
    void changeTick(TickEvent evt);
    void startGame(NewGameEvent evt);
    boolean getGameState();                    // Get game state (running or not)
    void forceGameOver();                      // Force game over (by pressing New Game button)
    
    
    // scrapped:
    // EventListener newGame(EventListener graphics);   // Graphics -> Logic
    // void gameWon();                                  // Logic -> Graphics

}
