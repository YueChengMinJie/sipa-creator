package com.sipa.tcp.creator.mbp;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cn.hutool.core.util.ReUtil;
import lombok.Data;
import lombok.ToString;

/**
 * @author caszhou
 * @date 2022/6/10
 */
@Data
@ToString
public class ConditionExpr {
    private static final Pattern DYNAMIC_PARAM_PATTERN = Pattern.compile("#\\{(.*?)}");

    private String findPattern;

    private String logicOperator;

    private String leftExpr;

    private String rightExpr;

    private String operator;

    // 如果operator是between, 会存在middleOperator和endExpr
    private String middleOperator;

    private String endExpr;

    private List<String> paramNames;

    public int parseDynamicParams(String content) {
        if (paramNames == null) {
            paramNames = Lists.newArrayList();
        }
        Matcher m = DYNAMIC_PARAM_PATTERN.matcher(content);
        int index = 0;
        while (m.find()) {
            String group = m.group(index);
            paramNames.add(group.substring(2, group.length() - 1));
            index++;
        }
        return index;
    }

    public String getFindPattern() {
        StringBuilder pattern = new StringBuilder();
        if (!Strings.isNullOrEmpty(logicOperator)) {
            pattern.append("\\s+");
            pattern.append(logicOperator);
            pattern.append("\\s+");
        }
        pattern.append(leftExpr);
        pattern.append("\\s*");
        pattern.append(operator);
        pattern.append("\\s*");
        pattern.append(ReUtil.escape(getRightExpr()));
        if (!Strings.isNullOrEmpty(middleOperator)) {
            pattern.append("\\s+");
            pattern.append(middleOperator);
            pattern.append("\\s+");
            pattern.append(ReUtil.escape(getEndExpr()));
        }
        return pattern.toString();
    }

    public static boolean isDynamicParam(String content) {
        return DYNAMIC_PARAM_PATTERN.matcher(content).find();
    }
}
