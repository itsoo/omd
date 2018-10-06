### 用户
#namespace("user")
  #include("user.sql")
#end

### 角色
#namespace("role")
  #include("role.sql")
#end

### 初始化缓存
#namespace("init")

  ### 全部权限
  #sql("authc")
    select * from t_authc
  #end

  ### 用户权限
  #sql("list")
    select 
        ra.authc_id,
        ur.user_id,
        a.url
      from t_role_authc ra
    inner join t_user_role ur
    inner join t_authc a
      on ur.role_id = ra.role_id
      and ra.authc_id = a.id
  #end
#end
