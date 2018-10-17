### 用户列表
#sql("list")
  select * from t_user
  #if(notBlank(nickname))
    where nickname like '%#(nickname)%'
  #end
#end

### 查询用户角色
#sql("findUserRole")
  select role_id from t_user_role
  where user_id = #para(0)
#end

### 登录查询
#sql("logon")
  select id, username, nickname, state from t_user
  where username = '#(username)'
  and password = '#(password)'
#end

### 查询用户
#sql("findUserList")
  select id, username, password from t_user
  where id in (
    #for(id : idArray)
      #(for.index == 0 ? "" : ",") #(id)
    #end
  )
#end
