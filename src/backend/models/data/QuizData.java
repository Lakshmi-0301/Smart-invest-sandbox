package backend.models.data;

import backend.models.Question;
import backend.models.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizData {

    public static Quiz getStockFundamentalsQuiz() {
        Quiz quiz = new Quiz("quiz-fundamentals", 70, 20); // ID, passing score, time limit

        List<Question> questions = new ArrayList<>();

        Question q1 = new Question();
        q1.setQuestionText("What does a stock represent?");
        q1.setOptions(Arrays.asList(
                "A loan to a company",
                "Ownership in a company",
                "A type of bond",
                "Government security"
        ));
        q1.setCorrectAnswerIndex(1);
        q1.setExplanation("Stocks represent ownership shares in a corporation.");
        q1.setQuestionType("MULTIPLE_CHOICE");
        questions.add(q1);

        Question q2 = new Question();
        q2.setQuestionText("What is the formula for market capitalization?");
        q2.setOptions(Arrays.asList(
                "Price per share × Total assets",
                "Shares outstanding × Price per share",
                "Revenue × Profit margin",
                "Debt + Equity"
        ));
        q2.setCorrectAnswerIndex(1);
        q2.setExplanation("Market capitalization is calculated as shares outstanding multiplied by stock price.");
        q2.setQuestionType("MULTIPLE_CHOICE");
        questions.add(q2);

        Question q3 = new Question();
        q3.setQuestionText("Which of these is a characteristic of a bull market?");
        q3.setOptions(Arrays.asList(
                "Falling prices and pessimism",
                "Rising prices and optimism",
                "Stagnant prices and uncertainty",
                "High volatility and fear"
        ));
        q3.setCorrectAnswerIndex(1);
        q3.setExplanation("Bull markets are characterized by rising prices and investor optimism.");
        q3.setQuestionType("MULTIPLE_CHOICE");
        questions.add(q3);

        Question q4 = new Question();
        q4.setQuestionText("Stock exchange helps in?");
        q4.setOptions(Arrays.asList(
                "Providing liquidity to the existing securities",
                "contributing to economic growth",
                "pricing of securities",
                "All of the above"
        ));
        q4.setCorrectAnswerIndex(3);
        q4.setExplanation("Stock exchage plays a crucial role by enhancing liquidity for investors, facilitating capital formation for economic growth and enabling efficient price discovery of securities.");
        q4.setQuestionType("MULTIPLE_CHOICE");
        questions.add(q4);
        quiz.setQuestions(questions);
        return quiz;
    }
}
