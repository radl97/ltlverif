package hu.mondokm.ltlverif.antlr;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;

public class LTLAPVisitor extends LTLGrammarBaseVisitor<Boolean> {

    private static LTLAPVisitor instance=new LTLAPVisitor();

    private LTLAPVisitor(){};

    public static LTLAPVisitor getInstance(){
        return instance;
    }

    HashMap<ParserRuleContext,Boolean> ltl=new HashMap<ParserRuleContext, Boolean>();

    @Override
    public Boolean visitModel(LTLGrammarParser.ModelContext ctx) {
        return super.visitModel(ctx);
    }

    @Override
    public Boolean visitImplyExpression(LTLGrammarParser.ImplyExpressionContext ctx) {
        super.visitImplyExpression(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for (LTLGrammarParser.OrExprContext op: ctx.ops){
            if(visitOrExpr(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitAndExpr(LTLGrammarParser.AndExprContext ctx) {
        super.visitAndExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for (LTLGrammarParser.NotExprContext op: ctx.ops){
            if(visitNotExpr(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitNotExpr(LTLGrammarParser.NotExprContext ctx) {
        super.visitNotExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for (LTLGrammarParser.NotExprContext op: ctx.ops){
            if(visitNotExpr(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        if(ctx.ltlExpr()!=null && visitLtlExpr(ctx.ltlExpr())) {
            ltl.put(ctx,true);
            return true;
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitLtlExpr(LTLGrammarParser.LtlExprContext ctx) {
        super.visitLtlExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        if(ctx.type!=null){
            ltl.put(ctx,true);
            return true;
        }
        boolean child=visitEqExpr(ctx.eqExpr());
        ltl.put(ctx,child);
        return child;
    }

    @Override
    public Boolean visitLtlOp(LTLGrammarParser.LtlOpContext ctx) {
        return false;
    }

    @Override
    public Boolean visitEqExpr(LTLGrammarParser.EqExprContext ctx) {
        super.visitEqExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for(LTLGrammarParser.RelationExprContext op:ctx.ops){
            if(visitRelationExpr(op)){
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitEqOperator(LTLGrammarParser.EqOperatorContext ctx) {
        return false;
    }

    @Override
    public Boolean visitRelationExpr(LTLGrammarParser.RelationExprContext ctx) {
        super.visitRelationExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for(LTLGrammarParser.AdditiveExprContext op:ctx.ops){
            if(visitAdditiveExpr(op)){
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitRelationOperator(LTLGrammarParser.RelationOperatorContext ctx) {
        return false;
    }

    @Override
    public Boolean visitAdditiveExpr(LTLGrammarParser.AdditiveExprContext ctx) {
        super.visitAdditiveExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for(LTLGrammarParser.MultiplicativeExprContext op:ctx.ops){
            if(visitMultiplicativeExpr(op)){
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitAdditiveOperator(LTLGrammarParser.AdditiveOperatorContext ctx) {
        return false;
    }

    @Override
    public Boolean visitMultiplicativeExpr(LTLGrammarParser.MultiplicativeExprContext ctx) {
        super.visitMultiplicativeExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for(LTLGrammarParser.NegExprContext op:ctx.ops){
            if(visitNegExpr(op)){
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitMultiplicativeOperator(LTLGrammarParser.MultiplicativeOperatorContext ctx) {
        return false;
    }

    @Override
    public Boolean visitNegExpr(LTLGrammarParser.NegExprContext ctx) {
        super.visitNegExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for (LTLGrammarParser.NegExprContext op: ctx.ops){
            if(visitNegExpr(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        if(ctx.primaryExpr()!=null && visitPrimaryExpr(ctx.primaryExpr())) {
            ltl.put(ctx,true);
            return true;
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitPrimaryExpr(LTLGrammarParser.PrimaryExprContext ctx) {
        super.visitPrimaryExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        boolean child=false;
        if(ctx.boolLitExpr()!=null) child=visitBoolLitExpr(ctx.boolLitExpr());
        if(ctx.intLitExpr()!=null) child=visitIntLitExpr(ctx.intLitExpr());
        if(ctx.parenExpr()!=null) child=visitParenExpr(ctx.parenExpr());
        ltl.put(ctx,child);
        return child;
    }

    @Override
    public Boolean visitBoolLitExpr(LTLGrammarParser.BoolLitExprContext ctx) {
        return false;
    }

    @Override
    public Boolean visitParenExpr(LTLGrammarParser.ParenExprContext ctx) {
        super.visitParenExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for (LTLGrammarParser.ImplyExpressionContext op: ctx.ops){
            if(visitImplyExpression(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;
    }

    @Override
    public Boolean visitIntLitExpr(LTLGrammarParser.IntLitExprContext ctx) {
        return false;
    }

    @Override
    public Boolean visitVariable(LTLGrammarParser.VariableContext ctx) {
        return false;
    }

    @Override
    public Boolean visitOrExpr(LTLGrammarParser.OrExprContext ctx) {
        super.visitOrExpr(ctx);
        if(ltl.get(ctx)!=null) return ltl.get(ctx);
        for(LTLGrammarParser.AndExprContext op:ctx.ops){
            if(visitAndExpr(op)) {
                ltl.put(ctx,true);
                return true;
            }
        }
        ltl.put(ctx,false);
        return false;

    }

}
