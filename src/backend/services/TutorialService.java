package backend.services;

import backend.models.TutorialSection;
import java.util.*;

public class TutorialService {
    private final Map<String, TutorialSection> tutorials;

    public TutorialService() {
        this.tutorials = initializeTutorials();
    }

    private Map<String, TutorialSection> initializeTutorials() {
        Map<String, TutorialSection> tutorialMap = new HashMap<>();

        TutorialSection basics = new TutorialSection();
        basics.setId("basics");
        basics.setTitle("Stock Market Basics");
        basics.setDescription("Learn the fundamental concepts of stock market trading");
        basics.setContent("<h3>What are Stocks?</h3><p>Stocks represent ownership in a company. When you buy a stock, you become a shareholder.</p><h3>Key Concepts:</h3><ul><li><strong>Bull Market:</strong> Rising prices</li><li><strong>Bear Market:</strong> Falling prices</li><li><strong>Portfolio:</strong> Your collection of investments</li><li><strong>Diversification:</strong> Spreading investments to reduce risk</li></ul>");
        basics.setExerciseQuestion("If a company has 1 million shares and you own 10,000, what percentage do you own?");
        basics.setExerciseAnswer("1%");
        basics.setHint("Calculate: (Your shares / Total shares) × 100");
        tutorialMap.put("basics", basics);

        TutorialSection buying = new TutorialSection();
        buying.setId("buying");
        buying.setTitle("How to Buy Stocks");
        buying.setDescription("Step-by-step guide to purchasing stocks");
        buying.setContent("<h3>Steps to Buy Stocks:</h3><ol><li>Research the company</li><li>Check financial metrics (P/E ratio, earnings, debt)</li><li>Decide on order type (Market vs Limit)</li><li>Place your order</li><li>Monitor your investment</li></ol><h3>Order Types:</h3><ul><li><strong>Market Order:</strong> Buy at current market price</li><li><strong>Limit Order:</strong> Buy only at specified price or better</li><li><strong>Stop Order:</strong> Becomes market order when price hits certain level</li></ul>");
        buying.setExerciseQuestion("You want to buy ABC stock, but only if it drops to $50. What order type should you use?");
        buying.setExerciseAnswer("Limit Order");
        buying.setHint("This order type lets you set a maximum purchase price");
        tutorialMap.put("buying", buying);

        TutorialSection selling = new TutorialSection();
        selling.setId("selling");
        selling.setTitle("When to Sell Stocks");
        selling.setDescription("Learn strategic approaches to selling stocks");
        selling.setContent("<h3>Selling Strategies:</h3><ul><li><strong>Profit Taking:</strong> Sell when you've reached your target gain</li><li><strong>Stop Loss:</strong> Sell automatically if price drops too much</li><li><strong>Rebalancing:</strong> Sell to maintain your target asset allocation</li><li><strong>Fundamental Change:</strong> Sell if company fundamentals deteriorate</li></ul><h3>Tax Considerations:</h3><p>Hold investments for over a year for favorable long-term capital gains tax rates.</p>");
        selling.setExerciseQuestion("You bought XYZ stock at $100 and it's now $150. You want to protect your profits if it drops to $140. What order type should you use?");
        selling.setExerciseAnswer("Stop Loss Order");
        selling.setHint("This order triggers a sale when the price falls to a specified level");
        tutorialMap.put("selling", selling);

        TutorialSection analysis = new TutorialSection();
        analysis.setId("analysis");
        analysis.setTitle("Stock Analysis Methods");
        analysis.setDescription("Learn fundamental and technical analysis techniques");
        analysis.setContent("<h3>Fundamental Analysis:</h3><ul><li>Examine financial statements</li><li>Analyze P/E ratio, EPS, revenue growth</li><li>Evaluate management team</li><li>Study industry trends</li></ul><h3>Technical Analysis:</h3><ul><li>Study price charts and patterns</li><li>Use indicators like Moving Averages, RSI, MACD</li><li>Identify support and resistance levels</li><li>Analyze trading volume</li></ul>");
        analysis.setExerciseQuestion("A company has earnings of $5 per share and its stock price is $100. What is its P/E ratio?");
        analysis.setExerciseAnswer("20");
        analysis.setHint("P/E ratio = Price per share / Earnings per share");
        tutorialMap.put("analysis", analysis);

        return tutorialMap;
    }

    public List<TutorialSection> getAllTutorials() {
        return new ArrayList<>(tutorials.values());
    }

    public TutorialSection getTutorial(String tutorialId) {
        return tutorials.get(tutorialId);
    }

    public boolean validateExercise(String tutorialId, String userAnswer) {
        TutorialSection tutorial = tutorials.get(tutorialId);
        if (tutorial != null && tutorial.getExerciseAnswer() != null) {
            return tutorial.getExerciseAnswer().equalsIgnoreCase(userAnswer.trim());
        }
        return false;
    }
}