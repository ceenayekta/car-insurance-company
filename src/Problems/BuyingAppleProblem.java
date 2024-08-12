package Problems;

import java.util.Arrays;

public class BuyingAppleProblem {

  public static void main(String[] args) {
    int[] dailyCosts = { 1, 3, 10, 10, 5, 10, 10, 5, 10, 5, 4, 4, 6, 3, 10, 5, 3, 4, 2, 3 };
    String[] expectations = {
      "[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]",
      "[2, 1, 0, 1, 2, 0, 1, 2, 0, 1, 1, 2, 0, 2, 0, 1, 2, 0, 2, 0]",
      "[3, 1, 0, 0, 3, 0, 0, 2, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[4, 1, 0, 0, 2, 0, 0, 2, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[5, 1, 0, 0, 1, 0, 0, 2, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[6, 1, 0, 0, 0, 0, 0, 2, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[7, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[8, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[9, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[10, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[11, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[12, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 2, 0, 2, 0]",
      "[14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 2, 0]",
      "[15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 2, 0, 2, 0]",
      "[16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 2, 0]",
      "[17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0]",
      "[18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0]",
      "[19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0]",
      "[20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]",
    };

    // test 1 to 20
    for (int i = 1; i <= 20; i++) {
      int[] result = handleBuying(dailyCosts, i, 0, 0, new int[dailyCosts.length]);
      boolean passedExpectation = Arrays.toString(result).equals(expectations[i-1]);
      System.out.println(Arrays.toString(result));
      System.out.println((passedExpectation ? "\u001B[32m" : "\u001B[31m") + expectations[i-1] + (passedExpectation ? " Succeed!" : " Failed!") + "\u001B[0m");
      System.out.println();
    }
  }
  
  public static int[] handleBuying(int[] dailyCosts, int expirySpan, int today, int inventory, int[] buyStrategy) {
    // find best day to buy
    int nearestCheaperDay = today;
    int tillDay = Math.min(today + expirySpan, dailyCosts.length);
    for (int i = today + 1; i < tillDay; i++) {
      if (dailyCosts[i] <= dailyCosts[nearestCheaperDay]) {
        nearestCheaperDay = i;
        break;
      }
    }
    // strategy: buy minimum amount to survive till nearestCheaperDay
    inventory += buyStrategy[today] = Math.max((nearestCheaperDay == today ? tillDay : nearestCheaperDay) - today - inventory, 0);
    // eat one and go next day
    if (++today < dailyCosts.length) {
      buyStrategy = handleBuying(dailyCosts, expirySpan, today, --inventory, buyStrategy);
    }
    return buyStrategy;
  }
}