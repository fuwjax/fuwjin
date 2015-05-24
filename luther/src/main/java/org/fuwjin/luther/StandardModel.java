package org.fuwjin.luther;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
* Created by fuwjax on 2/4/15.
*/
public class StandardModel implements Model, Node {
    private final Symbol symbol;
    private Node lastChild;

    public StandardModel(Symbol symbol) {
        this(symbol);
    }

    public StandardModel(Symbol symbol, Node... children) {
        this.symbol = symbol;
        this.children = children;
    }

    @Override
    public Model accept(int ch) {
          return add(new Char(ch));
    }

    @Override
    public Model set(Symbol key, Model value) {
        return add((StandardModel)value);
    }

    private Model add(Node value){
        Node[] newChildren = Arrays.copyOf(children, children.length+1);
        newChildren[children.length] = value;
        return new StandardModel(symbol, newChildren);
    }

    @Override
    public Model nest(Symbol lhs, Model result) {
        return set(lhs, result);
    }

    @Override
    public String match() {
        StringBuilder b = new StringBuilder();
        for(Node child: children){
            b.append(child.match());
        }
      return b.toString();
}

    @Override
    public List<Node> children() {
        return Arrays.asList(children);
    }

    public Symbol symbol() {
       return symbol;
    }

    @Override
    public void addAlternative(Model result) {
        StandardModel model = (StandardModel)result;
        if(isBetter(model)){
            this.children = model.children().toArray(new Node[model.children().size()]);
        }
    }

    public boolean isBetter(Node result) {
      if(children.length > result.children().size()){
          return true;
      }
      if(children.length < result.children().size()){
          return false;
      }
      for(int i=0;i<children.length;i++){
          if(children[i].isBetter(result.children().get(i))){
              return true;
          }
      }
      return false;
}

    @Override
    public boolean equals(Object obj) {
        try{
            StandardModel o = (StandardModel)obj;
            return symbol.equals(o.symbol()) && children().equals(o.children());
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
       return Objects.hash(symbol, children());
    }

    @Override
    public String toString() {
       return symbol.name()+children();
    }
}

interface Node {
    String match();

    List<Node> children();

    boolean isBetter(Node other);
}

class Char implements Node {
    private final int ch;

    public Char(int ch) {
        this.ch = ch;
    }

    @Override
    public boolean isBetter(Node other) {
        return true;
    }

    @Override
    public String match() {
        return new String(Character.toChars(ch));
    }

    @Override
    public List<Node> children() {
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        return match();
    }
}
