import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class US_elections {

    public static int solution(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){
        int[][] vote_table = getMinVotes(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
        if (vote_table == null) return -1;                          // joe can't win
        else return findOpt(vote_table);                            // joe can win --> compute the optimal solution
    }

    private static int findOpt(int arr[][]) {
        int n = arr[0][2];                          // number of elements (rows) in array
        int W = arr[1][2];                          // min delegates joe needs
        int table[][] = new int[n+1][W+1];          // table
        int I = 0; int V = 0;
        int left = 0; int right = 0;

        // filling table
        table[0][0] = 0;
        for (int j = 1; j <= W; j++) table[0][j] = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= W; j++) {
                I = i-1;
                V = j-arr[i-1][1];

                // compute left
                if(I==0 && j==0) left = 0;
                else if(I==0 && j>0) left = Integer.MAX_VALUE;
                else left = table[I][j];

                // compute right
                if(I==0 && V==0) right = 0 + arr[I][0];
                else if(I==0 && V>0) right = Integer.MAX_VALUE;
                else if(V<0) right = 0 + arr[I][0];
                else {
                    if (table[I][V] != Integer.MAX_VALUE) right = table[I][V] + arr[I][0];
                    else right = Integer.MAX_VALUE;
                }

                table[i][j]= findMin(left, right);
                }
            }
        return table[n][W];
    }

    private static int findMin(int a, int b)
    {
        return (a<b) ? a : b;
    }

    private static int[][] getMinVotes(int num_states, int[] delegates, int[] votes_Biden, int[] votes_Trump, int[] votes_Undecided){
        int[][] rtn;
        if (num_states>=3) rtn = new int[num_states][3];
        else rtn = new int[2][3];

        int joe = 0;                // votes for joe in state at hand
        int don = 0;                // votes for don in state at hand
        int un = 0;                 // undecided votes in state at hand
        int vote_diff = 0;          // biden votes - trump votes
        int cur_del = 0;            // delegates of state sat hand
        int max_del = 0;            // max delegates joe can win
        int min_del = 0;            // min delegates joe needs to win
        int tot_del = 0;            // sum of all state delegates
        int n = 0;                  // number of items in the array (number of states joe can win)
        int j = 0;                  // keeps track of the return array index (row)

        for(int i=0;i<num_states;i++) {
            joe = votes_Biden[i];
            don = votes_Trump[i];
            un = votes_Undecided[i];
            vote_diff = (joe - don);
            cur_del = delegates[i];
            tot_del += cur_del;

            // if joe is behind
            if (vote_diff < 0) {
                if (un > (-vote_diff)) {
                    rtn[j][0] = (((un + vote_diff) / 2) + (-vote_diff) + 1);
                    rtn[j][1] = cur_del;
                    max_del += cur_del;
                    j++; n++;
                }
            }

            // if joe is ahead
            else if (vote_diff > 0) {
                if (vote_diff > un) {
                    rtn[j][0] = 0;
                    rtn[j][1] = cur_del;
                    max_del += cur_del;
                    j++; n++;
                } else if (vote_diff < un) {
                    rtn[j][0] = (((un - vote_diff) / 2) + 1);
                    rtn[j][1] = cur_del;
                    max_del += cur_del;
                    j++; n++;
                } else if (vote_diff == un) {
                    rtn[j][0] = 1;
                    rtn[j][1] = cur_del;
                    max_del += cur_del;
                    j++; n++;
                }
            }

            // if the both have the same number of votes
            else if (vote_diff == 0) {
                if (un > 0) {
                    rtn[j][0] = (un / 2) + 1;
                    rtn[j][1] = cur_del;
                    max_del += cur_del;
                    j++; n++;
                }
            }
        }

        min_del = (tot_del/2)+1;
        rtn[0][2] = n;
        rtn[1][2] = min_del;
        if (max_del >= min_del) return rtn;         // joe can win
        else return null;                           // joe can't win
    }

    public static void main(String[] args) {
        try {
            String path = args[0];
            File myFile = new File(path);
            Scanner sc = new Scanner(myFile);
            int num_states = sc.nextInt();
            int[] delegates = new int[num_states];
            int[] votes_Biden = new int[num_states];
            int[] votes_Trump = new int[num_states];
            int[] votes_Undecided = new int[num_states];
            for (int state = 0; state<num_states; state++){
                delegates[state] =sc.nextInt();
                votes_Biden[state] = sc.nextInt();
                votes_Trump[state] = sc.nextInt();
                votes_Undecided[state] = sc.nextInt();
            }
            sc.close();
            int answer = solution(num_states, delegates, votes_Biden, votes_Trump, votes_Undecided);
            System.out.println(answer);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}