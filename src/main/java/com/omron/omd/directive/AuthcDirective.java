package com.omron.omd.directive;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import com.omron.omd.common.AppConst;
import com.omron.omd.util.AuthcUtil;

import javax.servlet.http.HttpSession;

/**
 * 权限控制指令
 *
 * @author zxy
 */
public class AuthcDirective extends Directive {

    @Override
    public void setExprList(ExprList exprList) {
        super.setExprList(exprList);
    }

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        // 获取 session
        HttpSession session = (HttpSession) scope.getData().get("session");
        // 获取 userId
        String userId = ((Record) session.getAttribute(AppConst.SESSION)).getStr("id");
        // 获取参数
        for (Expr actionKey : exprList.getExprArray()) {
            if (AuthcUtil.hasAuthc(userId, actionKey.toString())) {
                stat.exec(env, scope, writer);
                return;
            }
        }
    }

    @Override
    public boolean hasEnd() {
        return true;
    }
}
