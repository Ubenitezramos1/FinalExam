5. M_if_stmt(given (<bool_expr>) <stat1> inst <stat2>)
    if M_ bool_expr == error
        return error
    else if M_bool_expr == true
        <stat1>
    else
        <stat2>

6. M_while_stmt(run (<bool_expr>) <stat1>)
    if M_bool_expr == error
        return error
    else if M_bool_expr == false
        break
    else
        <stat1>

7. M_expr(<expr>, s) --> 
    if <expr> == <float>
        return M_float(<float>)
    else if <expr> == <var>
        if VARMAP[<var>, s] == undef
            return error
        else
            return VARMAP[<var>, s]
    else
        if M_expr(<left_term>, s) == error
            return error
        else if M_expr(<right_term>, s) == error
            return error
        else if <operator> == '%'
            return M_expr(<left_expr>, s) % M_expr(<right_expr>, s)
        else if <operator> == '/'
            return M_expr(<left_expr>, s) / M_expr(<right_expr>, s)
        else if <operator> == '*'
            return M_expr(<left_expr>, s) * M_expr(<right_expr>, s)
        else if <operator> == '+'
            return M_expr(<left_expr>, s) + M_expr(<right_expr>, s)
        else if <operator> == '-'
            return M_expr(<left_expr>, s) - M_expr(<right_expr>, s)

8. M_expr(<expr>, s) --> 
    if <expr> == <float>
        return M_float(<float>)
    else if <expr> == <var>
        if VARMAP[<var>, s] == undef
            return error
        else
            return VARMAP[<var>, s]
    else if <expr> == <binary_expr>
        if M_expr(<left_term>, s) == error
            return error
        else if M_expr(<right_term>, s) == error
            return error
        else if <operator> == '%'
            return M_expr(<left_expr>, s) % M_expr(<right_expr>, s)
        else if <operator> == '/'
            return M_expr(<left_expr>, s) / M_expr(<right_expr>, s)
        else if <operator> == '*'
            return M_expr(<left_expr>, s) * M_expr(<right_expr>, s)
        else if <operator> == '+'
            return M_expr(<left_expr>, s) + M_expr(<right_expr>, s)
        else if <operator> == '-'
            return M_expr(<left_expr>, s) - M_expr(<right_expr>, s)
    else if <expr> == <bool_expr>
        if M_expr(<left_term>, s) == error
            return error
        else if M_expr(<right_term>, s) == error
            return error
        else if <operator> == '<='
            if M_expr(<left_expr>, s) <= M_expr(<right_expr>, s)
                return true
            else
                return false
        else if <operator> == '>='
             if M_expr(<left_expr>, s) >= M_expr(<right_expr>, s)
                return true
            else
                return false
        else if <operator> == '<'
             if M_expr(<left_expr>, s) < M_expr(<right_expr>, s)
                return true
            else
                return false
        else if <operator> == '>'
             if M_expr(<left_expr>, s) > M_expr(<right_expr>, s)
                return true
            else
                return false
        else if <operator> == '=='
             if M_expr(<left_expr>, s) == M_expr(<right_expr>, s)
                return true
            else
                return false
        else if <operator> == '!='
             if M_expr(<left_expr>, s) != M_expr(<right_expr>, s)
                return true
            else
                return false

9. <asstmt> -> id = <expr>
<expr> -> <expr> + <expr>
    Type = "String" if both <expr>.Type are "String"
    Type = error otherwise
<expr> -> <expr> * <expr>
    Type = "String" if <expr>1.Type is "String" and <expr>2.Type is "Natural"
    Type = error otherwise
<expr> -> <expr> / <expr>
    Type = "Real" if <expr>1.Type is "Natural" or "Real" and <expr>2.Type is "Natural" or "Real"
    Type = error if <expr>2.Type is "Natural" or "Real" and its value is 0
    Type = error otherwise
<expr> -> <expr> % <expr>
    Type = "Natural" if <expr>1.Type is "Natural" and <expr>2.Type is "Natural"
    Type = error if <expr>2.Type is "Natural" and its value is 0
    Type = error otherwise
<expr> -> lvalue
    Type = lvalue.Type
<expr> -> bool
    Type = "Bool"
<expr> -> natural
    Type = "Natural"
<expr> -> real
    Type = "Real"
<expr> -> char
    Type = "Char"
<expr> -> string
    Type = "String"

Types: "Bool", "Natural", "Real", "Char", "String", error

10. 
    a. world1 = "sting" + " me " + "now";   //valid
    b. num trouble = 12%0;  //error
    c. num trouble2 = 12/1; //valid

11. 
    a. weakest precondition is {b > 3/2}
    b. weakest precondition is {x < -1}
    c. weakest precondition is {x < 0} and {y < -1}
    d. weakest precondition is {a > 3} and {b > -2/3}