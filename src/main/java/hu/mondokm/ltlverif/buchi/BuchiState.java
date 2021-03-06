package hu.mondokm.ltlverif.buchi;

import hu.bme.mit.theta.analysis.pred.PredState;
import hu.bme.mit.theta.core.model.Valuation;
import hu.bme.mit.theta.core.type.Expr;
import hu.bme.mit.theta.core.type.booltype.BoolLitExpr;
import hu.bme.mit.theta.core.type.booltype.BoolType;

import java.util.*;

public class BuchiState {

    private HashSet<BuchiAction> actions;

	private int id;
	
	public int getId() {
		return id;
	}

    public BuchiState(int id) {
		this.id = id;
	}

	public boolean isAccepting() {
        return accepting;
    }

    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }

    private boolean accepting;

    public boolean hasLoop() {
        return hasLoop;
    }

    public void setHasLoop(boolean hasLoop) {
        this.hasLoop = hasLoop;
    }

    private boolean hasLoop=false;

    public void addTransition(Expr<BoolType> boolTypeExpr, BuchiState buchiState) {
        transitions.put(boolTypeExpr, buchiState);
    }

    private HashMap<Expr<BoolType>, BuchiState> transitions=new HashMap<Expr<BoolType>, BuchiState>();

    public HashSet<BuchiState> nextState(Valuation valuation){
        HashSet<BuchiState> next=new HashSet<BuchiState>();
        for(Expr<BoolType> cond:transitions.keySet()){
            if(((BoolLitExpr) cond.eval(valuation)).getValue()) next.add(transitions.get(cond));
        }
        return next;
    }

    public Set<BuchiAction> getActions(){
        if(actions==null){
            actions=new HashSet<BuchiAction>();
            for(Expr<BoolType> cond:transitions.keySet()){
                actions.add(new BuchiAction(cond,transitions.get(cond)));
            }
        }

        return actions;
    }
    
    public int hashCode() {
    	return id;
    }
    
    public boolean equals(Object obj) {
    	return obj instanceof BuchiState && id == ((BuchiState) obj).getId();
    }

}
