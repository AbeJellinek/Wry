package org.wrylang.interpreter;

import org.wrylang.ast.Expr;
import org.wrylang.parser.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class WryIntrinsics {
    private Scope scope;

    public WryIntrinsics(Scope scope) {
        this.scope = scope;
        try {
            include("lib/stdlib.wry");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Intrinsic("throw")
    public Obj throwEx(Obj error) {
        throw new WryException(new RuntimeException(error.toString()), Position.NONE);
    }

    @Intrinsic("include")
    public Obj include(StringObj fileName) throws FileNotFoundException {
        return include(fileName.getValue());
    }

    private Obj include(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Lexer lexer;
        if (file.exists()) {
            lexer = new Lexer(new SourceReader(new FileReader(file)));
        } else {
            lexer = new Lexer(new SourceReader(new InputStreamReader(getClass().
                    getResourceAsStream("/" + fileName))));
        }

        Parser parser = new Parser(new Elider(lexer));

        Expr next;
        Obj result = Obj.NULL();
        while ((next = parser.next()) != null) {
            result = next.accept(scope);
        }

        return result;
    }

    @Intrinsic("print")
    public Obj print(Obj o) {
        System.out.println(o);
        return Obj.NULL();
    }

    @Intrinsic(value = "plus", target = "String")
    public Obj plus(StringObj self, Obj superObj, Obj o) {
        return new StringObj(scope, self.getValue() + o);
    }
}
