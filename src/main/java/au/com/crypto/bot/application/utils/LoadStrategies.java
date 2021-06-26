package au.com.crypto.bot.application.utils;

import au.com.crypto.bot.application.ApplicationControllers;
import au.com.crypto.bot.application.CONSTANTS;
import au.com.crypto.bot.application.analyzer.entities.*;
import au.com.crypto.bot.application.trade.Strategies;
import org.springframework.beans.factory.annotation.Autowired;
import serilogj.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class LoadStrategies {

    private static LoadStrategies loadStrategiesClazz;
    private static Strategies strategies;
    private ApplicationControllers ac;


    public static LoadStrategies getInstance() {
        if (loadStrategiesClazz == null) {
            loadStrategiesClazz = new LoadStrategies();
        }
        return loadStrategiesClazz;
    }

    private LoadStrategies() {
        ac = ApplicationControllers.getInstance();
        loadStrategies();
    }

    public Strategies getStrategies() {
        loadStrategies();
        return strategies;
    }

    public void setStrategies(Strategies strategies) {
        LoadStrategies.strategies = strategies;
    }

    private void loadStrategies() {

        List<Strategy> strategiesDB = ac.getStrategyController().findStrategyByStatus(CONSTANTS._active);
        Strategies strategies = new Strategies();
        List<Strategies.Strategy> strategyList = new ArrayList<>();
        if (strategiesDB != null && !strategiesDB.isEmpty()) {

            strategiesDB.forEach(s -> {
                Strategies.Strategy strategy = new Strategies.Strategy(s);
                List<StrategyConditions> conditions = ac.getStrategyConditionsController().findStrategyConditionsByStrategyIdAndVersion(
                        strategy.getStrategyId(), strategy.getVersion());
                List<StrategyConditions> conditionCloseList = conditions.stream()
                        .filter(c -> c.getConditionGroup().equalsIgnoreCase(CONSTANTS._close))
                        .collect(Collectors.toList());
                List<StrategyConditions> conditionOpenList = conditions.stream()
                        .filter(c -> c.getConditionGroup().equalsIgnoreCase(CONSTANTS._open))
                        .collect(Collectors.toList());


                //Close Condition SubGroups
                Set<Long> closeSubGroups = new HashSet<>();
                conditionCloseList.forEach(c -> closeSubGroups.add(c.getConditionSubGroup()));

                //Open Condition SubGroups
                Set<Long> openSubGroups = new HashSet<>();
                conditionOpenList.forEach(c -> openSubGroups.add(c.getConditionSubGroup()));

                //Close strategy
                closeSubGroups.forEach(subGroup -> {
                    Strategies.ConditionsGroup conditionsGroup = new Strategies.ConditionsGroup();
                    conditionCloseList.forEach(c -> {
                        if (c.getConditionSubGroup() == subGroup) {
                            Strategies.ConditionsGroup.Condition condition = new Strategies.ConditionsGroup.Condition(c, s.getSymbol());
                            conditionsGroup.getConditions().add(condition);
                        }
                    });
                    strategy.getCloseConditionGroups().add(conditionsGroup);
                });

                //Open strategy
                openSubGroups.forEach(subGroup -> {
                    Strategies.ConditionsGroup conditionsGroup = new Strategies.ConditionsGroup();
                    conditionOpenList.forEach(c -> {
                        if (c.getConditionSubGroup() == subGroup) {
                            Strategies.ConditionsGroup.Condition condition = new Strategies.ConditionsGroup.Condition(c, s.getSymbol());
                            conditionsGroup.getConditions().add(condition);
                        }
                    });
                    strategy.getOpenConditionGroups().add(conditionsGroup);
                });
                strategyList.add(strategy);
                strategies.setStrategies(strategyList);
            });
        }
//        if (strategies.getStrategies() == null || strategies.getStrategies().size() == 0) {
//            throw new Exception("NO Strategies found" );
//        }
        Log.information("{Application} Successfully loaded strategies, {Count} ", "Analyzer", (strategies.getStrategies() == null ? 0 : strategies.getStrategies().size()));
        if (strategies.getStrategies() != null) {
            strategies.getStrategies().forEach(s -> Log.information("{Application} Strategies - {Strategy} ", "Analyzer", s.getStrategyName()));
        }

        setStrategies(strategies);
    }

    //Load JSON file to JSON Object
    private String loadFile(String filepath) throws Exception {
        try {
            return Files.readString(Paths.get(filepath));
        } catch (IOException io) {
            io.printStackTrace();
            Log.error(io, "Error loading strategies file {path}", filepath);
            throw new Exception("Could not find any strategies in " + filepath);
        }
    }
}
