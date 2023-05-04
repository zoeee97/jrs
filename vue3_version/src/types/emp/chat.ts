/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-02-23 20:52:29
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-23 20:52:41
 * @Description: file content
 */
export interface Admin {
  id: number;
  name: string;
  phone: string;
  telephone: string;
  address: string;
  enabled: boolean;
  username: string;
  password?: any;
  userFace: string;
  remark?: any;
  roles: Roles[];
  accountNonExpired: boolean;
  accountNonLocked: boolean;
  credentialsNonExpired: boolean;
  authorities: Authorities[];
}

export interface Roles {
  id: number;
  name: string;
  nameZh: string;
}

export interface Authorities {
  authority: string;
}

export interface MsgObj{
  to:string,
  content:string
  notSelf:boolean
}