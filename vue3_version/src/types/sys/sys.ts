export interface AdminRole {
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
  id?: number;
  name: string;
  nameZh: string;
}

export interface Authorities {
  authority: string;
}

export interface SelectedRoles{
  id:number
}
