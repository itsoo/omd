### 角色列表
#sql("list")
  select * from t_role
  #if(!isBlank(name))
    where name like '%#(name)%'
  #end
#end

### 查询角色权限
#sql("findRoleAuth")
  select a.*, case
    when ra.authc_id is not null then 'true'
    else 'false' end as checked
  from t_authc a
    left join (
      select authc_id from t_role_authc where
      #if(!isBlank(id))
        role_id = #(id)
      #else
        1 <> 1
      #end
    ) ra on a.id = ra.authc_id
#end
