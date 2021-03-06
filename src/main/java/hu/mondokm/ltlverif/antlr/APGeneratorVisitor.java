package hu.mondokm.ltlverif.antlr;

import hu.bme.mit.theta.core.decl.VarDecl;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolType;
import hu.bme.mit.theta.core.type.booltype.ImplyExpr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static hu.bme.mit.theta.core.type.abstracttype.AbstractExprs.*;
import static hu.bme.mit.theta.core.type.booltype.BoolExprs.*;
import static hu.bme.mit.theta.core.type.inttype.IntExprs.Int;
import static hu.bme.mit.theta.core.type.inttype.IntExprs.Mod;

public class APGeneratorVisitor extends LTLGrammarBaseVisitor<Expr> {

    HashMap<String, VarDecl> vars;
    HashMap<String,Integer> literals;

    public  APGeneratorVisitor(HashMap<String,VarDecl> vars, HashMap<String,Integer> literals){
        this.vars=vars;
        this.literals=literals;
    }

    @Override
    public Expr visitModel(LTLGrammarParser.ModelContext ctx) {
        return super.visitModel(ctx);
    }

    @Override
    public Expr visitImplyExpression(LTLGrammarParser.ImplyExpressionContext ctx) {
        if(ctx.ops.size()>1){
            return Imply(visitOrExpr(ctx.ops.get(0)),visitOrExpr(ctx.ops.get(1)));
        }else return visitOrExpr(ctx.ops.get(0));
    }

    @Override
    public Expr visitOrExpr(LTLGrammarParser.OrExprContext ctx) {
        if(ctx.ops.size()==1) return visitAndExpr(ctx.ops.get(0));
        List<Expr<BoolType>> ops=new ArrayList<Expr<BoolType>>();
        for(LTLGrammarParser.AndExprContext child: ctx.ops){
            ops.add(visitAndExpr(child));
        }
        return Or(ops);
    }

    @Override
    public Expr<BoolType> visitAndExpr(LTLGrammarParser.AndExprContext ctx) {
        if(ctx.ops.size()==1) return visitNotExpr(ctx.ops.get(0));
        List<Expr<BoolType>> ops=new ArrayList<Expr<BoolType>>();
        for(LTLGrammarParser.NotExprContext child: ctx.ops){
            ops.add(visitNotExpr(child));
        }
        return And(ops);
    }

    @Override
    public Expr<BoolType> visitNotExpr(LTLGrammarParser.NotExprContext ctx) {
        if(ctx.ops.size()>0) return Not(visitNotExpr(ctx.ops.get(0)));
        else return visitBinaryLtlExpr(ctx.binaryLtlExpr());
    }

    @Override
    public Expr visitBinaryLtlExpr(LTLGrammarParser.BinaryLtlExprContext ctx) {
        return visitLtlExpr(ctx.ltlExpr());
    }

    @Override
    public Expr visitBinaryLtlOp(LTLGrammarParser.BinaryLtlOpContext ctx) {
        return super.visitBinaryLtlOp(ctx);
    }

    @Override
    public Expr visitLtlExpr(LTLGrammarParser.LtlExprContext ctx) {
        return visitEqExpr(ctx.eqExpr());
    }

    @Override
    public Expr visitLtlOp(LTLGrammarParser.LtlOpContext ctx) {
        return super.visitLtlOp(ctx);
    }

    @Override
    public Expr visitEqExpr(LTLGrammarParser.EqExprContext ctx) {
        if(ctx.ops.size()>1){
            if(ctx.oper.EQ()!=null) return Eq(visitRelationExpr(ctx.ops.get(0)),visitRelationExpr(ctx.ops.get(1)));
            else return Neq(visitRelationExpr(ctx.ops.get(0)),visitRelationExpr(ctx.ops.get(1)));
        }else return visitRelationExpr(ctx.ops.get(0));
    }

    @Override
    public Expr visitEqOperator(LTLGrammarParser.EqOperatorContext ctx) {
        return super.visitEqOperator(ctx);
    }

    @Override
    public Expr visitRelationExpr(LTLGrammarParser.RelationExprContext ctx) {
        if(ctx.ops.size()>1){
            if(ctx.oper.LEQ()!=null){
                return Leq(visitAdditiveExpr(ctx.ops.get(0)),visitAdditiveExpr(ctx.ops.get(1)));
            }else if(ctx.oper.GEQ()!=null){
                return Geq(visitAdditiveExpr(ctx.ops.get(0)),visitAdditiveExpr(ctx.ops.get(1)));
            }else if(ctx.oper.LT()!=null){
                return Lt(visitAdditiveExpr(ctx.ops.get(0)),visitAdditiveExpr(ctx.ops.get(1)));
            }else return Gt(visitAdditiveExpr(ctx.ops.get(0)),visitAdditiveExpr(ctx.ops.get(1)));
        }else return visitAdditiveExpr(ctx.ops.get(0));
    }

    @Override
    public Expr visitRelationOperator(LTLGrammarParser.RelationOperatorContext ctx) {
        return super.visitRelationOperator(ctx);
    }

    @Override
    public Expr visitAdditiveExpr(LTLGrammarParser.AdditiveExprContext ctx) {
        Expr res=visitMultiplicativeExpr(ctx.ops.get(0));
        for(int i=1;i<ctx.ops.size();i++){
            if(ctx.opers.get(i-1).PLUS()!=null){
                res=Add(res,visitMultiplicativeExpr(ctx.ops.get(i)));
            }else{
                res=Sub(res,visitMultiplicativeExpr(ctx.ops.get(i)));
            }
        }
        return res;

    }

    @Override
    public Expr visitAdditiveOperator(LTLGrammarParser.AdditiveOperatorContext ctx) {
        return super.visitAdditiveOperator(ctx);
    }

    @Override
    public Expr visitMultiplicativeExpr(LTLGrammarParser.MultiplicativeExprContext ctx) {
        Expr res=visitNegExpr(ctx.ops.get(0));
        for(int i=1;i<ctx.ops.size();i++){
            if(ctx.opers.get(i-1).DIV()!=null){
                res=Div(res,visitNegExpr(ctx.ops.get(i)));
            }else if(ctx.opers.get(i-1).MOD()!=null){
                res=Mod(res,visitNegExpr(ctx.ops.get(i)));
            }else{
                res=Mul(res,visitNegExpr(ctx.ops.get(i)));
            }
        }
        return res;
    }

    @Override
    public Expr visitMultiplicativeOperator(LTLGrammarParser.MultiplicativeOperatorContext ctx) {
        return super.visitMultiplicativeOperator(ctx);
    }

    @Override
    public Expr visitNegExpr(LTLGrammarParser.NegExprContext ctx) {
        if(ctx.ops.size()>0){
            return Neg(visitNegExpr(ctx.ops.get(0)));
        }else return visitPrimaryExpr(ctx.primaryExpr());
    }

    @Override
    public Expr visitPrimaryExpr(LTLGrammarParser.PrimaryExprContext ctx) {
        if(ctx.boolLitExpr()!=null){
            return visitBoolLitExpr(ctx.boolLitExpr());
        }else if(ctx.intLitExpr()!=null){
            return visitIntLitExpr(ctx.intLitExpr());
        }else return visitParenExpr(ctx.parenExpr());
    }

    @Override
    public Expr visitBoolLitExpr(LTLGrammarParser.BoolLitExprContext ctx) {
        if(ctx.value.getText().equals("true")) return True();
        else return False();
    }

    @Override
    public Expr visitParenExpr(LTLGrammarParser.ParenExprContext ctx) {
        if(ctx.variable()!=null) return visitVariable(ctx.variable());
        else return visitImplyExpression(ctx.ops.get(0));
    }

    @Override
    public Expr visitIntLitExpr(LTLGrammarParser.IntLitExprContext ctx) {
        return Int(Integer.parseInt(ctx.value.getText()));
    }

    @Override
    public Expr visitVariable(LTLGrammarParser.VariableContext ctx) {
        if(literals.containsKey(ctx.name.getText())) return Int(literals.get(ctx.name.getText()));
        VarDecl decl=vars.get(ctx.name.getText());
        if(decl==null) System.out.println("Variable ["+ctx.name.getText()+"] not found");
        return decl.getRef();
    }


}
