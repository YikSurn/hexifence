package aiproj.hexifence;

/*
 *   Referee:
 *      A mediator between two players. It is responsible to initialize
 *      the players and pass the plays between them and terminates the game.
 *      It is the responsibility of the players to check whether they have won and
 *      maintain the board state.
 *
 *   @author lrashidi
 */


public class SimulationReferee implements Piece{

    private static int NUM_GAMES = 1000;
    private static Player P1;
    private static Player P2;
    private static Move lastPlayedMove;

    /*
     * Input arguments: first board size, second path of player1 and third path of player2
     */
    public static void main(String[] args) {
        int blueWins = 0;
        int redWins = 0;
        int winner;

        double startTime = System.currentTimeMillis();
        for (int i=0; i<NUM_GAMES; i++) {
            winner = playGame(args);
            if (winner == BLUE) {
                blueWins++;
            } else if (winner == RED) {
                redWins++;
            }
        }
        double endTime = System.currentTimeMillis();
        double totalTime = endTime - startTime;


        System.out.println("Number of wins (BLUE) : " + blueWins);
        System.out.println("Number of wins (RED)  : " + redWins);
        System.out.println();
        System.out.println("Probability of winning (BLUE) : " + ((float)blueWins/NUM_GAMES));
        System.out.println("Probability of winning (RED)  : " + ((float)redWins/NUM_GAMES));
        System.out.println();
        System.out.println("Games with errors : " + (NUM_GAMES - blueWins - redWins));
        System.out.println();
        System.out.println("Total time taken (milliseconds) : " + totalTime);
        System.out.println("Total time taken (seconds)      : " + (totalTime/1000));
    }

    public static int playGame(String[] args)
    {
        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int dimension = Integer.valueOf(args[0]);
        int boardEmptyPieces=(dimension)*(9*dimension-3);
        // System.out.println("Referee started !");
        try{
            P1 = (Player)(Class.forName(args[1]).newInstance());
            P2 = (Player)(Class.forName(args[2]).newInstance());
        }
        catch(Exception e){
            System.out.println("Error "+ e.getMessage());
            System.exit(1);
        }

        P1.init(Integer.valueOf(args[0]), BLUE);
        P2.init(Integer.valueOf(args[0]), RED);

        int opponentResult=0;
        int turn=1;


                NumberofMoves++;
                lastPlayedMove=P1.makeMove();
        boardEmptyPieces--;
        turn =2;

        while(boardEmptyPieces > 0 && P1.getWinner() == 0 && P2.getWinner() ==0)
        {
        if (turn == 2){

            opponentResult = P2.opponentMove(lastPlayedMove);
            if(opponentResult<0)
            {
                System.out.println("Exception: Player 2 rejected the move of player 1.");
                P1.printBoard(System.out);
                P2.printBoard(System.out);
                System.exit(1);
            }
            else if(P2.getWinner()==0  && P1.getWinner()==0 && boardEmptyPieces>0){
                NumberofMoves++;
                if (opponentResult>0){
                    lastPlayedMove = P1.makeMove();
                    turn = 2;
                }
                else{
                    lastPlayedMove = P2.makeMove();
                    turn=1;
                }
                boardEmptyPieces--;
            }
        }
        else{

            opponentResult = P1.opponentMove(lastPlayedMove);
            if(opponentResult<0)
            {
                System.out.println("Exception: Player 1 rejected the move of player 2.");
                P2.printBoard(System.out);
                P1.printBoard(System.out);
                System.exit(1);
            }
            else if(P2.getWinner()==0  && P1.getWinner()==0 && boardEmptyPieces>0){
                                NumberofMoves++;
                                if (opponentResult>0){
                                        lastPlayedMove = P2.makeMove();
                                        turn = 1;
                                }
                else{
                                        lastPlayedMove = P1.makeMove();
                                        turn=2;
                                }
                                boardEmptyPieces--;
            }
        }

        }


        if(turn == 2){
            opponentResult = P2.opponentMove(lastPlayedMove);
            if(opponentResult < 0) {
            System.out.println("Exception: Player 2 rejected the move of player 1.");
            P1.printBoard(System.out);
            P2.printBoard(System.out);
            System.exit(1);
            }
        } else {
            opponentResult = P1.opponentMove(lastPlayedMove);
            if(opponentResult < 0) {
            System.out.println("Exception: Player 1 rejected the move of player 2.");
            P2.printBoard(System.out);
            P1.printBoard(System.out);
            System.exit(1);
            }
        }

        int winner_P1 = P1.getWinner();
        int winner_P2 = P2.getWinner();



        // System.out.println("Player one (BLUE) indicate winner as: "+ P1.getWinner());
        // System.out.println("Player two (RED) indicate winner as: "+ P2.getWinner());
        // System.out.println("Referee Finished !");

        if (winner_P1 == winner_P2) {
            return winner_P1;
        } else {
            return 0;
        }
    }

}
