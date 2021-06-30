import java.util.Scanner;
import java.util.InputMismatchException;
import java.awt.*;
import javax.swing.JFrame;
public class connect4{
    private static boolean end; 
    private static boolean reset; 
    private static int yourwin; 
    private static int cpuwin;

    public connect4(){
        end = false; 
        reset = false;
        yourwin = 0; 
        cpuwin = 0;
    }

    //main method to run when playing game
    public static void main(String args[]){
        int[] height = {5,5,5,5,5,5,5};
        char[][] board = {{'○','○','○','○','○','○','○'},
                {'○','○','○','○','○','○','○'},
                {'○','○','○','○','○','○','○'},
                {'○','○','○','○','○','○','○'},
                {'○','○','○','○','○','○','○'},
                {'○','○','○','○','○','○','○'}};

        //graphics
        JFrame frame = new JFrame("My Drawing");
        Canvas canvas = new Canvas();
        canvas.setSize(1500, 800);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        Graphics g = canvas.getGraphics();
        canvas.paint(g);
        while(reset == false){
            while(end == false){ 
                yourTurn(board, height); 
                if(end == true){
                    break; 
                }
                aiTurn(board, height);
                if(end == true){
                    break; 
                }
                drawBoard(board, g);
            }
            Scanner scan = new Scanner(System.in);
            System.out.println("Would you like to play again (yes/no)? You have " + yourwin + " win(s) and the computer has " + cpuwin + " win(s)."); 
            String answer = scan.nextLine(); 
            if(answer.equalsIgnoreCase("yes")){
                clearBoard(board); 
                for(int i = 0; i < height.length; i++){
                    height[i] = 5; 
                }
                end = false; 
            } else {
                System.out.println("Thanks for playing!"); 
                yourwin = 0; 
                cpuwin = 0; 
                break; 
            }
        }
    }
    
    public static void clearBoard(char[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                board[i][j] = '○';  
            }
        }
    }
    
    //graphics
    public static void drawBoard(char[][] board, Graphics g) {
        g.setColor(Color.black);
        g.fillRect(375, 75, 750, 650);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if(board[i][j]=='○'){
                    g.setColor(Color.white);
                }
                if(board[i][j]=='X'){
                    g.setColor(Color.red);
                }
                if(board[i][j]=='Y'){
                    g.setColor(Color.yellow);
                }
                g.fillOval(400 + 100 * j, 100 + 100 * i, 100, 100);
            }
        }
    }

    public static void printBoard(char[][] board){
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                System.out.print(board[i][j]);
            }
            System.out.println("");
        }
    }

    public static void updateBoard(char[][] board, char symbol, int column, int[] height){
        if(column > 6 || column < 0){
            System.out.println("Please enter a column between 1 and 7: "); 
            yourTurn(board, height); 
        } else if(height[column] < 0){
            System.out.println("Invalid move, column is full");
            yourTurn(board, height);
        } else{
            board[height[column]][column] = symbol;
            height[column]--;
            printBoard(board); 
        }
    }

    public static void yourTurn(char[][] board, int[] height){
        Scanner scan = new Scanner(System.in);
        System.out.println("Which column (1-7) would you like to place a chip in?"); 
        printBoard(board); 
        int column = -1; 
        while(column < 0){
            try {
                column = scan.nextInt() - 1;
                continue; 
            } catch (InputMismatchException e) {
                String bad_input = scan.next();
                System.out.println("Please enter a number: ");
            }
        }
        updateBoard(board, 'X', column, height);
        checkWin(board); 
        checkTie(height); 
    }

    //cpu move. contains possible win and counter movs and a random move as a saety net
    public static void aiTurn(char[][] board, int[] height){
        System.out.println("CPU's turn: ");
        int numx = 0;
        int numy = 0;
        boolean played = false;
        //vertical counter and win moves
        if(played != true){
            outerloop:
            for(int i = 0; i < board[0].length; i++){
                numx = 0; 
                numy = 0; 
                for(int j = board.length - 1; j >= 0; j--){
                    if(board[j][i] == 'X'){
                        numx++;
                        numy = 0; 
                    } else if(board[j][i] == 'Y'){
                        numx = 0; 
                        numy++; 
                    } else if(board[j][i] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(height[i] >= 1){
                        if(numy == 3 && board[j - 1][i] == '○'){
                            updateBoard(board, 'Y', i, height);
                            played = true;  
                            break outerloop; 
                        } else if(numx == 3 && board[j - 1][i] == '○'){
                            updateBoard(board, 'Y', i, height);
                            played = true; 
                            break outerloop; 
                        }
                    }
                }
            }
        }
        //horizontal counter and win moves to the right
        if(played != true){
            outerloop:
            for(int i = 0; i < board.length; i++){
                numx = 0; 
                numy = 0; 
                for(int j = 0; j < board[0].length; j++){
                    if(board[i][j] == 'X'){
                        numx++;
                        numy = 0; 
                    } else if(board[i][j] == 'Y'){
                        numx = 0; 
                        numy++; 
                    } else if(board[i][j] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(j != 6){
                        if(numy == 3 && i == 5 && board[i][j + 1] == '○'){
                            updateBoard(board, 'Y', j + 1, height); 
                            played = true; 
                            break outerloop;
                        } else if (numx == 3 && i == 5 && board[i][j + 1] == '○'){
                            updateBoard(board, 'Y', j + 1, height); 
                            played = true; 
                            break outerloop;
                        }
                        if(i < 5){
                            if(numy == 3 && board[i][j + 1] == '○' && board[i + 1][j + 1] != '○' && height[j + 1] >= 0){
                                updateBoard(board, 'Y', j + 1, height); 
                                played = true; 
                                break outerloop; 
                            } else if (numx == 3 && board[i][j + 1] == '○' && board[i + 1][j + 1] != '○' && height[j + 1] >= 0){
                                updateBoard(board, 'Y', j + 1, height);
                                played = true; 
                                break outerloop; 
                            }
                        }
                    }                 
                }
            }
        }
        //horizontal counter and win moves to the left
        if(played != true){
            outerloop:
            for(int i = 0; i < board.length; i++){
                numx = 0; 
                numy = 0; 
                for(int j = board[0].length - 1; j >= 0; j--){
                    if(board[i][j] == 'X'){
                        numx++;
                        numy = 0; 
                    } else if(board[i][j] == 'Y'){
                        numx = 0; 
                        numy++; 
                    } else if(board[i][j] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(j != 0){
                        if(numy == 3 && i == 5 && board[i][j - 1] == '○'){
                            updateBoard(board, 'Y', j - 1, height); 
                            played = true; 
                            break outerloop;
                        } else if (numx == 3 && i == 5 && board[i][j - 1] == '○'){
                            updateBoard(board, 'Y', j - 1, height); 
                            played = true; 
                            break outerloop;
                        }
                        if(i < 5){
                            if(numy == 3 && board[i][j - 1] == '○' && board[i + 1][j - 1] != '○' && height[j - 1] >= 0){
                                updateBoard(board, 'Y', j - 1, height); 
                                played = true; 
                                break outerloop; 
                            } else if(numx == 3 && board[i][j - 1] == '○' && board[i + 1][j - 1] != '○' && height[j - 1] >= 0){
                                updateBoard(board, 'Y', j - 1, height);
                                played = true; 
                                break outerloop; 
                            }
                        }
                    }
                }
            }
        }
        //diagonal counter and win moves
        //diagonal counter and win for bottom right - 1 to topleft
        int row; 
        int col;
        int start; 
        int columnstart = 6;
        if(played != true){
            outerloop:
            for(start = 0; start < 3; start++){
                numx = 0; 
                numy = 0; 
                columnstart--; 
                for(row = 5, col = columnstart; row >= start && col >= 0; row--, col--){
                    if(board[row][col] == 'X'){
                        numx++; 
                        numy = 0;
                    } else if(board[row][col] == 'Y'){
                        numy++; 
                        numx = 0; 
                    } else if(board[row][col] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(row != 0 && col != 0){
                        if(numy == 3 && board[row - 1][col - 1] == '○' && board[row][col - 1] != '○'){
                            updateBoard(board, 'Y', col - 1, height); 
                            played = true; 
                            break outerloop;
                        } else if(numx == 3 && board[row - 1][col - 1] == '○' && board[row][col - 1] != '○'){
                            updateBoard(board, 'Y', col - 1, height); 
                            played = true; 
                            break outerloop;
                        }
                    }
                }
            }
        }
        //diagonal check for bottom right to top left + 1
        int rowstart = 6; 
        if(played != true){
            outerloop:
            for(start = 0; start < 3; start++){
                numx = 0; 
                numy = 0; 
                rowstart--; 
                for(row = rowstart, col = 6; row >= 0 && col >= start + 1; row--, col--){
                    if(board[row][col] == 'X'){
                        numx++; 
                        numy = 0;
                    } else if(board[row][col] == 'Y'){
                        numy++; 
                        numx = 0; 
                    } else if(board[row][col] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(row != 0){
                        if(numy == 3 && board[row - 1][col - 1] == '○' && board[row][col - 1] != '○'){
                            updateBoard(board, 'Y', col - 1, height); 
                            played = true; 
                            break outerloop;
                        } else if(numx == 3 && board[row - 1][col - 1] == '○' && board[row][col - 1] != '○'){
                            updateBoard(board, 'Y', col - 1, height); 
                            played = true; 
                            break outerloop;
                        }
                    }
                }
            }
        }
        //diagonal counter and win for bottom left to top right - 1
        int columnend = 2;
        if(played != true){
            outerloop:
            for(start = 0; start < 3; start++){
                numx = 0; 
                numy = 0;
                columnend++; 
                for(row = start + 3, col = 0; row >= 0 && col < columnend; row--, col++){
                    if(board[row][col] == 'X'){
                        numx++; 
                        numy = 0;
                    } else if(board[row][col] == 'Y'){
                        numy++; 
                        numx = 0; 
                    } else if(board[row][col] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(row != 0){
                        if(numy == 3 && board[row - 1][col + 1] == '○' && board[row][col + 1] != '○'){
                            updateBoard(board, 'Y', col + 1, height); 
                            played = true; 
                            break outerloop; 
                        } else if(numx == 3 && board[row - 1][col + 1] == '○' && board[row][col + 1] != '○'){
                            updateBoard(board, 'Y', col + 1, height);  
                            played = true; 
                            break outerloop; 
                        }
                    }
                }
            }
        }
        //diagonal counter and win for bottom left + 1 to top right
        int rowend = -1; 
        if(played != true){
            outerloop:
            for(start = 0; start < 3; start++){
                numx = 0; 
                numy = 0; 
                rowend++; 
                for(row = 5, col = start + 1; row > rowend && col <= 6; row--, col++){
                    if(board[row][col] == 'X'){
                        numx++; 
                        numy = 0;
                    } else if(board[row][col] == 'Y'){
                        numy++; 
                        numx = 0; 
                    } else if(board[row][col] == '○'){
                        numx = 0; 
                        numy = 0; 
                    }
                    if(row != 0 && col != 6){
                        if(numy == 3 && board[row - 1][col + 1] == '○' && board[row][col + 1] != '○'){
                            updateBoard(board, 'Y', col + 1, height); 
                            played = true; 
                            break outerloop; 
                        } else if(numx == 3 && board[row - 1][col + 1] == '○' && board[row][col + 1] != '○'){
                            updateBoard(board, 'Y', col + 1, height); 
                            played = true; 
                            break outerloop; 
                        }
                    }
                }
            }
        }
        //battery counter moves (if there is an open space on either side of two adjacent x pieces)
        if(played != true){
            outerloop:
            for(int i = 0; i < board.length; i++){
                for(int j = 0; j < board[0].length; j++){
                    if(j < 1){
                        if(board[i][j] == '○' && board[i][j + 1] == 'X' && board[i][j + 2] == 'X' && board[i][j + 3] == '○'){
                            if(i == 5){
                                updateBoard(board, 'Y', j, height);
                                played = true; 
                                break outerloop; 
                            } else if(i < 5 && board[i + 1][j] != '○'){
                                updateBoard(board, 'Y', j, height);
                                played = true; 
                                break outerloop;
                            }
                        } else if(board[i][j + 1] == '○' && board[i][j + 2] == 'X' && board[i][j + 3] == 'X' && board[i][j + 4] == '○'){
                            if(i == 5){
                                updateBoard(board, 'Y', j + 1, height);
                                played = true; 
                                break outerloop;
                            } else if(i < 5 && board[i + 1][j + 1] != '○'){
                                updateBoard(board, 'Y', j + 1, height);
                                played = true;
                                break outerloop;
                            }
                        } else if(board[i][j + 2] == '○' && board[i][j + 3] == 'X' && board[i][j + 4] == 'X' && board[i][j + 5] == '○'){
                            if(i == 5){
                                updateBoard(board, 'Y', j + 2, height);
                                played = true; 
                                break outerloop;
                            } else if(i < 5 && board[i + 1][j + 2] != '○'){
                                updateBoard(board, 'Y', j + 2, height);
                                played = true; 
                                break outerloop;
                            } 
                        } else if(board[i][j + 3] == '○' && board[i][j + 4] == 'X' && board[i][j + 5] == 'X' && board[i][j + 6] == '○'){
                            if(i == 5){
                                updateBoard(board, 'Y', j + 3, height);
                                played = true; 
                                break outerloop;
                            } else if(i < 5 && board[i + 1][j + 3] != '○'){
                                updateBoard(board, 'Y', j + 3, height);
                                played = true; 
                                break outerloop;
                            } 
                        }
                    }
                }
            }
        }
        //random move  
        boolean loop = true; 
        int index = (int) (Math.random() * 7); 
        if(played != true){
            while(loop == true){ 
                if(height[index] < 0){
                    index = (int) (Math.random() * 7);   
                } else {
                    updateBoard(board, 'Y', index, height); 
                    loop = false;
                }
            } 
        }
        checkWin(board); 
        checkTie(height);
    }

    public static void checkWin(char[][] board){
        //vertical check 
        int numx = 0; 
        int numy = 0; 
        for(int i = 0; i < board[0].length; i++){
            numx = 0; 
            numy = 0; 
            for(int j = 0; j < board.length; j++){
                if(board[j][i] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[j][i] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[j][i] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!"); 
                    yourwin++; 
                    end = true;  
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true; 
                }
            }
        }
        //horizontal check 
        for(int i = 0; i < board.length; i++){
            numx = 0; 
            numy = 0; 
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[i][j] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[i][j] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!");
                    yourwin++; 
                    end = true;
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true;
                }
            }
        }
        //diagonal check for topleft to bottom right - 1
        int row; 
        int col;
        int start; 
        int columnend = board[0].length + 1; 
        for(start = 0; start < 3; start++){
            numx = 0; 
            numy = 0; 
            columnend--; 
            for(row = start, col = 0; row < board.length && col < columnend; row++, col++){
                if(board[row][col] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[row][col] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[row][col] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!"); 
                    yourwin++; 
                    end = true;
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true;
                }
            }
        }
        //diagonal check for bottom right to top left + 1
        int rowend = board.length + 1; 
        for(start = 0; start < 3; start++){
            numx = 0; 
            numy = 0; 
            rowend--; 
            for(row = 0, col = start + 1; row < rowend && col < board[0].length; row++, col++){
                if(board[row][col] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[row][col] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[row][col] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!"); 
                    yourwin++; 
                    end = true;
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true;
                }
            }
        }
        //diagonal check for bottom left to top right - 1
        columnend = 3;
        for(start = 0; start < 3; start++){
            numx = 0; 
            numy = 0;
            columnend++; 
            for(row = start + 3, col = 0; row >= 0 && col < columnend; row--, col++){
                if(board[row][col] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[row][col] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[row][col] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!");
                    yourwin++; 
                    end = true;
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true;
                }
            }
        }
        //diagonal check for top right to bottom left + 1
        columnend = 0; 
        for(start = 0; start < 3; start++){
            numx = 0; 
            numy = 0; 
            columnend++; 
            for(row = start, col = 6; row < board.length && col >= columnend; row++, col--){
                if(board[row][col] == 'X'){
                    numx++; 
                    numy = 0;
                } else if(board[row][col] == 'Y'){
                    numy++; 
                    numx = 0; 
                } else if(board[row][col] == '○'){
                    numx = 0; 
                    numy = 0; 
                }
                if(numx == 4){
                    System.out.println("You have won!");
                    yourwin++; 
                    end = true;
                } else if(numy == 4){
                    System.out.println("You have lost!"); 
                    cpuwin++; 
                    end = true;
                }
            }
        }
    }

    public static void checkTie(int[] height){
        int count = 0; 
        for(int i = 0; i < height.length; i++){
            if(height[i] < 0){
                count++; 
            }
        }
        if(count == 7){
            System.out.println("No more moves to be played! It's a tie!"); 
            end = true; 
        }
    }
}
