package com.sipa.tcp.creator.sqlparser;

import java.util.List;

import com.sipa.tcp.creator.mbp.ConditionExpr;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.values.ValuesStatement;

/**
 * 解析子查询以及自身的where条件部分
 * 
 * @author caszhou
 * @date 2022/6/10
 */
public class SelectConditionParser implements SelectVisitor, FromItemVisitor {
    private final WhereParser whereConditionParser = new WhereParser();

    public List<ConditionExpr> getParsedConditions() {
        return whereConditionParser.getConditions();
    }

    @Override
    public void visit(Table tableName) {}

    @Override
    public void visit(SubSelect subSelect) {
        subSelect.getSelectBody().accept(this);
    }

    @Override
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(this);
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {}

    @Override
    public void visit(ValuesList valuesList) {}

    @Override
    public void visit(TableFunction tableFunction) {}

    @Override
    public void visit(ParenthesisFromItem aThis) {}

    @Override
    public void visit(PlainSelect plainSelect) {
        Expression where = plainSelect.getWhere();
        if (where != null) {
            where.accept(whereConditionParser);
        }
        plainSelect.getFromItem().accept(this);
    }

    @Override
    public void visit(SetOperationList setOpList) {
        for (SelectBody sb : setOpList.getSelects()) {
            sb.accept(this);
        }
    }

    @Override
    public void visit(WithItem withItem) {}

    @Override
    public void visit(ValuesStatement aThis) {}
}
