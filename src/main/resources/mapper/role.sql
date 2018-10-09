### 角色列表
#sql("list")
  select * from t_role
  #if(!isBlank(name))
    where name like '%#(name)%'
  #end
#end

### 查询角色权限
#sql("findRoleAuthc")
  select a.*, case
    when ra.authc_id is not null then 'true'
    else 'false' end as checked
  from t_authc a
    left join (
      select authc_id from t_role_authc where
      #if(id != null)
        role_id = #(id)
      #else
        1 <> 1
      #end
    ) ra on a.id = ra.authc_id
#end

### 全部权限
#sql("allAuthc")
  select * from t_authc
#end

### 用户权限
#sql("allRoleAuthc")
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
